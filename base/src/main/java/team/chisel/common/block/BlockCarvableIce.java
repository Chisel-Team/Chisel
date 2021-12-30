package team.chisel.common.block;

import java.util.Random;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.chisel.api.block.VariationData;

@ParametersAreNonnullByDefault
public class BlockCarvableIce extends BlockCarvable {

	public BlockCarvableIce(VariationData variation) {
		super(Block.Properties.copy(Blocks.ICE), variation);
	}

	// From IceBlock - Do Not Edit

	public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
		super.playerDestroy(worldIn, player, pos, state, te, stack);
		if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
			if (worldIn.dimensionType().ultraWarm()) {
				worldIn.removeBlock(pos, false);
				return;
			}

			Material material = worldIn.getBlockState(pos.below()).getMaterial();
			if (material.blocksMotion() || material.isLiquid()) {
				worldIn.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
			}
		}

	}

	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		if (worldIn.getBrightness(LightLayer.BLOCK, pos) > 11 - state.getLightBlock(worldIn, pos)) {
			this.turnIntoWater(state, worldIn, pos);
		}
	}

	protected void turnIntoWater(BlockState state, Level world, BlockPos pos) {
		if (world.dimensionType().ultraWarm()) {
			world.removeBlock(pos, false);
		} else {
			world.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
			world.neighborChanged(pos, Blocks.WATER, pos);
		}
	}

	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.NORMAL;
	}

	// From BreakableBlock - Do Not Edit

	@OnlyIn(Dist.CLIENT)
	public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
		return adjacentBlockState.is(this) ? true : super.skipRendering(state, adjacentBlockState, side);
	}
}
