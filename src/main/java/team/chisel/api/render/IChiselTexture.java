package team.chisel.api.render;

import java.util.Collection;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;

/**
 * Represents a Chisel Texture/resource
 */
public interface IChiselTexture<T extends IBlockRenderType> {

    /**
     * Transforms a quad to conform with this texture
     * 
     * @param quad
     *            The Quad
     * @param context
     *            The Context NULL CONTEXT MEANS INVENTORY
     * @param quadGoal
     *            Amount of quads that should be made
     * @return A List of Quads
     */
    List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal);

    Collection<ResourceLocation> getTextures();
    
    /**
     * Gets the block render type of this texture
     * 
     * @return The Rendertype of this texture
     */
    T getType();

    /**
     * Gets the texture for a particle
     * 
     * @return The Texture for a particle
     */
    TextureAtlasSprite getParticle();

    /**
     * The layer this texture requires. The layers will be prioritized for a face in the order:
     * <p>
     * {@link BlockRenderLayer#TRANSLUCENT}<br/>
     * {@link BlockRenderLayer#CUTOUT}<br/>
     * {@link BlockRenderLayer#SOLID}<br/>
     * 
     * @return The layer of this texture.
     */
    BlockRenderLayer getLayer();
}
