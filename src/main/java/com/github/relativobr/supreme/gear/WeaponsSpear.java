package com.github.relativobr.supreme.gear;

import com.github.relativobr.supreme.resource.SupremeComponents;
import com.github.relativobr.supreme.resource.magical.SupremeCetrus;
import com.github.relativobr.supreme.util.SupremeItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Spear set. Spears only exist since Minecraft 1.21.11: only touch this class after
 * {@link SpearSupport#isSupported()} returned true, its static fields need the spear materials.
 */
@UtilityClass
public class WeaponsSpear {

  private static final Material DIAMOND_SPEAR = Material.matchMaterial("DIAMOND_SPEAR");
  private static final Material NETHERITE_SPEAR = Material.matchMaterial("NETHERITE_SPEAR");

  // basic

  public static final SlimefunItemStack TITANIUM_SPEAR = new SupremeItemStack("SUPREME_TITANIUM_SPEAR",
      DIAMOND_SPEAR);
  public static final ItemStack[] RECIPE_TITANIUM_SPEAR = {null, null, SupremeComponents.TITANIUM_PLATE, null,
      new ItemStack(Material.STICK), null, new ItemStack(Material.STICK), null, null};

  public static final SlimefunItemStack AURUM_SPEAR = new SupremeItemStack("SUPREME_AURUM_SPEAR", DIAMOND_SPEAR);
  public static final ItemStack[] RECIPE_AURUM_SPEAR = {null, null, SupremeComponents.AURUM_PLATE, null,
      new ItemStack(Material.STICK), null, new ItemStack(Material.STICK), null, null};

  public static final SlimefunItemStack ADAMANTIUM_SPEAR = new SupremeItemStack("SUPREME_ADAMANTIUM_SPEAR",
      DIAMOND_SPEAR);
  public static final ItemStack[] RECIPE_ADAMANTIUM_SPEAR = {null, null, SupremeComponents.ADAMANTIUM_PLATE, null,
      new ItemStack(Material.STICK), null, new ItemStack(Material.STICK), null, null};

  // thornium

  public static final SlimefunItemStack THORNIUM_SPEAR = new SupremeItemStack("SUPREME_SPEAR_THORNIUM",
      NETHERITE_SPEAR);
  public static final ItemStack[] RECIPE_THORNIUM_SPEAR = {null, SupremeComponents.THORNIUM_INGOT,
      SupremeComponents.THORNIUM_INGOT, null, SupremeCetrus.CETRUS_VENTUS, SupremeComponents.THORNIUM_INGOT,
      SlimefunItems.STAFF_ELEMENTAL, null, null};

  public static final SlimefunItemStack THORNIUM_SPEAR_MAGIC = new SupremeItemStack("SUPREME_SPEAR_MAGIC",
      NETHERITE_SPEAR);

  public static final SlimefunItemStack THORNIUM_SPEAR_RARE = new SupremeItemStack("SUPREME_SPEAR_RARE",
      NETHERITE_SPEAR);

  public static final SlimefunItemStack THORNIUM_SPEAR_EPIC = new SupremeItemStack("SUPREME_SPEAR_EPIC",
      NETHERITE_SPEAR);

  public static final SlimefunItemStack THORNIUM_SPEAR_LEGENDARY = new SupremeItemStack("SUPREME_SPEAR_LEGENDARY",
      NETHERITE_SPEAR);

  public static final SlimefunItemStack THORNIUM_SPEAR_SUPREME = new SupremeItemStack("SUPREME_SPEAR_SUPREME",
      NETHERITE_SPEAR);
}
