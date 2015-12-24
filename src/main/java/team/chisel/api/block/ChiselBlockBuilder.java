package team.chisel.api.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team.chisel.Chisel;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

/**
 * Building a ChiselBlockData
 */
@Setter
@Accessors(chain = true)
public class ChiselBlockBuilder {

    private final String domain;

    private final String blockName;

    /**
     * The Material for this block
     */
    private Material material;

    /**
     * The Blocks Hardness
     */
    private float hardness;

    /**
     * Whether the block is an opaque cube
     */
    private boolean isOpaqueCube;

    /**
     * Whether this block can be used to construct a beacon base
     */
    private boolean beaconBase;

    private int curIndex;

    private List<VariationBuilder> variations;

    protected ChiselBlockBuilder(String domain, String blockName){
        this.domain = domain;
        this.blockName = blockName;
        this.material = Material.rock;
        this.hardness = 1f;
        this.isOpaqueCube = true;
        this.beaconBase = false;
        this.variations = new ArrayList<VariationBuilder>();
    }

    public VariationBuilder newVariation(String name, String group){
        VariationBuilder builder = new VariationBuilder(this, name, group, curIndex);
        curIndex++;
        return builder;
    }

    public ChiselBlockData build(){
        if (variations.size() == 0){
            throw new IllegalArgumentException("Must have at least one variation!");
        }
        VariationData[] vars = new VariationData[variations.size()];
        for (int i = 0 ; i < variations.size() ; i++){
            vars[i] = variations.get(i).doBuild();
        }
        return new ChiselBlockData(this.blockName, vars, domain, material, hardness, isOpaqueCube, beaconBase);
    }

    @Accessors(chain = true)
    public static class VariationBuilder {

        /**
         * For Internal chisel use only
         */
        public interface IVariationBuilderDelegate {

            VariationData build(String name, String group, int index, ChiselRecipe recipe, ItemStack smeltedFrom, int amountSmelted, int light, float hardness,
                                boolean beaconBase, ResourceLocation texLocation, Map<EnumFacing, ResourceLocation> overrideMap);

        }
        
        private ChiselBlockBuilder parent;

        private String name;
        private String group;
        
        private int index;

        @Setter
        private ChiselRecipe recipe;

        private ItemStack smeltedFrom;
        private int amountSmelted;

        @Setter
        private int light;
        @Setter
        private float hardness;
        @Setter
        private boolean beaconBase;

        @Setter
        private ResourceLocation textureLocation;
        private Map<EnumFacing, ResourceLocation> overrideMap;

        private VariationBuilder(ChiselBlockBuilder parent, String name, String group, int index){
            this.parent = parent;
            this.name = name;
            this.group = group;
            this.index = index;
            this.light = 0;
            this.hardness = parent.hardness;
            this.beaconBase = parent.beaconBase;
            this.overrideMap = new HashMap<EnumFacing, ResourceLocation>();
        }

        public VariationBuilder setSmeltRecipe(ItemStack smeltedFrom, int amountSmelted){
            this.smeltedFrom = smeltedFrom;
            this.amountSmelted = amountSmelted;
            return this;
        }

        public VariationBuilder setTextureLocation(EnumFacing facing, ResourceLocation loc){
            this.overrideMap.put(facing, loc);
            return this;
        }

        public ChiselBlockBuilder buildVariation(){
            this.parent.variations.add(this);
            return this.parent;
        }

        private VariationData doBuild() {
            return Chisel.proxy.getBuilderDelegate().build(name, group, index, recipe, smeltedFrom, amountSmelted, light, hardness, beaconBase, textureLocation, overrideMap);
        }

        //todo I was here gonna implement ClientVariation stuff
    }


}
