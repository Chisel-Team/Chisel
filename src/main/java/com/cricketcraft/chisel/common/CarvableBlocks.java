package com.cricketcraft.chisel.common;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.BlockResources;
import com.cricketcraft.chisel.client.render.CTMBlockResources;
import com.cricketcraft.chisel.client.render.NonCTMModelRegistry;
import com.cricketcraft.chisel.client.render.ctm.CTMModelRegistry;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.ItemChiselBlock;
import com.cricketcraft.chisel.common.block.subblocks.CTMSubBlock;
import com.cricketcraft.chisel.common.block.subblocks.SubBlock;
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
        protected Variation[] createVariations(){
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
    private Variation[] variations;

    public static BlockCarvable getBlockWithName(String name){
        return blocks.get(name);
    }

    public BlockCarvable getBlock(){
        return getBlockWithName(name);
    }


    CarvableBlocks(String name){
        this.name=name;
        variations=createVariations();
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
        return variations;
        //throw new RuntimeException("Not getting overwritten");
    }

    protected Variation[] createVariations(){
        return new Variation[0];
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

            //BlockCarvable block = new BlockCarvable(b, b.getVariants().length);
            Variation[][] var = splitVariationArray(b.getVariants());
            for (int i=0;i<var.length;i++) {
                Variation[] vArray = var[i];
                BlockCarvable block = new BlockCarvable(b,vArray.length, i);
                for (Variation v : b.getVariants()) {
                    if (isCTM(b.getName(), v.getValue())) {
                        CTMModelRegistry.register(b.getName(), v.getValue(), var.length);
                        CTMBlockResources.preGenerateBlockResources(block, v.getValue());
                    }
                    else {
                        NonCTMModelRegistry.register(b.getName(), v.getValue(), var.length);
                        BlockResources.preGenerateBlockResources(block, v.getValue());
                    }
                }
                NonCTMModelRegistry.registerInventory(b, var.length);
                if (i==0) {
                    blocks.put(b.getName(), block);
                }
                else {
                    blocks.put(b.getName()+i, block);
                }
            }
        }
    }


    public static void initBlocks(){
        for (int i=0;i<blocks.size();i++){
            BlockCarvable block = (BlockCarvable)blocks.values().toArray()[i];
            for (int h=0;h<block.getType().getVariants().length;h++){
                Variation v = block.getType().getVariants()[h];
                if (block.getIndex()!=0){
                    int exclusion = block.getIndex()*16;
                    //Chisel.logger.info("i is "+i);
                    if (h<exclusion){
                        Chisel.logger.info("Exluding "+v.getName()+" from block "+blocks.keySet().toArray()[i]);
                        continue;
                    }
                }
                if (isCTM(block.getName(), v.getValue())){
                    block.addSubBlock(CTMSubBlock.generateSubBlock(block, v.getValue(), "Default CTM Sub Block"));
                }
                else {
                    block.addSubBlock(SubBlock.generateSubBlock(block, v.getValue(), "Default Non CTM Sub block"));
                }
            }
            GameRegistry.registerBlock(block, ItemChiselBlock.class, (String)blocks.keySet().toArray()[i]);
        }
    }

    private static Variation[][] splitVariationArray(Variation[] array){
        if (array.length<=16){
            return new Variation[][]{array};
        }
        int bound = ((int)Math.ceil(array.length/16)+1);
        Variation[][] vars = new Variation[bound][16];
        for (int i=0;i<array.length;i++){
            int cur = (int)i/16;
            if (cur>=bound){
                continue;
            }
            int leftover = (i%16);
            //Chisel.logger.info("cur: "+cur+" leftover: "+leftover);
            vars[cur][leftover] = array[i];
        }
        return vars;
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
