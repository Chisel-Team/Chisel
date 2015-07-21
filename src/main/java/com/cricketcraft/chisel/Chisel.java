package com.cricketcraft.chisel;

import com.cricketcraft.chisel.client.command.CommandTest;
import com.cricketcraft.chisel.client.gui.ChiselGuiHandler;
import com.cricketcraft.chisel.common.CarvableBlocks;
import com.cricketcraft.chisel.common.CommonProxy;
import com.cricketcraft.chisel.common.Reference;
import com.cricketcraft.chisel.common.item.ItemChisel;
import com.cricketcraft.chisel.common.variation.Variation;
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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Variation.doStuff();
        proxy.preInit();
        CarvableBlocks.preInitBlocks();
        itemChisel = new ItemChisel();
        GameRegistry.registerItem(itemChisel, "itemChisel");
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ChiselGuiHandler());

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        CarvableBlocks.initBlocks();
        ClientCommandHandler.instance.registerCommand(new CommandTest());
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
