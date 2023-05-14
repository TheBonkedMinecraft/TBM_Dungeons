package org.tbm.server.dungeons.dungeons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketDungeons;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketEnd;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketNether;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketOverworld;

import java.util.Objects;

import static org.tbm.server.dungeons.dungeons.Dungeons.STATE_CHANNEL;

@Mixin(ClientPlayNetworkHandler.class)
public class DeltaChunkUpdate {
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onChunkDeltaUpdate",
            require = 1,
            allow = 1,
            at = @At("TAIL"))
    private void logChunkDeltaPacket(ChunkDeltaUpdateS2CPacket packet, CallbackInfo ci){
        if (Objects.equals(client.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "overworld")))) {
            for (int dx = -4; dx <= 4; dx++) {
                for (int dy = -4; dy <= 4; dy++) {
                    for (int dz = -4; dz <= 4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new RequestStatePacketOverworld(new BlockPos(client.player.getPos().getX() + dx, client.player.getPos().getY() + dy, client.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(client.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "the_nether")))) {
            for (int dx = -4; dx <= 4; dx++) {
                for (int dy = -4; dy <= 4; dy++) {
                    for (int dz = -4; dz <= 4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new RequestStatePacketNether(new BlockPos(client.player.getPos().getX() + dx, client.player.getPos().getY() + dy, client.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(client.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "the_end")))) {
            for (int dx = -4; dx <= 4; dx++) {
                for (int dy = -4; dy <= 4; dy++) {
                    for (int dz = -4; dz <= 4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new RequestStatePacketEnd(new BlockPos(client.player.getPos().getX() + dx, client.player.getPos().getY() + dy, client.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(client.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("tbm_dungeons", "tbm_dungeons")))) {
            for (int dx = -4; dx <= 4; dx++) {
                for (int dy = -4; dy <= 4; dy++) {
                    for (int dz = -4; dz <= 4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new RequestStatePacketDungeons(new BlockPos(client.player.getPos().getX() + dx, client.player.getPos().getY() + dy, client.player.getPos().getZ() + dz)));
                    }
                }
            }
        }
    }
}
