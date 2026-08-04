package com.anatolyonhardmode.russiansurvival.item;

import com.anatolyonhardmode.russiansurvival.cold.PlayerSurvivalData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WarmingFoodItem extends Item {
    private final float warmth;
    private final boolean returnBowl;

    public WarmingFoodItem(Properties properties, float warmth, boolean returnBowl) {
        super(properties);
        this.warmth = warmth;
        this.returnBowl = returnBowl;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            PlayerSurvivalData data = (PlayerSurvivalData) player;
            data.russianSurvival$setCold(data.russianSurvival$getCold() - warmth);
        }
        if (returnBowl && result.isEmpty()) return new ItemStack(net.minecraft.world.item.Items.BOWL);
        return result;
    }
}
