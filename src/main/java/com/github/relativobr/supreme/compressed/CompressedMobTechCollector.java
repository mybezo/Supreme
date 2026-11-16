package com.github.relativobr.supreme.compressed;

import com.github.relativobr.supreme.machine.recipe.MobTechCollectorMachineRecipe;
import com.github.relativobr.supreme.machine.tech.MobTechCollector;
import com.github.relativobr.supreme.resource.SupremeComponents;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * Compressed {@link MobTechCollector}. Every collector that runs captures one nearby mob with one Empty MobTech
 * and produces one MobTech. The Empty MobTechs (from any input slot) and the nearby mobs decide how many of the
 * combined collectors run (up to the multiplier).
 */
public class CompressedMobTechCollector extends MobTechCollector implements Compressible, CompressedProcessor.Host {

  private final int multiplier;
  private final CompressedProcessor processor;

  @ParametersAreNonnullByDefault
  public CompressedMobTechCollector(ItemGroup category, SlimefunItemStack item, RecipeType recipeType,
      ItemStack[] recipe, int multiplier, int energyPerUnit) {
    super(category, item, recipeType, recipe);
    this.multiplier = multiplier;
    this.processor = new CompressedProcessor(this, energyPerUnit);
  }

  @Override
  public int getMultiplier() {
    return multiplier;
  }

  @Override
  protected void tick(Block b) {
    BlockMenu inv = BlockStorage.getInventory(b);
    if (inv != null) {
      processor.tick(b, inv);
    }
  }

  @Nullable
  @Override
  public CompressedProcessor.Operation startOperation(@Nonnull Block b, @Nonnull BlockMenu inv) {
    int emptyTechs = countEmptyTechs(inv);
    if (emptyTechs <= 0) {
      showReset(inv);
      return null;
    }

    int range = getMobRange();
    List<LivingEntity> nearbyMobs = new ArrayList<>();
    for (Entity entity : b.getWorld().getNearbyEntities(b.getLocation(), range, range, range,
        LivingEntity.class::isInstance)) {
      nearbyMobs.add((LivingEntity) entity);
    }

    boolean outputFull = false;
    for (MobTechCollectorMachineRecipe recipe : getMobTechRecipes()) {
      List<LivingEntity> matching = new ArrayList<>();
      for (LivingEntity mob : nearbyMobs) {
        if (recipe.test(mob)) {
          matching.add(mob);
        }
      }
      if (matching.isEmpty()) {
        continue;
      }

      ItemStack output = recipe.getOutput()[0].clone();
      output.setAmount(1);
      List<ItemStack> perUnit = List.of(output);
      int maxUnits = Math.min(multiplier, Math.min(emptyTechs, matching.size()));
      int units = CompressedUtil.fittingUnits(inv, getOutputSlots(), perUnit, maxUnits);
      if (units <= 0) {
        outputFull = true;
        continue;
      }

      for (int i = 0; i < units; i++) {
        matching.get(i).remove();
      }
      consumeEmptyTechs(inv, units);
      return new CompressedProcessor.Operation(recipe.getTicks(), units, output,
          () -> CompressedUtil.times(perUnit, units));
    }
    if (outputFull) {
      showOutputFull(inv);
    } else {
      showReset(inv);
    }
    return null;
  }

  private int countEmptyTechs(@Nonnull BlockMenu inv) {
    int count = 0;
    for (int slot : getInputSlots()) {
      ItemStack item = inv.getItemInSlot(slot);
      if (item != null && SlimefunUtils.isItemSimilar(item, SupremeComponents.EMPTY_MOBTECH, false, false)) {
        count += item.getAmount();
      }
    }
    return count;
  }

  private void consumeEmptyTechs(@Nonnull BlockMenu inv, int amount) {
    int remaining = amount;
    for (int slot : getInputSlots()) {
      if (remaining <= 0) {
        return;
      }
      ItemStack item = inv.getItemInSlot(slot);
      if (item != null && SlimefunUtils.isItemSimilar(item, SupremeComponents.EMPTY_MOBTECH, false, false)) {
        int consumed = Math.min(item.getAmount(), remaining);
        inv.consumeItem(slot, consumed);
        remaining -= consumed;
      }
    }
  }

  @Override
  public void showNoEnergy(@Nonnull BlockMenu inv, @Nonnull ItemStack display) {
    updateStatusConnectEnergy(inv, display);
  }

  @Override
  public void showOutputFull(@Nonnull BlockMenu inv) {
    updateStatusOutputFull(inv);
  }

  @Override
  public void showReset(@Nonnull BlockMenu inv) {
    updateStatusReset(inv);
  }

  @Nonnull
  @Override
  public List<ItemStack> getDisplayRecipes() {
    List<ItemStack> display = new ArrayList<>();
    display.add(CompressedUtil.infoItem(multiplier, "compressed.lore-machine"));
    display.add(CompressedUtil.filler());
    display.addAll(super.getDisplayRecipes());
    return display;
  }

  @Override
  protected void onMachineRemoved(@Nonnull Block b) {
    super.onMachineRemoved(b);
    processor.remove(b);
  }
}
