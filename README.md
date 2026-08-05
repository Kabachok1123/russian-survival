# Russian Survival

Russian Survival is a server-authoritative Fabric mod for Minecraft Java 1.21.1.
Install it and continue playing any ordinary new or existing world: cold starts
rising, shelter and fire matter, brown bears hunt across varied Overworld biomes,
warm clothing buys time, tea and borscht are safe heat, and Vodka is a powerful
emergency option with deliberately awful consequences.

There is no custom world type. Vanilla terrain, seeds, structures and progression
remain intact. The mod adds its content through biome and structure hooks: villages
are more frequent and may contain a banya, while the Nether gains the rare Siberian
Inferno climate region alongside its vanilla biomes.

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

The installable release is written to `build/libs/russian-survival-1.4.2.jar`.
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
2. Put Fabric API 0.116.15+1.21.1 and `russian-survival-1.4.2.jar` in the instance's
   `mods` folder.
3. Launch using Java 21.
4. Client and server both need the mod and Fabric API for multiplayer.

## Worlds

Create or open a world exactly as in vanilla Minecraft. No world preset or special
`level-type` is required. Existing chunks remain untouched, but all newly generated
Every Overworld biome keeps its vanilla registry ID, terrain and structures, but receives
a winter climate, snow, frozen water and a cold regional palette. New structures
appear in new chunks, while climate colors and snowfall also work in existing
ordinary worlds.

## Mechanics

### Cold

`cold_level` ranges from 0 (warm) to 100 (critical) and persists with the player.
It updates once per second, not every tick. Day/night, open sky, snowfall, storms,
water, altitude, underground depth, sprinting and insulation modify gain. A bounded
4-block heat scan recognizes lit campfires, furnaces, smokers, blast furnaces, fire,
lava, weak torches and a working samovar.

- 40: cold vignette and occasional teeth/audio feedback
- 60: Slowness I
- 75: Slowness II and Weakness I
- 90: Slowness III and Mining Fatigue I
- 100: vanilla freeze damage every configured interval

The default calm opening reaches critical cold in about 182 seconds from the
starting value of 20. Storm tuning yields roughly 104 seconds. Creative and
spectator players are exempt.

### Equipment and bears

Each vanilla leather armor piece provides early insulation. Ushanka and Fur Coat
provide substantially more but do not make water or storms harmless. Brown bears
spawn throughout varied Overworld biomes in groups of up to three and proactively
hunt nearby players. Rare Frostback and Snowstalker variants turn encounters into
recordable mini-events. Their search is bounded per player and capped by config.
Vanilla fish loot remains; an injected pool adds 0–2 Bear Fur.

Every vanilla forest family now has an additional high-weight bear spawn pool:
oak, flower, birch, old-growth birch, dark, taiga, snowy and old-growth taiga,
windswept forest, grove, cherry grove and all three jungle variants. Forest groups
can contain two to five bears, making woodland travel visibly more dangerous.

### Food, Vodka and Samovar

- Hot Tea removes 18 cold and grants Speed I for 15 seconds.
- Borscht removes 25 cold, grants brief Regeneration I and returns a bowl.
- Vodka removes 20 cold and grants 45 seconds of Resistance II, Strength II,
  Health Boost IV and strong cold protection. It simultaneously applies Drunk,
  nausea, camera drift and movement inversion/stagger. Stacking to intoxication 3
  causes Blindness and later Hangover. Bottles are returned.
- Fill a Samovar with a water bucket, ignite it with coal/charcoal, then use a glass
  bottle while carrying sugar and any leaves. It produces Hot Tea, emits steam and
  acts as a strong heat source for 90 seconds.

### Siberian Inferno and Abandoned Banya

Siberian Inferno is a cold, blue-gray Nether biome mixed alongside the five vanilla
Nether biomes. It contains Frozen Blackstone ore patches and original ambience.
Cold is active there but nearby Nether heat sources are especially valuable.

The entire Nether now participates in the survival loop, with each route behaving
differently:

- **Soul Sand Valley:** the fastest cold gain and Nether Permafrost deposits.
- **Basalt Deltas:** dangerous medium-high cold and Frozen Blackstone ore.
- **Nether Wastes:** steady cold and extra Frozen Blackstone deposits.
- **Warped Forest:** lower cold plus Nether Permafrost, making it a useful route.
- **Crimson Forest:** the warmest vanilla Nether biome and a natural rest stop.
- **Siberian Inferno:** the harshest region, rich in Frozen Blackstone.

Fire Resistance now also provides strong warmth in the Nether. Frozen Blackstone
remains a building resource and cannot be converted into Obsidian; portals still
require the normal Minecraft progression and resource routes.

Visually, the Nether is now **Hell Freezing Over**: all five vanilla Nether biomes
receive cold blue-gray fog, drifting snowflake ambience and dense veins of Nether
Permafrost. Soul Sand Valleys become pale blizzards, Basalt Deltas turn steel-blue,
Warped Forests gain deep cyan frost, and Crimson Forests keep a muted warm-red tone
as the safer heat route. Lava remains bright and dangerous, creating a strong
red-against-blue contrast instead of making every scene uniformly gray.

Abandoned Banya is a rare 7×7 jigsaw structure with a campfire, cauldron, samovar
and a loot barrel. Its random-spread spacing is 42 chunks by default. Loot is kept
modest: potatoes, fuel, ingredients, metal, rare tea and very rare Vodka.

Vanilla villages use closer spacing and can generate a compact working banya among
their houses. Village chests also gain a small chance for Hot Tea and Bear Fur.
Village bells are converted into functional Bear Bells when approached.

The redesigned Village Banya is a conspicuous 9×9 spruce-log cabin with a stepped
roof, tall smoking chimney, red banner, lantern, samovar, water cauldron, campfire,
bed and loot barrel. Its village pool weight is high, and the same building also
generates independently across the Overworld. Use
`/locate structure russian_survival:village_banya` to find the standalone version.

Bear Shrines are rare mossy-cobblestone and birch ruins found across the Overworld.
Each has a Bear Bell and a small supply chest. Vanilla biome IDs remain intact, so
locate commands and biome-specific structures keep working. Their presentation is
winterized instead: deserts become pale snow steppes, swamps become dark frozen
marshes, jungles become dense snowy wilderness, badlands become frosted rock country,
and oceans freeze beneath a cold blue-gray sky.

## Recipes

- **Ushanka:** top row `Bear Fur ×3`; second row `Bear Fur, Leather, Bear Fur`.
- **Fur Coat:** fur at both shoulders, a leather chestplate in the center, fur
  around/below it.
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
  multipliers, heat radius/strength, damage interval, armor insulation, bear spawn
  and aggression/cap, Vodka/Drunk/Hangover durations, Inferno enable flag, banya
  spacing and daily snow chance.
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
/locate biome russian_survival:siberian_inferno
/locate structure russian_survival:abandoned_banya
/locate structure russian_survival:bear_shrine
/weather rain 600
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

The automated balance tests assert the 150–210 second calm opening and 90–130
second storm opening. The resource validator parses every JSON file, resolves all
mod texture references and verifies every declared OGG stream.

The 1.4.2 release smoke test was performed with Java 21 and included:

- full Gradle build and JUnit pass;
- dedicated Fabric server startup with no client-class crash;
- ordinary world creation with all vanilla biome IDs preserved and winterized;
- successful locate of a vanilla stronghold (confirming progression);
- successful locate of Siberian Inferno and Abandoned Banya;
- successful load of Bear Shrine, birch-grove and expanded Nether worldgen data;
- graceful save of Overworld, Nether and End.

## Known limitations

- The first dev-client run may spend several minutes downloading Mojang's asset
  index. On the test machine this external download did not finish within the
  bounded smoke-test window; compilation and dedicated-server resource loading did.
- Ushanka uses a custom cuboid inventory/hand model; worn armor still uses the
  stable vanilla leather armor renderer for maximum mod compatibility.
- Banya spacing is registry data loaded before ordinary server config. The shipped
  value is 42 chunks; changing the JSON config field alone cannot rebuild an already
  loaded worldgen registry.

## Asset and code licenses

Source code is MIT licensed. Original PNG art and procedurally synthesized OGG audio
are CC0-1.0; details are in `ASSET_LICENSES.md`. The generated sounds contain no
third-party samples or music. AI-assisted concept sheets for the new cuboid models
are stored under `art/reference`; the shipped game models were authored as JSON.
