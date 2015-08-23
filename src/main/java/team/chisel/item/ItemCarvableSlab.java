package team.chisel.item;

import team.chisel.block.BlockCarvableSlab;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemCarvableSlab extends ItemCarvable {

	public ItemCarvableSlab(Block block) {
		super(block);
	}

	@Override
	public boolean func_150936_a(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack stack) {
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hx, float hy, float hz) {
		BlockCarvableSlab block = (BlockCarvableSlab) Block.getBlockFromItem(this);
		int meta = stack.getItemDamage();

		ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];
		Block clicked = world.getBlock(x, y, z);

		// the position of the block that we are attempting to place at
		int x2 = x + dir.offsetX;
		int y2 = y + dir.offsetY;
		int z2 = z + dir.offsetZ;

		Block at = world.getBlock(x2, y2, z2);

		boolean metaEquals = world.getBlockMetadata(x2, y2, z2) == meta;
		// if the metadata at the place target matches, and the block there matches either the top of bottom slab, try to fill in the rest of the block
		if (metaEquals && ((at == block.top && (hy <= 0.5D || hy == 1.0D)) || (at == block.bottom && (hy > 0.5D || hy == 0)))) {
			place(stack, world, x2, y2, z2, block.master, meta);
			return true;
		}

		// else if the block clicked on is a top or bottom slab of this type, try to fill in that block if possible
		boolean clickedMetaEquals = world.getBlockMetadata(x, y, z) == meta;
		if (clickedMetaEquals && ((clicked == block.bottom && dir == ForgeDirection.UP) || (clicked == block.top && dir == ForgeDirection.DOWN))) {
			place(stack, world, x, y, z, block.master, meta);
			return true;
		}

		// finally just try to place a normal slab
		if (metaEquals || at.isReplaceable(world, x, y, z)) {
			boolean top = hy > 0.5D && dir != ForgeDirection.UP || dir == ForgeDirection.DOWN;
			Block toPlace = top ? block.top : block.bottom;
			// if we can replace the clicked block do so
			if (clicked.isReplaceable(world, x, y, z)) {
				place(stack, world, x, y, z, toPlace, meta);
			} else {
				place(stack, world, x2, y2, z2, toPlace, meta);
			}
			return true;
		}
		return false;
	}

	private void place(ItemStack stack, World world, int x, int y, int z, Block toPlace, int metadata) {
		world.setBlock(x, y, z, toPlace, metadata, 2);
		stack.stackSize -= 1;
		world.playSoundEffect(x + 0.5f, y + 0.5f, z + 0.5f, this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F,
				this.field_150939_a.stepSound.getPitch() * 0.8F);
	}
}
