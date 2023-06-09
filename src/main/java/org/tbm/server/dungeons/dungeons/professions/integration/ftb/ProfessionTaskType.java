package org.tbm.server.dungeons.dungeons.professions.integration.ftb;

import org.tbm.server.dungeons.dungeons.professions.PlayerManager;
import org.tbm.server.dungeons.dungeons.professions.ProfessionsFabric;
import org.tbm.server.dungeons.dungeons.professions.api.ProfessionalPlayer;
import org.tbm.server.dungeons.dungeons.professions.config.ProfessionConfig;
import org.tbm.server.dungeons.dungeons.professions.profession.Profession;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.BooleanTask;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;


public class ProfessionTaskType extends BooleanTask {
    private ResourceLocation professionKey = new ResourceLocation("professions:unknown");
    private ProfessionEventAction action;
    private int level;

    public ProfessionTaskType(Quest q) {
        super(q);
        this.action = ProfessionEventAction.JOIN;
        this.level = -1;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("prof", professionKey.toString());
        nbt.putString("action", action.name());
        nbt.putInt("level", level);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.professionKey = new ResourceLocation(nbt.getString("prof"));
        this.action = ProfessionEventAction.valueOf(nbt.getString("action"));
        this.level = nbt.getInt("level");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeResourceLocation(professionKey);
        buffer.writeEnum(action);
        buffer.writeVarInt(level);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.professionKey = buffer.readResourceLocation();
        this.action = buffer.readEnum(ProfessionEventAction.class);
        this.level = buffer.readVarInt();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void getConfig(ConfigGroup config) {
        super.getConfig(config);
        TooltipList tooltipList = new TooltipList();
        tooltipList.add(Component.literal("This should be a key from the datapack professions."));
        config.addString("profession", professionKey.toString(), s -> {
            this.professionKey = new ResourceLocation(s);
        }, "professions:unknown").addInfo(tooltipList);
        config.addEnum("action", action, professionEventAction -> {
            this.action = professionEventAction;
        }, NameMap.of(this.action, ProfessionEventAction.values()).create(), ProfessionEventAction.JOIN);
        config.addInt("level", level, integer -> {
            this.level = integer;
        }, -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void addMouseOverText(TooltipList list, TeamData teamData) {
        list.blankLine();
        if (action == ProfessionEventAction.JOIN) {
            list.add(Component.translatable("ftbquests.task.professions.profession.join_req",
                            Component.literal(professionKey.toString()).setStyle(Style.EMPTY.withColor(ProfessionConfig.descriptors)))
                    .setStyle(Style.EMPTY.withColor(ProfessionConfig.headerBorders)));
        } else {
            list.add(Component.translatable("ftbquests.task.professions.profession.level_req",
                            Component.literal(professionKey.toString()).setStyle(Style.EMPTY.withColor(ProfessionConfig.descriptors)),
                            Component.literal(String.valueOf(level)).setStyle(Style.EMPTY.withColor(ProfessionConfig.descriptors)))
                    .setStyle(Style.EMPTY.withColor(ProfessionConfig.headerBorders)));
        }
        super.addMouseOverText(list, teamData);
    }

    @Override
    public boolean consumesResources() {
        return true;
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer serverPlayer) {
        ProfessionsFabric mod = ProfessionsFabric.getInstance();
        PlayerManager manager = mod.getPlayerManager();
        Profession profession = mod.getProfessionLoader().getProfession(professionKey);
        ProfessionalPlayer player = manager.getPlayer(serverPlayer.getUUID());
        if (profession == null || player == null) {
            return false;
        }
        if (action == ProfessionEventAction.JOIN && player.isOccupationActive(profession)) {
            return true;
        } else if (action == ProfessionEventAction.LEVEL && player.getOccupation(profession).getLevel() >= level) {
            return true;
        }

        return false;
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 20;
    }

    @Override
    public TaskType getType() {
        return FTBIntegration.PROFESSION;
    }
}
