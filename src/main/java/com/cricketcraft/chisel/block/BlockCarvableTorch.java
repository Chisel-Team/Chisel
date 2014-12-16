package com.cricketcraft.chisel.block;

import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.init.ModTabs;

public class BlockCarvableTorch extends BlockCarvable
{
    public CarvableHelper carverHelper;

    public BlockCarvableTorch()
    {
        super();

        carverHelper = new CarvableHelper();

        setCreativeTab(ModTabs.tabChiselBlocks);
    }
    @Override
    public int getRenderType()
    {
        return 2;
    }
}
