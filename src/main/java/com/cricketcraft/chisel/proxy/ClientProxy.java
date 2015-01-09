package com.cricketcraft.chisel.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityPresent;
import com.cricketcraft.chisel.client.render.BlockAdvancedMarbleRenderer;
import com.cricketcraft.chisel.client.render.BlockCarpetRenderer;
import com.cricketcraft.chisel.client.render.BlockEldritchRenderer;
import com.cricketcraft.chisel.client.render.BlockGlowRenderer;
import com.cricketcraft.chisel.client.render.BlockMarblePaneRenderer;
import com.cricketcraft.chisel.client.render.BlockMarblePillarRenderer;
import com.cricketcraft.chisel.client.render.BlockMarbleStairsRenderer;
import com.cricketcraft.chisel.client.render.renderStandardBlock_noSunlight;
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
import com.cricketcraft.chisel.init.ChiselBlocks;
import com.cricketcraft.chisel.init.ChiselItems;

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
		RenderingRegistry.registerBlockHandler(new renderStandardBlock_noSunlight());
		RenderingRegistry.registerBlockHandler(new BlockMarblePillarRenderer());
		RenderingRegistry.registerBlockHandler(new BlockEldritchRenderer());
		RenderingRegistry.registerBlockHandler(new BlockAdvancedMarbleRenderer());
		RenderingRegistry.registerBlockHandler(new BlockCarpetRenderer());
		RenderingRegistry.registerBlockHandler(new BlockGlowRenderer());

		RenderingRegistry.registerBlockHandler(new BlockTexturedOreRenderer());

		RenderAutoChisel rac = new RenderAutoChisel();
		RenderingRegistry.registerBlockHandler(rac);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ChiselBlocks.autoChisel), rac);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoChisel.class, rac);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPresent.class, new RenderPresent());

		for (int x = 0; x < 16; x++) {
			MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ChiselBlocks.present[x]), new ItemPresentRenderer(x));
		}

		RenderingRegistry.registerEntityRenderingHandler(EntityCloudInABottle.class, new RenderSnowball(ChiselItems.cloudinabottle));
		RenderingRegistry.registerEntityRenderingHandler(EntityBallOMoss.class, new RenderSnowball(ChiselItems.ballomoss));
		RenderingRegistry.registerEntityRenderingHandler(EntitySmashingRock.class, new RenderSnowball(ChiselItems.smashingrock));

		MinecraftForgeClient.registerItemRenderer(ChiselItems.chisel, renderer);
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getMinecraft().theWorld;
	}
}
