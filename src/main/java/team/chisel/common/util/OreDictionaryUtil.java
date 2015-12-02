package team.chisel.common.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Ore dictionary utility
 *
 * @author minecreatr
 */
public class OreDictionaryUtil {

//    public static void add(BlockCarvable block) {
//        for (int i=0;i<block.getTotalVariations();i++) {
//            OreDictionary.registerOre(block.getType().getOredictName(), new ItemStack(block, 1, i));
//        }
//    }
//
//    public static void addHonorary(CarvableBlocks block, ItemStack stack){
//        OreDictionary.registerOre(block.getOredictName(), stack);
//    }

    public static List<ItemStack> getVariations(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for (int id : ids) {
            stacks.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        }
        return stacks;
    }
}
