package org.tbm.server.dungeons.dungeons.component;

import net.minecraft.nbt.NbtCompound;

public class ModifiedMobTrack implements IModifiedMobComponent{
    boolean value = false;
    @Override
    public boolean getValue() {
        return this.value;
    }

    @Override
    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.value = tag.getBoolean("value");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("value", this.value);
    }
}
