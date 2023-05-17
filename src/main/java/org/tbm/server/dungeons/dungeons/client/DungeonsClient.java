package org.tbm.server.dungeons.dungeons.client;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.hud.Hud;
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
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.tbm.server.dungeons.dungeons.Dungeons;
import org.tbm.server.dungeons.dungeons.component.IntComponent;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketDungeons;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketEnd;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketNether;
import org.tbm.server.dungeons.dungeons.packet.RequestStatePacketOverworld;
import org.tbm.server.dungeons.dungeons.screen.DifficultySliderScreen;
import org.tbm.server.dungeons.dungeons.world.dimension.ModDimensions;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.tbm.server.dungeons.dungeons.Dungeons.STATE_CHANNEL;

@Environment(EnvType.CLIENT)
public class DungeonsClient implements ClientModInitializer {
    private static final KeyBinding BEGIN = new KeyBinding("key.owo-ui-academy.begin", GLFW_KEY_H, "key.categories.misc");

    static KeyBinding requestBlocks;

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(BEGIN);
        AtomicBoolean inDungeon = new AtomicBoolean(false);
        AtomicBoolean isHudLoaded = new AtomicBoolean(false);
        ClientTickEvents.END_WORLD_TICK.register(world ->{
            inDungeon.set(world.getRegistryKey() == ModDimensions.DUNGEONS_KEY);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(inDungeon.get() && client.player != null){
                IntComponent syncedTime = Dungeons.DUNGEONS_TICK.get(client.player);
                if (!isHudLoaded.get()) {
                    Hud.add(new Identifier("tbm_dungeons","time"),() ->
                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                .child(Components.item(Items.CLOCK.getDefaultStack()).margins(Insets.of(3)))
                                .child(Components.label(Text.literal(formatTicks(syncedTime.getValue())).formatted(Formatting.WHITE)).id("time"))
                                .alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
                                .padding(Insets.of(5))
                                .surface(Surface.BLANK)
                                .margins(Insets.of(5,0,5,0))
                                .positioning(Positioning.relative(15, 100)));
                    isHudLoaded.set(true);
                } else {
                    if (Hud.getComponent(new Identifier("tbm_dungeons","time")) != null) {
                        var labelElem = (LabelComponent) ((ParentComponent) Hud.getComponent(new Identifier("tbm_dungeons","time"))).childById(LabelComponent.class, "time");
                            labelElem.text(Text.literal(formatTicks(syncedTime.getValue())).formatted(Formatting.WHITE)).id("time");
                        }
                    }
            } else {
                if (isHudLoaded.get()) {
                    Hud.remove(new Identifier("tbm_dungeons","time"));
                    isHudLoaded.set(false);
                }
            }
            while (BEGIN.wasPressed()) {
                client.setScreen(new DifficultySliderScreen());
            }
        });



        final String category="key.categories.state_update";
        requestBlocks = new KeyBinding("key.state_update.reveal", GLFW_KEY_K, category);
        KeyBindingHelper.registerKeyBinding(requestBlocks);
        ClientTickEvents.END_CLIENT_TICK.register(e->keyPressed());
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommandManager.literal("ghost").executes(c -> {
                        this.execute();
                        return 0;
                    })
            );
        });
    }
    public void keyPressed() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (requestBlocks.wasPressed()) {
            this.execute();
            player.sendMessage(Text.translatable("msg.request"), false);
        }
    }
    public static String formatTicks(int timeInTicks)
    {
        int timeInSeconds = Math.floorDiv(timeInTicks, 20);
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return timeInTicks >= 0 ? formattedTime : "00:00:00";
    }


    public void execute() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (Objects.equals(mc.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "overworld")))) {
            for (int dx=-4; dx<=4; dx++) {
                for (int dy=-4; dy<=4; dy++) {
                    for (int dz=-4; dz<=4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new RequestStatePacketOverworld(new BlockPos(mc.player.getPos().getX() + dx, mc.player.getPos().getY() + dy, mc.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(mc.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "the_nether")))) {
            for (int dx=-4; dx<=4; dx++) {
                for (int dy=-4; dy<=4; dy++) {
                    for (int dz=-4; dz<=4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new RequestStatePacketNether(new BlockPos(mc.player.getPos().getX() + dx, mc.player.getPos().getY() + dy, mc.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(mc.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("minecraft", "the_end")))) {
            for (int dx=-4; dx<=4; dx++) {
                for (int dy=-4; dy<=4; dy++) {
                    for (int dz=-4; dz<=4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new RequestStatePacketEnd(new BlockPos(mc.player.getPos().getX() + dx, mc.player.getPos().getY() + dy, mc.player.getPos().getZ() + dz)));
                    }
                }
            }
        } else if (Objects.equals(mc.player.getWorld().getRegistryKey(), RegistryKey.of(Registry.WORLD_KEY, new Identifier("tbm_dungeons", "tbm_dungeons")))) {
            for (int dx=-4; dx<=4; dx++) {
                for (int dy=-4; dy<=4; dy++) {
                    for (int dz=-4; dz<=4; dz++) {
                        STATE_CHANNEL.clientHandle().send(new RequestStatePacketDungeons(new BlockPos(mc.player.getPos().getX() + dx, mc.player.getPos().getY() + dy, mc.player.getPos().getZ() + dz)));
                    }
                }
            }
        }

    }
}

