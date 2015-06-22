package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.ctmlib.ISubmapManager;
import com.cricketcraft.ctmlib.RenderBlocksCTM;
import com.cricketcraft.ctmlib.TextureSubmap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubmapManagerSlab implements ISubmapManager {

	@SideOnly(Side.CLIENT)
	private static RenderBlocksCTM rb;

	private TextureSubmap submap;
	private TextureSubmap submapSmall;
	private IIcon sideTexture;
	private String texture;

	public SubmapManagerSlab(String texture) {
		this.texture = texture;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return submapSmall.getBaseIcon();
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return getIcon(side, world.getBlockMetadata(x, y, z));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, Block block, IIconRegister register) {
		String path = modName + ":" + texture;
		submap = new TextureSubmap(register.registerIcon(path + "-ctm"), 4, 4);
		submapSmall = new TextureSubmap(register.registerIcon(path), 2, 2);
		sideTexture = register.registerIcon(path + "-side");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, Block block, IBlockAccess world) {
		if (rb == null) {
			rb = new RenderBlocksCTM();
		}
		rb.setRenderBoundsFromBlock(block);
		rb.submap = submap;
		rb.submapSmall = submapSmall;
		return rb;
	}
	
	private boolean hadOverride = false;

	@Override
	@SideOnly(Side.CLIENT)
	public void preRenderSide(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		RenderBlocksCTM rbctm = (RenderBlocksCTM) renderer;
		if (side.ordinal() < 2 && !rbctm.rendererOld.hasOverrideBlockTexture()) {
			rbctm.rendererOld.setOverrideBlockTexture(sideTexture);
			hadOverride = false;
		} else {
			hadOverride = true;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void postRenderSide(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		if (!hadOverride) {
			((RenderBlocksCTM) renderer).rendererOld.clearOverrideBlockTexture();
		}
	}
}
