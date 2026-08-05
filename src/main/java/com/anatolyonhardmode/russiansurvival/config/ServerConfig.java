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
        public double furCoatInsulation = 0.42;
        public int polarBearSpawnWeight = 22;
        public int polarBearAggressionRadius = 18;
        public int maxPolarBearsNearPlayer = 8;
        public int vodkaPositiveDurationTicks = 900;
        public int vodkaPositiveAmplifier = 1;
        public int drunkDurationTicks = 900;
        public int hangoverDurationTicks = 1200;
        public boolean enableSiberianInferno = true;
        public int abandonedBanyaSpacing = 42;
        public double snowChancePerDay = 0.45;
    }

    private ServerConfig() {}
}
