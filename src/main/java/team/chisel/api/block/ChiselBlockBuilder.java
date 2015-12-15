package team.chisel.api.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Building a ChiselBlockData
 */
public class ChiselBlockBuilder {

    private String domain;

    private String blockName;

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

    public ChiselBlockBuilder setMaterial(Material material){
        this.material = material;
        return this;
    }

    public ChiselBlockBuilder setHardness(float hardness){
        this.hardness = hardness;
        return this;
    }

    public ChiselBlockBuilder setOpaqueCube(boolean opaqueCube){
        this.isOpaqueCube = opaqueCube;
        return this;
    }

    public ChiselBlockBuilder setBeaconBase(boolean beaconBase){
        this.beaconBase = beaconBase;
        return this;
    }

    public VariationBuilder newVariation(String name){
        VariationBuilder builder = new VariationBuilder(this, name, curIndex);
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

    public static class VariationBuilder {

        /**
         * For Internal chisel use only
         */
        public interface IChiselBuilderInterface {

            VariationData build(String name, int index, ChiselRecipe recipe, ItemStack smeltedFrom, int amountSmelted, int light, float hardness,
                                boolean beaconBase, ResourceLocation texLocation, Map<EnumFacing, ResourceLocation> overrideMap);

        }

        private ChiselBlockBuilder parent;

        private String name;

        private int index;

        private ChiselRecipe recipe;

        private ItemStack smeltedFrom;

        private int amountSmelted;

        private int light;

        private float hardness;

        private boolean beaconBase;

        private ResourceLocation texLocation;

        private Map<EnumFacing, ResourceLocation> overrideMap;

        private static IChiselBuilderInterface builderInterface;




        private VariationBuilder(ChiselBlockBuilder parent, String name, int index){
            this.parent = parent;
            this.name = name;
            this.index = index;
            this.light = 0;
            this.hardness = parent.hardness;
            this.beaconBase = parent.beaconBase;
            this.overrideMap = new HashMap<EnumFacing, ResourceLocation>();
        }

        public VariationBuilder setRecipe(ChiselRecipe recipe){
            this.recipe = recipe;
            return this;
        }

        public VariationBuilder setSmeltRecipe(ItemStack smeltedFrom, int amountSmelted){
            this.smeltedFrom = smeltedFrom;
            this.amountSmelted = amountSmelted;
            return this;
        }

        public VariationBuilder setLight(int light){
            this.light = light;
            return this;
        }

        public VariationBuilder setHardness(float hardness){
            this.hardness = hardness;
            return this;
        }

        public VariationBuilder setBeaconBase(boolean beaconBase){
            this.beaconBase = beaconBase;
            return this;
        }

        public VariationBuilder setTextureLocation(ResourceLocation loc){
            this.texLocation = loc;
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

        private VariationData doBuild(){
            return builderInterface.build(name, index, recipe, smeltedFrom, amountSmelted, light, hardness,
                    beaconBase, texLocation, overrideMap);
        }


        public static void setInterface(IChiselBuilderInterface inter){
            if (builderInterface == null){
                builderInterface = inter;
            }
        }


        //todo I was here gonna implement ClientVariation stuff
    }


}
