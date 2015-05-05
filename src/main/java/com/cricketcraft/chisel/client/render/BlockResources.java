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



    public TextureAtlasSprite texture;

    /**
     * Side override texture
     */
    public TextureAtlasSprite side;

    /**
     * Top override texture
     */
    public TextureAtlasSprite top;

    /**
     * Bottom override texture
     */
    public TextureAtlasSprite bottom;

    protected List<String> lore;
    protected String variantName;
    protected BlockCarvable parent;

    protected BlockResources(){};

    public BlockResources(BlockCarvable parent, String name, List<String> lore, TextureAtlasSprite texture, TextureAtlasSprite side, TextureAtlasSprite top, TextureAtlasSprite bottom){
        this.texture=texture;
        this.lore=lore;
        this.variantName=name;
        this.parent=parent;
        this.side=side;
        this.top=top;
        this.bottom=bottom;
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

        TextureAtlasSprite side = null;
        TextureAtlasSprite top = null;
        TextureAtlasSprite bottom = null;
        if (hasSideOverride(parent.getName(), subBlockName)){
            side = map.getAtlasSprite(prefix+subBlockName+"-side");
        }
        if (hasTopOverride(parent.getName(), subBlockName)){
            top = map.getAtlasSprite(prefix+subBlockName+"-top");
        }
        if (hasBottomOverride(parent.getName(), subBlockName)){
            bottom = map.getAtlasSprite(prefix+subBlockName+"-bottom");
        }

        return new BlockResources(parent, subBlockName, lore, texture, side, top, bottom);
    }

    public static void preGenerateBlockResources(BlockCarvable parent, String subBlockName){
        String prefix = MOD_ID.toLowerCase()+":blocks/"+parent.getName()+"/";
        TextureStitcher.register(prefix+subBlockName);
        if (hasSideOverride(parent.getName(), subBlockName)){
            TextureStitcher.register(prefix+subBlockName+"-side");
        }
        if (hasTopOverride(parent.getName(), subBlockName)){
            TextureStitcher.register(prefix+subBlockName+"-top");
        }
        if (hasBottomOverride(parent.getName(), subBlockName)){
            TextureStitcher.register(prefix+subBlockName+"-bottom");
        }
    }

    protected static boolean hasSideOverride(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-side.png";
        return Chisel.class.getResource(path) !=null;
    }

    protected static boolean hasTopOverride(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-top.png";
        return Chisel.class.getResource(path) !=null;
    }

    protected static boolean hasBottomOverride(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-bottom.png";
        return Chisel.class.getResource(path) !=null;
    }

    protected static boolean isCTMH(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-ctmh.png";
        return Chisel.class.getResource(path) !=null;
    }

    protected static boolean isCTMV(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-ctmv.png";
        return Chisel.class.getResource(path) !=null;
    }


}
