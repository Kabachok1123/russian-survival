# Russian Survival

Russian Survival is a server-authoritative Fabric mod for Minecraft Java 1.21.1.
Install it and continue playing any ordinary new or existing world: cold starts
rising, shelter and fire matter, brown bears hunt across varied Overworld biomes,
warm clothing buys time, tea and borscht are safe heat, and Vodka is a powerful
emergency option with deliberately awful consequences.

There is no custom world type. Vanilla terrain, seeds, structures and progression
remain intact. The mod adds its content through biome and structure hooks: villages
are more frequent and may contain a banya, while all five ordinary Nether biomes
become frozen without adding a separate biome or dimension.

The goal is still to complete Minecraft and defeat the Ender Dragon. Every system
is balanced around that journey: hazards slow exploration and preparation, while
the mod's food, gear, settlements and risky buffs can accelerate progression when
used well. The mod does not duplicate vanilla's dragon advancement.

## Requirements

- Minecraft Java Edition 1.21.1
- Java 21
- Fabric Loader 0.19.3 or newer compatible loader
- Fabric API 0.116.15+1.21.1

The project uses Fabric Loom 1.17.17, Mojang's official 1.21.1 mappings, and the
Gradle 9.5.1 wrapper. Dependency versions were checked against Fabric Meta and the
official Fabric Maven repository on 2026-08-04.

## Build and development

On Windows PowerShell, select a Java 21 installation and run:

```powershell
$env:JAVA_HOME = 'C:\path\to\jdk-21'
.\gradlew.bat clean build
```

On Linux/macOS:

```bash
export JAVA_HOME=/path/to/jdk-21
./gradlew clean build
```

The installable release is written to `build/libs/russian-survival-1.6.0.jar`.
The `*-sources.jar` is for development and should not be installed as the mod.

Development runs:

```powershell
.\gradlew.bat runClient
.\gradlew.bat runServer
```

For the development server, accept Mojang's EULA in `run/eula.txt`. The checked-in
`run` directory is ignored; any local EULA acceptance is only for your own test
environment.

## Installation

1. Install Fabric Loader for Minecraft 1.21.1.
2. Put Fabric API 0.116.15+1.21.1 and `russian-survival-1.6.0.jar` in the instance's
   `mods` folder.
3. Launch using Java 21.
4. Client and server both need the mod and Fabric API for multiplayer.

## Worlds

Create or open a world exactly as in vanilla Minecraft. No world preset or special
`level-type` is required. Existing chunks remain untouched, but new worldgen content
appears in newly generated chunks. Every Overworld biome keeps its vanilla registry ID,
terrain and structures, but receives
a winter climate, snow, frozen water and a cold regional palette. New structures
appear in new chunks, while climate colors and snowfall also work in existing
ordinary worlds.

## Mechanics

### Cold

`cold_level` ranges from 0 (warm) to 100 (critical) and persists with the player.
It updates once per second, not every tick. Snowfall is maintained continuously in
the Overworld; night, open sky, storms, water, altitude, underground depth,
sprinting, Powder Snow and insulation modify gain. A bounded
4-block heat scan recognizes lit campfires, furnaces, smokers, blast furnaces, fire,
lava, weak torches and a working samovar.

- 20: the first visible edge frost appears
- 40: a clearly visible cold vignette and occasional teeth/audio feedback
- 60: Slowness I
- 75: Slowness II and Weakness I
- 90: Slowness III and Mining Fatigue I
- 100: freeze damage every two seconds: 0.5/1/2/3 damage on
  Peaceful/Easy/Normal/Hard

The default constant-snow opening reaches critical cold in about 96 seconds from
the starting value of 20; a snowstorm shortens it to roughly 74 seconds. Powder
Snow adds 2.8 cold per second before insulation and works alongside vanilla sinking
and freezing. Campfires and fire remove cold quickly. A torch within the heat radius
stops ordinary cold gain but removes only 0.02 cold per second; it cannot neutralize
Powder Snow immersion. Creative and spectator players are exempt.

Thunder weather is a full blizzard event rather than only a cold multiplier. Visibility
pulses between roughly 15 and 20 blocks, extra snow sweeps across exposed players and
irregular coherent gusts push them sideways. A roof or other solid shelter completely
blocks the physical wind; water also prevents it, and crouching reduces push strength
by about 58%. Vanilla Blindness, Darkness, underwater and lava fog keep priority.

### Equipment and bears

Each vanilla leather armor piece provides early insulation. Ushanka provides
substantially more but does not make water or storms harmless. Brown bears
spawn throughout varied Overworld biomes in groups of up to three and proactively
hunt nearby players. Rare stronger and faster variants create recordable
mini-events, but no naturally spawned bear receives a visible special name. Their
search is bounded per player and capped by config. Vanilla fish loot remains; an
injected pool guarantees 1–2 Bear Fur.

Every vanilla forest family now has an additional high-weight bear spawn pool:
oak, flower, birch, old-growth birch, dark, taiga, snowy and old-growth taiga,
windswept forest, grove, cherry grove and all three jungle variants. Forest groups
can contain two to five bears, making woodland travel visibly more dangerous.

### Food, Vodka and Samovar

- Hot Tea removes 18 cold and grants Speed I for 15 seconds.
- Borscht removes 25 cold, grants brief Regeneration I and returns a bowl.
- Vodka removes 20 cold, grants 15 seconds of Regeneration I and 45 seconds of
  Resistance II, Strength II, Health Boost IV and strong cold protection. It applies Drunk,
  nausea, camera drift and movement inversion/stagger. Stacking to intoxication 3
  causes Blindness and later Hangover. Bottles are returned.
- Fill a Samovar with a water bucket, ignite it with coal/charcoal, then use a glass
  bottle while carrying sugar and any leaves. It produces Hot Tea, emits steam and
  acts as a strong heat source for 90 seconds.

### Frozen Nether and Abandoned Banya

There is no separate custom Nether biome. The five vanilla Nether biomes retain
their registry IDs, terrain, fortresses, bastions and normal progression while
receiving frozen visuals and resources. Entering the Nether stops cold gain and
slowly lowers the meter by 0.18 per second; deliberate heat sources accelerate it.
Ambient Nether lava is ignored by the heat scan so it cannot instantly clear the meter.

The entire Nether remains visually frozen, but all five biomes now share the same
slow-recovery rule. Their terrain, visibility and resources still create distinct
routes through Soul Sand Valleys, Basalt Deltas, Nether Wastes and both forests.

Fire Resistance now also provides strong warmth in the Nether. Frozen Blackstone
remains a building resource and cannot be converted into Obsidian; portals still
require the normal Minecraft progression and resource routes.

Visually, the Nether is now **Hell Freezing Over**: all five vanilla Nether biomes
receive cold blue-gray fog, drifting snowflake ambience, 24 large Permafrost
placement attempts, 18 Packed Ice attempts and 5 rarer Blue Ice attempts per chunk.
Rare solid Obsidian basins represent lava lakes that froze in place, while ordinary
lava remains elsewhere for progression and a strong red-against-blue contrast.

Abandoned Banya is a rare 7×7 sealed jigsaw structure with a real door, benches,
full water cauldron, samovar, campfire, loot barrel and smoking chimney. It can
appear throughout the five vanilla Nether biomes. Its random-spread spacing is 42
chunks by default.

Vanilla villages use closer spacing and can generate a compact working banya among
their houses. Village chests also gain a small chance for Hot Tea and Bear Fur.
Village bells are converted into functional Bear Bells when approached.

The redesigned Village Banya is a sealed 9×9 spruce-log cabin with closed roof
gables, exterior spruce leaves and a tall smoking chimney. A door separates the
main room from a dedicated steam room with benches, a campfire and two full water
cauldrons. Its barrel guarantees several potato stacks for brewing Potato Mash,
with sugar, fuel, tea and very rare Vodka as secondary loot. Its village pool
weight is high, and the same building also generates independently. Use
`/locate structure russian_survival:village_banya` to find the standalone version.

Bear Shrines are rare mossy-cobblestone and birch ruins found across the Overworld.
Each has a Bear Bell and a small supply chest. Vanilla biome IDs remain intact, so
locate commands and biome-specific structures keep working. Their presentation is
winterized instead: deserts become pale snow steppes, swamps become dark frozen
marshes, jungles become dense snowy wilderness, badlands become frosted rock country,
and oceans freeze beneath a cold blue-gray sky.

## Recipes

- **Ushanka:** top row `Bear Fur ×3`; second row `Bear Fur, Leather, Bear Fur`.
- **Potato Mash:** 3 potatoes + sugar + water potion/bottle, shapeless.
- **Vodka:** smelt or smoke Potato Mash (300/150 ticks, 0.7 XP).
- **Hot Tea:** water potion/bottle + sugar + any leaves, shapeless; the Samovar is
  the immersive alternative.
- **Borscht:** bowl + 2 beetroot + cooked beef + baked potato, shapeless.
- **Samovar:** copper over a bucket, iron on both sides, campfire below.
- **Bear Bell:** string over copper/iron; place it and ring it to briefly outline
  bears within 48 blocks and provoke them into chasing the player.

## Configuration

Files are created safely on first launch and rewritten with defaults if malformed:

- `config/russian_survival-server.json`: cold enable/start/gain, weather and water
  multipliers, Powder Snow gain, Nether recovery, heat radius/strength, damage
  interval, armor insulation, bear spawn and aggression/cap, Vodka/Drunk/Hangover
  durations, banya spacing, daily snowstorm chance, gust frequency and wind strength.
- `config/russian_survival-client.json`: HUD visibility and offsets, reduced nausea,
  camera rotation toggle, input inversion toggle, vignette intensity and ambience
  volume.

Config field names are self-describing JSON. Restart after server balance changes.
The client accessibility toggles can be edited before launch; `showColdHud=false`
also provides a clean recording frame.

## Recording Guide

Commands require permission level 2 and are not part of survival progression:

```mcfunction
/russiansurvival cold 40
/russiansurvival cold 100
/give @s russian_survival:ushanka
/give @s russian_survival:vodka_bottle
/give @s russian_survival:hot_tea
/give @s russian_survival:bear_bell
/locate structure russian_survival:abandoned_banya
/locate structure russian_survival:bear_shrine
/weather thunder 600
/summon minecraft:polar_bear ~ ~ ~
/russiansurvival reload_config
```

For a clean HUD shot set `showColdHud` to `false` in the client config and restart.
To teleport after `/locate`, click the coordinates in chat when cheats are enabled.

## Testing

Run all unit/resource/build checks:

```powershell
.\gradlew.bat test build
python tools/validate_resources.py
```

The automated balance tests assert the 90–105 second constant-snow opening and
70–80 second storm opening. The resource validator parses every JSON file, resolves all
mod texture references and verifies every declared OGG stream.

The 1.6.0 release smoke test was performed with Java 21 and included:

- full Gradle build and JUnit pass;
- dev-client startup through sound and texture-atlas initialization with the
  blizzard fog mixin enabled;
- dedicated Fabric server startup with no client-class crash;
- ordinary world creation with all vanilla biome IDs preserved and winterized;
- successful locate of a vanilla stronghold (confirming progression);
- successful load of sealed banya templates and expanded frozen Nether worldgen;
- graceful save of Overworld, Nether and End.

## Known limitations

- The first dev-client run may spend several minutes downloading Mojang's asset
  index. On the test machine this external download did not finish within the
  bounded smoke-test window; compilation and dedicated-server resource loading did.
- Ushanka uses a custom cuboid model when worn; its inventory sprite is a separate
  readable 16×16 texture.
- Banya spacing is registry data loaded before ordinary server config. The shipped
  value is 42 chunks; changing the JSON config field alone cannot rebuild an already
  loaded worldgen registry.

## Asset and code licenses

Source code is MIT licensed. Original PNG art and procedurally synthesized OGG audio
are CC0-1.0; details are in `ASSET_LICENSES.md`. The generated sounds contain no
third-party samples or music. AI-assisted concept sheets for the new cuboid models
are stored under `art/reference`; the shipped game models were authored as JSON.
