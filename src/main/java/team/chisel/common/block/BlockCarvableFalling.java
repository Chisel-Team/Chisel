package team.chisel.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.chisel.api.block.VariationData;
import team.chisel.common.entity.EntityFallingBlockCarvable;
import team.chisel.common.init.ChiselBlocks;

public class BlockCarvableFalling extends BlockCarvable {
    public BlockCarvableFalling(Block.Properties properties, VariationData variation) {
        super(properties, variation);
    }

    private static boolean fallInstantly;

    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state) {
        /*if(worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1)).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)).getMaterial() == Material.WATER)
        {
            worldIn.setBlockState(pos, ChiselBlocks.concrete.getStateFromMeta(ChiselBlocks.concrete_powder.getMetaFromState(state)), 3);

            worldIn.scheduleUpdate(pos, ChiselBlocks.concrete, this.tickRate(worldIn));
        } else*/
            worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        /*if(worldIn.getBlockState(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1)).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)).getMaterial() == Material.WATER)
        {
            worldIn.setBlockState(pos, ChiselBlocks.concrete.getStateFromMeta(ChiselBlocks.concrete_powder.getMetaFromState(state)), 3);

            worldIn.scheduleUpdate(pos, ChiselBlocks.concrete, this.tickRate(worldIn));
        } else*/
            worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if(worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1)).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)).getMaterial() == Material.WATER)
        {
            worldIn.setBlockState(pos, ChiselBlocks.concrete.getStateFromMeta(ChiselBlocks.concrete_powder.getMetaFromState(state)), 3);
        } else if(!worldIn.isRemote) {
            this.checkFallable(worldIn, pos);
        }
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if(worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ())).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1)).getMaterial() == Material.WATER ||
                worldIn.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)).getMaterial() == Material.WATER)
        {
            worldIn.setBlockState(pos, ChiselBlocks.concrete.getStateFromMeta(ChiselBlocks.concrete_powder.getMetaFromState(state)), 3);
        }
    }

    private void checkFallable(World worldIn, BlockPos pos) {
        if((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {
            boolean i = true;
            if(!fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
                if(!worldIn.isRemote) {
                    EntityFallingBlockCarvable state1 = new EntityFallingBlockCarvable(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                    this.onStartFalling(state1);
                    worldIn.addEntity(state1);
                }
            } else {
                BlockState state = worldIn.getBlockState(pos);
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());

                BlockPos blockpos;
                for(blockpos = pos.down(); (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down()) {
                    ;
                }

                if(blockpos.getY() > 0) {
                    worldIn.setBlockState(blockpos.up(), state);
                }
            }
        }
    }

    protected void onStartFalling(EntityFallingBlockCarvable fallingEntity) {
    }

    public int tickRate(World worldIn) {
        return 2;
    }

    public static boolean canFallThrough(BlockState state) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR;
    }

    public void onEndFalling(World worldIn, BlockPos pos) {
    }

    public void onBroken(World worldIn, BlockPos p_190974_2_) {
    }

    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(rand.nextInt(16) == 0) {
            BlockPos blockpos = pos.down();
            if(canFallThrough(worldIn.getBlockState(blockpos))) {
                double d0 = (double)((float)pos.getX() + rand.nextFloat());
                double d1 = (double)pos.getY() - 0.05D;
                double d2 = (double)((float)pos.getZ() + rand.nextFloat());
                worldIn.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, stateIn), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public int getDustColor(BlockState p_189876_1_) {
        return -16777216;
    }
}
