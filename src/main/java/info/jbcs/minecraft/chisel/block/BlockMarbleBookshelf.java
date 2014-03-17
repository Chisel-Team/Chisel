package info.jbcs.minecraft.chisel.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMarbleBookshelf extends BlockCarvable {

	public BlockMarbleBookshelf() {
		super();
	}


	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		if(side<2)
			return Blocks.planks.getBlockTextureFromSide(side);
		return super.getIcon(side, metadata);
	}

    @Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side){
		if(side<2)
			return Blocks.planks.getBlockTextureFromSide(side);
    	return super.getIcon(world, x, y, z, side);
    }

	@Override
	public int quantityDropped(Random par1Random) {
		return 3;
	}

	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3) {
		return Items.book;
	}

	@Override
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		return 1;
	}
}
