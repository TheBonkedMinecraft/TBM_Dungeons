package org.tbm.server.dungeons.dungeons.professions.profession.rewards;

import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionContext;
import org.tbm.server.dungeons.dungeons.professions.profession.action.Action;
import org.tbm.server.dungeons.dungeons.professions.profession.progression.Occupation;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public interface Reward {
    RewardType getType();

    void giveReward(ProfessionContext context, Action action, Occupation occupation);

    /**
     * @return always returns a component, even if it has no contents.
     */
    @NotNull Component rewardChatInfo();

    interface Builder {
        Reward build();
    }
}
