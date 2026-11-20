package com.github.relativobr.supreme.compressed;

import static com.github.relativobr.supreme.util.CompatibilySupremeLegacy.getNewIdSupremeLegacy;

import com.github.relativobr.supreme.Supreme;
import com.github.relativobr.supreme.generators.GeneratorMob;
import com.github.relativobr.supreme.generators.SupremeCapacitor;
import com.github.relativobr.supreme.generators.SupremeGenerator;
import com.github.relativobr.supreme.generic.electric.EnergyCapacitor;
import com.github.relativobr.supreme.generic.electric.EnergyGenerator;
import com.github.relativobr.supreme.generic.machine.GenericMachine;
import com.github.relativobr.supreme.machine.MobCollector;
import com.github.relativobr.supreme.machine.VirtualAquarium;
import com.github.relativobr.supreme.machine.VirtualGarden;
import com.github.relativobr.supreme.machine.tech.MobTechCollector;
import com.github.relativobr.supreme.util.ItemGroups;
import com.github.relativobr.supreme.util.UtilEnergy;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import javax.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Registers the compressed machines, generators and capacitors (multiple of the original item combined into one),
 * configured by {@code compressed.yml}.
 *
 * <p>Every item gets one compressed version per multiplier (x8, x16, x32, x64). They are crafted in the Compressor:
 * x8 from 8 original items, every next multiplier from 2 of the previous one. Only the highest multiplier is listed
 * in the guide, the lower ones are in a hidden group (still craftable and viewable from the recipes).</p>
 */
@UtilityClass
public class CompressedSetup {

  public static final int[] MULTIPLIERS = {8, 16, 32, 64};

  private static final String CONFIG_FILE = "compressed.yml";

  /**
   * The kind of compressed item: decides the config section, the guide group and the lore.
   */
  private enum Category {
    MACHINES("machines", "compressed.lore-machine"),
    GENERATORS("generators", "compressed.lore-generator"),
    CAPACITORS("capacitors", "compressed.lore-capacitor");

    private final String configSection;
    private final String loreKey;

    Category(String configSection, String loreKey) {
      this.configSection = configSection;
      this.loreKey = loreKey;
    }

    @Nonnull
    ItemGroup itemGroup() {
      return this == MACHINES ? CompressedItemGroups.COMPRESSED_MACHINES : CompressedItemGroups.COMPRESSED_GENERATORS;
    }
  }

  /**
   * What a factory needs to create the compressed item of one multiplier.
   */
  private record Args(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, int multiplier) {

    /**
     * @return {@code value * multiplier}, or -1 when it does not fit in an int
     */
    int scale(int value) {
      return CompressedUtil.scale(value, multiplier);
    }
  }

  /**
   * Creates the compressed item from the registered original item.
   */
  @FunctionalInterface
  private interface Factory {

    @Nonnull
    SlimefunItem create(@Nonnull Args args, @Nonnull SlimefunItem original);
  }

  /**
   * The values of the original item that are multiplied (energy, buffer, capacity),
   * a multiplier is only available when all of them still fit in an int.
   */
  @FunctionalInterface
  private interface ScaledValues {

    @Nonnull
    int[] of(@Nonnull SlimefunItem original);
  }

  private record Entry(SlimefunItemStack original, Category category, ScaledValues values, Factory factory) {

    /**
     * The ID used in the config and for the compressed items (independent of the legacy ID option).
     */
    @Nonnull
    String id() {
      return getNewIdSupremeLegacy(original.getItemId());
    }
  }

  public static void setup(@Nonnull Supreme plugin) {
    List<Entry> entries = createEntries();
    Config cfg = loadConfig(plugin, entries);

    if (!cfg.getBoolean("enabled")) {
      return;
    }

    // a multiplier needs all the lower ones, its recipe uses the previous multiplier
    List<Integer> multipliers = new ArrayList<>();
    for (int multiplier : MULTIPLIERS) {
      if (!cfg.getBoolean("multipliers.x" + multiplier)) {
        break;
      }
      multipliers.add(multiplier);
    }
    if (multipliers.isEmpty()) {
      return;
    }

    int count = 0;
    for (Entry entry : entries) {
      if (cfg.getBoolean(entry.category().configSection + "." + entry.id())) {
        count += register(plugin, entry, multipliers);
      }
    }

    if (count > 0) {
      ItemGroups.MULTI_CATEGORY.setFixedGroup(CompressedItemGroups.BUTTON_SLOT, CompressedItemGroups.COMPRESSED_ITEMS);
    }
    plugin.log(Level.INFO, "Registered " + count + " compressed items.");
  }

  /**
   * Register all the multipliers of one entry.
   *
   * @return the amount of registered items
   */
  private static int register(@Nonnull Supreme plugin, @Nonnull Entry entry, @Nonnull List<Integer> multipliers) {
    SlimefunItem original = SlimefunItem.getById(entry.original().getItemId());
    if (original == null || original.isDisabled()) {
      return 0;
    }

    // the multipliers whose multiplied values still fit in an int
    int[] values = entry.values().of(original);
    List<Integer> available = new ArrayList<>();
    for (int multiplier : multipliers) {
      if (!fits(values, multiplier)) {
        plugin.log(Level.INFO, "Compressed " + entry.id() + " is limited to "
            + (available.isEmpty() ? "none" : "x" + available.get(available.size() - 1)) + " (value limit).");
        break;
      }
      available.add(multiplier);
    }

    ItemStack previous = original.getItem();
    int previousMultiplier = 1;
    for (int multiplier : available) {
      SlimefunItemStack item = createItemStack(entry, original, values, multiplier);
      ItemStack input = previous.clone();
      input.setAmount(multiplier / previousMultiplier);
      ItemStack[] recipe = new ItemStack[9];
      recipe[0] = input;

      // only the highest multiplier is listed in the guide
      boolean highest = multiplier == available.get(available.size() - 1);
      ItemGroup group = highest ? entry.category().itemGroup() : CompressedItemGroups.HIDDEN;
      entry.factory().create(new Args(group, item, recipe, multiplier), original).register(plugin);

      previous = item;
      previousMultiplier = multiplier;
    }
    return available.size();
  }

  private static boolean fits(@Nonnull int[] values, int multiplier) {
    for (int value : values) {
      if (CompressedUtil.scale(value, multiplier) < 0) {
        return false;
      }
    }
    return true;
  }

  @Nonnull
  private static SlimefunItemStack createItemStack(@Nonnull Entry entry, @Nonnull SlimefunItem original,
      @Nonnull int[] values, int multiplier) {
    ItemStack item = original.getItem().clone();
    ItemMeta meta = item.getItemMeta();
    if (meta != null) {
      String name = meta.hasDisplayName() ? meta.getDisplayName() : original.getItemName();
      meta.setDisplayName(name + color(" &a(" + multiplier + ")"));

      List<String> lore = new ArrayList<>();
      if (meta.hasLore() && meta.getLore() != null) {
        for (String line : meta.getLore()) {
          lore.add(scaleEnergyLine(line, entry.category(), values, multiplier));
        }
      }
      lore.add("");
      lore.add(color(Supreme.getLocalization().getString(entry.category().loreKey, multiplier)));
      meta.setLore(lore);
      item.setItemMeta(meta);
    }
    return new SlimefunItemStack(entry.id() + "_X" + multiplier, item);
  }

  // the energy lines of the original lore: LoreBuilder ("1000 J Buffer") and UtilEnergy ("1,000.00 J/tick")
  private static final Pattern ENERGY_LINE = Pattern.compile(
      "(\u26A1 \u00A77)(Infinite|[0-9][0-9.,]*)( J(?: Buffer| Capacity|/s|/tick))");

  /**
   * Replace the energy value of a lore line by the real value of the compressed item:
   * the buffer/capacity and the energy per tick of the original item, multiplied.
   */
  @Nonnull
  private static String scaleEnergyLine(@Nonnull String line, @Nonnull Category category, @Nonnull int[] values,
      int multiplier) {
    Matcher matcher = ENERGY_LINE.matcher(line);
    if (!matcher.find()) {
      return line;
    }
    String unit = matcher.group(3);
    boolean storage = unit.endsWith("Buffer") || unit.endsWith("Capacity");
    // machines and generators: {energy per tick, capacity/buffer}, capacitors: {capacity}
    int index = category == Category.CAPACITORS ? 0 : (storage ? 1 : 0);
    if (index >= values.length || (category == Category.CAPACITORS && !storage)) {
      return line;
    }
    long value = (long) values[index] * multiplier;
    String number = matcher.group(2);
    String formatted = number.contains(",") || number.contains(".") ? UtilEnergy.format(value)
        : String.valueOf(value);
    return line.substring(0, matcher.start(2)) + formatted + line.substring(matcher.end(2));
  }

  @Nonnull
  private static String color(@Nonnull String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }

  /**
   * Load {@code compressed.yml} and add the missing options, the existing values are kept.
   */
  @Nonnull
  private static Config loadConfig(@Nonnull Supreme plugin, @Nonnull List<Entry> entries) {
    File file = new File(plugin.getDataFolder(), CONFIG_FILE);
    if (!file.exists()) {
      plugin.saveResource(CONFIG_FILE, false);
    }
    Config cfg = new Config(file);
    cfg.setDefaultValue("enabled", true);
    for (int multiplier : MULTIPLIERS) {
      cfg.setDefaultValue("multipliers.x" + multiplier, true);
    }
    for (Entry entry : entries) {
      cfg.setDefaultValue(entry.category().configSection + "." + entry.id(), true);
    }
    cfg.save();
    return cfg;
  }

  @Nonnull
  private static List<Entry> createEntries() {
    List<Entry> entries = new ArrayList<>();

    // machines: the time and speed are the same as the original, the energy is used per running unit
    for (SlimefunItemStack item : List.of(VirtualGarden.VIRTUAL_GARDEN_MACHINE, VirtualGarden.VIRTUAL_GARDEN_MACHINE_II,
        VirtualGarden.VIRTUAL_GARDEN_MACHINE_III)) {
      entries.add(new Entry(item, Category.MACHINES, CompressedSetup::machineValues, (a, o) -> {
        VirtualGarden m = (VirtualGarden) o;
        return setupMachine(new CompressedVirtualGarden(a.itemGroup(), a.item(), RecipeType.COMPRESSOR, a.recipe(),
            a.multiplier(), m.getEnergyConsumption()), a, m);
      }));
    }
    for (SlimefunItemStack item : List.of(VirtualAquarium.VIRTUAL_AQUARIUM_MACHINE,
        VirtualAquarium.VIRTUAL_AQUARIUM_MACHINE_II, VirtualAquarium.VIRTUAL_AQUARIUM_MACHINE_III)) {
      entries.add(new Entry(item, Category.MACHINES, CompressedSetup::machineValues, (a, o) -> {
        VirtualAquarium m = (VirtualAquarium) o;
        return setupMachine(new CompressedVirtualAquarium(a.itemGroup(), a.item(), RecipeType.COMPRESSOR,
            a.recipe(), a.multiplier(), m.getEnergyConsumption()), a, m);
      }));
    }
    for (SlimefunItemStack item : List.of(MobCollector.MOB_COLLECTOR_MACHINE, MobCollector.MOB_COLLECTOR_MACHINE_II,
        MobCollector.MOB_COLLECTOR_MACHINE_III)) {
      entries.add(new Entry(item, Category.MACHINES, CompressedSetup::machineValues, (a, o) -> {
        MobCollector m = (MobCollector) o;
        CompressedMobCollector compressed = new CompressedMobCollector(a.itemGroup(), a.item(),
            RecipeType.COMPRESSOR, a.recipe(), a.multiplier(), m.getEnergyConsumption());
        compressed.setMobRange(m.getMobRange());
        return setupMachine(compressed, a, m);
      }));
    }
    for (SlimefunItemStack item : List.of(MobTechCollector.MOB_TECH_COLLECTOR_MACHINE_I,
        MobTechCollector.MOB_TECH_COLLECTOR_MACHINE_II, MobTechCollector.MOB_TECH_COLLECTOR_MACHINE_III)) {
      entries.add(new Entry(item, Category.MACHINES, CompressedSetup::machineValues, (a, o) -> {
        MobTechCollector m = (MobTechCollector) o;
        CompressedMobTechCollector compressed = new CompressedMobTechCollector(a.itemGroup(), a.item(),
            RecipeType.COMPRESSOR, a.recipe(), a.multiplier(), m.getEnergyConsumption());
        compressed.setMobRange(m.getMobRange());
        return setupMachine(compressed, a, m);
      }));
    }

    // generators: the energy per tick and the buffer are multiplied
    for (SlimefunItemStack item : List.of(SupremeGenerator.BASIC_IGNIS_GENERATOR, SupremeGenerator.IGNIS_GENERATOR,
        SupremeGenerator.BASIC_VENTUS_GENERATOR, SupremeGenerator.VENTUS_GENERATOR,
        SupremeGenerator.BASIC_AQUA_GENERATOR, SupremeGenerator.AQUA_GENERATOR,
        SupremeGenerator.BASIC_LUX_GENERATOR, SupremeGenerator.LUX_GENERATOR,
        SupremeGenerator.BASIC_LUMIUM_GENERATOR, SupremeGenerator.LUMIUM_GENERATOR,
        SupremeGenerator.THORNIUM_GENERATOR, SupremeGenerator.SUPREME_GENERATOR)) {
      entries.add(new Entry(item, Category.GENERATORS,
          o -> new int[] {((EnergyGenerator) o).getEnergy(), ((EnergyGenerator) o).getBuffer()}, (a, o) -> {
        EnergyGenerator g = (EnergyGenerator) o;
        int energy = a.scale(g.getEnergy());
        int buffer = a.scale(g.getBuffer());
        return new CompressedEnergyGenerator(a.itemGroup(), a.item(), RecipeType.COMPRESSOR, a.recipe(),
            a.multiplier()).setType(g.getType()).setEnergy(energy).setBuffer(buffer);
      }));
    }
    for (SlimefunItemStack item : List.of(GeneratorMob.GENERATOR_MOB_BASIC, GeneratorMob.GENERATOR_MOB_MEDIUM,
        GeneratorMob.GENERATOR_MOB_ADVANCED)) {
      entries.add(new Entry(item, Category.GENERATORS,
          o -> new int[] {((GeneratorMob) o).getEnergyProduction(), ((GeneratorMob) o).getCapacity()}, (a, o) -> {
        GeneratorMob g = (GeneratorMob) o;
        int energy = a.scale(g.getEnergyProduction());
        int buffer = a.scale(g.getCapacity());
        return new CompressedGeneratorMob(a.itemGroup(), a.item(), RecipeType.COMPRESSOR, a.recipe(),
            a.multiplier()).setEnergy(energy).setBuffer(buffer).setMobRange(g.getMobRange());
      }));
    }

    // capacitors: the capacity is multiplied, up to the int limit
    for (SlimefunItemStack item : List.of(SupremeCapacitor.AURUM_CAPACITOR, SupremeCapacitor.TITANIUM_CAPACITOR,
        SupremeCapacitor.ADAMANTIUM_CAPACITOR, SupremeCapacitor.THORNIUM_CAPACITOR,
        SupremeCapacitor.SUPREME_CAPACITOR)) {
      entries.add(new Entry(item, Category.CAPACITORS, o -> new int[] {((EnergyCapacitor) o).getCapacity()},
          (a, o) -> {
        EnergyCapacitor c = (EnergyCapacitor) o;
        int capacity = a.scale(c.getCapacity());
        return new CompressedCapacitor(a.itemGroup(), a.item(), RecipeType.COMPRESSOR, a.recipe(), capacity,
            a.multiplier());
      }));
    }
    return entries;
  }

  /**
   * The multiplied values of a machine: energy per tick (at full load) and capacity.
   */
  @Nonnull
  private static int[] machineValues(@Nonnull SlimefunItem original) {
    GenericMachine machine = (GenericMachine) original;
    return new int[] {machine.getEnergyConsumption(), machine.getCapacity()};
  }

  /**
   * Copy the settings of the original machine: same time and speed, multiplied energy (at full load) and capacity.
   */
  @Nonnull
  private static SlimefunItem setupMachine(@Nonnull GenericMachine compressed,
      @Nonnull Args a, @Nonnull GenericMachine original) {
    compressed.setTimeProcess(original.getTimeProcess());
    compressed.setProcessingSpeed(original.getSpeed());
    compressed.setCapacity(a.scale(original.getCapacity()));
    compressed.setEnergyConsumption(a.scale(original.getEnergyConsumption()));
    return compressed;
  }
}
