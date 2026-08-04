package com.anatolyonhardmode.russiansurvival.mixin;

import com.anatolyonhardmode.russiansurvival.cold.PlayerSurvivalData;
import com.anatolyonhardmode.russiansurvival.config.ServerConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerDataMixin implements PlayerSurvivalData {
    @Unique private float russianSurvival$cold = (float) ServerConfig.values.startingCold;
    @Unique private int russianSurvival$intoxication;
    @Unique private int russianSurvival$intoxicationCooldown;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void russianSurvival$writeData(CompoundTag tag, CallbackInfo ci) {
        tag.putFloat("RussianSurvivalCold", russianSurvival$cold);
        tag.putInt("RussianSurvivalIntoxication", russianSurvival$intoxication);
        tag.putInt("RussianSurvivalIntoxicationCooldown", russianSurvival$intoxicationCooldown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void russianSurvival$readData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("RussianSurvivalCold")) russianSurvival$cold = tag.getFloat("RussianSurvivalCold");
        russianSurvival$intoxication = tag.getInt("RussianSurvivalIntoxication");
        russianSurvival$intoxicationCooldown = tag.getInt("RussianSurvivalIntoxicationCooldown");
    }

    @Override public float russianSurvival$getCold() { return russianSurvival$cold; }
    @Override public void russianSurvival$setCold(float value) { russianSurvival$cold = Math.max(0, Math.min(100, value)); }
    @Override public int russianSurvival$getIntoxication() { return russianSurvival$intoxication; }
    @Override public void russianSurvival$setIntoxication(int value) { russianSurvival$intoxication = Math.max(0, Math.min(3, value)); }
    @Override public int russianSurvival$getIntoxicationCooldown() { return russianSurvival$intoxicationCooldown; }
    @Override public void russianSurvival$setIntoxicationCooldown(int ticks) { russianSurvival$intoxicationCooldown = Math.max(0, ticks); }
}
