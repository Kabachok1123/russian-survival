# Changelog

## 1.5.0 — Frozen Vanilla Nether and Sealed Banya

- Removed the Fur Coat item, recipe, textures and insulation setting.
- Removed the separate Siberian Inferno biome; all frozen Nether content now uses
  the five vanilla biome IDs, and the Russian Hell advancement triggers on entering
  the Nether.
- Doubled Nether Permafrost generation, added abundant Packed Ice, rarer Blue Ice
  and occasional solid Obsidian basins representing frozen lava lakes.
- Rebuilt the cold HUD as a segmented labeled meter with a severity-scaled icy
  full-screen vignette and critical pulse.
- Sealed the Village Banya roof gables, added a separate steam room, two full water
  cauldrons, benches, spruce foliage and a taller smoking chimney.
- Added a proper door, water, benches and chimney to the Abandoned Banya.
- Made multiple stacks of potatoes guaranteed in banya barrels for Vodka brewing.

## 1.4.2 — Vanilla Obsidian Progression

- Removed the Frozen Blackstone-to-Obsidian recipe. Obsidian once again requires
  the normal Minecraft acquisition methods.

## 1.4.1 — Clearer Ushanka Icon

- Replaced the undersized inventory sprite with a larger, immediately recognizable
  brown ushanka silhouette while preserving the worn 3D armor model unchanged.

## 1.4.0 — Hell Freezing Over

- Added high-weight brown-bear spawn pools to every vanilla forest family,
  including birch forests, dark forests, taigas, groves and jungles; woodland
  groups can now contain two to five bears.
- Gave all five vanilla Nether biomes distinct frozen fog palettes while keeping
  Crimson Forests visually warmer as the safer route.
- Added drifting snowflake ambience around players throughout the Nether.
- Spread Nether Permafrost through every vanilla Nether region and increased its
  generation to twelve placement attempts per chunk.
- Kept lava bright and dangerous as both a heat source and a strong red-blue visual
  contrast for recording.

## 1.3.2 — Visible Village Banya

- Rebuilt the village banya as a recognizable 9×9 spruce cabin with a stepped roof,
  tall smoking chimney, red banner, lantern and complete heated interior.
- Increased its village house-pool weight from 3 to 8.
- Added an independently generated Overworld version locatable as
  `russian_survival:village_banya`.

## 1.3.1 — More Brown Bears

- Increased brown-bear spawn weight, group size and nearby population cap; renamed
  the vanilla-compatible entity to Brown Bear in English and Бурый медведь in Russian.
- Added a conservative config migration for installations still using the 1.3.0
  bear defaults.

## 1.3.0 — Winterized Biomes

- Restored every vanilla Overworld biome ID so locate commands and biome-specific
  structures remain valid.
- Applied snow precipitation, freezing, cold water/sky/fog colors and winter foliage
  to all 57 Overworld biomes without introducing a custom world type.
- Added distinct palettes for frozen steppes and dark winter jungle/swamp regions,
  preserving recognizable terrain and vegetation for more varied video scenes.
- Added a standalone freeze-top configured feature to avoid vanilla feature-order
  cycles when generating new chunks.
- Smoke-tested a completely new normal world through spawn generation and server
  startup.

## 1.2.0 — Winter World and Nether Rework

- Converted every newly generated Overworld biome region to Snowy Taiga or Taiga
  without adding a separate world type.
- Added a real cuboid Ushanka renderer that follows the player's head when worn.
- Fixed the Samovar hiding adjacent block faces and rebuilt its geometry and brass
  textures.
- Made the Bear Bell a placeable block, added it to a rare Bear Shrine structure,
  and converted village bells when players approach.
- Expanded cold and resource generation across every vanilla Nether biome, with
  Crimson Forests acting as the warm route and Fire Resistance providing warmth.
- Removed the redundant custom Ender Dragon advancement; vanilla progression owns
  that milestone.

## 1.1.0 — Universal Progression Update

- Removed the separate Russian Winter world preset; mechanics now work in ordinary
  new and existing worlds, with new content appearing in newly generated chunks.
- Fixed inverted controls lingering after Drunk expires; Hangover now starts at the
  same transition and has its own status icon.
- Added status icons for Drunk and Hangover.
- Recolored polar-bear rendering to brown without changing the Bear Fur item texture.
- Increased bear spawns across varied Overworld biomes and added rare Frostback and
  Snowstalker variants.
- Increased village frequency, added themed village loot, and injected a compact
  banya with a samovar into village house pools.
- Added Siberian Inferno to ordinary Nether biome generation.
- Rebuilt the Ushanka item and Samovar as cuboid 3D models.
- Added the Bear Bell as a risky recording-friendly item that provokes nearby bears.

## 1.0.0

- Initial playable release.
