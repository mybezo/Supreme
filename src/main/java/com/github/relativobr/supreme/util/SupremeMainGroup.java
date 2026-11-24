package com.github.relativobr.supreme.util;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.GuideHistory;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.guide.SurvivalSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The main Supreme group in the Slimefun guide. It lists the sub groups like Slimefun's
 * {@link NestedItemGroup} and can show one extra button at a fixed slot (the "Compressed Items" menu).
 */
public class SupremeMainGroup extends NestedItemGroup {

  // sub groups are listed in the slots 9 to 44
  private static final int FIRST_SLOT = 9;
  private static final int LAST_SLOT = 44;

  private final List<SubItemGroup> subGroups = new ArrayList<>();
  private final Map<SubItemGroup, ItemGroup> views = new HashMap<>();
  private int fixedSlot = -1;
  private ItemGroup fixedGroup;

  @ParametersAreNonnullByDefault
  public SupremeMainGroup(NamespacedKey key, ItemStack item) {
    super(key, item);
  }

  /**
   * Show a button at a fixed slot that opens the given group.
   */
  public void setFixedGroup(int slot, @Nullable ItemGroup group) {
    this.fixedSlot = slot;
    this.fixedGroup = group;
  }

  /**
   * Open {@code view} instead of {@code group} when the group is clicked in this menu
   * (a different layout of the same items).
   */
  public void setView(@Nonnull SubItemGroup group, @Nonnull ItemGroup view) {
    views.put(group, view);
  }

  @Override
  public void addSubGroup(@Nonnull SubItemGroup group) {
    super.addSubGroup(group);
    if (!subGroups.contains(group)) {
      subGroups.add(group);
    }
  }

  @Override
  public void removeSubGroup(@Nonnull SubItemGroup group) {
    super.removeSubGroup(group);
    subGroups.remove(group);
  }

  @Override
  public void open(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
    openGuide(p, profile, mode, 1);
  }

  private boolean isFixedSlot(int slot) {
    return fixedGroup != null && slot == fixedSlot;
  }

  private int groupsPerPage() {
    int slots = LAST_SLOT - FIRST_SLOT + 1;
    return fixedGroup != null && fixedSlot >= FIRST_SLOT && fixedSlot <= LAST_SLOT ? slots - 1 : slots;
  }

  @ParametersAreNonnullByDefault
  private void openGuide(Player p, PlayerProfile profile, SlimefunGuideMode mode, int page) {
    GuideHistory history = profile.getGuideHistory();
    if (mode == SlimefunGuideMode.SURVIVAL_MODE) {
      history.add(this, page);
    }

    ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.main"));
    SurvivalSlimefunGuide guide = (SurvivalSlimefunGuide) Slimefun.getRegistry().getSlimefunGuide(mode);

    menu.setEmptySlotsClickable(false);
    menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
    guide.createHeader(p, profile, menu);

    menu.addItem(1, new CustomItemStack(ChestMenuUtils.getBackButton(p, "",
        ChatColor.GRAY + Slimefun.getLocalization().getMessage(p, "guide.back.guide"))));
    menu.addMenuClickHandler(1, (pl, s, is, action) -> {
      if (mode == SlimefunGuideMode.SURVIVAL_MODE) {
        history.goBack(guide);
      } else {
        SlimefunGuide.openMainMenu(profile, mode, history.getMainMenuPage());
      }
      return false;
    });

    List<SubItemGroup> visible = new ArrayList<>();
    for (SubItemGroup group : subGroups) {
      if (group.isVisibleInNested(p)) {
        visible.add(group);
      }
    }

    int perPage = groupsPerPage();
    int pages = Math.max(1, (visible.size() - 1) / perPage + 1);
    int index = (page - 1) * perPage;
    for (int slot = FIRST_SLOT; slot <= LAST_SLOT && index < visible.size(); slot++) {
      if (isFixedSlot(slot)) {
        continue;
      }
      SubItemGroup group = visible.get(index++);
      ItemGroup target = views.getOrDefault(group, group);
      menu.addItem(slot, group.getItem(p));
      menu.addMenuClickHandler(slot, (pl, s, item, action) -> {
        SlimefunGuide.openItemGroup(profile, target, mode, 1);
        return false;
      });
    }

    if (fixedGroup != null) {
      ItemGroup group = fixedGroup;
      menu.addItem(fixedSlot, group.getItem(p));
      menu.addMenuClickHandler(fixedSlot, (pl, s, item, action) -> {
        SlimefunGuide.openItemGroup(profile, group, mode, 1);
        return false;
      });
    }

    menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
    menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
      if (page > 1) {
        openGuide(p, profile, mode, page - 1);
      }
      return false;
    });

    menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
    menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
      if (page < pages) {
        openGuide(p, profile, mode, page + 1);
      }
      return false;
    });

    menu.open(p);
  }
}
