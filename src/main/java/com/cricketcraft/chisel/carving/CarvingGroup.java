package com.cricketcraft.chisel.carving;

import java.util.ArrayList;

public class CarvingGroup
{
    public CarvingGroup(String n)
    {
        name = n;
    }

    String name;
    String className;
    String sound;
    String oreName;

    ArrayList<CarvingVariation> variations = new ArrayList<CarvingVariation>();
}