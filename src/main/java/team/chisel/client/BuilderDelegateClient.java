package team.chisel.client;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.block.ChiselBlockBuilder;
import team.chisel.api.block.ChiselRecipe;
import team.chisel.api.block.ClientVariationData;
import team.chisel.api.block.VariationData;

public class BuilderDelegateClient implements ChiselBlockBuilder.VariationBuilder.IVariationBuilderDelegate {

    @Override
    public VariationData build(String name, String group, int index, ChiselRecipe recipe, ItemStack smeltedFrom, int amountSmelted, ResourceLocation texLocation,
            Map<EnumFacing, ResourceLocation> overrideMap) {
        return new ClientVariationData(name, group, recipe, smeltedFrom, amountSmelted, index, texLocation, overrideMap);
    }
}