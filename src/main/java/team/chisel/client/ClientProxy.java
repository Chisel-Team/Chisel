package team.chisel.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;
import team.chisel.api.block.ChiselBlockBuilder.VariationBuilder.IVariationBuilderDelegate;
import team.chisel.client.handler.DebugHandler;
import team.chisel.client.render.ChiselModelRegistry;
import team.chisel.common.CommonProxy;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.init.TextureTypeRegistry;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void construct(FMLPreInitializationEvent event) {
        TextureTypeRegistry.preInit(event);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

        ModelLoader.setCustomModelResourceLocation(Chisel.itemChisel, 0, new ModelResourceLocation(MOD_ID + ":itemChisel", "inventory"));
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
    }

    @Override
    public void init() {

    }

    @Override
    public void preTextureStitch() {
        try {
            ReflectionHelper.setPrivateValue(TextureMap.class, Minecraft.getMinecraft().getTextureMapBlocks(), false, "skipFirst");
        } catch (Exception exception) {
            // Older version of forge, this is fine because it means this is not needed so no crash
        }
    }

    @Override
    public void initiateFaceData(BlockCarvable carvable) {
        carvable.setBlockFaceData(new BlockFaceData(carvable.getVariations()));
    }

    private static final IVariationBuilderDelegate CLIENT_DELEGATE = new BuilderDelegateClient();

    @Override
    public IVariationBuilderDelegate getBuilderDelegate() {
        return CLIENT_DELEGATE;
    }
}
