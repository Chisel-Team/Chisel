package team.chisel.common.util;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.nbt.CompoundTag;

public interface NBTSaveable {

    void write(CompoundTag tag);

    void read(CompoundTag tag);
}
