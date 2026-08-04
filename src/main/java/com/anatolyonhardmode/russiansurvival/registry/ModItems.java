package com.anatolyonhardmode.russiansurvival.registry;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import com.anatolyonhardmode.russiansurvival.item.BorschtItem;
import com.anatolyonhardmode.russiansurvival.item.HotTeaItem;
import com.anatolyonhardmode.russiansurvival.item.VodkaItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public final class ModItems {
    public static final Item BEAR_FUR = register("bear_fur", new Item(new Item.Properties()));
    public static final Item USHANKA = register("ushanka", new ArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties().durability(125)));
    public static final Item FUR_COAT = register("fur_coat", new ArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(180)));
    public static final Item POTATO_MASH = register("potato_mash", new Item(new Item.Properties().stacksTo(16)));
    public static final Item VODKA_BOTTLE = register("vodka_bottle", new VodkaItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(0).saturationModifier(0).alwaysEdible().build())));
    public static final Item HOT_TEA = register("hot_tea", new HotTeaItem(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).alwaysEdible().build())));
    public static final Item BORSCHT = register("borscht", new BorschtItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(10).saturationModifier(0.9F).build())));
    public static final Item SAMOVAR = register("samovar", new BlockItem(ModBlocks.SAMOVAR, new Item.Properties()));
    public static final Item FROZEN_BLACKSTONE = register("frozen_blackstone", new BlockItem(ModBlocks.FROZEN_BLACKSTONE, new Item.Properties()));
    public static final Item NETHER_PERMAFROST = register("nether_permafrost", new BlockItem(ModBlocks.NETHER_PERMAFROST, new Item.Properties()));

    private static Item register(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, name), item);
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> entries.accept(BEAR_FUR));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> { entries.accept(POTATO_MASH); entries.accept(VODKA_BOTTLE); entries.accept(HOT_TEA); entries.accept(BORSCHT); });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> { entries.accept(USHANKA); entries.accept(FUR_COAT); });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> entries.accept(SAMOVAR));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> { entries.accept(FROZEN_BLACKSTONE); entries.accept(NETHER_PERMAFROST); });
    }
    private ModItems() {}
}
