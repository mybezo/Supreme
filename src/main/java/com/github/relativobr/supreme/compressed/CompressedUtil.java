package com.github.relativobr.supreme.compressed;

import com.github.relativobr.supreme.Supreme;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Shared helpers of the compressed items.
 */
@UtilityClass
public class CompressedUtil {

  /**
   * @return {@code value * multiplier}, or -1 when the result does not fit in an int
   */
  public static int scale(int value, int multiplier) {
    long result = (long) value * multiplier;
    return result > Integer.MAX_VALUE ? -1 : (int) result;
  }

  /**
   * The item shown on the guide page of a compressed item.
   */
  @Nonnull
  public static ItemStack infoItem(int multiplier, @Nonnull String loreKey) {
    return new CustomItemStack(Material.KNOWLEDGE_BOOK,
        Supreme.getLocalization().getString("compressed.multiplier", multiplier),
        Supreme.getLocalization().getString(loreKey, multiplier));
  }

  /**
   * An empty slot in a guide recipe list.
   */
  @Nonnull
  public static ItemStack filler() {
    return new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ");
  }

  /**
   * Multiply the amount of every item. The amounts may exceed the max stack size,
   * {@link #fits} and {@link #push} split them into valid stacks.
   */
  @Nonnull
  public static List<ItemStack> times(@Nonnull List<ItemStack> perUnit, int units) {
    List<ItemStack> result = new ArrayList<>(perUnit.size());
    for (ItemStack item : perUnit) {
      ItemStack copy = item.clone();
      copy.setAmount(item.getAmount() * units);
      result.add(copy);
    }
    return result;
  }

  /**
   * Add {@code item} to {@code items}, merging it with a similar item already in the list.
   */
  public static void merge(@Nonnull List<ItemStack> items, @Nonnull ItemStack item, int amount) {
    for (ItemStack existing : items) {
      if (ItemUtils.canStack(existing, item)) {
        existing.setAmount(existing.getAmount() + amount);
        return;
      }
    }
    ItemStack copy = item.clone();
    copy.setAmount(amount);
    items.add(copy);
  }

  /**
   * Whether all the items fit in the slots, splitting them into stacks of their max stack size.
   */
  public static boolean fits(@Nonnull BlockMenu inv, @Nonnull int[] slots, @Nonnull List<ItemStack> items) {
    ItemStack[] contents = new ItemStack[slots.length];
    int[] amounts = new int[slots.length];
    for (int i = 0; i < slots.length; i++) {
      ItemStack item = inv.getItemInSlot(slots[i]);
      if (item != null && item.getType() != Material.AIR) {
        contents[i] = item;
        amounts[i] = item.getAmount();
      }
    }

    for (ItemStack item : items) {
      int remaining = item.getAmount();
      int maxStackSize = item.getMaxStackSize();
      // fill the similar stacks first, then the empty slots
      for (int i = 0; i < slots.length && remaining > 0; i++) {
        if (contents[i] != null && ItemUtils.canStack(contents[i], item)) {
          int space = Math.max(0, maxStackSize - amounts[i]);
          int added = Math.min(space, remaining);
          amounts[i] += added;
          remaining -= added;
        }
      }
      for (int i = 0; i < slots.length && remaining > 0; i++) {
        if (contents[i] == null) {
          int added = Math.min(maxStackSize, remaining);
          contents[i] = item;
          amounts[i] = added;
          remaining -= added;
        }
      }
      if (remaining > 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * The highest amount of units (1 to {@code maxUnits}) whose output fits in the slots, 0 if none fits.
   */
  public static int fittingUnits(@Nonnull BlockMenu inv, @Nonnull int[] slots, @Nonnull List<ItemStack> perUnit,
      int maxUnits) {
    if (maxUnits <= 0 || !fits(inv, slots, times(perUnit, 1))) {
      return 0;
    }
    int low = 1;
    int high = maxUnits;
    while (low < high) {
      int mid = (low + high + 1) / 2;
      if (fits(inv, slots, times(perUnit, mid))) {
        low = mid;
      } else {
        high = mid - 1;
      }
    }
    return low;
  }

  /**
   * Push the items into the slots, split into stacks of their max stack size.
   * Check {@link #fits} first, items that do not fit are not pushed.
   */
  public static void push(@Nonnull BlockMenu inv, @Nonnull int[] slots, @Nonnull List<ItemStack> items) {
    for (ItemStack item : items) {
      int remaining = item.getAmount();
      int maxStackSize = item.getMaxStackSize();
      while (remaining > 0) {
        int amount = Math.min(maxStackSize, remaining);
        ItemStack stack = item.clone();
        stack.setAmount(amount);
        inv.pushItem(stack, slots);
        remaining -= amount;
      }
    }
  }

  /**
   * How many units a tool-like input supports. A tool with durability is used once per operation
   * (like in the original machine), however many units run, so it allows all the units. An item without
   * durability is consumed like in the original machine, one per unit.
   */
  public static int toolUnits(@Nullable ItemStack item, int maxUnits) {
    if (item == null) {
      return 0;
    }
    ItemMeta meta = item.getItemMeta();
    if (meta == null || meta.isUnbreakable() || item.getType().getMaxDurability() > 0) {
      return maxUnits;
    }
    return Math.min(maxUnits, item.getAmount());
  }

  /**
   * Use a tool-like input for one operation of the given units, see {@link #toolUnits}:
   * a tool with durability loses 2 durability once (it breaks like in the original machine),
   * an item without durability is consumed once per unit, an unbreakable item is not used up.
   */
  public static void useTool(@Nonnull BlockMenu inv, int slot, @Nonnull ItemStack item, int units) {
    ItemMeta meta = item.getItemMeta();
    if (meta == null || meta.isUnbreakable()) {
      return;
    }
    int maxDurability = item.getType().getMaxDurability();
    if (maxDurability <= 0) {
      inv.consumeItem(slot, units);
      return;
    }
    if (meta instanceof Damageable damageable) {
      int damage = damageable.getDamage() + 2;
      if (damage >= maxDurability) {
        inv.consumeItem(slot);
      } else {
        damageable.setDamage(damage);
        item.setItemMeta(meta);
        inv.replaceExistingItem(slot, item);
      }
    }
  }
}
