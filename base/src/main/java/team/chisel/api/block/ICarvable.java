package team.chisel.api.block;

import javax.annotation.ParametersAreNonnullByDefault;

public interface ICarvable {

    /**
     * @param variation
     *            The index of the variation (0-15).
     * @return The {@link VariationData} for the variation index.
     */
    VariationData getVariation();
}
