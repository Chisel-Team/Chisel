package team.chisel.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.chisel.common.block.BlockCarvableFalling;
import team.chisel.common.init.ChiselBlocks;

import java.util.Iterator;

public class EntityFallingBlockCarvable extends EntityFallingBlock {
    public EntityFallingBlockCarvable(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
        super(worldIn, x, y, z, fallingBlockState);
    }

    public void onUpdate() {
        BlockPos blockPos = new BlockPos(this);

        if(getBlock() != null &&
                world.getBlockState(blockPos).getMaterial() == Material.WATER &&
                getBlock().getBlock() == ChiselBlocks.concrete_powder &&
                this.world.setBlockState(blockPos, ChiselBlocks.concrete.getStateFromMeta(ChiselBlocks.concrete_powder.getMetaFromState(getBlock())), 3))
        {
            this.setDead();
            finishFalling(ChiselBlocks.concrete, blockPos);
        }

        super.onUpdate();
    }

    private void finishFalling(Block block, BlockPos blockPos)
    {
        if(block instanceof BlockCarvableFalling) {
            ((BlockCarvableFalling)block).onEndFalling(this.world, blockPos);
        }

        if(this.tileEntityData != null && block instanceof ITileEntityProvider) {
            TileEntity te = this.world.getTileEntity(blockPos);
            if(te != null) {
                NBTTagCompound nbtTagCompound = te.writeToNBT(new NBTTagCompound());
                Iterator iter = this.tileEntityData.getKeySet().iterator();

                while(iter.hasNext()) {
                    String s = (String)iter.next();
                    NBTBase nbtBase = this.tileEntityData.getTag(s);
                    if(!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                        nbtTagCompound.setTag(s, nbtBase.copy());
                    }
                }

                te.readFromNBT(nbtTagCompound);
                te.markDirty();
            }
        }
    }
}