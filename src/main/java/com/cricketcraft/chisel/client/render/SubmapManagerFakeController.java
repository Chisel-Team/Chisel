package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.init.ChiselBlocks;
import com.cricketcraft.ctmlib.CTM;
import com.cricketcraft.ctmlib.TextureSubmap;
import com.google.common.base.Optional;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static com.cricketcraft.ctmlib.Dir.*;

public class SubmapManagerFakeController extends SubmapManagerBase {

	private TextureSubmap map;
	private CTM ctm = CTM.getInstance();
	private int meta;
	private String texturePath;

	public SubmapManagerFakeController(int meta, String texture) {
		ctm.disableObscuredFaceCheck = Optional.of(true);
		this.meta = meta;
		texturePath = texture;
	}

	public SubmapManagerFakeController(int meta) {
		ctm.disableObscuredFaceCheck = Optional.of(true);
		this.meta = meta;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return map.getSubIcon(0, 0);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		ctm.buildConnectionMap(world, x, y, z, side, ChiselBlocks.futura, meta);

		if (ctm.connectedAnd(TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT)) {
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

		if (texturePath != null) {
			map = new TextureSubmap(register.registerIcon(modName + ":" + texturePath), 2, 2);
		} else {
			Chisel.logger.info("SubmapManagerFakeController was called on block " + block.getUnlocalizedName() + " without declaring texture, using a default texture instead.");
			map = new TextureSubmap(register.registerIcon(modName + ":futura/WIP/controller"), 2, 2);
		}
	}
}
