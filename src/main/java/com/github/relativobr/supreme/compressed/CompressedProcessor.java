package com.github.relativobr.supreme.compressed;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Runs the operations of a compressed machine.
 *
 * <p>An operation runs {@code units} of the combined machines at once (1 up to the multiplier): it takes the same
 * time as the original machine, uses {@code units} times its energy per tick and produces {@code units} times its
 * output. The machine decides the units when the operation starts, from the input it finds.
 * The results are kept until they fit in the output slots, so nothing is lost.</p>
 */
public final class CompressedProcessor {

  /**
   * The machine side of the processor (the energy methods come from {@link EnergyNetComponent}).
   */
  public interface Host extends EnergyNetComponent {

    /**
     * Start a new operation: find the input, decide the units and consume the input.
     *
     * @return the operation, or null if none can start (the host updates the status itself)
     */
    @Nullable
    Operation startOperation(@Nonnull Block b, @Nonnull BlockMenu inv);

    int[] getOutputSlots();

    int getStatusSlot();

    int getSpeed();

    void showNoEnergy(@Nonnull BlockMenu inv, @Nonnull ItemStack display);

    void showOutputFull(@Nonnull BlockMenu inv);

    void showReset(@Nonnull BlockMenu inv);
  }

  /**
   * A running operation.
   */
  public static final class Operation {

    private final int ticks;
    private final int units;
    private final ItemStack display;
    private final Supplier<List<ItemStack>> results;
    private int ticksLeft;
    private List<ItemStack> finishedResults;

    /**
     * @param ticks the ticks of one operation of the original machine
     * @param units the amount of combined machines running
     * @param display the item shown in the progress bar
     * @param results the results of all units, computed when the operation finishes
     */
    public Operation(int ticks, int units, @Nonnull ItemStack display, @Nonnull Supplier<List<ItemStack>> results) {
      this.ticks = Math.max(ticks, 1);
      this.units = units;
      this.display = display;
      this.results = results;
      this.ticksLeft = this.ticks;
    }
  }

  private final Host host;
  private final int energyPerUnit;
  private final Map<Block, Operation> operations = new HashMap<>();

  /**
   * @param energyPerUnit the energy per tick of one original machine
   */
  public CompressedProcessor(@Nonnull Host host, int energyPerUnit) {
    this.host = host;
    this.energyPerUnit = energyPerUnit;
  }

  public void tick(@Nonnull Block b, @Nonnull BlockMenu inv) {
    Operation operation = operations.get(b);
    if (operation == null) {
      operation = host.startOperation(b, inv);
      if (operation != null) {
        operations.put(b, operation);
      }
      return;
    }

    if (operation.finishedResults == null) {
      // the units are at most the multiplier, energyPerUnit * multiplier is checked to fit in an int
      int energy = energyPerUnit * operation.units;
      Location l = b.getLocation();
      if (host.getCharge(l) < energy) {
        host.showNoEnergy(inv, operation.display);
        return;
      }
      host.removeCharge(l, energy);

      operation.ticksLeft -= host.getSpeed();
      if (operation.ticksLeft > 0) {
        ChestMenuUtils.updateProgressbar(inv, host.getStatusSlot(), operation.ticksLeft, operation.ticks,
            operation.display);
        return;
      }
      operation.finishedResults = operation.results.get();
    }

    if (!CompressedUtil.fits(inv, host.getOutputSlots(), operation.finishedResults)) {
      host.showOutputFull(inv);
      return;
    }
    CompressedUtil.push(inv, host.getOutputSlots(), operation.finishedResults);
    operations.remove(b);
    host.showReset(inv);
  }

  /**
   * Forget the operation of a removed machine.
   */
  public void remove(@Nonnull Block b) {
    operations.remove(b);
  }
}
