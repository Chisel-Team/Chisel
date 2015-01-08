package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.client.render.BlockMarblePillarRenderer;

public class BlockMarblePillar extends BlockCarvable {

	public IIcon sides[] = new IIcon[6];

	public BlockMarblePillar(Material m) {
		super(m);
	}

	@Override
	public int getRenderType() {
		return BlockMarblePillarRenderer.id;
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
		CarvableVariation var = carverHelper.variations.get(metadata);

		if (index >= 4)
			return var.iconTop;
		if (var.seamsCtmVert == null)
			return var.icon;
		return var.seamsCtmVert.icons[index];
	}

}
