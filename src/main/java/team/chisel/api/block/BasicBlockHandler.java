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
 * An Abstract block handler class that allows you to implement any IBlockHandler features you want, but not all of them
 * It already has them implemented, just override what you need
 *
 * Must have an empty constructor
 *
 * @author minecreatr
 */
public abstract class BasicBlockHandler implements IBlockHandler{

    //Todo PREVENT INFINITE LOOP OF METHOD CALLING!!!

    /**
     * Empty Constructor
     */
    public BasicBlockHandler(){

    }

    public float getBlockHardness(World worldIn, BlockPos pos) {
        return getBlock(worldIn, pos).getBlockHardness(worldIn, pos);
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return getBlock(worldIn, pos).getSelectedBoundingBox(worldIn, pos);
    }

    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        getBlock(worldIn, pos).addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        getBlock(worldIn, pos).updateTick(worldIn, pos, state, rand);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        getBlock(worldIn, pos).randomDisplayTick(worldIn, pos, state, rand);
    }

    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        getBlock(worldIn, pos).onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        getBlock(worldIn, pos).onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        getBlock(worldIn, pos).onBlockAdded(worldIn, pos, state);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        return getBlock(worldIn, pos).onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
        getBlock(worldIn, pos).onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }

    private Block getBlock(World w, BlockPos pos) {
        return w.getBlockState(pos).getBlock();
    }
}
