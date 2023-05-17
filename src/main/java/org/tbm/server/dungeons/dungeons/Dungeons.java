package org.tbm.server.dungeons.dungeons;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.wispforest.owo.network.OwoNetChannel;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.tbm.server.dungeons.dungeons.component.BlockPosComponent;
import org.tbm.server.dungeons.dungeons.component.DungeonsPortalPos;
import org.tbm.server.dungeons.dungeons.component.DungeonsTick;
import org.tbm.server.dungeons.dungeons.component.IntComponent;
import org.tbm.server.dungeons.dungeons.effect.ModEffects;
import org.tbm.server.dungeons.dungeons.item.ModItems;
import org.tbm.server.dungeons.dungeons.packet.*;
import org.tbm.server.dungeons.dungeons.potion.ModPotions;
import org.tbm.server.dungeons.dungeons.world.dimension.ModDimensions;
import org.tbm.server.dungeons.dungeons.world.dimension.ModPortals;

public class Dungeons implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "tbm_dungeons";
    public static final OwoNetChannel STATE_CHANNEL = OwoNetChannel.create(new Identifier("tbm_dungeons", "state"));
    public static final ComponentKey<IntComponent> DUNGEONS_TICK =
            ComponentRegistry.getOrCreate(new Identifier("tbm_dungeons", "dungeons_tick"), IntComponent.class);
    public static final ComponentKey<BlockPosComponent> PORTAL_POS =
            ComponentRegistry.getOrCreate(new Identifier("tbm_dungeons", "portal_pos"), BlockPosComponent.class);
    @Override
    public void onInitialize(){
        ModDimensions.register();
        ModPortals.registerPortals();
        ModItems.registerModItems();
        ModEffects.registerEffects();
        ModPotions.registerPotions();
        STATE_CHANNEL.registerServerbound(RequestStatePacketOverworld.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft","overworld"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(World.OVERWORLD).getBlockState(packet.pos())));
            }
        });
        STATE_CHANNEL.registerServerbound(RequestStatePacketNether.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft","the_nether"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(World.NETHER).getBlockState(packet.pos())));
            }
        });
        STATE_CHANNEL.registerServerbound(RequestStatePacketEnd.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft","the_end"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(World.END).getBlockState(packet.pos())));
            }
        });
        STATE_CHANNEL.registerServerbound(RequestStatePacketDungeons.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("tbm_dungeons","tbm_dungeons"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(ModDimensions.DUNGEONS_KEY).getBlockState(packet.pos())));
            }
        });
    }
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(DUNGEONS_TICK, player -> new DungeonsTick(player), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(PORTAL_POS, player -> new DungeonsPortalPos(), RespawnCopyStrategy.ALWAYS_COPY);

    }
}
