package com.anatolyonhardmode.russiansurvival.client;

import net.fabricmc.api.ClientModInitializer;
import com.anatolyonhardmode.russiansurvival.config.ClientConfig;
import com.anatolyonhardmode.russiansurvival.network.ColdSyncPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import com.anatolyonhardmode.russiansurvival.registry.ModEffects;

public final class RussianSurvivalClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientConfig.load();
        UshankaArmorRenderer.initialize();
        ClientPlayNetworking.registerGlobalReceiver(ColdSyncPayload.TYPE, (payload, context) -> {
            ClientColdState.cold = payload.cold();
            ClientColdState.intoxication = payload.intoxication();
        });
        HudRenderCallback.EVENT.register(ColdHud::render);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || ClientConfig.values.disableCameraRotation || ClientConfig.values.reducedNausea) return;
            if (client.player.hasEffect(ModEffects.DRUNK) && client.player.tickCount % 37 == 0) {
                float turn = (client.player.getRandom().nextFloat() - 0.5F) * 8.0F * ClientColdState.intoxication;
                client.player.setYRot(client.player.getYRot() + turn);
            }
        });
    }
}
