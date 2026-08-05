package com.anatolyonhardmode.russiansurvival.mixin.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void russianSurvival$limitBlizzardVisibility(Camera camera, FogRenderer.FogMode fogMode,
                                                                 float viewDistance, boolean thickFog,
                                                                 float partialTick, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null
                || client.level.dimension() != Level.OVERWORLD || !client.level.isThundering()
                || camera.getFluidInCamera() != FogType.NONE
                || client.player.hasEffect(MobEffects.BLINDNESS)
                || client.player.hasEffect(MobEffects.DARKNESS)) return;

        float pulse = 17.5F + (float) Math.sin((client.level.getGameTime() + partialTick) * 0.045F) * 2.0F;
        RenderSystem.setShaderFogStart(fogMode == FogRenderer.FogMode.FOG_SKY ? 0.0F : 1.5F);
        RenderSystem.setShaderFogEnd(pulse);
        RenderSystem.setShaderFogShape(FogShape.SPHERE);
    }
}
