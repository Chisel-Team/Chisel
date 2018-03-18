package team.chisel.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Features;
import team.chisel.api.chunkdata.ChunkData;
import team.chisel.client.handler.DebugHandler;
import team.chisel.client.render.ChiselModelRegistry;
import team.chisel.client.render.ModelLoaderChisel;
import team.chisel.client.render.RenderAutoChisel;
import team.chisel.common.CommonProxy;
import team.chisel.common.block.TileAutoChisel;
import team.chisel.common.init.ChiselBlocks;
import team.chisel.common.init.ChiselItems;
import team.chisel.ctm.client.texture.ctx.OffsetProviderRegistry;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(ModelLoaderChisel.INSTANCE);
        
        if (Features.CHISEL.enabled()) {
            ModelLoader.setCustomModelResourceLocation(ChiselItems.chisel_iron, 0, new ModelResourceLocation(ChiselItems.chisel_iron.getRegistryName(), "inventory"));
            ModelLoader.setCustomModelResourceLocation(ChiselItems.chisel_diamond, 0, new ModelResourceLocation(ChiselItems.chisel_diamond.getRegistryName(), "inventory"));
            ModelLoader.setCustomModelResourceLocation(ChiselItems.chisel_hitech, 0, new ModelResourceLocation(ChiselItems.chisel_hitech.getRegistryName(), "inventory"));
            ModelLoader.setCustomModelResourceLocation(ChiselItems.offsettool, 0, new ModelResourceLocation(ChiselItems.offsettool.getRegistryName(), "inventory"));
        }
        
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ChiselBlocks.auto_chisel), 0, new ModelResourceLocation(ChiselBlocks.auto_chisel.getRegistryName(), "normal"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileAutoChisel.class, new RenderAutoChisel());

        // ModelBakery.addVariantName(Chisel.itemChisel, MOD_ID+":itemChisel");
        // MinecraftForge.EVENT_BUS.register(new CTMModelRegistry.BakedEventListener());
        // MinecraftForge.EVENT_BUS.register(new NonCTMModelRegistry.BakedEventListener());
        MinecraftForge.EVENT_BUS.register(new TextureStitcher());
        MinecraftForge.EVENT_BUS.register(ChiselModelRegistry.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new DebugHandler());
        if (Minecraft.getMinecraft().getResourceManager() instanceof SimpleReloadableResourceManager) {
            SimpleReloadableResourceManager manager = (SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
            manager.registerReloadListener(ChiselPackReloadListener.INSTANCE);
        }

        OffsetProviderRegistry.INSTANCE.registerProvider((world, pos) -> ChunkData.getOffsetForChunk(world, pos).getOffset());        
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
        return Minecraft.getMinecraft().world;
    }
    
    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }
}
