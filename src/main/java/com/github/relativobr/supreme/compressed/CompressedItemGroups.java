package com.github.relativobr.supreme.compressed;

import com.github.relativobr.supreme.Supreme;
import com.github.relativobr.supreme.util.SupremeMainGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import javax.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

/**
 * The guide groups of the compressed items.
 */
@UtilityClass
public class CompressedItemGroups {

  /**
   * The slot of the "Compressed Items" button in the main Supreme menu.
   */
  public static final int BUTTON_SLOT = 40;

  // head texture of the "Compressed Items" button (Mybezo's skin)
  private static final String COMPRESSED_ITEMS_TEXTURE =
      "82600941fab88efc41bb132d873d3b5c5f35f7720ebd9375476c1a18955bb03e";

  // same icons as the original Machines and Electric groups
  private static final String ELECTRIC_TEXTURE = "77400ea19dbd84f75c39ad6823ac4ef786f39f48fc6f84602366ac29b837422";

  /**
   * The "Compressed Items" menu, opened from the main Supreme menu only.
   */
  public static final SupremeMainGroup COMPRESSED_ITEMS = new SupremeMainGroup(
      new NamespacedKey(Supreme.inst(), "SUPREME_COMPRESSED_ITEMS"),
      new CustomItemStack(SlimefunUtils.getCustomHead(COMPRESSED_ITEMS_TEXTURE),
          Supreme.getLocalization().getCategoryName("compressed_items"))) {

    @Override
    public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
      // not listed in the main Slimefun guide
      return false;
    }
  };

  public static final SubItemGroup COMPRESSED_MACHINES = new SubItemGroup(
      new NamespacedKey(Supreme.inst(), "SUPREME_COMPRESSED_MACHINES"), COMPRESSED_ITEMS,
      new CustomItemStack(Material.OBSERVER, Supreme.getLocalization().getCategoryName("compressed_machines")));

  public static final SubItemGroup COMPRESSED_GENERATORS = new SubItemGroup(
      new NamespacedKey(Supreme.inst(), "SUPREME_COMPRESSED_GENERATORS"), COMPRESSED_ITEMS,
      new CustomItemStack(SlimefunUtils.getCustomHead(ELECTRIC_TEXTURE),
          Supreme.getLocalization().getCategoryName("compressed_generators")));

  /**
   * The lower multipliers: craftable and viewable from the recipes, but not listed in the guide.
   */
  public static final ItemGroup HIDDEN = new ItemGroup(
      new NamespacedKey(Supreme.inst(), "SUPREME_COMPRESSED_HIDDEN"),
      new CustomItemStack(Material.BARRIER, "Compressed (hidden)")) {

    @Override
    public boolean isVisible(@Nonnull Player p) {
      return false;
    }
  };
}
