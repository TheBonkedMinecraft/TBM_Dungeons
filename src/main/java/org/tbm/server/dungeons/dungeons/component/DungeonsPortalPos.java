package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class DungeonsPortalPos implements BlockPosComponent, AutoSyncedComponent {

    private BlockPos pos = new BlockPos(0,0,0);

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

    @Override
    public void setBlockPos(BlockPos pos) {
        this.pos = pos;
    }

    @Override public void readFromNbt(NbtCompound tag) { this.pos = BlockPos.fromLong(tag.getLong("dungeonsPos")); }
    @Override public void writeToNbt(NbtCompound tag) { tag.putLong("dungeonsPos", this.pos.asLong()); }

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
