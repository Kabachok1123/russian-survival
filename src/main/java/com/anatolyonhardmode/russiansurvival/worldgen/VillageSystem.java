package com.anatolyonhardmode.russiansurvival.worldgen;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import com.anatolyonhardmode.russiansurvival.mixin.StructureTemplatePoolAccessor;
import com.anatolyonhardmode.russiansurvival.registry.ModItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
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
        int weight = 3;
        for (int i = 0; i < weight; i++) accessor.russianSurvival$getTemplates().add(banya);
    }

    private VillageSystem() {}
}
