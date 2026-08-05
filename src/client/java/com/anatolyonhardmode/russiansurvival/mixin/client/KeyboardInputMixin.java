package com.anatolyonhardmode.russiansurvival.mixin.client;

import com.anatolyonhardmode.russiansurvival.client.ClientColdState;
import com.anatolyonhardmode.russiansurvival.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import com.anatolyonhardmode.russiansurvival.registry.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {
    @Inject(method = "tick", at = @At("TAIL"))
    private void russianSurvival$distortInput(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        if (ClientConfig.values.disableInputInversion) return;
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || !client.player.hasEffect(ModEffects.DRUNK)) return;
        int phase = (client.player.tickCount / 30) % 7;
        if (phase == 2 || phase == 5) leftImpulse = -leftImpulse;
        if (ClientColdState.intoxication >= 2 && phase == 4) forwardImpulse = -forwardImpulse;
        if (ClientColdState.intoxication >= 3 && client.player.tickCount % 43 == 0) jumping = true;
    }
}
