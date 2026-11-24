package com.github.relativobr.supreme.util;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.core.guide.GuideHistory;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.guide.SurvivalSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A guide view of an item group that shows every kind of item on its own row of 9 slots
 * (for example one row of swords, one row of spears). Full rows come first, the shorter rows
 * start on the next page, each still on its own row. Items that are in no row are listed after them.
 *
 * <p>The items stay in their original group (used by the search and the item pages), this group only
 * changes how the group is shown when it is opened from the Supreme menu.</p>
 */
public class RowItemGroup extends FlexItemGroup {

  private static final int ROW_SIZE = 9;
  private static final int ROWS_PER_PAGE = 4;
  private static final int FIRST_SLOT = 9;

  private final ItemGroup source;
  private final List<List<String>> rows;

  /**
   * @param source the group that holds the items
   * @param rows the item IDs of every row, in display order
   */
  @ParametersAreNonnullByDefault
  public RowItemGroup(NamespacedKey key, ItemStack item, ItemGroup source, List<List<String>> rows) {
    super(key, item);
    this.source = source;
    this.rows = rows;
  }

  @Override
  public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
    // only opened from the Supreme menu, not listed in the main Slimefun guide
    return false;
  }

  @Override
  public void open(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
    openPage(p, profile, mode, 1);
  }

  /**
   * The pages, each a list of up to 4 rows: the full rows first, then the shorter rows on new pages.
   */
  @Nonnull
  private List<List<List<SlimefunItem>>> buildPages(@Nonnull Player p) {
    List<List<SlimefunItem>> fullRows = new ArrayList<>();
    List<List<SlimefunItem>> shortRows = new ArrayList<>();
    Set<SlimefunItem> placed = new HashSet<>();

    for (List<String> row : rows) {
      List<SlimefunItem> items = new ArrayList<>();
      for (String id : row) {
        SlimefunItem item = SlimefunItem.getById(id);
        if (item != null && item.getItemGroup() == source && placed.add(item) && !item.isDisabledIn(p.getWorld())) {
          items.add(item);
        }
      }
      addRows(items, fullRows, shortRows);
    }

    // items of the group that are in no row (new or renamed items) are still shown
    List<SlimefunItem> others = new ArrayList<>();
    for (SlimefunItem item : source.getItems()) {
      if (!placed.contains(item) && !item.isDisabledIn(p.getWorld())) {
        others.add(item);
      }
    }
    addRows(others, fullRows, shortRows);

    List<List<List<SlimefunItem>>> pages = new ArrayList<>();
    addPages(fullRows, pages);
    addPages(shortRows, pages);
    if (pages.isEmpty()) {
      pages.add(new ArrayList<>());
    }
    return pages;
  }

  private static void addRows(@Nonnull List<SlimefunItem> items, @Nonnull List<List<SlimefunItem>> fullRows,
      @Nonnull List<List<SlimefunItem>> shortRows) {
    for (int i = 0; i < items.size(); i += ROW_SIZE) {
      List<SlimefunItem> row = new ArrayList<>(items.subList(i, Math.min(items.size(), i + ROW_SIZE)));
      (row.size() == ROW_SIZE ? fullRows : shortRows).add(row);
    }
  }

  private static void addPages(@Nonnull List<List<SlimefunItem>> rows, @Nonnull List<List<List<SlimefunItem>>> pages) {
    for (int i = 0; i < rows.size(); i += ROWS_PER_PAGE) {
      pages.add(new ArrayList<>(rows.subList(i, Math.min(rows.size(), i + ROWS_PER_PAGE))));
    }
  }

  @ParametersAreNonnullByDefault
  private void openPage(Player p, PlayerProfile profile, SlimefunGuideMode mode, int requestedPage) {
    List<List<List<SlimefunItem>>> pages = buildPages(p);
    int pageCount = pages.size();
    int page = Math.max(1, Math.min(requestedPage, pageCount));

    GuideHistory history = profile.getGuideHistory();
    if (mode == SlimefunGuideMode.SURVIVAL_MODE) {
      history.add(this, page);
    }

    SurvivalSlimefunGuide guide = (SurvivalSlimefunGuide) Slimefun.getRegistry().getSlimefunGuide(mode);
    ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.main"));
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

    List<List<SlimefunItem>> pageRows = pages.get(page - 1);
    for (int r = 0; r < pageRows.size(); r++) {
      List<SlimefunItem> row = pageRows.get(r);
      for (int c = 0; c < row.size(); c++) {
        displayItem(menu, guide, p, profile, row.get(c), page, FIRST_SLOT + r * ROW_SIZE + c);
      }
    }

    menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pageCount));
    menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
      if (page > 1) {
        openPage(p, profile, mode, page - 1);
      }
      return false;
    });
    menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pageCount));
    menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
      if (page < pageCount) {
        openPage(p, profile, mode, page + 1);
      }
      return false;
    });

    menu.open(p);
  }

  /**
   * Show one item like the Slimefun guide does: no permission, locked research, or the item itself.
   */
  @ParametersAreNonnullByDefault
  private void displayItem(ChestMenu menu, SurvivalSlimefunGuide guide, Player p, PlayerProfile profile,
      SlimefunItem item, int page, int slot) {
    boolean survival = guide.getMode() == SlimefunGuideMode.SURVIVAL_MODE;
    Research research = item.getResearch();

    if (survival && !Slimefun.getPermissionsService().hasPermission(p, item)) {
      List<String> message = Slimefun.getPermissionsService().getLore(item);
      menu.addItem(slot, new CustomItemStack(ChestMenuUtils.getNoPermissionItem(), item.getItemName(),
          message.toArray(new String[0])));
      menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
    } else if (survival && research != null && !profile.hasUnlocked(research)) {
      menu.addItem(slot, new CustomItemStack(ChestMenuUtils.getNotResearchedItem(),
          ChatColor.WHITE + ItemUtils.getItemName(item.getItem()),
          "&4&l" + Slimefun.getLocalization().getMessage(p, "guide.locked"), "", "&a> Click to unlock", "",
          "&7Cost: &b" + research.getCost() + " Level(s)"));
      menu.addMenuClickHandler(slot, (pl, s, is, action) -> {
        research.unlockFromGuide(guide, p, profile, item, this, page);
        return false;
      });
    } else {
      menu.addItem(slot, item.getItem());
      menu.addMenuClickHandler(slot, (pl, s, is, action) -> {
        try {
          if (survival) {
            guide.displayItem(profile, item, true);
          } else if (pl.hasPermission("slimefun.cheat.items")) {
            if (item instanceof MultiBlockMachine) {
              Slimefun.getLocalization().sendMessage(pl, "guide.cheat.no-multiblocks");
            } else {
              ItemStack clone = item.getItem().clone();
              if (action.isShiftClicked()) {
                clone.setAmount(clone.getMaxStackSize());
              }
              pl.getInventory().addItem(clone);
            }
          } else {
            Slimefun.getLocalization().sendMessage(pl, "messages.no-permission", true);
          }
        } catch (Exception | LinkageError x) {
          item.getAddon().getLogger().log(Level.SEVERE, x, () -> "Could not open the guide page of " + item.getId());
        }
        return false;
      });
    }
  }
}
