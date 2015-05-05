package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.TextureStitcher;
import com.cricketcraft.chisel.common.Reference;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.util.ReflectionUtil;
import net.minecraft.block.BlockFence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the block resources for a connected textures block
 * Each of these is for 1 sub block
 *
 * @author minecreatr
 */
public class CTMBlockResources extends BlockResources{

    private static List<CTMBlockResources> allResources = new ArrayList<CTMBlockResources>();

    public TextureAtlasSprite ctmTexture;

    public boolean isCTMH = false;

    public boolean isCTMV = false;


    public CTMBlockResources(BlockCarvable parent, String name, List<String> lore, TextureAtlasSprite texture, TextureAtlasSprite ctmTexture,
                             TextureAtlasSprite side, TextureAtlasSprite top, TextureAtlasSprite bottom, boolean isCTMH, boolean isCTMV){
        super(parent, name, lore, texture, side, top, bottom);
        this.ctmTexture=ctmTexture;
        this.texture=texture;
        this.isCTMH=isCTMH;
        this.isCTMV=isCTMV;
        allResources.add(this);
    }


    /**
     * Generates a ctm block resources for the given information
     * @param parent
     * @param subBlockName
     * @return The generated block resource
     */
    public static CTMBlockResources generateBlockResources(BlockCarvable parent, String subBlockName, List<String> lore){
        String prefix = MOD_ID.toLowerCase()+":blocks/"+parent.getName()+"/";
        Chisel.logger.info(prefix);
        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
        if (map==null){
            throw new RuntimeException("TextureMap is null");
        }
        boolean isCTMH = isCTMH(parent.getName(), subBlockName);
        boolean isCTMV = isCTMV(parent.getName(), subBlockName);
        TextureAtlasSprite texture = map.getAtlasSprite(prefix+subBlockName);
        TextureAtlasSprite ctm;
        if (isCTMH){
            ctm = map.getAtlasSprite(prefix+subBlockName+"-ctmh");
        }
        else if (isCTMV){
            ctm = map.getAtlasSprite(prefix+subBlockName+"-ctmv");
        }
        else {
            ctm = map.getAtlasSprite(prefix + subBlockName + "-ctm");
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
        else if (top!=null){
            bottom = top;
        }

        return new CTMBlockResources(parent, subBlockName, lore, texture, ctm, side, top, bottom, isCTMH, isCTMV);
    }

    public static void preGenerateBlockResources(BlockCarvable parent, String subBlockName){
        String prefix = MOD_ID.toLowerCase()+":blocks/"+parent.getName()+"/";
        TextureStitcher.register(prefix+subBlockName);
        if (isCTMH(parent.getName(), subBlockName)){
            TextureStitcher.register(prefix+subBlockName+"-ctmh");
        }
        else if (isCTMV(parent.getName(), subBlockName)){
            TextureStitcher.register(prefix+subBlockName+"ctmv");
        }
        else {
            TextureStitcher.register(prefix + subBlockName + "-ctm");
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

    @Override
    public TextureAtlasSprite getDefaultTexture(){
        if (isCTMH||isCTMV){
//            TextureAtlasSprite temp = this.ctmTexture;
//            ReflectionUtil.setPrivateValue(TextureAtlasSprite.class, "maxU", temp, temp.getMaxU()-8);
//            ReflectionUtil.setPrivateValue(TextureAtlasSprite.class, "maxV", temp, temp.getMaxV()-8);
            return this.ctmTexture;
        }
        return this.texture;
    }

}
