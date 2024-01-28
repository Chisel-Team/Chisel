package team.chisel.client;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import team.chisel.Chisel;
import team.chisel.Features;
import team.chisel.api.chunkdata.ChunkData;
import team.chisel.client.render.RenderAutoChisel;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.init.ChiselTileEntities;
import team.chisel.ctm.client.texture.ctx.OffsetProviderRegistry;

import java.util.Map;

@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD)
public class ClientProxy {

    @SubscribeEvent
    public static void registerRenderers(RegisterRenderers event) {
        event.registerBlockEntityRenderer(ChiselTileEntities.AUTO_CHISEL_TE.get(), RenderAutoChisel::new);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
// TODO       MinecraftForge.EVENT_BUS.register(new DebugHandler());

        OffsetProviderRegistry.INSTANCE.registerProvider((world, pos) -> ChunkData.getOffsetForChunk(world, pos).getOffset());        
    }

    @SubscribeEvent
    public static void registerColor(ColorHandlerEvent.Item event) {
        for (Map.Entry<String, BlockEntry<BlockCarvable>> entry : Features.WATERSTONE.entrySet()) {
            event.getBlockColors().register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null && pTintIndex == 1 ? BiomeColors.getAverageWaterColor(pLevel, pPos) : -1, entry.getValue().get());
            event.getItemColors().register((pStack, pTintIndex) -> pTintIndex == 1 ? 0x3F76E4 : -1, entry.getValue().get().asItem());
        }
    }
 
    public static Level getClientWorld() {
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().level);
    }
    
    public static Player getClientPlayer() {
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player);
    }

    public static LevelRenderer getWorldRenderer() {
    	return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().levelRenderer);
    }

    public static void resetWaitTimer() {
        Minecraft.getInstance().gameMode.destroyDelay = 5;
    }
}
