package team.chisel.api.render;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;

import java.util.List;

/**
 * Represents a Chisel Texture/resource
 */
public interface IChiselTexture {

    /**
     * Gets a list of quads for the side for this texture
     * 
     * @param side
     *            The Side
     * @param context
     *            The Context NULL CONTEXT MEANS INVENTORY
     * @param quadGoal
     *            Amount of quads that should be made
     * @return A List of Quads
     */
    List<BakedQuad> getSideQuads(EnumFacing side, IBlockRenderContext context, int quadGoal);

    /**
     * Gets the block render type of this texture
     * 
     * @return The Rendertype of this texture
     */
    IBlockRenderType getBlockRenderType();

    /**
     * Gets the texture for a particle
     * 
     * @return The Texture for a particle
     */
    TextureAtlasSprite getParticle();

    /**
     * The layer this texture requires. The layers will be prioritized for a face in the order:
     * <p>
     * {@link EnumWorldBlockLayer#TRANSLUCENT}<br/>
     * {@link EnumWorldBlockLayer#CUTOUT}<br/>
     * {@link EnumWorldBlockLayer#SOLID}<br/>
     * 
     * @return The layer of this texture.
     */
    EnumWorldBlockLayer getLayer();
}
