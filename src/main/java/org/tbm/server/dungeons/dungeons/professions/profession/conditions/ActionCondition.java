package org.tbm.server.dungeons.dungeons.professions.profession.conditions;

import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionContext;
import org.tbm.server.dungeons.dungeons.professions.profession.conditions.builtin.InvertedCondition;

import java.util.function.Predicate;

public interface ActionCondition extends Predicate<ProfessionContext> {
    ActionConditionType getType();

    @FunctionalInterface
    interface Builder {
        ActionCondition build();

        default Builder invert() {
            return InvertedCondition.invert(this);
        }
    }

}
