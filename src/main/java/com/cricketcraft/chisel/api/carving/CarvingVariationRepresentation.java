package com.cricketcraft.chisel.api.carving;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * Holds the data for a carving variation, ie block and meta
 *
 * @author minecreatr
 */
public class CarvingVariationRepresentation {

    /**
     * The block
     */
    private Block block;

    /**
     * The metadata
     */
    private int meta;

    public CarvingVariationRepresentation(Block block, int meta){
        this.block=block;
        this.meta=meta;
    }

    public Block getBlock(){
        return this.block;
    }

    public int getMeta(){
        return this.meta;
    }

    /**
     * Gets the item stack for this carving variation for inventory purposes
     * @return The Item Stack
     */
    public ItemStack getStack(){
        return new ItemStack(getBlock(), 1, getMeta());
    }

    public boolean equals(Object object){
        if (object instanceof CarvingVariationRepresentation){
            CarvingVariationRepresentation o = (CarvingVariationRepresentation)object;
            if ((o.getBlock()==getBlock())&&(o.getMeta()==getMeta())){
                return true;
            }
        }
        return false;
    }
}
