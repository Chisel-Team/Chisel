package info.jbcs.minecraft.chisel;

import info.jbcs.minecraft.chisel.entity.EntityBallOMoss;
import info.jbcs.minecraft.chisel.entity.EntityCloudInABottle;
import info.jbcs.minecraft.chisel.client.render.BlockAdvancedMarbleRenderer;
import info.jbcs.minecraft.chisel.client.render.BlockCarpetRenderer;
import info.jbcs.minecraft.chisel.client.render.BlockEldritchRenderer;
import info.jbcs.minecraft.chisel.client.render.BlockMarblePaneRenderer;
import info.jbcs.minecraft.chisel.client.render.BlockMarblePillarRenderer;
import info.jbcs.minecraft.chisel.client.render.BlockMarbleStairsRenderer;
import info.jbcs.minecraft.chisel.client.render.BlockNoCTMRenderer;
import info.jbcs.minecraft.chisel.client.render.BlockRoadLineRenderer;
import info.jbcs.minecraft.chisel.client.render.BlockSnakeStoneRenderer;
import info.jbcs.minecraft.chisel.client.render.BlockSpikesRenderer;
import info.jbcs.minecraft.chisel.client.render.BlockTexturedOreRenderer;
import info.jbcs.minecraft.chisel.client.render.ItemChiselRenderer;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    ItemChiselRenderer renderer = new ItemChiselRenderer();

    @Override
    public void preInit()
    {
    }

    @Override
    public void init()
    {
        RenderingRegistry.registerBlockHandler(new BlockMarbleStairsRenderer());
        RenderingRegistry.registerBlockHandler(new BlockMarblePaneRenderer());
        RenderingRegistry.registerBlockHandler(new BlockRoadLineRenderer());
        RenderingRegistry.registerBlockHandler(new BlockSnakeStoneRenderer());
        RenderingRegistry.registerBlockHandler(new BlockNoCTMRenderer());
        RenderingRegistry.registerBlockHandler(new BlockSpikesRenderer());
        RenderingRegistry.registerBlockHandler(new BlockMarblePillarRenderer());
        RenderingRegistry.registerBlockHandler(new BlockEldritchRenderer());
        RenderingRegistry.registerBlockHandler(new BlockAdvancedMarbleRenderer());
        RenderingRegistry.registerBlockHandler(new BlockCarpetRenderer());

        RenderingRegistry.registerBlockHandler(new BlockTexturedOreRenderer());

        RenderingRegistry.registerEntityRenderingHandler(EntityCloudInABottle.class, new RenderSnowball(Chisel.itemCloudInABottle));
        RenderingRegistry.registerEntityRenderingHandler(EntityBallOMoss.class, new RenderSnowball(Chisel.itemBallOMoss));

        MinecraftForgeClient.registerItemRenderer(Chisel.chisel, renderer);
    }
}
