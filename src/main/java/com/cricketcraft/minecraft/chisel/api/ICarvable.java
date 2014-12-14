package com.cricketcraft.minecraft.chisel.api;

import com.cricketcraft.minecraft.chisel.carving.CarvableVariation;

public interface ICarvable
{
    public CarvableVariation getVariation(int metadata);
}
