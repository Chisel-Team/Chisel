package com.cricketcraft.chisel.client.player;

import com.cricketcraft.chisel.init.ChiselItems;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class PlayerSpecials {

	private List<String> names = new ArrayList<String>();
	private boolean resetRender;

	public PlayerSpecials() {
		names.add("Cricket");
		names.add("Drullkus");
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderItem(RenderPlayerEvent.Specials.Post event) {
		EntityPlayer player = event.entityPlayer;

		for (int x = 0; x < names.size(); x++) {
			if (player.getCommandSenderName().equals(names.get(x))) {
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
				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);

				IIcon icon = ChiselItems.obsidianChisel.getIcon(new ItemStack(ChiselItems.obsidianChisel, 1, 0), 1);
				ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0F / 16.0F);
				GL11.glPopMatrix();
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderDev(RenderLivingEvent.Pre event) {
		for (int x = 0; x < names.size(); x++) {
			if (EnumChatFormatting.getTextWithoutFormattingCodes(event.entity.getCommandSenderName()).equals(names.get(x))) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				resetRender = true;
			}
		}
	}

	@SubscribeEvent
	public void entityColorRender(RenderLivingEvent.Post event) {
		if (this.resetRender) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(3042);
			resetRender = false;
		}
	}
}
