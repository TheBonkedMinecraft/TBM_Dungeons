package org.tbm.server.dungeons.dungeons;

import net.fabricmc.api.ModInitializer;
import org.tbm.server.dungeons.dungeons.effect.ModEffects;
import org.tbm.server.dungeons.dungeons.item.ModItems;
import org.tbm.server.dungeons.dungeons.potion.ModPotions;
import org.tbm.server.dungeons.dungeons.world.dimension.ModDimensions;
import org.tbm.server.dungeons.dungeons.world.dimension.ModPortals;

public class Dungeons implements ModInitializer {
    public static final String MOD_ID = "tbm_dungeons";

    @Override
    public void onInitialize(){
        ModDimensions.register();
        ModPortals.registerPortals();
        ModItems.registerModItems();
        ModEffects.registerEffects();
        ModPotions.registerPotions();

    }
}
