package team.chisel.common.util;

import net.minecraft.nbt.CompoundTag;

public interface NBTSaveable {

    void write(CompoundTag tag);

    void read(CompoundTag tag);
}
