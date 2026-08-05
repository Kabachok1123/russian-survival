package com.anatolyonhardmode.russiansurvival.worldgen;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;

public final class NetherWinterSystem {
    public static void initialize() {
        BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "frozen_nether"))
                .add(ModificationPhase.POST_PROCESSING,
                        BiomeSelectors.includeByKey(Biomes.NETHER_WASTES, Biomes.SOUL_SAND_VALLEY,
                                Biomes.BASALT_DELTAS, Biomes.WARPED_FOREST, Biomes.CRIMSON_FOREST), context -> {
                            context.getEffects().setSkyColor(0x243744);
                            context.getEffects().setFogColor(0x587486);
                            context.getEffects().setWaterColor(0x244E67);
                            context.getEffects().setWaterFogColor(0x142E42);
                        });

        BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "soul_blizzard"))
                .add(ModificationPhase.POST_PROCESSING,
                        BiomeSelectors.includeByKey(Biomes.SOUL_SAND_VALLEY), context ->
                                context.getEffects().setFogColor(0x91AAB7));
        BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "basalt_frost"))
                .add(ModificationPhase.POST_PROCESSING,
                        BiomeSelectors.includeByKey(Biomes.BASALT_DELTAS), context ->
                                context.getEffects().setFogColor(0x405968));
        BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "warped_frost"))
                .add(ModificationPhase.POST_PROCESSING,
                        BiomeSelectors.includeByKey(Biomes.WARPED_FOREST), context ->
                                context.getEffects().setFogColor(0x326C7B));
        BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "crimson_refuge"))
                .add(ModificationPhase.POST_PROCESSING,
                        BiomeSelectors.includeByKey(Biomes.CRIMSON_FOREST), context ->
                                context.getEffects().setFogColor(0x694A57));
    }

    private NetherWinterSystem() {}
}
