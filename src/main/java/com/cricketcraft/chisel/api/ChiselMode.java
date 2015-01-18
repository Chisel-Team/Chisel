package com.cricketcraft.chisel.api;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.cricketcraft.chisel.network.PacketHandler;
import com.cricketcraft.chisel.network.message.MessageChiselSound;

public enum ChiselMode {

	SINGLE {

		@Override
		public void chiselAll(EntityPlayer player, World world, int x, int y, int z, ForgeDirection side, ICarvingVariation variation) {
			setVariation(player, world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), variation);
		}
	},
	PANEL {

		@Override
		public void chiselAll(EntityPlayer player, World world, int x, int y, int z, ForgeDirection side, ICarvingVariation variation) {
			Block block = world.getBlock(x, y, z);
			int meta = world.getBlockMetadata(x, y, z);
			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					if (side == ForgeDirection.UP || side == ForgeDirection.DOWN) {
						setVariation(player, world, x + dx, y, z + dy, block, meta, variation);
					} else if (side == ForgeDirection.EAST || side == ForgeDirection.WEST) {
						setVariation(player, world, x, y + dy, z + dx, block, meta, variation);
					} else {
						setVariation(player, world, x + dx, y + dy, z, block, meta, variation);
					}
				}
			}
		}
	},
	COLUMN {

		@Override
		public void chiselAll(EntityPlayer player, World world, int x, int y, int z, ForgeDirection side, ICarvingVariation variation) {
			Block block = world.getBlock(x, y, z);
			int meta = world.getBlockMetadata(x, y, z);
			int facing = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			for (int i = -1; i <= 1; i++) {
				if (side != ForgeDirection.DOWN && side != ForgeDirection.UP) {
					setVariation(player, world, x, y + i, z, block, meta, variation);
				} else {
					if (facing == 0 || facing == 2) {
						setVariation(player, world, x, y, z + i, block, meta, variation);
					} else {
						setVariation(player, world, x + i, y, z, block, meta, variation);
					}
				}
			}
		}
	},
	ROW {

		@Override
		public void chiselAll(EntityPlayer player, World world, int x, int y, int z, ForgeDirection side, ICarvingVariation variation) {
			Block block = world.getBlock(x, y, z);
			int meta = world.getBlockMetadata(x, y, z);
			int facing = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			for (int i = -1; i <= 1; i++) {
				if (side != ForgeDirection.DOWN && side != ForgeDirection.UP) {
					if (side == ForgeDirection.EAST || side == ForgeDirection.WEST) {
						setVariation(player, world, x, y, z + i, block, meta, variation);
					} else {
						setVariation(player, world, x + i, y, z, block, meta, variation);
					}
				} else {
					if (facing == 0 || facing == 2) {
						setVariation(player, world, x + i, y, z, block, meta, variation);
					} else {
						setVariation(player, world, x, y, z + i, block, meta, variation);
					}
				}
			}
		}
	};

	public abstract void chiselAll(EntityPlayer player, World world, int x, int y, int z, ForgeDirection side, ICarvingVariation variation);

	/**
	 * Assumes that the player is holding a chisel
	 */
	private static void setVariation(EntityPlayer player, World world, int x, int y, int z, Block origBlock, int origMeta, ICarvingVariation v) {
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		if (block == v.getBlock() && meta == v.getBlockMeta()) {
			return; // don't chisel to the same thing
		}
		if (origBlock != block || origMeta != meta) {
			return; // don't chisel if this doesn't match the target block (for the AOE modes)
		}

		PacketHandler.INSTANCE.sendTo(new MessageChiselSound(x, y, z, v), (EntityPlayerMP) player);
		world.setBlock(x, y, z, v.getBlock(), v.getBlockMeta(), 3);
		((IChiselItem) player.getCurrentEquippedItem().getItem()).onChisel(world, player.inventory, player.inventory.currentItem, player.getCurrentEquippedItem(), v);
	}

	public ChiselMode next() {
		ChiselMode[] values = values();
		return values[(ordinal() + 1) % values.length];
	}
}
