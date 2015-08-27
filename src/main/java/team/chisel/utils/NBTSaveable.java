package team.chisel.utils;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTSaveable {

	void write(NBTTagCompound tag);

	void read(NBTTagCompound tag);
}