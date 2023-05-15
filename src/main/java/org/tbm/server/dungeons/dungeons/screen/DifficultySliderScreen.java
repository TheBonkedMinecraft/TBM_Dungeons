package org.tbm.server.dungeons.dungeons.screen;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class DifficultySliderScreen extends BaseOwoScreen<FlowLayout> {

    public final MinecraftClient playerMc = MinecraftClient.getInstance();

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
        var player = (LivingEntity) playerMc.player;
        rootComponent
                .child(
                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                .child(
                                        Components.entity(
                                                        Sizing.fixed(64),
                                                        player
                                                )
                                                .allowMouseRotation(true)
                                                .lookAtCursor(true)
                                                .scaleToFit(true)
                                )
                                .padding(Insets.of(10))
                                .surface(Surface.DARK_PANEL)
                                .verticalAlignment(VerticalAlignment.CENTER)
                                .horizontalAlignment(HorizontalAlignment.CENTER)
                );

    }
}