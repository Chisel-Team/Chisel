package com.chiselmod.chisel.client.render;

import com.chiselmod.chisel.common.block.BlockCarvable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.List;

/**
 * Represents the resources for a non Connected Texture block
 * aka Textures and lore
 * Each of these is for a sub block
 *
 * @author minecreatr
 */
public class BlockResources implements IBlockResources{

    public TextureAtlasSprite texture;
    private List<String> lore;
    private String variantName;
    private BlockCarvable parent;

    protected BlockResources(){};

    public BlockResources(String name, TextureAtlasSprite texture, List<String> lore, BlockCarvable parent){
        this.texture=texture;
        this.lore=lore;
        this.variantName=name;
        this.parent=parent;
    }

    public TextureAtlasSprite getDefaultTexture(){
        return this.texture;
    }

    public BlockCarvable getParent(){
        return this.parent;
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof BlockResources){
            BlockResources res = (BlockResources)object;
            return (res.getParent().equals(this.getParent()))&&(this.getVariantName().equals(res.getVariantName()));
        }
        return false;
    }

    public String getVariantName(){
        return this.variantName;
    }

    public List<String> getLore(){
        return this.lore;
    }
}
