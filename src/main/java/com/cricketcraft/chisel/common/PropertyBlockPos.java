package com.cricketcraft.chisel.common;

import net.minecraft.util.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

/**
 * Property for block position
 *
 * @author minecreatr
 */
public class PropertyBlockPos implements IUnlistedProperty<BlockPos>{

    public String getName(){
        return "pos";
    }

    public boolean isValid(BlockPos var){
        return true;
    }

    public Class getType(){
        return BlockPos.class;
    }

    public String valueToString(BlockPos pos){
        return pos.toString();
    }
}
