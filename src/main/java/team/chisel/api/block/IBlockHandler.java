package team.chisel.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * Handles things for a block class
 *
 * Must have a constructor that takes in nothing
 *
 * Recommended to extend BasicBlockHandler instead of implementing directly in case of api changes
 */
public interface IBlockHandler {

    /**
     * Get the block hardness at the specified location
     */
    float getBlockHardness(World worldIn, BlockPos pos);

    /**
     * Gets the selected bounding box
     */
    @SideOnly(Side.CLIENT)
    AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos);

    /**
     * Add all collision boxes of this Block to the list that intersect with the given mask.
     *
     * @param collidingEntity the Entity colliding with this Block
     */
    void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity);

    /**
     * Called on random update ticks
     */
    void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand);

    @SideOnly(Side.CLIENT)
    void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand);

    void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state);

    void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock);

    void onBlockAdded(World worldIn, BlockPos pos, IBlockState state);

    boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ);

    void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn);
}
