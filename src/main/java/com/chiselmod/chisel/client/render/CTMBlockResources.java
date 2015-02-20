package com.chiselmod.chisel.client.render;

import com.chiselmod.chisel.Chisel;
import com.chiselmod.chisel.client.TextureStitcher;
import com.chiselmod.chisel.common.Reference;
import com.chiselmod.chisel.common.block.BlockCarvable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the block resources for a connected textures block
 * Each of these is for 1 sub block
 *
 * @author minecreatr
 */
public class CTMBlockResources implements IBlockResources, Reference{

    private static List<CTMBlockResources> allResources = new ArrayList<CTMBlockResources>();


    /**
     * Block as not connected to anything else, aka default
     * ###
     * # #
     * ###
     */
    public TextureAtlasSprite noConnect;
    /**
     * One block connection
     * # #
     * # #
     * ###
     */
    public TextureAtlasSprite oneConnect;
    /**
     * Two block connections in a pillar/vertical line
     * # #
     * # #
     * # #
     */
    public TextureAtlasSprite twoConnectPillar;
    /**
     * Two block connections as a corner
     * #
     * #
     * ###
     */
    public TextureAtlasSprite twoConnectCorner;

    /**
     * Three Connections
     *
     *
     * ###
     */
    public TextureAtlasSprite threeConnect;
    /**
     * All Connect
     *
     *
     *
     */
    public TextureAtlasSprite allConnect;

    private BlockCarvable parent;
    private String variantName;
    private List<String> lore;


    public CTMBlockResources(BlockCarvable parent, String name, List<String> lore, TextureAtlasSprite noConnect, TextureAtlasSprite oneConnect,
                             TextureAtlasSprite twoConnectPillar, TextureAtlasSprite twoConnectCorner, TextureAtlasSprite threeConnect, TextureAtlasSprite allConnect){
        this.noConnect=noConnect;
        this.oneConnect=oneConnect;
        this.twoConnectPillar=twoConnectPillar;
        this.twoConnectCorner=twoConnectCorner;
        this.threeConnect=threeConnect;
        this.allConnect=allConnect;
        this.parent=parent;
        this.variantName=name;
        this.lore=lore;

    }

    public String getVariantName(){
        return this.variantName;
    }

    public BlockCarvable getParent(){
        return this.parent;
    }

    public List<String> getLore(){
        return this.lore;
    }

    public TextureAtlasSprite getDefaultTexture(){
        return this.noConnect;
    }

    /**
     * Generates a ctm block resources for the given information
     * @param parent
     * @param subBlockName
     * @return The generated block resource
     */
    public static CTMBlockResources generateBlockResources(BlockCarvable parent, String subBlockName, List<String> lore){
        String prefix = MOD_ID.toLowerCase()+":blocks/"+parent.getName()+"/"+subBlockName+"/";
        Chisel.logger.info(prefix);
        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
        if (map==null){
            throw new RuntimeException("TextureMap is null");
        }
        TextureAtlasSprite noConnect = map.getAtlasSprite(prefix + "noConnect");
        TextureAtlasSprite oneConnect = map.getAtlasSprite(prefix + "oneConnect");
        TextureAtlasSprite twoConnectPillar = map.getAtlasSprite(prefix + "twoConnectPillar");
        TextureAtlasSprite twoConnectCorner = map.getAtlasSprite(prefix + "twoConnectCorner");
        TextureAtlasSprite threeConnect = map.getAtlasSprite(prefix + "threeConnect");
        TextureAtlasSprite allConnect = map.getAtlasSprite(prefix + "allConnect");
        return new CTMBlockResources(parent, subBlockName, lore, noConnect, oneConnect, twoConnectPillar, twoConnectCorner,
                threeConnect, allConnect);
    }

    public static void preGenerateBlockResources(BlockCarvable parent, String subBlockName){
        String prefix = MOD_ID.toLowerCase()+":blocks/"+parent.getName()+"/"+subBlockName+"/";
        TextureStitcher.register(prefix+"noConnect");
        TextureStitcher.register(prefix+"oneConnect");
        TextureStitcher.register(prefix+"twoConnectPillar");
        TextureStitcher.register(prefix+"twoConnectCorner");
        TextureStitcher.register(prefix+"threeConnect");
        TextureStitcher.register(prefix+"allConnect");
    }

    public void refresh(TextureMap map){
        noConnect=map.getAtlasSprite(noConnect.getIconName());
        oneConnect=map.getAtlasSprite(oneConnect.getIconName());
        twoConnectPillar=map.getAtlasSprite(twoConnectPillar.getIconName());
        twoConnectCorner=map.getAtlasSprite(twoConnectCorner.getIconName());
        threeConnect=map.getAtlasSprite(threeConnect.getIconName());
        allConnect=map.getAtlasSprite(allConnect.getIconName());
    }

    public static void refreshAll(TextureMap map){
        for (CTMBlockResources resource : allResources){
            resource.refresh(map);
            Chisel.logger.info("Refreshing Resource "+resource.getVariantName()+"-"+resource.getParent().getName());
        }
    }
}
