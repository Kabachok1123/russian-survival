package com.anatolyonhardmode.russiansurvival.worldgen;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import com.anatolyonhardmode.russiansurvival.config.ServerConfig;
import net.fabricmc.fabric.api.biome.v1.NetherBiomes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

public final class ModBiomes {
    public static final ResourceKey<Biome> SIBERIAN_INFERNO = ResourceKey.create(
            Registries.BIOME, ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "siberian_inferno"));

    public static void initialize() {
        if (!ServerConfig.values.enableSiberianInferno) return;
        NetherBiomes.addNetherBiome(SIBERIAN_INFERNO,
                Climate.parameters(-0.85F, 0.65F, 0.0F, 0.0F, 0.0F, -0.25F, 0.0F));
    }
    private ModBiomes() {}
}
