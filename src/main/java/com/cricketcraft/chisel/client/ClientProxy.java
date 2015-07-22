package com.cricketcraft.chisel.client;

import com.cricketcraft.chisel.client.handler.DebugHandler;
import com.cricketcraft.chisel.client.render.NonCTMModelRegistry;
import com.cricketcraft.chisel.client.render.ctm.CTMModelRegistry;
import com.cricketcraft.chisel.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The Client Proxy
 *
 * @author minecreatr
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public void preInit() {

        MinecraftForge.EVENT_BUS.register(new CTMModelRegistry.BakedEventListener());
        MinecraftForge.EVENT_BUS.register(new NonCTMModelRegistry.BakedEventListener());
        MinecraftForge.EVENT_BUS.register(new TextureStitcher());
        MinecraftForge.EVENT_BUS.register(new DebugHandler());
    }

    @Override
    public void preTextureStitch() {
        ReflectionHelper.setPrivateValue(TextureMap.class, Minecraft.getMinecraft().getTextureMapBlocks(), false, "skipFirst");
    }
}
