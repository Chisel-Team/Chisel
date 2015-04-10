package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.TextureStitcher;
import com.cricketcraft.chisel.common.Reference;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import net.minecraft.block.BlockFence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

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


    public CTMBlockResources(BlockCarvable parent, String name, List<String> lore, TextureAtlasSprite texture, TextureAtlasSprite ctmTexture){
        super(parent, name, lore, texture);
        this.ctmTexture=ctmTexture;
        this.texture=texture;
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
        TextureAtlasSprite texture = map.getAtlasSprite(prefix+subBlockName);
        TextureAtlasSprite ctm = map.getAtlasSprite(prefix+subBlockName+"-ctm");
        return new CTMBlockResources(parent, subBlockName, lore, texture, ctm);
    }

    public static void preGenerateBlockResources(BlockCarvable parent, String subBlockName){
        String prefix = MOD_ID.toLowerCase()+":blocks/"+parent.getName()+"/";
        TextureStitcher.register(prefix+subBlockName);
        TextureStitcher.register(prefix+subBlockName+"-ctm");
    }

}
