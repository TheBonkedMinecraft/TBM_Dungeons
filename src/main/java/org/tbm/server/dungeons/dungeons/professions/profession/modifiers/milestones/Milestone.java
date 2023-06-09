package org.tbm.server.dungeons.dungeons.professions.profession.modifiers.milestones;

import org.tbm.server.dungeons.dungeons.professions.api.ProfessionalPlayer;
import org.tbm.server.dungeons.dungeons.professions.profession.progression.Occupation;

public interface Milestone {

    MilestoneType getType();

    int getLevel();

    boolean giveMilestoneReward(ProfessionalPlayer context, Occupation occupation);

    @FunctionalInterface
    interface Builder {
        Milestone build();
    }


}
