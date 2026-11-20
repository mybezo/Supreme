package com.github.relativobr.supreme.setup;

import static com.github.relativobr.supreme.util.CompatibilySupremeLegacy.getNewIdSupremeLegacy;

import com.github.relativobr.supreme.Supreme;
import com.github.relativobr.supreme.compressed.Compressible;
import com.github.relativobr.supreme.generic.machine.GenericMachine;
import com.github.relativobr.supreme.machine.AbstractQuarry;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import java.io.File;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import lombok.experimental.UtilityClass;

/**
 * Applies the {@code machine-time} section of config.yml: the process time of every machine (each tier),
 * in seconds with the default Slimefun ticker (every 10 game ticks).
 *
 * <p>The missing machines are added with their current time (from the {@code base-time-*} options and the
 * built-in values), the existing values are kept. Compressed machines use the time of their original machine.</p>
 */
@UtilityClass
public class SetupMachineTime {

  private static final String SECTION = "machine-time";

  public static void setup(@Nonnull Supreme plugin) {
    File file = new File(plugin.getDataFolder(), "config.yml");
    // read the file itself: the defaults of the jar must not hide the missing machines
    Config cfg = new Config(file);

    int changed = 0;
    for (SlimefunItem item : Slimefun.getRegistry().getAllSlimefunItems()) {
      if (item.getAddon() != plugin || item instanceof Compressible) {
        continue;
      }
      String path = SECTION + "." + getNewIdSupremeLegacy(item.getId());

      if (item instanceof GenericMachine machine) {
        int speed = Math.max(1, machine.getSpeed());
        cfg.setDefaultValue(path, toConfigValue(machine.getTimeProcess() / (double) speed));
        double seconds = readSeconds(plugin, cfg, path);
        if (seconds > 0) {
          // progress per Slimefun tick is the speed and a recipe of N seconds has 2N ticks
          machine.setTimeProcess((int) Math.max(1, Math.round(seconds * speed)));
          changed++;
        }
      } else if (item instanceof AbstractQuarry quarry) {
        cfg.setDefaultValue(path, toConfigValue(quarry.getProductionDelay() / 2.0));
        double seconds = readSeconds(plugin, cfg, path);
        if (seconds > 0) {
          // one Slimefun tick is half a second
          quarry.setProductionDelay((int) Math.round(seconds * 2));
          changed++;
        }
      }
    }
    cfg.save();
    plugin.log(Level.INFO, "Machine time loaded for " + changed + " machines.");
  }

  private static double readSeconds(@Nonnull Supreme plugin, @Nonnull Config cfg, @Nonnull String path) {
    double seconds = cfg.getDouble(path);
    if (seconds <= 0) {
      plugin.log(Level.WARNING, "Invalid " + path + ": " + cfg.getValue(path) + ", it must be above 0. Using the default.");
      return -1;
    }
    return seconds;
  }

  /**
   * Whole numbers are written without decimals, others with up to 2 decimals.
   */
  @Nonnull
  private static Object toConfigValue(double seconds) {
    if (seconds == Math.rint(seconds)) {
      return (int) seconds;
    }
    return Math.round(seconds * 100) / 100.0;
  }
}
