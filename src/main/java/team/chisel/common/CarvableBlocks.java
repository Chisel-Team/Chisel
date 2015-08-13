package team.chisel.common;

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

import java.util.HashMap;
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
            return new Variation[]{
				c.value("caution"),
				c.value("crate"),
				c.value("thermal"),
				c.value("adv"),
				c.value("egregious"),
				c.value("bolted")};
        }
    },
    ANDESITE("andesite") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("pillar"),
				c.value("lbrick"),
				c.value("ornate"),
				c.value("prism"),
				c.value("tiles")};
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"stone#5", "stone#6"};
        }
    },
    BOOKSHELF("bookshelf") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("default"), 
				c.value("rainbow"), 
				c.value("necromancer-novice"),
				c.value("necromancer"), 
				c.value("redtomes"), 
				c.value("abandoned"), 
				c.value("hoarder"), 
				c.value("brim"), 
				c.value("historician")};
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"bookshelf#0"};
        }
    },
    BRICK("brick") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("large"), 
				c.value("mortarless"), 
				c.value("varied"), 
				c.value("aged"), 
				c.value("yellow")};
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"brick_block#0"};
        }
    },
    BRONZE("bronze", true) {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("caution"), 
				c.value("crate"), 
				c.value("thermal"), 
				c.value("adv"), 
				c.value("egregious"), 
				c.value("bolted")};
        }
    },
    COBBLESTONE("cobblestone") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("aligned_brick"), 
				c.value("detailed_brick"), 
				c.value("small_brick"),
                    
				c.value("large_tile"), 
				c.value("small_tile"), 
				c.value("french_tile"), 
				c.value("french_tile2"),
                    
				c.value("creeper_tile"), 
				c.value("damaged_tile"), 
				c.value("huge_tile"), 
				c.value("creeper_panel"),
                    
				c.value("dent"), 
				c.value("panel"), 
				c.value("light_panel"), 
				c.value("dark_panel")};
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"cobblestone#0"};
        }
    },
    COBBLESTONE_MOSSY("cobblestone_mossy") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("small_brick"),
                    
				c.value("large_tile"), 
				c.value("small_tile"), 
				c.value("french_tile"), 
				c.value("french_tile2"),
                    
				c.value("creeper_tile"), 
				c.value("damaged_tile"), 
				c.value("huge_tile"), 
				c.value("creeper_panel"),
                    
				c.value("dent"), 
				c.value("light_panel"), 
				c.value("dark_panel")};
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"mossy_cobblestone#0"};
        }
    },
    CLOUD("cloud") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("cloud"), 
				c.value("large"), 
				c.value("small"), 
				c.value("vertical"), 
				c.value("grid")};
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
            return new Variation[]{
				c.value("default"), 
				c.value("block"), 
				c.value("doubleslab"), 
				c.value("blocks"), 
				c.value("weathered"),
                    
				c.value("weathered_block"), 
				c.value("weathered_doubleslab"), 
				c.value("weathered_blocks"), 
				c.value("weathered_half"), 
				c.value("weathered_block_half"),
                    
				c.value("asphalt")};
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
            return new Variation[]{
				c.value("caution"), 
				c.value("crate"), 
				c.value("thermal"), 
				c.value("adv"), 
				c.value("egregious"), 
				c.value("bolted")};
        }
    },
    DIAMOND_BLOCK("diamond_block", true) {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("embossed"), 
				c.value("gem"), 
				c.value("cells"), 
				c.value("space"), 
				c.value("spaceblack"), 
				c.value("simple"),
                    
				c.value("bismuth"), 
				c.value("crushed"), 
				c.value("four"), 
				c.value("fourornate"), 
				c.value("zelda"), 
				c.value("ornatelayer")};
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
            return new Variation[]{
				c.value("pillar"), 
				c.value("lbrick"), 
				c.value("ornate"), 
				c.value("prism"), 
				c.value("tiles")};
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"stone#3", "stone#4"};
        }
    },
    DIRT("dirt") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("bricks"), 
				c.value("netherbricks"), 
				c.value("bricks3"), 
				c.value("cobble"), 
				c.value("reinforcedCobble"),
                    
				c.value("reinforced"), 
				c.value("happy"), 
				c.value("bricks2"), 
				c.value("bricksdirt2"), 
				c.value("hor"), 
				c.value("vert"), 
				c.value("layers"),
                    
				c.value("vertical"), 
				c.value("chunky")};
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"dirt#0", "dirt#1", "dirt#2"};
        }
    },
    EMERALD_BLOCK("emerald", true) {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("panel"), 
				c.value("panelclassic"), 
				c.value("smooth"), 
				c.value("chunk"), 
				c.value("goldborder"),
                    
				c.value("zelda"), 
				c.value("cell"), 
				c.value("cellbismuth"), 
				c.value("four"), 
				c.value("fourornate"), 
				c.value("ornate")};
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"emerald_block#0"};
        }
    },
    END_STONE("end_stone") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("chiseled")};
        }

        @Override
        public String[] createHonorarySubBlocks() {
            return new String[]{"end_stone#0"};
        }
    },
    FACTORY("factory") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("dots"), 
				c.value("rust"), 
				c.value("rust2"), 
				c.value("platex"), 
				c.value("wireframewhite"),
                    
				c.value("wireframe"), 
				c.value("hazard"), 
				c.value("hazardorange"), 
				c.value("circuit"), 
				c.value("metalbox"), 
				c.value("goldplate"),
                    
				c.value("goldplating"), 
				c.value("grinder"), 
				c.value("plating"), 
				c.value("rustplates"), 
				c.value("column"), 
				c.value("iceiceice"),
                    
				c.value("vent"), 
				c.value("tilemosaic"), 
				c.value("wireframeblue")};
        }
    },
    FANTASY("fantasy") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("brick"), 
				c.value("brick-faded"), 
				c.value("brick-wear"), 
				c.value("bricks"), 
				c.value("decor"),
                    
				c.value("decor-block"), 
				c.value("pillar"), 
				c.value("pillar-decorated"), 
				c.value("gold-decor-1"), 
				c.value("gold-decor-2"),
                    
				c.value("gold-decor-3"), 
				c.value("gold-decor-4"), 
				c.value("plate"), 
				c.value("block"), 
				c.value("bricks-chaotic"), 
				c.value("bricks-wear"),

                    
				c.value("purple-brick"), 
				c.value("purple-brick-faded"), 
				c.value("purple-brick-wear"), 
				c.value("purple-bricks"), 
				c.value("purple-decor"),
                    
				c.value("purple-decor-block"), 
				c.value("purple-pillar"), 
				c.value("purple-pillar-decorated"), 
				c.value("purple-plate"),
                    
				c.value("purple-block"), 
				c.value("purple-bricks-chaotic"), 
				c.value("purple-bricks-wear")
            };
        }
    },
    GLOWSTONE("glowstone") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("sulphur-cobble"), 
				c.value("sulphur-corroded"), 
				c.value("sulphur-glass"), 
				c.value("sulphur-neon"),
                    
				c.value("sulphur-ornate"), 
				c.value("sulphur-rocky"), 
				c.value("sulphur-shale"), 
				c.value("sulphur-tile"), 
				c.value("sulphur-weavelanternlight"),
                    
				c.value("cobble"), 
				c.value("growth"), 
				c.value("layers"), 
				c.value("tilecorroded"), 
				c.value("bismuth"), 
				c.value("bismuth-panel")};
        }
    },
    GOLD_BLOCK("gold_block") {
        @Override
        protected Variation[] createVariations(Variation.VariationCreator c) {
            return new Variation[]{
				c.value("largeingot")};
        }

        @Override
        public String getOredictName() {
            return "blockGold";
        }

    };

    protected String name;
    private static Map<String, BlockCarvable> blocks = new HashMap<String, BlockCarvable>();
    private Variation[] variations;
    private boolean isBeaconBase;
    private IRecipe recipe;

    protected PropertyVariation propertyVariation;


    public static BlockCarvable getBlockWithName(String name) {
        return blocks.get(name);
    }

    public BlockCarvable getBlock() {
        return getBlockWithName(name);
    }

    public BlockCarvable getBlock(int index) {
        if (index == 0) {
            return getBlockWithName(name);
        } else {
            return getBlockWithName(name + index);
        }
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
        //throw new RuntimeException("Not getting overwritten");
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
                if (i == 0) {
                    blocks.put(b.getName(), block);
                } else {
                    blocks.put(b.getName() + i, block);
                }
                NonCTMModelRegistry.registerInventory(b, i);
            }
        }
        for (int i = 0; i < blocks.size(); i++) {
            BlockCarvable block = (BlockCarvable) blocks.values().toArray()[i];
            final ModelResourceLocation location;
            location = new ModelResourceLocation(MOD_ID.toLowerCase() + ":" + block.getName(), "inventory");
            GameRegistry.registerBlock(block, ItemChiselBlock.class, (String) blocks.keySet().toArray()[i]);
            for (Variation v : block.getType().getVariants()) {
                Chisel.debug("Setting custom model resource location " + location + " for block " + blocks.keySet().toArray()[i] + " and meta " + Variation.metaFromVariation(block.getType(), v));
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), Variation.metaFromVariation(block.getType(), v), location);
            }
        }

    }


    public static void initBlocks() {
        for (int i = 0; i < blocks.size(); i++) {
            BlockCarvable block = (BlockCarvable) blocks.values().toArray()[i];
            CarvableBlocks b = block.getType();
            if (block.getIndex() == 0 && b.getCrafting().length!=0){
                b.recipe = GameRegistry.addShapedRecipe(new ItemStack(block, b.getCraftingAmount(), 0), b.getCrafting());
            }
            if (block.getIndex() == 0 && b.getSmeltedItem()!=null){
                GameRegistry.addSmelting(b.getSmeltedItem(), new ItemStack(block, b.getCraftingAmount(), 0), 0.35F);
            }
            if (b.createHonorarySubBlocks().length!=0){
                for (String s : b.createHonorarySubBlocks()){
                    OreDictionaryUtil.addHonorary(b, parseStack(s));
                }
            }
            for (int h = 0; h < block.getType().getVariants().length; h++) {
                OreDictionaryUtil.add(block);
                Variation v = block.getType().getVariants()[h];
                if (block.getIndex() != 0) {
                    int exclusion = block.getIndex() * 16;
                    if (h < exclusion) {
                        Chisel.debug("Excluding " + v.getName() + " from block " + blocks.keySet().toArray()[i]);
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
