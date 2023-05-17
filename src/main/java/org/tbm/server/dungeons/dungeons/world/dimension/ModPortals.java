package org.tbm.server.dungeons.dungeons.world.dimension;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.SHOULDTP;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.tbm.server.dungeons.dungeons.Dungeons;
import org.tbm.server.dungeons.dungeons.component.BlockPosComponent;
import org.tbm.server.dungeons.dungeons.component.IntComponent;
import org.tbm.server.dungeons.dungeons.item.ModItems;

public class ModPortals {
    public static void registerPortals(){
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.REINFORCED_DEEPSLATE)
                .destDimID(ModDimensions.DUNGEONS_KEY.getValue())
                .lightWithItem(ModItems.DUNGEON_CRYSTAL)
                .tintColor(255,105,97)
                .setReturnPortalSearchYRange(64,384)
                .registerIgniteEvent((player, world, headPos, legsPos, ignitionSource) -> {
                    BlockPosComponent portalPos = Dungeons.PORTAL_POS.get(player);
                    portalPos.setBlockPos(headPos);
                    var mainHand = player.getStackInHand(Hand.MAIN_HAND);
                    var offHand = player.getStackInHand(Hand.OFF_HAND);
                    if (offHand.isItemEqual(ModItems.DUNGEON_CRYSTAL.getDefaultStack())){
                        player.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                    }
                    if (mainHand.isItemEqual(ModItems.DUNGEON_CRYSTAL.getDefaultStack())){
                        player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                    }
                })
                .registerBeforeTPEvent((entity) -> {
                    if (entity.isPlayer()) {
                        IntComponent time = Dungeons.DUNGEONS_TICK.get(entity);
                        time.setValue(72000);
                    }
                    return SHOULDTP.CONTINUE_TP;
                })
                .registerPortal()
                .setPostTPEvent(entity -> {
                    if (entity.isPlayer() && entity.world.getRegistryKey() == ModDimensions.DUNGEONS_KEY) {
                        IntComponent time = Dungeons.DUNGEONS_TICK.get(entity);
                        ServerTickEvents.END_SERVER_TICK.register((server) -> {
                            time.decrement();
                            if (time.getValue() == 0) {
                                if (entity.world.getRegistryKey() == ModDimensions.DUNGEONS_KEY) {
                                    entity.damage(DamageSource.DRYOUT,999999);
                                    BlockPosComponent portalPos = Dungeons.PORTAL_POS.get(entity);
                                    BlockPos pos = portalPos.getBlockPos();
                                    entity.getWorld().breakBlock(pos, false);
                                }
                            }
                        });
                    }
                    if (entity.isPlayer() && entity.world.getRegistryKey() == World.OVERWORLD) {
                        BlockPosComponent portalPos = Dungeons.PORTAL_POS.get(entity);
                        BlockPos pos = portalPos.getBlockPos();
                        entity.getWorld().breakBlock(pos, false);
                    }
                });
    }
}
