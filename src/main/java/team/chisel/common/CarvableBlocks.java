package team.chisel.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import team.chisel.Chisel;
import team.chisel.client.render.BlockResources;
import team.chisel.client.render.CTMBlockResources;
import team.chisel.client.render.NonCTMModelRegistry;
import team.chisel.client.render.ctm.CTMModelRegistry;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.ItemChiselBlock;
import team.chisel.common.block.subblocks.CTMSubBlock;
import team.chisel.common.block.subblocks.SubBlock;
import team.chisel.common.util.OreDictionaryUtil;
import team.chisel.common.variation.PropertyVariation;
import team.chisel.common.variation.Variation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
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
public enum CarvableBlocks implements Reference {

    ANTIBLOCK("antiblock") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return Variation.getColors(c);
        }

        @Override
        protected Object[] getCrafting(){
            return new Object[]{"SSS", "SGS", "SSS", 'S', Blocks.stone, 'G', Items.glowstone_dust};
        }

        @Override
        protected int getCraftingAmount(){
            return 8;
        }

    },

    ALUMINIUM("aluminium", true) {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"caution",
				"crate",
				"thermal",
				"adv",
				"egregious",
				"bolted");
        }
    },
    ANDESITE("andesite") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"pillar",
				"lbrick",
				"ornate",
				"prism",
				"tiles");
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"stone#5", "stone#6"};
        }
    },
    BOOKSHELF("bookshelf" ){
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"default", 
				"rainbow", 
				"necromancer-novice",
				"necromancer", 
				"redtomes", 
				"abandoned", 
				"hoarder", 
				"brim", 
				"historician");
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"bookshelf#0"};
        }
    },
    BRICK("brick") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"large", 
				"mortarless", 
				"varied", 
				"aged", 
				"yellow");
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"brick_block#0"};
        }
    },
    BRONZE("bronze", true) {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"caution", 
				"crate", 
				"thermal", 
				"adv", 
				"egregious", 
				"bolted");
        }
    },
    COBBLESTONE("cobblestone") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"aligned_brick", 
				"detailed_brick", 
				"small_brick",
                    
				"large_tile", 
				"small_tile", 
				"french_tile", 
				"french_tile2",
                    
				"creeper_tile", 
				"damaged_tile", 
				"huge_tile", 
				"creeper_panel",
                    
				"dent", 
				"panel", 
				"light_panel", 
				"dark_panel");
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"cobblestone#0"};
        }
    },
    COBBLESTONE_MOSSY("cobblestone_mossy"){
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"small_brick",
                    
				"large_tile", 
				"small_tile", 
				"french_tile", 
				"french_tile2",
                    
				"creeper_tile", 
				"damaged_tile", 
				"huge_tile", 
				"creeper_panel",
                    
				"dent", 
				"light_panel", 
				"dark_panel");
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"mossy_cobblestone#0"};
        }
    },
    CLOUD("cloud" ){
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"cloud", 
				"large", 
				"small", 
				"vertical", 
				"grid");
        }

        @Override
        protected Material getMaterial() {
            return Material.cloth;
        }

        @Override
        protected float getBlockHardness() {
            return 0.2f;
        }

        @Override
        public int getOpacity() {
            return 3;
        }

        @Override
        public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entity) {
            entity.fallDistance = 0.0F;

            if (entity.motionY < 0.0D) {
                entity.motionY *= 0.0050000000000000001D;
            } else if (entity.motionY > 0) {
                entity.motionY += 0.0285;
            }
        }
    },
    CONCRETE("concrete") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"default", 
				"block", 
				"doubleslab", 
				"blocks", 
				"weathered",
                    
				"weathered_block", 
				"weathered_doubleslab", 
				"weathered_blocks", 
				"weathered_half", 
				"weathered_block_half",
                    
				"asphalt");
        }

        @Override
        public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entity) {
            double velocity = Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);

            if (!(entity instanceof EntityPlayerSP))
                return;
            if (velocity == 0)
                return;

            EntityPlayerSP player = (EntityPlayerSP) entity;

            if (Math.abs(player.movementInput.moveForward) < 0.75f && Math.abs(player.movementInput.moveStrafe) < 0.75f)
                return;

            entity.motionX = entity.motionX / velocity;
            entity.motionZ = entity.motionZ / velocity;
        }

        @Override
        public ItemStack getSmeltedItem(){
            return new ItemStack(Blocks.gravel);
        }
    },
    COPPER("copper", true) {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"caution", 
				"crate", 
				"thermal", 
				"adv", 
				"egregious", 
				"bolted");
        }
    },
    DIAMOND_BLOCK("diamond_block", true) {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"embossed", 
				"gem", 
				"cells", 
				"space", 
				"spaceblack", 
				"simple",
                    
				"bismuth", 
				"crushed", 
				"four", 
				"fourornate", 
				"zelda", 
				"ornatelayer");
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"diamond_block#0"};
        }

        @Override
        public String getOredictName() {
            return "blockDiamond";
        }
    },
    DIORITE("diorite") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"pillar", 
				"lbrick", 
				"ornate", 
				"prism", 
				"tiles");
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"stone#3", "stone#4"};
        }
    },
    DIRT("dirt") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"bricks", 
				"netherbricks", 
				"bricks3", 
				"cobble", 
				"reinforcedCobble",
                    
				"reinforced", 
				"happy", 
				"bricks2", 
				"bricksdirt2", 
				"hor", 
				"vert", 
				"layers",
                    
				"vertical", 
				"chunky");
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"dirt#0", "dirt#1", "dirt#2"};
        }
    },
    EMERALD_BLOCK("emerald", true) {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"panel", 
				"panelclassic", 
				"smooth", 
				"chunk", 
				"goldborder",
                    
				"zelda", 
				"cell", 
				"cellbismuth", 
				"four", 
				"fourornate", 
				"ornate");
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"emerald_block#0"};
        }
    },
    END_STONE("end_stone") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"chiseled");
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"end_stone#0"};
        }
    },
    FACTORY("factory") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"dots", 
				"rust", 
				"rust2", 
				"platex", 
				"wireframewhite",
                    
				"wireframe", 
				"hazard", 
				"hazardorange", 
				"circuit", 
				"metalbox", 
				"goldplate",
                    
				"goldplating", 
				"grinder", 
				"plating", 
				"rustplates", 
				"column", 
				"iceiceice",
                    
				"vent", 
				"tilemosaic", 
				"wireframeblue");
        }
    },
    FANTASY("fantasy") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"brick", 
				"brick-faded", 
				"brick-wear", 
				"bricks", 
				"decor",
                    
				"decor-block", 
				"pillar", 
				"pillar-decorated", 
				"gold-decor-1", 
				"gold-decor-2",
                    
				"gold-decor-3", 
				"gold-decor-4", 
				"plate", 
				"block", 
				"bricks-chaotic", 
				"bricks-wear",

                    
				"purple-brick", 
				"purple-brick-faded", 
				"purple-brick-wear", 
				"purple-bricks", 
				"purple-decor",
                    
				"purple-decor-block", 
				"purple-pillar", 
				"purple-pillar-decorated", 
				"purple-plate",
                    
				"purple-block", 
				"purple-bricks-chaotic", 
				"purple-bricks-wear");
        }
    },
    GLOWSTONE("glowstone"){
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"sulphur-cobble", 
				"sulphur-corroded", 
				"sulphur-glass", 
				"sulphur-neon",
                    
				"sulphur-ornate", 
				"sulphur-rocky", 
				"sulphur-shale", 
				"sulphur-tile", 
				"sulphur-weavelanternlight",
                    
				"cobble", 
				"growth", 
				"layers", 
				"tilecorroded", 
				"bismuth", 
				"bismuth-panel");
        }
    },
    GOLD_BLOCK("gold_block") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return c.values(
				"largeingot");
        }

        @Override
        public String getOredictName() {
            return "blockGold";
        }

    },
    NULL("null"){
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[0];
        }
    };

    protected String name;
    private Variation[] variations;
    private boolean isBeaconBase;
    private IRecipe recipe;
    private List<BlockCarvable> instances;

    protected PropertyVariation propertyVariation;



    public BlockCarvable getBlock() {
        return getBlock(0);
    }

    public BlockCarvable getBlock(int index) {
        return instances.get(index);
    }



    /**
     * Gets the block from a string with the format {mod}:{block name}
     * So for vanilla cobblestone it would be vanilla:cobblestone
     *
     * @param in
     * @return
     */
    private static Block getBlockFromString(String in) {
        if (!in.contains(":")){
            return (Block) Block.blockRegistry.getObject(new ResourceLocation(in));
        }
        String[] parts = in.split(":");
        return GameRegistry.findBlock(parts[0], parts[1]);
    }

    private static ItemStack parseStack(String in){
        int meta = 0;
        String block;
        if (!in.contains("#")){
            block = in;
        }
        else {
            String[] parts = in.split("#");
            block = parts[0];
            meta = Integer.parseInt(parts[1]);
        }
        return new ItemStack(getBlockFromString(block), 1, meta);
    }


    CarvableBlocks(String name) {
        this(name, false);
    }

    CarvableBlocks(String name, boolean isBeaconBase) {
        this.name = name;
        propertyVariation = new PropertyVariation();
        variations = createVariations(Variation.creator(propertyVariation));
        this.isBeaconBase = isBeaconBase;
        this.instances = new ArrayList<BlockCarvable>();
    }

    /**
     * Get the name of this block
     *
     * @return The Name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get a array of the variants of the block
     * The order DOES matter
     *
     * @return The Variants
     */
    public Variation[] getVariants() {
        return variations;
        //throw new RuntimeException("Not getting overwritten";
    }

    protected abstract Variation[] createVariations(Variation.VariationCreator c);

    public boolean isOpaqueCube() {
        return true;
    }

    protected Material getMaterial() {
        return Material.rock;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    protected float getBlockHardness() {
        return 1f;
    }

    public int getOpacity() {
        return 1;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
    }

    protected Object[] getCrafting(){
        return new Object[0];
    }

    protected String[] createHonorarySubBlocks() {
        return new String[0];
    }

    protected int getCraftingAmount(){
        return 1;
    }

    public ItemStack getSmeltedItem(){
        return null;
    }

    public String getSound(){
        return MOD_ID+":chisel.fallback";
    }

    public static CarvableBlocks fromItemStack(ItemStack stack){
        if (Block.getBlockFromItem(stack.getItem())!=null){
            Block blockIn = Block.getBlockFromItem(stack.getItem());
            if (blockIn instanceof BlockCarvable){
                return ((BlockCarvable) blockIn).getType();
            }
        }
        return NULL;
    }

    protected String[] getLore(String v) {
        return new String[]{"chisel.lore." + getName() + "." + v + ".1"};
    }


    /**
     * Gets the Ore dictionary name of this block
     *
     * @return The Ore Dictionary name
     */
    public String getOredictName() {
        return getName();
    }

    /**
     * Gets a list of mods required for this block to load
     *
     * @return List of mods
     */
    public String[] getRequiredMods() {
        return new String[0];
    }

    public static void preInitBlocks() {
        for (CarvableBlocks b : values()) {
            if (b==NULL){
                continue;
            }
            if (!shouldBlockLoad(b)) {
                continue;
            }

            Variation[][] var = splitVariationArray(b.getVariants());
            for (int i = 0; i < var.length; i++) {
                Variation[] vArray = var[i];
                //Chisel.debug("index is "+i+" for "+b.getName());
                BlockCarvable block = new BlockCarvable(b.getMaterial(), b, vArray.length, i, b.propertyVariation, b.isBeaconBase);
//                if (i == 0 && b.getCrafting().length!=0){
//                    b.recipe = GameRegistry.addShapedRecipe(new ItemStack(block, b.getCraftingAmount(), 0), b.getCrafting());
//                }
                block.setHardness(b.getBlockHardness());
                block.setLightOpacity(b.getOpacity());
                if (block.VARIATION == null) {
                    throw new RuntimeException("Variation is null");
                }
                int count = 0;
                for (Variation v : b.getVariants()) {
                    if (isCTM(b.getName(), v.getValue())) {
                        CTMBlockResources.preGenerateBlockResources(block, v.getValue());
                        CTMModelRegistry.register(b.getName(), v.getValue(), var.length);
                    } else {
                        int type = BlockResources.preGenerateBlockResources(block, v.getValue());
                        NonCTMModelRegistry.register(b.getName(), v.getValue(), var.length, type);
                    }
                    count++;
                }
                b.instances.add(block);
                NonCTMModelRegistry.registerInventory(b, i);
            }
        }
        for (CarvableBlocks b : values()) {
            if (b==NULL){
                continue;
            }
            for (BlockCarvable block : b.instances) {
                final ModelResourceLocation location;
                location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block.getName(), "inventory");
                GameRegistry.registerBlock(block, ItemChiselBlock.class, block.getIndexName());
                for (Variation v : block.getType().getVariants()) {
                    Chisel.debug("Setting custom model resource location " + location + " for block " + block + " and meta " + Variation.metaFromVariation(block.getType(), v));
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), Variation.metaFromVariation(block.getType(), v), location);
                }
            }
        }

    }


    public static void initBlocks() {
        for (CarvableBlocks b : values()) {
            if (b==NULL){
                continue;
            }
            for (BlockCarvable block : b.instances) {
                if (block.getIndex() == 0 && b.getCrafting().length != 0) {
                    b.recipe = GameRegistry.addShapedRecipe(new ItemStack(block, b.getCraftingAmount(), 0), b.getCrafting());
                }
                if (block.getIndex() == 0 && b.getSmeltedItem() != null) {
                    GameRegistry.addSmelting(b.getSmeltedItem(), new ItemStack(block, b.getCraftingAmount(), 0), 0.35F);
                }
                if (b.createHonorarySubBlocks().length != 0) {
                    for (String s : b.createHonorarySubBlocks()) {
                        OreDictionaryUtil.addHonorary(b, parseStack(s));
                    }
                }
                for (int h = 0; h < block.getType().getVariants().length; h++) {
                    OreDictionaryUtil.add(block);
                    Variation v = block.getType().getVariants()[h];
                    if (block.getIndex() != 0) {
                        int exclusion = block.getIndex() * 16;
                        if (h < exclusion) {
                            Chisel.debug("Excluding " + v.getName() + " from block " + block);
                            continue;
                        }
                    }
                    if (isCTM(block.getName(), v.getValue())) {
                        block.addSubBlock(CTMSubBlock.generateSubBlock(block, v.getValue(), block.getType().getLore(v.getValue())));
                    } else {
                        block.addSubBlock(SubBlock.generateSubBlock(block, v.getValue(), block.getType().getLore(v.getValue())));
                    }
                }
            }
        }
    }


    private static Variation[][] splitVariationArray(Variation[] array) {
        if (array.length <= 16) {
            return new Variation[][]{array};
        }
        int bound = ((int) Math.ceil(array.length / 16) + 1);
        Variation[][] vars = new Variation[bound][16];
        for (int i = 0; i < array.length; i++) {
            int cur = (int) i / 16;
            if (cur >= bound) {
                continue;
            }
            int leftover = (i % 16);
            //Chisel.debug("cur: "+cur+" leftover: "+leftover);
            vars[cur][leftover] = array[i];
        }
        return vars;
    }

    private static boolean shouldBlockLoad(CarvableBlocks c) {
        for (String r : c.getRequiredMods()) {
            if (!Loader.isModLoaded(r)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isCTM(String blockName, String variation) {
        String ctmPath = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-ctm.png";
        String ctmHPath = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-ctmh.png";
        String ctmVPath = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-ctmv.png";
        if (Chisel.class.getResource(ctmPath) != null) {
            return true;
        }
        if (Chisel.class.getResource(ctmHPath) != null) {
            return true;
        }
        if (Chisel.class.getResource(ctmVPath) != null) {
            return true;
        }
        return false;
    }

    public static CarvableBlocks getBlock(String name) {
        for (CarvableBlocks b : values()) {
            if (b.getName().equals(name)) {
                return b;
            }
        }
        return null;
    }

    public static CarvableBlocks getBlock(BlockCarvable block) {
        return getBlock(block.getName());
    }

    public PropertyVariation getPropertyVariation() {
        return this.propertyVariation;
    }


}
