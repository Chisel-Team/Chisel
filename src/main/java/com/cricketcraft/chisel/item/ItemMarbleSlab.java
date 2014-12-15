package com.cricketcraft.chisel.item;

import com.cricketcraft.chisel.block.BlockMarbleSlab;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMarbleSlab extends ItemCarvable
{

    public ItemMarbleSlab(Block block)
    {
        super(block);
    }

    @Override
    public boolean func_150936_a(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack stack)
    {
        BlockMarbleSlab block = (BlockMarbleSlab) Block.getBlockFromItem(this);

        switch(side)
        {
            case 0:
                --y;
                break;
            case 1:
                ++y;
                break;
            case 2:
                --z;
                break;
            case 3:
                ++z;
                break;
            case 4:
                --x;
                break;
            case 5:
                ++x;
                break;
        }

        Block targetBlock = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        return (targetBlock.equals(block) || targetBlock.equals(block.top)) && meta == stack.getItemDamage() || world.canPlaceEntityOnSide(block, x, y, z, false, side, (Entity) null, stack);

    }


    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hx, float hy, float hz)
    {
        BlockMarbleSlab block = (BlockMarbleSlab) Block.getBlockFromItem(this);

        Block targetBlock = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        boolean metaMatches = meta == stack.getItemDamage();

        if(metaMatches && side == 0 && targetBlock.equals(block.top))
        {
            world.setBlock(x, y, z, block.master, meta, 2);
            stack.stackSize -= 1;
            return true;
        } else if(metaMatches && side == 1 && targetBlock.equals(block.bottom))
        {
            world.setBlock(x, y, z, block.master, meta, 2);
            stack.stackSize -= 1;
            return true;
        }

        boolean result = super.onItemUse(stack, player, world, x, y, z, side, hz, hy, hz);


        switch(side)
        {
            case 0:
                --y;
                break;
            case 1:
                ++y;
                break;
            case 2:
                --z;
                break;
            case 3:
                ++z;
                break;
            case 4:
                --x;
                break;
            case 5:
                ++x;
                break;
        }

        targetBlock = world.getBlock(x, y, z);
        meta = world.getBlockMetadata(x, y, z);

        if(!result && (targetBlock.equals(block.top) || targetBlock.equals(block.bottom)) && meta == stack.getItemDamage())
        {
            world.setBlock(x, y, z, block.master, meta, 2);
            return true;
        }

        if(!result)
            return false;

        if(side != 0 && (side == 1 || hy <= 0.5D))
            return true;


        //TODO allow top slabs
        //world.setBlock(x, y, z, block.top, meta, 2);
        return true;
    }
}
