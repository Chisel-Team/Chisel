package team.chisel.common.util;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.nbt.CompoundNBT;

@ParametersAreNonnullByDefault
public interface NBTSaveable {

    void write(CompoundNBT tag);

    void read(CompoundNBT tag);
}
