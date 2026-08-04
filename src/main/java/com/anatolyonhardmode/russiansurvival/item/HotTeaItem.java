package com.anatolyonhardmode.russiansurvival.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class HotTeaItem extends WarmingFoodItem {
    public HotTeaItem(Properties properties) { super(properties, 18, false); }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide) entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 15, 0));
        return result;
    }
}
