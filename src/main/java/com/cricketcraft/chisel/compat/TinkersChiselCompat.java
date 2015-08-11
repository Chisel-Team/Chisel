package com.cricketcraft.chisel.compat;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.carving.Carving;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Combat for the tinkers chisel, in its own class as to not cause clutter
 *
 * @author minecreatr
 */
public class TinkersChiselCompat {


    public static final String TINKERS_ID = "TConstruct";

    private static Class TINKERS_MOD_CLASS;
    private static Class DETAILING_CLASS;
    private static Class DETAIL_INPUT_CLASS;

    private static Method GET_DETAILING;

    private static Field DETAIL_FIELD;

    private static Field INPUT_STACK;
    private static Field INPUT_META;
    private static Field OUTPUT_STACK;
    private static Field OUTPUT_META;

    public static void addTConstructDetailingSupport(){
        if (Loader.isModLoaded(TINKERS_ID)){
            loadReflection();
            List<ItemStack> stacks = getAllChiseling();
            for (ItemStack stack : stacks) {
                if (Block.getBlockFromItem(stack.getItem()) != null) {
                    Chisel.logger.info("Added variation for stack "+stack.toString());
                    Carving.chisel.addVariation("tinkers", Block.getBlockFromItem(stack.getItem()), stack.getItemDamage(), 0);
                } else {
                    Chisel.logger.info("Item "+stack.getItem()+" has no block form");
                }
            }
        }
    }


    private static void loadReflection(){
        if (TINKERS_MOD_CLASS == null || DETAILING_CLASS == null || DETAIL_INPUT_CLASS == null || GET_DETAILING == null || DETAIL_FIELD == null) {
            try {
                TINKERS_MOD_CLASS = Class.forName("tconstruct.TConstruct");
                DETAILING_CLASS = Class.forName("tconstruct.library.crafting.Detailing");
                DETAIL_INPUT_CLASS = Class.forName("tconstruct.library.crafting.Detailing$DetailInput");

                GET_DETAILING = TINKERS_MOD_CLASS.getDeclaredMethod("getChiselDetailing");

                DETAIL_FIELD = DETAILING_CLASS.getDeclaredField("detailing");

                INPUT_STACK = DETAIL_INPUT_CLASS.getDeclaredField("input");
                INPUT_META = DETAIL_INPUT_CLASS.getDeclaredField("inputMeta");
                OUTPUT_STACK = DETAIL_INPUT_CLASS.getDeclaredField("output");
                OUTPUT_META = DETAIL_INPUT_CLASS.getDeclaredField("outputMeta");

            } catch (Exception exception) {
                exception.printStackTrace();
                Chisel.logger.error("Error adding tinkers detailing support");
            }
        }
    }

    private static List<ItemStack> getAllChiseling(){
        try {
            List<ItemStack> toReturn = new ArrayList<ItemStack>();
            Object detailingObj = GET_DETAILING.invoke(null);
            Object detailList = DETAIL_FIELD.get(detailingObj);
            if (detailList instanceof List){
                List list = (List) detailList;
                for (Object detail : list){
                    ItemStack stackInput = (ItemStack) INPUT_STACK.get(detail);
                    int inMeta = (Integer) INPUT_META.get(detail);

                    ItemStack stackOutput = (ItemStack) OUTPUT_STACK.get(detail);
                    int outMeta = (Integer) OUTPUT_META.get(detail);

                    stackInput.setItemDamage(inMeta);
                    stackOutput.setItemDamage(outMeta);
                    toReturn.add(stackInput);
                    toReturn.add(stackOutput);
                }
            }
            return toReturn;
        } catch (Exception exception){
            exception.printStackTrace();
            Chisel.logger.error("Error adding tinkers detailing support");
            return null;
        }
    }
}
