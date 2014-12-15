package com.cricketcraft.chisel.api;

import com.cricketcraft.chisel.carving.CarvableVariation;

public interface ICarvable
{
    public CarvableVariation getVariation(int metadata);
}
