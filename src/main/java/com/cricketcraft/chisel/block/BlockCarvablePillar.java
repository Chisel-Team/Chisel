package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.api.carving.IVariationInfo;
import com.cricketcraft.chisel.client.render.RendererPillar;

public class BlockCarvablePillar extends BlockCarvable {

	public IIcon sides[] = new IIcon[6];

	public BlockCarvablePillar(Material m) {
		super(m);
	}

	@Override
	public int getRenderType() {
		return RendererPillar.id;
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return sides[side];
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return sides[side];
	}

	public IIcon getCtmIcon(int index, int metadata) {
		// Workaround to prevent crash with invalid meta
		IVariationInfo var = null;
		if (metadata <= carverHelper.infoList.size())
			var = carverHelper.infoList.get(metadata);
		else
			var = carverHelper.infoList.get(0);
		return var.getSubmapManager().getIcon(0, metadata);

		// TODO fix pillars
//		if (index >= 4)
//			return var.iconTop;
//		if (var.seamsCtmVert == null)
//			return var.icon;
//		return var.seamsCtmVert.getSubIcon(index % 2, index / 2);
	}

}
