package com.github.relativobr.supreme.compressed;

import com.github.relativobr.supreme.machine.VirtualGarden;
import com.github.relativobr.supreme.machine.recipe.VirtualGardenMachineRecipe;
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
 * Compressed {@link VirtualGarden}: the seed is not consumed, the amount of seeds in the stack
 * decides how many of the combined gardens run (up to the multiplier).
 */
public class CompressedVirtualGarden extends VirtualGarden implements Compressible, CompressedProcessor.Host {

  private final int multiplier;
  private final CompressedProcessor processor;

  @ParametersAreNonnullByDefault
  public CompressedVirtualGarden(ItemGroup category, SlimefunItemStack item, RecipeType recipeType,
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
      ItemStack seed = inv.getItemInSlot(slot);
      if (seed == null) {
        continue;
      }
      for (VirtualGardenMachineRecipe recipe : getVirtualGardenRecipes()) {
        ItemStack input = recipe.getInput()[0];
        if (input == null || seed.getType() != input.getType()) {
          continue;
        }
        List<ItemStack> perUnit = outputs(recipe);
        int units = CompressedUtil.fittingUnits(inv, getOutputSlots(), perUnit,
            Math.min(seed.getAmount(), multiplier));
        if (units <= 0) {
          outputFull = true;
          continue;
        }
        return new CompressedProcessor.Operation(getTimeProcess() * 2, units, perUnit.get(0),
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
   * The output of one garden: one of each recipe output, like the original machine.
   */
  @Nonnull
  private static List<ItemStack> outputs(@Nonnull VirtualGardenMachineRecipe recipe) {
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
