package com.anatolyonhardmode.russiansurvival.bear;

import com.anatolyonhardmode.russiansurvival.config.ServerConfig;
import com.anatolyonhardmode.russiansurvival.cold.ColdSystem;
import com.anatolyonhardmode.russiansurvival.registry.ModItems;
import com.anatolyonhardmode.russiansurvival.sound.ModSounds;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.AABB;

public final class BearSystem {
    public static void initialize() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.SNOWY_TAIGA, Biomes.TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.SNOWY_PLAINS),
                MobCategory.CREATURE, EntityType.POLAR_BEAR, ServerConfig.values.polarBearSpawnWeight, 1, 2);
        LootTableEvents.MODIFY.register((key, table, source, registries) -> {
            if (source.isBuiltin() && key.equals(EntityType.POLAR_BEAR.getDefaultLootTable())) {
                table.pool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.BEAR_FUR)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))).build());
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(level -> {
            if (level.getGameTime() % 20 != 0 || !ColdSystem.isRussianWinter(level)) return;
            double radius = ServerConfig.values.polarBearAggressionRadius;
            for (Player player : level.players()) {
                AABB box = player.getBoundingBox().inflate(radius);
                int seen = 0;
                for (PolarBear bear : level.getEntitiesOfClass(PolarBear.class, box, PolarBear::isAlive)) {
                    if (++seen > ServerConfig.values.maxPolarBearsNearPlayer) break;
                    if (bear.getTarget() == null && bear.hasLineOfSight(player)) {
                        bear.setTarget(player);
                        bear.playSound(ModSounds.BEAR_WARNING, 1.0F, 0.85F + bear.getRandom().nextFloat() * 0.2F);
                    }
                    if (bear.getTarget() == player && bear.getRandom().nextInt(16) == 0) bear.playSound(ModSounds.BEAR_STEP, 0.45F, 0.9F + bear.getRandom().nextFloat() * 0.15F);
                }
            }
        });
    }
    private BearSystem() {}
}
