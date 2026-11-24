package com.github.relativobr.supreme.gear;

import com.github.relativobr.supreme.resource.SupremeComponents;
import com.github.relativobr.supreme.util.SupremeItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public final class WeaponsBasic {

  public static final SlimefunItemStack ADAMANTIUM_SWORD = new SupremeItemStack("SUPREME_ADAMANTIUM_SWORD",
      Material.DIAMOND_SWORD);
  public static final ItemStack[] RECIPE_ADAMANTIUM_SWORD = {null, SupremeComponents.ADAMANTIUM_PLATE, null, null,
      SupremeComponents.ADAMANTIUM_PLATE, null, null, new ItemStack(Material.STICK), null};

  public static final SlimefunItemStack AURUM_SWORD = new SupremeItemStack("SUPREME_AURUM_SWORD",
      Material.DIAMOND_SWORD);
  public static final ItemStack[] RECIPE_AURUM_SWORD = {null, SupremeComponents.AURUM_PLATE, null, null,
      SupremeComponents.AURUM_PLATE, null, null, new ItemStack(Material.STICK), null};

  public static final SlimefunItemStack TITANIUM_SWORD = new SupremeItemStack("SUPREME_TITANIUM_SWORD",
      Material.DIAMOND_SWORD);
  public static final ItemStack[] RECIPE_TITANIUM_SWORD = {null, SupremeComponents.TITANIUM_PLATE, null, null,
      SupremeComponents.TITANIUM_PLATE, null, null, new ItemStack(Material.STICK), null};

  public static final SlimefunItemStack TITANIUM_MACE = new SupremeItemStack("SUPREME_TITANIUM_MACE", Material.MACE);
  public static final ItemStack[] RECIPE_TITANIUM_MACE = {SupremeComponents.TITANIUM_PLATE, SupremeComponents.TITANIUM_PLATE,
      SupremeComponents.TITANIUM_PLATE, SupremeComponents.TITANIUM_PLATE, new ItemStack(Material.HEAVY_CORE),
      SupremeComponents.TITANIUM_PLATE, null, new ItemStack(Material.BREEZE_ROD), null};

  public static final SlimefunItemStack TITANIUM_TRIDENT = new SupremeItemStack("SUPREME_TITANIUM_TRIDENT",
      Material.TRIDENT);
  public static final ItemStack[] RECIPE_TITANIUM_TRIDENT = {SupremeComponents.TITANIUM_PLATE, SupremeComponents.TITANIUM_PLATE,
      SupremeComponents.TITANIUM_PLATE, null, new ItemStack(Material.PRISMARINE_CRYSTALS), null, null,
      new ItemStack(Material.PRISMARINE_SHARD), null};

  public static final SlimefunItemStack AURUM_MACE = new SupremeItemStack("SUPREME_AURUM_MACE", Material.MACE);
  public static final ItemStack[] RECIPE_AURUM_MACE = {SupremeComponents.AURUM_PLATE, SupremeComponents.AURUM_PLATE,
      SupremeComponents.AURUM_PLATE, SupremeComponents.AURUM_PLATE, new ItemStack(Material.HEAVY_CORE),
      SupremeComponents.AURUM_PLATE, null, new ItemStack(Material.BREEZE_ROD), null};

  public static final SlimefunItemStack AURUM_TRIDENT = new SupremeItemStack("SUPREME_AURUM_TRIDENT",
      Material.TRIDENT);
  public static final ItemStack[] RECIPE_AURUM_TRIDENT = {SupremeComponents.AURUM_PLATE, SupremeComponents.AURUM_PLATE,
      SupremeComponents.AURUM_PLATE, null, new ItemStack(Material.PRISMARINE_CRYSTALS), null, null,
      new ItemStack(Material.PRISMARINE_SHARD), null};

  public static final SlimefunItemStack ADAMANTIUM_MACE = new SupremeItemStack("SUPREME_ADAMANTIUM_MACE", Material.MACE);
  public static final ItemStack[] RECIPE_ADAMANTIUM_MACE = {SupremeComponents.ADAMANTIUM_PLATE, SupremeComponents.ADAMANTIUM_PLATE,
      SupremeComponents.ADAMANTIUM_PLATE, SupremeComponents.ADAMANTIUM_PLATE, new ItemStack(Material.HEAVY_CORE),
      SupremeComponents.ADAMANTIUM_PLATE, null, new ItemStack(Material.BREEZE_ROD), null};

  public static final SlimefunItemStack ADAMANTIUM_TRIDENT = new SupremeItemStack("SUPREME_ADAMANTIUM_TRIDENT",
      Material.TRIDENT);
  public static final ItemStack[] RECIPE_ADAMANTIUM_TRIDENT = {SupremeComponents.ADAMANTIUM_PLATE, SupremeComponents.ADAMANTIUM_PLATE,
      SupremeComponents.ADAMANTIUM_PLATE, null, new ItemStack(Material.PRISMARINE_CRYSTALS), null, null,
      new ItemStack(Material.PRISMARINE_SHARD), null};
}
