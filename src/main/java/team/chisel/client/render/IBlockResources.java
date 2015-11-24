package team.chisel.client.render;

import team.chisel.api.render.RenderType;
import team.chisel.common.block.BlockCarvable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;


/**
 * Represents a class that holds certain resources for a block
 *
 * @author minecreatr
 */
public interface IBlockResources {

    public TextureAtlasSprite getDefaultTexture();

    public String getParentName();

    public String getVariantName();

    public RenderType getType();


}
