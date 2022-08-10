package team.chisel.api.block;

public interface ICarvable {

    /**
     * @param variation The index of the variation (0-15).
     * @return The {@link VariationData} for the variation index.
     */
    VariationData getVariation();
}
