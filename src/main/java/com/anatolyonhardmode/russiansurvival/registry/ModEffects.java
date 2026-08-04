package com.anatolyonhardmode.russiansurvival.registry;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import com.anatolyonhardmode.russiansurvival.effect.DrunkEffect;
import com.anatolyonhardmode.russiansurvival.effect.HangoverEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public final class ModEffects {
    public static final Holder<MobEffect> DRUNK = register("drunk", new DrunkEffect());
    public static final Holder<MobEffect> HANGOVER = register("hangover", new HangoverEffect());

    private static Holder<MobEffect> register(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
                ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, name), effect);
    }

    public static void initialize() {}
    private ModEffects() {}
}
