package com.cricketcraft.chisel.common;

import net.minecraft.util.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Property for block position
 *
 * @author minecreatr
 */
public class PropertyBlockPos implements IUnlistedProperty<BlockPos>{

    @Override
    public String getName(){
        return "pos";
    }

    @Override
    public boolean isValid(BlockPos var){
        return true;
    }

    @Override
    public Class getType(){
        return BlockPos.class;
    }

    @Override
    public String valueToString(BlockPos pos){
        return pos.toString();
    }
}
