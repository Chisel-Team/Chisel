package team.chisel.client.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import team.chisel.api.rendering.IShaderRenderItem;

public class ItemStarFieldRenderer implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Minecraft mc = Minecraft.getMinecraft();
		this.processLightLevel(type, item, data);
		switch (type) {
		case ENTITY: {
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.5F, 0F, 0F);
			if (item.isOnItemFrame())
				GL11.glTranslatef(0F, -0.3F, 0.01F);
			render(item, null);
			GL11.glPopMatrix();

			break;
		}
		case EQUIPPED: {
			render(item, data[1] instanceof EntityPlayer ? (EntityPlayer) data[1] : null);
			break;
		}
		case EQUIPPED_FIRST_PERSON: {
			render(item, data[1] instanceof EntityPlayer ? (EntityPlayer) data[1] : null);
			break;
		}
		case INVENTORY: {
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderHelper.enableGUIStandardItemLighting();

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			RenderItem r = RenderItem.getInstance();
			r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);

			if (item.getItem() instanceof IShaderRenderItem) {
				GL11.glDepthFunc(GL11.GL_EQUAL);
				GL11.glDepthMask(false);
				IShaderRenderItem icri = (IShaderRenderItem) (item.getItem());

				StarFieldRendererHelper.cosmicOpacity = icri.getMaskMultiplier(item, null);
				StarFieldRendererHelper.inventoryRender = true;
				StarFieldRendererHelper.useShader();

				GL11.glColor4d(1, 1, 1, 1);

				Tessellator t = Tessellator.instance;
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/entity/end_portal.png"));

				t.startDrawingQuads();
				t.addVertex(0, 0, 0);
				t.addVertex(0, 16, 0);
				t.addVertex(16, 16, 0);
				t.addVertex(16, 0, 0);
				t.draw();

				StarFieldRendererHelper.releaseShader();
				StarFieldRendererHelper.inventoryRender = false;

				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);

				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);

				IIcon overlay = ((IShaderRenderItem) item.getItem()).getOverlayTexture(item, mc.thePlayer);

				float minu = overlay.getMinU();
				float minv = overlay.getMinV();
				float maxu = overlay.getMaxU();
				float maxv = overlay.getMaxV();

				GL11.glDisable(GL11.GL_LIGHTING);
				t.startDrawingQuads();
				t.addVertexWithUV(0, 0, 0, minu, minv);
				t.addVertexWithUV(0, 16, 0, minu, maxv);
				t.addVertexWithUV(16, 16, 0, maxu, maxv);
				t.addVertexWithUV(16, 0, 0, maxu, minv);
				t.draw();
				GL11.glEnable(GL11.GL_LIGHTING);

				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDepthMask(true);
			
			r.renderWithColor = true;

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			break;
		}
		default:
			break;
		}
	}

	public void render(ItemStack item, EntityPlayer player) {

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		float r, g, b;
		IIcon icon;
		float f, f1, f2, f3;
		float scale = 1F / 16F;

		icon = this.getStackIcon(item);

		f = icon.getMinU();
		f1 = icon.getMaxU();
		f2 = icon.getMinV();
		f3 = icon.getMaxV();

		int colour = item.getItem().getColorFromItemStack(item, 0);
		r = (float) (colour >> 16 & 255) / 255.0F;
		g = (float) (colour >> 8 & 255) / 255.0F;
		b = (float) (colour & 255) / 255.0F;
		GL11.glColor4f(r, g, b, 1.0F);
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		if (item.getItem() instanceof IShaderRenderItem) {
			GL11.glDepthFunc(GL11.GL_EQUAL);
			GL11.glDepthMask(false);
			IShaderRenderItem icri = (IShaderRenderItem) (item.getItem());

			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/entity/end_portal.png"));

			StarFieldRendererHelper.cosmicOpacity = icri.getMaskMultiplier(item, player);
			StarFieldRendererHelper.useShader();

			ItemRenderer.renderItemIn2D(Tessellator.instance, 0, 0, 0, 0, 0, 0, scale);
			StarFieldRendererHelper.releaseShader();

			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);

			icon = ((IShaderRenderItem) item.getItem()).getOverlayTexture(item, player);

			f = icon.getMinU();
			f1 = icon.getMaxU();
			f2 = icon.getMinV();
			f3 = icon.getMaxV();

			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);

			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDepthMask(true);
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();

		GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	public void processLightLevel(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
		case ENTITY: {
			EntityItem ent = (EntityItem) (data[1]);
			if (ent != null) {
				StarFieldRendererHelper.setLightFromLocation(ent.worldObj, MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ));
			}
			break;
		}
		case EQUIPPED: {
			EntityLivingBase ent = (EntityLivingBase) (data[1]);
			if (ent != null) {
				StarFieldRendererHelper.setLightFromLocation(ent.worldObj, MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ));
			}
			break;
		}
		case EQUIPPED_FIRST_PERSON: {
			EntityLivingBase ent = (EntityLivingBase) (data[1]);
			if (ent != null) {
				StarFieldRendererHelper.setLightFromLocation(ent.worldObj, MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ));
			}
			break;
		}
		case INVENTORY: {
			StarFieldRendererHelper.setLightLevel(1.2f);
			return;
		}
		default: {
			StarFieldRendererHelper.setLightLevel(1.0f);
			return;
		}
		}
	}

	public IIcon getStackIcon(ItemStack stack) {
		return stack.getItem().getIcon(stack, 0);
	}

}
