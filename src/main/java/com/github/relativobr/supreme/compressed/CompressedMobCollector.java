package com.github.relativobr.supreme.compressed;

import com.github.relativobr.supreme.machine.MobCollector;
import com.github.relativobr.supreme.machine.recipe.MobCollectorMachineRecipe;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * Compressed {@link MobCollector}. Glass bottles (and other items without durability) are consumed for every
 * collector that runs, so they limit how many of the combined collectors run. A tool with durability loses the
 * durability of all the collectors at once, so it lasts multiplier times fewer operations (unless unbreakable). The mobs are only required nearby,
 * they are not consumed.
 */
public class CompressedMobCollector extends MobCollector implements Compressible, CompressedProcessor.Host {

  private final int multiplier;
  private final CompressedProcessor processor;

  @ParametersAreNonnullByDefault
  public CompressedMobCollector(ItemGroup category, SlimefunItemStack item, RecipeType recipeType,
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
    // the nearby mobs are scanned once per operation start
    List<LivingEntity> nearbyMobs = null;
    boolean outputFull = false;

    for (int slot : getInputSlots()) {
      ItemStack item = inv.getItemInSlot(slot);
      if (item == null) {
        continue;
      }
      for (MobCollectorMachineRecipe recipe : getMobCollectorRecipes()) {
        ItemStack input = recipe.getInput()[0];
        if (input == null || item.getType() != input.getType()) {
          continue;
        }
        if (nearbyMobs == null) {
          nearbyMobs = getNearbyMobs(b);
        }
        if (nearbyMobs.stream().noneMatch(recipe::test)) {
          continue;
        }

        boolean bottle = item.getType() == Material.GLASS_BOTTLE;
        // the original machine consumes "speed" bottles per operation, the last operation may use fewer
        int maxUnits = bottle
            ? Math.min(multiplier, (item.getAmount() + getSpeed() - 1) / getSpeed())
            : CompressedUtil.toolUnits(item, multiplier);
        List<ItemStack> perUnit = outputs(recipe);
        int units = CompressedUtil.fittingUnits(inv, getOutputSlots(), perUnit, maxUnits);
        if (units <= 0) {
          outputFull = true;
          continue;
        }

        if (bottle) {
          inv.consumeItem(slot, Math.min(item.getAmount(), units * getSpeed()));
        } else {
          CompressedUtil.useTool(inv, slot, item, units);
        }
        return new CompressedProcessor.Operation(recipe.getTicks(), units, perUnit.get(0),
            () -> CompressedUtil.times(perUnit, units));
      }
    }
    if (outputFull) {
      showOutputFull(inv);
    } else {
      showReset(inv);
    }
    return null;
  }

  /**
   * The output of one collector: one of each recipe output, like the original machine.
   */
  @Nonnull
  private static List<ItemStack> outputs(@Nonnull MobCollectorMachineRecipe recipe) {
    List<ItemStack> outputs = new ArrayList<>();
    for (ItemStack output : recipe.getOutput()) {
      if (output != null && output.getType() != Material.AIR) {
        CompressedUtil.merge(outputs, output, 1);
      }
    }
    return outputs;
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
