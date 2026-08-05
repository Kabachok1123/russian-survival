package com.anatolyonhardmode.russiansurvival.cold;

import com.anatolyonhardmode.russiansurvival.config.ServerConfig;
import com.anatolyonhardmode.russiansurvival.network.ColdSyncPayload;
import com.anatolyonhardmode.russiansurvival.registry.ModBlocks;
import com.anatolyonhardmode.russiansurvival.registry.ModEffects;
import com.anatolyonhardmode.russiansurvival.registry.ModItems;
import com.anatolyonhardmode.russiansurvival.sound.ModSounds;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;

public final class ColdSystem {
    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTickCount() % 20 != 0) return;
            for (ServerPlayer player : server.getPlayerList().getPlayers()) tickPlayer(player);
            ServerLevel overworld = server.overworld();
            boolean newDay = overworld.getGameTime() % 24000 < 20;
            if (!overworld.isRaining() || newDay) {
                boolean snowstorm = newDay && overworld.random.nextDouble() < ServerConfig.values.snowstormChancePerDay;
                overworld.setWeatherParameters(0, 24000 * 2, true, snowstorm);
            }
        });
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            PlayerSurvivalData oldData = (PlayerSurvivalData) oldPlayer;
            PlayerSurvivalData newData = (PlayerSurvivalData) newPlayer;
            newData.russianSurvival$setCold(alive ? oldData.russianSurvival$getCold() : (float) ServerConfig.values.startingCold);
            newData.russianSurvival$setIntoxication(alive ? oldData.russianSurvival$getIntoxication() : 0);
            newData.russianSurvival$setIntoxicationCooldown(alive ? oldData.russianSurvival$getIntoxicationCooldown() : 0);
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                Commands.literal("russiansurvival").requires(source -> source.hasPermission(2))
                        .then(Commands.literal("cold")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0, 100))
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            ((PlayerSurvivalData) player).russianSurvival$setCold(FloatArgumentType.getFloat(context, "value"));
                                            tickPlayer(player);
                                            return 1;
                                        })))
                        .then(Commands.literal("reload_config").executes(context -> { ServerConfig.load(); return 1; }))));
    }

    private static void tickPlayer(ServerPlayer player) {
        PlayerSurvivalData data = (PlayerSurvivalData) player;
        if (!ServerConfig.values.enableColdSystem || player.isCreative() || player.isSpectator()) {
            sync(player, data);
            return;
        }

        float cold = data.russianSurvival$getCold();
        if (player.level().dimension() == Level.OVERWORLD) {
            double change = calculateOverworldColdChange(player);
            cold = (float) Math.max(0, Math.min(100, cold + change));
            data.russianSurvival$setCold(cold);
        } else if (player.level().dimension() == Level.NETHER) {
            double recovery = ServerConfig.values.netherColdRecoveryPerSecond
                    + nearbyHeat(player.serverLevel(), player.blockPosition(), true);
            if (player.hasEffect(MobEffects.FIRE_RESISTANCE)) recovery += 0.25;
            data.russianSurvival$setCold(cold = (float) Math.max(0, cold - recovery));
        } else {
            data.russianSurvival$setCold(cold = Math.max(0, cold - 0.35F));
        }

        updateThresholdEffects(player, cold);
        if (cold >= 40) award(player, "welcome_to_siberia", "cold_40");
        updateIntoxication(player, data);
        sync(player, data);
    }

    private static double calculateOverworldColdChange(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        BlockPos pos = player.blockPosition();
        double gain = ServerConfig.values.baseColdGainPerSecond;
        Holder<Biome> biome = level.getBiome(pos);
        float biomeTemperature = biome.value().getBaseTemperature();
        if (biomeTemperature <= 0.2F) gain *= 1.35;
        else if (biomeTemperature >= 1.5F) gain *= 0.35;
        else if (biomeTemperature >= 0.9F) gain *= 0.7;
        boolean outside = level.canSeeSky(pos.above());
        if (!outside) gain *= 0.4;
        if (pos.getY() < level.getSeaLevel() - 25) gain -= 0.38;
        if (!level.isDay() && outside) gain *= 1.35;
        if (level.isRainingAt(pos)) gain *= level.isThundering() ? ServerConfig.values.snowstormMultiplier : ServerConfig.values.snowMultiplier;
        if (player.isInWater()) gain *= ServerConfig.values.waterMultiplier;
        if (pos.getY() > 120) gain *= 1.0 + Math.min(0.75, (pos.getY() - 120) / 120.0);
        if (player.isSprinting()) gain *= 0.82;
        boolean inPowderSnow = touchingPowderSnow(level, pos);
        if (inPowderSnow) gain += ServerConfig.values.powderSnowColdGainPerSecond;
        gain *= 1.0 - insulation(player);
        if (player.hasEffect(ModEffects.DRUNK)) gain = Math.min(gain, -0.25);
        gain -= nearbyHeat(level, pos, false);
        if (!inPowderSnow && nearbyTorch(level, pos)) gain = Math.min(gain, -0.02);
        return gain;
    }

    private static boolean touchingPowderSnow(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.POWDER_SNOW)
                || level.getBlockState(pos.above()).is(Blocks.POWDER_SNOW)
                || level.getBlockState(pos.below()).is(Blocks.POWDER_SNOW);
    }

    private static double insulation(ServerPlayer player) {
        double insulation = 0;
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.is(ModItems.USHANKA)) insulation += ServerConfig.values.ushankaInsulation;
            else if (stack.getItem() instanceof ArmorItem armor && armor.getMaterial() == net.minecraft.world.item.ArmorMaterials.LEATHER) insulation += ServerConfig.values.leatherPieceInsulation;
        }
        return Math.min(0.82, insulation);
    }

    private static double nearbyHeat(ServerLevel level, BlockPos center, boolean ignoreLava) {
        int radius = Math.max(1, Math.min(6, ServerConfig.values.heatSourceRadius));
        double best = 0;
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -2, -radius), center.offset(radius, 2, radius))) {
            BlockState state = level.getBlockState(pos);
            double strength = heatStrength(state, ignoreLava);
            if (strength <= 0) continue;
            double distance = Math.sqrt(pos.distSqr(center));
            best = Math.max(best, strength * Math.max(0.15, 1.0 - distance / (radius + 1.0)));
        }
        return best * ServerConfig.values.heatSourceStrength;
    }

    private static boolean nearbyTorch(ServerLevel level, BlockPos center) {
        int radius = Math.max(1, Math.min(6, ServerConfig.values.heatSourceRadius));
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -2, -radius), center.offset(radius, 2, radius))) {
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH)
                    || state.is(Blocks.SOUL_TORCH) || state.is(Blocks.SOUL_WALL_TORCH)) return true;
        }
        return false;
    }

    private static double heatStrength(BlockState state, boolean ignoreLava) {
        if (state.is(ModBlocks.SAMOVAR) && state.hasProperty(com.anatolyonhardmode.russiansurvival.block.SamovarBlock.LIT) && state.getValue(com.anatolyonhardmode.russiansurvival.block.SamovarBlock.LIT)) return 2.0;
        if (state.is(Blocks.LAVA)) return ignoreLava ? 0 : 1.4;
        if (state.is(Blocks.FIRE)) return 1.8;
        if (state.is(Blocks.CAMPFIRE)) return lit(state) ? 1.8 : 0;
        if (state.is(Blocks.SOUL_CAMPFIRE) || state.is(Blocks.SOUL_FIRE)) return lit(state) ? 1.15 : 0;
        if (state.is(Blocks.FURNACE) || state.is(Blocks.BLAST_FURNACE) || state.is(Blocks.SMOKER)) return lit(state) ? 1.2 : 0;
        if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH) || state.is(Blocks.SOUL_TORCH) || state.is(Blocks.SOUL_WALL_TORCH)) return 0;
        return 0;
    }

    private static boolean lit(BlockState state) {
        return !state.hasProperty(BlockStateProperties.LIT) || state.getValue(BlockStateProperties.LIT);
    }

    private static void updateThresholdEffects(ServerPlayer player, float cold) {
        if (cold >= 60) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, cold >= 90 ? 2 : cold >= 75 ? 1 : 0, true, false, true));
        if (cold >= 75) player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, true, false, true));
        if (cold >= 90) player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0, true, false, true));
        int damageInterval = Math.max(20, ServerConfig.values.freezeDamageIntervalTicks);
        if (cold >= 100 && player.tickCount % damageInterval < 20) {
            player.hurt(player.damageSources().freeze(), freezeDamage(player.level().getDifficulty()));
        }
        if (cold >= 60 && player.getRandom().nextInt(4) == 0) {
            player.serverLevel().sendParticles(ParticleTypes.CLOUD, player.getX(), player.getEyeY(), player.getZ(), 2, 0.15, 0.08, 0.15, 0.01);
        }
        if (cold >= 40 && player.getRandom().nextInt(20) == 0) {
            player.serverLevel().playSound(null, player.blockPosition(), player.getRandom().nextBoolean() ? ModSounds.TEETH_1 : ModSounds.TEETH_2, SoundSource.PLAYERS, 0.45F, 0.95F + player.getRandom().nextFloat() * 0.1F);
        }
        if (cold >= 60 && player.getRandom().nextInt(14) == 0) player.playSound(ModSounds.BREATH, 0.35F, 1.0F);
        if (player.level().dimension() == Level.NETHER) {
            player.serverLevel().sendParticles(ParticleTypes.SNOWFLAKE,
                    player.getX(), player.getY() + 1.5, player.getZ(),
                    9, 5.0, 2.0, 5.0, 0.015);
        }
        if (coldBiomeSound(player) && player.getRandom().nextInt(35) == 0) player.playSound(player.getRandom().nextBoolean() ? ModSounds.WIND_1 : ModSounds.WIND_2, 0.28F, 0.9F + player.getRandom().nextFloat() * 0.2F);
    }

    private static boolean coldBiomeSound(ServerPlayer player) { return player.level().dimension() == Level.OVERWORLD && player.level().canSeeSky(player.blockPosition().above()); }

    private static float freezeDamage(Difficulty difficulty) {
        return switch (difficulty) {
            case PEACEFUL -> 0.5F;
            case EASY -> 1.0F;
            case NORMAL -> 2.0F;
            case HARD -> 3.0F;
        };
    }

    private static void updateIntoxication(ServerPlayer player, PlayerSurvivalData data) {
        if (player.hasEffect(ModEffects.DRUNK)) return;
        if (data.russianSurvival$getIntoxication() > 0) {
            int duration = ServerConfig.values.hangoverDurationTicks;
            player.addEffect(new MobEffectInstance(ModEffects.HANGOVER, duration, 0));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, 0));
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, duration, 0));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration, 0));
            data.russianSurvival$setIntoxication(0);
            data.russianSurvival$setIntoxicationCooldown(0);
        }
    }

    private static void sync(ServerPlayer player, PlayerSurvivalData data) {
        if (ServerPlayNetworking.canSend(player, ColdSyncPayload.TYPE)) ServerPlayNetworking.send(player, new ColdSyncPayload(data.russianSurvival$getCold(), data.russianSurvival$getIntoxication()));
    }

    public static void award(ServerPlayer player, String advancementName, String criterion) {
        AdvancementHolder advancement = player.server.getAdvancements().get(ResourceLocation.fromNamespaceAndPath("russian_survival", advancementName));
        if (advancement != null) player.getAdvancements().award(advancement, criterion);
    }

    private ColdSystem() {}
}
