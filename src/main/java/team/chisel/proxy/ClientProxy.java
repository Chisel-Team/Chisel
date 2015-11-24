package team.chisel.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import team.chisel.Features;
import team.chisel.block.BlockCarvableBeacon;
import team.chisel.block.tileentity.TileEntityAutoChisel;
import team.chisel.block.tileentity.TileEntityCarvableBeacon;
import team.chisel.block.tileentity.TileEntityPresent;
import team.chisel.client.ClientTickHandler;
import team.chisel.client.render.*;
import team.chisel.client.render.item.ItemChiselRenderer;
import team.chisel.client.render.item.ItemStarFieldRenderer;
import team.chisel.client.render.tile.RenderAutoChisel;
import team.chisel.client.render.tile.RenderCarvableBeacon;
import team.chisel.client.render.tile.RenderPresent;
import team.chisel.entity.EntityBallOMoss;
import team.chisel.entity.EntityChiselSnowman;
import team.chisel.entity.EntityCloudInABottle;
import team.chisel.entity.EntitySmashingRock;
import team.chisel.init.ChiselBlocks;
import team.chisel.init.ChiselItems;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	ItemChiselRenderer renderer = new ItemChiselRenderer();

	@Override
	public void preInit() {
	}

	@Override
	public void init() {
		RenderingRegistry.registerBlockHandler(new RendererCTM());
		RenderingRegistry.registerBlockHandler(new RendererStairs());
		RenderingRegistry.registerBlockHandler(new RendererCTMPane());
		RenderingRegistry.registerBlockHandler(new RendererRoadLine());
		RenderingRegistry.registerBlockHandler(new RendererSnakeStone());
		RenderingRegistry.registerBlockHandler(new RendererEldritch());
		RenderingRegistry.registerBlockHandler(new RendererLayeredGlow());
		RenderingRegistry.registerBlockHandler(new RendererSimpleLayered());
		RenderingRegistry.registerBlockHandler(new RenderRoofing());
		RenderingRegistry.registerBlockHandler(new RendererMultiLayer());

		if (Features.AUTO_CHISEL.enabled()) {
			RenderAutoChisel rac = new RenderAutoChisel();
			RenderingRegistry.registerBlockHandler(rac);
			MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ChiselBlocks.autoChisel), rac);
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoChisel.class, rac);
		}

		if (Features.BEACON.enabled()) {
			RenderCarvableBeacon rcb = new RenderCarvableBeacon();
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCarvableBeacon.class, rcb);
			RenderingRegistry.registerBlockHandler(rcb);
			BlockCarvableBeacon.renderId = rcb.getRenderId();
		}

		if (Features.PRESENT.enabled()) {
			RenderPresent rp = new RenderPresent();
			MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ChiselBlocks.present), rp);
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPresent.class, rp);
		}

		if (Features.CLOUD.enabled()) {
			RenderingRegistry.registerEntityRenderingHandler(EntityCloudInABottle.class, new RenderSnowball(ChiselItems.cloudinabottle));
		}
		if (Features.BALL_OF_MOSS.enabled()) {
			RenderingRegistry.registerEntityRenderingHandler(EntityBallOMoss.class, new RenderSnowball(ChiselItems.ballomoss));
		}
		if (Features.SMASHING_ROCK.enabled()) {
			RenderingRegistry.registerEntityRenderingHandler(EntitySmashingRock.class, new RenderSnowball(ChiselItems.smashingrock));
		}
		if (Features.PUMPKIN.enabled()) {
			RenderingRegistry.registerEntityRenderingHandler(EntityChiselSnowman.class, new RenderChiselSnowman());
		}

		if (Features.CHISEL.enabled()) {
			MinecraftForgeClient.registerItemRenderer(ChiselItems.chisel, renderer);
			MinecraftForgeClient.registerItemRenderer(ChiselItems.diamondChisel, renderer);
		}

		ItemStarFieldRenderer enderStarField = new ItemStarFieldRenderer();
		if (Features.OFFSET_TOOL.enabled()) {
			MinecraftForge.EVENT_BUS.register(ChiselItems.offsettool);
			MinecraftForgeClient.registerItemRenderer(ChiselItems.offsettool, enderStarField);
		}

        DevPlayer.init();

//		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ChiselBlocks.antiBlock), new SubmapManagerAntiblock.AntiblockItemRenderer());

        MinecraftForge.EVENT_BUS.register(new InterpolatedIcon.RegistrationHandler());

        FMLCommonHandler.instance().bus().register(new ClientTickHandler());

		ShaderHelper.initShaders();
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
