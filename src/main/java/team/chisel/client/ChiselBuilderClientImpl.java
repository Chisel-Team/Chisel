package team.chisel.client;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.block.ChiselBlockBuilder;
import team.chisel.api.block.ChiselRecipe;
import team.chisel.api.block.ClientVariationData;
import team.chisel.api.block.VariationData;
import team.chisel.api.render.ChiselFace;
import team.chisel.common.util.json.JsonHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for builder stuff
 */
public class ChiselBuilderClientImpl implements ChiselBlockBuilder.VariationBuilder.IChiselBuilderInterface {

    @Override
    public VariationData build(String name, int index, ChiselRecipe recipe, ItemStack smeltedFrom, int amountSmelted, int light, float hardness,
                               boolean beaconBase, ResourceLocation texLocation, Map<EnumFacing, ResourceLocation> overrideMap){
        ChiselFace defaultFace = JsonHelper.getOrCreateFace(texLocation);
        Map<EnumFacing, ChiselFace> faceMap = new HashMap<EnumFacing, ChiselFace>();
        for (Map.Entry<EnumFacing, ResourceLocation> entry : overrideMap.entrySet()){
            faceMap.put(entry.getKey(), JsonHelper.getOrCreateFace(entry.getValue()));
        }
        return new ClientVariationData(name, recipe, smeltedFrom, amountSmelted, light, hardness, beaconBase, index, defaultFace, faceMap);
    }

}