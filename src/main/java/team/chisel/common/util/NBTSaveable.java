package team.chisel.common.util;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.nbt.NBTTagCompound;

@ParametersAreNonnullByDefault
public interface NBTSaveable {

    void write(NBTTagCompound tag);

    void read(NBTTagCompound tag);
}
