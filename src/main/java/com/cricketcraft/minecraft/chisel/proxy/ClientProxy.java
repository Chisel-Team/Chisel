package com.cricketcraft.minecraft.chisel.proxy;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.cricketcraft.minecraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.minecraft.chisel.block.tileentity.TileEntityPresent;
import com.cricketcraft.minecraft.chisel.client.render.BlockAdvancedMarbleRenderer;
import com.cricketcraft.minecraft.chisel.client.render.BlockCarpetRenderer;
import com.cricketcraft.minecraft.chisel.client.render.BlockEldritchRenderer;
import com.cricketcraft.minecraft.chisel.client.render.BlockMarblePaneRenderer;
import com.cricketcraft.minecraft.chisel.client.render.BlockMarblePillarRenderer;
import com.cricketcraft.minecraft.chisel.client.render.BlockMarbleStairsRenderer;
import com.cricketcraft.minecraft.chisel.client.render.BlockNoCTMRenderer;
import com.cricketcraft.minecraft.chisel.client.render.BlockRoadLineRenderer;
import com.cricketcraft.minecraft.chisel.client.render.BlockSnakeStoneRenderer;
import com.cricketcraft.minecraft.chisel.client.render.BlockSpikesRenderer;
import com.cricketcraft.minecraft.chisel.client.render.BlockTexturedOreRenderer;
import com.cricketcraft.minecraft.chisel.client.render.item.ItemAutoChiselRenderer;
import com.cricketcraft.minecraft.chisel.client.render.item.ItemChiselRenderer;
import com.cricketcraft.minecraft.chisel.client.render.item.ItemPresentRenderer;
import com.cricketcraft.minecraft.chisel.client.render.tile.RenderAutoChisel;
import com.cricketcraft.minecraft.chisel.client.render.tile.RenderPresent;
import com.cricketcraft.minecraft.chisel.entity.EntityBallOMoss;
import com.cricketcraft.minecraft.chisel.entity.EntityCloudInABottle;
import com.cricketcraft.minecraft.chisel.entity.EntitySmashingRock;
import com.cricketcraft.minecraft.chisel.init.ModBlocks;
import com.cricketcraft.minecraft.chisel.init.ModItems;

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
