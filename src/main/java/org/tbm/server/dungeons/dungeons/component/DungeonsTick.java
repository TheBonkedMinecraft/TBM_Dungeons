package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class DungeonsTick implements IDungeonsTickComponent, AutoSyncedComponent {
    private int value = 72000;
    private final Object provider;

    public DungeonsTick(PlayerEntity player) {
        this.provider = player;
    }


    @Override public int getValue() { return this.value; }
    @Override
    public void setValue(int value) {
        this.value = value;
    }
    @Override public void decrement() {
        if (this.value >= 0) {
            this.value--;
            ModComponents.DUNGEONS_TICK.sync(this.provider);
        }
    }
    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.provider;
    }
    @Override public void readFromNbt(NbtCompound tag) { this.value = tag.getInt("value"); }
    @Override public void writeToNbt(NbtCompound tag) { tag.putInt("value", this.value); }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        this.writeSyncPacket(buf);
    }

    private void writeSyncPacket(PacketByteBuf buf) {
        buf.writeVarInt(this.value);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.value = buf.readVarInt();
    }

}