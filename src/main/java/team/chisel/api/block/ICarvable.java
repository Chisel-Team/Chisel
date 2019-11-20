package team.chisel.api.block;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;

@ParametersAreNonnullByDefault
public interface ICarvable {

    /**
     * @return The index of this block, each index holds 16 variations.
     */
    int getIndex();

    /**
     * @return The total amount of variations for all indeces of this block.
     */
    int getTotalVariations();

    /**
     * @param variation
     *            The index of the variation (0-15).
     * @return The {@link VariationData} for the variation index.
     */
    VariationData getVariationData(int variation);

    /**
     * @return All variations for this block instance.
     */
    VariationData[] getVariations();

    /**
     * Gets the variation index from the world state.
     * 
     * @param state
     *            The current {@link BlockState}.
     * @return The variation index.
     */
    int getVariationIndex(BlockState state);
    
    IProperty<Integer> getMetaProp();

//    /**
//     * Called to get the quad mutator, called multiple times so please cache this value
//     */
//    @SideOnly(Side.CLIENT)
//    IQuadMutator getQuadMutator();
}
