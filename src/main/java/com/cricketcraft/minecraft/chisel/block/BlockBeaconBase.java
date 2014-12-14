package com.cricketcraft.minecraft.chisel.block;

import net.minecraft.world.IBlockAccess;

/**
 * Created by Pokefenn
 */
public class BlockBeaconBase extends BlockCarvable
{

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
    {
        return true;
    }
}
