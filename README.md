<div align="center">

# Supreme

**A Slimefun addon with 100+ new resources, machines, generators, weapons, tools and armor.**

Forge Titanium, Aurum, Adamantium and Thornium, then upgrade your gear through
Magic, Rare, Epic, Legendary and **Supreme** tiers.

[![Java CI](https://github.com/mybezo/Supreme/actions/workflows/maven.yml/badge.svg)](https://github.com/mybezo/Supreme/actions/workflows/maven.yml)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21%2B-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![Version](https://img.shields.io/badge/version-2.0.0-blue)
[![License](https://img.shields.io/badge/license-GPL--3.0-lightgrey)](LICENSE)

</div>

---

## Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [What's new in 2.0.0](#whats-new-in-200)
- [Machines](#machines)
- [Generators & Capacitors](#generators--capacitors)
- [Gear: Weapons, Tools & Armor](#gear-weapons-tools--armor)
- [Resources](#resources)
- [Configuration](#configuration)
- [Building from source](#building-from-source)
- [Support](#support)
- [Credits & License](#credits--license)

## Features

- **100+ resources**: alloys, plates, cores, magical scepters and attributes
- **Multiblock & electric machines** to forge ingots, cores and magical items
- **Automation**: quarries, mob collector, virtual garden and virtual aquarium
- **MobTech system**: collect, mutate and evolve mob "tech" heads to speed up machines
- **12 generators and 5 capacitors**, each with its own placement rule
- **Tiered gear**: swords, bows, shields, spears, pickaxes, axes, shovels, fishing rods and full armor sets
- **Translations**: English (`en-US`), Portuguese (`pt-BR`) and Simplified Chinese (`zh-CN`)
- **Highly configurable**: enchantments, effects, power values, quarry output and more

## Requirements

| | |
|---|---|
| **Server** | Spigot or Paper **1.21+** (spears need **1.21.11+**) |
| **Java** | 21 or newer |
| **Slimefun** | Latest [Slimefun Dev build](https://blob.build/project/Slimefun4/Dev) |

> [!IMPORTANT]
> Version 2.0.0 no longer supports Minecraft 1.17–1.20.4. Stay on an older Supreme build for those versions.

## Installation

1. Download `Supreme.jar` from the **Artifacts** section of the latest successful
   [Java CI run](https://github.com/mybezo/Supreme/actions/workflows/maven.yml).
2. Put it in your server's `plugins/` folder next to Slimefun.
3. Start the server. `plugins/Supreme/config.yml` and the language files are generated on first start.
4. Optional: set `options.lang` in `config.yml` to `en-US`, `pt-BR` or `zh-CN`.

## What's new in 2.0.0

**Compatibility**
- Updated for Minecraft 1.21+ and the latest Slimefun Dev builds (Java 21)

**New items**
- **Spear set**: Titanium, Aurum, Adamantium and Thornium → Supreme (Minecraft 1.21.11+), with the Lunge enchantment up to level 20
- **Fishing rod set**: Titanium, Aurum, Adamantium and Thornium → Supreme

**Changes**
- Tech Generator cards and card components now stack up to **64**

**Performance & fixes**
- Each quarry now keeps its own production timer and on/off switch. Before, quarries of the same type shared one timer and one switch.
- Quarries no longer copy the target chest inventory every tick
- Mob Collector scans for nearby mobs once per tick instead of once per recipe
- Machines clear their progress when broken, so it no longer leaks or resumes on re-placement
- Tech Generator now runs on the main thread (safe inventory access)

**Removed**
- **CheckInventory** lamp. Break any placed ones before updating.

## Machines

### Multiblock machines

| Machine | Description |
|---|---|
| **Core Fabricator** | Craft resource cores |
| **Gear Fabricator** | Craft weapons, tools and armor |
| **Magical Fabricator** | Craft magical items |

### Electric machines

| Machine | Description |
|---|---|
| **Electric Core Machine** | Craft core resources with power |
| **Electric Magical Machine** | Craft magical resources with power |
| **Electric Gear Fabricator** | Upgrade gear to higher tiers with power |
| **Forge Ingot** | Forge ingot resources |
| **Forge Magical** | Forge magical resources |
| **Foundry** | Foundry and synthesizer items |
| **Magic Altar** | Craft runes and magical items |

### Automation

| Machine | Description |
|---|---|
| **Mob Collector** | Collects drops from nearby mobs using shears, swords or bottles |
| **Virtual Garden** | Grows crops and natural resources |
| **Virtual Aquarium** | Collects items you would normally fish up at sea |
| **Quarries** | Cobblestone, Coal, Iron, Gold, Diamond, Thornium and Nuggets of Supreme. Place one on top of a chest, trapped chest, barrel or shulker box. |

### MobTech

| Machine | Description |
|---|---|
| **MobTech Collector** | Captures nearby mobs as MobTech items that reduce processing time, increase output or save energy |
| **Tech Mutation** | Evolves MobTech by chance: 20–25% success in tier I up to 80–100% in tier III |
| **Tech Robotic** | Evolves MobTech without luck, using a fixed amount of resources (64× tier I, 32× tier II, 16× tier III) |
| **Tech Generator** | Slowly generates materials from a crafting card. Bee, Golem and Zombie MobTech speed it up. |

## Generators & Capacitors

| Generator | Requirement |
|---|---|
| **Generator Mob** | Generates energy from methane produced by nearby animals |
| **Basic Ignis / Ignis Generator** | Fire below the block |
| **Basic Ventus / Ventus Generator** | Open faces exposed to the wind |
| **Basic Aqua / Aqua Generator** | Water below the block |
| **Basic Lux / Lux Generator** | Sunlight or moonlight |
| **Basic Lumium / Lumium Generator** | Placed below ground |
| **Thornium Generator** | Works anywhere |
| **Supreme Generator** | Works anywhere |

**Capacitors:** Aurum, Titanium, Adamantium, Thornium and Supreme.

Capacity, buffer and output of every generator and capacitor can be changed in `power-section`.

## Gear: Weapons, Tools & Armor

All gear is crafted in the **Gear Fabricator** and is **Soulbound**. Thornium gear and higher is unbreakable by default.

| Tier | Material | Stage |
|---|---|---|
| Titanium | Titanium Plate | Basic |
| Aurum | Aurum Plate | Basic |
| Adamantium | Adamantium Plate | Basic |
| Thornium | Thornium Ingot | Advanced |
| Magic | upgrade of Thornium | Advanced |
| Rare | upgrade of Magic | Advanced |
| Epic | upgrade of Rare | Endgame |
| Legendary | upgrade of Epic | Endgame |
| Supreme | upgrade of Legendary | Endgame |

| Category | Items |
|---|---|
| **Weapons** | Sword, Spear¹, Bow², Shield² |
| **Tools** | Pickaxe, Axe, Shovel, Fishing Rod |
| **Armor** | Helmet, Chestplate, Leggings, Boots |

¹ Requires Minecraft 1.21.11+. On older servers the spear set is skipped automatically.
² Thornium tiers only.

## Resources

### Resource Cores

| Core | Description |
|---|---|
| **Core of Life** | Fragments of life collected from the world |
| **Core of Death** | Souls of entities that have gone beyond |
| **Core of Color** | Colors collected around the world |
| **Core of Block** | Blocks collected around the world |
| **Core of Nature** | Natural materials collected around the world |
| **Core of Alloy** | Ores collected around the world |

### Magical Resources

| Item | Description |
|---|---|
| **Cetrus Lux / Ventus / Lumium / Aqua / Ignis** | Super elemental scepters |
| **Attribute Magic** | Protection, Unbreaking and Mending |
| **Attribute Bomb** | Blast, Fire and Projectile Protection, Thorns |
| **Attribute Fortune** | Efficiency and Fortune |
| **Attribute Impetus** | Sharpness, Fire Aspect and Looting |

## Configuration

All settings live in `plugins/Supreme/config.yml`. New keys added by updates are read from the
defaults inside the jar, so you don't have to regenerate your config.

<details>
<summary><b>General options</b> (<code>options</code>)</summary>

| Option | Default | Description |
|---|---|---|
| `lang` | `en-US` | Language: `en-US`, `pt-BR` or `zh-CN` |
| `auto-update` | `true` | Auto-update (only for `Dev` builds from Blob Builds) |
| `use-legacy-supremeexpansion-item-id` | `false` | Keep item IDs compatible with the old SupremeExpansion addon |
| `custom-bc` | `false` | Alternative balance: lower attribute enchantments and fewer Mob Collector drops |
| `enable-generators` | `true` | Enable generators |
| `limit-production-generators` | `false` | Reduce generator output (factor 5×) |
| `delay-time-valid-generators` | `600` | Delay before rechecking a generator after nearby blocks change |
| `enable-quarry` | `true` | Enable quarries |
| `limit-production-quarry` | `false` | Hard mode: 50% chance for a quarry cycle to fail |
| `custom-ticker-delay` | `2` | Quarry production delay |
| `base-time-virtual-garden` | `15` | Virtual Garden base processing time |
| `base-time-virtual-aquarium` | `15` | Virtual Aquarium base processing time |
| `base-time-mob-collector` | `15` | Mob Collector base processing time |
| `base-time-tech-generator` | `1800` | Tech Generator base processing time |
| `tech-generator-max-amount` | `64` | Tech Generator output amount |
| `machine-max-attempt-consumed` | `30` | Electric machines: max retries for consuming input items |
| `mob-tech-enable-bee` | `true` | Enable Bee MobTech |
| `mob-tech-enable-iron-golem` | `true` | Enable Iron Golem MobTech |
| `mob-tech-enable-zombie` | `true` | Enable Zombie MobTech |
| `enable-weapons` | `true` | Enable weapons |
| `enable-tools` | `true` | Enable tools |
| `enable-armor` | `true` | Enable armor |
| `enable-tech` | `true` | Enable MobTech machines and resources |

</details>

<details>
<summary><b>Other sections</b></summary>

| Section | Description |
|---|---|
| `quarry-custom-output` | Items and chances produced by each quarry (up to 12 items per quarry) |
| `supreme-enchant` | Enchantments and `unbreakable` flag per gear item (level `0` disables, max `100`) |
| `supreme-effect` | Potion effects granted by gear |
| `power-section` | Capacity, buffer and energy of every generator and capacitor |

Example: change the Supreme Spear enchantments.

```yaml
supreme-enchant:
  supreme_spear_supreme:
    unbreakable: true
    sharpness: 50
    looting: 20
    fire_aspect: 20
    mending: 20
    lunge: 20
```

</details>

## Building from source

Requires **JDK 21** and **Maven**.

```bash
git clone https://github.com/mybezo/Supreme.git
cd Supreme
mvn package
```

The plugin jar is written to `target/Supreme.jar`.

## Support

Found a bug or have a suggestion? [Open an issue](https://github.com/mybezo/Supreme/issues) or a pull request,
or ask in the Slimefun Addon Community on Discord.

<p>
  <a href="https://discord.gg/slimefun">
    <img src="https://discordapp.com/api/guilds/565557184348422174/widget.png?style=banner3" alt="Slimefun Discord"/>
  </a>
  <a href="https://discord.gg/SqD3gg5SAU">
    <img src="https://discordapp.com/api/guilds/809178621424041997/widget.png?style=banner3" alt="Slimefun Addon Community Discord"/>
  </a>
</p>

## Credits & License

Created by **RelativoBR**, **Especttra**, **WilianSantosBR** and **Mynothauro**.
Fork of [RelativoBR/Supreme](https://github.com/RelativoBR/Supreme).

Licensed under the [GNU General Public License v3.0](LICENSE).
