package team.chisel.api.block;

import net.minecraft.block.material.Material;

import java.util.List;

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
    public List<VariationData> variations;

    /**
     * The Mod or map that owns this block, if its a mod this should be the modid, for ones added by chisel this is just "chisel"
     */
    public String owner;

    /**
     * The Handler, must implement team.chisel.api.block.IBlockHandler, if null or not a class with use the default handler
     */
    public String handler;

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
     * Whether this block should tick randomly
     */
    public boolean ticksRandomly;

    /**
     * The Tick rate of this block, default 10
     */
    public int tickRate;


    protected ChiselBlockData(String name, List<VariationData> variations, String owner, String handler, Material material, float hardness,
                              boolean opaqueCube, boolean beaconBase, boolean ticks, int tickRate){
        this.name = name;
        this.variations = variations;
        this.owner = owner;
        this.handler = handler;
        this.material = material;
        this.hardness = hardness;
        this.isOpaqueCube = opaqueCube;
        this.beaconBase = beaconBase;
        this.ticksRandomly = ticks;
        this.tickRate = tickRate;;
    }





}