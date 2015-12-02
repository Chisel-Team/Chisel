package team.chisel.api.render;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import java.util.List;

/**
 * Represents a Chisel Texture/resource
 */
public interface IChiselTexture<CTX extends IBlockRenderContext> {

    /**
     * Gets a list of quads for the side for this texture
     * @param side The Side
     * @param context The Context
     * @return A List of Quads
     */
    List<BakedQuad> getSideQuads(EnumFacing side, CTX context);

    /**
     * Gets the block render type of this texture
     * @return The Rendertype of this texture
     */
    IBlockRenderType<CTX> getBlockRenderType();

    /**
     * Gets the texture for a particle
     * @return The Texture for a particle
     */
    TextureAtlasSprite getParticle();

}
