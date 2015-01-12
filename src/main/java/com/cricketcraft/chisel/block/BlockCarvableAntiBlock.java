package com.cricketcraft.chisel.block;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class BlockCarvableAntiBlock extends BlockCarvable implements ICarvable {

	public CarvableHelper carverHelper;

	public BlockCarvableAntiBlock() {
		super(Material.rock);
		carverHelper = new CarvableHelper();
	}

	@Override
	public int getRenderType() {
		return Chisel.renderCTMNoLightId;
	}

	/*
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	*/
}
