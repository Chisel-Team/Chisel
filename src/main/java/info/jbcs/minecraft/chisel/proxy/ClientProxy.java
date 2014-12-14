package info.jbcs.minecraft.chisel.proxy;

import info.jbcs.minecraft.chisel.block.tileentity.TileEntityAutoChisel;
import info.jbcs.minecraft.chisel.block.tileentity.TileEntityPresent;
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
import info.jbcs.minecraft.chisel.client.render.item.ItemAutoChiselRenderer;
import info.jbcs.minecraft.chisel.client.render.item.ItemChiselRenderer;
import info.jbcs.minecraft.chisel.client.render.item.ItemPresentRenderer;
import info.jbcs.minecraft.chisel.client.render.tile.RenderAutoChisel;
import info.jbcs.minecraft.chisel.client.render.tile.RenderPresent;
import info.jbcs.minecraft.chisel.entity.EntityBallOMoss;
import info.jbcs.minecraft.chisel.entity.EntityCloudInABottle;
import info.jbcs.minecraft.chisel.entity.EntitySmashingRock;
import info.jbcs.minecraft.chisel.init.ModBlocks;
import info.jbcs.minecraft.chisel.init.ModItems;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
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

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoChisel.class, new RenderAutoChisel());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPresent.class, new RenderPresent());

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoChisel), new ItemAutoChiselRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.present), new ItemPresentRenderer());

        RenderingRegistry.registerEntityRenderingHandler(EntityCloudInABottle.class, new RenderSnowball(ModItems.itemCloudInABottle));
        RenderingRegistry.registerEntityRenderingHandler(EntityBallOMoss.class, new RenderSnowball(ModItems.itemBallOMoss));
        RenderingRegistry.registerEntityRenderingHandler(EntitySmashingRock.class, new RenderSnowball(ModItems.smashingRock));

        MinecraftForgeClient.registerItemRenderer(ModItems.chisel, renderer);
    }
}
