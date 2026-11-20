package com.github.relativobr.supreme.compressed;

/**
 * A compressed machine, generator or capacitor: several of the original item combined into one.
 */
public interface Compressible {

  /**
   * @return how many of the original item are combined into this one (8, 16, 32 or 64)
   */
  int getMultiplier();
}
