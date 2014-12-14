package com.cricketcraft.minecraft.chisel.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import com.cricketcraft.minecraft.chisel.carving.CarvableHelper;
import com.cricketcraft.minecraft.chisel.carving.CarvableVariation;
import com.cricketcraft.minecraft.chisel.item.ItemCarvable;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockMarbleStairsMaker
{
    public CarvableHelper carverHelper;
    BlockMarbleStairs blocks[];

    //int idStart;
    Block blockBase;
    String blockName;

    public BlockMarbleStairsMaker(Block base)
    {
        carverHelper = new CarvableHelper();
        blockBase = base;
    }

    public void create(String name)
    {
        create(null, name);
    }

    public void create(BlockMarbleStairsMakerCreator creator, String name)
    {
        blocks = new BlockMarbleStairs[carverHelper.variations.size() / 2];
        for(int i = 0; i < blocks.length; i++)
        {
            String n = name + "." + i;
            blocks[i] = creator == null ?
                    new BlockMarbleStairs(blockBase, i * 2, carverHelper) :
                    creator.create(blockBase, i * 2, carverHelper);

            blocks[i].setBlockName(n);
            GameRegistry.registerBlock(blocks[i], ItemCarvable.class, n);

            for(int meta = 0; meta < 2 && i * 2 + meta < carverHelper.variations.size(); meta++)
            {
                CarvableVariation variation = carverHelper.variations.get(i * 2 + meta);

                for(int j = 0; j < 8; j++)
                    carverHelper.registerVariation(name + ".orientation." + j, variation, blocks[i], j + meta * 8);

                CraftingManager.getInstance().addRecipe(new ItemStack(blocks[i], 4, meta * 8), new Object[]{"*  ", "** ", "***", '*', new ItemStack(blockBase, 1, i * 2 + meta)});
            }

            CarvableHelper.chiselBlocks.add(blocks[i]);
        }

    }

}
