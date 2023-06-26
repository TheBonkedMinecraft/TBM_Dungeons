package org.tbm.server.dungeons.dungeons.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import org.tbm.server.dungeons.dungeons.Dungeons;

public class ModMessages {
    public static final Identifier FLUID_SYNC = new Identifier(Dungeons.MOD_ID, "fluid_sync");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(FLUID_SYNC, FluidSyncS2CPacket::receive);
    }
}
