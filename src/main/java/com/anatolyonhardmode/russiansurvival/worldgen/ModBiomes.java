package com.anatolyonhardmode.russiansurvival.worldgen;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class ModBiomes {
    private static final ResourceKey<PlacedFeature> FROZEN_BLACKSTONE_PATCH = placed("frozen_blackstone_patch");
    private static final ResourceKey<PlacedFeature> NETHER_PERMAFROST_PATCH = placed("nether_permafrost_patch");
    private static final ResourceKey<PlacedFeature> PACKED_ICE_PATCH = placed("nether_packed_ice_patch");
    private static final ResourceKey<PlacedFeature> BLUE_ICE_PATCH = placed("nether_blue_ice_patch");
    private static final ResourceKey<PlacedFeature> FROZEN_OBSIDIAN_LAKE = placed("frozen_obsidian_lake");

    public static void initialize() {
        WinterWorldSystem.initialize();
        NetherWinterSystem.initialize();
        var vanillaNether = BiomeSelectors.includeByKey(Biomes.NETHER_WASTES,
                Biomes.SOUL_SAND_VALLEY, Biomes.BASALT_DELTAS,
                Biomes.WARPED_FOREST, Biomes.CRIMSON_FOREST);
        BiomeModifications.addFeature(vanillaNether,
                GenerationStep.Decoration.UNDERGROUND_ORES, FROZEN_BLACKSTONE_PATCH);
        BiomeModifications.addFeature(vanillaNether,
                GenerationStep.Decoration.UNDERGROUND_ORES, NETHER_PERMAFROST_PATCH);
        BiomeModifications.addFeature(vanillaNether,
                GenerationStep.Decoration.UNDERGROUND_ORES, PACKED_ICE_PATCH);
        BiomeModifications.addFeature(vanillaNether,
                GenerationStep.Decoration.UNDERGROUND_ORES, BLUE_ICE_PATCH);
        BiomeModifications.addFeature(vanillaNether,
                GenerationStep.Decoration.LAKES, FROZEN_OBSIDIAN_LAKE);
    }

    private static ResourceKey<PlacedFeature> placed(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, name));
    }
    private ModBiomes() {}
}
