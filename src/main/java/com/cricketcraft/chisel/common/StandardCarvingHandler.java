package com.cricketcraft.chisel.common;

import com.cricketcraft.chisel.api.carving.CarvingVariationRepresentation;
import com.cricketcraft.chisel.api.carving.ICarvingHandler;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.variation.Variation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * The regular carving handler
 *
 * @author minecreatr
 */
public class StandardCarvingHandler implements ICarvingHandler{

    public CarvingVariationRepresentation[] getCarvingVariations(Item item){
        BlockCarvable block = (BlockCarvable) Block.getBlockFromItem(item);
        return block.getType().getCarvingVariations();

    }

    public ItemStack carveItem(ItemStack stack, Variation variation){
        BlockCarvable block = (BlockCarvable) Block.getBlockFromItem(stack.getItem());
        stack.setItemDamage(Variation.metaFromVariation(block.getType(), variation));
        return stack;
    }
}
