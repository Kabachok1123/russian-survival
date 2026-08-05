package com.anatolyonhardmode.russiansurvival.mixin.client;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PolarBear;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PolarBearRenderer.class)
public final class PolarBearRendererMixin {
    private static final ResourceLocation BROWN_BEAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "textures/entity/brown_bear.png");

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/PolarBear;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"), cancellable = true)
    private void russianSurvival$useBrownBearTexture(PolarBear bear,
                                                     CallbackInfoReturnable<ResourceLocation> cir) {
        cir.setReturnValue(BROWN_BEAR_TEXTURE);
    }
}
