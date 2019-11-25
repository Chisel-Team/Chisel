package team.chisel;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;

import mezz.jei.config.forge.Configuration;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import team.chisel.api.ChiselAPIProps;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.client.gui.ChiselGuiHandler;
import team.chisel.client.gui.PacketChiselButton;
import team.chisel.client.gui.PacketHitechSettings;
import team.chisel.common.Reference;
import team.chisel.common.block.MessageAutochiselFX;
import team.chisel.common.block.MessageUpdateAutochiselSource;
import team.chisel.common.carving.CarvingVariationRegistry;
import team.chisel.common.carving.ChiselModeRegistry;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselBlocks;
import team.chisel.common.init.ChiselFuelHandler;
import team.chisel.common.init.ChiselSounds;
import team.chisel.common.integration.imc.IMCHandler;
import team.chisel.common.item.ChiselController;
import team.chisel.common.item.ChiselMode;
import team.chisel.common.item.PacketChiselMode;
import team.chisel.common.util.GenerationHandler;
import team.chisel.common.util.PerChunkData;
import team.chisel.common.util.PerChunkData.MessageChunkData;

@Mod(Reference.MOD_ID)
public class Chisel implements Reference {

    public static final Logger logger = LogManager.getLogger(MOD_NAME);

//    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY, modId = MOD_ID)
//    public static CommonProxy proxy;

    public static final boolean debug = false;// StringUtils.isEmpty(System.getProperty("chisel.debug"));

    public static final SimpleChannel network = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    
    static {
        network.registerMessage(0, PacketChiselButton.class, PacketChiselButton::encode, PacketChiselButton::decode, PacketChiselButton::handle);
        network.registerMessage(1, PacketHitechSettings.class, PacketHitechSettings::encode, PacketHitechSettings::decode, PacketHitechSettings::handle);
        network.registerMessage(2, MessageChunkData.class, MessageChunkData::encode, MessageChunkData::decode, MessageChunkData::handle);
        network.registerMessage(3, PacketChiselMode.class, PacketChiselMode::encode, PacketChiselMode::decode, PacketChiselMode::handle);
        network.registerMessage(4, MessageUpdateAutochiselSource.class, MessageUpdateAutochiselSource::encode, MessageUpdateAutochiselSource::decode, MessageUpdateAutochiselSource::handle);
        network.registerMessage(5, MessageAutochiselFX.class, MessageAutochiselFX::encode, MessageAutochiselFX::decode, MessageAutochiselFX::handle);
    }
    
    private static Map<String, Block> remaps = ImmutableMap.of();
    
    public Chisel() {
        CarvingUtils.chisel = new CarvingVariationRegistry();
        CarvingUtils.modes = ChiselModeRegistry.INSTANCE;
        ChiselMode.values(); // static init our modes
        ChiselAPIProps.MOD_ID = MOD_ID;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ChiselSounds.init();
        
        proxy.construct(event);

        File configFile = event.getSuggestedConfigurationFile();
        Configurations.configExists = configFile.exists();
        Configurations.config = new Configuration(configFile);
        Configurations.config.load();
        Configurations.refreshConfig();

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
        
        // If we add a future vanilla block, it should be added here once the vanilla version exists
        remaps = ImmutableMap.<String, Block>builder()
                .put("concrete_powder", Blocks.CONCRETE_POWDER)
                .put("concrete", Blocks.CONCRETE)
                .build();

        GameRegistry.registerFuelHandler(new ChiselFuelHandler());

        addCompactorPressRecipe(1000, new ItemStack(Blocks.BONE_BLOCK), new ItemStack(ChiselBlocks.limestone2, 1, 7));
        addCompactorPressRecipe(1000, new ItemStack(ChiselBlocks.limestone2, 1, 7), new ItemStack(ChiselBlocks.marble2, 1, 7));

        /*
//      Example of IMC
                
        FMLInterModComms.sendMessage(MOD_ID, IMC.ADD_VARIATION.toString(), "marble|minecraft:dirt|0");
        CompoundNBT testtag = new CompoundNBT();
        testtag.setString("group", "marble");
        testtag.setTag("stack", new ItemStack(Items.DIAMOND_PICKAXE, 1, 100).serializeNBT());
        FMLInterModComms.sendMessage(MOD_ID, IMC.ADD_VARIATION_V2.toString(), testtag);
        testtag = new CompoundNBT();
        testtag.setString("group", "marble");
        testtag.setString("block", Blocks.WOOL.getRegistryName().toString());
        FMLInterModComms.sendMessage(MOD_ID, IMC.ADD_VARIATION_V2.toString(), testtag);
        testtag = new CompoundNBT();
        testtag.setString("group", "marble");
        testtag.setString("block", Blocks.WOOL.getRegistryName().toString());
        testtag.setInteger("meta", Blocks.WOOL.getMetaFromState(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN)));
        FMLInterModComms.sendMessage(MOD_ID, IMC.ADD_VARIATION_V2.toString(), testtag);
        testtag = new CompoundNBT();
        testtag.setString("group", "marble");
        testtag.setTag("stack", new ItemStack(Items.REDSTONE).serializeNBT());
        testtag.setString("block", Blocks.REDSTONE_WIRE.getRegistryName().toString());
        FMLInterModComms.sendMessage(MOD_ID, IMC.ADD_VARIATION_V2.toString(), testtag);
        
        testtag = new CompoundNBT();
        testtag.setString("group", "marble");
        testtag.setTag("stack", new ItemStack(ChiselBlocks.marble, 1, 3).serializeNBT());
        FMLInterModComms.sendMessage(MOD_ID, IMC.REMOVE_VARIATION_V2.toString(), testtag);
        testtag = new CompoundNBT();
        testtag.setString("group", "marble");
        testtag.setString("block", ChiselBlocks.marbleextra.getRegistryName().toString());
        FMLInterModComms.sendMessage(MOD_ID, IMC.REMOVE_VARIATION_V2.toString(), testtag);
        testtag = new CompoundNBT();
        testtag.setString("group", "marble");
        testtag.setString("block", ChiselBlocks.marbleextra.getRegistryName().toString());
        testtag.setInteger("meta", 5);
        FMLInterModComms.sendMessage(MOD_ID, IMC.REMOVE_VARIATION_V2.toString(), testtag);
        */
    }

    private static void addCompactorPressRecipe(int energy, ItemStack input, ItemStack output)
    {

        CompoundNBT message = new CompoundNBT();

        message.setInteger("energy", energy);
        message.setTag("input", new CompoundNBT());
        message.setTag("output", new CompoundNBT());

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
    
    @SubscribeEvent
    public void onMissingBlock(MissingMappings<Block> event) {
        for (Mapping<Block> mapping : event.getMappings()) {
            Optional.ofNullable(remaps.get(mapping.key.getResourcePath())).ifPresent(mapping::remap);
        }
    }
    
    @SubscribeEvent
    public void onMissingItem(MissingMappings<Item> event) {
        for (Mapping<Item> mapping : event.getMappings()) {
            Optional.ofNullable(remaps.get(mapping.key.getResourcePath())).map(Item::getItemFromBlock).ifPresent(mapping::remap);
        }
    }
}
