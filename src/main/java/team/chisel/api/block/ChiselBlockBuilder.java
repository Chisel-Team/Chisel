package team.chisel.api.block;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.client.render.ChiselModelRegistry;
import team.chisel.common.init.BlockRegistry;

/**
 * Building a ChiselBlockData
 */
@Setter
@Accessors(chain = true)
public class ChiselBlockBuilder<T extends Block & ICarvable> {

    private final Material material;
    private final String domain;
    private final String blockName;
    
    private SoundType sound;

    private int curIndex;

    private List<VariationBuilder<T>> variations;
        
    private BlockProvider<T> provider;

    protected ChiselBlockBuilder(Material material, String domain, String blockName, BlockProvider<T> provider){
        this.material = material;
        this.domain = domain;
        this.blockName = blockName;
        this.provider = provider;
        this.variations = new ArrayList<VariationBuilder<T>>();
    }

    public VariationBuilder<T> newVariation(String name, String group){
        VariationBuilder<T> builder = new VariationBuilder<>(this, name, group, curIndex);
        curIndex++;
        return builder;
    }

    @SuppressWarnings("unchecked")
    public T[] build() {
        if (variations.size() == 0){
            throw new IllegalArgumentException("Must have at least one variation!");
        }
        VariationData[] vars = new VariationData[variations.size()];
        for (int i = 0; i < variations.size(); i++) {
            vars[i] = variations.get(i).doBuild();
        }
        VariationData[][] data = BlockRegistry.splitVariationArray(vars);
        T[] ret = (T[]) Array.newInstance(provider.getBlockClass(), data.length);
        for (int i = 0; i < ret.length; i++) {
            ret[i] = provider.createBlock(material, ret.length, i, data[i]);
            ret[i].setRegistryName(blockName);
            ret[i].setUnlocalizedName(domain + '.' + blockName);
            if (sound != null) {
                ret[i].stepSound = sound;
            }
            GameRegistry.registerBlock(ret[i], provider.getItemClass());
            ChiselModelRegistry.INSTANCE.register(ret[i]);
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j].group != null) {
                    CarvingUtils.getChiselRegistry().addVariation(data[i][j].group, ret[i].getStateFromMeta(j), i * 16 + j);
                }
            }
        }
        return ret;
    }

    @Accessors(chain = true)
    public static class VariationBuilder<T extends Block & ICarvable> {

        /**
         * For Internal chisel use only
         */
        public interface IVariationBuilderDelegate {

            VariationData build(String name, String group, int index, ChiselRecipe recipe, ItemStack smeltedFrom, int amountSmelted, ResourceLocation texLocation,
                    Map<EnumFacing, ResourceLocation> overrideMap);

        }
        
        private ChiselBlockBuilder<T> parent;

        private String name;
        private String group;
        
        private int index;

        @Setter
        private ChiselRecipe recipe;

        private ItemStack smeltedFrom;
        private int amountSmelted;

        @Setter
        private ResourceLocation textureLocation;
        private Map<EnumFacing, ResourceLocation> overrideMap;

        private VariationBuilder(ChiselBlockBuilder<T> parent, String name, String group, int index){
            this.parent = parent;
            this.name = name;
            this.group = group;
            this.index = index;
            this.overrideMap = new HashMap<EnumFacing, ResourceLocation>();
            this.textureLocation = new ResourceLocation(parent.domain, parent.blockName + "/" + name);
        }

        public VariationBuilder<T> setSmeltRecipe(ItemStack smeltedFrom, int amountSmelted){
            this.smeltedFrom = smeltedFrom;
            this.amountSmelted = amountSmelted;
            return this;
        }
        
        @Tolerate
        public VariationBuilder<T> setTextureLocation(String path) {
            return setTextureLocation(new ResourceLocation(parent.domain, path));
        }
        
        public VariationBuilder<T> setTextureLocation(EnumFacing facing, String path){
            return setTextureLocation(facing, new ResourceLocation(parent.domain, path));
        }

        public VariationBuilder<T> setTextureLocation(EnumFacing facing, ResourceLocation loc){
            this.overrideMap.put(facing, loc);
            return this;
        }

        public ChiselBlockBuilder<T> buildVariation(){
            this.parent.variations.add(this);
            return this.parent;
        }
        
        public VariationBuilder<T> next(String name, String group) {
            return buildVariation().newVariation(name, group);
        }
        
        public T[] build() {
            return buildVariation().build();
        }

        private VariationData doBuild() {
            return Chisel.proxy.getBuilderDelegate().build(name, group, index, recipe, smeltedFrom, amountSmelted, textureLocation, overrideMap);
        }

        //todo I was here gonna implement ClientVariation stuff
    }


}
