package com.cricketcraft.chisel.common.block;

import com.cricketcraft.chisel.common.CarvableBlocks;
import com.cricketcraft.chisel.common.variation.Variation;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Class for the item for the chisel block
 *
 * @author minecreatr
 */
public class ItemChiselBlock extends ItemBlock{

    private Variation[] variations;

    public ItemChiselBlock(Block block){
        super(block);
        BlockCarvable b = (BlockCarvable)block;
        this.setHasSubtypes(true);
        this.variations= CarvableBlocks.getBlock(b).getVariants();
    }


    public String getUnlocalizedName(ItemStack stack){
        Variation curVariation = this.variations[stack.getMetadata()];
        return super.getUnlocalizedName(stack)+"."+curVariation;
    }

    public int getMetadata(int meta){
        return meta;
    }

//    public int getMetadata(ItemStack stack){
//        return stack.getMetadata();
//    }
}
