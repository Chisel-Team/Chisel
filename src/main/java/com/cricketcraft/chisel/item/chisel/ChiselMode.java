package com.cricketcraft.chisel.item.chisel;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.cricketcraft.chisel.api.carving.IChiselMode;
import com.cricketcraft.chisel.network.PacketHandler;
import com.cricketcraft.chisel.network.message.MessageChiselSound;

public enum ChiselMode implements IChiselMode {

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

	/**
	 * Assumes that the player is holding a chisel
	 */
	private static void setVariation(EntityPlayer player, World world, int x, int y, int z, Block origBlock, int origMeta, ICarvingVariation v) {
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		ItemStack held = player.getCurrentEquippedItem();
		if (block == v.getBlock() && meta == v.getBlockMeta()) {
			return; // don't chisel to the same thing
		}
		if (origBlock != block || origMeta != meta) {
			return; // don't chisel if this doesn't match the target block (for the AOE modes)
		}

		if (held != null && held.getItem() instanceof IChiselItem) {
			if (origBlock == v.getBlock()) {
				world.setBlockMetadataWithNotify(x, y, z, v.getBlockMeta(), 3);
			} else {
				world.setBlock(x, y, z, v.getBlock(), v.getBlockMeta(), 3);
			}
			boolean breakChisel = false;
			if (((IChiselItem) player.getCurrentEquippedItem().getItem()).onChisel(world, player.getCurrentEquippedItem(), v)) {
				player.getCurrentEquippedItem().damageItem(1, player);
				if (player.getCurrentEquippedItem().stackSize <= 0) {
					player.inventory.mainInventory[player.inventory.currentItem] = null;
					breakChisel = true;
				}
			}
			PacketHandler.INSTANCE.sendTo(new MessageChiselSound(x, y, z, v, breakChisel), (EntityPlayerMP) player);
		}
	}

	public static ChiselMode next(IChiselMode currentMode) {
		if (currentMode instanceof ChiselMode) {
			ChiselMode[] values = values();
			return values[(((ChiselMode) currentMode).ordinal() + 1) % values.length];
		}
		return SINGLE;
	}
}
