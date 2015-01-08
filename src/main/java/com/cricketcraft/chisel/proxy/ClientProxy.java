package com.cricketcraft.chisel.proxy;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityPresent;
import com.cricketcraft.chisel.client.render.BlockAdvancedMarbleRenderer;
import com.cricketcraft.chisel.client.render.BlockCarpetRenderer;
import com.cricketcraft.chisel.client.render.BlockEldritchRenderer;
import com.cricketcraft.chisel.client.render.BlockMarblePaneRenderer;
import com.cricketcraft.chisel.client.render.BlockMarblePillarRenderer;
import com.cricketcraft.chisel.client.render.BlockMarbleStairsRenderer;
import com.cricketcraft.chisel.client.render.BlockNoCTMRenderer;
import com.cricketcraft.chisel.client.render.BlockRoadLineRenderer;
import com.cricketcraft.chisel.client.render.BlockSnakeStoneRenderer;
import com.cricketcraft.chisel.client.render.BlockTexturedOreRenderer;
import com.cricketcraft.chisel.client.render.item.ItemChiselRenderer;
import com.cricketcraft.chisel.client.render.item.ItemPresentRenderer;
import com.cricketcraft.chisel.client.render.tile.RenderAutoChisel;
import com.cricketcraft.chisel.client.render.tile.RenderPresent;
import com.cricketcraft.chisel.entity.EntityBallOMoss;
import com.cricketcraft.chisel.entity.EntityCloudInABottle;
import com.cricketcraft.chisel.entity.EntitySmashingRock;
import com.cricketcraft.chisel.init.ModBlocks;
import com.cricketcraft.chisel.init.ModItems;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	ItemChiselRenderer renderer = new ItemChiselRenderer();

	@Override
	public void preInit() {
	}

	@Override
	public void init() {
		RenderingRegistry.registerBlockHandler(new BlockMarbleStairsRenderer());
		RenderingRegistry.registerBlockHandler(new BlockMarblePaneRenderer());
		RenderingRegistry.registerBlockHandler(new BlockRoadLineRenderer());
		RenderingRegistry.registerBlockHandler(new BlockSnakeStoneRenderer());
		RenderingRegistry.registerBlockHandler(new BlockNoCTMRenderer());
		RenderingRegistry.registerBlockHandler(new BlockMarblePillarRenderer());
		RenderingRegistry.registerBlockHandler(new BlockEldritchRenderer());
		RenderingRegistry.registerBlockHandler(new BlockAdvancedMarbleRenderer());
		RenderingRegistry.registerBlockHandler(new BlockCarpetRenderer());

		RenderingRegistry.registerBlockHandler(new BlockTexturedOreRenderer());

		RenderAutoChisel rac = new RenderAutoChisel();
		Chisel.renderAutoChiselId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(rac);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoChisel), rac);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoChisel.class, rac);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPresent.class, new RenderPresent());

		for (int x = 0; x < 16; x++) {
			MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.present[x]), new ItemPresentRenderer(x));
		}

		RenderingRegistry.registerEntityRenderingHandler(EntityCloudInABottle.class, new RenderSnowball(ModItems.itemCloudInABottle));
		RenderingRegistry.registerEntityRenderingHandler(EntityBallOMoss.class, new RenderSnowball(ModItems.itemBallOMoss));
		RenderingRegistry.registerEntityRenderingHandler(EntitySmashingRock.class, new RenderSnowball(ModItems.smashingRock));

		MinecraftForgeClient.registerItemRenderer(ModItems.chisel, renderer);
	}
}
