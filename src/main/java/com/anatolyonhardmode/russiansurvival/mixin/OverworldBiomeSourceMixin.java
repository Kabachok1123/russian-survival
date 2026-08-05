package com.anatolyonhardmode.russiansurvival.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiNoiseBiomeSource.class)
public abstract class OverworldBiomeSourceMixin {
    @Inject(method = "getNoiseBiome", at = @At("RETURN"), cancellable = true)
    private void russianSurvival$replaceOverworldBiomes(int quartX, int quartY, int quartZ,
                                                        Climate.Sampler sampler,
                                                        CallbackInfoReturnable<Holder<Biome>> cir) {
        BiomeSource source = (BiomeSource) (Object) this;
        Holder<Biome> snowyTaiga = source.possibleBiomes().stream()
                .filter(holder -> holder.is(Biomes.SNOWY_TAIGA)).findFirst().orElse(null);
        Holder<Biome> taiga = source.possibleBiomes().stream()
                .filter(holder -> holder.is(Biomes.TAIGA)).findFirst().orElse(null);
        if (snowyTaiga == null || taiga == null) return;

        long regionHash = (quartX >> 6) * 73428767L ^ (quartZ >> 6) * 912931L;
        cir.setReturnValue(Math.floorMod(regionHash, 4) == 0 ? taiga : snowyTaiga);
    }
}
