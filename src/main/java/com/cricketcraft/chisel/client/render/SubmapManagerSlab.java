package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.rendering.ISubmapManager;
import com.cricketcraft.chisel.api.rendering.TextureSubmap;

public class SubmapManagerSlab implements ISubmapManager<RenderBlocksCTM> {

	private static RenderBlocksCTM rb = new RenderBlocksCTM();

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
	public void registerIcons(String modName, Block block, IIconRegister register) {
		String path = modName + ":" + texture;
		submap = new TextureSubmap(register.registerIcon(path + "-ctm"), 4, 4);
		submapSmall = new TextureSubmap(register.registerIcon(path), 2, 2);
		sideTexture = register.registerIcon(path + "-side");
	}

	@Override
	public RenderBlocksCTM createRenderContext(RenderBlocks rendererOld, IBlockAccess world) {
		rb.renderMaxY = 1f / 2f;
		rb.submap = submap;
		rb.submapSmall = submapSmall;
		return rb;
	}
	
	private boolean hadOverride = false;

	@Override
	public void preRenderSide(RenderBlocksCTM renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		if (side.ordinal() < 2 && !renderer.rendererOld.hasOverrideBlockTexture()) {
			renderer.rendererOld.setOverrideBlockTexture(sideTexture);
			hadOverride = false;
		} else {
			hadOverride = true;
		}
	}

	@Override
	public void postRenderSide(RenderBlocksCTM renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		if (!hadOverride) {
			renderer.rendererOld.clearOverrideBlockTexture();
		}
	}
}
