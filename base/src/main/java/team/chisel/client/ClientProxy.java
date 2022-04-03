package team.chisel.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import team.chisel.api.chunkdata.ChunkData;
import team.chisel.client.render.RenderAutoChisel;
import team.chisel.common.init.ChiselTileEntities;
import team.chisel.ctm.client.texture.ctx.OffsetProviderRegistry;

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
