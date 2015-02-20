package com.chiselmod.chisel.common.block.subblocks;

import com.chiselmod.chisel.common.block.BlockCarvable;

/**
 * Implementation of ISubBlock
 *
 * @author minecreatr
 */
public class SubBlock implements ISubBlock{

    protected String name;

    protected BlockCarvable parent;

    public SubBlock(String name, BlockCarvable parent){
        this.name=name;
        this.parent=parent;
    }

    public String getName(){
        return this.name;
    }

    public BlockCarvable getParent(){
        return this.parent;
    }
}
