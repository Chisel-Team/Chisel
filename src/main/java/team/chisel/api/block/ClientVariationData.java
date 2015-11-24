package team.chisel.api.block;

import net.minecraft.item.ItemStack;
import team.chisel.api.render.RenderType;

/**
 * The Version of VariationData used on the client
 */
public class ClientVariationData extends VariationData {


    /**
     * The Type of the variation, like CTM or V4, see IBlockResources for values
     */
    public RenderType type;

    /**
     * The String for the location of the main block texture
     */
    public String main;

    /**
     * The String for the location of the ctm texture, if it exists
     */
    public String ctm;

    /**
     * The String for the location of the side ovveride texture if it exists
     */
    public String side;

    /**
     * The String for the location of the top ovveride texture if it exists
     */
    public String top;

    /**
     * The String for the location of the bottom ovveride texture if it exists
     */
    public String bottom;
    
    public ClientVariationData(String name, String[] recipe, ItemStack smeltedFrom, int amountCrafted, float light, float hardness, boolean beaconBase,
                               RenderType type, String main, String ctm, String side, String top, String bottom){
        super(name, recipe, smeltedFrom, amountCrafted, light, hardness, beaconBase);
        this.type = type;
        this.main = main;
        this.ctm = ctm;
        this.side = side;
        this.top = top;
        this.bottom = bottom;
        
    }

}