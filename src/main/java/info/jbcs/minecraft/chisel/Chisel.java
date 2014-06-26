package info.jbcs.minecraft.chisel;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.Action;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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


@Mod(modid = "chisel", name = "Chisel", version = "1.5.4g"/*, dependencies = "after:ForgeMicroblock;"*/)
public class Chisel
{
    public static ItemChisel chisel;
    //	public static ItemChisel needle;
    public static ItemCloudInABottle itemCloudInABottle;
    public static ItemBallOMoss itemBallOMoss;
    public static ItemSmashingRock smashingRock;

    public static CreativeTabs tabChisel;

    public static boolean multipartLoaded = false;

    public static boolean configExists;
    public static boolean oldPillars;
    public static boolean disableCTM;
    public static double concreteVelocity;
    public static int particlesTickrate;
    public static boolean blockDescriptions;
    public static boolean ghostCloud;

    public static final StepSoundEx soundHolystoneFootstep = new StepSoundEx("chisel:holystone", "chisel:holystone", "chisel:holystone", 1.0f);
    public static final StepSoundEx soundTempleFootstep = new StepSoundEx("dig.stone", "chisel:temple-footstep", "dig.stone", 1.0f);
    public static final StepSoundEx soundMetalFootstep = new StepSoundEx("chisel:metal", "chisel:metal", "chisel:metal", 1.0f);

    static Configuration config;

    public static int RenderEldritchId;
    public static int RenderCTMId;
    public static int RenderCarpetId;

    @Instance("chisel")
    public static Chisel instance;

    @SidedProxy(clientSide = "info.jbcs.minecraft.chisel.ClientProxy", serverSide = "info.jbcs.minecraft.chisel.CommonProxy")
    public static CommonProxy proxy;

    public static boolean chiselStoneToCobbleBricks;
    public static boolean enableChiseling;

    public static boolean featureEnabled(String feature)
    {
        return Chisel.config.get("features", feature, true).getBoolean(true);
    }

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
        configExists = configFile.exists();
        config = new Configuration(configFile);
        config.load();

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

        if(featureEnabled("cloud"))
        {
            itemCloudInABottle = (ItemCloudInABottle) new ItemCloudInABottle().setTextureName("Chisel:cloudinabottle").setCreativeTab(CreativeTabs.tabTools);
            EntityRegistry.registerModEntity(EntityCloudInABottle.class, "CloudInABottle", 1, this, 40, 1, true);
            GameRegistry.registerItem(itemCloudInABottle, "chisel.cloudinabottle");
        }

        if(featureEnabled("ballOfMoss"))
        {
            itemBallOMoss = (ItemBallOMoss) new ItemBallOMoss().setTextureName("Chisel:ballomoss").setCreativeTab(CreativeTabs.tabTools);
            EntityRegistry.registerModEntity(EntityBallOMoss.class, "BallOMoss", 2, this, 40, 1, true);
            GameRegistry.registerItem(itemBallOMoss, "ballomoss");
        }

        if(featureEnabled("smashingRock"))
        {
            smashingRock = (ItemSmashingRock) new ItemSmashingRock().setTextureName("Chisel:smashingrock").setCreativeTab(CreativeTabs.tabTools);
            EntityRegistry.registerModEntity(EntitySmashingRock.class, "SmashingRock", 2, this, 40, 1, true);
            GameRegistry.registerItem(smashingRock, "smashingrock");
        }

        concreteVelocity = config.get("general", "concreteVelocity", 0.45, "Traversing concrete roads, players will acceleration to this velocity. For reference, normal running speed is about 0.28. Set to 0 to disable acceleration.").getDouble(0.45);
        particlesTickrate = config.get("client", "particleTickrate", 1, "Particle tick rate. Greater value = less particles.").getInt(1);
        oldPillars = config.get("client", "pillarOldGraphics", false, "Use old pillar textures").getBoolean(false);
        disableCTM = !config.get("client", "connectedTextures", true, "Enable connected textures").getBoolean(true);
        blockDescriptions = config.get("cslient", "tooltipsUseBlockDescriptions", true, "Make variations of blocks have the same name, and use the description in tooltip to distinguish them.").getBoolean(true);
        chiselStoneToCobbleBricks = config.get("general", "chiselStoneToCobbleBricks", true, "Chisel stone to cobblestone and bricks by left-click.").getBoolean(true);
        enableChiseling = config.get("general", "enableChiselingLeftClicking", false, "Change blocks to another block using the Chisel and left-click.").getBoolean(false);
        ghostCloud = config.get("general", "doesCloudRenderLikeGhost", true).getBoolean(true);
        //iceMelt = config.get("general", "doesIceMeltIntoWater", true).getBoolean(true);

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


        GameRegistry.registerWorldGenerator(new ChiselWorldGenerator(ChiselBlocks.blockMarble, 32, config.get("worldgen", "marbleAmount", 8, "Amount of marble to generate in the world; use 0 for none").getInt(8)), 1000);
        GameRegistry.registerWorldGenerator(new ChiselWorldGenerator(ChiselBlocks.blockLimestone, 32, config.get("worldgen", "limestoneAmount", 4, "Amount of limestone to generate in the world; use 0 for none").getInt(4)), 1000);

        proxy.init();

        config.save();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        new ChiselModCompatibility().postInit(event);
    }
}
