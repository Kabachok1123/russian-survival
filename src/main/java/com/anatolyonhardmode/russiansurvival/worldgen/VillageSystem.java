package com.anatolyonhardmode.russiansurvival.worldgen;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import com.anatolyonhardmode.russiansurvival.mixin.StructureTemplatePoolAccessor;
import com.anatolyonhardmode.russiansurvival.registry.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.block.Blocks;
import com.anatolyonhardmode.russiansurvival.registry.ModBlocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public final class VillageSystem {
    private static final String[] HOUSE_POOLS = {
            "village/plains/houses", "village/desert/houses", "village/savanna/houses",
            "village/snowy/houses", "village/taiga/houses"
    };

    public static void initialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Registry<StructureTemplatePool> pools = server.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
            for (String path : HOUSE_POOLS) addBanya(pools, path);
        });

        LootTableEvents.MODIFY.register((key, table, source, registries) -> {
            String path = key.location().getPath();
            if (!source.isBuiltin() || !path.startsWith("chests/village/")) return;
            table.pool(LootPool.lootPool()
                    .setRolls(UniformGenerator.between(0, 2))
                    .add(LootItem.lootTableItem(ModItems.HOT_TEA).setWeight(5))
                    .add(LootItem.lootTableItem(ModItems.BEAR_FUR).setWeight(2)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                    .build());
        });

        ServerTickEvents.END_WORLD_TICK.register(level -> {
            if (level.dimension() != net.minecraft.world.level.Level.OVERWORLD || level.getGameTime() % 200 != 0) return;
            for (net.minecraft.world.entity.player.Player player : level.players()) {
                net.minecraft.core.BlockPos center = player.blockPosition();
                for (net.minecraft.core.BlockPos pos : net.minecraft.core.BlockPos.betweenClosed(
                        center.offset(-32, -10, -32), center.offset(32, 10, 32))) {
                    if (level.getBlockState(pos).is(Blocks.BELL) && level.isVillage(pos)) {
                        level.setBlock(pos, ModBlocks.BEAR_BELL.defaultBlockState(), 3);
                    }
                }
            }
        });
    }

    private static void addBanya(Registry<StructureTemplatePool> pools, String path) {
        StructureTemplatePool pool = pools.get(ResourceLocation.withDefaultNamespace(path));
        if (pool == null) {
            RussianSurvival.LOGGER.warn("Village pool {} was not found", path);
            return;
        }
        StructurePoolElement banya = StructurePoolElement
                .single(RussianSurvival.MOD_ID + ":village/banya")
                .apply(StructureTemplatePool.Projection.RIGID);
        StructureTemplatePoolAccessor accessor = (StructureTemplatePoolAccessor) pool;
        int weight = 8;
        for (int i = 0; i < weight; i++) accessor.russianSurvival$getTemplates().add(banya);
    }

    private VillageSystem() {}
}
