package com.cricketcraft.chisel.common;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.carving.CarvingVariationRepresentation;
import com.cricketcraft.chisel.client.render.BlockResources;
import com.cricketcraft.chisel.client.render.CTMBlockResources;
import com.cricketcraft.chisel.client.render.NonCTMModelRegistry;
import com.cricketcraft.chisel.client.render.ctm.CTMModelRegistry;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.ItemChiselBlock;
import com.cricketcraft.chisel.common.block.subblocks.CTMSubBlock;
import com.cricketcraft.chisel.common.block.subblocks.SubBlock;
import com.cricketcraft.chisel.common.variation.PropertyVariation;
import com.cricketcraft.chisel.common.variation.Variation;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import scala.actors.threadpool.Arrays;

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
        protected Variation[] createVariations(Variation.VariationCreator c){
            return Variation.getColors(c);
        }

        @Override
        public String getOrdictName(){
            return "antiblock";
        }
    },

    COBBLESTONE("cobblestone"){
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c){
            return new Variation[]{c.value("fancy")};
        }

        @Override
        public String[] createHonorarySubBlocks(){return new String[]{"cobblestone#0"};}
    },
    FACTORY("factory"){
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c){
            return new Variation[]{c.value("wireframe"), c.value("circuit")};
        }

    },
    ALUMINIUM("aluminium", true){
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c){
            return new Variation[]{c.value("caution"), c.value("crate"), c.value("thermal"), c.value("adv"), c.value("egregious"), c.value("bolted")};
        }
    },
    ANDESITE("andesite"){

        @Override
        protected Variation[] createVariations(Variation.VariationCreator c){
            return new Variation[]{c.value("pillar"), c.value("lbrick"), c.value("ornate"), c.value("prism"), c.value("tiles")};
        }

        @Override
        public String[] createHonorarySubBlocks(){
            return new String[]{"stone#5", "stone#6"};
        }
    },
    BOOKSHELF("bookshelf"){
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c){
            return new Variation[]{c.value("default"), c.value("rainbow"), c.value("necromancer-novice"),
                    c.value("necromancer"), c.value("redtomes"), c.value("abandoned"), c.value("hoarder"), c.value("brim"), c.value("historician")};
        }

        @Override
        public String[] createHonorarySubBlocks(){
            return new String[]{"bookshelf#0"};
        }
    }

    ;

    protected String name;
    private static Map<String, BlockCarvable> blocks = new HashMap<String, BlockCarvable>();
    private Variation[] variations;
    private boolean isBeaconBase;

    protected PropertyVariation propertyVariation;

    private CarvingVariationRepresentation[] carvingVariations;

    public static BlockCarvable getBlockWithName(String name){
        return blocks.get(name);
    }

    public BlockCarvable getBlock(){
        return getBlockWithName(name);
    }

    public CarvingVariationRepresentation[] getCarvingVariations(){
        return this.carvingVariations;
    }


    private static void initCarvingVariations(){
        for (CarvableBlocks b : values()){
            int l = b.getVariants().length+b.createHonorarySubBlocks().length;
            CarvingVariationRepresentation[] r = new CarvingVariationRepresentation[l];
            for (int i=0;i<b.getVariants().length;i++){
                Variation v = b.getVariants()[i];
                r[i]=new CarvingVariationRepresentation(b.getBlock(), i);
            }
            int index = b.getVariants().length;
            for (int i=0;i<b.createHonorarySubBlocks().length;i++){
                String data = b.createHonorarySubBlocks()[i];
                String[] p = data.split("#");
                r[index+i]=new CarvingVariationRepresentation(getBlockFromString(p[0]), Integer.parseInt(p[1]));
            }
            b.carvingVariations=r;
        }
    }

    /**
     * Gets the block from a string with the format {mod}:{block name}
     * So for vanilla cobblestone it would be vanilla:cobblestone
     * @param in
     * @return
     */
    private static Block getBlockFromString(String in){
        String[] parts = in.split(":");
        if (parts.length<2){
            return (Block)Block.blockRegistry.getObject(new ResourceLocation(parts[0]));
        }
        else {
            return GameRegistry.findBlock(parts[0], parts[1]);
        }
    }


    CarvableBlocks(String name){
        this(name, false);
    }

    CarvableBlocks(String name, boolean isBeaconBase){
        this.name=name;
        propertyVariation = new PropertyVariation();
        variations=createVariations(Variation.creator(propertyVariation));
        this.isBeaconBase=isBeaconBase;
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

    protected abstract Variation[] createVariations(Variation.VariationCreator c);

    protected String[] createHonorarySubBlocks(){return new String[0];}

    protected String[] getLore(String v){
        return new String[]{"chisel.lore."+getName()+"."+v+".1"};
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

            Variation[][] var = splitVariationArray(b.getVariants());
            for (int i=0;i<var.length;i++) {
                Variation[] vArray = var[i];
                BlockCarvable block = new BlockCarvable(b,vArray.length, i, b.propertyVariation, b.isBeaconBase);
                if (block.VARIATION==null){
                    throw new RuntimeException("Variation is null");
                }
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
        for (int i=0;i<blocks.size();i++){
            BlockCarvable block = (BlockCarvable)blocks.values().toArray()[i];
            final ModelResourceLocation location;
            if (block.getIndex()==0) {
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":"+block.getName(), "inventory");
            } else {
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":"+block.getName()+i, "inventory");
            }
            GameRegistry.registerBlock(block, ItemChiselBlock.class, (String)blocks.keySet().toArray()[i]);
            for (Variation v : block.getType().getVariants()){
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), Variation.metaFromVariation(block.getType(), v), location);
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
                    if (h<exclusion){
                        Chisel.logger.info("Excluding "+v.getName()+" from block "+blocks.keySet().toArray()[i]);
                        continue;
                    }
                }
                if (isCTM(block.getName(), v.getValue())){
                    block.addSubBlock(CTMSubBlock.generateSubBlock(block, v.getValue(), block.getType().getLore(v.getValue())));
                }
                else {
                    block.addSubBlock(SubBlock.generateSubBlock(block, v.getValue(), block.getType().getLore(v.getValue())));
                }
            }
        }
        initCarvingVariations();
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
        String ctmPath = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-ctm.png";
        String ctmHPath = "/assets/"+MOD_ID.toLowerCase()+"/textures/blocks/"+blockName+"/"+variation+"-ctmh.png";
        if (Chisel.class.getResource(ctmPath)!=null){
            return true;
        }
        if (Chisel.class.getResource(ctmHPath)!=null){
            return true;
        }
        return false;
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

    public PropertyVariation getPropertyVariation(){
        return this.propertyVariation;
    }


}
