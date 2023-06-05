package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class LastUpdatedDifficulty implements ILastUpdatedDifficultyComponent, AutoSyncedComponent {

    long value = 0;

    Object provider;

    public LastUpdatedDifficulty(PlayerEntity player) {
        this.provider = player;
    }

    @Override
    public long getValue() {
        return this.value;
    }

    @Override
    public void setValue(long value) {
        this.value = value;
        ModComponents.LAST_UPDATED.sync(this.provider);
    }

    @Override public void readFromNbt(NbtCompound tag) { this.value = tag.getLong("value"); }
    @Override public void writeToNbt(NbtCompound tag) { tag.putLong("value", this.value); }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.provider;
    }
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        this.writeSyncPacket(buf);
    }
    private void writeSyncPacket(PacketByteBuf buf) {
        buf.writeVarLong(this.value);
    }
    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.value = buf.readVarLong();
    }
}
