package org.tbm.server.dungeons.dungeons.screen;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.tbm.server.dungeons.dungeons.Dungeons;
import org.tbm.server.dungeons.dungeons.component.IDifficultyComponent;
import org.tbm.server.dungeons.dungeons.packet.C2SDifficultyUpdate;

import java.util.concurrent.atomic.AtomicInteger;

public class DifficultyOptionScreen extends BaseOwoScreen<FlowLayout> {

    String[] difficulties = {"Easy", "Normal", "Hard", "Extreme"};
    public final MinecraftClient playerMc = MinecraftClient.getInstance();
    String[] descriptions = {
            "§l- EASY MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4",
            "§l- NORMAL MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4",
            "§l- HARD MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4",
            "§l- EXTREME MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4",
    };

    AtomicInteger pos = new AtomicInteger(1);

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        if (playerMc.player != null) {
            IDifficultyComponent difficulty = Dungeons.DIFFICULTY_SETTING.get(playerMc.player);
            pos.set(difficulty.getValue());
        }
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }
    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);


        rootComponent.child(
                Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(Components.box(Sizing.fixed(160), Sizing.fixed(160)))
                        .padding(Insets.of(4))
                        .surface(Surface.DARK_PANEL)
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .positioning(Positioning.relative(50, 50))
        );

        rootComponent.child(
                Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(Components.label(Text.literal("Difficulty Settings").formatted(Formatting.WHITE)))
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .sizing(Sizing.content(), Sizing.content())
                        .positioning(Positioning.relative(50, 30))
        );

        rootComponent.child(
                Components.label(Text.literal(descriptions[pos.get()]).formatted(Formatting.BOLD))
                        .sizing(Sizing.content(), Sizing.content())
                        .positioning(Positioning.relative(50, 55))
                        .id("description")
        );

        LabelComponent label = rootComponent.childById(LabelComponent.class, "description");

        rootComponent.child(
                Components.button(Text.literal(difficulties[pos.get()]).formatted(Formatting.BOLD), (ButtonComponent button) -> {
                            if (pos.get() == 3) {
                                pos.set(0);
                            } else {
                                pos.getAndIncrement();
                            }
                            button.setMessage(Text.literal(difficulties[pos.get()]).formatted(Formatting.BOLD));
                            button.setWidth(56);
                            label.text(Text.literal(descriptions[pos.get()]));
                        })
                        .positioning(Positioning.relative(50, 38))
                        .sizing(Sizing.fixed(56), Sizing.fixed(20))
                        .id("difficulty")
        );

        rootComponent.child(
                Components.button(Text.literal("Done").formatted(Formatting.BOLD), (ButtonComponent button) -> {
                            Dungeons.STATE_CHANNEL.clientHandle().send(new C2SDifficultyUpdate(pos.get()));
                            close();
                        })
                        .sizing(Sizing.fixed(56), Sizing.fixed(20))
                        .positioning(Positioning.relative(50, 70))
        );
    }
}