package org.tbm.server.dungeons.dungeons.screen;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.tbm.server.dungeons.dungeons.Dungeons;
import org.tbm.server.dungeons.dungeons.component.IDifficultyComponent;
import org.tbm.server.dungeons.dungeons.component.ILastUpdatedDifficultyComponent;
import org.tbm.server.dungeons.dungeons.component.ModComponents;
import org.tbm.server.dungeons.dungeons.packet.C2SDifficultyUpdate;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DifficultyOptionScreen extends BaseUIModelScreen<FlowLayout> {

    String[] difficulties = {"Easy", "Normal", "Hard", "Extreme"};
    public final MinecraftClient playerMc = MinecraftClient.getInstance();
    String[] descriptions = {
            "§l- EASY MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4",
            "§l- NORMAL MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4",
            "§l- HARD MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4",
            "§l- EXTREME MODE (line1)\n§l- Line 2\n§l- Line 3\n§l- Line 4",
    };

    AtomicLong lastUpdate = new AtomicLong(0);

    AtomicInteger pos = new AtomicInteger(1);

    public DifficultyOptionScreen() {
        super(FlowLayout.class, DataSource.asset(new Identifier("tbm_dungeons", "difficultyoptionscreen")));
        if (playerMc.player != null) {
            IDifficultyComponent difficulty = ModComponents.DIFFICULTY_SETTING.get(playerMc.player);
            ILastUpdatedDifficultyComponent lastUpdated = ModComponents.LAST_UPDATED.get(playerMc.player);
            pos.set(difficulty.getValue());
            lastUpdate.set(lastUpdated.getValue());
        }
    }


    @Override
    protected void build(FlowLayout rootComponent) {

        // pre-setting descriptions / difficulty button peepoRun
        LabelComponent label = rootComponent.childById(LabelComponent.class, "description");
        label.text(Text.literal(descriptions[pos.get()]));

        ButtonComponent difficultyButton = rootComponent.childById(ButtonComponent.class, "difficultybutton");
        difficultyButton.setMessage(Text.literal(difficulties[pos.get()]).formatted(Formatting.BOLD));

        // clock item display + 7 day time notifier (i had to put the components in the xml file instead of having just the two components as code-driven,
        // but it would cause it to go whack if it was code driven with different ui scales so i just shoved it in there instead !vanish peepoRun
        ItemComponent clock = rootComponent.childById(ItemComponent.class, "clock");
        LabelComponent timelimit = rootComponent.childById(LabelComponent.class, "time");
        ButtonComponent confirmbutton = rootComponent.childById(ButtonComponent.class, "confirm_difficulty");


        // click listeners Gladge

        rootComponent.childById(ButtonComponent.class, "difficultybutton").onPress(button -> {
            if (pos.get() == 3) {
                pos.set(0);
            } else {
                pos.getAndIncrement();
            }
            button.setMessage(Text.literal(difficulties[pos.get()]).formatted(Formatting.BOLD));
            button.setWidth(56);
            label.text(Text.literal(descriptions[pos.get()]).formatted(Formatting.BOLD));
        });

        // 7 day restrictions (ily slow for the code)

        if (System.currentTimeMillis() - lastUpdate.get() < 604800000) {
            confirmbutton.visible = false;
            clock.stack(Items.CLOCK.getDefaultStack());
            Date currentDate = new Date(lastUpdate.get() + 604800000);
            timelimit.text(Text.literal("Next change available on: " + DateFormat.getInstance().format(currentDate)).formatted(Formatting.WHITE));
        } else {
            confirmbutton.setMessage(Text.literal("Done").formatted(Formatting.BOLD));
            confirmbutton.onPress(button -> {
                confirmbutton.visible = true;
                Dungeons.STATE_CHANNEL.clientHandle().send(new C2SDifficultyUpdate(pos.get()));
                close();
            });
        }
    }
}
