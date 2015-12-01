package team.chisel.api.block;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
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
    public IChiselTexture<? extends IBlockRenderContext> defaultTexture;

    /**
     * The chisel texture for the sides of this block
     */
    private Map<EnumFacing, IChiselTexture<? extends IBlockRenderContext>> sideOverrides;

    private List<IBlockRenderType<? extends IBlockRenderContext>> typesUsed;
    
    public ClientVariationData(String name, IRecipe recipe, ItemStack smeltedFrom, int amountSmelted, int light, float hardness, boolean beaconBase,
                               IChiselTexture<? extends IBlockRenderContext> defaultTexture, Map<EnumFacing, IChiselTexture<? extends IBlockRenderContext>> sideOverrides){
        super(name, recipe, smeltedFrom, amountSmelted, light, hardness, beaconBase);
        this.defaultTexture = defaultTexture;
        this.sideOverrides = sideOverrides;
        this.typesUsed = new ArrayList<IBlockRenderType<? extends IBlockRenderContext>>();
        List<IBlockRenderType<? extends IBlockRenderContext>> allTypes = new ArrayList<IBlockRenderType<? extends IBlockRenderContext>>();
        for (IChiselTexture<? extends IBlockRenderContext> texture : sideOverrides.values()){
            allTypes.addAll(texture.getBlockRenderTypes());
        }
        for (IBlockRenderType<? extends IBlockRenderContext> type : allTypes){
            if (!typesUsed.contains(type)){
                typesUsed.add(type);
            }
        }
        for (IBlockRenderType<? extends IBlockRenderContext> type : defaultTexture.getBlockRenderTypes()) {
            if (!typesUsed.contains(type)) {
                typesUsed.add(type);
            }
        }
    }

    public List<IBlockRenderType<? extends IBlockRenderContext>> getTypesUsed() {
        return this.typesUsed;
    }

    public IChiselTexture<? extends IBlockRenderContext> getTextureForSide(EnumFacing facing){
        if (sideOverrides.containsKey(facing)){
            return sideOverrides.get(facing);
        }
        else {
            return defaultTexture;
        }
    }

}