package com.anatolyonhardmode.russiansurvival.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class ModNetworking {
    public static void initialize() {
        PayloadTypeRegistry.playS2C().register(ColdSyncPayload.TYPE, ColdSyncPayload.CODEC);
    }
    private ModNetworking() {}
}
