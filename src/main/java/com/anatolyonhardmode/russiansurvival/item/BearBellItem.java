package com.anatolyonhardmode.russiansurvival.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class BearBellItem extends Item {
    public BearBellItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        level.playSound(player, player.blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, 1.5F, 0.75F);
        if (!level.isClientSide()) {
            for (PolarBear bear : level.getEntitiesOfClass(PolarBear.class,
                    player.getBoundingBox().inflate(48.0), PolarBear::isAlive)) {
                bear.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, false, false));
                bear.setTarget(player);
            }
            player.getCooldowns().addCooldown(this, 100);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
