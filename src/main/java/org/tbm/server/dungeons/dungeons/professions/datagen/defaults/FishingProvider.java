package org.tbm.server.dungeons.dungeons.professions.datagen.defaults;

import org.tbm.server.dungeons.dungeons.professions.datagen.NamedProfessionBuilder;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionBuilder;
import org.tbm.server.dungeons.dungeons.professions.profession.action.builtin.items.FishingAction;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.perks.Perks;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.perks.builtin.ScalingAttributePerk;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;

import static org.tbm.server.dungeons.dungeons.professions.profession.action.Actions.FISH_ACTION;

public class FishingProvider extends NamedProfessionBuilder {

    public FishingProvider() {
        super(ProfessionBuilder.profession(
                TextColor.parseColor("#0a91c7"),
                TextColor.parseColor("#FFFFFF"),
                new String[]{
                        "Earn money and experience",
                        "by fishing."},
                "Fishing", 100));
    }


    @Override
    public void addData(ProfessionBuilder builder) {
        builder.addExperienceScaling(defaultLevelParser())
                .incomeScaling(defaultIncomeParser())
                .addAction(FISH_ACTION, FishingAction.fish()
                        .item(Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH)
                        .reward(moneyReward(25))
                        .reward(expReward(25))
                        .build());
        builder.addPerk(Perks.SCALING_ATTRIBUTE_PERK, ScalingAttributePerk.scaling()
                .level(1).attribute(Attributes.MAX_HEALTH).increaseBy(0.20));
        builder.addPerk(Perks.SCALING_ATTRIBUTE_PERK, ScalingAttributePerk.scaling()
                .level(10).attribute(Attributes.ATTACK_DAMAGE).increaseBy(0.05));
        builder.addPerk(Perks.SCALING_ATTRIBUTE_PERK, ScalingAttributePerk.scaling()
                .level(20).attribute(Attributes.LUCK).increaseBy(0.05));
    }
}
