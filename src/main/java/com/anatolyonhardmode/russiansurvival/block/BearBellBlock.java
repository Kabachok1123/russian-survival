package com.anatolyonhardmode.russiansurvival.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class BearBellBlock extends Block {
    public BearBellBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        level.playSound(player, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 0.72F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.0,
                    pos.getZ() + 0.5, 8, 0.35, 0.25, 0.35, 0.1);
            for (PolarBear bear : level.getEntitiesOfClass(PolarBear.class,
                    new net.minecraft.world.phys.AABB(pos).inflate(48.0), PolarBear::isAlive)) {
                bear.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, false, false));
                bear.setTarget(player);
            }
            player.getCooldowns().addCooldown(asItem(), 100);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
