package org.tbm.server.dungeons.dungeons.professions.profession.rewards.builtin;

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
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.Serializer;
import org.jetbrains.annotations.NotNull;

public record ItemReward(Item item, int count) implements Reward {

    @Override
    public RewardType getType() {
        return Rewards.ITEM_REWARD;
    }

    @Override
    public void giveReward(ProfessionContext context, Action action, Occupation occupation) {
        ServerPlayer player = context.getParameter(ProfessionParameter.THIS_PLAYER).getPlayer();
        if (player == null) {
            return;
        }
        ItemStack toDrop = new ItemStack(this.item);
        toDrop.setCount(this.count);
        Block.popResource(player.getLevel(), player.getOnPos().above(), toDrop);
    }

    @Override
    public @NotNull Component rewardChatInfo() {
        return Component.literal("Item ").setStyle(Style.EMPTY.withColor(TextColor.parseColor("#d14f88"))).append(item.getDescription());
    }

    public static class RewardSerializer implements Serializer<ItemReward> {

        @Override
        public void serialize(JsonObject json, ItemReward value, JsonSerializationContext serializationContext) {
            json.addProperty("item", Registry.ITEM.getKey(value.item).toString());
            json.addProperty("count", Registry.ITEM.getKey(value.item).toString());
        }

        @Override
        public ItemReward deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
            Item item = GsonHelper.getAsItem(json, "item");
            int count = GsonHelper.getAsInt(json, "count", 1);
            return new ItemReward(item, count);
        }
    }

    public static class Builder implements Reward.Builder {
        private Item item;
        private int count;

        public Builder item(Item item, int count) {
            this.item = item;
            this.count = count;
            return this;
        }

        @Override
        public Reward build() {
            return new ItemReward(item, count);
        }
    }
}
