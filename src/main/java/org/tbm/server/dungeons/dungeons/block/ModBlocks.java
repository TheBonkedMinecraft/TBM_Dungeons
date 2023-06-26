package org.tbm.server.dungeons.dungeons.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.tbm.server.dungeons.dungeons.Dungeons;
import org.tbm.server.dungeons.dungeons.block.custom.GemInfusingStationBlock;

public class ModBlocks {
    public static final Block GEM_INFUSING_STATION = registerBlock("gem_infusing_station",
            new GemInfusingStationBlock(FabricBlockSettings.of(Material.METAL)
                    .strength(4f).requiresTool().nonOpaque()), ItemGroup.MISC);


    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(Dungeons.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registry.ITEM, new Identifier(Dungeons.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {}
}