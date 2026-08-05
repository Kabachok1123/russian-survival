package com.anatolyonhardmode.russiansurvival;

import net.fabricmc.api.ModInitializer;
import com.anatolyonhardmode.russiansurvival.bear.BearSystem;
import com.anatolyonhardmode.russiansurvival.cold.ColdSystem;
import com.anatolyonhardmode.russiansurvival.config.ServerConfig;
import com.anatolyonhardmode.russiansurvival.network.ModNetworking;
import com.anatolyonhardmode.russiansurvival.registry.ModBlocks;
import com.anatolyonhardmode.russiansurvival.registry.ModEffects;
import com.anatolyonhardmode.russiansurvival.registry.ModItems;
import com.anatolyonhardmode.russiansurvival.sound.ModSounds;
import com.anatolyonhardmode.russiansurvival.worldgen.ModBiomes;
import com.anatolyonhardmode.russiansurvival.worldgen.VillageSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RussianSurvival implements ModInitializer {
    public static final String MOD_ID = "russian_survival";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ServerConfig.load();
        ModSounds.initialize();
        ModEffects.initialize();
        ModBlocks.initialize();
        ModItems.initialize();
        ModNetworking.initialize();
        ModBiomes.initialize();
        ColdSystem.initialize();
        BearSystem.initialize();
        VillageSystem.initialize();
        LOGGER.info("Russian Survival is bracing for winter.");
    }
}
