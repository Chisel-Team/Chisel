package com.cricketcraft.chisel.compat.fmp;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.minecraft.TorchPart;

import com.cricketcraft.chisel.block.BlockCarvableTorch;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockChiselTorchPart extends ItemBlock {

	private BlockCarvableTorch torch;

	// Blame FML for the duped params, these references will always be the same
	public ItemBlockChiselTorchPart(Block block, BlockCarvableTorch torch) {
		super(block);
		this.torch = torch;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean func_150936_a(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack stack) {
		return (placePart(world, new BlockCoord(x, y, z).offset(side), stack, side, false)) || (super.func_150936_a(world, x, y, z, side, player, stack));
	}

	public TMultiPart createMultiPart(World world, BlockCoord pos, ItemStack item, int side) {
		return new PartChiselTorch(torch.idx, TorchPart.sideMetaMap[side ^ 1]);
	}

	public boolean placePart(World world, BlockCoord pos, ItemStack item, int side, boolean doPlace) {
		TileMultipart tile = TileMultipart.getOrConvertTile(world, pos);
		if (tile == null) {
			return false;
		}
		TMultiPart part = createMultiPart(world, pos, item, side);
		if (part == null) {
			return false;
		}
		if (tile.canAddPart(part)) {
			if (doPlace) {
				TileMultipart.addPart(world, pos, part);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Block block = world.getBlock(x, y, z);
		BlockCoord pos = new BlockCoord(x, y, z);
		if (!block.isReplaceable(world, x, y, z)) {
			pos = new BlockCoord(x, y, z).offset(side);
		}
		if (placePart(world, pos, stack, side, !world.isRemote)) {
			world.playSoundEffect(pos.x + 0.5F, pos.y + 0.5F, pos.z + 0.5F, field_150939_a.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
			stack.stackSize -= 1;
			return true;
		}
		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}
}
