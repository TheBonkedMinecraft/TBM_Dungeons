package org.tbm.server.dungeons.dungeons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.tbm.server.dungeons.dungeons.packet.C2SRequestStateDungeons;
import org.tbm.server.dungeons.dungeons.packet.C2SRequestStateEnd;
import org.tbm.server.dungeons.dungeons.packet.C2SRequestStateNether;
import org.tbm.server.dungeons.dungeons.packet.C2SRequestStateOverworld;
import org.tbm.server.dungeons.dungeons.screen.DifficultyOptionScreen;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.tbm.server.dungeons.dungeons.Dungeons.STATE_CHANNEL;

@Environment(EnvType.CLIENT)
public class DungeonsClient implements ClientModInitializer {
    private static final KeyBinding BEGIN = new KeyBinding("key.dungeons.difficulty.show", GLFW_KEY_H, "key.categories.misc");

    static KeyBinding requestBlocks;

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(BEGIN);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (BEGIN.wasPressed()) {
                client.setScreen(new DifficultyOptionScreen());
            }
        });

        final String category = "key.categories.misc";
        requestBlocks = new KeyBinding("key.state_update.reveal", GLFW_KEY_K, category);
        KeyBindingHelper.registerKeyBinding(requestBlocks);
        ClientTickEvents.END_CLIENT_TICK.register(e->keyPressed());
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager.literal("statesync").executes(c -> {
                    this.execute();
                    return 0;
                })
        ));
    }
    public void keyPressed() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (requestBlocks.wasPressed()) {
            this.execute();
            player.sendMessage(Text.translatable("msg.request"), false);
        }
    }
    public void execute() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (Objects.equals(mc.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "overworld")))) {
            for (int dx=-4; dx<=4; dx++) {
                for (int dy=-4; dy<=4; dy++) {
                    for (int dz=-4; dz<=4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new C2SRequestStateOverworld(new BlockPos(mc.player.getPos().getX() + dx, mc.player.getPos().getY() + dy, mc.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(mc.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "the_nether")))) {
            for (int dx=-4; dx<=4; dx++) {
                for (int dy=-4; dy<=4; dy++) {
                    for (int dz=-4; dz<=4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new C2SRequestStateNether(new BlockPos(mc.player.getPos().getX() + dx, mc.player.getPos().getY() + dy, mc.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(mc.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "the_end")))) {
            for (int dx=-4; dx<=4; dx++) {
                for (int dy=-4; dy<=4; dy++) {
                    for (int dz=-4; dz<=4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new C2SRequestStateEnd(new BlockPos(mc.player.getPos().getX() + dx, mc.player.getPos().getY() + dy, mc.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(mc.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("tbm_dungeons", "tbm_dungeons")))) {
            for (int dx=-4; dx<=4; dx++) {
                for (int dy=-4; dy<=4; dy++) {
                    for (int dz=-4; dz<=4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new C2SRequestStateDungeons(new BlockPos(mc.player.getPos().getX() + dx, mc.player.getPos().getY() + dy, mc.player.getPos().getZ() + dz)));
                    }
                }
            }
        }
    }
}

