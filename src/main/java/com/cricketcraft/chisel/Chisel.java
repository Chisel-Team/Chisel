package com.cricketcraft.chisel;

import com.cricketcraft.chisel.client.TextureStitcher;
import com.cricketcraft.chisel.client.command.CommandTest;
import com.cricketcraft.chisel.client.render.ctm.CTMModelRegistry;
import com.cricketcraft.chisel.common.CarvableBlocks;
import com.cricketcraft.chisel.common.CommonProxy;
import com.cricketcraft.chisel.common.Reference;
import com.cricketcraft.chisel.common.variation.Variation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.util.IRegistry;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;


/**
 * Main mod file for Chisel!
 */

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.MOD_NAME)
public class Chisel implements Reference{

    public static final Logger logger = LogManager.getLogger(MOD_NAME);

    @Mod.Instance(MOD_ID)
    public static Chisel instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY, modId = MOD_ID)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        Variation.doStuff();
        MinecraftForge.EVENT_BUS.register(new CTMModelRegistry.BakedEventListener());
        MinecraftForge.EVENT_BUS.register(new TextureStitcher());
        CarvableBlocks.preInitBlocks();

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
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
