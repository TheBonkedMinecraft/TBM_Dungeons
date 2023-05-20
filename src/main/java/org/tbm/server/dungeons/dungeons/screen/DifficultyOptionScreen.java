package org.tbm.server.dungeons.dungeons.screen;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class DifficultyOptionScreen extends BaseOwoScreen<FlowLayout> {

    String[] difficulties = {"Normal", "Hard", "Extreme", "Easy"};

    String[] descriptions = {
            "§l- NORMAL MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4", // NORMAL MODE
            "§l- HARD MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4", // HARD MODE
            "§l- EXTREME MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4", // EXTREME MODE
            "§l- EASY MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4", // EASY MODE
    }; // honestly, probably not the best way to store the descriptions KEKW
    // when you open and close the UI it will default back to Normal mode because it doesn't store the data, but
    // if we send the data to the server once clicking 'Done', after a time limit that (I think) we said we were going
    // to put on the difficulty change, we could request the stored difficulty data off the server and display it like that instead
    // as it won't be happening too often... no idea how to code that though peepoRun Sadge (sorry D:)
    // I don't even know if you'll see this note

    int pos = 0;

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }
    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);


        // UI Components now peepoRun


        rootComponent.child(
                Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(Components.box(Sizing.fixed(160), Sizing.fixed(145)))
                        .padding(Insets.of(4))
                        .surface(Surface.DARK_PANEL)
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .positioning(Positioning.relative(50, 50))
        );

        rootComponent.child(
                Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(Components.label(Text.literal("Dungeons Settings").formatted(Formatting.BOLD)))
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .sizing(Sizing.content(), Sizing.content())
                        .positioning(Positioning.relative(50, 30))
        );

        rootComponent.child(
                Components.label(Text.literal(descriptions[pos]).formatted(Formatting.BOLD))
                        .sizing(Sizing.content(), Sizing.content())
                        .positioning(Positioning.relative(50, 55))
                        .id("description")


        );

        LabelComponent label = rootComponent.childById(LabelComponent.class, "description");

        rootComponent.child(
                Components.button(Text.literal("Normal").formatted(Formatting.BOLD), (ButtonComponent button) -> {
                            if (pos == 3) {
                                pos = 0;
                                button.setMessage(Text.literal(difficulties[pos]).formatted(Formatting.BOLD));
                                button.setWidth(56);
                                label.text(Text.literal(descriptions[pos]));
                            } else {
                                pos++;
                                button.setMessage(Text.literal(difficulties[pos]).formatted(Formatting.BOLD));
                                button.setWidth(56);
                                label.text(Text.literal(descriptions[pos]));
                            }
                        })      .positioning(Positioning.relative(50, 38))
                        .sizing(Sizing.fixed(56), Sizing.fixed(20))
                        .id("difficulty")
        );

        rootComponent.child(
                Components.button(Text.literal("Done").formatted(Formatting.BOLD), (ButtonComponent button) -> {
                            // retrieve data
                            String retrievedDifficulty = difficulties[pos];
                            System.out.println("DATA:\nDifficulty: [" + retrievedDifficulty + "]");
                            close();
                        })
                        .sizing(Sizing.fixed(56), Sizing.fixed(20))
                        .positioning(Positioning.relative(50, 70))
        );
    }
}