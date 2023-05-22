package org.tbm.server.dungeons.dungeons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.tbm.server.dungeons.dungeons.world.dimension.ModDimensions;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.tbm.server.dungeons.dungeons.packet.C2SRequestStateDungeons;
import org.tbm.server.dungeons.dungeons.packet.C2SRequestStateEnd;
import org.tbm.server.dungeons.dungeons.packet.C2SRequestStateNether;
import org.tbm.server.dungeons.dungeons.packet.C2SRequestStateOverworld;

import java.util.Objects;

import static org.tbm.server.dungeons.dungeons.Dungeons.STATE_CHANNEL;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class DeltaChunkUpdate {
    @Shadow
    private MinecraftClient client;


    @Inject(method = "onChunkDeltaUpdate",
            require = 1,
            allow = 1,
            at = @At("TAIL"))
    private void logChunkDeltaPacket(ChunkDeltaUpdateS2CPacket packet, CallbackInfo ci){
        if (Objects.equals(client.player.getWorld().getRegistryKey(), World.OVERWORLD)) {
            for (int dx = -4; dx <= 4; dx++) {
                for (int dy = -4; dy <= 4; dy++) {
                    for (int dz = -4; dz <= 4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new C2SRequestStateOverworld(new BlockPos(client.player.getPos().getX() + dx, client.player.getPos().getY() + dy, client.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(client.player.getWorld().getRegistryKey(), World.NETHER)) {
            for (int dx = -4; dx <= 4; dx++) {
                for (int dy = -4; dy <= 4; dy++) {
                    for (int dz = -4; dz <= 4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new C2SRequestStateNether(new BlockPos(client.player.getPos().getX() + dx, client.player.getPos().getY() + dy, client.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(client.player.getWorld().getRegistryKey(), World.END)) {
            for (int dx = -4; dx <= 4; dx++) {
                for (int dy = -4; dy <= 4; dy++) {
                    for (int dz = -4; dz <= 4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new C2SRequestStateEnd(new BlockPos(client.player.getPos().getX() + dx, client.player.getPos().getY() + dy, client.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(client.player.getWorld().getRegistryKey(), ModDimensions.DUNGEONS_KEY)) {
            for (int dx = -4; dx <= 4; dx++) {
                for (int dy = -4; dy <= 4; dy++) {
                    for (int dz = -4; dz <= 4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new C2SRequestStateDungeons(new BlockPos(client.player.getPos().getX() + dx, client.player.getPos().getY() + dy, client.player.getPos().getZ() + dz)));
                    }
                }
            }
        }
    }
}
