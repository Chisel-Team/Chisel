package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.api.rendering.CTM;
import com.cricketcraft.chisel.api.rendering.TextureSubmap;
import com.cricketcraft.chisel.init.ChiselBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static com.cricketcraft.chisel.api.rendering.Dir.*;

public class SubmapManagerFakeController extends SubmapManagerBase {

	private TextureSubmap map;
	private CTM ctm = CTM.getInstance();
	private int meta;

	public SubmapManagerFakeController(int meta) {
		ctm.disableObscuredFaceCheck = true;
		this.meta = meta;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return map.getSubIcon(0, 0);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		ctm.buildConnectionMap(world, x, y, z, side, ChiselBlocks.futura, meta);
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
	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, Block block, IIconRegister register) {
		if(meta == 2){
			map = new TextureSubmap(register.registerIcon(modName + ":futura/WIP/controller"), 2, 2);
		} else {
			map = new TextureSubmap(register.registerIcon(modName + ":futura/WIP/controllerPurple"), 2, 2);
		}
	}
}
