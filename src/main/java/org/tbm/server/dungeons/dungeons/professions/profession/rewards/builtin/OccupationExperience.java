package org.tbm.server.dungeons.dungeons.professions.profession.rewards.builtin;

import org.tbm.server.dungeons.dungeons.professions.PlayerManager;
import org.tbm.server.dungeons.dungeons.professions.ProfessionPlatform;
import org.tbm.server.dungeons.dungeons.professions.api.ProfessionalPlayer;
import org.tbm.server.dungeons.dungeons.professions.config.ProfessionConfig;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionContext;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionParameter;
import org.tbm.server.dungeons.dungeons.professions.profession.action.Action;
import org.tbm.server.dungeons.dungeons.professions.profession.progression.Occupation;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.Reward;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.RewardType;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.Rewards;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.Serializer;
import org.jetbrains.annotations.NotNull;


public record OccupationExperience(double expAmount) implements Reward {

    @Override
    public RewardType getType() {
        return Rewards.EXPERIENCE_REWARD;
    }

    @Override
    public void giveReward(ProfessionContext context, Action action, Occupation occupation) {
        // if true, player levels up.
        ProfessionalPlayer player = context.getParameter(ProfessionParameter.THIS_PLAYER);
        context.getParameter(ProfessionParameter.ACTION_LOGGER).addExpReward(rewardChatInfo(), expAmount, occupation);
        int currentLevel = occupation.getLevel();
        if (occupation.addExp(action.modifyReward(context, this, expAmount), player)) {
            PlayerManager manager = ProfessionPlatform.platform.getPlayerManager();
            manager.levelUp(player, occupation, currentLevel);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public @NotNull Component rewardChatInfo() {
        return Component.literal(String.format("%.2fxp", expAmount)).setStyle(Style.EMPTY.withColor(ProfessionConfig.experience));
    }

    public static class RewardSerializer implements Serializer<OccupationExperience> {

        @Override
        public void serialize(JsonObject json, OccupationExperience value, @NotNull JsonSerializationContext serializationContext) {
            json.addProperty("amount", value.expAmount);
        }

        @Override
        public OccupationExperience deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext serializationContext) {
            double expAmount = GsonHelper.getAsDouble(json, "amount");
            return new OccupationExperience(expAmount);
        }
    }

    public static class Builder implements Reward.Builder {
        private double exp;

        public Builder exp(double exp) {
            this.exp = exp;
            return this;
        }

        @Override
        public Reward build() {
            return new OccupationExperience(exp);
        }
    }
}
