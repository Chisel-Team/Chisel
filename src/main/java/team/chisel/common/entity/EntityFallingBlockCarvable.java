package team.chisel.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.chisel.common.block.BlockCarvableFalling;
import team.chisel.common.init.ChiselBlocks;

import java.util.Iterator;

public class EntityFallingBlockCarvable extends FallingBlockEntity {
    public EntityFallingBlockCarvable(World worldIn, double x, double y, double z, BlockState fallingBlockState) {
        super(worldIn, x, y, z, fallingBlockState);
    }

    @Override
    public void tick() {
        BlockPos blockPos = new BlockPos(this);

        if(getBlockState() != null &&
                world.getBlockState(blockPos).getMaterial() == Material.WATER &&
                getBlockState().getBlock() == ChiselBlocks.concrete_powder &&
                this.world.setBlockState(blockPos, ChiselBlocks.concrete.getStateFromMeta(ChiselBlocks.concrete_powder.getMetaFromState(getBlockState())), 3))
        {
            this.remove();
            finishFalling(ChiselBlocks.concrete, blockPos);
        }

        super.tick();
    }

    private void finishFalling(Block block, BlockPos blockPos)
    {
        if(block instanceof BlockCarvableFalling) {
            ((BlockCarvableFalling)block).onEndFalling(this.world, blockPos);
        }

        if(this.tileEntityData != null && block instanceof ITileEntityProvider) {
            TileEntity te = this.world.getTileEntity(blockPos);
            if(te != null) {
                CompoundNBT nbtTagCompound = te.write(new CompoundNBT());
                Iterator iter = this.tileEntityData.keySet().iterator();

                while(iter.hasNext()) {
                    String s = (String)iter.next();
                    INBT nbtBase = this.tileEntityData.get(s);
                    if(!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                        nbtTagCompound.put(s, nbtBase.copy());
                    }
                }

                te.read(nbtTagCompound);
                te.markDirty();
            }
        }
    }
}