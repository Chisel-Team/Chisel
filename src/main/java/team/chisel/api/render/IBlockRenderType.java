package team.chisel.api.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Interface for chisel block render types
 * MUST HAVE A NO ARGS CONSTRUCTOR
 */
public interface IBlockRenderType {

    /**
     * Make a Chisel Texture from a list of sprites
     * @param sprites The Sprites
     * @return A Chisel Texture
     */
    IChiselTexture makeTexture(TextureAtlasSprite... sprites);

    /**
     * Gets the block render context for this block
     * @param world The world block access
     * @param pos The block position
     * @return The block render context
     */
    IBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos);
}
