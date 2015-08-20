package team.chisel.common.variation;

import team.chisel.Chisel;
import net.minecraft.block.properties.PropertyHelper;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents the Property for a variation
 *
 * @author minecreatr
 */
public class PropertyVariation extends PropertyHelper {

    Collection<Variation> values = new ArrayList<Variation>();

    public PropertyVariation() {
        super("variation", Variation.class);
        Chisel.debug("PropertyVariation created");
    }

    @Override
    public Collection getAllowedValues() {
        return values;
    }

    @Override
    public String getName(Comparable en) {
        return ((Variation) en).getValue();
    }

}
