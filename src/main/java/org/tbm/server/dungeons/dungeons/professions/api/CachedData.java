package org.tbm.server.dungeons.dungeons.professions.api;

import org.tbm.server.dungeons.dungeons.professions.profession.action.Action;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.perks.Perk;
import org.tbm.server.dungeons.dungeons.professions.profession.modifiers.perks.PerkType;
import org.tbm.server.dungeons.dungeons.professions.profession.progression.Occupation;
import org.tbm.server.dungeons.dungeons.professions.profession.unlock.Unlock;
import org.tbm.server.dungeons.dungeons.professions.util.SeededValueList;

import java.util.Collection;
import java.util.List;

public interface CachedData {

    <T> SeededValueList<Unlock.Singular<T>> getUnlock(T object);

    <T> SeededValueList<Action.Singular<T>> getAction(T object);

    Occupation getOccupation();

    Collection<Unlock.Singular<?>> getUnlockables();

    List<Unlock.Singular<?>> getUnlockedKnowledge();

    Collection<Perk> getPerkByType(PerkType type);

    Collection<Perk> getUnlockedPerkByType(PerkType type, ProfessionalPlayer player);

    Collection<Perk> allPerks();

    <T> SeededValueList<Action.Singular<T>> getActions();
}
