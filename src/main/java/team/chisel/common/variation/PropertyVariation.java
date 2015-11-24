package team.chisel.common.variation;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.Chisel;
import net.minecraft.block.properties.PropertyHelper;
import team.chisel.common.block.BlockCarvable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the Property for a variation
 *
 * @author minecreatr
 */
public class PropertyVariation extends PropertyHelper {

    private static final Map<BlockCarvable, PropertyVariation> propertyVariationMap = new HashMap<BlockCarvable, PropertyVariation>();

    Collection<Variation> values = new ArrayList<Variation>();

    public PropertyVariation() {
        super("variation", Variation.class);
        Chisel.debug("PropertyVariation created");
    }

    @Override
    public Collection getAllowedValues() {
        return values;
    }

    public void setBlock(BlockCarvable block){
        propertyVariationMap.put(block, this);
    }

    @Override
    public String getName(Comparable en) {
        return ((Variation) en).getValue();
    }

    public static PropertyVariation getVariation(IBlockAccess world, BlockPos pos){
        return getVariation(world.getBlockState(pos).getBlock());
    }

    public static PropertyVariation getVariation(Block block){
        if (block instanceof BlockCarvable){
            return propertyVariationMap.get(block);
        }
        else {
            return null;
        }
    }

}
