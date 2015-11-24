package team.chisel.api.block;

import net.minecraft.item.ItemStack;

/**
 * Represents data about a specific variation
 */
public class VariationData {

    /**
     * The Name of this variation
     */
    public String name;

    /**
     * The Recipe for this variation, if null it cant be crafted
     */
    public String[] recipe;

    /**
     * The Itemstack that is smelted into this variation, if null it cant be smelted
     */
    public ItemStack smeltedFrom;

    /**
     * Gets the amount of this block produced through crafting/smelting
     */
    public int amountCrafted;

    /**
     * The Light value provided by this block
     */
    public float light;

    /**
     * The Block hardness
     */
    public float hardness;

    /**
     * Whether this block can be used to construct a beacon base
     */
    boolean beaconBase;

    public VariationData(String name, String[] recipe, ItemStack smeltedFrom, int amountCrafted, float light, float hardness, boolean beaconBase){
        this.name = name;
        this.recipe = recipe;
        this.smeltedFrom = smeltedFrom;
        this.amountCrafted = amountCrafted;
        this.light = light;
        this.hardness = hardness;
        this.beaconBase = beaconBase;
    }



}