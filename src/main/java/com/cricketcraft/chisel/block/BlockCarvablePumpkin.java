package com.cricketcraft.chisel.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.api.carving.CarvableHelper;
import com.cricketcraft.chisel.api.carving.IVariationInfo;
import com.cricketcraft.chisel.entity.EntityChiselSnowman;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarvablePumpkin extends BlockPumpkin implements ICarvable {

	public CarvableHelper carverHelper;

	@SideOnly(Side.CLIENT)
	private IIcon top, face;

	private String faceLocation;

	public BlockCarvablePumpkin(boolean isOn) {
		super(isOn);
		this.setStepSound(Block.soundTypeWood);
		if (isOn)
			setLightLevel(10.0F);
		carverHelper = new CarvableHelper(this);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if (world.getBlock(x, y - 1, z) == Blocks.snow && world.getBlock(x, y - 2, z) == Blocks.snow) {
			if (!world.isRemote) {
				// Let's grab the pumpkin before we start
				ItemStack pumpkin = new ItemStack(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));

				world.setBlock(x, y, z, Blocks.air, 0, 2);
				world.setBlock(x, y - 1, z, Blocks.air, 0, 2);
				world.setBlock(x, y - 2, z, Blocks.air, 0, 2);
				EntityChiselSnowman snowman = new EntityChiselSnowman(world);
				snowman.setCurrentItemOrArmor(2, pumpkin);
				snowman.setLocationAndAngles(x + 0.5D, y - 1.95D, z + 0.5D, 0.0F, 0.0F);
				world.spawnEntityInWorld(snowman);
				world.notifyBlockChange(x, y, z, Blocks.air);
				world.notifyBlockChange(x, y - 1, z, Blocks.air);
				world.notifyBlockChange(x, y - 2, z, Blocks.air);
			}

			// Spawn some lovely particles
			for (int c = 0; c < 120; ++c) {
				world.spawnParticle("snowshovel", x + world.rand.nextDouble(), y - 2 + world.rand.nextDouble() * 2.5D, z + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		} else if (world.getBlock(x, y - 1, z) == Blocks.iron_block || world.getBlock(x, y - 2, z) == Blocks.iron_block) {
			boolean flag = world.getBlock(x - 1, y - 1, z) == Blocks.iron_block && world.getBlock(x + 1, y - 1, z) == Blocks.iron_block;
			boolean flag1 = world.getBlock(x, y - 1, z - 1) == Blocks.iron_block && world.getBlock(x, y - 1, z + 1) == Blocks.iron_block;

			if (flag || flag1) {
				world.setBlock(x, y, z, Blocks.air, 0, 2);
				world.setBlock(x, y - 1, z, Blocks.air, 0, 2);
				world.setBlock(x, y - 2, z, Blocks.air, 0, 2);

				if (flag) {
					world.setBlock(x - 1, y - 1, z, Blocks.air, 0, 2);
					world.setBlock(x + 1, y - 1, z, Blocks.air, 0, 2);
				} else {
					world.setBlock(x, y - 1, z - 1, Blocks.air, 0, 2);
					world.setBlock(x, y - 1, z + 1, Blocks.air, 0, 2);
				}

				EntityIronGolem ironGolem = new EntityIronGolem(world);
				ironGolem.setPlayerCreated(true);
				ironGolem.setLocationAndAngles(x + 0.5D, y - 1.95D, z + 0.5D, 0.0F, 0.0F);
				world.spawnEntityInWorld(ironGolem);

				world.notifyBlockChange(x, y, z, Blocks.air);
				world.notifyBlockChange(x, y - 1, z, Blocks.air);
				world.notifyBlockChange(x, y - 2, z, Blocks.air);

				if (flag) {
					world.notifyBlockChange(x - 1, y - 1, z, Blocks.air);
					world.notifyBlockChange(x + 1, y - 1, z, Blocks.air);
				} else {
					world.notifyBlockChange(x, y - 1, z - 1, Blocks.air);
					world.notifyBlockChange(x, y - 1, z + 1, Blocks.air);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? top : (side == 0 ? top : (meta == 2 && side == 2 ? face : (meta == 3 && side == 5 ? face
				: (meta == 0 && side == 3 ? face : (meta == 1 && side == 4 ? face : this.blockIcon)))));
	}

	@Override
	public void registerBlockIcons(IIconRegister icon) {
		top = icon.registerIcon(Chisel.MOD_ID + ":pumpkin/pumpkin_top");
		face = icon.registerIcon(Chisel.MOD_ID + ":" + faceLocation);
		this.blockIcon = icon.registerIcon(Chisel.MOD_ID + ":pumpkin/pumpkin_side");
	}

	@Override
	public IVariationInfo getManager(IBlockAccess world, int x, int y, int z, int metadata) {
		return carverHelper.getVariation(metadata);
	}

	@Override
	public IVariationInfo getManager(int meta) {
		return carverHelper.getVariation(meta);
	}

	public void setInformation(String textureLocation) {
		this.faceLocation = textureLocation;
	}
}
