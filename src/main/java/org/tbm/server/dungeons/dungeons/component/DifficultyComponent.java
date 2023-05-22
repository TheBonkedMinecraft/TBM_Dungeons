package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.tbm.server.dungeons.dungeons.Dungeons;

import java.util.concurrent.atomic.AtomicInteger;

public class DifficultyComponent implements IDifficultyComponent, AutoSyncedComponent {

    private final AtomicInteger value = new AtomicInteger(1);
    private final Object provider;

    public DifficultyComponent(PlayerEntity player) {
        this.provider = player;
    }
    @Override
    public int getValue() {
        return this.value.get();
    }

    @Override
    public void setValue(int value) {
        this.value.set(value);
        Dungeons.DIFFICULTY_SETTING.sync(this.provider);
    }

    @Override public void readFromNbt(NbtCompound tag) { this.value.set(tag.getInt("value")); }
    @Override public void writeToNbt(NbtCompound tag) { tag.putInt("value", this.value.get()); }
    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.provider;
    }
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        this.writeSyncPacket(buf);
    }
    private void writeSyncPacket(PacketByteBuf buf) {
        buf.writeVarInt(this.value.get());
    }
    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.value.set(buf.readVarInt());
    }
}
