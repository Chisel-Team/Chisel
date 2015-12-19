package team.chisel.api.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;

/**
 * The Version of VariationData used on the client
 */
public class ClientVariationData extends VariationData {


    /**
     * The Default chisel texture for the sides of this block
     */
    public ResourceLocation defaultFace;

    /**
     * The chisel texture for the sides of this block
     */
    public Map<EnumFacing, ResourceLocation> sideOverrides;
    
    public ClientVariationData(String name, String group, ChiselRecipe recipe, ItemStack smeltedFrom, int amountSmelted,
                               int light, float hardness, boolean beaconBase, int index,
                               ResourceLocation defaultFace, Map<EnumFacing, ResourceLocation> sideOverrides){
        super(name, group, recipe, smeltedFrom, amountSmelted, light, hardness, beaconBase, index);
        this.defaultFace = defaultFace;
        this.sideOverrides = sideOverrides;
    }
}