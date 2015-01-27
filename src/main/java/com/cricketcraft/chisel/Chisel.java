package com.cricketcraft.chisel;

import com.cricketcraft.chisel.block.BlockCarvable;
import com.cricketcraft.chisel.client.player.PlayerSpecials;
import com.cricketcraft.chisel.compat.Compatibility;
import com.cricketcraft.chisel.config.Configurations;
import com.cricketcraft.chisel.init.ChiselBlocks;
import com.cricketcraft.chisel.init.ChiselTabs;
import com.cricketcraft.chisel.item.chisel.ChiselController;
import com.cricketcraft.chisel.network.ChiselGuiHandler;
import com.cricketcraft.chisel.network.PacketHandler;
import com.cricketcraft.chisel.proxy.CommonProxy;
import com.cricketcraft.chisel.utils.General;
import com.cricketcraft.chisel.world.GeneratorChisel;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Chisel.MOD_ID, name = Chisel.MOD_NAME, version = Chisel.VERSION, guiFactory = "com.cricketcraft.chisel.client.gui.GuiFactory", dependencies = "after:ForgeMultipart;after:Thaumcraft;after:appliedenergistics2;after:Railcraft;after:AWWayofTime;after:TwilightForest")
public class Chisel {

	public static final String MOD_ID = "chisel";
	public static final BlockCarvable.SoundType soundTempleFootstep = new BlockCarvable.SoundType("dig.stone", MOD_ID + ":step.templeblock", 1.0f, 1.0f);
	public static final String MOD_NAME = "Chisel 2";
	public static final String VERSION = "@VERSION@";
	public static final BlockCarvable.SoundType soundHolystoneFootstep = new BlockCarvable.SoundType("holystone", 1.0f, 1.0f);
	public static final BlockCarvable.SoundType soundMetalFootstep = new BlockCarvable.SoundType("metal", 1.0f, 1.0f);
	public static boolean multipartLoaded = false;
	public static int renderEldritchId;
	public static int renderCTMId;
	public static int renderCTMNoLightId;
	public static int renderCarpetId;
	public static int renderAutoChiselId;
	public static int renderGlowId;
	public static int renderLayeredId;
	public static int roadLineId;

	public static final Logger logger = LogManager.getLogger(MOD_NAME);

	@Instance(MOD_ID)
	public static Chisel instance;

	@SidedProxy(clientSide = "com.cricketcraft.chisel.proxy.ClientProxy", serverSide = "com.cricketcraft.chisel.proxy.CommonProxy")
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

		ChiselTabs.preInit();
		Features.preInit();
		PacketHandler.init();
		ChiselController.INSTANCE.preInit();
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

		proxy.init();
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(instance);

		FMLCommonHandler.instance().bus().register(new PlayerSpecials());


		FMLInterModComms.sendMessage("Waila", "register", "com.cricketcraft.chisel.compat.WailaCompat.register");
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
		ChiselTabs.postInit();
		Compatibility.init(event);
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals("chisel")) {
			Configurations.refreshConfig();
		}
	}
}
