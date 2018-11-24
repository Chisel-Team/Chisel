package team.chisel.client;

import java.lang.reflect.Field;

import com.google.common.collect.ImmutableSet;

import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionRange;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;
import team.chisel.Features;
import team.chisel.api.block.ChiselBlockBuilder;
import team.chisel.api.chunkdata.ChunkData;
import team.chisel.client.handler.DebugHandler;
import team.chisel.client.render.ChiselModelRegistry;
import team.chisel.client.render.ModelLoaderChisel;
import team.chisel.client.render.RenderAutoChisel;
import team.chisel.common.CommonProxy;
import team.chisel.common.block.TileAutoChisel;
import team.chisel.common.init.ChiselBlocks;
import team.chisel.ctm.client.texture.ctx.OffsetProviderRegistry;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    
    private boolean ctmPresent = true;

    @Override
    @SneakyThrows
    public void construct(FMLPreInitializationEvent event) {
        super.construct(event);

        if (!(Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            Field missingException = ReflectionHelper.findField(FMLClientHandler.class, "modsMissing");
            VersionRange range = VersionRange.createFromVersionSpec("[MC1.10.2-0.2.3.26,)");
            if (!Loader.isModLoaded("ctm") || !range.containsVersion(Loader.instance().getIndexedModList().get("ctm").getProcessedVersion())) {
                if (missingException.get(FMLClientHandler.instance()) == null) {
                    missingException.set(FMLClientHandler.instance(), new MissingModsException(ImmutableSet.of(new DefaultArtifactVersion("CTM", range)), MOD_ID, MOD_NAME));
                }
                ctmPresent = false;
            }
        }
    }
    
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if (isCTMPresent()) {
            preInit_impl();
        }
    }
    
    // Runs unsafe code that references CTM
    @Optional.Method(modid = "ctm")
    private void preInit_impl() {
        
        ModelLoaderRegistry.registerLoader(ModelLoaderChisel.INSTANCE);
        
        ModelLoader.setCustomModelResourceLocation(Chisel.itemChiselIron, 0, new ModelResourceLocation(Chisel.itemChiselIron.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Chisel.itemChiselDiamond, 0, new ModelResourceLocation(Chisel.itemChiselDiamond.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Chisel.itemChiselHitech, 0, new ModelResourceLocation(Chisel.itemChiselHitech.getRegistryName(), "inventory"));

        ModelLoader.setCustomModelResourceLocation(Chisel.itemOffsetTool, 0, new ModelResourceLocation(Chisel.itemOffsetTool.getRegistryName(), "inventory"));
        
        if (Features.AUTOCHISEL.enabled()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ChiselBlocks.auto_chisel), 0, new ModelResourceLocation(ChiselBlocks.auto_chisel.getRegistryName(), "normal"));
            ClientRegistry.bindTileEntitySpecialRenderer(TileAutoChisel.class, new RenderAutoChisel());
        }

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
    
    @Override
    public boolean isCTMPresent() {
        return ctmPresent;
    }
}
