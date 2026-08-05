package com.anatolyonhardmode.russiansurvival.sound;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class ModSounds {
    public static final SoundEvent WIND_1 = register("ambient.wind_1");
    public static final SoundEvent WIND_2 = register("ambient.wind_2");
    public static final SoundEvent TEETH_1 = register("player.teeth_1");
    public static final SoundEvent TEETH_2 = register("player.teeth_2");
    public static final SoundEvent BREATH = register("player.cold_breath");
    public static final SoundEvent BEAR_WARNING = register("entity.polar_bear.warning");
    public static final SoundEvent BEAR_STEP = register("entity.polar_bear.snow_step");
    public static final SoundEvent VODKA_OPEN = register("item.vodka.open");
    public static final SoundEvent VODKA_DRINK = register("item.vodka.drink");
    public static final SoundEvent DRUNK_STING = register("effect.drunk_sting");
    public static final SoundEvent BOTTLE_CLINK = register("item.vodka.clink");
    public static final SoundEvent SAMOVAR_BOIL = register("block.samovar.boil");
    public static final SoundEvent SAMOVAR_WHISTLE = register("block.samovar.whistle");

    private static SoundEvent register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
    public static void initialize() {}
    private ModSounds() {}
}
