package org.tbm.server.dungeons.dungeons;

import io.wispforest.owo.network.OwoNetChannel;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.tbm.server.dungeons.dungeons.effect.ModEffects;
import org.tbm.server.dungeons.dungeons.item.ModItems;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketDungeons;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketEnd;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketNether;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketOverworld;
import org.tbm.server.dungeons.dungeons.potion.ModPotions;
import org.tbm.server.dungeons.dungeons.world.dimension.ModDimensions;
import org.tbm.server.dungeons.dungeons.world.dimension.ModPortals;

public class Dungeons implements ModInitializer {
    public static final String MOD_ID = "tbm_dungeons";
    public static final OwoNetChannel STATE_CHANNEL = OwoNetChannel.create(new Identifier("tbm_dungeons", "state"));

    @Override
    public void onInitialize(){
        ModDimensions.register();
        ModPortals.registerPortals();
        ModItems.registerModItems();
        ModEffects.registerEffects();
        ModPotions.registerPotions();
        STATE_CHANNEL.registerServerbound(RequestStatePacketOverworld.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft","overworld"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft","overworld"))).getBlockState(packet.pos())));
            }
        });
        STATE_CHANNEL.registerServerbound(RequestStatePacketNether.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft","the_nether"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "the_nether"))).getBlockState(packet.pos())));
            }
        });
        STATE_CHANNEL.registerServerbound(RequestStatePacketEnd.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft","the_end"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "the_end"))).getBlockState(packet.pos())));
            }
        });
        STATE_CHANNEL.registerServerbound(RequestStatePacketDungeons.class, (packet, server) -> {
            if (server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("tbm_dungeons","tbm_dungeons"))).getBlockState(packet.pos()).toString().contains("everycomp")) {
                server.netHandler().connection.send(new BlockUpdateS2CPacket(packet.pos(), server.runtime().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier("tbm_dungeons", "tbm_dungeons"))).getBlockState(packet.pos())));
            }
        });
    }
}
