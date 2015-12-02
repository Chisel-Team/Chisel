package team.chisel;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import team.chisel.client.command.CommandTest;
import team.chisel.client.gui.ChiselGuiHandler;
import team.chisel.common.CommonProxy;
import team.chisel.common.Reference;
import team.chisel.common.item.ItemChisel;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Main mod file for Chisel!
 */

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.MOD_NAME)
public class Chisel implements Reference {

    public static final Logger logger = LogManager.getLogger(MOD_NAME);

    @Mod.Instance(MOD_ID)
    public static Chisel instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY, modId = MOD_ID)
    public static CommonProxy proxy;

    public static ItemChisel itemChisel;

    public static final boolean debug = StringUtils.isEmpty(System.getProperty("chisel.debug"));

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        itemChisel = new ItemChisel();
        GameRegistry.registerItem(itemChisel, "itemChisel");
        GameRegistry.addShapedRecipe(new ItemStack(itemChisel), " x", "s ", 'x', Items.iron_ingot, 's', Items.stick);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ChiselGuiHandler());
        proxy.preInit();

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
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
