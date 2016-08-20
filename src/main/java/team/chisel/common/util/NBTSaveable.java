package team.chisel.common.util;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTSaveable {

    void write(NBTTagCompound tag);

    void read(NBTTagCompound tag);
}
