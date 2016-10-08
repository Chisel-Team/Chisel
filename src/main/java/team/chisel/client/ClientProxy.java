package team.chisel.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;
import team.chisel.client.handler.DebugHandler;
import team.chisel.client.handler.TooltipHandler;
import team.chisel.client.render.ChiselModelRegistry;
import team.chisel.client.render.ModelLoaderChisel;
import team.chisel.common.CommonProxy;
import team.chisel.common.init.TextureTypeRegistry;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void construct(FMLPreInitializationEvent event) {
        TextureTypeRegistry.preInit(event);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        
        ModelLoaderRegistry.registerLoader(new ModelLoaderChisel());
        
        ModelLoader.setCustomModelResourceLocation(Chisel.itemChiselIron, 0, new ModelResourceLocation(Chisel.itemChiselIron.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Chisel.itemChiselDiamond, 0, new ModelResourceLocation(Chisel.itemChiselDiamond.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Chisel.itemChiselHitech, 0, new ModelResourceLocation(Chisel.itemChiselHitech.getRegistryName(), "inventory"));

        ModelLoader.setCustomModelResourceLocation(Chisel.itemOffsetTool, 0, new ModelResourceLocation(Chisel.itemOffsetTool.getRegistryName(), "inventory"));

        // ModelBakery.addVariantName(Chisel.itemChisel, MOD_ID+":itemChisel");
        // MinecraftForge.EVENT_BUS.register(new CTMModelRegistry.BakedEventListener());
        // MinecraftForge.EVENT_BUS.register(new NonCTMModelRegistry.BakedEventListener());
        MinecraftForge.EVENT_BUS.register(new TextureStitcher());
        MinecraftForge.EVENT_BUS.register(ChiselModelRegistry.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new DebugHandler());
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
        if (Minecraft.getMinecraft().getResourceManager() instanceof SimpleReloadableResourceManager) {
            SimpleReloadableResourceManager manager = (SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
            manager.registerReloadListener(ChiselPackReloadListener.INSTANCE);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void preTextureStitch() {
        try {
//            ReflectionHelper.setPrivateValue(TextureMap.class, Minecraft.getMinecraft().getTextureMapBlocks(), false, "skipFirst");
        } catch (Exception exception) {
            // Older version of forge, this is fine because it means this is not needed so no crash
        }
    }
    
    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }
}
