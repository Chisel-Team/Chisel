package com.cricketcraft.chisel;

import com.cricketcraft.chisel.block.BlockCarvable;
import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityPresent;
import com.cricketcraft.chisel.client.gui.GuiAutoChisel;
import com.cricketcraft.chisel.client.gui.GuiChisel;
import com.cricketcraft.chisel.client.gui.GuiPresent;
import com.cricketcraft.chisel.compat.Compatibility;
import com.cricketcraft.chisel.compat.FMPIntegration;
import com.cricketcraft.chisel.compat.ModIntegration;
import com.cricketcraft.chisel.init.ModBlocks;
import com.cricketcraft.chisel.init.ModItems;
import com.cricketcraft.chisel.init.ModTabs;
import com.cricketcraft.chisel.inventory.ContainerAutoChisel;
import com.cricketcraft.chisel.inventory.ContainerChisel;
import com.cricketcraft.chisel.inventory.ContainerPresent;
import com.cricketcraft.chisel.inventory.InventoryChiselSelection;
import com.cricketcraft.chisel.proxy.CommonProxy;
import com.cricketcraft.chisel.utils.General;
import com.cricketcraft.chisel.world.GeneratorLimestone;
import com.cricketcraft.chisel.world.GeneratorMarble;
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
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Mod(modid = Chisel.MOD_ID, name = Chisel.MOD_NAME, version = Chisel.VERSION, guiFactory = "com.cricketcraft.chisel.client.gui.GuiFactory",  dependencies = "after:ForgeMultipart;after:Thaumcraft")
public class Chisel
{
    public static final List<String> modsSupported = new ArrayList<String>();
    public static final String MOD_ID = "chisel";
    public static final BlockCarvable.SoundType soundTempleFootstep = new BlockCarvable.SoundType("dig.stone", MOD_ID + ":step.templeblock", 1.0f, 1.0f);
    public static final String MOD_NAME = "Chisel 2";
    public static final String VERSION = "2.2.1";
    public static final BlockCarvable.SoundType soundHolystoneFootstep = new BlockCarvable.SoundType("holystone", 1.0f, 1.0f);
    public static final BlockCarvable.SoundType soundMetalFootstep = new BlockCarvable.SoundType("metal", 1.0f, 1.0f);
    public static boolean multipartLoaded = false;
    public static int RenderEldritchId;
    public static int RenderCTMId;
    public static int RenderCarpetId;

    @Instance(MOD_ID)
    public static Chisel instance;

    @SidedProxy(clientSide = "com.cricketcraft.chisel.proxy.ClientProxy", serverSide = "com.cricketcraft.chisel.proxy.CommonProxy")
    public static CommonProxy proxy;

    @SideOnly(Side.CLIENT)
    private static void initModIntegration() {
        if(Configurations.enableFMP) {
            ModIntegration.addMod(FMPIntegration.class);
        }

        ModIntegration.init();
    }

    @EventHandler
    public void missingMapping(FMLMissingMappingsEvent event) {
        BlockNameConversion.init();

        for (MissingMapping m : event.get()) {
            // This bug was introduced along with Chisel 1.5.2, and was fixed in 1.5.3.
            // Ice Stairs were called null.0-7 instead of other names, and Marble/Limestone stairs did not exist.
            // This fixes the bug.
            if (m.name.startsWith("null.") && m.name.length() == 6 && m.type == Type.BLOCK) {
                m.warn();//(Action.WARN);
            }

            // Fix mapping of snakestoneSand, snakestoneStone, limestoneStairs, marbleStairs when loading an old (1.5.4) save
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
        modsSupported.add("Thaumcraft");
        modsSupported.add("Twilight Forest");
        modsSupported.add("appliedenergistics2");
        modsSupported.add("Railcraft");

        File configFile = event.getSuggestedConfigurationFile();
        Configurations.configExists = configFile.exists();
        Configurations.config = new Configuration(configFile);
        Configurations.config.load();
        Configurations.refreshConfig();

        ModTabs.load();
        ModBlocks.load();
        ModItems.load();
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Crafting.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new IGuiHandler() {
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                switch (ID) {
                    case 0:
                        return new ContainerChisel(player.inventory, new InventoryChiselSelection(null));
                    case 1:
                        TileEntity tileentity = world.getTileEntity(x, y, z);
                        if (tileentity instanceof TileEntityAutoChisel)
                            return new ContainerAutoChisel(player.inventory, (TileEntityAutoChisel) tileentity);
                    case 2:
                        TileEntity tileEntity = world.getTileEntity(x, y, z);
                        if (tileEntity instanceof TileEntityPresent)
                            return new ContainerPresent(player.inventory, (TileEntityPresent) tileEntity);
                    default:
                        return null;
                }
            }

            @Override
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                switch (ID) {
                    case 0:
                        return new GuiChisel(player.inventory, new InventoryChiselSelection(null));
                    case 1:
                        TileEntity tileentity = world.getTileEntity(x, y, z);
                        if (tileentity instanceof TileEntityAutoChisel)
                            return new GuiAutoChisel(player.inventory, (TileEntityAutoChisel) tileentity);
                    case 2:
                        TileEntity tileEntity = world.getTileEntity(x, y, z);
                        if (tileEntity instanceof TileEntityPresent)
                            return new GuiPresent(player.inventory, (TileEntityPresent) tileEntity);
                    default:
                        return null;
                }
            }
        });


        GameRegistry.registerWorldGenerator(new GeneratorMarble(ModBlocks.marble, 32, Configurations.marbleAmount), 1000);
        GameRegistry.registerWorldGenerator(new GeneratorLimestone(ModBlocks.limestone, 32, Configurations.limestoneAmount), 1000);

        if (event.getSide() == Side.CLIENT) {
            initModIntegration();
        }

        proxy.init();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ChiselLeftClick());
        FMLCommonHandler.instance().bus().register(instance);

        FMLInterModComms.sendMessage("Waila", "register", "com.cricketcraft.chisel.Waila.register");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	ModBlocks.postLoad();
        ModIntegration.postInit();
        Compatibility.init(event);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals("chisel")) {
            Configurations.refreshConfig();
        }
    }

}
