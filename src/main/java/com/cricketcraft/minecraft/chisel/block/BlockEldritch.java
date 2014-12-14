package com.cricketcraft.minecraft.chisel.block;

import com.cricketcraft.minecraft.chisel.Chisel;


public class BlockEldritch extends BlockCarvable
{

    public BlockEldritch()
    {
        super();
    }

    @Override
    public int getRenderType()
    {
        return Chisel.RenderEldritchId;
    }

}
