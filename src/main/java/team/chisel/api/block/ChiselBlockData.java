package team.chisel.api.block;

import net.minecraft.block.material.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data about a chisel block, contains all the data from the json
 */
public class ChiselBlockData {

    /**
     * Name of this block
     */
    public String name;
    /**
     * A List of all the variations of this block
     */
    public VariationData[] variations;

    /**
     * The Mod or map that owns this block, if its a mod this should be the modid, for ones added by chisel this is just "chisel"
     */
    public String owner;

    /**
     * The Material for this block
     */
    public Material material;

    /**
     * The Blocks Hardness
     */
    public float hardness;

    /**
     * Whether the block is an opaque cube
     */
    public boolean isOpaqueCube;

    /**
     * Whether this block can be used to construct a beacon base
     */
    public boolean beaconBase;

    /**
     * Map of String names to the index of variation data in the variations array
     */
    private Map<String, Integer> nameVariationMap;



    protected ChiselBlockData(String name, VariationData[] variations, String owner, Material material, float hardness,
                              boolean opaqueCube, boolean beaconBase){
        this.name = name;
        this.variations = variations;
        this.owner = owner;
        this.material = material;
        this.hardness = hardness;
        this.isOpaqueCube = opaqueCube;
        this.beaconBase = beaconBase;
        this.nameVariationMap = new HashMap<String, Integer>();
        for (int i = 0 ; i < variations.length ; i++){
            if (variations[i] != null){
                nameVariationMap.put(variations[i].name, i);
                variations[i].index = i;
            }
        }
    }

    public VariationData getVariation(String name){
        if (nameVariationMap.containsKey(name)){
            return this.variations[nameVariationMap.get(name)];
        }
        else {
            return variations[0];
        }
    }





}