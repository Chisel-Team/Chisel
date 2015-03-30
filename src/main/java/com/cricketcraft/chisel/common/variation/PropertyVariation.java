package com.cricketcraft.chisel.common.variation;

import net.minecraft.block.properties.PropertyHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents the Property for a variation
 *
 * @author minecreatr
 */
public class PropertyVariation extends PropertyHelper{

    static Collection<Variation> values = new ArrayList<Variation>();

    public PropertyVariation(){
        super("variation", Variation.class);
    }

    public Collection getAllowedValues(){
        return values;
    }

    public String getName(Comparable en){
        return ((Variation)en).getValue();
    }

}
