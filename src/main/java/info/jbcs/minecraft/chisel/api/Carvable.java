package info.jbcs.minecraft.chisel.api;

import info.jbcs.minecraft.chisel.CarvableVariation;

public interface Carvable {
	public CarvableVariation getVariation(int metadata);
}
