package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.api.client.CTM;
import com.cricketcraft.chisel.api.client.ISubmapManager;
import com.cricketcraft.chisel.init.ChiselBlocks;

import static com.cricketcraft.chisel.api.client.Dir.*;

public class SubmapManagerFakeController implements ISubmapManager {

	private TextureSubmap map;
	private CTM ctm = CTM.getInstance();

	public SubmapManagerFakeController() {
		ctm.disableObscuredFaceCheck = true;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return map.icons[0];
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		ctm.buildConnectionMap(world, x, y, z, side, ChiselBlocks.futura, 2);
		if (ctm.connectedAnd(TOP, BOTTOM, LEFT, RIGHT)) {
			return map.icons[3];
		} else if (ctm.connectedAnd(TOP, BOTTOM)) {
			return map.icons[2];
		} else if (ctm.connectedAnd(LEFT, RIGHT)) {
			return map.icons[1];
		} else {
			return map.icons[0];
		}
	}

	@Override
	public void registerIcons(String modName, Block block, IIconRegister register) {
		map = new TextureSubmap(register.registerIcon(modName + ":futura/WIP/controllerWIP-ctm"), 2, 2);
	}
}
