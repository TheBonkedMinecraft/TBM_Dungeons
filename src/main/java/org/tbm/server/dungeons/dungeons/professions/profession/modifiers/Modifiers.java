package org.tbm.server.dungeons.dungeons.professions.profession.modifiers;

import org.tbm.server.dungeons.dungeons.professions.api.ProfessionalPlayer;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.milestones.Milestone;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.perks.Perk;
import org.tbm.server.dungeons.dungeons.professions.profession.progression.Occupation;

public interface Modifiers {

    void handleLevelUp(ProfessionalPlayer player, Occupation occupation);

    Milestone[] getMilestones();

    Perk[] getPerks();

}
