package team.chisel;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import lombok.SneakyThrows;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionRange;
import net.minecraftforge.fml.relauncher.Side;
import team.chisel.api.ChiselAPIProps;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.client.gui.ChiselGuiHandler;
import team.chisel.client.gui.PacketChiselButton;
import team.chisel.client.gui.PacketHitechSettings;
import team.chisel.common.CommonProxy;
import team.chisel.common.Reference;
import team.chisel.common.carving.Carving;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselBlocks;
import team.chisel.common.init.ChiselFuelHandler;
import team.chisel.common.integration.imc.IMCHandler;
import team.chisel.common.item.ChiselController;
import team.chisel.common.util.GenerationHandler;
import team.chisel.common.util.PerChunkData;
import team.chisel.common.util.PerChunkData.MessageChunkData;
import team.chisel.common.util.PerChunkData.MessageChunkDataHandler;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.MOD_NAME, dependencies = "required-after:forge@[14.21.0.2363,);", acceptedMinecraftVersions = "[1.12, 1.12.1)")
public class Chisel implements Reference {

    public static final Logger logger = LogManager.getLogger(MOD_NAME);

    @SuppressWarnings("null")
    @Mod.Instance(MOD_ID)
    public static @Nonnull Chisel instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY, modId = MOD_ID)
    public static CommonProxy proxy;

    public static final boolean debug = false;// StringUtils.isEmpty(System.getProperty("chisel.debug"));

    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
    static {
        network.registerMessage(PacketChiselButton.Handler.class, PacketChiselButton.class, 0, Side.SERVER);
        network.registerMessage(PacketHitechSettings.Handler.class, PacketHitechSettings.class, 1, Side.SERVER);
        network.registerMessage(MessageChunkDataHandler.class, MessageChunkData.class, 2, Side.CLIENT);
    }
    
    public Chisel() {
        CarvingUtils.chisel = Carving.chisel;
        ChiselAPIProps.MOD_ID = MOD_ID;
    }
    
    @Mod.EventHandler
    @SneakyThrows
    public void onConstruct(FMLConstructionEvent event) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            VersionRange range = VersionRange.createFromVersionSpec("[MC1.11.2-0.1.0.2,)");
            if (!Loader.isModLoaded("ctm") || !range.containsVersion(Loader.instance().getIndexedModList().get("ctm").getProcessedVersion())) {
                throw new MissingModsException(ImmutableSet.of(new DefaultArtifactVersion("ctm", range)), MOD_ID, MOD_NAME);
            }
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.construct(event);

        File configFile = event.getSuggestedConfigurationFile();
        Configurations.configExists = configFile.exists();
        Configurations.config = new Configuration(configFile);
        Configurations.config.load();
        Configurations.refreshConfig();

        // TODO once forge has recipes
//        GameRegistry.addRecipe(new ShapedOreRecipe(itemChiselIron, " x", "s ", 'x', "ingotIron", 's', "stickWood"));
//        GameRegistry.addRecipe(new ShapedOreRecipe(itemChiselDiamond, " x", "s ", 'x', "gemDiamond", 's', "stickWood"));
//        GameRegistry.addRecipe(new ShapelessOreRecipe(itemChiselHitech, itemChiselDiamond, "dustRedstone", "ingotGold"));
//        GameRegistry.addRecipe(new ShapedOreRecipe(itemOffsetTool, "-o", "|-", 'o', Items.ENDER_PEARL, '|', "stickWood", '-', "ingotIron"));

        MinecraftForge.EVENT_BUS.register(PerChunkData.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ChiselController.class);

        GameRegistry.registerWorldGenerator(GenerationHandler.INSTANCE, 2);
        MinecraftForge.EVENT_BUS.register(GenerationHandler.INSTANCE);
        MinecraftForge.TERRAIN_GEN_BUS.register(GenerationHandler.INSTANCE);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ChiselGuiHandler());

        //EntityRegistry.registerModEntity(EntityFallingBlockCarvable.class, "falling_block", 60, Chisel.instance, 64, 3, false);

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        // BlockRegistry.init(event);

        GameRegistry.registerFuelHandler(new ChiselFuelHandler());

        addCompactorPressRecipe(1000, new ItemStack(Blocks.BONE_BLOCK), new ItemStack(ChiselBlocks.limestone2, 1, 7));
        addCompactorPressRecipe(1000, new ItemStack(ChiselBlocks.limestone2, 1, 7), new ItemStack(ChiselBlocks.marble2, 1, 7));

        /*
        Example of IMC

        FMLInterModComms.sendMessage("chisel", "variation:add", "treated_wood|immersiveengineering:treatedWood|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "treated_wood|immersiveengineering:treatedWood|1");
        FMLInterModComms.sendMessage("chisel", "variation:add", "treated_wood|immersiveengineering:treatedWood|2");
        */
    }

    private static void addCompactorPressRecipe(int energy, ItemStack input, ItemStack output)
    {

        NBTTagCompound message = new NBTTagCompound();

        message.setInteger("energy", energy);
        message.setTag("input", new NBTTagCompound());
        message.setTag("output", new NBTTagCompound());

        input.writeToNBT(message.getCompoundTag("input"));
        output.writeToNBT(message.getCompoundTag("output"));

        FMLInterModComms.sendMessage("thermalexpansion", "addcompactorpressrecipe", message);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
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

    @Mod.EventHandler
    public void onIMC(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage msg : event.getMessages()) {
            IMCHandler.INSTANCE.handleMessage(msg);
        }
    }
}
