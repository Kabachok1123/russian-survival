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
import net.minecraft.resources.ResourceKey;
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

public final class ColdSystem {
    private static final ResourceKey<Biome> SIBERIAN_INFERNO = ResourceKey.create(
            net.minecraft.core.registries.Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath("russian_survival", "siberian_inferno"));

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTickCount() % 20 != 0) return;
            for (ServerPlayer player : server.getPlayerList().getPlayers()) tickPlayer(player);
            ServerLevel overworld = server.overworld();
            if (overworld.getGameTime() % 24000 == 0 && overworld.random.nextDouble() < ServerConfig.values.snowChancePerDay) {
                overworld.setWeatherParameters(0, 20 * 60 * (3 + overworld.random.nextInt(5)), true, overworld.random.nextInt(5) == 0);
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

        boolean coldBiome = isColdBiome(player);
        float cold = data.russianSurvival$getCold();
        if (coldBiome) {
            double change = calculateColdChange(player);
            cold = (float) Math.max(0, Math.min(100, cold + change));
            data.russianSurvival$setCold(cold);
        } else if (player.level().dimension() != Level.NETHER) {
            data.russianSurvival$setCold(cold = Math.max(0, cold - 0.35F));
        }

        updateThresholdEffects(player, cold);
        if (cold >= 40) award(player, "welcome_to_siberia", "cold_40");
        updateIntoxication(player, data);
        sync(player, data);
    }

    private static boolean isColdBiome(ServerPlayer player) {
        Holder<Biome> biome = player.level().getBiome(player.blockPosition());
        return player.level().dimension() == Level.OVERWORLD
                || biome.is(SIBERIAN_INFERNO);
    }

    private static double calculateColdChange(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        BlockPos pos = player.blockPosition();
        double gain = ServerConfig.values.baseColdGainPerSecond;
        float biomeTemperature = level.getBiome(pos).value().getBaseTemperature();
        if (biomeTemperature <= 0.2F) gain *= 1.35;
        else if (biomeTemperature >= 1.5F) gain *= 0.35;
        else if (biomeTemperature >= 0.9F) gain *= 0.7;
        boolean outside = level.canSeeSky(pos.above());
        if (!outside) gain *= 0.4;
        if (pos.getY() < level.getSeaLevel() - 25) gain -= 0.38;
        if (!level.isDay() && outside) gain *= 1.35;
        if (level.isRainingAt(pos)) gain *= level.isThundering() ? ServerConfig.values.snowstormMultiplier : ServerConfig.values.snowMultiplier;
        if (player.isInWaterOrRain()) gain *= ServerConfig.values.waterMultiplier;
        if (pos.getY() > 120) gain *= 1.0 + Math.min(0.75, (pos.getY() - 120) / 120.0);
        if (player.isSprinting()) gain *= 0.82;
        gain *= 1.0 - insulation(player);
        if (player.hasEffect(ModEffects.DRUNK)) gain = Math.min(gain, -0.25);
        gain -= nearbyHeat(level, pos);
        return gain;
    }

    private static double insulation(ServerPlayer player) {
        double insulation = 0;
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack.is(ModItems.USHANKA)) insulation += ServerConfig.values.ushankaInsulation;
            else if (stack.is(ModItems.FUR_COAT)) insulation += ServerConfig.values.furCoatInsulation;
            else if (stack.getItem() instanceof ArmorItem armor && armor.getMaterial() == net.minecraft.world.item.ArmorMaterials.LEATHER) insulation += ServerConfig.values.leatherPieceInsulation;
        }
        return Math.min(0.82, insulation);
    }

    private static double nearbyHeat(ServerLevel level, BlockPos center) {
        int radius = Math.max(1, Math.min(6, ServerConfig.values.heatSourceRadius));
        double best = 0;
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -2, -radius), center.offset(radius, 2, radius))) {
            BlockState state = level.getBlockState(pos);
            double strength = heatStrength(state);
            if (strength <= 0) continue;
            double distance = Math.sqrt(pos.distSqr(center));
            best = Math.max(best, strength * Math.max(0.15, 1.0 - distance / (radius + 1.0)));
        }
        return best * ServerConfig.values.heatSourceStrength;
    }

    private static double heatStrength(BlockState state) {
        if (state.is(ModBlocks.SAMOVAR) && state.hasProperty(com.anatolyonhardmode.russiansurvival.block.SamovarBlock.LIT) && state.getValue(com.anatolyonhardmode.russiansurvival.block.SamovarBlock.LIT)) return 1.25;
        if (state.is(Blocks.LAVA) || state.is(Blocks.FIRE)) return 1.0;
        if (state.is(Blocks.CAMPFIRE)) return lit(state) ? 1.0 : 0;
        if (state.is(Blocks.SOUL_CAMPFIRE) || state.is(Blocks.SOUL_FIRE)) return lit(state) ? 0.65 : 0;
        if (state.is(Blocks.FURNACE) || state.is(Blocks.BLAST_FURNACE) || state.is(Blocks.SMOKER)) return lit(state) ? 0.9 : 0;
        if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH) || state.is(Blocks.SOUL_TORCH) || state.is(Blocks.SOUL_WALL_TORCH)) return 0.08;
        return 0;
    }

    private static boolean lit(BlockState state) {
        return !state.hasProperty(BlockStateProperties.LIT) || state.getValue(BlockStateProperties.LIT);
    }

    private static void updateThresholdEffects(ServerPlayer player, float cold) {
        if (cold >= 60) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, cold >= 90 ? 2 : cold >= 75 ? 1 : 0, true, false, true));
        if (cold >= 75) player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, true, false, true));
        if (cold >= 90) player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0, true, false, true));
        if (cold >= 100 && player.tickCount % Math.max(20, ServerConfig.values.freezeDamageIntervalTicks) == 0) player.hurt(player.damageSources().freeze(), 1.0F);
        if (cold >= 60 && player.getRandom().nextInt(4) == 0) {
            player.serverLevel().sendParticles(ParticleTypes.CLOUD, player.getX(), player.getEyeY(), player.getZ(), 2, 0.15, 0.08, 0.15, 0.01);
        }
        if (cold >= 40 && player.getRandom().nextInt(20) == 0) {
            player.serverLevel().playSound(null, player.blockPosition(), player.getRandom().nextBoolean() ? ModSounds.TEETH_1 : ModSounds.TEETH_2, SoundSource.PLAYERS, 0.45F, 0.95F + player.getRandom().nextFloat() * 0.1F);
        }
        if (cold >= 60 && player.getRandom().nextInt(14) == 0) player.playSound(ModSounds.BREATH, 0.35F, 1.0F);
        if (coldBiomeSound(player) && player.getRandom().nextInt(35) == 0) player.playSound(player.getRandom().nextBoolean() ? ModSounds.WIND_1 : ModSounds.WIND_2, 0.28F, 0.9F + player.getRandom().nextFloat() * 0.2F);
    }

    private static boolean coldBiomeSound(ServerPlayer player) { return isColdBiome(player) && player.level().canSeeSky(player.blockPosition().above()); }

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
