package org.tbm.server.dungeons.dungeons.professions;

import com.epherical.octoecon.api.Economy;
import org.tbm.server.dungeons.dungeons.professions.api.ProfessionalPlayer;
import org.tbm.server.dungeons.dungeons.professions.client.ProfessionsClient;
import org.tbm.server.dungeons.dungeons.professions.datapack.ProfessionLoader;
import org.tbm.server.dungeons.dungeons.professions.events.OccupationEvents;
import org.tbm.server.dungeons.dungeons.professions.networking.ClientHandler;
import org.tbm.server.dungeons.dungeons.professions.networking.ClientNetworking;
import org.tbm.server.dungeons.dungeons.professions.networking.CommandButtons;
import org.tbm.server.dungeons.dungeons.professions.networking.ServerHandler;
import org.tbm.server.dungeons.dungeons.professions.profession.Profession;
import org.tbm.server.dungeons.dungeons.professions.profession.ProfessionContext;
import org.tbm.server.dungeons.dungeons.professions.profession.action.AbstractAction;
import org.tbm.server.dungeons.dungeons.professions.profession.progression.OccupationSlot;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.RewardType;
import org.tbm.server.dungeons.dungeons.professions.profession.rewards.Rewards;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.nio.file.Path;
import java.util.Map;

public class FabricPlatform extends ProfessionPlatform<FabricPlatform> {


    @Override
    public FabricPlatform getPlatform() {
        return this;
    }

    @Override
    public boolean isClientEnvironment() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public boolean isServerEnvironment() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }

    @Override
    public MinecraftServer server() {
        return ProfessionsFabric.getInstance().getMinecraftServer();
    }

    @Override
    public Economy economy() {
        return ProfessionsFabric.getEconomy();
    }

    @Override
    public boolean checkPermission(Player player, String perm, int defIntPerm) {
        return Permissions.check(player, perm, defIntPerm);
    }

    @Override
    public boolean checkDynamicPermission(Player player, String basePerm, String dynamic, int defIntPerm) {
        return Permissions.check(player, basePerm + "." + dynamic, defIntPerm);
    }

    @Override
    public boolean checkPermission(Player player, String perm) {
        return Permissions.check(player, perm);
    }

    @Override
    public void sendButtonPacket(CommandButtons buttons) {
        ClientHandler.sendButtonPacket(buttons);
    }

    @Override
    public ProfessionLoader getProfessionLoader() {
        return ProfessionsFabric.getInstance().getProfessionLoader();
    }

    @Override
    public PlayerManager getPlayerManager() {
        return ProfessionsFabric.getInstance().getPlayerManager();
    }

    @Override
    public void sendBeforeActionHandledEvent(ProfessionContext context, ProfessionalPlayer player) {
        OccupationEvents.BEFORE_ACTION_HANDLED_EVENT.invoker().onHandleAction(context, player);
    }

    @Override
    public void professionJoinEvent(ProfessionalPlayer player, Profession profession, OccupationSlot slot, ServerPlayer serverPlayer) {
        OccupationEvents.PROFESSION_JOIN_EVENT.invoker().onProfessionJoin(player, profession, slot, serverPlayer);
    }

    @Override
    public void professionLeaveEvent(ProfessionalPlayer player, Profession profession, ServerPlayer serverPlayer) {
        OccupationEvents.PROFESSION_LEAVE_EVENT.invoker().onProfessionLeave(player, profession, serverPlayer);
    }

    @Override
    public void sendSyncRequest(ServerPlayer player) {
        ServerHandler.sendSyncRequest(player);
    }

    @Override
    public ClientNetworking getClientNetworking() {
        return ProfessionsClient.clientHandler;
    }

    @Override
    public boolean skipReward(RewardType type) {
        return type.equals(Rewards.PAYMENT_REWARD);
    }

    @Override
    public Path getRootConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("professions");
    }

    @Override
    public Component displayInformation(AbstractAction action, Map<RewardType, Component> map) {
        return Component.translatable(" (%s | %s%s)",
                map.get(Rewards.PAYMENT_REWARD),
                map.get(Rewards.EXPERIENCE_REWARD),
                action.extraRewardInformation(map));
    }
}
