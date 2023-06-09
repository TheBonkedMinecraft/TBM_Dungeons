package org.tbm.server.dungeons.dungeons.professions.profession.action;


import org.tbm.server.dungeons.dungeons.professions.profession.Profession;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionContext;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionParameter;
import org.tbm.server.dungeons.dungeons.professions.profession.progression.Occupation;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.Reward;
import org.tbm.server.dungeons.dungeons.professions.util.ActionDisplay;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Predicate;

public interface Action<T> extends Predicate<ProfessionContext> {

    ActionType getType();

    boolean handleAction(ProfessionContext context, Occupation player);

    void giveRewards(ProfessionContext context, Occupation occupation);

    List<Component> displayInformation();

    List<ActionDisplay.Icon> clientFriendlyInformation(Component actionType);

    List<Singular<T>> convertToSingle(Profession profession);

    /**
     * Called after it has already been shown the action is successful.
     */
    double modifyReward(ProfessionContext context, Reward reward, double base);

    default void logAction(ProfessionContext context, Component component) {
        context.getParameter(ProfessionParameter.ACTION_LOGGER).addAction(this, component);
    }

    @FunctionalInterface
    interface Builder {
        Action build();
    }

    interface Singular<T> {

        ActionType getType();

        T getObject();

        Component getProfessionDisplay();

        Profession getProfession();

        Component createActionComponent();

        boolean handleAction(ProfessionContext context, Occupation player);

        void giveRewards(ProfessionContext context, Occupation occupation);
    }
}
