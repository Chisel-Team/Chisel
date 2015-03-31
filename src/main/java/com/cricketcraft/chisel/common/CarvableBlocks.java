package com.cricketcraft.chisel.common;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.BlockResources;
import com.cricketcraft.chisel.client.render.CTMBlockResources;
import com.cricketcraft.chisel.client.render.ctm.CTMModelRegistry;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.ItemChiselBlock;
import com.cricketcraft.chisel.common.block.subblocks.CTMSubBlock;
import com.cricketcraft.chisel.common.variation.Variation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.init.Blocks;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents all the different carvable blocks
 *
 * @author minecreatr
 */
public enum CarvableBlocks implements Reference{

    ANTIBLOCK("antiblock"){
        @Override
        public Variation[] getVariants(){
            return Variation.getColors();
        }

        @Override
        public String getOrdictName(){
            return "antiblock";
        }
    }

    ;

    protected String name;
    private static Map<String, BlockCarvable> blocks = new HashMap<String, BlockCarvable>();

    public static BlockCarvable getBlockWithName(String name){
        return blocks.get(name);
    }

    public BlockCarvable getBlock(){
        return getBlockWithName(name);
    }


    CarvableBlocks(String name){
        this.name=name;
    }

    /**
     * Get the name of this block
     * @return The Name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get a array of the variants of the block
     * The order DOES matter
     * @return The Variants
     */
    public Variation[] getVariants(){
        //return new Variation[]{Variation.DEFAULT};
        throw new RuntimeException("Not getting overwritten");
    }

    /**
     * Gets the block it is based on. So if this is chiselable cobblestone it will return Blocks.cobblestone
     * @return The Block this block is based on
     */
    public Block getBaseBlock(){
        return null;
    }

    /**
     * Gets the Ore dictionary name of this block
     * @return The Ore Dictionary name
     */
    public String getOrdictName(){
        return getName();
    }

    /**
     * Gets a list of mods required for this block to load
     * @return List of mods
     */
    public String[] getRequiredMods(){
        return new String[0];
    }

    public static void preInitBlocks(){
        for (CarvableBlocks b : values()){
            if (!shouldBlockLoad(b)){
                continue;
            }
            BlockCarvable block = new BlockCarvable(b);
            for (Variation v : b.getVariants()){
                if (isCTM(b.getName(), v.getValue())){
                    CTMModelRegistry.register(b.getName(), v.getValue());
                    CTMBlockResources.preGenerateBlockResources(block, v.getValue());
                }
            }
            blocks.put(b.getName(), block);
        }
    }

    public static void initBlocks(){
        for (BlockCarvable block : blocks.values()){
            for (Variation v : block.getType().getVariants()){
                if (isCTM(block.getName(), v.getValue())){
                    block.addSubBlock(CTMSubBlock.generateSubBlock(block, v.getValue(), "Default Sub Block"));
                }
            }
            GameRegistry.registerBlock(block, ItemChiselBlock.class, block.getName());
        }
    }

    private static boolean shouldBlockLoad(CarvableBlocks c){
        for (String r : c.getRequiredMods()){
            if (!Loader.isModLoaded(r)){
                return false;
            }
        }
        return true;
    }

    private static boolean isCTM(String blockName, String variation){
        String path = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-ctm.png";
        return Chisel.class.getResource(path) !=null;
    }

    public static CarvableBlocks getBlock(String name){
        for (CarvableBlocks b : values()){
            if (b.getName().equals(name)){
                return b;
            }
        }
        return null;
    }

    public static CarvableBlocks getBlock(BlockCarvable block){
        return getBlock(block.getName());
    }

}
