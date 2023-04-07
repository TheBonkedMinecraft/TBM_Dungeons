package org.tbm.server.dungeons.dungeons.world.dimension;

import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import org.tbm.server.dungeons.dungeons.item.ModItems;

public class ModPortals {
    public static void registerPortals(){
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.REINFORCED_DEEPSLATE)
                .destDimID(ModDimensions.DUNGEONS_KEY.getValue())
                .lightWithItem(ModItems.DUNGEON_CRYSTAL)
                .tintColor(255,105,97)
                .registerPortal();
    }
}
