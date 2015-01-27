package com.cricketcraft.chisel.client.player;

import com.cricketcraft.chisel.client.GeneralChiselClient;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class PlayerSpecials {

	public PlayerSpecials() {
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderCricket(RenderPlayerEvent event) {
		if (!"TheCricket26".equals(event.entityPlayer.getDisplayName())) {
			return;
		}
		if (event.entityPlayer.isInvisible()) {
			return;
		}

		ModelRenderer body = null;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();

		GeneralChiselClient.spawnCricketFX(event.entityPlayer.getEntityWorld(), event.entityPlayer.posX, event.entityPlayer.posY, event.entityPlayer.posZ);

		GL11.glPopMatrix();
	}
}
