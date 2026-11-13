package com.github.relativobr.supreme.gear;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;

/**
 * Spears were added in Minecraft 1.21.11. Kept separate from {@link WeaponsSpear} so the check can run
 * without loading the spear items on older servers.
 */
@UtilityClass
public class SpearSupport {

  private static final boolean SUPPORTED = Material.matchMaterial("DIAMOND_SPEAR") != null
      && Material.matchMaterial("NETHERITE_SPEAR") != null;

  public static boolean isSupported() {
    return SUPPORTED;
  }
}
