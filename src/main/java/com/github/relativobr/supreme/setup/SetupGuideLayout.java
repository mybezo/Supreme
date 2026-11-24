package com.github.relativobr.supreme.setup;

import static com.github.relativobr.supreme.util.CompatibilySupremeLegacy.getOldIdSupremeLegacy;

import com.github.relativobr.supreme.Supreme;
import com.github.relativobr.supreme.util.ItemGroups;
import com.github.relativobr.supreme.util.RowItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

/**
 * Shows the gear groups (weapons, tools, armor) in the guide with one row per kind of item:
 * Titanium, Aurum, Adamantium, then Thornium to Supreme.
 */
@UtilityClass
public class SetupGuideLayout {

  private static final String[] BASIC_TIERS = {"TITANIUM", "AURUM", "ADAMANTIUM"};
  private static final String[] THORNIUM_TIERS = {"THORNIUM", "MAGIC", "RARE", "EPIC", "LEGENDARY", "SUPREME"};

  public static void setup(@Nonnull Supreme plugin) {
    view(plugin, (SubItemGroup) ItemGroups.WEAPONS_CATEGORY, "SUPREME_WEAPONS_ROWS", Material.NETHERITE_SWORD,
        "weapons", "SWORD", "SPEAR", "MACE", "TRIDENT", "BOW", "SHIELD");
    view(plugin, (SubItemGroup) ItemGroups.TOOLS_CATEGORY, "SUPREME_TOOLS_ROWS", Material.NETHERITE_PICKAXE,
        "tools", "PICKAXE", "AXE", "SHOVEL", "FISHING_ROD");
    view(plugin, (SubItemGroup) ItemGroups.ARMOR_CATEGORY, "SUPREME_ARMOR_ROWS", Material.NETHERITE_CHESTPLATE,
        "armor", "HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS");
  }

  private static void view(@Nonnull Supreme plugin, @Nonnull SubItemGroup group, @Nonnull String key,
      @Nonnull Material icon, @Nonnull String nameKey, @Nonnull String... kinds) {
    List<List<String>> rows = new ArrayList<>();
    for (String kind : kinds) {
      rows.add(row(kind));
    }
    RowItemGroup view = new RowItemGroup(new NamespacedKey(plugin, key),
        new CustomItemStack(icon, Supreme.getLocalization().getCategoryName(nameKey)), group, rows);
    ItemGroups.MULTI_CATEGORY.setView(group, view);
  }

  /**
   * The IDs of one kind of item in tier order, e.g. SUPREME_TITANIUM_SWORD ... SUPREME_SWORD_SUPREME.
   */
  @Nonnull
  private static List<String> row(@Nonnull String kind) {
    List<String> ids = new ArrayList<>();
    for (String tier : BASIC_TIERS) {
      ids.add(getOldIdSupremeLegacy("SUPREME_" + tier + "_" + kind));
    }
    for (String tier : THORNIUM_TIERS) {
      ids.add(getOldIdSupremeLegacy("SUPREME_" + kind + "_" + tier));
    }
    return ids;
  }
}
