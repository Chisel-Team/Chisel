package info.jbcs.minecraft.chisel.api;

import info.jbcs.minecraft.chisel.carving.CarvableVariation;

public interface ICarvable
{
    public CarvableVariation getVariation(int metadata);
}
