package com.github.relativobr.supreme.gear;

import com.github.relativobr.supreme.Supreme;
import com.github.relativobr.supreme.machine.multiblock.MultiBlockGearFabricator;
import com.github.relativobr.supreme.util.EnchantsAndEffectsUtil;
import com.github.relativobr.supreme.util.ItemGroups;
import com.github.relativobr.supreme.util.ItemTier;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.bukkit.inventory.ItemStack;

public class AbstractWeapons extends SlimefunItem implements Soulbound, NotPlaceable {

  @ParametersAreNonnullByDefault
  public AbstractWeapons(@Nonnull SlimefunItemStack stack, ItemStack[] recipe) {
    super(ItemGroups.WEAPONS_CATEGORY, stack, MultiBlockGearFabricator.getMachine(), recipe);
  }

  public static void setup(Supreme sup) {

    // add Enchant
    EnchantsAndEffectsUtil.addGearBasicEnchants(WeaponsBasic.TITANIUM_SWORD, WeaponsBasic.AURUM_SWORD, WeaponsBasic.ADAMANTIUM_SWORD);

    // add Enchant
    EnchantsAndEffectsUtil.addGearThorniumEnchants(WeaponsThornium.THORNIUM_BOW, WeaponsThornium.THORNIUM_BOW_MAGIC,
        WeaponsThornium.THORNIUM_BOW_RARE, WeaponsThornium.THORNIUM_BOW_EPIC, WeaponsThornium.THORNIUM_BOW_LEGENDARY,
        WeaponsThornium.THORNIUM_BOW_SUPREME, WeaponsThornium.THORNIUM_SWORD, WeaponsThornium.THORNIUM_SWORD_MAGIC,
        WeaponsThornium.THORNIUM_SWORD_RARE, WeaponsThornium.THORNIUM_SWORD_EPIC,
        WeaponsThornium.THORNIUM_SWORD_LEGENDARY, WeaponsThornium.THORNIUM_SWORD_SUPREME,
        WeaponsThornium.THORNIUM_SHIELD, WeaponsThornium.THORNIUM_SHIELD_MAGIC, WeaponsThornium.THORNIUM_SHIELD_RARE,
        WeaponsThornium.THORNIUM_SHIELD_EPIC, WeaponsThornium.THORNIUM_SHIELD_LEGENDARY,
        WeaponsThornium.THORNIUM_SHIELD_SUPREME);

    final AbstractWeapons bow = new AbstractWeapons(WeaponsThornium.THORNIUM_BOW, WeaponsThornium.RECIPE_THORNIUM_BOW);
    bow.register(sup);
    final AbstractWeapons bowMagic = new AbstractWeapons(WeaponsThornium.THORNIUM_BOW_MAGIC,
        ItemTier.getMagicRecipe(WeaponsThornium.THORNIUM_BOW));
    bowMagic.register(sup);
    final AbstractWeapons bowRare = new AbstractWeapons(WeaponsThornium.THORNIUM_BOW_RARE,
        ItemTier.getRareRecipe(WeaponsThornium.THORNIUM_BOW_MAGIC));
    bowRare.register(sup);
    final AbstractWeapons bowEpic = new AbstractWeapons(WeaponsThornium.THORNIUM_BOW_EPIC,
        ItemTier.getEpicRecipe(WeaponsThornium.THORNIUM_BOW_RARE));
    bowEpic.register(sup);
    final AbstractWeapons bowLegendary = new AbstractWeapons(WeaponsThornium.THORNIUM_BOW_LEGENDARY,
        ItemTier.getLegendaryRecipe(WeaponsThornium.THORNIUM_BOW_EPIC));
    bowLegendary.register(sup);
    final AbstractWeapons bowSupreme = new AbstractWeapons(WeaponsThornium.THORNIUM_BOW_SUPREME,
        ItemTier.getSupremeRecipe(WeaponsThornium.THORNIUM_BOW_LEGENDARY));
    bowSupreme.register(sup);

    final AbstractWeapons titaniumSword = new AbstractWeapons(WeaponsBasic.TITANIUM_SWORD,
        WeaponsBasic.RECIPE_TITANIUM_SWORD);
    titaniumSword.register(sup);
    final AbstractWeapons aurumSword = new AbstractWeapons(WeaponsBasic.AURUM_SWORD, WeaponsBasic.RECIPE_AURUM_SWORD);
    aurumSword.register(sup);
    final AbstractWeapons adamantiumSword = new AbstractWeapons(WeaponsBasic.ADAMANTIUM_SWORD,
        WeaponsBasic.RECIPE_ADAMANTIUM_SWORD);
    adamantiumSword.register(sup);

    final AbstractWeapons sword = new AbstractWeapons(WeaponsThornium.THORNIUM_SWORD,
        WeaponsThornium.RECIPE_THORNIUM_SWORD);
    sword.register(sup);
    final AbstractWeapons swordMagic = new AbstractWeapons(WeaponsThornium.THORNIUM_SWORD_MAGIC,
        ItemTier.getMagicRecipe(WeaponsThornium.THORNIUM_SWORD));
    swordMagic.register(sup);
    final AbstractWeapons swordRare = new AbstractWeapons(WeaponsThornium.THORNIUM_SWORD_RARE,
        ItemTier.getRareRecipe(WeaponsThornium.THORNIUM_SWORD_MAGIC));
    swordRare.register(sup);
    final AbstractWeapons swordEpic = new AbstractWeapons(WeaponsThornium.THORNIUM_SWORD_EPIC,
        ItemTier.getEpicRecipe(WeaponsThornium.THORNIUM_SWORD_RARE));
    swordEpic.register(sup);
    final AbstractWeapons swordLegendary = new AbstractWeapons(WeaponsThornium.THORNIUM_SWORD_LEGENDARY,
        ItemTier.getLegendaryRecipe(WeaponsThornium.THORNIUM_SWORD_EPIC));
    swordLegendary.register(sup);
    final AbstractWeapons swordSupreme = new AbstractWeapons(WeaponsThornium.THORNIUM_SWORD_SUPREME,
        ItemTier.getSupremeRecipe(WeaponsThornium.THORNIUM_SWORD_LEGENDARY));
    swordSupreme.register(sup);

    final AbstractWeapons shield = new AbstractWeapons(WeaponsThornium.THORNIUM_SHIELD,
        WeaponsThornium.RECIPE_THORNIUM_SHIELD);
    shield.register(sup);
    final AbstractWeapons shieldMagic = new AbstractWeapons(WeaponsThornium.THORNIUM_SHIELD_MAGIC,
        ItemTier.getMagicRecipe(WeaponsThornium.THORNIUM_SHIELD));
    shieldMagic.register(sup);
    final AbstractWeapons shieldRare = new AbstractWeapons(WeaponsThornium.THORNIUM_SHIELD_RARE,
        ItemTier.getRareRecipe(WeaponsThornium.THORNIUM_SHIELD_MAGIC));
    shieldRare.register(sup);
    final AbstractWeapons shieldEpic = new AbstractWeapons(WeaponsThornium.THORNIUM_SHIELD_EPIC,
        ItemTier.getEpicRecipe(WeaponsThornium.THORNIUM_SHIELD_RARE));
    shieldEpic.register(sup);
    final AbstractWeapons shieldLegendary = new AbstractWeapons(WeaponsThornium.THORNIUM_SHIELD_LEGENDARY,
        ItemTier.getLegendaryRecipe(WeaponsThornium.THORNIUM_SHIELD_EPIC));
    shieldLegendary.register(sup);
    final AbstractWeapons shieldSupreme = new AbstractWeapons(WeaponsThornium.THORNIUM_SHIELD_SUPREME,
        ItemTier.getSupremeRecipe(WeaponsThornium.THORNIUM_SHIELD_LEGENDARY));
    shieldSupreme.register(sup);

    // mace and trident sets
    EnchantsAndEffectsUtil.addGearBasicEnchants(WeaponsBasic.TITANIUM_MACE, WeaponsBasic.AURUM_MACE,
        WeaponsBasic.ADAMANTIUM_MACE);
    EnchantsAndEffectsUtil.addGearThorniumEnchants(WeaponsThornium.THORNIUM_MACE, WeaponsThornium.THORNIUM_MACE_MAGIC, WeaponsThornium.THORNIUM_MACE_RARE, WeaponsThornium.THORNIUM_MACE_EPIC, WeaponsThornium.THORNIUM_MACE_LEGENDARY, WeaponsThornium.THORNIUM_MACE_SUPREME);
    new AbstractWeapons(WeaponsBasic.TITANIUM_MACE, WeaponsBasic.RECIPE_TITANIUM_MACE).register(sup);
    new AbstractWeapons(WeaponsBasic.AURUM_MACE, WeaponsBasic.RECIPE_AURUM_MACE).register(sup);
    new AbstractWeapons(WeaponsBasic.ADAMANTIUM_MACE, WeaponsBasic.RECIPE_ADAMANTIUM_MACE).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_MACE, WeaponsThornium.RECIPE_THORNIUM_MACE).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_MACE_MAGIC, ItemTier.getMagicRecipe(WeaponsThornium.THORNIUM_MACE)).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_MACE_RARE, ItemTier.getRareRecipe(WeaponsThornium.THORNIUM_MACE_MAGIC)).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_MACE_EPIC, ItemTier.getEpicRecipe(WeaponsThornium.THORNIUM_MACE_RARE)).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_MACE_LEGENDARY, ItemTier.getLegendaryRecipe(WeaponsThornium.THORNIUM_MACE_EPIC)).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_MACE_SUPREME, ItemTier.getSupremeRecipe(WeaponsThornium.THORNIUM_MACE_LEGENDARY)).register(sup);
    EnchantsAndEffectsUtil.addGearBasicEnchants(WeaponsBasic.TITANIUM_TRIDENT, WeaponsBasic.AURUM_TRIDENT,
        WeaponsBasic.ADAMANTIUM_TRIDENT);
    EnchantsAndEffectsUtil.addGearThorniumEnchants(WeaponsThornium.THORNIUM_TRIDENT, WeaponsThornium.THORNIUM_TRIDENT_MAGIC, WeaponsThornium.THORNIUM_TRIDENT_RARE, WeaponsThornium.THORNIUM_TRIDENT_EPIC, WeaponsThornium.THORNIUM_TRIDENT_LEGENDARY, WeaponsThornium.THORNIUM_TRIDENT_SUPREME);
    new AbstractWeapons(WeaponsBasic.TITANIUM_TRIDENT, WeaponsBasic.RECIPE_TITANIUM_TRIDENT).register(sup);
    new AbstractWeapons(WeaponsBasic.AURUM_TRIDENT, WeaponsBasic.RECIPE_AURUM_TRIDENT).register(sup);
    new AbstractWeapons(WeaponsBasic.ADAMANTIUM_TRIDENT, WeaponsBasic.RECIPE_ADAMANTIUM_TRIDENT).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_TRIDENT, WeaponsThornium.RECIPE_THORNIUM_TRIDENT).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_TRIDENT_MAGIC, ItemTier.getMagicRecipe(WeaponsThornium.THORNIUM_TRIDENT)).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_TRIDENT_RARE, ItemTier.getRareRecipe(WeaponsThornium.THORNIUM_TRIDENT_MAGIC)).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_TRIDENT_EPIC, ItemTier.getEpicRecipe(WeaponsThornium.THORNIUM_TRIDENT_RARE)).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_TRIDENT_LEGENDARY, ItemTier.getLegendaryRecipe(WeaponsThornium.THORNIUM_TRIDENT_EPIC)).register(sup);
    new AbstractWeapons(WeaponsThornium.THORNIUM_TRIDENT_SUPREME, ItemTier.getSupremeRecipe(WeaponsThornium.THORNIUM_TRIDENT_LEGENDARY)).register(sup);

    // spear set (Minecraft 1.21.11+)
    if (SpearSupport.isSupported()) {
      setupSpears(sup);
    } else {
      sup.log(Level.INFO, "Spears need Minecraft 1.21.11+, Supreme spear set disabled");
    }
  }

  private static void setupSpears(Supreme sup) {
    EnchantsAndEffectsUtil.addGearBasicEnchants(WeaponsSpear.TITANIUM_SPEAR, WeaponsSpear.AURUM_SPEAR,
        WeaponsSpear.ADAMANTIUM_SPEAR);
    EnchantsAndEffectsUtil.addGearThorniumEnchants(WeaponsSpear.THORNIUM_SPEAR, WeaponsSpear.THORNIUM_SPEAR_MAGIC,
        WeaponsSpear.THORNIUM_SPEAR_RARE, WeaponsSpear.THORNIUM_SPEAR_EPIC, WeaponsSpear.THORNIUM_SPEAR_LEGENDARY,
        WeaponsSpear.THORNIUM_SPEAR_SUPREME);

    new AbstractWeapons(WeaponsSpear.TITANIUM_SPEAR, WeaponsSpear.RECIPE_TITANIUM_SPEAR).register(sup);
    new AbstractWeapons(WeaponsSpear.AURUM_SPEAR, WeaponsSpear.RECIPE_AURUM_SPEAR).register(sup);
    new AbstractWeapons(WeaponsSpear.ADAMANTIUM_SPEAR, WeaponsSpear.RECIPE_ADAMANTIUM_SPEAR).register(sup);
    new AbstractWeapons(WeaponsSpear.THORNIUM_SPEAR, WeaponsSpear.RECIPE_THORNIUM_SPEAR).register(sup);
    new AbstractWeapons(WeaponsSpear.THORNIUM_SPEAR_MAGIC, ItemTier.getMagicRecipe(WeaponsSpear.THORNIUM_SPEAR)).register(sup);
    new AbstractWeapons(WeaponsSpear.THORNIUM_SPEAR_RARE, ItemTier.getRareRecipe(WeaponsSpear.THORNIUM_SPEAR_MAGIC)).register(sup);
    new AbstractWeapons(WeaponsSpear.THORNIUM_SPEAR_EPIC, ItemTier.getEpicRecipe(WeaponsSpear.THORNIUM_SPEAR_RARE)).register(sup);
    new AbstractWeapons(WeaponsSpear.THORNIUM_SPEAR_LEGENDARY, ItemTier.getLegendaryRecipe(WeaponsSpear.THORNIUM_SPEAR_EPIC)).register(sup);
    new AbstractWeapons(WeaponsSpear.THORNIUM_SPEAR_SUPREME, ItemTier.getSupremeRecipe(WeaponsSpear.THORNIUM_SPEAR_LEGENDARY)).register(sup);
  }
}
