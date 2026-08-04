package com.anatolyonhardmode.russiansurvival.registry;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import com.anatolyonhardmode.russiansurvival.block.SamovarBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {
    public static final Block SAMOVAR = register("samovar", new SamovarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(3.5F).sound(SoundType.COPPER)));
    public static final Block FROZEN_BLACKSTONE = register("frozen_blackstone", new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(1.5F, 6.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final Block NETHER_PERMAFROST = register("nether_permafrost", new Block(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).strength(1.0F).friction(0.92F).sound(SoundType.GLASS)));

    private static Block register(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, name), block);
    }
    public static void initialize() {}
    private ModBlocks() {}
}
