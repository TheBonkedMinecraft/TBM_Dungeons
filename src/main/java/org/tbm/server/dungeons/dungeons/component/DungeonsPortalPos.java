package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class DungeonsPortalPos implements IDungeonsPortalPosComponent, AutoSyncedComponent {

    private BlockPos pos = new BlockPos(0,0,0);

    Object provider;

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }
    public DungeonsPortalPos(PlayerEntity player) {
        this.provider = player;
    }

    @Override
    public void setBlockPos(BlockPos pos) {
        this.pos = pos;
        ModComponents.PORTAL_POS.sync(this.provider);
    }

    @Override public void readFromNbt(NbtCompound tag) { this.pos = BlockPos.fromLong(tag.getLong("dungeonsPos")); }
    @Override public void writeToNbt(NbtCompound tag) { tag.putLong("dungeonsPos", this.pos.asLong()); }
    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.provider;
    }
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        this.writeSyncPacket(buf);
    }

    private void writeSyncPacket(PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.pos = buf.readBlockPos();
    }
}
