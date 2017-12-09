package team.chisel;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import team.chisel.api.ChiselAPIProps;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.client.gui.ChiselGuiHandler;
import team.chisel.client.gui.PacketChiselButton;
import team.chisel.client.gui.PacketHitechSettings;
import team.chisel.common.CommonProxy;
import team.chisel.common.Reference;
import team.chisel.common.carving.Carving;
import team.chisel.common.carving.ChiselModeRegistry;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselBlocks;
import team.chisel.common.init.ChiselFuelHandler;
import team.chisel.common.init.ChiselSounds;
import team.chisel.common.integration.imc.IMCHandler;
import team.chisel.common.item.ChiselController;
import team.chisel.common.item.ChiselMode;
import team.chisel.common.item.ItemChisel;
import team.chisel.common.item.ItemChisel.ChiselType;
import team.chisel.common.item.ItemOffsetTool;
import team.chisel.common.item.PacketChiselMode;
import team.chisel.common.util.GenerationHandler;
import team.chisel.common.util.PerChunkData;
import team.chisel.common.util.PerChunkData.MessageChunkData;
import team.chisel.common.util.PerChunkData.MessageChunkDataHandler;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.MOD_NAME, acceptedMinecraftVersions = "[1.11, 1.12)")
public class Chisel implements Reference {

    public static final Logger logger = LogManager.getLogger(MOD_NAME);

    @SuppressWarnings("null")
    @Mod.Instance(MOD_ID)
    public static @Nonnull Chisel instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY, modId = MOD_ID)
    public static CommonProxy proxy;

    @SuppressWarnings("null")
    public static @Nonnull ItemChisel itemChiselIron, itemChiselDiamond, itemChiselHitech;
    @SuppressWarnings("null")
    public static @Nonnull ItemOffsetTool itemOffsetTool;

    public static final boolean debug = false;// StringUtils.isEmpty(System.getProperty("chisel.debug"));

    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
    static {
        network.registerMessage(PacketChiselButton.Handler.class, PacketChiselButton.class, 0, Side.SERVER);
        network.registerMessage(PacketHitechSettings.Handler.class, PacketHitechSettings.class, 1, Side.SERVER);
        network.registerMessage(MessageChunkDataHandler.class, MessageChunkData.class, 2, Side.CLIENT);
        network.registerMessage(PacketChiselMode.Handler.class, PacketChiselMode.class, 3, Side.SERVER);
    }
    
    public Chisel() {
        CarvingUtils.chisel = Carving.chisel;
        CarvingUtils.modes = ChiselModeRegistry.INSTANCE;
        ChiselMode.values(); // static init our modes
        ChiselAPIProps.MOD_ID = MOD_ID;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ChiselSounds.init();
        
        proxy.construct(event);

        File configFile = event.getSuggestedConfigurationFile();
        Configurations.configExists = configFile.exists();
        Configurations.config = new Configuration(configFile);
        Configurations.config.load();
        Configurations.refreshConfig();

        itemChiselIron = new ItemChisel(ChiselType.IRON);
        itemChiselDiamond = new ItemChisel(ChiselType.DIAMOND);
        itemChiselHitech = new ItemChisel(ChiselType.HITECH);

        itemOffsetTool = new ItemOffsetTool();

        GameRegistry.register(itemChiselIron);
        GameRegistry.register(itemChiselDiamond);
        GameRegistry.register(itemChiselHitech);

        GameRegistry.register(itemOffsetTool);

        if (!Configurations.chiselRecipe) {
            GameRegistry.addRecipe(new ShapedOreRecipe(itemChiselIron, " x", "s ", 'x', "ingotIron", 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(itemChiselDiamond, " x", "s ", 'x', "gemDiamond", 's', "stickWood"));
        } else {
            GameRegistry.addRecipe(new ShapedOreRecipe(itemChiselIron, " xx", " xx", "s  ", 'x', "ingotIron", 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(itemChiselDiamond, " xx", " xx", "s  ", 'x', "gemDiamond", 's', "stickWood"));
        }
        GameRegistry.addRecipe(new ShapelessOreRecipe(itemChiselHitech, itemChiselDiamond, "dustRedstone", "ingotGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemOffsetTool, "-o", "|-", 'o', Items.ENDER_PEARL, '|', "stickWood", '-', "ingotIron"));

        MinecraftForge.EVENT_BUS.register(PerChunkData.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ChiselController.class);

        GameRegistry.registerWorldGenerator(GenerationHandler.INSTANCE, 2);
        MinecraftForge.EVENT_BUS.register(GenerationHandler.INSTANCE);
        MinecraftForge.TERRAIN_GEN_BUS.register(GenerationHandler.INSTANCE);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ChiselGuiHandler());

        Features.preInit();

        //EntityRegistry.registerModEntity(EntityFallingBlockCarvable.class, "falling_block", 60, Chisel.instance, 64, 3, false);

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Features.init();

        proxy.init();
        // BlockRegistry.init(event);

        GameRegistry.registerFuelHandler(new ChiselFuelHandler());

        addCompactorPressRecipe(1000, new ItemStack(Blocks.BONE_BLOCK), new ItemStack(ChiselBlocks.limestone2, 1, 7));
        addCompactorPressRecipe(1000, new ItemStack(ChiselBlocks.limestone2, 1, 7), new ItemStack(ChiselBlocks.marble2, 1, 7));

        /*
//      Example of IMC
                
        FMLInterModComms.sendMessage(MOD_ID, IMC.ADD_VARIATION.toString(), "marble|minecraft:dirt|0");
        NBTTagCompound testtag = new NBTTagCompound();
        testtag.setString("group", "marble");
        testtag.setTag("stack", new ItemStack(Items.DIAMOND_PICKAXE, 1, 100).serializeNBT());
        FMLInterModComms.sendMessage(MOD_ID, IMC.ADD_VARIATION_V2.toString(), testtag);
        testtag = new NBTTagCompound();
        testtag.setString("group", "marble");
        testtag.setString("block", Blocks.WOOL.getRegistryName().toString());
        FMLInterModComms.sendMessage(MOD_ID, IMC.ADD_VARIATION_V2.toString(), testtag);
        testtag = new NBTTagCompound();
        testtag.setString("group", "marble");
        testtag.setString("block", Blocks.WOOL.getRegistryName().toString());
        testtag.setInteger("meta", Blocks.WOOL.getMetaFromState(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN)));
        FMLInterModComms.sendMessage(MOD_ID, IMC.ADD_VARIATION_V2.toString(), testtag);
        testtag = new NBTTagCompound();
        testtag.setString("group", "marble");
        testtag.setTag("stack", new ItemStack(Items.REDSTONE).serializeNBT());
        testtag.setString("block", Blocks.REDSTONE_WIRE.getRegistryName().toString());
        FMLInterModComms.sendMessage(MOD_ID, IMC.ADD_VARIATION_V2.toString(), testtag);
        
        testtag = new NBTTagCompound();
        testtag.setString("group", "marble");
        testtag.setTag("stack", new ItemStack(ChiselBlocks.marble, 1, 3).serializeNBT());
        FMLInterModComms.sendMessage(MOD_ID, IMC.REMOVE_VARIATION_V2.toString(), testtag);
        testtag = new NBTTagCompound();
        testtag.setString("group", "marble");
        testtag.setString("block", ChiselBlocks.marbleextra.getRegistryName().toString());
        FMLInterModComms.sendMessage(MOD_ID, IMC.REMOVE_VARIATION_V2.toString(), testtag);
        testtag = new NBTTagCompound();
        testtag.setString("group", "marble");
        testtag.setString("block", ChiselBlocks.marbleextra.getRegistryName().toString());
        testtag.setInteger("meta", 5);
        FMLInterModComms.sendMessage(MOD_ID, IMC.REMOVE_VARIATION_V2.toString(), testtag);
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
        IMCHandler.INSTANCE.imcCounts.forEachEntry((s, c) -> {
            Chisel.logger.info("Received {} IMC messages from mod {}.", c, s);
            return true;
        });
        IMCHandler.INSTANCE.imcCounts.clear();
    }

    @Mod.EventHandler //TODO fix
    public void onMissingMappings(FMLMissingMappingsEvent event) {
        for (FMLMissingMappingsEvent.MissingMapping mapping : event.get()) {
            if (mapping.resourceLocation.getResourceDomain().equals(Reference.MOD_ID)) {
                @Nonnull
                String path = mapping.resourceLocation.getResourcePath();
                if (path.endsWith("extra")) {
                    path = path.replace("extra", "2");
                    ResourceLocation newRes = new ResourceLocation(mapping.resourceLocation.getResourceDomain(), path);
                    Block block = ForgeRegistries.BLOCKS.getValue(newRes);
                    if (block != null) {
                        if (mapping.type == GameRegistry.Type.BLOCK) {
                            mapping.remap(block);
                        } else {
                            mapping.remap(Item.getItemFromBlock(block));
                        }
                    }
                }

                if(path.endsWith("bookshelf"))
                {
                    path = path.replace("bookshelf", "bookshelf_oak");
                    ResourceLocation newRes = new ResourceLocation(mapping.resourceLocation.getResourceDomain(), path);
                    Block block = ForgeRegistries.BLOCKS.getValue(newRes);

                    if (block != null) {
                        if (mapping.type == GameRegistry.Type.BLOCK) {
                            mapping.remap(block);
                        } else {
                            mapping.remap(Item.getItemFromBlock(block));
                        }
                    }
                }
                else if (path.toLowerCase().endsWith("bookshelf_dark-oak"))
                {
                    path = path.replace("bookshelf_dark-oak", "bookshelf_darkoak");
                    ResourceLocation newRes = new ResourceLocation(mapping.resourceLocation.getResourceDomain(), path);
                    Block block = ForgeRegistries.BLOCKS.getValue(newRes);

                    if (block != null) {
                        if (mapping.type == GameRegistry.Type.BLOCK) {
                            mapping.remap(block);
                        } else {
                            mapping.remap(Item.getItemFromBlock(block));
                        }
                    }
                }
            }
        }
    }

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
