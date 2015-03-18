package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.common.block.BlockCarvable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.List;

/**
 * Represents a class that holds certain resources for a block
 *
 * @author minecreatr
 */
public interface IBlockResources {

    public TextureAtlasSprite getDefaultTexture();

    public BlockCarvable getParent();

    public String getVariantName();

    public List<String> getLore();
}
