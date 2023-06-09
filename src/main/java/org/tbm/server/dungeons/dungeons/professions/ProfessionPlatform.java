package org.tbm.server.dungeons.dungeons.professions;

import com.epherical.octoecon.api.Economy;
import org.tbm.server.dungeons.dungeons.professions.api.ProfessionalPlayer;
import org.tbm.server.dungeons.dungeons.professions.datapack.ProfessionLoader;
import org.tbm.server.dungeons.dungeons.professions.networking.ClientNetworking;
import org.tbm.server.dungeons.dungeons.professions.networking.CommandButtons;
import org.tbm.server.dungeons.dungeons.professions.profession.Profession;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionContext;
import org.tbm.server.dungeons.dungeons.professions.profession.action.AbstractAction;
import org.tbm.server.dungeons.dungeons.professions.profession.progression.OccupationSlot;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.RewardType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.nio.file.Path;
import java.util.Map;

public abstract class ProfessionPlatform<T> {

    public static ProfessionPlatform<?> platform;

    protected ProfessionPlatform() {
        platform = this;
    }

    public static <T> void create(ProfessionPlatform<T> value) {
        platform = value;
    }

    public abstract T getPlatform();

    public abstract boolean isClientEnvironment();

    public abstract boolean isServerEnvironment();

    public abstract MinecraftServer server();

    public abstract Economy economy();

    public abstract boolean checkPermission(Player player, String perm, int defIntPerm);

    public abstract boolean checkDynamicPermission(Player player, String basePerm, String dynamic, int defIntPerm);

    public abstract boolean checkPermission(Player player, String perm);

    public abstract void sendButtonPacket(CommandButtons buttons);

    public abstract ProfessionLoader getProfessionLoader();

    public abstract PlayerManager getPlayerManager();

    public abstract void sendBeforeActionHandledEvent(ProfessionContext context, ProfessionalPlayer player);

    public abstract void professionJoinEvent(ProfessionalPlayer player, Profession profession, OccupationSlot slot, ServerPlayer serverPlayer);

    public abstract void professionLeaveEvent(ProfessionalPlayer player, Profession profession, ServerPlayer serverPlayer);

    public abstract void sendSyncRequest(ServerPlayer player);

    public abstract ClientNetworking getClientNetworking();

    public abstract boolean skipReward(RewardType type);

    public abstract Path getRootConfigPath();

    /**
     * Some platforms may not have the same key rewards that should be displayed in /professions info.
     * E.G forge does not have a payment reward, only experience.
     */
    public abstract Component displayInformation(AbstractAction action, Map<RewardType, Component> map);


}
