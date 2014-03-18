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
import info.jbcs.minecraft.chisel.block.BlockCarvablePowered;
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
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.Action;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;


@Mod(modid = "chisel", name = "Chisel", version = "1.5.3", dependencies = "required-after:asielib")
public class Chisel {
	public static ItemChisel				chisel;
//	public static ItemChisel				needle;
	public static Item						itemIceshard;
	public static ItemCloudInABottle		itemCloudInABottle;
	public static ItemBallOMoss				itemBallOMoss;
	
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
	public static boolean chiselStoneToCobbleBricks;
	public static boolean enableChiseling;
	
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
    
    public static boolean featureEnabled(String feature) {
    	return Chisel.config.get("features", feature, true).getBoolean(true);
    }
	
	@EventHandler
	public void missingMapping(FMLMissingMappingsEvent event) {
		for(MissingMapping m: event.get()) {
			// This bug was introduced along with Chisel 1.5.2, and was fixed in 1.5.3.
			// Ice Stairs were called null.0-7 instead of other names, and Marble/Limestone stairs did not exist.
			// This fixes the bug.
			if(m.name.startsWith("null.") && m.name.length() == 6 && m.type == Type.BLOCK) {
				m.setAction(Action.WARN);
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
		
		if(featureEnabled("cloud")) {
			itemCloudInABottle=(ItemCloudInABottle) new ItemCloudInABottle().setUnlocalizedName("Chisel:cloudinabottle").setTextureName("Chisel:cloudinabottle").setCreativeTab(CreativeTabs.tabTools);
			LanguageRegistry.addName(itemCloudInABottle, "Cloud in a bottle");
			EntityRegistry.registerModEntity(EntityCloudInABottle.class, "CloudInABottle", 1, this, 40, 1, true);
			GameRegistry.registerItem(itemCloudInABottle, "chisel.cloudinabottle");
		}
		
		if(featureEnabled("ballOfMoss")) {
			itemBallOMoss=(ItemBallOMoss) new ItemBallOMoss().setUnlocalizedName("Chisel:ballomoss").setTextureName("Chisel:ballomoss").setCreativeTab(CreativeTabs.tabTools);
			LanguageRegistry.addName(itemBallOMoss, "Ball O' Moss");
			EntityRegistry.registerModEntity(EntityBallOMoss.class, "BallOMoss", 2, this, 40, 1, true);
			GameRegistry.registerItem(itemBallOMoss, "chisel.ballomoss");
		}
		
		concreteVelocity=config.get("general", "concreteVelocity",0.45,"Traversing concrete roads, players will acceleration to this velocity. For reference, normal running speed is about 0.28. Set to 0 to disable acceleration.").getDouble(0.45);
		particlesTickrate=config.get("client", "particleTickrate",1,"Particle tick rate. Greater value = less particles.").getInt(1);
		oldPillars=config.get("client", "pillarOldGraphics",false,"Use old pillar textures").getBoolean(false);
		disableCTM=!config.get("client", "connectedTextures",true,"Enable connected textures").getBoolean(true);
		blockDescriptions=config.get("client", "tooltipsUseBlockDescriptions",true,"Make variations of blocks have the same name, and use the description in tooltip to distinguish them.").getBoolean(true);
		chiselStoneToCobbleBricks=config.get("general", "chiselStoneToCobbleBricks",true,"Chisel stone to cobblestone and bricks by left-click.").getBoolean(true);
		enableChiseling=config.get("general", "enableChiseling",true,"Change blocks to another block using the Chisel and left-click.").getBoolean(true);
		
		/* if(dropIceShards){
			itemIceshard=new Item().setCreativeTab(CreativeTabs.tabMaterials).setUnlocalizedName("Chisel:iceshard").setTextureName("Chisel:iceshard");
//			itemIceshard=new Item(2582).setCreativeTab(CreativeTabs.tabMaterials).setUnlocalizedName("Chisel:iceshard").func_111206_d("Chisel:iceshard");
			LanguageRegistry.addName(itemIceshard, "Ice shard");
			GameRegistry.registerItem(itemIceshard, "chisel.iceshard");
			
			CraftingManager.getInstance().addRecipe(new ItemStack(Blocks.ice, 1), new Object[] { "**", "**", '*', itemIceshard, });
		} */
		
		if(config.get("client", "customCreativeTab", true, "Add a new tab in creative mode and put all blocks that work with chisel there.").getBoolean(true)){
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

		ChiselBlocks.load();
	    
		proxy.preInit();
 	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		packet = new PacketHandler("chisel", new Packets(), new Packets());

		
	    Block concreteRecipeBlock = Block.getBlockFromName(config.get("tweaks", "concrete recipe block","gravel","Unlocalized name of the block that, when burned, will produce concrete (examples: lightgem, stone)").getString());
	    if(concreteRecipeBlock == null) concreteRecipeBlock = Blocks.gravel;
	    
        FurnaceRecipes.smelting().func_151393_a(concreteRecipeBlock, new ItemStack(ChiselBlocks.blockConcrete), 0.1F);

		CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockSandstoneScribbles, 1), new Object[] { "X", 'X', new ItemStack(ChiselBlocks.blockSandstone, 1, 8), });
		for(int meta=0;meta<16;meta++){
			CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockMarbleSlab, 6, 0), new Object[] { "***", '*', new ItemStack(ChiselBlocks.blockMarble, 1, meta)});
			CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockLimestoneSlab, 6, 0), new Object[] {"***", '*', new ItemStack(ChiselBlocks.blockLimestone, 1, meta)});
			CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockMarblePillarSlab, 6, 0), new Object[] {"***", '*', new ItemStack(ChiselBlocks.blockMarblePillar, 1, meta)});

			CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockMarblePillar, 6), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.blockMarble, 1, meta), });
			CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockMarble, 4), new Object[] { "XX", "XX", 'X', new ItemStack(ChiselBlocks.blockMarblePillar, 1, meta), });

			CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockIcePillar, 6), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.blockIce, 1, meta), });
			CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockIce, 4), new Object[] { "XX", "XX", 'X', new ItemStack(ChiselBlocks.blockIcePillar, 1, meta), });
			
			CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockSandstone, 1), new Object[] { "X", 'X', new ItemStack(ChiselBlocks.blockSandstoneScribbles, 1, meta), });

			CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockCarpet, 8, meta), new Object[] { "YYY", "YXY", "YYY", 'X', new ItemStack(Items.string, 1), 'Y', new ItemStack(Blocks.wool, 1, meta), });
			CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockCarpetFloor, 3, meta), new Object[] { "XX", 'X', new ItemStack(ChiselBlocks.blockCarpet, 1, meta), });
		}
		
		CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockHolystone, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.feather, 1)});
		CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockLavastone, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.lava_bucket, 1)});
		CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockFft, 8, 0),       new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.gold_nugget, 1)});
		CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockTyrian, 8, 0),    new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1)});
		CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockTemple, 8, 0),    new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.dye, 1, 4)});
		CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockFactory,32,0),    new Object[] { "*X*", "X X", "*X*", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1)});
		
		
		if(config.get("general", "chiselAlternateRecipe", false, "Use alternative crafting recipe for the chisel").getBoolean(false)){
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


		
        GameRegistry.registerWorldGenerator(new MarbleWorldGenerator(ChiselBlocks.blockMarble, 32, config.get("worldgen", "marbleAmount", 8, "Amount of marble to generate in the world; use 0 for none").getInt(8)), 1000);
        GameRegistry.registerWorldGenerator(new MarbleWorldGenerator(ChiselBlocks.blockLimestone, 32, config.get("worldgen", "limestoneAmount", 4, "Amount of limestone to generate in the world; use 0 for none").getInt(4)), 1000);

		proxy.init();
		
        config.save();
        
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		new ChiselModCompatibility().postInit(event);
	}
}
