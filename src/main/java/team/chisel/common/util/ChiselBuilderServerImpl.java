package team.chisel.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.block.ChiselBlockBuilder;
import team.chisel.api.block.ChiselRecipe;
import team.chisel.api.block.VariationData;

import java.util.Map;

/**
 * Implementation for builder stuff
 */
public class ChiselBuilderServerImpl implements ChiselBlockBuilder.VariationBuilder.IVariationBuilderDelegate {

    @Override
    public VariationData build(String name, String group, int index, ChiselRecipe recipe, ItemStack smeltedFrom, int amountSmelted, int light, float hardness,
                               boolean beaconBase, ResourceLocation texLocation, Map<EnumFacing, ResourceLocation> overrideMap){
        return new VariationData(name, group, recipe, smeltedFrom, amountSmelted, light, hardness, beaconBase, index);
    }

}
