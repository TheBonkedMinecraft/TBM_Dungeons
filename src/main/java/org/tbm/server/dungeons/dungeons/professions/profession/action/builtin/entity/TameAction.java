package org.tbm.server.dungeons.dungeons.professions.profession.action.builtin.entity;

import org.tbm.server.dungeons.dungeons.professions.profession.Profession;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionContext;
import org.tbm.server.dungeons.dungeons.professions.profession.action.Action;
import org.tbm.server.dungeons.dungeons.professions.profession.action.ActionType;
import org.tbm.server.dungeons.dungeons.professions.profession.action.Actions;
import org.tbm.server.dungeons.dungeons.professions.profession.conditions.ActionCondition;
import org.tbm.server.dungeons.dungeons.professions.profession.progression.Occupation;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.Reward;
import org.tbm.server.dungeons.dungeons.professions.util.ActionEntry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class TameAction extends AbstractEntityAction {

    protected TameAction(ActionCondition[] conditions, Reward[] rewards, List<ActionEntry<EntityType<?>>> types) {
        super(conditions, rewards, types);
    }

    @Override
    public ActionType getType() {
        return Actions.TAME_ENTITY;
    }

    @Override
    public List<Singular<EntityType<?>>> convertToSingle(Profession profession) {
        List<Singular<EntityType<?>>> list = new ArrayList<>();
        for (EntityType<?> realEntity : getRealEntities()) {
            list.add(new Single(realEntity, profession));
        }
        return list;
    }

    public static Builder tame() {
        return new Builder();
    }

    public static class Serializer extends AbstractEntityAction.Serializer<TameAction> {

        @Override
        public TameAction deserialize(JsonObject object, JsonDeserializationContext context, ActionCondition[] conditions, Reward[] rewards) {
            return new TameAction(conditions, rewards, deserializeEntities(object));
        }
    }

    public static class Builder extends AbstractEntityAction.Builder<Builder> {

        @Override
        protected Builder instance() {
            return this;
        }

        @Override
        public Action build() {
            return new TameAction(this.getConditions(), this.getRewards(), this.entries);
        }
    }

    public class Single extends AbstractSingle<EntityType<?>> {

        public Single(EntityType<?> value, Profession profession) {
            super(value, profession);
        }

        @Override
        public ActionType getType() {
            return TameAction.this.getType();
        }

        @Override
        public Component createActionComponent() {
            return Component.translatable(getType().getTranslationKey());
        }

        @Override
        public boolean handleAction(ProfessionContext context, Occupation player) {
            return TameAction.this.handleAction(context, player);
        }

        @Override
        public void giveRewards(ProfessionContext context, Occupation occupation) {
            TameAction.this.giveRewards(context, occupation);
        }
    }
}
