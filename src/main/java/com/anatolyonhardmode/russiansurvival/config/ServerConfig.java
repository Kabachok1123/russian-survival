package com.anatolyonhardmode.russiansurvival.config;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ServerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("russian_survival-server.json");
    public static Values values = new Values();

    public static void load() {
        try {
            if (Files.exists(PATH)) values = GSON.fromJson(Files.readString(PATH), Values.class);
            if (values == null) values = new Values();
            if (values.configVersion < 2) {
                if (values.polarBearSpawnWeight == 22 && values.polarBearAggressionRadius == 18
                        && values.maxPolarBearsNearPlayer == 8) {
                    values.polarBearSpawnWeight = 38;
                    values.polarBearAggressionRadius = 20;
                    values.maxPolarBearsNearPlayer = 12;
                }
                values.configVersion = 2;
            }
        } catch (Exception exception) {
            RussianSurvival.LOGGER.warn("Invalid server config; restoring safe defaults", exception);
            values = new Values();
        }
        save();
    }

    private static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(values));
        } catch (IOException exception) {
            RussianSurvival.LOGGER.error("Could not write server config", exception);
        }
    }

    public static final class Values {
        public int configVersion = 2;
        public boolean enableColdSystem = true;
        public double startingCold = 20.0;
        public double baseColdGainPerSecond = 0.44;
        public double snowMultiplier = 1.5;
        public double snowstormMultiplier = 2.0;
        public double waterMultiplier = 2.5;
        public int heatSourceRadius = 4;
        public double heatSourceStrength = 1.8;
        public int freezeDamageIntervalTicks = 40;
        public double leatherPieceInsulation = 0.08;
        public double ushankaInsulation = 0.33;
        public int polarBearSpawnWeight = 38;
        public int polarBearAggressionRadius = 20;
        public int maxPolarBearsNearPlayer = 12;
        public int vodkaPositiveDurationTicks = 900;
        public int vodkaPositiveAmplifier = 1;
        public int drunkDurationTicks = 900;
        public int hangoverDurationTicks = 1200;
        public int abandonedBanyaSpacing = 42;
        public double snowChancePerDay = 0.45;
    }

    private ServerConfig() {}
}
