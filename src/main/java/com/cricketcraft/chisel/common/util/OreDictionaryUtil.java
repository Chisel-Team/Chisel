package com.cricketcraft.chisel.common.util;

import com.cricketcraft.chisel.common.block.BlockCarvable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Ore dictionary utility
 *
 * @author minecreatr
 */
public class OreDictionaryUtil {


    public static void add(BlockCarvable block) {
        for (int i=0;i<block.getTotalVariations();i++) {
            OreDictionary.registerOre(block.getType().getOredictName(), new ItemStack(block, 1, i));
        }
    }

    public static List<ItemStack> getVariations(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for (int id : ids) {
            stacks.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        }
        return stacks;
    }
}
