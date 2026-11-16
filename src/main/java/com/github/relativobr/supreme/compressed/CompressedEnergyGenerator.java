package com.github.relativobr.supreme.compressed;

import com.github.relativobr.supreme.generic.electric.EnergyGenerator;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.bukkit.inventory.ItemStack;

/**
 * Compressed {@link EnergyGenerator}: the energy per tick and the buffer are multiplied,
 * the placement requirement is the same as the original generator.
 */
public class CompressedEnergyGenerator extends EnergyGenerator implements Compressible, RecipeDisplayItem {

  private final int multiplier;

  @ParametersAreNonnullByDefault
  public CompressedEnergyGenerator(ItemGroup category, SlimefunItemStack item, RecipeType recipeType,
      ItemStack[] recipe, int multiplier) {
    super(category, item, recipeType, recipe);
    this.multiplier = multiplier;
  }

  @Override
  public int getMultiplier() {
    return multiplier;
  }

  @Nonnull
  @Override
  public List<ItemStack> getDisplayRecipes() {
    return List.of(CompressedUtil.infoItem(multiplier, "compressed.lore-generator"));
  }
}
