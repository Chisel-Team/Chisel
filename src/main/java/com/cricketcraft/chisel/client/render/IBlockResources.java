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

    public static final int NORMAL = 0;
    public static final int CTM = 1;
    public static final int CTMH = 2;
    public static final int CTMV = 3;
    public static final int V9 = 4;
    public static final int V4 = 5;
    public static final int R16 = 6;
    public static final int R9 = 7;
    public static final int R4 = 8;

    public TextureAtlasSprite getDefaultTexture();

    public BlockCarvable getParent();

    public String getVariantName();

    public List<String> getLore();
    
    public int getType();


}
