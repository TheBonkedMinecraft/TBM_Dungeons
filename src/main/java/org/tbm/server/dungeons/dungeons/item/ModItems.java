package org.tbm.server.dungeons.dungeons.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.tbm.server.dungeons.dungeons.Dungeons;


public class ModItems {
    //public static final Item DUNGEON_CRYSTAL = registerItem("dungeon_crystal",
    //        new Item(new FabricItemSettings().fireproof().group(ItemGroup.MATERIALS).maxCount(1).rarity(Rarity.EPIC)));
    //public static final Item BUILDER_POUCH = registerItem("builder_pouch", new Item(new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS).maxCount(1).rarity(Rarity.COMMON)));
    public static final Item HEART_OF_ATTRACTION = registerItem("heart_of_attraction",
            new Item(new FabricItemSettings().fireproof().group(ItemGroup.MATERIALS).maxCount(64).rarity(Rarity.RARE).food(FoodComponents.GOLDEN_APPLE)));
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Dungeons.MOD_ID, name), item);
    }
    public static void registerModItems() {}
}