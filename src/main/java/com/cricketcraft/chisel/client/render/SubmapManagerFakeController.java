package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.rendering.CTM;
import com.cricketcraft.chisel.api.rendering.ISubmapManager;
import com.cricketcraft.chisel.api.rendering.TextureSubmap;
import com.cricketcraft.chisel.init.ChiselBlocks;

import static com.cricketcraft.chisel.api.rendering.Dir.*;

public class SubmapManagerFakeController implements ISubmapManager<RenderBlocks> {

	private TextureSubmap map;
	private CTM ctm = CTM.getInstance();

	public SubmapManagerFakeController() {
		ctm.disableObscuredFaceCheck = true;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return map.getSubIcon(0, 0);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		ctm.buildConnectionMap(world, x, y, z, side, ChiselBlocks.futura, 2);
		if (ctm.connectedAnd(TOP, BOTTOM, LEFT, RIGHT)) {
			return map.getSubIcon(1, 1);
		} else if (ctm.connectedAnd(TOP, BOTTOM)) {
			return map.getSubIcon(0, 1);
		} else if (ctm.connectedAnd(LEFT, RIGHT)) {
			return map.getSubIcon(1, 0);
		} else {
			return map.getSubIcon(0, 0);
		}
	}

	@Override
	public void registerIcons(String modName, Block block, IIconRegister register) {
		map = new TextureSubmap(register.registerIcon(modName + ":futura/WIP/controllerWIP-ctm"), 2, 2);
	}

	@Override
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, IBlockAccess world) {
		return rendererOld;
	}

	@Override
	public void preRenderSide(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
	}

	@Override
	public void postRenderSide(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
	}
}
