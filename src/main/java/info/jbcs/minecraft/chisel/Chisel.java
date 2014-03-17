package info.jbcs.minecraft.chisel;

import info.jbcs.minecraft.chisel.block.BlockCloud;
import info.jbcs.minecraft.chisel.block.BlockConcrete;
import info.jbcs.minecraft.chisel.block.BlockEldritch;
import info.jbcs.minecraft.chisel.block.BlockCarvableGlass;
import info.jbcs.minecraft.chisel.block.BlockHolystone;
import info.jbcs.minecraft.chisel.block.BlockLavastone;
import info.jbcs.minecraft.chisel.block.BlockLightstoneCarvable;
import info.jbcs.minecraft.chisel.block.BlockCarvable;
import info.jbcs.minecraft.chisel.block.BlockMarbleBookshelf;
import info.jbcs.minecraft.chisel.block.BlockMarbleCarpet;
import info.jbcs.minecraft.chisel.block.BlockMarbleIce;
import info.jbcs.minecraft.chisel.block.BlockMarbleIceStairs;
import info.jbcs.minecraft.chisel.block.BlockCarvablePane;
import info.jbcs.minecraft.chisel.block.BlockMarblePillar;
import info.jbcs.minecraft.chisel.block.BlockMarbleSlab;
import info.jbcs.minecraft.chisel.block.BlockMarbleStairs;
import info.jbcs.minecraft.chisel.block.BlockMarbleStairsMaker;
import info.jbcs.minecraft.chisel.block.BlockMarbleStairsMakerCreator;
import info.jbcs.minecraft.chisel.block.BlockMarbleWall;
import info.jbcs.minecraft.chisel.block.BlockPoweredMarble;
import info.jbcs.minecraft.chisel.block.BlockRoadLine;
import info.jbcs.minecraft.chisel.block.BlockSnakestone;
import info.jbcs.minecraft.chisel.block.BlockSnakestoneObsidian;
import info.jbcs.minecraft.chisel.block.BlockSpikes;
import info.jbcs.minecraft.chisel.entity.EntityBallOMoss;
import info.jbcs.minecraft.chisel.entity.EntityCloudInABottle;
import info.jbcs.minecraft.chisel.inventory.ContainerChisel;
import info.jbcs.minecraft.chisel.inventory.GuiChisel;
import info.jbcs.minecraft.chisel.inventory.InventoryChiselSelection;
import info.jbcs.minecraft.chisel.item.ItemBallOMoss;
import info.jbcs.minecraft.chisel.item.ItemCarvable;
import info.jbcs.minecraft.chisel.item.ItemChisel;
import info.jbcs.minecraft.chisel.item.ItemCloudInABottle;
import info.jbcs.minecraft.chisel.item.ItemMarbleSlab;
import info.jbcs.minecraft.utilities.General;

import java.io.File;
import java.util.ArrayList;

import pl.asie.lib.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;


@Mod(modid = "chisel", name = "Chisel", version = "1.5.1", dependencies = "required-after:asielib")
public class Chisel {
	public static ItemChisel				chisel;
//	public static ItemChisel				needle;
	public static Item						itemIceshard;
	public static ItemCloudInABottle		itemCloudInABottle;
	public static ItemBallOMoss				itemBallOMoss;

	public static BlockCarvable				blockMarble;
	public static BlockCarvable				blockMarblePillar;
	public static BlockCarvable				blockLimestone;
	public static BlockMarbleSlab			blockMarbleSlab;
	public static BlockMarbleSlab			blockLimestoneSlab;
	public static BlockMarbleSlab			blockMarblePillarSlab;
	public static BlockCarvable				blockCobblestone;
	public static BlockMarbleWall			cobblestoneWall;
	public static BlockCarvableGlass		blockGlass;
	public static BlockCarvablePane			blockPaneGlass;
	public static BlockCarvable				blockSandstone;
	public static BlockCarvable				blockSandstoneScribbles;
	public static BlockConcrete				blockConcrete;
	public static BlockRoadLine				blockRoadLine;	
	public static BlockCarvable				blockIron;
	public static BlockCarvable				blockGold;
	public static BlockCarvable				blockDiamond;
	public static BlockLightstoneCarvable	blockLightstone;
	public static BlockCarvable				blockLapis;
	public static BlockCarvable				blockEmerald;
	public static BlockCarvable				blockNetherBrick;
	public static BlockCarvable				blockNetherrack;
	public static BlockCarvable				blockCobblestoneMossy;
	public static BlockCarvable				stoneBrick;
	public static BlockMarbleStairs			blockMarbleStairs;
	public static BlockMarbleStairs			blockLimestoneStairs;
	public static BlockCarvablePane			blockPaneIron;
	public static BlockMarbleIce			blockIce;
	public static BlockMarbleIce			blockIcePillar;
	public static BlockMarbleIceStairs		blockIceStairs;
	public static BlockCarvable[]			blockPlanks=new BlockCarvable[6];
	public static BlockCarvable				blockObsidian;
	public static BlockPoweredMarble		blockRedstone;
	public static BlockHolystone			blockHolystone;
	public static BlockLavastone			blockLavastone;
	public static BlockCarvable				blockFft;
	public static BlockCarvable				blockCarpet;
	public static BlockMarbleCarpet			blockCarpetFloor;
	public static BlockCarvable				blockBookshelf;
	public static BlockCarvable				blockTyrian;
	public static BlockCarvable				blockDirt;
	public static BlockCloud				blockCloud;
	public static BlockCarvable				blockTemple;
	public static BlockCarvable				blockTempleMossy;
	public static BlockCarvable				blockFactory;
	
	public static BlockSnakestone			blockSnakestone;
	public static BlockSnakestone			blockSandSnakestone;
	public static BlockSnakestoneObsidian	blockObsidianSnakestone;
	
	public static BlockSpikes				blockSpiketrap;
	
	// 1.7
	public static BlockCarvableGlass[]		blockStainedGlass = new BlockCarvableGlass[16];
	public static BlockCarvablePane[]		blockStainedGlassPane = new BlockCarvablePane[16];
	
	public static CreativeTabs				tabChisel;
	
	public static boolean 					configExists;	
	public static boolean 					dropIceShards;
	public static boolean					oldPillars;
	public static boolean					disableCTM;	
	public static double 					concreteVelocity;
	public static int						particlesTickrate;
	public static boolean 					blockDescriptions;
	
    public static final StepSoundEx			soundHolystoneFootstep = new StepSoundEx("chisel:holystone", "chisel:holystone", "chisel:holystone",1.0f);
    public static final StepSoundEx			soundTempleFootstep = new StepSoundEx("dig.stone", "chisel:temple-footstep", "dig.stone",1.0f);
    public static final StepSoundEx			soundMetalFootstep = new StepSoundEx("chisel:metal", "chisel:metal", "chisel:metal",1.0f);
	
	static Configuration config;
	
	public static int RenderEldritchId;
	public static int RenderCTMId;
	public static int RenderCarpetId;

	@Instance("chisel")
	public static Chisel instance;

	@SidedProxy(clientSide = "info.jbcs.minecraft.chisel.ProxyClient", serverSide = "info.jbcs.minecraft.chisel.Proxy")
	public static Proxy proxy;

	public static PacketHandler packet;
	
    public void walk(File root,ArrayList<File> fl) {
		File[] list = root.listFiles();
		if (list == null) return;

		for (File f : list) {
			if (f.isDirectory()) {
				walk(f,fl);
			} else {
				fl.add(f);
			}
		}
    }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File configFile=event.getSuggestedConfigurationFile();
		configExists=configFile.exists();
		config = new Configuration(configFile);
		config.load();
		
		chisel = (ItemChisel) new ItemChisel(Carving.chisel).setTextureName("chisel:chisel").setUnlocalizedName("chisel").setCreativeTab(CreativeTabs.tabTools);
		LanguageRegistry.addName(chisel, "Chisel");
		GameRegistry.registerItem(chisel, "chisel.chisel");
		
//		needle = (ItemChisel) new ItemChisel(config.getItem("needle",7816).getInt(),Carving.needle).setTextureName("chisel:needle").setUnlocalizedName("needle").setCreativeTab(CreativeTabs.tabTools);
//		LanguageRegistry.addName(needle, "Needle");
		
		itemCloudInABottle=(ItemCloudInABottle) new ItemCloudInABottle().setUnlocalizedName("Chisel:cloudinabottle").setTextureName("Chisel:cloudinabottle").setCreativeTab(CreativeTabs.tabTools);
		LanguageRegistry.addName(itemCloudInABottle, "Cloud in a bottle");
		EntityRegistry.registerModEntity(EntityCloudInABottle.class, "CloudInABottle", 1, this, 40, 1, true);
		GameRegistry.registerItem(itemCloudInABottle, "chisel.cloudinabottle");
		itemBallOMoss=(ItemBallOMoss) new ItemBallOMoss().setUnlocalizedName("Chisel:ballomoss").setTextureName("Chisel:ballomoss").setCreativeTab(CreativeTabs.tabTools);
		LanguageRegistry.addName(itemBallOMoss, "Ball O' Moss");
		EntityRegistry.registerModEntity(EntityBallOMoss.class, "BallOMoss", 2, this, 40, 1, true);
		GameRegistry.registerItem(itemBallOMoss, "chisel.ballomoss");

		dropIceShards=config.get("general", "ice drops ice shards",true,"Reworks ice blocks to drop Ice shards when destroyed").getBoolean(true);
		concreteVelocity=config.get("general", "concrete velocity",0.45,"Traversing concrete roads, players will acceleration to this velocity. For reference, normal running speed is about 0.28. Set to 0 to disable acceleration.").getDouble(0.45);
		particlesTickrate=config.get("general", "particle tickrate",1,"Particle tick rate. Greater value = less particles.").getInt(1);
		oldPillars=config.get("general", "old pillar graphics",false,"Use old pillar textures").getBoolean(false);
		disableCTM=config.get("general", "disable connected textures",false,"Disable connected textures").getBoolean(false);
		blockDescriptions=config.get("general", "use block descriptions in tooltips",true,"Make variations of blocks have the same name, and use the description in tooltip to distinguish them.").getBoolean(true);
		
		if(dropIceShards){
			itemIceshard=new Item().setCreativeTab(CreativeTabs.tabMaterials).setUnlocalizedName("Chisel:iceshard").setTextureName("Chisel:iceshard");
//			itemIceshard=new Item(2582).setCreativeTab(CreativeTabs.tabMaterials).setUnlocalizedName("Chisel:iceshard").func_111206_d("Chisel:iceshard");
			LanguageRegistry.addName(itemIceshard, "Ice shard");
			GameRegistry.registerItem(itemIceshard, "chisel.iceshard");
			
			CraftingManager.getInstance().addRecipe(new ItemStack(Blocks.ice, 1), new Object[] { "**", "**", '*', itemIceshard, });
		}
		
		if(config.get("general", "use custom creative tab", true, "Add a new tab in creative mode and put all blocks that work with chisel there.").getBoolean(true)){
			tabChisel = new CreativeTabs("tabChisel") {
				@Override
				public Item getTabIconItem() {
					return chisel;
				}
			};
			
			LanguageRegistry.instance().addStringLocalization("itemGroup.tabChisel", "en_US", "Chisel blocks");
		} else{
			tabChisel = CreativeTabs.tabBlock;
		}

		blockMarble = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
		blockMarble.carverHelper.setBlockName("Marble");
		blockMarble.carverHelper.addVariation("Raw marble", 0, "marble");
		blockMarble.carverHelper.addVariation("Marble brick", 1, "marble/a1-stoneornamental-marblebrick");
		blockMarble.carverHelper.addVariation("Classic marble panel", 2, "marble/a1-stoneornamental-marbleclassicpanel");
		blockMarble.carverHelper.addVariation("Ornate marble panel", 3, "marble/a1-stoneornamental-marbleornate");
		blockMarble.carverHelper.addVariation("Marble panel", 4, "marble/panel");
		blockMarble.carverHelper.addVariation("Marble block", 5, "marble/block");
		blockMarble.carverHelper.addVariation("Dark creeper marble", 6, "marble/terrain-pistonback-marblecreeperdark");
		blockMarble.carverHelper.addVariation("Light creeper marble", 7, "marble/terrain-pistonback-marblecreeperlight");
		blockMarble.carverHelper.addVariation("Carved marble", 8, "marble/a1-stoneornamental-marblecarved");
		blockMarble.carverHelper.addVariation("Radial carved marble", 9, "marble/a1-stoneornamental-marblecarvedradial");
		blockMarble.carverHelper.addVariation("Marble with dent", 10, "marble/terrain-pistonback-marbledent");
		blockMarble.carverHelper.addVariation("Marble with large dent", 11, "marble/terrain-pistonback-marbledent-small");
		blockMarble.carverHelper.addVariation("Marble tiles", 12, "marble/marble-bricks");
		blockMarble.carverHelper.addVariation("Arranged marble tiles", 13, "marble/marble-arranged-bricks");
		blockMarble.carverHelper.addVariation("Fancy marble tiles", 14, "marble/marble-fancy-bricks");
		blockMarble.carverHelper.addVariation("Marble blocks", 15, "marble/marble-blocks");
		blockMarble.carverHelper.register(blockMarble, "marble");
		OreDictionary.registerOre("blockMarble", blockMarble);
		Carving.chisel.registerOre("marble","blockMarble");

		blockMarbleSlab = (BlockMarbleSlab) new BlockMarbleSlab(blockMarble).setHardness(2.0F).setResistance(10F);
		blockMarbleSlab.carverHelper.setBlockName("Marble Slab");
		blockMarbleSlab.carverHelper.addVariation("Raw marble slab", 0, "marble");
		blockMarbleSlab.carverHelper.addVariation("Marble brick slab", 1, "marbleslab/a1-stoneornamental-marblebrick");
		blockMarbleSlab.carverHelper.addVariation("Classic marble panel slab", 2, "marbleslab/a1-stoneornamental-marbleclassicpanel");
		blockMarbleSlab.carverHelper.addVariation("Ornate marble panel slab", 3, "marbleslab/a1-stoneornamental-marbleornate");
		blockMarbleSlab.carverHelper.addVariation("Marble panel slab", 4, "marbleslab/a1-stoneornamental-marblepanel");
		blockMarbleSlab.carverHelper.addVariation("Marble block slab", 5, "marbleslab/terrain-pistonback-marble");
		blockMarbleSlab.carverHelper.addVariation("Dark creeper marble slab", 6, "marbleslab/terrain-pistonback-marblecreeperdark");
		blockMarbleSlab.carverHelper.addVariation("Light creeper marble slab", 7, "marbleslab/terrain-pistonback-marblecreeperlight");
		blockMarbleSlab.carverHelper.addVariation("Carved marble slab", 8, "marbleslab/a1-stoneornamental-marblecarved");
		blockMarbleSlab.carverHelper.addVariation("Radial carved marble slab", 9, "marbleslab/a1-stoneornamental-marblecarvedradial");
		blockMarbleSlab.carverHelper.addVariation("Marble slab with dent", 10, "marbleslab/terrain-pistonback-marbledent");
		blockMarbleSlab.carverHelper.addVariation("Marble slab with large dent ", 11, "marbleslab/terrain-pistonback-marbledent-small");
		blockMarbleSlab.carverHelper.addVariation("Marble tiles slab", 12, "marbleslab/marble-bricks");
		blockMarbleSlab.carverHelper.addVariation("Arranged marble tiles slab", 13, "marbleslab/marble-arranged-bricks");
		blockMarbleSlab.carverHelper.addVariation("Fancy marble tiles slab", 14, "marbleslab/marble-fancy-bricks");
		blockMarbleSlab.carverHelper.addVariation("Marble blocks slab", 15, "marbleslab/marble-blocks");
		blockMarbleSlab.carverHelper.register(blockMarbleSlab, "marbleSlab", ItemMarbleSlab.class);

		if(oldPillars){
			blockMarblePillar = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
			blockMarblePillar.carverHelper.setBlockName("Marble Pillar");
			blockMarblePillar.carverHelper.addVariation("Marble pillar", 0, "marblepillarold/column");
			blockMarblePillar.carverHelper.addVariation("Marble pillar capstone", 1, "marblepillarold/capstone");
			blockMarblePillar.carverHelper.addVariation("Marble pillar base", 2, "marblepillarold/base");
			blockMarblePillar.carverHelper.addVariation("Small marble pillar", 3, "marblepillarold/small");
			blockMarblePillar.carverHelper.addVariation("Carved marble pillar", 4, "marblepillarold/pillar-carved");
			blockMarblePillar.carverHelper.addVariation("Ornamental marble pillar", 5, "marblepillarold/a1-stoneornamental-marblegreek");
			blockMarblePillar.carverHelper.addVariation("Greek marble pillar", 6, "marblepillarold/a1-stonepillar-greek");
			blockMarblePillar.carverHelper.addVariation("Plain marble pillar", 7, "marblepillarold/a1-stonepillar-plain");
			blockMarblePillar.carverHelper.addVariation("Greek marble pillar capstone", 8, "marblepillarold/a1-stonepillar-greektopplain");
			blockMarblePillar.carverHelper.addVariation("Plain marble pillar capstone", 9, "marblepillarold/a1-stonepillar-plaintopplain");
			blockMarblePillar.carverHelper.addVariation("Greek marble pillar base", 10, "marblepillarold/a1-stonepillar-greekbottomplain");
			blockMarblePillar.carverHelper.addVariation("Plain marble pillar base", 11, "marblepillarold/a1-stonepillar-plainbottomplain");
			blockMarblePillar.carverHelper.addVariation("Greek marble pillar ornate capstone", 12, "marblepillarold/a1-stonepillar-greektopgreek");
			blockMarblePillar.carverHelper.addVariation("Plain marble pillar ornate capstone", 13, "marblepillarold/a1-stonepillar-plaintopgreek");
			blockMarblePillar.carverHelper.addVariation("Greek marble pillar ornate base", 14, "marblepillarold/a1-stonepillar-greekbottomgreek");
			blockMarblePillar.carverHelper.addVariation("Plain marble pillar ornate base", 15, "marblepillarold/a1-stonepillar-plainbottomgreek");
		} else{
			blockMarblePillar = (BlockCarvable) new BlockMarblePillar(Material.rock).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
			blockMarblePillar.carverHelper.setBlockName("Marble Pillar");
			blockMarblePillar.carverHelper.addVariation("Marble pillar", 0, "marblepillar/pillar");
			blockMarblePillar.carverHelper.addVariation("Original marble pillar", 1, "marblepillar/default");
			blockMarblePillar.carverHelper.addVariation("Simplistic marble pillar", 2, "marblepillar/simple");
			blockMarblePillar.carverHelper.addVariation("Convex marble pillar", 3, "marblepillar/convex");
			blockMarblePillar.carverHelper.addVariation("Rough marble pillar", 4, "marblepillar/rough");
			blockMarblePillar.carverHelper.addVariation("Greek marble pillar with ornate capstone", 5, "marblepillar/greekdecor");
			blockMarblePillar.carverHelper.addVariation("Greek marble pillar", 6, "marblepillar/greekgreek");
			blockMarblePillar.carverHelper.addVariation("Greek marble pillar with plain capstone", 7, "marblepillar/greekplain");
			blockMarblePillar.carverHelper.addVariation("Plain marble pillar with ornate capstone", 8, "marblepillar/plaindecor");
			blockMarblePillar.carverHelper.addVariation("Plain marble pillar with Greek capstone", 9, "marblepillar/plaingreek");
			blockMarblePillar.carverHelper.addVariation("Plain marble pillar", 10, "marblepillar/plainplain");
			blockMarblePillar.carverHelper.addVariation("Wide marble pillar with ornate capstone", 11, "marblepillar/widedecor");
			blockMarblePillar.carverHelper.addVariation("Wide marble pillar with Greek capstone", 12, "marblepillar/widegreek");
			blockMarblePillar.carverHelper.addVariation("Wide marble pillar with plain capstone", 13, "marblepillar/wideplain");
			blockMarblePillar.carverHelper.addVariation("Carved pillar capstone", 14, "marblepillar/carved");
			blockMarblePillar.carverHelper.addVariation("Ornamental pillar capstone", 15, "marblepillar/ornamental");
		}
		blockMarblePillar.carverHelper.register(blockMarblePillar, "marblePillar");
		Carving.chisel.setGroupClass("marblePillar", "marble");
		
		blockMarblePillarSlab = (BlockMarbleSlab) new BlockMarbleSlab(blockMarblePillar).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
		blockMarblePillarSlab.carverHelper.setBlockName("Marble Pillar Slab");
		if(oldPillars){
			blockMarblePillarSlab.carverHelper.addVariation("Marble pillar slab", 0, "marblepillarslabold/column");
			blockMarblePillarSlab.carverHelper.addVariation("Marble pillar capstone slab", 1, "marblepillarslabold/capstone");
			blockMarblePillarSlab.carverHelper.addVariation("Marble pillar base slab", 2, "marblepillarslabold/base");
			blockMarblePillarSlab.carverHelper.addVariation("Small marble pillar slab", 3, "marblepillarslabold/small");
			blockMarblePillarSlab.carverHelper.addVariation("Carved marble pillar slab", 4, "marblepillarslabold/pillar-carved");
			blockMarblePillarSlab.carverHelper.addVariation("Ornamental marble pillar slab", 5, "marblepillarslabold/a1-stoneornamental-marblegreek");
			blockMarblePillarSlab.carverHelper.addVariation("Greek marble pillar slab", 6, "marblepillarslabold/a1-stonepillar-greek");
			blockMarblePillarSlab.carverHelper.addVariation("Plain marble pillar slab", 7, "marblepillarslabold/a1-stonepillar-plain");
			blockMarblePillarSlab.carverHelper.addVariation("Greek marble pillar capstone slab", 8, "marblepillarslabold/a1-stonepillar-greektopplain");
			blockMarblePillarSlab.carverHelper.addVariation("Plain marble pillar capstone slab", 9, "marblepillarslabold/a1-stonepillar-plaintopplain");
			blockMarblePillarSlab.carverHelper.addVariation("Greek marble pillar base slab", 10, "marblepillarslabold/a1-stonepillar-greekbottomplain");
			blockMarblePillarSlab.carverHelper.addVariation("Plain marble pillar base slab", 11, "marblepillarslabold/a1-stonepillar-plainbottomplain");
			blockMarblePillarSlab.carverHelper.addVariation("Greek marble pillar ornate capstone slab", 12, "marblepillarslabold/a1-stonepillar-greektopgreek");
			blockMarblePillarSlab.carverHelper.addVariation("Plain marble pillar ornate capstone slab", 13, "marblepillarslabold/a1-stonepillar-plaintopgreek");
			blockMarblePillarSlab.carverHelper.addVariation("Greek marble pillar ornate base slab", 14, "marblepillarslabold/a1-stonepillar-greekbottomgreek");
			blockMarblePillarSlab.carverHelper.addVariation("Plain marble pillar ornate base slab", 15, "marblepillarslabold/a1-stonepillar-plainbottomgreek");
		} else{
			blockMarblePillarSlab.carverHelper.addVariation("Marble pillar slab", 0, "marblepillarslab/pillar");
			blockMarblePillarSlab.carverHelper.addVariation("Original marble pillar slab", 1, "marblepillarslab/default");
			blockMarblePillarSlab.carverHelper.addVariation("Simplistic marble pillar slab", 2, "marblepillarslab/simple");
			blockMarblePillarSlab.carverHelper.addVariation("Convex marble pillar slab", 3, "marblepillarslab/convex");
			blockMarblePillarSlab.carverHelper.addVariation("Rough marble pillar slab", 4, "marblepillarslab/rough");
			blockMarblePillarSlab.carverHelper.addVariation("Greek marble pillar slab with ornate capstone", 5, "marblepillarslab/greekdecor");
			blockMarblePillarSlab.carverHelper.addVariation("Greek marble pillar slab", 6, "marblepillarslab/greekgreek");
			blockMarblePillarSlab.carverHelper.addVariation("Greek marble pillar slab with plain capstone", 7, "marblepillarslab/greekplain");
			blockMarblePillarSlab.carverHelper.addVariation("Plain marble pillar slab with ornate capstone", 8, "marblepillarslab/plaindecor");
			blockMarblePillarSlab.carverHelper.addVariation("Plain marble pillar slab with Greek capstone", 9, "marblepillarslab/plaingreek");
			blockMarblePillarSlab.carverHelper.addVariation("Plain marble pillar slab", 10, "marblepillarslab/plainplain");
			blockMarblePillarSlab.carverHelper.addVariation("Wide marble pillar slab with ornate capstone", 11, "marblepillarslab/widedecor");
			blockMarblePillarSlab.carverHelper.addVariation("Wide marble pillar slab with Greek capstone", 12, "marblepillarslab/widegreek");
			blockMarblePillarSlab.carverHelper.addVariation("Wide marble pillar slab with plain capstone", 13, "marblepillarslab/wideplain");
			blockMarblePillarSlab.carverHelper.addVariation("Carved pillar capstone slab", 14, "marblepillarslab/carved");
			blockMarblePillarSlab.carverHelper.addVariation("Ornamental pillar capstone slab", 15, "marblepillarslab/ornamental");
		}
		blockMarblePillarSlab.carverHelper.register(blockMarblePillarSlab, "marblePillarSlab", ItemMarbleSlab.class);

		BlockMarbleStairsMaker makerMarbleStairs=new BlockMarbleStairsMaker(blockMarble);
		makerMarbleStairs.carverHelper.setBlockName("Marble Stairs");
		makerMarbleStairs.carverHelper.addVariation("Raw marble stairs", 0, "marble");
		makerMarbleStairs.carverHelper.addVariation("Marble brick stairs", 1, "marbleslab/a1-stoneornamental-marblebrick");
		makerMarbleStairs.carverHelper.addVariation("Classic marble panel stairs", 2, "marbleslab/a1-stoneornamental-marbleclassicpanel");
		makerMarbleStairs.carverHelper.addVariation("Ornate marble panel stairs", 3, "marbleslab/a1-stoneornamental-marbleornate");
		makerMarbleStairs.carverHelper.addVariation("Marble panel stairs", 4, "marbleslab/a1-stoneornamental-marblepanel");
		makerMarbleStairs.carverHelper.addVariation("Marble block stairs", 5, "marbleslab/terrain-pistonback-marble");
		makerMarbleStairs.carverHelper.addVariation("Dark creeper marble stairs", 6, "marbleslab/terrain-pistonback-marblecreeperdark");
		makerMarbleStairs.carverHelper.addVariation("Light creeper marble stairs", 7, "marbleslab/terrain-pistonback-marblecreeperlight");
		makerMarbleStairs.carverHelper.addVariation("Carved marble stairs", 8, "marbleslab/a1-stoneornamental-marblecarved");
		makerMarbleStairs.carverHelper.addVariation("Radial carved marble stairs", 9, "marbleslab/a1-stoneornamental-marblecarvedradial");
		makerMarbleStairs.carverHelper.addVariation("Marble stairs with dent", 10, "marbleslab/terrain-pistonback-marbledent");
		makerMarbleStairs.carverHelper.addVariation("Marble stairs with large dent ", 11, "marbleslab/terrain-pistonback-marbledent-small");
		makerMarbleStairs.carverHelper.addVariation("Marble tiles stairs", 12, "marbleslab/marble-bricks");
		makerMarbleStairs.carverHelper.addVariation("Arranged marble tiles stairs", 13, "marbleslab/marble-arranged-bricks");
		makerMarbleStairs.carverHelper.addVariation("Fancy marble tiles stairs", 14, "marbleslab/marble-fancy-bricks");
		makerMarbleStairs.carverHelper.addVariation("Marble blocks stairs", 15, "marbleslab/marble-blocks");
		makerMarbleStairs.create();
		
		blockLimestone = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
		blockLimestone.carverHelper.setBlockName("Limestone");
		blockLimestone.carverHelper.addVariation("Limestone", 0, "limestone");
		blockLimestone.carverHelper.addVariation("Small limestone tiles", 1, "limestone/terrain-cobbsmalltilelight");
		blockLimestone.carverHelper.addVariation("French limestone tiles", 2, "limestone/terrain-cob-frenchlight");
		blockLimestone.carverHelper.addVariation("French limestone tiles", 3, "limestone/terrain-cob-french2light");
		blockLimestone.carverHelper.addVariation("Creeper limestone tiles", 4, "limestone/terrain-cobmoss-creepdungeonlight");
		blockLimestone.carverHelper.addVariation("Small limestone bricks", 5, "limestone/terrain-cob-smallbricklight");
		blockLimestone.carverHelper.addVariation("Damaged limestone tiles", 6, "limestone/terrain-mossysmalltilelight");
		blockLimestone.carverHelper.addVariation("Smooth limestone", 7, "limestone/terrain-pistonback-dungeon");
		blockLimestone.carverHelper.addVariation("Limestone with ornate panel", 8, "limestone/terrain-pistonback-dungeonornate");
		blockLimestone.carverHelper.addVariation("Limestone with ornate panel", 9, "limestone/terrain-pistonback-dungeonvent");
		blockLimestone.carverHelper.addVariation("Limestone with creeper panel", 10, "limestone/terrain-pistonback-lightcreeper");
		blockLimestone.carverHelper.addVariation("Limestone with dent", 11, "limestone/terrain-pistonback-lightdent");
		blockLimestone.carverHelper.addVariation("Limestone with panel", 12, "limestone/terrain-pistonback-lightemboss");
		blockLimestone.carverHelper.addVariation("Large limestone tiles", 13, "limestone/terrain-pistonback-lightfour");
		blockLimestone.carverHelper.addVariation("Limestone with light panel", 14, "limestone/terrain-pistonback-lightmarker");
		blockLimestone.carverHelper.addVariation("Limestone with dark panel", 15, "limestone/terrain-pistonback-lightpanel");
		blockLimestone.carverHelper.register(blockLimestone, "limestone");
		OreDictionary.registerOre("blockLimestone", blockLimestone);
		Carving.chisel.registerOre("limestone","blockLimestone");
		
		blockLimestoneSlab = (BlockMarbleSlab) new BlockMarbleSlab(blockLimestone).setHardness(2.0F).setResistance(10F);
		blockLimestoneSlab.carverHelper.setBlockName("Limestone Slab");
		blockLimestoneSlab.carverHelper.addVariation("Limestone slab", 0, "limestone");
		blockLimestoneSlab.carverHelper.addVariation("Small limestone tiles slab", 1, "limestone/terrain-cobbsmalltilelight");
		blockLimestoneSlab.carverHelper.addVariation("French limestone tiles slab", 2, "limestone/terrain-cob-frenchlight");
		blockLimestoneSlab.carverHelper.addVariation("French limestone tiles slab", 3, "limestone/terrain-cob-french2light");
		blockLimestoneSlab.carverHelper.addVariation("Creeper limestone tiles slab", 4, "limestone/terrain-cobmoss-creepdungeonlight");
		blockLimestoneSlab.carverHelper.addVariation("Small limestone bricks slab", 5, "limestone/terrain-cob-smallbricklight");
		blockLimestoneSlab.carverHelper.addVariation("Damaged limestone tiles slab", 6, "limestone/terrain-mossysmalltilelight");
		blockLimestoneSlab.carverHelper.addVariation("Smooth limestone slab", 7, "limestone/terrain-pistonback-dungeon");
		blockLimestoneSlab.carverHelper.addVariation("Limestone slab with ornate panel", 8, "limestone/terrain-pistonback-dungeonornate");
		blockLimestoneSlab.carverHelper.addVariation("Limestone slab with ornate panel", 9, "limestone/terrain-pistonback-dungeonvent");
		blockLimestoneSlab.carverHelper.addVariation("Limestone slab with creeper panel", 10, "limestone/terrain-pistonback-lightcreeper");
		blockLimestoneSlab.carverHelper.addVariation("Limestone slab with dent", 11, "limestone/terrain-pistonback-lightdent");
		blockLimestoneSlab.carverHelper.addVariation("Limestone slab with panel", 12, "limestone/terrain-pistonback-lightemboss");
		blockLimestoneSlab.carverHelper.addVariation("Large limestone tiles slab", 13, "limestone/terrain-pistonback-lightfour");
		blockLimestoneSlab.carverHelper.addVariation("Limestone slab with light panel", 14, "limestone/terrain-pistonback-lightmarker");
		blockLimestoneSlab.carverHelper.addVariation("Limestone slab with dark panel", 15, "limestone/terrain-pistonback-lightpanel");
		blockLimestoneSlab.carverHelper.register(blockLimestoneSlab, "limestoneSlab", ItemMarbleSlab.class);

		BlockMarbleStairsMaker makerLimestoneStairs=new BlockMarbleStairsMaker(blockLimestone);
		makerLimestoneStairs.carverHelper.setBlockName("Limestone Stairs");
		makerLimestoneStairs.carverHelper.addVariation("Limestone stairs", 0, "limestone");
		makerLimestoneStairs.carverHelper.addVariation("Small limestone tiles stairs", 1, "limestone/terrain-cobbsmalltilelight");
		makerLimestoneStairs.carverHelper.addVariation("French limestone tiles stairs", 2, "limestone/terrain-cob-frenchlight");
		makerLimestoneStairs.carverHelper.addVariation("French limestone tiles stairs", 3, "limestone/terrain-cob-french2light");
		makerLimestoneStairs.carverHelper.addVariation("Creeper limestone tiles stairs", 4, "limestone/terrain-cobmoss-creepdungeonlight");
		makerLimestoneStairs.carverHelper.addVariation("Small limestone bricks stairs", 5, "limestone/terrain-cob-smallbricklight");
		makerLimestoneStairs.carverHelper.addVariation("Damaged limestone tiles stairs", 6, "limestone/terrain-mossysmalltilelight");
		makerLimestoneStairs.carverHelper.addVariation("Smooth limestone stairs", 7, "limestone/terrain-pistonback-dungeon");
		makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with ornate panel", 8, "limestone/terrain-pistonback-dungeonornate");
		makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with ornate panel", 9, "limestone/terrain-pistonback-dungeonvent");
		makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with creeper panel", 10, "limestone/terrain-pistonback-lightcreeper");
		makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with dent", 11, "limestone/terrain-pistonback-lightdent");
		makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with panel", 12, "limestone/terrain-pistonback-lightemboss");
		makerLimestoneStairs.carverHelper.addVariation("Large limestone tiles stairs", 13, "limestone/terrain-pistonback-lightfour");
		makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with light panel", 14, "limestone/terrain-pistonback-lightmarker");
		makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with dark panel", 15, "limestone/terrain-pistonback-lightpanel");
		makerLimestoneStairs.create();
		
		blockCobblestone = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
		blockCobblestone.carverHelper.addVariation("Cobblestone", 0, Blocks.cobblestone);
		blockCobblestone.carverHelper.addVariation("Aligned cobblestone bricks", 1, "cobblestone/terrain-cobb-brickaligned");
		blockCobblestone.carverHelper.addVariation("Detailed cobblestone bricks", 2, "cobblestone/terrain-cob-detailedbrick");
		blockCobblestone.carverHelper.addVariation("Small cobblestone bricks", 3, "cobblestone/terrain-cob-smallbrick");
		blockCobblestone.carverHelper.addVariation("Large cobblestone tiles", 4, "cobblestone/terrain-cobblargetiledark");
		blockCobblestone.carverHelper.addVariation("Small cobblestone tiles", 5, "cobblestone/terrain-cobbsmalltile");
		blockCobblestone.carverHelper.addVariation("French cobblestone tiles", 6, "cobblestone/terrain-cob-french");
		blockCobblestone.carverHelper.addVariation("French cobblestone tiles", 7, "cobblestone/terrain-cob-french2");
		blockCobblestone.carverHelper.addVariation("Creeper cobblestone tiles", 8, "cobblestone/terrain-cobmoss-creepdungeon");
		blockCobblestone.carverHelper.addVariation("Damaged cobblestone tiles", 9, "cobblestone/terrain-mossysmalltiledark");
		blockCobblestone.carverHelper.addVariation("Huge cobblestone tiles", 10, "cobblestone/terrain-pistonback-dungeontile");
		blockCobblestone.carverHelper.addVariation("Cobblestone with creeper panel", 11, "cobblestone/terrain-pistonback-darkcreeper");
		blockCobblestone.carverHelper.addVariation("Cobblestone with dent", 12, "cobblestone/terrain-pistonback-darkdent");
		blockCobblestone.carverHelper.addVariation("Cobblestone with panel", 13, "cobblestone/terrain-pistonback-darkemboss");
		blockCobblestone.carverHelper.addVariation("Cobblestone with light panel", 14, "cobblestone/terrain-pistonback-darkmarker");
		blockCobblestone.carverHelper.addVariation("Cobblestone with dark panel", 15, "cobblestone/terrain-pistonback-darkpanel");
		blockCobblestone.carverHelper.register(blockCobblestone, "cobblestone");
		Carving.chisel.registerOre("cobblestone","blockCobble");

		blockGlass = (BlockCarvableGlass) new BlockCarvableGlass().setHardness(0.3F).setStepSound(Block.soundTypeGlass);
		blockGlass.carverHelper.addVariation("Glass", 0, Blocks.glass);
		blockGlass.carverHelper.addVariation("Bubble glass", 1, "glass/terrain-glassbubble");
		blockGlass.carverHelper.addVariation("Chinese glass", 2, "glass/terrain-glass-chinese");
		blockGlass.carverHelper.addVariation("Japanese(?) glass", 3, "glass/japanese");
		blockGlass.carverHelper.addVariation("Dungeon glass", 4, "glass/terrain-glassdungeon");
		blockGlass.carverHelper.addVariation("Light glass", 5, "glass/terrain-glasslight");
		blockGlass.carverHelper.addVariation("Borderless glass", 6, "glass/terrain-glassnoborder");
		blockGlass.carverHelper.addVariation("Ornate steel glass", 7, "glass/terrain-glass-ornatesteel");
		blockGlass.carverHelper.addVariation("Screen", 8, "glass/terrain-glass-screen");
		blockGlass.carverHelper.addVariation("Shale glass", 9, "glass/terrain-glassshale");
		blockGlass.carverHelper.addVariation("Steel frame glass", 10, "glass/terrain-glass-steelframe");
		blockGlass.carverHelper.addVariation("Stone frame glass", 11, "glass/terrain-glassstone");
		blockGlass.carverHelper.addVariation("Streak glass", 12, "glass/terrain-glassstreak");
		blockGlass.carverHelper.addVariation("Thick grid glass", 13, "glass/terrain-glass-thickgrid");
		blockGlass.carverHelper.addVariation("Thin grid glass", 14, "glass/terrain-glass-thingrid");
		blockGlass.carverHelper.addVariation("Modern Iron Fence", 15, "glass/a1-glasswindow-ironfencemodern");
		blockGlass.carverHelper.register(blockGlass, "glass");
		Carving.chisel.registerOre("glass","blockGlass");
		
		blockSandstone = (BlockCarvable) new BlockCarvable().setStepSound(Block.soundTypeStone).setHardness(0.8F);
		blockSandstone.carverHelper.addVariation("Sandstone", 0, Blocks.sandstone, 0);
		blockSandstone.carverHelper.addVariation("Chiseled Sandstone", 1, Blocks.sandstone, 1);
		blockSandstone.carverHelper.addVariation("Smooth Sandstone", 2, Blocks.sandstone, 2);
		blockSandstone.carverHelper.addVariation("Faded sandstone", 3, "sandstone/faded");
		blockSandstone.carverHelper.addVariation("Sandstone pillar", 4, "sandstone/column");
		blockSandstone.carverHelper.addVariation("Sandstone pillar capstone", 5, "sandstone/capstone");
		blockSandstone.carverHelper.addVariation("Small sandstone pillar ", 6, "sandstone/small");
		blockSandstone.carverHelper.addVariation("Sandstone pillar base", 7, "sandstone/base");
		blockSandstone.carverHelper.addVariation("Smooth & flat sandstone", 8, "sandstone/smooth");
		blockSandstone.carverHelper.addVariation("Smooth sandstone pillar capstone", 9, "sandstone/smooth-cap");
		blockSandstone.carverHelper.addVariation("Small smooth sandstone pillar", 10, "sandstone/smooth-small");
		blockSandstone.carverHelper.addVariation("Smooth sandstone pillar base", 11, "sandstone/smooth-base");
		blockSandstone.carverHelper.addVariation("Sandstone block", 12, "sandstone/block");
		blockSandstone.carverHelper.addVariation("Small sandstone blocks", 13, "sandstone/blocks");
		blockSandstone.carverHelper.addVariation("Sandstone mosaic", 14, "sandstone/mosaic");
		blockSandstone.carverHelper.addVariation("Stacked sandstone tiles", 15, "sandstone/horizontal-tiles");
		blockSandstone.carverHelper.register(blockSandstone, "sandstone");
		Carving.chisel.registerOre("sandstone","blockSandstone");

		blockSandSnakestone=(BlockSnakestone) new BlockSnakestone("Chisel:snakestone/sandsnake/").setBlockName("sandSnakestone");
		GameRegistry.registerBlock(blockSandSnakestone, ItemCarvable.class, blockSandSnakestone.getUnlocalizedName());
		LanguageRegistry.addName(new ItemStack(blockSandSnakestone, 1, 1), "Sandstone snake block head");
		LanguageRegistry.addName(new ItemStack(blockSandSnakestone, 1, 13), "Sandstone snake block body");		
		Carving.chisel.addVariation("sandstone", blockSandSnakestone, 1, 16);
		Carving.chisel.addVariation("sandstone", blockSandSnakestone, 13, 17);
		
		blockSandstoneScribbles = (BlockCarvable) new BlockCarvable().setStepSound(Block.soundTypeStone).setHardness(0.8F);
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 0, "sandstone-scribbles/scribbles-0");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 1, "sandstone-scribbles/scribbles-1");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 2, "sandstone-scribbles/scribbles-2");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 3, "sandstone-scribbles/scribbles-3");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 4, "sandstone-scribbles/scribbles-4");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 5, "sandstone-scribbles/scribbles-5");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 6, "sandstone-scribbles/scribbles-6");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 7, "sandstone-scribbles/scribbles-7");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 8, "sandstone-scribbles/scribbles-8");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 9, "sandstone-scribbles/scribbles-9");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 10, "sandstone-scribbles/scribbles-10");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 11, "sandstone-scribbles/scribbles-11");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 12, "sandstone-scribbles/scribbles-12");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 13, "sandstone-scribbles/scribbles-13");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 14, "sandstone-scribbles/scribbles-14");
		blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 15, "sandstone-scribbles/scribbles-15");
		blockSandstoneScribbles.carverHelper.register(blockSandstoneScribbles, "sandstoneScribbles");

		blockConcrete = (BlockConcrete) new BlockConcrete().setStepSound(Block.soundTypeStone).setHardness(0.5F);
		blockConcrete.carverHelper.addVariation("Concrete", 0, "concrete/default");
		blockConcrete.carverHelper.addVariation("Concrete block", 1, "concrete/block");
		blockConcrete.carverHelper.addVariation("Concrete double slab", 2, "concrete/doubleslab");
		blockConcrete.carverHelper.addVariation("Small concrete blocks", 3, "concrete/blocks");
		blockConcrete.carverHelper.addVariation("Weathered concrete", 4, "concrete/weathered");
		blockConcrete.carverHelper.addVariation("Weathered concrete block", 5, "concrete/weathered-block");
		blockConcrete.carverHelper.addVariation("Weathered concrete double slab", 6, "concrete/weathered-doubleslab");
		blockConcrete.carverHelper.addVariation("Small weathered blocks", 7, "concrete/weathered-blocks");
		blockConcrete.carverHelper.addVariation("Partly weathered concrete", 8, "concrete/weathered-half");
		blockConcrete.carverHelper.addVariation("Partly weathered concrete block", 9, "concrete/weathered-block-half");
		blockConcrete.carverHelper.addVariation("Asphalt", 10, "concrete/asphalt");
		blockConcrete.carverHelper.register(blockConcrete, "concrete");
		OreDictionary.registerOre("blockConcrete", blockConcrete);
		Carving.chisel.registerOre("concrete","blockConcrete");
		
		blockRoadLine = (BlockRoadLine) new BlockRoadLine().setStepSound(Block.soundTypeStone).setHardness(0.01F).setBlockName("roadLine");
		GameRegistry.registerBlock(blockRoadLine, ItemCarvable.class, "roadLine");
		LanguageRegistry.addName(new ItemStack(blockRoadLine, 1, 0), "Road lines");
				
		blockIron = (BlockCarvable) new BlockCarvable().setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal);
		blockIron.carverHelper.addVariation("Iron block", 0, Blocks.iron_block);
		blockIron.carverHelper.addVariation("Large iron ingots", 1, "iron/terrain-iron-largeingot");
		blockIron.carverHelper.addVariation("Small iron ingots", 2, "iron/terrain-iron-smallingot");
		blockIron.carverHelper.addVariation("Iron gears", 3, "iron/terrain-iron-gears");
		blockIron.carverHelper.addVariation("Iron bricks", 4, "iron/terrain-iron-brick");
		blockIron.carverHelper.addVariation("Iron plates", 5, "iron/terrain-iron-plates");
		blockIron.carverHelper.addVariation("Iron plates with rivets", 6, "iron/terrain-iron-rivets");
		blockIron.carverHelper.addVariation("Iron coin stack heads up", 7, "iron/terrain-iron-coin-heads");
		blockIron.carverHelper.addVariation("Iron coin stack heads down", 8, "iron/terrain-iron-coin-tails");
		blockIron.carverHelper.addVariation("Dark iron crate", 9, "iron/terrain-iron-crate-dark");
		blockIron.carverHelper.addVariation("Light iron crate", 10, "iron/terrain-iron-crate-light");
		blockIron.carverHelper.addVariation("Iron block with moon decoration", 11, "iron/terrain-iron-moon");
		blockIron.carverHelper.addVariation("Iron moon in purple obsidian", 12, "iron/terrain-iron-space");
		blockIron.carverHelper.addVariation("Iron moon in obsidian", 13, "iron/terrain-iron-spaceblack");
		blockIron.carverHelper.addVariation("Iron vents", 14, "iron/terrain-iron-vents");
		blockIron.carverHelper.addVariation("Iron block simple", 15, "iron/terrain-iron-simple");
		blockIron.carverHelper.register(blockIron, "iron");
		Carving.chisel.registerOre("iron","blockIron");

		blockGold = (BlockCarvable) new BlockCarvable().setHardness(3F).setResistance(10F).setStepSound(Block.soundTypeMetal);
		blockGold.carverHelper.addVariation("Gold block", 0, Blocks.gold_block);
		blockGold.carverHelper.addVariation("Large golden ingots", 1, "gold/terrain-gold-largeingot");
		blockGold.carverHelper.addVariation("Small golden ingots", 2, "gold/terrain-gold-smallingot");
		blockGold.carverHelper.addVariation("Golden bricks", 3, "gold/terrain-gold-brick");
		blockGold.carverHelper.addVariation("Gold cart", 4, "gold/terrain-gold-cart");
		blockGold.carverHelper.addVariation("Golden coin stack heads up", 5, "gold/terrain-gold-coin-heads");
		blockGold.carverHelper.addVariation("Golden coin stack heads down", 6, "gold/terrain-gold-coin-tails");
		blockGold.carverHelper.addVariation("Dark gold crate", 7, "gold/terrain-gold-crate-dark");
		blockGold.carverHelper.addVariation("Light gold crate", 8, "gold/terrain-gold-crate-light");
		blockGold.carverHelper.addVariation("Golden plates", 9, "gold/terrain-gold-plates");
		blockGold.carverHelper.addVariation("Iron plates with rivets", 10, "gold/terrain-gold-rivets");
		blockGold.carverHelper.addVariation("Gold block with star decoration", 11, "gold/terrain-gold-star");
		blockGold.carverHelper.addVariation("Golden star in purple obsidian", 12, "gold/terrain-gold-space");
		blockGold.carverHelper.addVariation("Golden star in obsidian", 13, "gold/terrain-gold-spaceblack");
		blockGold.carverHelper.addVariation("Gold block simple", 14, "gold/terrain-gold-simple");
		blockGold.carverHelper.register(blockGold, "gold");
		Carving.chisel.registerOre("gold","blockGold");

		blockDiamond = (BlockCarvable) new BlockCarvable().setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal);
		blockDiamond.carverHelper.addVariation("Diamond block", 0, Blocks.diamond_block);
		blockDiamond.carverHelper.addVariation("Embossed diamond block", 1, "diamond/terrain-diamond-embossed");
		blockDiamond.carverHelper.addVariation("Diamond block with panel", 2, "diamond/terrain-diamond-gem");
		blockDiamond.carverHelper.addVariation("Diamond cells", 3, "diamond/terrain-diamond-cells");
		blockDiamond.carverHelper.addVariation("Diamonds in purple obsidian", 4, "diamond/terrain-diamond-space");
		blockDiamond.carverHelper.addVariation("Diamonds in obsidian", 5, "diamond/terrain-diamond-spaceblack");
		blockDiamond.carverHelper.addVariation("Diamond block simple", 6, "diamond/terrain-diamond-simple");
		blockDiamond.carverHelper.addVariation("Bismuth", 7, "diamond/a1-blockdiamond-bismuth");
		blockDiamond.carverHelper.addVariation("Crushed diamond", 8, "diamond/a1-blockdiamond-crushed");
		blockDiamond.carverHelper.addVariation("Small diamond blocks", 9, "diamond/a1-blockdiamond-four");
		blockDiamond.carverHelper.addVariation("Small ornate diamond blocks", 10, "diamond/a1-blockdiamond-fourornate");
		blockDiamond.carverHelper.addVariation("Zelda diamond block", 11, "diamond/a1-blockdiamond-zelda");
		blockDiamond.carverHelper.addVariation("Diamond block with ornate layer", 12, "diamond/a1-blockdiamond-ornatelayer");
		blockDiamond.carverHelper.register(blockDiamond, "diamond");
		Carving.chisel.registerOre("diamond","blockDiamond");

		blockLightstone = (BlockLightstoneCarvable) new BlockLightstoneCarvable().setHardness(0.3F).setLightLevel(1.0F).setStepSound(Block.soundTypeGlass);
		blockLightstone.carverHelper.addVariation("Glowstone", 0, Blocks.glowstone);
		blockLightstone.carverHelper.addVariation("Cobble glowstone block", 1, "lightstone/terrain-sulphur-cobble");
		blockLightstone.carverHelper.addVariation("Corroded glowstone blocks", 2, "lightstone/terrain-sulphur-corroded");
		blockLightstone.carverHelper.addVariation("Glowstone blocks with glass", 3, "lightstone/terrain-sulphur-glass");
		blockLightstone.carverHelper.addVariation("Neon glowstone", 4, "lightstone/terrain-sulphur-neon");
		blockLightstone.carverHelper.addVariation("Ornate glowstone blocks", 5, "lightstone/terrain-sulphur-ornate");
		blockLightstone.carverHelper.addVariation("Rocky glowstone", 6, "lightstone/terrain-sulphur-rocky");
		blockLightstone.carverHelper.addVariation("Shale-shaped glowstone", 7, "lightstone/terrain-sulphur-shale");
		blockLightstone.carverHelper.addVariation("Glowstone tiles", 8, "lightstone/terrain-sulphur-tile");
		blockLightstone.carverHelper.addVariation("Fancy glowstone latern", 9, "lightstone/terrain-sulphur-weavelanternlight");
		blockLightstone.carverHelper.addVariation("Crumbling glowstone block", 10, "lightstone/a1-glowstone-cobble");
		blockLightstone.carverHelper.addVariation("Organic glowstone growth block", 11, "lightstone/a1-glowstone-growth");
		blockLightstone.carverHelper.addVariation("Glowstone layers", 12, "lightstone/a1-glowstone-layers");
		blockLightstone.carverHelper.addVariation("Corroded glowstone tiles", 13, "lightstone/a1-glowstone-tilecorroded");
		blockLightstone.carverHelper.addVariation("Glowstone bismuth", 14, "lightstone/glowstone-bismuth");
		blockLightstone.carverHelper.addVariation("Glowstone bismuth panel", 15, "lightstone/glowstone-bismuth-panel");
		blockLightstone.carverHelper.register(blockLightstone, "lightstone");
		Carving.chisel.registerOre("lightstone","blockGlowstone");

		blockLapis = (BlockCarvable) new BlockCarvable().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone);
		blockLapis.carverHelper.addVariation("Lapis block", 0, Blocks.lapis_block);
		blockLapis.carverHelper.addVariation("Chunky lapis block", 1, "lapis/terrain-lapisblock-chunky");
		blockLapis.carverHelper.addVariation("Dark lapis block", 2, "lapis/terrain-lapisblock-panel");
		blockLapis.carverHelper.addVariation("Zelda lapis block", 3, "lapis/terrain-lapisblock-zelda");
		blockLapis.carverHelper.addVariation("Ornate lapis block", 4, "lapis/terrain-lapisornate");
		blockLapis.carverHelper.addVariation("Lapis tile", 5, "lapis/terrain-lapistile");
		blockLapis.carverHelper.addVariation("Lapis panel", 6, "lapis/a1-blocklapis-panel");
		blockLapis.carverHelper.addVariation("Smooth lapis", 7, "lapis/a1-blocklapis-smooth");
		blockLapis.carverHelper.addVariation("Lapis with ornate layer", 8, "lapis/a1-blocklapis-ornatelayer");
		blockLapis.carverHelper.register(blockLapis, "lapis");
		Carving.chisel.registerOre("lapis","blockLapis");

		blockEmerald = (BlockCarvable) new BlockCarvable().setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal);
		blockEmerald.carverHelper.addVariation("Emerald block", 0, Blocks.emerald_block);
		blockEmerald.carverHelper.addVariation("Emerald panel", 1, "emerald/a1-blockemerald-emeraldpanel");
		blockEmerald.carverHelper.addVariation("Classic emerald panel", 2, "emerald/a1-blockemerald-emeraldpanelclassic");
		blockEmerald.carverHelper.addVariation("Smooth emerald", 3, "emerald/a1-blockemerald-emeraldsmooth");
		blockEmerald.carverHelper.addVariation("Emerald chunk", 4, "emerald/a1-blockemerald-emeraldchunk");
		blockEmerald.carverHelper.addVariation("Emerald with ornate layer", 5, "emerald/a1-blockemerald-emeraldornatelayer");
		blockEmerald.carverHelper.addVariation("Zelda emerald block", 6, "emerald/a1-blockemerald-emeraldzelda");
		blockEmerald.carverHelper.addVariation("Emerald cell", 7, "emerald/a1-blockquartz-cell");
		blockEmerald.carverHelper.addVariation("Emerald bismuth", 8, "emerald/a1-blockquartz-cellbismuth");
		blockEmerald.carverHelper.addVariation("Small emerald blocks", 9, "emerald/a1-blockquartz-four");
		blockEmerald.carverHelper.addVariation("Small ornate emerald blocks", 10, "emerald/a1-blockquartz-fourornate");
		blockEmerald.carverHelper.addVariation("Ornate emerald block", 11, "emerald/a1-blockquartz-ornate");
		blockEmerald.carverHelper.register(blockEmerald, "emerald");
		Carving.chisel.registerOre("emerald","blockEmerald");

		blockNetherBrick = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypeStone);
		blockNetherBrick.carverHelper.addVariation("Nether brick", 0, Blocks.nether_brick);
		blockNetherBrick.carverHelper.addVariation("Blue nether brick", 1, "netherbrick/a1-netherbrick-brinstar");
		blockNetherBrick.carverHelper.addVariation("Spattered nether brick", 2, "netherbrick/a1-netherbrick-classicspatter");
		blockNetherBrick.carverHelper.addVariation("Nether brick made of guts", 3, "netherbrick/a1-netherbrick-guts");
		blockNetherBrick.carverHelper.addVariation("Dark nether brick made of guts", 4, "netherbrick/a1-netherbrick-gutsdark");
		blockNetherBrick.carverHelper.addVariation("Small nether brick made of guts", 5, "netherbrick/a1-netherbrick-gutssmall");
		blockNetherBrick.carverHelper.addVariation("Blue nether brick with lava", 6, "netherbrick/a1-netherbrick-lavabrinstar");
		blockNetherBrick.carverHelper.addVariation("Brown nether brick", 7, "netherbrick/a1-netherbrick-lavabrown");
		blockNetherBrick.carverHelper.addVariation("Obsidian nether brick", 8, "netherbrick/a1-netherbrick-lavaobsidian");
		blockNetherBrick.carverHelper.addVariation("Stone nether brick", 9, "netherbrick/a1-netherbrick-lavastonedark");
		blockNetherBrick.carverHelper.addVariation("Nether brick made of meat", 10, "netherbrick/a1-netherbrick-meat");
		blockNetherBrick.carverHelper.addVariation("Red nether brick made of meat", 11, "netherbrick/a1-netherbrick-meatred");
		blockNetherBrick.carverHelper.addVariation("Small nether brick made of meat", 12, "netherbrick/a1-netherbrick-meatredsmall");
		blockNetherBrick.carverHelper.addVariation("Small red nether brick made of meat", 13, "netherbrick/a1-netherbrick-meatsmall");
		blockNetherBrick.carverHelper.addVariation("Red nether brick", 14, "netherbrick/a1-netherbrick-red");
		blockNetherBrick.carverHelper.addVariation("Small red nether brick", 15, "netherbrick/a1-netherbrick-redsmall");
		blockNetherBrick.carverHelper.register(blockNetherBrick, "netherBrick");
		Carving.chisel.registerOre("netherBrick","netherBrick");

		blockNetherrack = (BlockCarvable) new BlockCarvable().setHardness(0.4F).setStepSound(Block.soundTypeStone);
		blockNetherrack.carverHelper.addVariation("Netherrack", 0, Blocks.netherrack);
		blockNetherrack.carverHelper.addVariation("Nethegravel with blood", 1, "netherrack/a1-netherrack-bloodgravel");
		blockNetherrack.carverHelper.addVariation("Netherrack with blood", 2, "netherrack/a1-netherrack-bloodrock");
		blockNetherrack.carverHelper.addVariation("Darker netherrack with blood", 3, "netherrack/a1-netherrack-bloodrockgrey");
		blockNetherrack.carverHelper.addVariation("Blue netherrack", 4, "netherrack/a1-netherrack-brinstar");
		blockNetherrack.carverHelper.addVariation("Shale blue netherrack", 5, "netherrack/a1-netherrack-brinstarshale");
		blockNetherrack.carverHelper.addVariation("Classic netherrack", 6, "netherrack/a1-netherrack-classic");
		blockNetherrack.carverHelper.addVariation("Spattered netherrack", 7, "netherrack/a1-netherrack-classicspatter");
		blockNetherrack.carverHelper.addVariation("Netherrack made of guts", 8, "netherrack/a1-netherrack-guts");
		blockNetherrack.carverHelper.addVariation("Dark netherrack made of guts", 9, "netherrack/a1-netherrack-gutsdark");
		blockNetherrack.carverHelper.addVariation("Netherrack made of meat", 10, "netherrack/a1-netherrack-meat");
		blockNetherrack.carverHelper.addVariation("Red netherrack made of meat", 11, "netherrack/a1-netherrack-meatred");
		blockNetherrack.carverHelper.addVariation("Netherrack made of smaller meat chunks", 12, "netherrack/a1-netherrack-meatrock");
		blockNetherrack.carverHelper.addVariation("Dark red netherrack", 13, "netherrack/a1-netherrack-red");
		blockNetherrack.carverHelper.addVariation("Netherrack with lava flowing", 14, "netherrack/a1-netherrack-wells");
		blockNetherrack.carverHelper.register(blockNetherrack, "hellrock");
		Carving.chisel.registerOre("hellrock","blockNetherrack");

		blockCobblestoneMossy = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypeStone);
		blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone", 0, Blocks.mossy_cobblestone);
		blockCobblestoneMossy.carverHelper.addVariation("Aligned mossy cobblestone bricks", 1, "cobblestonemossy/terrain-cobb-brickaligned");
		blockCobblestoneMossy.carverHelper.addVariation("Detailed mossy cobblestone bricks", 2, "cobblestonemossy/terrain-cob-detailedbrick");
		blockCobblestoneMossy.carverHelper.addVariation("Small mossy cobblestone bricks", 3, "cobblestonemossy/terrain-cob-smallbrick");
		blockCobblestoneMossy.carverHelper.addVariation("Large mossy cobblestone tiles", 4, "cobblestonemossy/terrain-cobblargetiledark");
		blockCobblestoneMossy.carverHelper.addVariation("Small mossy cobblestone tiles", 5, "cobblestonemossy/terrain-cobbsmalltile");
		blockCobblestoneMossy.carverHelper.addVariation("French mossy cobblestone tiles", 6, "cobblestonemossy/terrain-cob-french");
		blockCobblestoneMossy.carverHelper.addVariation("French mossy cobblestone tiles", 7, "cobblestonemossy/terrain-cob-french2");
		blockCobblestoneMossy.carverHelper.addVariation("Creeper mossy cobblestone tiles", 8, "cobblestonemossy/terrain-cobmoss-creepdungeon");
		blockCobblestoneMossy.carverHelper.addVariation("Damaged mossy cobblestone tiles", 9, "cobblestonemossy/terrain-mossysmalltiledark");
		blockCobblestoneMossy.carverHelper.addVariation("Huge mossy cobblestone tiles", 10, "cobblestonemossy/terrain-pistonback-dungeontile");
		blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone with creeper panel", 11, "cobblestonemossy/terrain-pistonback-darkcreeper");
		blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone with dent", 12, "cobblestonemossy/terrain-pistonback-darkdent");
		blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone with panel", 13, "cobblestonemossy/terrain-pistonback-darkemboss");
		blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone with light panel", 14, "cobblestonemossy/terrain-pistonback-darkmarker");
		blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone with dark panel", 15, "cobblestonemossy/terrain-pistonback-darkpanel");
		blockCobblestoneMossy.carverHelper.register(blockCobblestoneMossy, "stoneMoss");
		Carving.chisel.registerOre("stoneMoss","blockCobblestoneMossy");
		
		stoneBrick = (BlockCarvable) new BlockCarvable().setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypeStone);
		stoneBrick.carverHelper.addVariation("Stone bricks", 0, Blocks.stonebrick,0);
		stoneBrick.carverHelper.addVariation("Mossy stone bricks", 1, Blocks.stonebrick,1);
		stoneBrick.carverHelper.addVariation("Cracked stone bricks", 2, Blocks.stonebrick,2);
		stoneBrick.carverHelper.addVariation("Chiseled stone bricks", 3, Blocks.stonebrick,3);
		stoneBrick.carverHelper.addVariation("Small stone bricks", 4, "stonebrick/smallbricks");
		stoneBrick.carverHelper.addVariation("Wide stone bricks", 5, "stonebrick/largebricks");
		stoneBrick.carverHelper.addVariation("Small disordered stone bricks", 6, "stonebrick/smallchaotic");
		stoneBrick.carverHelper.addVariation("Disordered stone bricks", 7, "stonebrick/chaoticbricks");
		stoneBrick.carverHelper.addVariation("Disordered stone panels", 8, "stonebrick/chaotic");
		stoneBrick.carverHelper.addVariation("Stone bricks in a fancy arrangement", 9, "stonebrick/fancy");
		stoneBrick.carverHelper.addVariation("Ornate stone brick tiles", 10, "stonebrick/ornate");
		stoneBrick.carverHelper.addVariation("Large ornate stone brick tiles", 11, "stonebrick/largeornate");
		stoneBrick.carverHelper.addVariation("Stone panel", 12, "stonebrick/panel-hard");
		stoneBrick.carverHelper.addVariation("Sunken stone panel", 13, "stonebrick/sunken");
		stoneBrick.carverHelper.addVariation("Ornate stone panel", 14, "stonebrick/ornatepanel");
		stoneBrick.carverHelper.addVariation("Poison stone brick", 15, "stonebrick/poison");
		stoneBrick.carverHelper.register(stoneBrick, "stoneBrick");
		Carving.chisel.registerOre("stoneBrick","blockStoneBrick");

		blockSnakestone=(BlockSnakestone) new BlockSnakestone("Chisel:snakestone/snake/").setBlockName("snakestone");
		GameRegistry.registerBlock(blockSnakestone, ItemCarvable.class, blockSnakestone.getUnlocalizedName());
		LanguageRegistry.addName(new ItemStack(blockSnakestone, 1, 1), "Stone snake block head");
		LanguageRegistry.addName(new ItemStack(blockSnakestone, 1, 13), "Stone snake block body");		
		Carving.chisel.addVariation("stoneBrick", blockSnakestone, 1, 16);
		Carving.chisel.addVariation("stoneBrick", blockSnakestone, 13, 17);
		
		blockIce = (BlockMarbleIce) new BlockMarbleIce().setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass);
		blockIce.carverHelper.addVariation("Ice block", 0, Blocks.ice);
		blockIce.carverHelper.addVariation("Rough ice block", 1, "ice/a1-ice-light");
		blockIce.carverHelper.addVariation("Cobbleice", 2, "ice/a1-stonecobble-icecobble");
		blockIce.carverHelper.addVariation("Large rough ice bricks", 3, "ice/a1-netherbrick-ice");
		blockIce.carverHelper.addVariation("Large ice bricks", 4, "ice/a1-stonecobble-icebrick");
		blockIce.carverHelper.addVariation("Small ice bricks", 5, "ice/a1-stonecobble-icebricksmall");
		blockIce.carverHelper.addVariation("Fancy glass wall", 6, "ice/a1-stonecobble-icedungeon");
		blockIce.carverHelper.addVariation("Large ice tiles", 7, "ice/a1-stonecobble-icefour");
		blockIce.carverHelper.addVariation("Fancy ice tiles", 8, "ice/a1-stonecobble-icefrench");
		blockIce.carverHelper.addVariation("Sunken ice tiles", 9, "ice/sunkentiles");
		blockIce.carverHelper.addVariation("Disordered ice tiles", 10, "ice/tiles");
		blockIce.carverHelper.addVariation("Ice panel", 11, "ice/a1-stonecobble-icepanel");
		blockIce.carverHelper.addVariation("Ice double slab", 12, "ice/a1-stoneslab-ice");
		blockIce.carverHelper.addVariation("Ice Zelda block", 13, "ice/zelda");
		blockIce.carverHelper.addVariation("Ice Bismuth block", 14, "ice/bismuth");
		blockIce.carverHelper.addVariation("Ice Poison block", 15, "ice/poison");
		blockIce.carverHelper.register(blockIce, "ice");
		Carving.chisel.registerOre("ice","blockIce");
		
		blockIcePillar = (BlockMarbleIce) new BlockMarbleIce().setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass);
		blockIcePillar.carverHelper.setBlockName("Ice Pillar");
		blockIcePillar.carverHelper.addVariation("Ice pillar", 0, "icepillar/column");
		blockIcePillar.carverHelper.addVariation("Ice pillar capstone", 1, "icepillar/capstone");
		blockIcePillar.carverHelper.addVariation("Ice pillar base", 2, "icepillar/base");
		blockIcePillar.carverHelper.addVariation("Small ice pillar", 3, "icepillar/small");
		blockIcePillar.carverHelper.addVariation("Carved ice pillar", 4, "icepillar/pillar-carved");
		blockIcePillar.carverHelper.addVariation("Ornamental ice pillar", 5, "icepillar/a1-stoneornamental-marblegreek");
		blockIcePillar.carverHelper.addVariation("Greek ice pillar", 6, "icepillar/a1-stonepillar-greek");
		blockIcePillar.carverHelper.addVariation("Plain ice pillar", 7, "icepillar/a1-stonepillar-plain");
		blockIcePillar.carverHelper.addVariation("Greek ice pillar capstone", 8, "icepillar/a1-stonepillar-greektopplain");
		blockIcePillar.carverHelper.addVariation("Plain ice pillar capstone", 9, "icepillar/a1-stonepillar-plaintopplain");
		blockIcePillar.carverHelper.addVariation("Greek ice pillar base", 10, "icepillar/a1-stonepillar-greekbottomplain");
		blockIcePillar.carverHelper.addVariation("Plain ice pillar base", 11, "icepillar/a1-stonepillar-plainbottomplain");
		blockIcePillar.carverHelper.addVariation("Greek ice pillar ornate capstone", 12, "icepillar/a1-stonepillar-greektopgreek");
		blockIcePillar.carverHelper.addVariation("Plain ice pillar ornate capstone", 13, "icepillar/a1-stonepillar-plaintopgreek");
		blockIcePillar.carverHelper.addVariation("Greek ice pillar ornate base", 14, "icepillar/a1-stonepillar-greekbottomgreek");
		blockIcePillar.carverHelper.addVariation("Plain ice pillar ornate base", 15, "icepillar/a1-stonepillar-plainbottomgreek");
		blockIcePillar.carverHelper.register(blockIcePillar, "icePillar");
		Carving.chisel.setGroupClass("icePillar", "ice");

		BlockMarbleStairsMaker makerIceStairs=new BlockMarbleStairsMaker(Blocks.ice);
		makerIceStairs.carverHelper.setBlockName("Ice Stairs");
		makerIceStairs.carverHelper.addVariation("Ice stairs", 0, Blocks.ice);
		makerIceStairs.carverHelper.addVariation("Rough ice stairs", 1, "ice/a1-ice-light");
		makerIceStairs.carverHelper.addVariation("Cobbleice stairs", 2, "ice/a1-stonecobble-icecobble");
		makerIceStairs.carverHelper.addVariation("Large rough ice brick stairs", 3, "ice/a1-netherbrick-ice");
		makerIceStairs.carverHelper.addVariation("Large ice brick stairs", 4, "ice/a1-stonecobble-icebrick");
		makerIceStairs.carverHelper.addVariation("Small ice brick stairs", 5, "ice/a1-stonecobble-icebricksmall");
		makerIceStairs.carverHelper.addVariation("Fancy ice wall stairs", 6, "ice/a1-stonecobble-icedungeon");
		makerIceStairs.carverHelper.addVariation("Large ice tile stairs", 7, "ice/a1-stonecobble-icefour");
		makerIceStairs.carverHelper.addVariation("Fancy ice tile stairs", 8, "ice/a1-stonecobble-icefrench");
		makerIceStairs.carverHelper.addVariation("Sunken ice tile stairs", 9, "ice/sunkentiles");
		makerIceStairs.carverHelper.addVariation("Disordered ice tile stairs", 10, "ice/tiles");
		makerIceStairs.carverHelper.addVariation("Ice panel stairs", 11, "ice/a1-stonecobble-icepanel");
		makerIceStairs.carverHelper.addVariation("Ice double slab stairs", 12, "ice/a1-stoneslab-ice");
		makerIceStairs.carverHelper.addVariation("Ice Zelda stairs", 13, "ice/zelda");
		makerIceStairs.carverHelper.addVariation("Ice bismuth stairs", 14, "ice/bismuth");
		makerIceStairs.carverHelper.addVariation("Ice poison stairs", 15, "ice/poison");
		makerIceStairs.create(new BlockMarbleStairsMakerCreator(){
			@Override public BlockMarbleStairs create(Block block, int meta, CarvableHelper helper) {
				return new BlockMarbleIceStairs(block,meta,helper);
			}
		});
		
		String[] plank_names={"oak", "spruce", "birch", "jungle", "acacia", "dark-oak"};
		String[] plank_ucnames={"Oak", "Spruce", "Birch", "Jungle", "Acacia", "Dark Oak"};
		for(int i=0;i<6;i++){
			String n=plank_names[i];
			String u=plank_ucnames[i];
			
			blockPlanks[i] = (BlockCarvable) (new BlockCarvable()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood);
			blockPlanks[i].carverHelper.setBlockName(u+" Wood Planks");
			blockPlanks[i].carverHelper.addVariation("Smooth "+n+" wood planks", 1, "planks-"+n+"/clean");
			blockPlanks[i].carverHelper.addVariation("Short "+n+" wood planks", 2, "planks-"+n+"/short");
			blockPlanks[i].carverHelper.addVariation("Fancy "+n+" wood plank arrangement", 6, "planks-"+n+"/fancy");
			blockPlanks[i].carverHelper.addVariation(u+" wood panel", 8, "planks-"+n+"/panel-nails");
			blockPlanks[i].carverHelper.addVariation(u+" wood double slab", 9, "planks-"+n+"/double");
			blockPlanks[i].carverHelper.addVariation(u+" wood crate", 10, "planks-"+n+"/crate");
			blockPlanks[i].carverHelper.addVariation("Fancy "+n+" wood crate", 11, "planks-"+n+"/crate-fancy");
			blockPlanks[i].carverHelper.addVariation("Large long "+n+" wood planks", 13, "planks-"+n+"/large");
			if(i < 4) { // TODO: We lack textures for these!
				blockPlanks[i].carverHelper.addVariation("Vertical "+n+" wood planks", 3, "planks-"+n+"/vertical");
				blockPlanks[i].carverHelper.addVariation("Vertical uneven "+n+" wood planks", 4, "planks-"+n+"/vertical-uneven");
				blockPlanks[i].carverHelper.addVariation(u+" wood parquet", 5, "planks-"+n+"/parquet");
				blockPlanks[i].carverHelper.addVariation(u+" wood plank blinds", 7, "planks-"+n+"/blinds");
				blockPlanks[i].carverHelper.addVariation(u+" wood scaffold", 12, "planks-"+n+"/crateex");
				blockPlanks[i].carverHelper.addVariation(u+" wood planks in disarray", 14, "planks-"+n+"/chaotic-hor");
				blockPlanks[i].carverHelper.addVariation("Vertical "+n+" wood planks in disarray", 15, "planks-"+n+"/chaotic");
			}
			blockPlanks[i].carverHelper.register(blockPlanks[i], "wood-"+n);
			Carving.chisel.addVariation("wood-"+n, Blocks.planks, i, 0);
			Blocks.planks.setHarvestLevel("chisel", 0, i);
		    blockPlanks[i].setHarvestLevel("axe", 0);
			
			Carving.chisel.setVariationSound("wood-"+n, "chisel:chisel-wood");
		}
		
	    blockObsidian = (BlockCarvable) new BlockCarvable().setHardness(50.0F).setResistance(2000.0F).setStepSound(Block.soundTypeStone);
	    blockObsidian.carverHelper.addVariation("Obsidian", 0, Blocks.obsidian);
	    blockObsidian.carverHelper.addVariation("Large obsidian pillar", 1, "obsidian/pillar");
	    blockObsidian.carverHelper.addVariation("Obsidian pillar", 2, "obsidian/pillar-quartz");
	    blockObsidian.carverHelper.addVariation("Chiseled obsidian", 3, "obsidian/chiseled");
	    blockObsidian.carverHelper.addVariation("Shiny obsidian panel", 4, "obsidian/panel-shiny");
	    blockObsidian.carverHelper.addVariation("Obsidian panel", 5, "obsidian/panel");
	    blockObsidian.carverHelper.addVariation("Organic-looking obsidian chunks", 6, "obsidian/chunks");
	    blockObsidian.carverHelper.addVariation("Organic-looking obsidian growth", 7, "obsidian/growth");
	    blockObsidian.carverHelper.addVariation("Obsidian crystal", 8, "obsidian/crystal");
	    blockObsidian.carverHelper.addVariation("Obsidian panel with an ancient map on it", 9, "obsidian/map-a");
	    blockObsidian.carverHelper.addVariation("Obsidian panel with a map of some weird region on it", 10, "obsidian/map-b");
	    blockObsidian.carverHelper.addVariation("Bright obsidian panel", 11, "obsidian/panel-light");
	    blockObsidian.carverHelper.addVariation("Obsidian blocks", 12, "obsidian/blocks");
	    blockObsidian.carverHelper.addVariation("Obsidian tiles", 13, "obsidian/tiles");
	    blockObsidian.carverHelper.addVariation("Light obsidian blocks with Greek decor", 14, "obsidian/greek");
	    blockObsidian.carverHelper.addVariation("Small obsidian blocks inside an oak wood crate", 15, "obsidian/crate");
	    blockObsidian.carverHelper.register(blockObsidian, "obsidian");
		Carving.chisel.registerOre("obsidian","blockObsidian");
	    
	    blockObsidianSnakestone=(BlockSnakestoneObsidian) new BlockSnakestoneObsidian("Chisel:snakestone/obsidian/").setBlockName("obsidianSnakestone").setHardness(50.0F).setResistance(2000.0F);
		GameRegistry.registerBlock(blockObsidianSnakestone, ItemCarvable.class, blockObsidianSnakestone.getUnlocalizedName());
		LanguageRegistry.addName(new ItemStack(blockObsidianSnakestone, 1, 1), "Obsidian snakestone head");
		LanguageRegistry.addName(new ItemStack(blockObsidianSnakestone, 1, 13), "Obsidian snakestone body");		
		Carving.chisel.addVariation("obsidian", blockObsidianSnakestone, 1, 16);
		Carving.chisel.addVariation("obsidian", blockObsidianSnakestone, 13, 17);
		
		if(config.get("general", "Add panes", true, "Changes pane rendering algorithm a bit.").getBoolean(true)){
			blockPaneIron = (BlockCarvablePane) new BlockCarvablePane(Material.iron, true).setHardness(0.3F).setStepSound(Block.soundTypeMetal);
			blockPaneIron.carverHelper.addVariation("Iron bars", 0, Blocks.iron_bars);
			blockPaneIron.carverHelper.addVariation("Iron bars without frame", 1, "ironpane/fenceIron");
			blockPaneIron.carverHelper.addVariation("Menacing iron bars", 2, "ironpane/barbedwire");
			blockPaneIron.carverHelper.addVariation("Iron cage bars", 3, "ironpane/cage");
			blockPaneIron.carverHelper.addVariation("Menacing iron spikes", 4, "ironpane/fenceIronTop");
			blockPaneIron.carverHelper.addVariation("Thick iron grid", 5, "ironpane/terrain-glass-thickgrid");
			blockPaneIron.carverHelper.addVariation("Thin iron grid", 6, "ironpane/terrain-glass-thingrid");
			blockPaneIron.carverHelper.addVariation("Ornate iron pane fence", 7, "ironpane/terrain-glass-ornatesteel");
			blockPaneIron.carverHelper.addVariation("Vertical iron bars", 8, "ironpane/bars");
			blockPaneIron.carverHelper.addVariation("Iron spikes", 9, "ironpane/spikes");
			blockPaneIron.carverHelper.register(blockPaneIron, "fenceIron");

			blockPaneGlass = (BlockCarvablePane) new BlockCarvablePane(Material.glass, false).setHardness(0.3F).setStepSound(Block.soundTypeGlass);
			blockPaneGlass.carverHelper.addVariation("Glass pane", 0, Blocks.glass_pane);
			blockPaneGlass.carverHelper.addVariation("Bubble glass pane", 1, "glasspane/terrain-glassbubble");
			blockPaneGlass.carverHelper.addVariation("Borderless glass pane", 2, "glasspane/terrain-glassnoborder");
			blockPaneGlass.carverHelper.addVariation("Screen pane", 3, "glasspane/terrain-glass-screen");
			blockPaneGlass.carverHelper.addVariation("Streak glass pane", 4, "glasspane/terrain-glassstreak");
			blockPaneGlass.carverHelper.addVariation("Chinese glass pane", 12, "glasspane/chinese");
			blockPaneGlass.carverHelper.addVariation("Chinese glass pane with golden frame", 13, "glasspane/chinese2");
			blockPaneGlass.carverHelper.addVariation("Japanese glass pane", 14, "glasspane/japanese");
			blockPaneGlass.carverHelper.addVariation("Ornate japanese glass pane", 15, "glasspane/japanese2");
			blockPaneGlass.carverHelper.register(blockPaneGlass, "thinGlass");
		}
		
		blockRedstone = (BlockPoweredMarble) (new BlockPoweredMarble(Material.iron)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal);
		blockRedstone.carverHelper.addVariation("Redstone block", 0, Blocks.redstone_block);
		blockRedstone.carverHelper.addVariation("Smooth redstone", 1, "redstone/smooth");
		blockRedstone.carverHelper.addVariation("Large redstone block", 2, "redstone/block");
	    blockRedstone.carverHelper.addVariation("Small redstone blocks", 3, "redstone/blocks");
	    blockRedstone.carverHelper.addVariation("Redstone bricks", 4, "redstone/bricks");
	    blockRedstone.carverHelper.addVariation("Small redstone bricks", 5, "redstone/smallbricks");
	    blockRedstone.carverHelper.addVariation("Chaotic redstone bricks", 6, "redstone/smallchaotic");
	    blockRedstone.carverHelper.addVariation("Chiseled redstone", 7, "redstone/chiseled");
	    blockRedstone.carverHelper.addVariation("Redstone Greek decoration", 8, "redstone/ere");
	    blockRedstone.carverHelper.addVariation("Ornate redstone tiles", 9, "redstone/ornate-tiles");
	    blockRedstone.carverHelper.addVariation("Redstone pillar", 10, "redstone/pillar");
	    blockRedstone.carverHelper.addVariation("Redstone tiles", 11, "redstone/tiles");
	    blockRedstone.carverHelper.addVariation("Redstone circuit", 12, "redstone/circuit");
	    blockRedstone.carverHelper.addVariation("Redstone supaplex circuit", 13, "redstone/supaplex");
	    blockRedstone.carverHelper.addVariation("Redstone skulls", 14, "redstone/a1-blockredstone-skullred");
	    blockRedstone.carverHelper.addVariation("Redstone Zelda block", 15, "redstone/a1-blockredstone-redstonezelda");
	    blockRedstone.carverHelper.register(blockRedstone, "blockRedstone");
		Carving.chisel.registerOre("blockRedstone","blockRedstone");

	    blockHolystone = (BlockHolystone) new BlockHolystone(Material.rock).setHardness(2.0F).setResistance(10F).setStepSound(soundHolystoneFootstep);
	    blockHolystone.carverHelper.addVariation("Holystone", 0, "holystone/holystone");
	    blockHolystone.carverHelper.addVariation("Smooth holystone", 1, "holystone/smooth");
	    blockHolystone.carverHelper.addVariation("Mysterious holystone symbol", 2, "holystone/love");
	    blockHolystone.carverHelper.addVariation("Chiseled holystone", 3, "holystone/chiseled");
	    blockHolystone.carverHelper.addVariation("Holystone blocks", 4, "holystone/blocks");
	    blockHolystone.carverHelper.addVariation("Rough holystone blocks", 5, "holystone/blocks-rough");
	    blockHolystone.carverHelper.addVariation("Holystone bricks", 6, "holystone/brick");
	    blockHolystone.carverHelper.addVariation("Large holystone bricks", 7, "holystone/largebricks");
	    blockHolystone.carverHelper.addVariation("Holystone platform", 8, "holystone/platform");
	    blockHolystone.carverHelper.addVariation("Holystone platform tiles", 9, "holystone/platform-tiles");
	    blockHolystone.carverHelper.addVariation("Fancy holystone construction", 10, "holystone/construction");
	    blockHolystone.carverHelper.addVariation("Fancy holystone tiles", 11, "holystone/fancy-tiles");
	    blockHolystone.carverHelper.addVariation("Smooth holystone plate", 12, "holystone/plate");
	    blockHolystone.carverHelper.addVariation("Holystone plate", 13, "holystone/plate-rough");
	    blockHolystone.carverHelper.register(blockHolystone, "blockHolystone");
		OreDictionary.registerOre("blockHolystone", blockHolystone);
		Carving.chisel.registerOre("blockHolystone","blockHolystone");
	    
		blockLavastone=(BlockLavastone) new BlockLavastone(Material.rock, "lava_flow").setHardness(2.0F).setResistance(10F);
		blockLavastone.carverHelper.addVariation("Lavastone", 0, "lavastone/cobble");
		blockLavastone.carverHelper.addVariation("Black lavastone", 1, "lavastone/black");
		blockLavastone.carverHelper.addVariation("Lavastone tiles", 2, "lavastone/tiles");
		blockLavastone.carverHelper.addVariation("Chaotic lavastone bricks", 3, "lavastone/chaotic");
		blockLavastone.carverHelper.addVariation("Lava creeper in tiles", 4, "lavastone/creeper");
		blockLavastone.carverHelper.addVariation("Lava panel", 5, "lavastone/panel");
		blockLavastone.carverHelper.addVariation("Ornate lava panel", 6, "lavastone/panel-ornate");
		blockLavastone.carverHelper.register(blockLavastone, "blockLavastone");
		OreDictionary.registerOre("blockLavastone", blockLavastone);
		Carving.chisel.registerOre("blockLavastone","blockLavastone");
		
		blockFft=(BlockCarvable) new BlockCarvable(Material.rock).setHardness(2.0F).setResistance(10F);
		blockFft.carverHelper.setBlockName("Fantasy Block");
		blockFft.carverHelper.addVariation("Fantasy brick", 0, "fft/brick");
		blockFft.carverHelper.addVariation("Faded fantasy brick", 1, "fft/brick-faded");
		blockFft.carverHelper.addVariation("Weared fantasy brick", 2, "fft/brick-wear");
		blockFft.carverHelper.addVariation("Damaged fantasy bricks", 3, "fft/bricks");
		blockFft.carverHelper.addVariation("Fantasy decoration", 4, "fft/decor");
		blockFft.carverHelper.addVariation("Fantasy decoration block", 5, "fft/decor-block");
		blockFft.carverHelper.addVariation("Fantasy pillar", 6, "fft/pillar");
		blockFft.carverHelper.addVariation("Fantasy pillar decoration", 7, "fft/pillar-decorated");
		blockFft.carverHelper.addVariation("Fantasy gold snake decoration", 8, "fft/gold-decor-1");
		blockFft.carverHelper.addVariation("Fantasy gold noise decoration", 9, "fft/gold-decor-2");
		blockFft.carverHelper.addVariation("Fantasy gold engravings decoration", 10, "fft/gold-decor-3");
		blockFft.carverHelper.addVariation("Fantasy gold chains decoration", 11, "fft/gold-decor-4");
		blockFft.carverHelper.addVariation("Fantasy plate decoration", 12, "fft/plate");
		blockFft.carverHelper.addVariation("Fantasy block", 13, "fft/block");
		blockFft.carverHelper.addVariation("Fantasy bricks in disarray", 14, "fft/bricks-chaotic");
		blockFft.carverHelper.addVariation("Weared fantasy bricks", 15, "fft/bricks-wear");
		blockFft.carverHelper.register(blockFft, "blockFft");
		OreDictionary.registerOre("blockFft", blockFft);
		Carving.chisel.registerOre("blockFft","blockFft");
		
		blockCarpet=(BlockCarvable) new BlockCarvable(Material.cloth).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeCloth);
		blockCarpet.carverHelper.setBlockName("Carpet Block");
		blockCarpet.carverHelper.addVariation("White carpet block", 0, "carpet/white");
		blockCarpet.carverHelper.addVariation("Orange carpet block", 1, "carpet/orange");
		blockCarpet.carverHelper.addVariation("Magenta carpet block", 2, "carpet/lily");
		blockCarpet.carverHelper.addVariation("Light blue carpet block", 3, "carpet/lightblue");
		blockCarpet.carverHelper.addVariation("Yellow carpet block", 4, "carpet/yellow");
		blockCarpet.carverHelper.addVariation("Light green carpet block", 5, "carpet/lightgreen");
		blockCarpet.carverHelper.addVariation("Pink carpet block", 6, "carpet/pink");
		blockCarpet.carverHelper.addVariation("Dark grey carpet block", 7, "carpet/darkgrey");
		blockCarpet.carverHelper.addVariation("Grey carpet block", 8, "carpet/grey");
		blockCarpet.carverHelper.addVariation("Teal carpet block", 9, "carpet/teal");
		blockCarpet.carverHelper.addVariation("Purple carpet block", 10, "carpet/purple");
		blockCarpet.carverHelper.addVariation("Dark blue carpet block", 11, "carpet/darkblue");
		blockCarpet.carverHelper.addVariation("Brown carpet block", 12, "carpet/brown");
		blockCarpet.carverHelper.addVariation("Green carpet block", 13, "carpet/green");
		blockCarpet.carverHelper.addVariation("Red carpet block", 14, "carpet/red");
		blockCarpet.carverHelper.addVariation("Black carpet block", 15, "carpet/black");
		blockCarpet.carverHelper.forbidChiseling=true;		
		blockCarpet.carverHelper.register(blockCarpet, "blockCarpet");
		OreDictionary.registerOre("blockCarpet", blockCarpet);
		Carving.chisel.registerOre("blockCarpet","blockCarpet");
		
		blockCarpetFloor=(BlockMarbleCarpet) new BlockMarbleCarpet(Material.cloth).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeCloth);
		blockCarpetFloor.carverHelper.setBlockName("Carpet");
		blockCarpetFloor.carverHelper.addVariation("White carpet", 0, "carpet/white");
		blockCarpetFloor.carverHelper.addVariation("Orange carpet", 1, "carpet/orange");
		blockCarpetFloor.carverHelper.addVariation("Magenta carpet", 2, "carpet/lily");
		blockCarpetFloor.carverHelper.addVariation("Light blue carpet", 3, "carpet/lightblue");
		blockCarpetFloor.carverHelper.addVariation("Yellow carpet", 4, "carpet/yellow");
		blockCarpetFloor.carverHelper.addVariation("Light green carpet", 5, "carpet/lightgreen");
		blockCarpetFloor.carverHelper.addVariation("Pink carpet", 6, "carpet/pink");
		blockCarpetFloor.carverHelper.addVariation("Dark grey carpet", 7, "carpet/darkgrey");
		blockCarpetFloor.carverHelper.addVariation("Grey carpet", 8, "carpet/grey");
		blockCarpetFloor.carverHelper.addVariation("Teal carpet", 9, "carpet/teal");
		blockCarpetFloor.carverHelper.addVariation("Purple carpet", 10, "carpet/purple");
		blockCarpetFloor.carverHelper.addVariation("Dark blue carpet", 11, "carpet/darkblue");
		blockCarpetFloor.carverHelper.addVariation("Brown carpet", 12, "carpet/brown");
		blockCarpetFloor.carverHelper.addVariation("Green carpet", 13, "carpet/green");
		blockCarpetFloor.carverHelper.addVariation("Red carpet", 14, "carpet/red");
		blockCarpetFloor.carverHelper.addVariation("Black carpet", 15, "carpet/black");
		blockCarpetFloor.carverHelper.forbidChiseling=true;		
		blockCarpetFloor.carverHelper.register(blockCarpetFloor, "blockCarpetFloor");
		
		for(int i=0;i<16;i++){
			String group="carpet."+i;
			
			Carving.needle.addVariation(group, Blocks.carpet, i, 0);
			Carving.needle.addVariation(group, blockCarpetFloor, i, 2);
			Carving.needle.addVariation(group, blockCarpet, i, 1);
		}
		
		blockBookshelf=(BlockCarvable) new BlockMarbleBookshelf().setHardness(1.5F).setStepSound(Block.soundTypeWood);
		blockBookshelf.carverHelper.addVariation("Bookshelf", 0, Blocks.bookshelf);
		blockBookshelf.carverHelper.addVariation("Bookshelf with rainbow colored books", 1, "bookshelf/rainbow");
		blockBookshelf.carverHelper.addVariation("Necromancer novice's bookshelf", 2, "bookshelf/necromancer-novice");
		blockBookshelf.carverHelper.addVariation("Necromancer's bookshelf", 3, "bookshelf/necromancer");
		blockBookshelf.carverHelper.addVariation("Bookshelf with red tomes", 4, "bookshelf/redtomes");
		blockBookshelf.carverHelper.addVariation("Abandoned bookshelf", 5, "bookshelf/abandoned");
		blockBookshelf.carverHelper.addVariation("Hoarder's bookshelf", 6, "bookshelf/hoarder");
		blockBookshelf.carverHelper.addVariation("Bookshelf filled to brim with boring pastel books", 7, "bookshelf/brim");
		blockBookshelf.carverHelper.addVariation("Historician's bookshelf", 8, "bookshelf/historician");
		blockBookshelf.carverHelper.register(blockBookshelf, "blockBookshelf");
		blockBookshelf.setHarvestLevel("axe", 0);
		Carving.chisel.registerOre("blockBookshelf","blockBookshelf");
		
		
		blockTyrian=(BlockCarvable) new BlockCarvable(Material.iron).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal);
		blockTyrian.carverHelper.setBlockName("Futuristic Armor Plating Block");
		blockTyrian.carverHelper.addVariation("Futuristic armor plating block", 0, "tyrian/shining");
		blockTyrian.carverHelper.addVariation("Bleak futuristic armor plating block", 1, "tyrian/tyrian");
		blockTyrian.carverHelper.addVariation("Purple futuristic armor plating block", 2, "tyrian/chaotic");
		blockTyrian.carverHelper.addVariation("Faded purple futuristic armor plating block", 3, "tyrian/softplate");
		blockTyrian.carverHelper.addVariation("Rusted futuristic armor plating block", 4, "tyrian/rust");
		blockTyrian.carverHelper.addVariation("Elaborate futuristic armor plating block", 5, "tyrian/elaborate");
		blockTyrian.carverHelper.addVariation("Futuristic armor plating block with many seams", 6, "tyrian/routes");
		blockTyrian.carverHelper.addVariation("Futuristic platform block", 7, "tyrian/platform");
		blockTyrian.carverHelper.addVariation("Futuristic armor plating tiles", 8, "tyrian/platetiles");
		blockTyrian.carverHelper.addVariation("Diagonal futuristic armor plating block", 9, "tyrian/diagonal");
		blockTyrian.carverHelper.addVariation("Futuristic armor plating block with dent", 10, "tyrian/dent");
		blockTyrian.carverHelper.addVariation("Blue futuristic armor plating block", 11, "tyrian/blueplating");
		blockTyrian.carverHelper.addVariation("Black futuristic armor plating block", 12, "tyrian/black");
		blockTyrian.carverHelper.addVariation("Black futuristic armor plating tiles", 13, "tyrian/black2");
		blockTyrian.carverHelper.addVariation("Black futuristic armor plating block with an opening", 14, "tyrian/opening");
		blockTyrian.carverHelper.addVariation("Futuristic armor plating with shining metal bits", 15, "tyrian/plate");
		blockTyrian.carverHelper.register(blockTyrian, "blockTyrian");
		OreDictionary.registerOre("blockTyrian", blockTyrian);
		Carving.chisel.registerOre("blockTyrian","blockTyrian");
		
	    blockDirt = (BlockCarvable) new BlockCarvable(Material.ground).setHardness(0.5F).setStepSound(Block.soundTypeGravel).setBlockName("dirt.default");
	    blockDirt.carverHelper.setBlockName("Dirt");
	    blockDirt.carverHelper.addVariation("Dirt", 0, Blocks.dirt);
	    blockDirt.carverHelper.addVariation("Dirt bricks in disarray", 1, "dirt/bricks");
	    blockDirt.carverHelper.addVariation("Dirt bricks imitating nether brick design", 2, "dirt/netherbricks");
	    blockDirt.carverHelper.addVariation("Dirt bricks", 3, "dirt/bricks3");
	    blockDirt.carverHelper.addVariation("Cobbledirt", 4, "dirt/cobble");
	    blockDirt.carverHelper.addVariation("Reinforced cobbledirt", 5, "dirt/reinforced");
	    blockDirt.carverHelper.addVariation("Reinforced dirt", 6, "dirt/dirt-reinforced");
	    blockDirt.carverHelper.addVariation("Happy dirt", 7, "dirt/happy");
	    blockDirt.carverHelper.addVariation("Large dirt bricks", 8, "dirt/bricks2");
	    blockDirt.carverHelper.addVariation("Large dirt bricks on top of dirt", 9, "dirt/bricks+dirt2");
	    blockDirt.carverHelper.addVariation("Horizontal dirt", 10, "dirt/hor");
	    blockDirt.carverHelper.addVariation("Vertical dirt", 11, "dirt/vert");
	    blockDirt.carverHelper.addVariation("Dirt layers", 12, "dirt/layers");
	    blockDirt.carverHelper.addVariation("Crumbling dirt", 13, "dirt/vertical");
	    blockDirt.carverHelper.register(blockDirt, "blockDirt");
	    blockDirt.setHarvestLevel("shovel", 0);
		OreDictionary.registerOre("blockDirt", blockDirt);
		Carving.chisel.registerOre("blockDirt","blockDirt");
	    
	    blockTemple = (BlockCarvable) new BlockEldritch().setHardness(2.0F).setResistance(10F).setStepSound(soundTempleFootstep);
	    blockTemple.carverHelper.setBlockName("Temple Block");
	    blockTemple.carverHelper.addVariation("Temple cobblestone", 0, "temple/cobble");
	    blockTemple.carverHelper.addVariation("Orante temple block", 1, "temple/ornate");
	    blockTemple.carverHelper.addVariation("Temple plate", 2, "temple/plate");
	    blockTemple.carverHelper.addVariation("Cracked temple plate", 3, "temple/plate-cracked");
	    blockTemple.carverHelper.addVariation("Temple bricks", 4, "temple/bricks");
	    blockTemple.carverHelper.addVariation("Large temple bricks", 5, "temple/bricks-large");
	    blockTemple.carverHelper.addVariation("Weared temple bricks", 6, "temple/bricks-weared");
	    blockTemple.carverHelper.addVariation("Temple bricks in disarray", 7, "temple/bricks-disarray");
	    blockTemple.carverHelper.addVariation("Temple column", 8, "temple/column");
	    blockTemple.carverHelper.addVariation("Temple stand", 9, "temple/stand");
	    blockTemple.carverHelper.addVariation("Temple mosaic stand", 10, "temple/stand-mosaic");
	    blockTemple.carverHelper.addVariation("Temple creeper stand", 11, "temple/stand-creeper");
	    blockTemple.carverHelper.addVariation("Temple tiles", 12, "temple/tiles");
	    blockTemple.carverHelper.addVariation("Small temple tiles", 13, "temple/smalltiles");
	    blockTemple.carverHelper.addVariation("Light temple tiles", 14, "temple/tiles-light");
	    blockTemple.carverHelper.addVariation("Small light temple tiles", 15, "temple/smalltiles-light");
	    blockTemple.carverHelper.register(blockTemple, "blockTemple");
	    
	    blockTempleMossy=(BlockCarvable) new BlockEldritch().setHardness(2.0F).setResistance(10F).setStepSound(soundTempleFootstep);
	    blockTempleMossy.carverHelper.setBlockName("Mossy Temple Block");
	    blockTempleMossy.carverHelper.addVariation("Mossy temple cobblestone", 0, "templemossy/cobble");
	    blockTempleMossy.carverHelper.addVariation("Orante mossy temple block", 1, "templemossy/ornate");
	    blockTempleMossy.carverHelper.addVariation("Mossy temple plate", 2, "templemossy/plate");
	    blockTempleMossy.carverHelper.addVariation("Cracked mossy temple plate", 3, "templemossy/plate-cracked");
	    blockTempleMossy.carverHelper.addVariation("Mossy temple bricks", 4, "templemossy/bricks");
	    blockTempleMossy.carverHelper.addVariation("Large mossy temple bricks", 5, "templemossy/bricks-large");
	    blockTempleMossy.carverHelper.addVariation("Weared mossy temple bricks", 6, "templemossy/bricks-weared");
	    blockTempleMossy.carverHelper.addVariation("Mossy temple bricks in disarray", 7, "templemossy/bricks-disarray");
	    blockTempleMossy.carverHelper.addVariation("Mossy temple column", 8, "templemossy/column");
	    blockTempleMossy.carverHelper.addVariation("Mossy temple stand", 9, "templemossy/stand");
	    blockTempleMossy.carverHelper.addVariation("Mossy temple mosaic stand", 10, "templemossy/stand-mosaic");
	    blockTempleMossy.carverHelper.addVariation("Mossy temple creeper stand", 11, "templemossy/stand-creeper");
	    blockTempleMossy.carverHelper.addVariation("Mossy temple tiles", 12, "templemossy/tiles");
	    blockTempleMossy.carverHelper.addVariation("Small mossy temple tiles", 13, "templemossy/smalltiles");
	    blockTempleMossy.carverHelper.addVariation("Light mossy temple tiles", 14, "templemossy/tiles-light");
	    blockTempleMossy.carverHelper.addVariation("Small light mossy  temple tiles", 15, "templemossy/smalltiles-light");
	    blockTempleMossy.carverHelper.register(blockTempleMossy, "blockTempleMossy");
	    
		blockCloud=(BlockCloud) new BlockCloud().setHardness(0.2F).setLightOpacity(3).setStepSound(Block.soundTypeCloth);
		blockCloud.carverHelper.addVariation("Cloud block", 0, "cloud/cloud");
		blockCloud.carverHelper.register(blockCloud, "blockCloud");
		OreDictionary.registerOre("blockCloud", blockCloud);
		Carving.chisel.registerOre("blockCloud","blockCloud");
	    
	    blockFactory=(BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(soundMetalFootstep);
	    blockFactory.carverHelper.setBlockName("Factory Block");
	    blockFactory.carverHelper.addVariation("Rusty metal plate with dot pattern", 0, "factory/dots");
	    blockFactory.carverHelper.addVariation("Rusty metal plate", 1, "factory/rust2");
	    blockFactory.carverHelper.addVariation("Very rusty metal plate", 2, "factory/rust");
	    blockFactory.carverHelper.addVariation("A metal plate with almost no rust on it", 3, "factory/platex");
	    blockFactory.carverHelper.addVariation("Wireframe", 4, "factory/wireframewhite");
	    blockFactory.carverHelper.addVariation("Wireframe in a shade of purple", 5, "factory/wireframe");
	    blockFactory.carverHelper.addVariation("Yellow-black hazard block", 6, "factory/hazard");
	    blockFactory.carverHelper.addVariation("Orange-white hazard block", 7, "factory/hazardorange");
	    blockFactory.carverHelper.addVariation("Fancy circuit block", 8, "factory/circuit");
	    blockFactory.carverHelper.addVariation("Fancy metal box", 9, "factory/metalbox");
	    blockFactory.carverHelper.addVariation("Fancy circuit block with gold plating", 10, "factory/goldplate");
	    blockFactory.carverHelper.addVariation("Rusty purple block with gold plating", 11, "factory/goldplating");
	    blockFactory.carverHelper.addVariation("Shiny metal construction", 12, "factory/grinder");
	    blockFactory.carverHelper.addVariation("Weared metal wall with openings for ventilation", 13, "factory/plating");
	    blockFactory.carverHelper.addVariation("Rusty metal plates", 14, "factory/rustplates");
	    blockFactory.carverHelper.addVariation("Weared metal column", 15, "factory/column");
	    blockFactory.carverHelper.register(blockFactory, "blockFactory");
	    
	    // 1.7! Let's go! Let's go-go!
	    /*
	    String[] sGNames = new String[]{
	    		"White", "Orange", "Magenta", "Light Blue",
	    		"Yellow", "Lime", "Pink", "Gray",
	    		"Light Gray", "Cyan", "Purple", "Blue",
	    		"Brown", "Green", "Red", "Black"
	    };
	    
	    for(int i = 0; i < 16; i++) {
	    	String odName = "stainedGlass" + sGNames[i].replaceAll(" ", "");
	    	String texName = "glassdyed/" + sGNames[i].toLowerCase().replaceAll(" ", "") + "-";
	    	int glassPrefix = 0;
	    	int glassId = i;
	    	blockStainedGlass[glassId] = (BlockCarvableGlass) new BlockCarvableGlass().setHardness(0.3F).setStepSound(Block.soundTypeGlass);
	    	blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " Stained Glass", glassPrefix + 0, Blocks.stained_glass, i);
	    	blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " Bubble Stained Glass", glassPrefix + 1, texName + "bubble");
	    	blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " Stained Glass Panel", glassPrefix + 2, texName + "panel");
	    	blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " Fancy Stained Glass Panel", glassPrefix + 3, texName + "panel-fancy");
	    	blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " Small Stained Glass", glassPrefix + 4, texName + "quad");
	    	blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " Small Fancy Stained Glass", glassPrefix + 5, texName + "quad-fancy");
	    	blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " Borderless Stained Glass", glassPrefix + 6, texName + "transparent");
		   	blockStainedGlass[glassId].carverHelper.register(blockStainedGlass[glassId], "stainedGlass."+glassId, glassPrefix, glassPrefix+7);
	    }
	    */
	    Blocks.stone.setHarvestLevel("chisel", 0, 0);
	    
		proxy.preInit();
 	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		packet = new PacketHandler("chisel", new Packets(), new Packets());

		
	    Block concreteRecipeBlock = Block.getBlockFromName(config.get("tweaks", "concrete recipe block","gravel","Unlocalized name of the block that, when burned, will produce concrete (examples: lightgem, stone)").getString());
	    if(concreteRecipeBlock == null) concreteRecipeBlock = Blocks.gravel;
	    
        FurnaceRecipes.smelting().func_151393_a(concreteRecipeBlock, new ItemStack(blockConcrete), 0.1F);

		CraftingManager.getInstance().addRecipe(new ItemStack(blockSandstoneScribbles, 1), new Object[] { "X", 'X', new ItemStack(blockSandstone, 1, 8), });
		for(int meta=0;meta<16;meta++){
			CraftingManager.getInstance().addRecipe(new ItemStack(blockMarbleSlab, 6, 0), new Object[] { "***", '*', new ItemStack(blockMarble, 1, meta)});
			CraftingManager.getInstance().addRecipe(new ItemStack(blockLimestoneSlab, 6, 0), new Object[] {"***", '*', new ItemStack(blockLimestone, 1, meta)});
			CraftingManager.getInstance().addRecipe(new ItemStack(blockMarblePillarSlab, 6, 0), new Object[] {"***", '*', new ItemStack(blockMarblePillar, 1, meta)});

			CraftingManager.getInstance().addRecipe(new ItemStack(blockMarblePillar, 6), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(blockMarble, 1, meta), });
			CraftingManager.getInstance().addRecipe(new ItemStack(blockMarble, 4), new Object[] { "XX", "XX", 'X', new ItemStack(blockMarblePillar, 1, meta), });

			CraftingManager.getInstance().addRecipe(new ItemStack(blockIcePillar, 6), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(blockIce, 1, meta), });
			CraftingManager.getInstance().addRecipe(new ItemStack(blockIce, 4), new Object[] { "XX", "XX", 'X', new ItemStack(blockIcePillar, 1, meta), });
			
			CraftingManager.getInstance().addRecipe(new ItemStack(blockSandstone, 1), new Object[] { "X", 'X', new ItemStack(blockSandstoneScribbles, 1, meta), });

			CraftingManager.getInstance().addRecipe(new ItemStack(blockCarpet, 8, meta), new Object[] { "YYY", "YXY", "YYY", 'X', new ItemStack(Items.string, 1), 'Y', new ItemStack(Blocks.wool, 1, meta), });
			CraftingManager.getInstance().addRecipe(new ItemStack(blockCarpetFloor, 3, meta), new Object[] { "XX", 'X', new ItemStack(blockCarpet, 1, meta), });
		}
		
		CraftingManager.getInstance().addRecipe(new ItemStack(blockHolystone, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.feather, 1)});
		CraftingManager.getInstance().addRecipe(new ItemStack(blockLavastone, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.lava_bucket, 1)});
		CraftingManager.getInstance().addRecipe(new ItemStack(blockFft, 8, 0),       new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.gold_nugget, 1)});
		CraftingManager.getInstance().addRecipe(new ItemStack(blockTyrian, 8, 0),    new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1)});
		CraftingManager.getInstance().addRecipe(new ItemStack(blockTemple, 8, 0),    new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.dye, 1, 4)});
		CraftingManager.getInstance().addRecipe(new ItemStack(blockFactory,32,0),    new Object[] { "*X*", "X X", "*X*", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1)});
		
		
		if(config.get("general", "Alternative recipe", false, "Use alternative crafting recipe for the chisel").getBoolean(false)){
			CraftingManager.getInstance().addRecipe(new ItemStack(chisel, 1), new Object[] { " YY", " YY", "X  ", 'X', Items.stick, 'Y', Items.iron_ingot });
		} else{
			CraftingManager.getInstance().addRecipe(new ItemStack(chisel, 1), new Object[] { " Y", "X ", 'X', Items.stick, 'Y', Items.iron_ingot });
		}
		
		CraftingManager.getInstance().addRecipe(new ItemStack(itemBallOMoss, 1), new Object[] { "XYX","YXY","XYX", 'X', Blocks.vine, 'Y', Items.stick });
		CraftingManager.getInstance().addRecipe(new ItemStack(itemCloudInABottle, 1), new Object[] { "X X","XYX"," X ", 'X', Blocks.glass, 'Y', Items.quartz });


		NetworkRegistry.INSTANCE.registerGuiHandler(this, new IGuiHandler(){
			@Override
			public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
				return new ContainerChisel(player.inventory, new InventoryChiselSelection(null));
			}

			@Override
			public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
				return new GuiChisel(player.inventory, new InventoryChiselSelection(null));
			}
		});


		
        GameRegistry.registerWorldGenerator(new MarbleWorldGenerator(blockMarble, 32, config.get("general", "Worldgen marble amount", 8, "Amount of marble to generate in the world; use 0 for none").getInt(8)), 1000);
        GameRegistry.registerWorldGenerator(new MarbleWorldGenerator(blockLimestone, 32, config.get("general", "Worldgen limestone amount", 4, "Amount of limestone to generate in the world; use 0 for none").getInt(4)), 1000);

		proxy.init();
		
        config.save();
        
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		new ChiselModCompatibility().postInit(event);
	}
}
