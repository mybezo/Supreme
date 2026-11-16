package com.github.relativobr.supreme.compressed;

import com.github.relativobr.supreme.machine.VirtualAquarium;
import com.github.relativobr.supreme.machine.recipe.VirtualAquariumMachineRecipe;
import com.github.relativobr.supreme.util.UtilMachine;
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
import org.bukkit.inventory.ItemStack;

/**
 * Compressed {@link VirtualAquarium}: all the combined aquariums run in one operation (limited by the free output
 * space), the fishing tool is only used once per operation like in the original machine.
 * Every aquarium rolls its own catch.
 */
public class CompressedVirtualAquarium extends VirtualAquarium implements Compressible, CompressedProcessor.Host {

  private final int multiplier;
  private final CompressedProcessor processor;

  @ParametersAreNonnullByDefault
  public CompressedVirtualAquarium(ItemGroup category, SlimefunItemStack item, RecipeType recipeType,
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
    boolean outputFull = false;
    for (int slot : getInputSlots()) {
      ItemStack tool = inv.getItemInSlot(slot);
      if (tool == null) {
        continue;
      }
      for (VirtualAquariumMachineRecipe recipe : getVirtualAquariumRecipes()) {
        ItemStack input = recipe.getInput()[0];
        if (input == null || tool.getType() != input.getType()) {
          continue;
        }
        ItemStack[] catches = recipe.getOutput();
        int units = maxUnitsForAnyCatch(inv, catches, CompressedUtil.toolUnits(tool, multiplier));
        if (units <= 0) {
          outputFull = true;
          continue;
        }
        CompressedUtil.useTool(inv, slot, tool, units);
        ItemStack display = catches.length > 0 && catches[0] != null ? catches[0] : new ItemStack(Material.COD);
        return new CompressedProcessor.Operation(recipe.getTicks(), units, display, () -> roll(catches, units));
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
   * The catch is random, the units are limited so that even the same catch for every unit fits.
   */
  private int maxUnitsForAnyCatch(@Nonnull BlockMenu inv, @Nonnull ItemStack[] catches, int maxUnits) {
    int units = maxUnits;
    for (ItemStack possible : catches) {
      if (possible == null || possible.getType() == Material.AIR) {
        continue;
      }
      ItemStack single = possible.clone();
      single.setAmount(1);
      units = Math.min(units, CompressedUtil.fittingUnits(inv, getOutputSlots(), List.of(single), units));
    }
    return units;
  }

  /**
   * Roll the catch of every unit, like the original machine does once.
   */
  @Nonnull
  private static List<ItemStack> roll(@Nonnull ItemStack[] catches, int units) {
    List<ItemStack> results = new ArrayList<>();
    for (int i = 0; i < units; i++) {
      ItemStack material = UtilMachine.getMaterial(catches, UtilMachine.getRandomInt());
      if (material != null) {
        CompressedUtil.merge(results, material, 1);
      }
    }
    return results;
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
