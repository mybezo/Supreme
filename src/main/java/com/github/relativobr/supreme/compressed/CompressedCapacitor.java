package com.github.relativobr.supreme.compressed;

import com.github.relativobr.supreme.generic.electric.EnergyCapacitor;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.bukkit.inventory.ItemStack;

/**
 * Compressed {@link EnergyCapacitor}: the capacity is multiplied.
 */
public class CompressedCapacitor extends EnergyCapacitor implements Compressible, RecipeDisplayItem {

  private final int multiplier;

  @ParametersAreNonnullByDefault
  public CompressedCapacitor(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
      int capacity, int multiplier) {
    super(category, item, recipeType, recipe, capacity);
    this.multiplier = multiplier;
  }

  @Override
  public int getMultiplier() {
    return multiplier;
  }

  @Nonnull
  @Override
  public List<ItemStack> getDisplayRecipes() {
    return List.of(CompressedUtil.infoItem(multiplier, "compressed.lore-capacitor"));
  }
}
