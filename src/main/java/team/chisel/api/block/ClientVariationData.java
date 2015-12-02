package team.chisel.api.block;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import team.chisel.api.render.ChiselFace;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.RenderType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Version of VariationData used on the client
 */
public class ClientVariationData extends VariationData {


    /**
     * The Default chisel texture for the sides of this block
     */
    public ChiselFace defaultFace;

    /**
     * The chisel texture for the sides of this block
     */
    private Map<EnumFacing, ChiselFace> sideOverrides;

    private List<IBlockRenderType<? extends IBlockRenderContext>> typesUsed;
    
    public ClientVariationData(String name, IRecipe recipe, ItemStack smeltedFrom, int amountSmelted, int light, float hardness, boolean beaconBase,
                               ChiselFace defaultFace, Map<EnumFacing, ChiselFace> sideOverrides){
        super(name, recipe, smeltedFrom, amountSmelted, light, hardness, beaconBase);
        this.defaultFace = defaultFace;
        this.sideOverrides = sideOverrides;
        this.typesUsed = new ArrayList<IBlockRenderType<? extends IBlockRenderContext>>();
        for (ChiselFace face : sideOverrides.values()){
            for (IChiselTexture<? extends IBlockRenderContext> texture : face.getTextureList()) {
                if (!typesUsed.contains(texture.getBlockRenderType())){
                    typesUsed.add(texture.getBlockRenderType());
                }
            }
        }
        for (IChiselTexture<? extends IBlockRenderContext> texture : defaultFace.getTextureList()) {
            if (!typesUsed.contains(texture.getBlockRenderType())) {
                typesUsed.add(texture.getBlockRenderType());
            }
        }
    }

    public List<IBlockRenderType<? extends IBlockRenderContext>> getTypesUsed() {
        return this.typesUsed;
    }

    public ChiselFace getFaceForSide(EnumFacing facing){
        if (facing == null){
            return defaultFace;
        }
        else if (sideOverrides.containsKey(facing)){
            return sideOverrides.get(facing);
        }
        else {
            return defaultFace;
        }
    }

}