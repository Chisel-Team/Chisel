package team.chisel.client.render.tile;

import java.util.HashMap;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import team.chisel.Chisel;
import team.chisel.block.tileentity.TileEntityPresent;
import team.chisel.init.ChiselBlocks;

import com.google.common.collect.Maps;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderPresent extends TileEntitySpecialRenderer implements IItemRenderer {

	private ModelChest smallChest = new ModelChest();
	private ModelChest largeChest = new ModelLargeChest();

	private HashMap<Integer, ResourceLocation> textureCache = Maps.newHashMap();

	public void renderTileEntityAt(TileEntityPresent present, double x, double y, double z, float partialTicks) {
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		int rotation = present.getRotation();
		
		// math taken from vanilla chests
        float lidAngle = present.getPrevLidPos() + (present.getLidPos()- present.getPrevLidPos()) * partialTicks;
        lidAngle = 1.0F - lidAngle;
        lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
        
		bindTexture(present);
		if (!present.isConnected()) {
			GL11.glRotatef(90 * rotation + 180, 0, 1, 0);
			switch (rotation) {
			case 0:
				GL11.glTranslatef(-1, 0, -1);
				break;
			case 1:
				GL11.glTranslatef(0, 0, -1);
				break;
			case 3:
				GL11.glTranslatef(-1, 0, 0);
				break;
			}
			smallChest.chestLid.rotateAngleX = -(lidAngle * (float) Math.PI / 2.0F);
			smallChest.renderAll();
		} else if (present.isParent()) {
			ForgeDirection dir = present.getConnectionDir();
			switch (dir) {
			case NORTH:
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glTranslatef(0, 0, -1);
				break;
			case SOUTH:
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glTranslatef(-1, 0, -1);
				break;
			case WEST:
				GL11.glTranslatef(-1, 0, 0);
				break;
			default:
				break;
			}
			if (rotation == 0 || rotation == 3) {
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glTranslatef(-2, 0, -1);
			}
			largeChest.chestLid.rotateAngleX = -(lidAngle * (float) Math.PI / 2.0F);
			largeChest.renderAll();
		}
		
		smallChest.chestLid.rotateAngleX = 0;
		largeChest.chestLid.rotateAngleX = 0;
		GL11.glPopMatrix();
	}

	private void bindTexture(TileEntityPresent present) {
		int idx = present.getWorldObj().getBlockMetadata(present.xCoord, present.yCoord, present.zCoord) + (present.isConnected() ? 0 : 16);
		ResourceLocation rl = textureCache.get(idx);
		if (rl == null) {
			String res = ChiselBlocks.present.getModelTexture(idx % 16);
			res += present.isConnected() ? "_double.png" : ".png";
			rl = new ResourceLocation("chisel", res);
			textureCache.put(idx, rl);
		}
		bindTexture(rl);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
		this.renderTileEntityAt((TileEntityPresent) tileEntity, x, y, z, partialTicks);
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type != ItemRenderType.FIRST_PERSON_MAP;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(new ResourceLocation(Chisel.MOD_ID, ChiselBlocks.present.getModelTexture(item.getItemDamage()) + ".png"));
		switch (type) {
		case ENTITY:
			renderBlock(0.0F, 1.0F, 0.0F);
			break;
		case EQUIPPED:
			renderBlock(0.5F, 1.5F, 0.5F);
			break;
		case EQUIPPED_FIRST_PERSON:
			renderBlock(0.5F, 1.0F, 0.5F);
			break;
		case INVENTORY:
			renderInventory(1.0F, 1.0F, 1.0F);
			break;
		default:
			break;
		}
	}

	private void renderBlock(float x, float y, float z) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glScaled(-1, -1, 1);
		smallChest.renderAll();
		GL11.glPopMatrix();
	}

	private void renderInventory(float x, float y, float z) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(180F, 1F, 0, 0);
		GL11.glRotatef(-90F, 0, 1F, 0);
		GL11.glScalef(1.0F, 1.0F, 1.0F);
		smallChest.renderAll();
		GL11.glPopMatrix();
	}
}
