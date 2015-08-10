package team.chisel.common.variation;

import team.chisel.common.CarvableBlocks;
import net.minecraft.util.IStringSerializable;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Represents a variation of a chisel block
 *
 * @author minecreatr
 */
public class Variation implements IStringSerializable, Comparable<Variation> {

    private String value;

    private PropertyVariation property;

    private Variation(PropertyVariation v, String value) {
        this.value = value;
        this.property = v;
        property.values.add(this);
    }

    public static void doStuff() {
    }

    ;

    public String getValue() {
        return this.value;
    }


    public static Variation createVariation(String value, PropertyVariation v) {
        return new Variation(v, value);
    }

    public static VariationCreator creator(PropertyVariation p) {
        return new VariationCreator(p);
    }

    public static class VariationCreator {

        private PropertyVariation p;

        private VariationCreator(PropertyVariation p) {
            this.p = p;
        }

        public Variation value(String v) {
            return createVariation(v, p);
        }
    }


    /**
     * Gets the variations for a block that uses colors
     *
     * @return
     */
    public static Variation[] getColors(VariationCreator c) {
        PropertyVariation p = c.p;
        return new Variation[]{createVariation("white", p), createVariation("orange", p), createVariation("magenta", p), createVariation("light_blue", p),
                createVariation("yellow", p), createVariation("lime", p), createVariation("pink", p), createVariation("gray", p),
                createVariation("light_gray", p), createVariation("cyan", p), createVariation("purple", p), createVariation("blue", p),
                createVariation("brown", p), createVariation("green", p), createVariation("red", p), createVariation("black", p)};
    }


    /**
     * Returns a variation array of main and colors
     *
     * @param main The Main variations
     * @return main + colors
     */
    public static Variation[] withColors(Variation[] main, VariationCreator c) {
        return ArrayUtils.addAll(getColors(c), main);
    }


    /**
     * Gets the meta value for a variation
     *
     * @param blocks The Block it is
     * @param v      The Variation it is
     * @return The Variation
     */
    public static int metaFromVariation(CarvableBlocks blocks, Variation v) {
        for (int i = 0; i < blocks.getVariants().length; i++) {
            Variation l = blocks.getVariants()[i];
            if (l.equals(v)) {
                if (i >= 16) {
                    return i % 16;
                }
                return i;
            }
        }
        return 0;
    }

    public static Variation fromMeta(CarvableBlocks block, int meta, int index) {
        if (block == null) {
            throw new RuntimeException("" + block);
        }
        try {
            return block.getVariants()[(index * 16) + meta];
        } catch (ArrayIndexOutOfBoundsException e) {
            return block.getVariants()[index * 16];
        }
    }


    public boolean equals(Object object) {
        if (object instanceof Variation) {
            return this.getValue().equals(((Variation) object).getValue());
        }
        return false;
    }


    @Override
    public String getName() {
        return this.getValue();
    }

    @Override
    public int compareTo(Variation v) {
        if (this.equals(v)) {
            return 0;
        }
        return 1;
    }

    public String toString() {
        //throw new RuntimeException("Giving name";)
        return getValue();
    }
}
