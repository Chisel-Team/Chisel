package team.chisel.common.block;

import java.util.Random;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.chisel.api.block.VariationData;

@ParametersAreNonnullByDefault
public class BlockCarvableIce extends BlockCarvable {

	public BlockCarvableIce(VariationData variation) {
		super(Block.Properties.from(Blocks.ICE), variation);
	}

	// From IceBlock - Do Not Edit

	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
			if (worldIn.getDimensionType().isUltrawarm()) {
				worldIn.removeBlock(pos, false);
				return;
			}

			Material material = worldIn.getBlockState(pos.down()).getMaterial();
			if (material.blocksMovement() || material.isLiquid()) {
				worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
			}
		}

	}

	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (worldIn.getLightFor(LightType.BLOCK, pos) > 11 - state.getOpacity(worldIn, pos)) {
			this.turnIntoWater(state, worldIn, pos);
		}
	}

	protected void turnIntoWater(BlockState state, World world, BlockPos pos) {
		if (world.getDimensionType().isUltrawarm()) {
			world.removeBlock(pos, false);
		} else {
			world.setBlockState(pos, Blocks.WATER.getDefaultState());
			world.neighborChanged(pos, Blocks.WATER, pos);
		}
	}

	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.NORMAL;
	}

	// From BreakableBlock - Do Not Edit

	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
		return adjacentBlockState.matchesBlock(this) ? true : super.isSideInvisible(state, adjacentBlockState, side);
	}
}
