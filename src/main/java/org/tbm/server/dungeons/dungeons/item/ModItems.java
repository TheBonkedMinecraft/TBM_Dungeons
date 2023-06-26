package org.tbm.server.dungeons.dungeons.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.tbm.server.dungeons.dungeons.Dungeons;
import org.tbm.server.dungeons.dungeons.gear.tier1.Helmet;


public class ModItems {
    public static final Item DUNGEON_CRYSTAL = registerItem("dungeon_crystal",
            new Item(new FabricItemSettings().fireproof().group(ItemGroup.MATERIALS).maxCount(1).rarity(Rarity.EPIC)));
    public static final Item HEART_OF_ATTRACTION = registerItem("heart_of_attraction",
            new Item(new FabricItemSettings().fireproof().group(ItemGroup.MATERIALS).maxCount(64).rarity(Rarity.RARE).food(FoodComponents.GOLDEN_APPLE)));

    public static final Item MYTHRIL_HELMET = registerItem("mythril_helmet", new Helmet(10));
    public static final Item MYTHRIL_CHESTPLATE = registerItem("mythril_chestplate",
            new ArmorItem(ModArmorMaterials.MYTHRIL, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item MYTHRIL_LEGGINGS = registerItem("mythril_leggings",
            new ArmorItem(ModArmorMaterials.MYTHRIL, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));
    public static final Item MYTHRIL_BOOTS = registerItem("mythril_boots",
            new ArmorItem(ModArmorMaterials.MYTHRIL, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ItemGroup.COMBAT)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Dungeons.MOD_ID, name), item);
    }
    public static void registerModItems() {
    }
}