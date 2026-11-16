package com.github.relativobr.supreme.compressed;

import com.github.relativobr.supreme.generators.GeneratorMob;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.bukkit.inventory.ItemStack;

/**
 * Compressed {@link GeneratorMob}: the energy per tick and the buffer are multiplied,
 * it still needs animals nearby.
 */
public class CompressedGeneratorMob extends GeneratorMob implements Compressible {

  private final int multiplier;

  @ParametersAreNonnullByDefault
  public CompressedGeneratorMob(ItemGroup category, SlimefunItemStack item, RecipeType recipeType,
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
