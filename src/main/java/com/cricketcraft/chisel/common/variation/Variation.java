package com.cricketcraft.chisel.common.variation;

import com.cricketcraft.chisel.common.CarvableBlocks;
import net.minecraft.util.IStringSerializable;

/**
 * Represents a variation of a chisel block
 *
 * @author minecreatr
 */
public class Variation implements IStringSerializable, Comparable<Variation>{

    public static final Variation WHITE = createVariation("white");
    public static final Variation ORANGE = createVariation("orange");
    public static final Variation MAGENTA = createVariation("magenta");
    public static final Variation LIGHT_BLUE = createVariation("light_blue");
    public static final Variation YELLOW = createVariation("yellow");
    public static final Variation LIME = createVariation("lime");
    public static final Variation PINK = createVariation("pink");
    public static final Variation GRAY = createVariation("gray");
    public static final Variation LIGHT_GRAY = createVariation("light_gray");
    public static final Variation CYAN = createVariation("cyan");
    public static final Variation PURPLE = createVariation("purple");
    public static final Variation BLUE = createVariation("blue");
    public static final Variation BROWN = createVariation("brown");
    public static final Variation GREEN = createVariation("green");
    public static final Variation RED = createVariation("red");
    public static final Variation BLACK = createVariation("black");



    private String value;

    private Variation(String value){
        this.value=value;
        for (Variation v : PropertyVariation.values){
            if (v.equals(this)){
                return;
            }
        }
        PropertyVariation.values.add(this);
    }

    public static void doStuff(){};

    public String getValue(){
        return this.value;
    }


    public static Variation createVariation(String value){
        return new Variation(value);
    }


    /**
     * Gets the variations for a block that uses colors
     * @return
     */
    public static Variation[] getColors(){
        return new Variation[]{WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK};
    }


    /**
     * Gets the meta value for a variation
     * @param blocks The Block it is
     * @param v The Variation it is
     * @return The Variation
     */
    public static int metaFromVariation(CarvableBlocks blocks, Variation v){
        for (int i=0;i<blocks.getVariants().length;i++){
            Variation l = blocks.getVariants()[i];
            if (l.equals(v)) {
                return i;
            }
        }
        return 0;
    }

    public static Variation fromMeta(CarvableBlocks block, int meta){
        if (block==null){
            throw new RuntimeException(""+block);
        }
        return block.getVariants()[meta];
    }


    public boolean equals(Object object){
        if (object instanceof Variation){
            return this.getValue().equals(((Variation)object).getValue());
        }
        return false;
    }



    public String getName(){
        return this.getValue();
    }

    public int compareTo(Variation v){
        if (this.equals(v)){
            return 0;
        }
        return 1;
    }

    public String toString(){
        return getValue();
    }
}
