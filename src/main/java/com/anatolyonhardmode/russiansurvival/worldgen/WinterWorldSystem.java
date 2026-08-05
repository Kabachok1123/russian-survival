package com.anatolyonhardmode.russiansurvival.worldgen;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class WinterWorldSystem {
    private static final ResourceKey<PlacedFeature> FREEZE_TOP = mod("winter_freeze_top");

    public static void initialize() {
        BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "winter_weather"))
                .add(ModificationPhase.POST_PROCESSING, BiomeSelectors.foundInOverworld(), context -> {
                    context.getWeather().setPrecipitation(true);
                    context.getWeather().setTemperature(-0.35F);
                    context.getWeather().setDownfall(0.85F);
                    context.getEffects().setSkyColor(0x8FAFCB);
                    context.getEffects().setFogColor(0xC7D8E1);
                    context.getEffects().setWaterColor(0x355F8A);
                    context.getEffects().setWaterFogColor(0x183B59);
                    context.getEffects().setGrassColor(0x91A889);
                    context.getEffects().setFoliageColor(0x78936F);
                });

        BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "dark_winter_regions"))
                .add(ModificationPhase.POST_PROCESSING,
                        BiomeSelectors.includeByKey(Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.JUNGLE,
                                Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.DARK_FOREST), context -> {
                            context.getEffects().setFogColor(0x879BA3);
                            context.getEffects().setGrassColor(0x536C5D);
                            context.getEffects().setFoliageColor(0x486353);
                            context.getEffects().setWaterColor(0x294F5D);
                        });

        BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "frozen_steppe_regions"))
                .add(ModificationPhase.POST_PROCESSING,
                        BiomeSelectors.includeByKey(Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU,
                                Biomes.WINDSWEPT_SAVANNA, Biomes.BADLANDS, Biomes.ERODED_BADLANDS,
                                Biomes.WOODED_BADLANDS), context -> {
                            context.getEffects().setFogColor(0xD4D2C8);
                            context.getEffects().setGrassColor(0xB5B59A);
                            context.getEffects().setFoliageColor(0x9EA58A);
                        });

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION, FREEZE_TOP);
    }

    private static ResourceKey<PlacedFeature> mod(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, path));
    }

    private WinterWorldSystem() {}
}
