package com.cricketcraft.chisel.proxy;

import com.cricketcraft.chisel.block.BlockCarvableBeacon;
import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityCarvableBeacon;
import com.cricketcraft.chisel.block.tileentity.TileEntityPresent;
import com.cricketcraft.chisel.client.player.PlayerSpecials;
import com.cricketcraft.chisel.client.render.*;
import com.cricketcraft.chisel.client.render.item.ItemChiselRenderer;
import com.cricketcraft.chisel.client.render.tile.RenderAutoChisel;
import com.cricketcraft.chisel.client.render.tile.RenderCarvableBeacon;
import com.cricketcraft.chisel.client.render.tile.RenderPresent;
import com.cricketcraft.chisel.entity.EntityBallOMoss;
import com.cricketcraft.chisel.entity.EntityChiselSnowman;
import com.cricketcraft.chisel.entity.EntityCloudInABottle;
import com.cricketcraft.chisel.entity.EntitySmashingRock;
import com.cricketcraft.chisel.init.ChiselBlocks;
import com.cricketcraft.chisel.init.ChiselItems;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	ItemChiselRenderer renderer = new ItemChiselRenderer();
	
	@Override
	public void preInit() {
	}

	@Override
	public void init() {
		RenderingRegistry.registerBlockHandler(new RendererStairs());
		RenderingRegistry.registerBlockHandler(new RendererCTMPane());
		RenderingRegistry.registerBlockHandler(new RendererRoadLine());
		RenderingRegistry.registerBlockHandler(new RendererSnakeStone());
		RenderingRegistry.registerBlockHandler(new RendererEldritch());
		RenderingRegistry.registerBlockHandler(new RendererLayeredGlow());
		RenderingRegistry.registerBlockHandler(new RendererSimpleLayered());
		
		RenderingRegistry.registerBlockHandler(new RendererMultiLayer());

		RenderAutoChisel rac = new RenderAutoChisel();
		RenderingRegistry.registerBlockHandler(rac);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ChiselBlocks.autoChisel), rac);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoChisel.class, rac);
		
		RenderCarvableBeacon rcb = new RenderCarvableBeacon();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCarvableBeacon.class, rcb);
		RenderingRegistry.registerBlockHandler(rcb);
		BlockCarvableBeacon.renderId = rcb.getRenderId();
		
		RenderPresent rp = new RenderPresent();
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ChiselBlocks.present), rp);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPresent.class, rp);

		RenderingRegistry.registerEntityRenderingHandler(EntityCloudInABottle.class, new RenderSnowball(ChiselItems.cloudinabottle));
		RenderingRegistry.registerEntityRenderingHandler(EntityBallOMoss.class, new RenderSnowball(ChiselItems.ballomoss));
		RenderingRegistry.registerEntityRenderingHandler(EntitySmashingRock.class, new RenderSnowball(ChiselItems.smashingrock));
        RenderingRegistry.registerEntityRenderingHandler(EntityChiselSnowman.class, new RenderChiselSnowman());

		MinecraftForgeClient.registerItemRenderer(ChiselItems.chisel, renderer);
		MinecraftForgeClient.registerItemRenderer(ChiselItems.diamondChisel, renderer);

		PlayerSpecials.init();
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
