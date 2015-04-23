package com.cricketcraft.chisel.client.player;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.init.ChiselItems;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerSpecials {
	
	private interface IDevRenderer {
		
		void renderPlayer(EntityPlayer player, boolean post);
		
		void renderExtras(EntityPlayer player, boolean post);
	}
	
	private class RenderHolstered implements IDevRenderer {
		
		private ItemStack toRender;
		
		private RenderHolstered(ItemStack toRender) {
			this.toRender = toRender;
		}

		@Override
		public void renderExtras(EntityPlayer player, boolean post) {
			if (!post) {
				GL11.glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
				if (player.isSneaking()) {
					GL11.glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);
				}
				boolean armor = player.getCurrentArmor(1) != null;
				GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-0.25F, -0.85F, armor ? 0.2F : 0.28F);
				GL11.glScalef(0.5F, 0.5F, 0.5F);
				GL11.glColor3f(1.0F, 1.0F, 1.0F);
				int light = 0xF000F0;
				int lightmapX = light % 0x10000;
				int lightmapY = light / 0x10000;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);

				IIcon icon = toRender.getIconIndex();
				ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0F / 16.0F);
				GL11.glPopMatrix();
			}
		}
		
		@Override
		public void renderPlayer(EntityPlayer player, boolean post) {
		}
	}
	
	private class RenderTranslucent implements IDevRenderer	{
		
		@Override
		public void renderExtras(EntityPlayer player, boolean post) {	
		}
		
		@Override
		public void renderPlayer(EntityPlayer player, boolean post) {
			if (!post) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			} else {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_BLEND);
			}
		}
	}

	public static final PlayerSpecials INSTANCE = new PlayerSpecials();

	private Multimap<UUID, IDevRenderer> renderMap = ArrayListMultimap.create();

	private PlayerSpecials() {
		RenderHolstered backChisel = new RenderHolstered(new ItemStack(ChiselItems.obsidianChisel));
		RenderTranslucent translucent = new RenderTranslucent();

		renderMap.putAll(UUID.fromString("a7529984-8cb2-4fb9-b799-97980f770101"), Lists.newArrayList(backChisel, translucent)); // Cricket
        renderMap.putAll(UUID.fromString("a1d2532b-ee11-4ca3-b4c5-76e168d4c98e"), Lists.newArrayList(backChisel, translucent)); // TheCricket26
		renderMap.putAll(UUID.fromString("5399b615-3440-4c66-939d-ab1375952ac3"), Lists.newArrayList(backChisel, translucent)); // Drullkus

		renderMap.put(UUID.fromString("671516b1-4fb3-4c03-aa7c-9c88cfab3ae8"), new RenderHolstered(new ItemStack(ChiselItems.diamondChisel))); // tterrag

		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void init() {
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerRenderPre(RenderLivingEvent.Pre event) {
        if(nameIsGood(event.entity)){
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
	}

	@SubscribeEvent
	public void onPlayerRenderPost(RenderLivingEvent.Post event) {
        if(nameIsGood(event.entity)){
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
        }
	}

	@SubscribeEvent
	public void onPlayerRenderSpecialPre(RenderPlayerEvent.Specials.Pre event) {
		Collection<IDevRenderer> renders = renderMap.get(event.entityPlayer.getUniqueID());
		for (IDevRenderer r : renders) {
			r.renderExtras(event.entityPlayer, false);
		}
	}

	@SubscribeEvent
	public void onPlayerRenderSpecialPost(RenderPlayerEvent.Specials.Post event) {
		Collection<IDevRenderer> renders = renderMap.get(event.entityPlayer.getUniqueID());
		for (IDevRenderer r : renders) {
			r.renderExtras(event.entityPlayer, true);
		}
	}

    private boolean nameIsGood(Entity entity){
        if(EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCommandSenderName()).equals("Cricket") || EnumChatFormatting.getTextWithoutFormattingCodes(entity.getCommandSenderName()).equals("TheCricket26"))
            return true;
        return false;
    }
}
