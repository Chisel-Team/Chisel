package team.chisel.common.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryUtil {

    public static List<ItemStack> getVariations(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for (int id : ids) {
            stacks.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        }
        return stacks;
    }
}
