package com.anatolyonhardmode.russiansurvival.worldgen;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import com.anatolyonhardmode.russiansurvival.config.ServerConfig;
import net.fabricmc.fabric.api.biome.v1.NetherBiomes;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class ModBiomes {
    public static final ResourceKey<Biome> SIBERIAN_INFERNO = ResourceKey.create(
            Registries.BIOME, ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "siberian_inferno"));
    private static final ResourceKey<PlacedFeature> FROZEN_BLACKSTONE_PATCH = placed("frozen_blackstone_patch");
    private static final ResourceKey<PlacedFeature> NETHER_PERMAFROST_PATCH = placed("nether_permafrost_patch");

    public static void initialize() {
        WinterWorldSystem.initialize();
        NetherWinterSystem.initialize();
        if (ServerConfig.values.enableSiberianInferno) {
            NetherBiomes.addNetherBiome(SIBERIAN_INFERNO,
                    Climate.parameters(-0.85F, 0.65F, 0.0F, 0.0F, 0.0F, -0.25F, 0.0F));
        }
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.NETHER_WASTES, Biomes.BASALT_DELTAS),
                GenerationStep.Decoration.UNDERGROUND_ORES, FROZEN_BLACKSTONE_PATCH);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.NETHER_WASTES,
                        Biomes.SOUL_SAND_VALLEY, Biomes.BASALT_DELTAS,
                        Biomes.WARPED_FOREST, Biomes.CRIMSON_FOREST),
                GenerationStep.Decoration.UNDERGROUND_ORES, NETHER_PERMAFROST_PATCH);
    }

    private static ResourceKey<PlacedFeature> placed(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, name));
    }
    private ModBiomes() {}
}
