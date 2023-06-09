package org.tbm.server.dungeons.dungeons.professions.integration.wthit;

import org.tbm.server.dungeons.dungeons.professions.Constants;
import org.tbm.server.dungeons.dungeons.professions.ProfessionsFabric;
import org.tbm.server.dungeons.dungeons.professions.api.ProfessionalPlayer;
import org.tbm.server.dungeons.dungeons.professions.config.ProfessionConfig;
import org.tbm.server.dungeons.dungeons.professions.profession.unlock.Unlock;
import org.tbm.server.dungeons.dungeons.professions.profession.unlock.Unlocks;
import mcp.mobius.waila.api.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.UUID;

public class ProfWailaPlugin implements IWailaPlugin, IBlockComponentProvider, IServerDataProvider<Block> {

    public static final ResourceLocation LOCKED = Constants.modID("locked");

    @Override
    public void register(IRegistrar registrar) {
        registrar.addConfig(LOCKED, true);
        registrar.addComponent(this, TooltipPosition.BODY, Block.class);
    }


    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (config.getBoolean(LOCKED)) {
            UUID uuid = accessor.getPlayer().getUUID();
            ProfessionalPlayer pPlayer = ProfessionsFabric.getInstance().getPlayerManager().getPlayer(uuid);
            if (pPlayer == null) {
                return;
            }
            Block block = accessor.getBlock();
            for (Unlock.Singular<Block> blockSingular : pPlayer.getLockedKnowledge(Unlocks.BLOCK_DROP_UNLOCK, block)) {
                tooltip.addLine(Component.literal("❌ ").setStyle(Style.EMPTY.withColor(ProfessionConfig.errors))
                        .append(Component.translatable(blockSingular.getType().getTranslationKey(),
                                        blockSingular.getProfessionDisplay(),
                                        blockSingular.createUnlockComponent())
                                .setStyle(Style.EMPTY.withColor(ProfessionConfig.descriptors))));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag data, IServerAccessor accessor, IPluginConfig config) {

    }
}
