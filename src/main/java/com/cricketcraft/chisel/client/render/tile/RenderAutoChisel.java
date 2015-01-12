package com.cricketcraft.chisel.client.render.tile;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.client.model.obj.WavefrontObject;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

import static org.lwjgl.opengl.GL11.*;

public class RenderAutoChisel extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

	private static final WavefrontObject model = new WavefrontObject(new ResourceLocation(Chisel.MOD_ID, "models/autoChisel/autoChisel.obj"));
	private static final ResourceLocation texture = new ResourceLocation(Chisel.MOD_ID, "textures/blocks/autoChisel/autoChisel.png");

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return helper == ItemRendererHelper.INVENTORY_BLOCK || helper == ItemRendererHelper.ENTITY_BOBBING || helper == ItemRendererHelper.ENTITY_ROTATION;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
		case ENTITY:
			glTranslatef(-0.5f, 0, 0.5f);
			break;
		case EQUIPPED:
			glRotatef(20, 0, 1, 0);
			glRotatef(-15, 1, 0, -1);
			glScalef(0.65f, 0.65f, 0.65f);
			glTranslatef(0.75f, -0.5f, 1.25f);
			break;
		case EQUIPPED_FIRST_PERSON:
			glRotatef(20, 0, 0, 1);
			glRotatef(30, 0, 1, 0);
			glTranslatef(0.2f, 0, -0.1f);
			glScalef(0.5f, 0.5f, 0.5f);
			break;
		case FIRST_PERSON_MAP:
			break;
		case INVENTORY:
			glTranslatef(-0.5f, -0.5f, 0.5f);
			break;
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		model.renderAll();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Tessellator tes = Tessellator.instance;
		IIcon icon = block.getIcon(0, 0);
		tes.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		tes.setColorOpaque_F(1, 1, 1);
		tes.addTranslation(x, y, z + 1);
		for (GroupObject go : model.groupObjects) {
			for (Face f : go.faces) {
				Vertex n = f.faceNormal;
				tes.setNormal(n.x, n.y, n.z);
				for (int i = 0; i < f.vertices.length; i++) {
					Vertex v = f.vertices[i];
					TextureCoordinate t = f.textureCoordinates[i];
					tes.addVertexWithUV(v.x, v.y, v.z, icon.getInterpolatedU(t.u * 16), icon.getInterpolatedV(t.v * 16));
				}
			}
		}
		tes.addTranslation(-x, -y, -z - 1);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return Chisel.renderAutoChiselId;
	}

	private final RenderItem renderItem;

	public RenderAutoChisel() {
		Chisel.renderAutoChiselId = RenderingRegistry.getNextAvailableRenderId();

		renderItem = new RenderItem() {

			@Override
			public boolean shouldBob() {
				return false;
			}
		};
		renderItem.setRenderManager(RenderManager.instance);
	}

	private static final Random rand = new Random();

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float scale) {

		// Render Blocks
		TileEntityAutoChisel autoChisel = (TileEntityAutoChisel) tile;
		EntityItem item = autoChisel.getItemForRendering(TileEntityAutoChisel.TARGET);

		rand.setSeed(tile.xCoord + tile.yCoord + tile.zCoord);
		rand.nextBoolean();

		float max = 0.35f;

		if (!Minecraft.getMinecraft().isGamePaused()) {
			autoChisel.xRot += (rand.nextFloat() * max) - (max / 2);
			autoChisel.yRot += (rand.nextFloat() * max) - (max / 2);
			autoChisel.zRot += (rand.nextFloat() * max) - (max / 2);
		}

		if (item != null) {
			glPushMatrix();
			glPushAttrib(GL_ALL_ATTRIB_BITS);
			glTranslated(x + 0.5, y + 1.5, z + 0.5);
			glRotatef(autoChisel.xRot, 1, 0, 0);
			glRotatef(autoChisel.yRot, 0, 1, 0);
			glRotatef(autoChisel.zRot, 0, 0, 1);
			glEnable(GL_BLEND);
			glDepthMask(false);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_CONSTANT_ALPHA);
			renderItem.doRender(item, 0, 0, 0, 0, 0);
			glPopMatrix();
			glPopAttrib();
		}

		item = autoChisel.getItemForRendering(TileEntityAutoChisel.BASE);
		if (item != null) {
			glPushMatrix();
			glTranslated(x + 0.35, y + 0.934, z + 0.5);
			item.getEntityItem().stackSize = autoChisel.getLastBase() == null ? 1 : autoChisel.getLastBase().stackSize;
			renderItem.doRender(item, 0, 0, 0, 0, 0);
			glPopMatrix();
		}

		item = autoChisel.getItemForRendering(TileEntityAutoChisel.CHISEL);
		if (item != null) {
			glPushMatrix();
			glTranslated(x + 0.7, y + 1.01, z + 0.5);
			float rot = autoChisel.chiselRot == 0 ? 0 : autoChisel.chiseling ? autoChisel.chiselRot + (TileEntityAutoChisel.rotAmnt * scale) : autoChisel.chiselRot
					- (TileEntityAutoChisel.rotAmnt * scale);
			glRotatef(rot, 0, 0, 1);
			glTranslated(-0.12, 0, 0);
			glScalef(0.9f, 0.9f, 0.9f);
			renderItem.doRender(item, 0, 0, 0, 0, 0);
			glPopMatrix();
		} else {
			autoChisel.chiselRot = 0;
			autoChisel.chiseling = false;
		}
	}
}
