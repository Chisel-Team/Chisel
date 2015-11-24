package team.chisel.api.render;

import net.minecraft.client.renderer.block.model.BakedQuad;
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
     * Whether this texture is actually a combined texture that has child textures
     */
    boolean isCombined();

    /**
     * Gets a list of all the block render types needed for this texture
     */
    List<IBlockRenderType> getBlockRenderTypes();
}
