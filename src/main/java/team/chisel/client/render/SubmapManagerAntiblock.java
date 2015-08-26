package team.chisel.client.render;

import org.lwjgl.opengl.GL11;

import team.chisel.ctmlib.Drawing;
import team.chisel.ctmlib.RenderBlocksCTM;
import team.chisel.ctmlib.TextureSubmap;
import team.chisel.init.ChiselBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubmapManagerAntiblock extends SubmapManagerBase {
	
	@SideOnly(Side.CLIENT)
	public static class AntiblockItemRenderer implements IItemRenderer {

		@Override
		public boolean handleRenderType(ItemStack item, ItemRenderType type) {
			return true;
		}

		@Override
		public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
			return true;
		}

		@Override
		public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
			GL11.glDisable(GL11.GL_LIGHTING);
			Drawing.drawBlock(ChiselBlocks.antiBlock, ChiselBlocks.antiBlock.getIcon(0, item.getItemDamage()), (RenderBlocks) data[0]);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}

	@SideOnly(Side.CLIENT)
	private static class RenderBlocksCTMFullbright extends RenderBlocksCTM {

		@Override
		protected void fillLightmap(int bottomLeft, int bottomRight, int topRight, int topLeft) {
			ao();
			int maxLight = 0xF000F0;
			super.fillLightmap(maxLight, maxLight, maxLight, maxLight);
		}

		@Override
		protected void fillColormap(float bottomLeft, float bottomRight, float topRight, float topLeft, float[][] map) {
			ao();
			int color = 0xFFFFFF;
			super.fillColormap(color, color, color, color, map);
		}
		
		@Override
		public boolean renderStandardBlock(Block block, int x, int y, int z) {
			boolean ret = super.renderStandardBlock(block, x, y, z);
			this.enableAO = false;
			return ret;
		}
		
		private void ao() {
			if (this.inWorld) {
				this.enableAO = true;
			}
		}
	};

	@SideOnly(Side.CLIENT)
	private static RenderBlocksCTMFullbright rb;

	private String color;
	private TextureSubmap submap, submapSmall;

	public SubmapManagerAntiblock(String color) {
		this.color = color;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return submapSmall.getBaseIcon();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, Block block, IIconRegister register) {
		submap = new TextureSubmap(register.registerIcon(modName + ":antiblock/" + color + "-antiBlock-ctm"), 4, 4);
		submapSmall = new TextureSubmap(register.registerIcon(modName + ":antiblock/" + color + "-antiBlock"), 2, 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, Block block, IBlockAccess world) {
		if (rb == null) {
			rb = new RenderBlocksCTMFullbright();
		}
		rb.setRenderBoundsFromBlock(block);
		rb.submap = submap;
		rb.submapSmall = submapSmall;
		return rb;
	}
}
