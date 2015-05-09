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

    public int type;

    protected BlockResources(){};

    public BlockResources(BlockCarvable parent, String name, List<String> lore, TextureAtlasSprite texture,
                          TextureAtlasSprite side, TextureAtlasSprite top, TextureAtlasSprite bottom, int type){
        this.texture=texture;
        this.lore=lore;
        this.variantName=name;
        this.parent=parent;
        this.side=side;
        this.top=top;
        this.bottom=bottom;
        this.type=type;
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
        int type = NORMAL;
        TextureAtlasSprite texture;
        if (isV9(parent.getName(), subBlockName)){
            texture = map.getAtlasSprite(prefix+subBlockName+"-v9");
            type=V9;
        }
        else if (isV4(parent.getName(), subBlockName)){
            texture = map.getAtlasSprite(prefix+subBlockName+"-v4");
            type=V4;
        }
        else if (isR16(parent.getName(), subBlockName)){
            texture = map.getAtlasSprite(prefix+subBlockName+"-r16");
            type=R16;
        }
        else if (isR9(parent.getName(), subBlockName)){
            texture = map.getAtlasSprite(prefix+subBlockName+"-r9");
            type=R9;
        }
        else if (isR4(parent.getName(), subBlockName)){
            texture = map.getAtlasSprite(prefix+subBlockName+"-r4");
            type = R4;
        }
        else {
            texture = map.getAtlasSprite(prefix+subBlockName);
        }

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
        if (top!=null&&bottom==null){
            bottom=top;
        }

        return new BlockResources(parent, subBlockName, lore, texture, side, top, bottom, type);
    }

    public static void preGenerateBlockResources(BlockCarvable parent, String subBlockName){
        String prefix = MOD_ID.toLowerCase()+":blocks/"+parent.getName()+"/";
        if (isV9(parent.getName(), subBlockName)){
            TextureStitcher.register(prefix+subBlockName+"-v9");
        }
        else if (isV4(parent.getName(), subBlockName)){
            TextureStitcher.register(prefix+subBlockName+"-v4");
        }
        else if (isR16(parent.getName(), subBlockName)){
            TextureStitcher.register(prefix+subBlockName+"-r16");
        }
        else if (isR9(parent.getName(), subBlockName)){
            TextureStitcher.register(prefix+subBlockName+"-r9");
        }
        else if (isR4(parent.getName(), subBlockName)){
            TextureStitcher.register(prefix+subBlockName+"-r4");
        }
        else {
            TextureStitcher.register(prefix + subBlockName);
        }
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

    protected static boolean isV9(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-v9.png";
        return Chisel.class.getResource(path) !=null;
    }

    protected static boolean isV4(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-v4.png";
        return Chisel.class.getResource(path) !=null;
    }

    protected static boolean isR16(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-v9.png";
        return Chisel.class.getResource(path) !=null;
    }

    protected static boolean isR9(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-v9.png";
        return Chisel.class.getResource(path) !=null;
    }

    protected static boolean isR4(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-v9.png";
        return Chisel.class.getResource(path) !=null;
    }

    public static int getIntervals(int in){
        if (in==V9||in==R9){
            return 3;
        }
        else if (in==V4||in==R4){
            return 2;
        }
        else if (in==R16){
            return 4;
        }
        return 1;
    }


}
