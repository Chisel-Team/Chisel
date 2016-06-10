package team.chisel.common.util;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.block.ChiselBlockBuilder;
import team.chisel.api.block.ChiselRecipe;
import team.chisel.api.block.VariationData;

/**
 * Implementation for builder stuff
 */
public class BuilderDelegateServer implements ChiselBlockBuilder.VariationBuilder.IVariationBuilderDelegate {

    @Override
    public VariationData build(String name, String group, int index, ChiselRecipe recipe, ItemStack smeltedFrom, int amountSmelted, boolean opaque, ResourceLocation texLocation,
            Map<EnumFacing, ResourceLocation> overrideMap) {
        return new VariationData(name, group, recipe, smeltedFrom, amountSmelted, index, opaque);
    }

}
