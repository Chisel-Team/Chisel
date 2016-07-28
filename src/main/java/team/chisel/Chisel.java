package team.chisel;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.client.gui.ChiselGuiHandler;
import team.chisel.client.gui.PacketChiselButton;
import team.chisel.client.gui.PacketChiselNBT;
import team.chisel.common.CommonProxy;
import team.chisel.common.Reference;
import team.chisel.common.carving.Carving;
import team.chisel.common.config.Configurations;
import team.chisel.common.item.ItemChisel;
import team.chisel.common.item.ItemChisel.ChiselType;
import team.chisel.common.util.GenerationHandler;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.MOD_NAME, acceptedMinecraftVersions = "[1.9.4, 1.11)")
public class Chisel implements Reference {

    public static final Logger logger = LogManager.getLogger(MOD_NAME);

    @Mod.Instance(MOD_ID)
    public static Chisel instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY, modId = MOD_ID)
    public static CommonProxy proxy;

    @SuppressWarnings("null")
    public static @Nonnull ItemChisel itemChiselIron, itemChiselDiamond, itemChiselHitech;

    public static final boolean debug = false;// StringUtils.isEmpty(System.getProperty("chisel.debug"));
    
    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
    static {
        network.registerMessage(PacketChiselButton.Handler.class, PacketChiselButton.class, 0, Side.SERVER);
        network.registerMessage(PacketChiselNBT.Handler.class, PacketChiselNBT.class, 1, Side.SERVER);
    }

    public Chisel() {
        CarvingUtils.chisel = Carving.chisel;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.construct(event);

        File configFile = event.getSuggestedConfigurationFile();
        Configurations.configExists = configFile.exists();
        Configurations.config = new Configuration(configFile);
        Configurations.config.load();
        Configurations.refreshConfig();

        itemChiselIron = new ItemChisel(ChiselType.IRON);
        itemChiselDiamond = new ItemChisel(ChiselType.DIAMOND);
        itemChiselHitech = new ItemChisel(ChiselType.HITECH);
        
        GameRegistry.register(itemChiselIron);
        GameRegistry.register(itemChiselDiamond);
        GameRegistry.register(itemChiselHitech);

        GameRegistry.addRecipe(new ShapedOreRecipe(itemChiselIron, " x", "s ", 'x', "ingotIron", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemChiselDiamond, " x", "s ", 'x', "gemDiamond", 's', "stickWood"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(itemChiselHitech, itemChiselDiamond, "dustRedstone", "ingotGold"));
        
        GameRegistry.registerWorldGenerator(GenerationHandler.INSTANCE, 2);
        MinecraftForge.EVENT_BUS.register(GenerationHandler.INSTANCE);
        MinecraftForge.TERRAIN_GEN_BUS.register(GenerationHandler.INSTANCE);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ChiselGuiHandler());

        Features.preInit();
        
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Features.init();
        
        proxy.init();
        // BlockRegistry.init(event);
    }

    /**
     * Sends a debug message, basically a wrapper for the logger that only prints when debugging is enabled
     * 
     * @param message
     */
    public static void debug(String message) {
        if (debug) {
            logger.info(message);
        }
    }

    public static void debug(float[] array) {
        if (debug) {
            String message = "[";
            for (float obj : array) {
                message = message + obj + " ";
            }
            debug(message + "]");
        }
    }

    //@Mod.EventHandler TODO fix
    //public void onMissingMappings(FMLMissingMappingsEvent event) {
    //    for (FMLMissingMappingsEvent.MissingMapping mapping : event.get()) {
    //        if (mapping.resourceLocation.getResourceDomain().equals(Reference.MOD_ID)) {
    //            @Nonnull
    //            String path = mapping.resourceLocation.getResourcePath();
    //            if (path.endsWith("extra")) {
    //                path = path.replace("extra", "1");
    //                ResourceLocation newRes = new ResourceLocation(mapping.resourceLocation.getResourceDomain(), path);
    //                Block block = ForgeRegistries.BLOCKS.getValue(newRes);
    //                if (block != null) {
    //                    if (mapping.type == Type.BLOCK) {
    //                        mapping.remap(block);
    //                    } else {
    //                        mapping.remap(Item.getItemFromBlock(block));
    //                    }
    //                }
    //            }
    //        }
    //    }
    //}

    // @Mod.EventHandler
    // public void postInit(FMLPostInitializationEvent event){
    // Map ro;
    // try {
    // Field f = Minecraft.class.getDeclaredField("modelManager");
    // f.setAccessible(true);
    // ModelManager m=(ModelManager)f.get(Minecraft.getMinecraft());
    // Field f2 = ModelManager.class.getDeclaredField("modelRegistry");
    // f2.setAccessible(true);
    // IRegistry r = (IRegistry)f2.get(m);
    // Field f3 = r.getClass().getDeclaredField("registryObjects");
    // f3.setAccessible(true);
    // ro = (Map)f3.get(r);
    // } catch (Exception exception){
    // throw new RuntimeException(exception);
    // }
    // for (Map.Entry e : (Set<Map.Entry>)ro.entrySet()){
    // logger.info(e.getKey().toString()+" "+e.getValue().toString());
    // }
    // throw new RuntimeException("look");
    //
    // }

}
