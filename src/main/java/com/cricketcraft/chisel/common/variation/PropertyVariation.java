package com.cricketcraft.chisel.common.variation;

import com.cricketcraft.chisel.Chisel;
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
        Chisel.logger.info("PropertyVariation created");
    }

    @Override
    public Collection getAllowedValues() {
        if (values.size() == 0) {
            throw new RuntimeException("0 Values?");
        }
        return values;
    }

    @Override
    public String getName(Comparable en) {
        return ((Variation) en).getValue();
    }

}
