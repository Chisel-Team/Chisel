package info.jbcs.minecraft.chisel;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import info.jbcs.minecraft.chisel.carving.Carving;
import info.jbcs.minecraft.chisel.client.gui.GuiChisel;
import info.jbcs.minecraft.chisel.entity.EntityBallOMoss;
import info.jbcs.minecraft.chisel.entity.EntityCloudInABottle;
import info.jbcs.minecraft.chisel.entity.EntitySmashingRock;
import info.jbcs.minecraft.chisel.inventory.ContainerChisel;
import info.jbcs.minecraft.chisel.inventory.InventoryChiselSelection;
import info.jbcs.minecraft.chisel.item.ItemBallOMoss;
import info.jbcs.minecraft.chisel.item.ItemChisel;
import info.jbcs.minecraft.chisel.item.ItemCloudInABottle;
import info.jbcs.minecraft.chisel.item.ItemSmashingRock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.io.File;


@Mod(modid = Chisel.MOD_ID, name = Chisel.MOD_NAME, version = "@MOD_VERSION@", guiFactory = "info.jbcs.minecraft.chisel.client.gui.GuiFactory"/*, dependencies = "after:ForgeMicroblock;"*/)
public class Chisel
{
    public static final String MOD_ID = "chisel";
    public static final String MOD_NAME = "Chisel";

    public static ItemChisel chisel;
    //	public static ItemChisel needle;
    public static ItemCloudInABottle itemCloudInABottle;
    public static ItemBallOMoss itemBallOMoss;
    public static ItemSmashingRock smashingRock;

    public static CreativeTabs tabChisel;

    public static boolean multipartLoaded = false;

    public static final StepSoundEx soundHolystoneFootstep = new StepSoundEx("chisel:holystone", "chisel:holystone", "chisel:holystone", 1.0f);
    public static final StepSoundEx soundTempleFootstep = new StepSoundEx("dig.stone", "chisel:temple-footstep", "dig.stone", 1.0f);
    public static final StepSoundEx soundMetalFootstep = new StepSoundEx("chisel:metal", "chisel:metal", "chisel:metal", 1.0f);

    public static int RenderEldritchId;
    public static int RenderCTMId;
    public static int RenderCarpetId;

    @Instance(MOD_ID)
    public static Chisel instance;

    @SidedProxy(clientSide = "info.jbcs.minecraft.chisel.ClientProxy", serverSide = "info.jbcs.minecraft.chisel.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void missingMapping(FMLMissingMappingsEvent event)
    {
        for(MissingMapping m : event.get())
        {
            // This bug was introduced along with Chisel 1.5.2, and was fixed in 1.5.3.
            // Ice Stairs were called null.0-7 instead of other names, and Marble/Limestone stairs did not exist.
            // This fixes the bug.
            if(m.name.startsWith("null.") && m.name.length() == 6 && m.type == Type.BLOCK)
            {
                m.warn();//(Action.WARN);
            }
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        File configFile = event.getSuggestedConfigurationFile();
        Configurations.configExists = configFile.exists();
        Configurations.config = new Configuration(configFile);
        Configurations.config.load();
        Configurations.refreshConfig();

        tabChisel = new CreativeTabs("tabChisel")
        {
            @Override
            public Item getTabIconItem()
            {
                return chisel;
            }
        };

        chisel = (ItemChisel) new ItemChisel(Carving.chisel).setTextureName("chisel:chisel").setCreativeTab(CreativeTabs.tabTools);
        GameRegistry.registerItem(chisel, "chisel");

        if(Configurations.featureEnabled("cloud"))
        {
            itemCloudInABottle = (ItemCloudInABottle) new ItemCloudInABottle().setTextureName("Chisel:cloudinabottle").setCreativeTab(CreativeTabs.tabTools);
            EntityRegistry.registerModEntity(EntityCloudInABottle.class, "CloudInABottle", 1, this, 40, 1, true);
            GameRegistry.registerItem(itemCloudInABottle, "chisel.cloudinabottle");
        }

        if(Configurations.featureEnabled("ballOfMoss"))
        {
            itemBallOMoss = (ItemBallOMoss) new ItemBallOMoss().setTextureName("Chisel:ballomoss").setCreativeTab(CreativeTabs.tabTools);
            EntityRegistry.registerModEntity(EntityBallOMoss.class, "BallOMoss", 2, this, 40, 1, true);
            GameRegistry.registerItem(itemBallOMoss, "ballomoss");
        }

        if(Configurations.featureEnabled("smashingRock"))
        {
            smashingRock = (ItemSmashingRock) new ItemSmashingRock().setTextureName("Chisel:smashingrock").setCreativeTab(CreativeTabs.tabTools);
            EntityRegistry.registerModEntity(EntitySmashingRock.class, "SmashingRock", 2, this, 40, 1, true);
            GameRegistry.registerItem(smashingRock, "smashingrock");
        }
        ChiselBlocks.load();
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Crafting.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new IGuiHandler()
        {
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
            {
                return new ContainerChisel(player.inventory, new InventoryChiselSelection(null));
            }

            @Override
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
            {
                return new GuiChisel(player.inventory, new InventoryChiselSelection(null));
            }
        });


        GameRegistry.registerWorldGenerator(new ChiselWorldGenerator(ChiselBlocks.blockMarble, 32, Configurations.marbleAmount), 1000);
        GameRegistry.registerWorldGenerator(new ChiselWorldGenerator(ChiselBlocks.blockLimestone, 32, Configurations.limestoneAmount), 1000);

        proxy.init();
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(instance);

        FMLInterModComms.sendMessage("Waila", "register", "info.jbcs.minecraft.chisel.Waila.register");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        new ChiselModCompatibility().postInit(event);

        if(Loader.isModLoaded("ProjRed|Exploration") && GameRegistry.findBlock("ProjRed|Exploration", "projectred.exploration.stone") != null)
        {
            Carving.chisel.addVariation("marble", GameRegistry.findBlock("ProjRed|Exploration", "projectred.exploration.stone"), 0, 99);
            GameRegistry.findBlock("ProjRed|Exploration", "projectred.exploration.stone").setHarvestLevel("chisel", 0, 0);
        }
    }


    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if(event.modID.equals("chisel"))
        {
            Configurations.refreshConfig();
        }
    }
}
