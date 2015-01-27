package com.cricketcraft.chisel.api.carving;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IChiselMode {

	void chiselAll(EntityPlayer player, World world, int x, int y, int z, ForgeDirection side, ICarvingVariation variation);

	/**
	 * Implemented implicitly by enums. If your IChiselMode is not an enum
	 * constant, this needs to be implemented explicitly.
	 * 
	 * @return The name of the mode.
	 */
	String name();
}
