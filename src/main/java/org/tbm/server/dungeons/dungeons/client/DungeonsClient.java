package org.tbm.server.dungeons.dungeons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class DungeonsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Hello from ModDimensions Client");
    }
}