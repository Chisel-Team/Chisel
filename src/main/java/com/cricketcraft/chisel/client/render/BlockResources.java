package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.TextureStitcher;
import com.cricketcraft.chisel.common.Reference;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the resources for a non Connected Texture block
 * aka Textures and lore
 * Each of these is for a sub block
 *
 * @author minecreatr
 */
public class BlockResources implements IBlockResources, Reference{


    private static List<BlockResources> allResources = new ArrayList<BlockResources>();

    public TextureAtlasSprite texture;
    protected List<String> lore;
    protected String variantName;
    protected BlockCarvable parent;

    protected BlockResources(){};

    public BlockResources(BlockCarvable parent, String name, List<String> lore, TextureAtlasSprite texture){
        this.texture=texture;
        this.lore=lore;
        this.variantName=name;
        this.parent=parent;
        if (!(this instanceof CTMBlockResources)){
            allResources.add(this);
        }
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

    public static BlockResources generateBlockResources(BlockCarvable parent, String subBlockName, List<String> lore){
        String prefix = MOD_ID.toLowerCase()+":blocks/"+parent.getName()+"/";
        Chisel.logger.info(prefix);
        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
        if (map==null){
            throw new RuntimeException("TextureMap is null");
        }
        TextureAtlasSprite texture = map.getAtlasSprite(prefix+subBlockName);
        return new BlockResources(parent, subBlockName, lore, texture);
    }

    public static void preGenerateBlockResources(BlockCarvable parent, String subBlockName){
        String prefix = MOD_ID.toLowerCase()+":blocks/"+parent.getName()+"/";
        TextureStitcher.register(prefix+subBlockName);
    }

    public void refresh(TextureMap map){
        texture = map.getAtlasSprite(texture.getIconName());
    }

    public static void refreshAll(TextureMap map){
        for (BlockResources resource : allResources){
            resource.refresh(map);
            Chisel.logger.info("Refreshing Resource "+resource.getVariantName()+"-"+resource.getParent().getName());
        }
    }
}
