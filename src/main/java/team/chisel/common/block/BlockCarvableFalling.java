package team.chisel.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.api.block.VariationData;
import team.chisel.common.entity.EntityFallingBlockCarvable;
import team.chisel.common.init.ChiselBlocks;

import java.util.Random;

public class BlockCarvableFalling extends BlockCarvable {
    public BlockCarvableFalling(Material material, int index, int max, VariationData... variations) {
        super(material, index, max, variations);
    }

    private static boolean fallInstantly;

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
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
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
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
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
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

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
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
                    worldIn.spawnEntity(state1);
                }
            } else {
                IBlockState state = worldIn.getBlockState(pos);
                worldIn.setBlockToAir(pos);

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

    public static boolean canFallThrough(IBlockState state) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR;
    }

    public void onEndFalling(World worldIn, BlockPos pos) {
    }

    public void onBroken(World worldIn, BlockPos p_190974_2_) {
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(rand.nextInt(16) == 0) {
            BlockPos blockpos = pos.down();
            if(canFallThrough(worldIn.getBlockState(blockpos))) {
                double d0 = (double)((float)pos.getX() + rand.nextFloat());
                double d1 = (double)pos.getY() - 0.05D;
                double d2 = (double)((float)pos.getZ() + rand.nextFloat());
                worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[]{Block.getStateId(stateIn)});
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState p_189876_1_) {
        return -16777216;
    }
}
