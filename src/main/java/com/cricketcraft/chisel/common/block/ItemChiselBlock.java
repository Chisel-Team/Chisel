package com.cricketcraft.chisel.common.block;

import com.cricketcraft.chisel.Chisel;
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
        CarvableBlocks bl = CarvableBlocks.getBlock(b);
        this.setHasSubtypes(true);
        if (b.getIndex()==0) {
            this.variations = bl.getVariants();
        } else {
            int left = (bl.getVariants().length%16);
            Variation[] var = new Variation[left];
            int index = b.getIndex()*16;
            int cur = 0;
            for (int i=0;i<bl.getVariants().length;i++){
                if (i>=index&&cur<=var.length){
                    if (bl.getVariants()[i]==null){
                        continue;
                    }
                    var[cur]=bl.getVariants()[i];
                    cur++;
                }
            }
            this.variations=var;
        }
    }


    public String getUnlocalizedName(ItemStack stack){
        try {
            Variation curVariation = this.variations[stack.getMetadata()];
            return super.getUnlocalizedName(stack)+"."+curVariation;
        } catch (IndexOutOfBoundsException e){
            Chisel.logger.info("Meta is: "+stack.getMetadata()+" and length is "+this.variations.length);
            return super.getUnlocalizedName(stack)+"."+"null";
        }
    }

    public int getMetadata(int meta){
        return meta;
    }

//    public int getMetadata(ItemStack stack){
//        return stack.getMetadata();
//    }
}
