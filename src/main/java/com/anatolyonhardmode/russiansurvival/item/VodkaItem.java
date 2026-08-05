package com.anatolyonhardmode.russiansurvival.item;

import com.anatolyonhardmode.russiansurvival.cold.PlayerSurvivalData;
import com.anatolyonhardmode.russiansurvival.cold.ColdSystem;
import com.anatolyonhardmode.russiansurvival.config.ServerConfig;
import com.anatolyonhardmode.russiansurvival.registry.ModEffects;
import com.anatolyonhardmode.russiansurvival.sound.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public final class VodkaItem extends Item {
    public VodkaItem(Properties properties) { super(properties); }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            PlayerSurvivalData data = (PlayerSurvivalData) player;
            int intoxication = Math.min(3, data.russianSurvival$getIntoxication() + 1);
            data.russianSurvival$setIntoxication(intoxication);
            data.russianSurvival$setIntoxicationCooldown(20 * 120);
            data.russianSurvival$setCold(data.russianSurvival$getCold() - 20);
            int positive = ServerConfig.values.vodkaPositiveDurationTicks;
            int drunk = ServerConfig.values.drunkDurationTicks + (intoxication - 1) * 300;
            int amp = ServerConfig.values.vodkaPositiveAmplifier;
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, positive, amp));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, positive, amp));
            player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, positive, 3));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 15, 0));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, drunk, Math.min(1, intoxication - 1)));
            player.addEffect(new MobEffectInstance(ModEffects.DRUNK, drunk, intoxication - 1));
            if (intoxication == 3) player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 0));
            if (intoxication == 3) ColdSystem.award(player, "bad_decision", "intoxication_3");
            level.playSound(null, player.blockPosition(), ModSounds.DRUNK_STING, SoundSource.PLAYERS, 0.8F, 1.0F);
            level.playSound(null, player.blockPosition(), ModSounds.VODKA_DRINK, SoundSource.PLAYERS, 0.65F, 0.95F + player.getRandom().nextFloat() * 0.1F);
            if (!player.getAbilities().instabuild) {
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.getInventory().add(bottle)) player.drop(bottle, false);
            }
        }
        return result;
    }
}
