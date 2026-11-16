package com.github.relativobr.supreme.generic.electric;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import org.bukkit.inventory.ItemStack;

public class EnergyCapacitor extends Capacitor {

  public EnergyCapacitor(ItemGroup categories, SlimefunItemStack item, ItemStack[] recipe,
      int capacity) {
    this(categories, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, capacity);
  }

  public EnergyCapacitor(ItemGroup categories, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
      int capacity) {
    super(categories, capacity, item, recipeType, recipe);
  }

}
