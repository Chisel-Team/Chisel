package team.chisel;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import team.chisel.api.block.ChiselBlockData;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.client.command.CommandTest;
import team.chisel.client.gui.ChiselGuiHandler;
import team.chisel.common.CommonProxy;
import team.chisel.common.Reference;
import team.chisel.common.carving.Carving;
import team.chisel.common.init.BlockRegistry;
import team.chisel.common.item.ItemChisel;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.MOD_NAME)
public class Chisel implements Reference {

    public static final Logger logger = LogManager.getLogger(MOD_NAME);

    @Mod.Instance(MOD_ID)
    public static Chisel instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY, modId = MOD_ID)
    public static CommonProxy proxy;

    public static ItemChisel itemChisel;

    public static final boolean debug = true;//StringUtils.isEmpty(System.getProperty("chisel.debug"));

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.construct(event);
        itemChisel = new ItemChisel();
        GameRegistry.registerItem(itemChisel, "itemChisel");
        GameRegistry.addShapedRecipe(new ItemStack(itemChisel), " x", "s ", 'x', Items.iron_ingot, 's', Items.stick);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ChiselGuiHandler());

        ChiselBlockFactory factory = ChiselBlockFactory.newFactory("chisel");
        ChiselBlockData voidstone = factory.newBlock("voidstone").newVariation("normal", "test").setTextureLocation(new ResourceLocation("chisel", "textures/blocks/voidstone/normal.json"))
                .buildVariation().build();
        BlockRegistry.registerBlock(voidstone);

        ChiselBlockData bookshelf = factory.newBlock("bookshelf").newVariation("necromancer", "test").setTextureLocation(new ResourceLocation("chisel", "textures/blocks/bookshelf/necromancer.json"))
                .buildVariation().build();
        BlockRegistry.registerBlock(bookshelf);

        Carving.chisel.addVariation("test", Blocks.bedrock.getDefaultState(), 99);
        
        proxy.preInit(event);
        BlockRegistry.preInit(event);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        //BlockRegistry.init(event);
        ClientCommandHandler.instance.registerCommand(new CommandTest());

    }

    /**
     * Sends a debug message, basically a wrapper for the logger that only prints when debugging is enabled
     * @param message
     */
    public static void debug(String message){
        if (debug){
            logger.info(message);
        }
    }

    public static void debug(float[] array){
        if (debug) {
            String message = "[";
            for (float obj : array) {
                message = message + obj + " ";
            }
            debug(message+"]");
        }
    }

//    @Mod.EventHandler
//    public void postInit(FMLPostInitializationEvent event){
//        Map ro;
//        try {
//            Field f = Minecraft.class.getDeclaredField("modelManager");
//            f.setAccessible(true);
//            ModelManager m=(ModelManager)f.get(Minecraft.getMinecraft());
//            Field f2 = ModelManager.class.getDeclaredField("modelRegistry");
//            f2.setAccessible(true);
//            IRegistry r = (IRegistry)f2.get(m);
//            Field f3 = r.getClass().getDeclaredField("registryObjects");
//            f3.setAccessible(true);
//            ro = (Map)f3.get(r);
//        } catch (Exception exception){
//            throw new RuntimeException(exception);
//        }
//        for (Map.Entry e : (Set<Map.Entry>)ro.entrySet()){
//            logger.info(e.getKey().toString()+" "+e.getValue().toString());
//        }
//        throw new RuntimeException("look");
//
//    }

}
