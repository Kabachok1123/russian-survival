package com.anatolyonhardmode.russiansurvival.block;

import com.anatolyonhardmode.russiansurvival.registry.ModItems;
import com.anatolyonhardmode.russiansurvival.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public final class SamovarBlock extends Block {
    public static final BooleanProperty WATER = BooleanProperty.create("water");
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public SamovarBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(WATER, false).setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATER, LIT);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                               Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.is(Items.WATER_BUCKET) && !state.getValue(WATER)) {
            if (!player.getAbilities().instabuild) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            level.setBlock(pos, state.setValue(WATER, true), 3);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if ((stack.is(Items.COAL) || stack.is(Items.CHARCOAL)) && state.getValue(WATER) && !state.getValue(LIT)) {
            stack.consume(1, player);
            level.setBlock(pos, state.setValue(LIT, true), 3);
            level.scheduleTick(pos, this, 20 * 90);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (stack.is(Items.GLASS_BOTTLE) && state.getValue(WATER) && state.getValue(LIT)) {
            if (!level.isClientSide && consumeTeaIngredients(player)) {
                stack.shrink(1);
                ItemStack tea = new ItemStack(ModItems.HOT_TEA);
                if (!player.getInventory().add(tea)) player.drop(tea, false);
                level.playSound(null, pos, ModSounds.SAMOVAR_WHISTLE, SoundSource.BLOCKS, 0.65F, 1.05F);
                level.setBlock(pos, state.setValue(WATER, false), 3);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static boolean consumeTeaIngredients(Player player) {
        if (player.getAbilities().instabuild) return true;
        ItemStack sugar = ItemStack.EMPTY;
        ItemStack leaves = ItemStack.EMPTY;
        for (ItemStack candidate : player.getInventory().items) {
            if (sugar.isEmpty() && candidate.is(Items.SUGAR)) sugar = candidate;
            if (leaves.isEmpty() && candidate.is(net.minecraft.tags.ItemTags.LEAVES)) leaves = candidate;
        }
        if (sugar.isEmpty() || leaves.isEmpty()) return false;
        sugar.shrink(1);
        leaves.shrink(1);
        return true;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) level.setBlock(pos, state.setValue(LIT, false), 3);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;
        level.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 1.05, pos.getZ() + 0.5, 0, 0.025, 0);
        if (random.nextInt(45) == 0) level.playLocalSound(pos, ModSounds.SAMOVAR_BOIL, SoundSource.BLOCKS, 0.3F, 0.95F + random.nextFloat() * 0.1F, false);
    }
}
