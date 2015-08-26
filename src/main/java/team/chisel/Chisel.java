package team.chisel;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import team.chisel.api.ChiselAPIProps;
import team.chisel.api.ChiselTabs;
import team.chisel.api.Statistics;
import team.chisel.api.carving.CarvableHelper;
import team.chisel.block.BlockCarvable;
import team.chisel.carving.Carving;
import team.chisel.compat.Compatibility;
import team.chisel.compat.IMCHandler;
import team.chisel.compat.fmp.FMPCompat;
import team.chisel.config.Configurations;
import team.chisel.entity.EntityChiselSnowman;
import team.chisel.init.ChiselBlocks;
import team.chisel.init.TabsInit;
import team.chisel.item.ItemCarvable;
import team.chisel.item.chisel.ChiselController;
import team.chisel.network.ChiselGuiHandler;
import team.chisel.network.PacketHandler;
import team.chisel.proxy.CommonProxy;
import team.chisel.utils.General;
import team.chisel.world.GeneratorChisel;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;

@Mod(modid = Chisel.MOD_ID, name = Chisel.MOD_NAME, version = Chisel.VERSION, guiFactory = "team.chisel.client.gui.GuiFactory", dependencies = "after:EE3;after:ForgeMultipart;after:Thaumcraft;after:appliedenergistics2;after:Railcraft;after:AWWayofTime;after:TwilightForest")
public class Chisel {

	public static final String MOD_ID = "chisel";
	public static final BlockCarvable.SoundType soundTempleFootstep = new BlockCarvable.SoundType("dig.stone", MOD_ID + ":step.templeblock", 1.0f, 1.0f);
	public static final String MOD_NAME = "Chisel";
	public static final String VERSION = "@VERSION@";
	public static final BlockCarvable.SoundType soundHolystoneFootstep = new BlockCarvable.SoundType("holystone", 1.0f, 1.0f);
	public static final BlockCarvable.SoundType soundMetalFootstep = new BlockCarvable.SoundType("metal", 1.0f, 1.0f);
	public static boolean multipartLoaded = false;
	public static int renderEldritchId;
	public static int renderAutoChiselId;
	public static int renderGlowId;
	public static int renderLayeredId;
	public static int roadLineId;

	public static final Logger logger = LogManager.getLogger(MOD_NAME);

	@Instance(MOD_ID)
	public static Chisel instance;

	public Chisel() {
		ChiselAPIProps.MOD_ID = MOD_ID;
		CarvableHelper.itemCarvableClass = ItemCarvable.class;
		Carving.construct();
	}

	@SidedProxy(clientSide = "team.chisel.proxy.ClientProxy", serverSide = "team.chisel.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void missingMapping(FMLMissingMappingsEvent event) {
		BlockNameConversion.init();

		for (MissingMapping m : event.get()) {
			// This bug was introduced along with Chisel 1.5.2, and was fixed in
			// 1.5.3.
			// Ice Stairs were called null.0-7 instead of other names, and
			// Marble/Limestone stairs did not exist.
			// This fixes the bug.
			if (m.name.startsWith("null.") && m.name.length() == 6 && m.type == Type.BLOCK) {
				m.warn();// (Action.WARN);
			}

			// Fix mapping of snakestoneSand, snakestoneStone, limestoneStairs,
			// marbleStairs when loading an old (1.5.4) save
			else if (m.type == Type.BLOCK) {
				final Block block = BlockNameConversion.findBlock(m.name);

				if (block != null) {
					m.remap(block);
					FMLLog.getLogger().info("Remapping block " + m.name + " to " + General.getName(block));
				} else
					FMLLog.getLogger().warn("Block " + m.name + " could not get remapped.");
			} else if (m.type == Type.ITEM) {
				final Item item = BlockNameConversion.findItem(m.name);

				if (item != null) {
					m.remap(item);
					FMLLog.getLogger().info("Remapping item " + m.name + " to " + General.getName(item));

				} else
					FMLLog.getLogger().warn("Item " + m.name + " could not get remapped.");
			}
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		File configFile = event.getSuggestedConfigurationFile();
		Configurations.configExists = configFile.exists();
		Configurations.config = new Configuration(configFile);
		Configurations.config.load();
		Configurations.refreshConfig();

		TabsInit.preInit();
		Features.preInit();
		Statistics.init();
		PacketHandler.init();
		ChiselController.INSTANCE.preInit();
		if (Loader.isModLoaded("ForgeMultipart")) {
			new FMPCompat().init();
		}
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		Features.init();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ChiselGuiHandler());

		addWorldgen(Features.MARBLE, ChiselBlocks.marble, Configurations.marbleAmount);
		addWorldgen(Features.LIMESTONE, ChiselBlocks.limestone, Configurations.limestoneAmount);
		addWorldgen(Features.ANDESITE, ChiselBlocks.andesite, Configurations.andesiteAmount, 40, 100, 0.5);
		addWorldgen(Features.GRANITE, ChiselBlocks.granite, Configurations.graniteAmount, 40, 100, 0.5);
		addWorldgen(Features.DIORITE, ChiselBlocks.diorite, Configurations.dioriteAmount, 40, 100, 0.5);
		GameRegistry.registerWorldGenerator(GeneratorChisel.INSTANCE, 1000);

		EntityRegistry.registerModEntity(EntityChiselSnowman.class, "snowman", 0, this, 80, 1, true);

		proxy.init();
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(instance);

		FMLInterModComms.sendMessage("Waila", "register", "team.chisel.compat.WailaCompat.register");
	}

	private void addWorldgen(Features feature, Block block, double... data) {
		if (feature.enabled()) {
			if (data.length == 1) {
				GeneratorChisel.INSTANCE.addFeature(block, 32, (int) data[0]);
			} else if (data.length > 1 && data.length < 4) {
				GeneratorChisel.INSTANCE.addFeature(block, 32, (int) data[0], (int) data[1], (int) data[2]);
			} else if (data.length == 4) {
				GeneratorChisel.INSTANCE.addFeature(block, 32, (int) data[0], (int) data[1], (int) data[2], data[3]);
			}
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		TabsInit.postInit();
		Compatibility.init(event);
	}

	@EventHandler
	public void onIMC(IMCEvent event) {
		for (IMCMessage msg : event.getMessages()) {
			IMCHandler.INSTANCE.handleMessage(msg);
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals("chisel")) {
			Configurations.refreshConfig();
		}
	}
}
