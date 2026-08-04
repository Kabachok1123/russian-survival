package com.anatolyonhardmode.russiansurvival.config;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("russian_survival-client.json");
    public static Values values = new Values();

    public static void load() {
        try {
            if (Files.exists(PATH)) values = GSON.fromJson(Files.readString(PATH), Values.class);
            if (values == null) values = new Values();
        } catch (Exception exception) {
            RussianSurvival.LOGGER.warn("Invalid client config; restoring defaults", exception);
            values = new Values();
        }
        try { Files.createDirectories(PATH.getParent()); Files.writeString(PATH, GSON.toJson(values)); }
        catch (Exception exception) { RussianSurvival.LOGGER.error("Could not write client config", exception); }
    }

    public static final class Values {
        public boolean showColdHud = true;
        public int hudOffsetX = 0;
        public int hudOffsetY = 0;
        public boolean reducedNausea = false;
        public boolean disableCameraRotation = false;
        public boolean disableInputInversion = false;
        public float coldVignetteIntensity = 1.0F;
        public float extraAmbientSoundVolume = 0.65F;
    }
    private ClientConfig() {}
}
