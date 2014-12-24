package com.cricketcraft.chisel.block.tileentity;

import com.cricketcraft.chisel.block.BlockPresent;
import com.cricketcraft.chisel.inventory.ContainerPresent;
import com.cricketcraft.chisel.inventory.InventoryLargePresent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;

import java.util.Iterator;
import java.util.List;

public class TileEntityPresent extends TileEntityChest {

    public int type;
    private int ticksSinceSync;

    public TileEntityPresent(int type) {
        this.type = type;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }

    public void checkIfAdjacentBlocksAreMe(TileEntityPresent tile, int meta) {
        if (tile.isInvalid()) {
            this.adjacentChestChecked = false;
        } else if (this.adjacentChestChecked) {
            switch (meta) {
                case 0:
                    if (this.adjacentChestZPos != tile) {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case 1:
                    if (this.adjacentChestXNeg != tile) {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case 2:
                    if (this.adjacentChestZNeg != tile) {
                        this.adjacentChestChecked = false;
                    }

                    break;
                case 3:
                    if (this.adjacentChestXPos != tile) {
                        this.adjacentChestChecked = false;
                    }
            }
        }
    }

    public void checkForAdjacentPresents() {
        if (!this.adjacentChestChecked) {
            TileEntityPresent zNeg = (TileEntityPresent) this.adjacentChestZNeg;
            TileEntityPresent xPos = (TileEntityPresent) this.adjacentChestXPos;
            TileEntityPresent xNeg = (TileEntityPresent) this.adjacentChestXNeg;
            TileEntityPresent zPos = (TileEntityPresent) this.adjacentChestZPos;
            this.adjacentChestChecked = true;

            if (this.isBlockPresent(xCoord - 1, yCoord, zCoord)) {
                xNeg = (TileEntityPresent) this.worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
            }

            if (this.isBlockPresent(xCoord + 1, yCoord, zCoord)) {
                xPos = (TileEntityPresent) this.worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
            }

            if (this.isBlockPresent(xCoord, yCoord, zCoord - 1)) {
                zNeg = (TileEntityPresent) this.worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
            }

            if (this.isBlockPresent(xCoord, yCoord, zCoord + 1)) {
                zPos = (TileEntityPresent) this.worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
            }

            if (this.adjacentChestZNeg != null) {
                zNeg.checkIfAdjacentBlocksAreMe(this, 0);
            }

            if (this.adjacentChestZPos != null) {
                zPos.checkIfAdjacentBlocksAreMe(this, 2);
            }

            if (this.adjacentChestXPos != null) {
                xPos.checkIfAdjacentBlocksAreMe(this, 1);
            }

            if (this.adjacentChestXNeg != null) {
                xNeg.checkIfAdjacentBlocksAreMe(this, 3);
            }

            this.adjacentChestXNeg = xNeg;
            this.adjacentChestXPos = xPos;
            this.adjacentChestZPos = zPos;
            this.adjacentChestZNeg = zNeg;
        }
    }

    private boolean isBlockPresent(int x, int y, int z) {
        if (this.worldObj == null) {
            return false;
        } else {
            Block block = this.worldObj.getBlock(x, y, z);
            return block instanceof BlockPresent;
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        this.checkForAdjacentChests();
        ++this.ticksSinceSync;
        float f;

        if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            f = 5.0F;
            List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((double)((float)this.xCoord - f), (double)((float)this.yCoord - f), (double)((float)this.zCoord - f), (double)((float)(this.xCoord + 1) + f), (double)((float)(this.yCoord + 1) + f), (double)((float)(this.zCoord + 1) + f)));
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                EntityPlayer entityplayer = (EntityPlayer)iterator.next();

                if (entityplayer.openContainer instanceof ContainerPresent)
                {
                    IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this || iinventory instanceof InventoryLargePresent && ((InventoryLargePresent)iinventory).isPartOfLargePresent(this))
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        f = 0.1F;
        double d2;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
        {
            double d1 = (double)this.xCoord + 0.5D;
            d2 = (double)this.zCoord + 0.5D;

            if (this.adjacentChestZPos != null)
            {
                d2 += 0.5D;
            }

            if (this.adjacentChestXPos != null)
            {
                d1 += 0.5D;
            }

            this.worldObj.playSoundEffect(d1, (double)this.yCoord + 0.5D, d2, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f1 = this.lidAngle;

            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += f;
            }
            else
            {
                this.lidAngle -= f;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;

            if (this.lidAngle < f2 && f1 >= f2 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null)
            {
                d2 = (double)this.xCoord + 0.5D;
                double d0 = (double)this.zCoord + 0.5D;

                if (this.adjacentChestZPos != null)
                {
                    d0 += 0.5D;
                }

                if (this.adjacentChestXPos != null)
                {
                    d2 += 0.5D;
                }

                this.worldObj.playSoundEffect(d2, (double)this.yCoord + 0.5D, d0, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    @Override
    public void openInventory() {
        if (numPlayersUsing < 0) {
            numPlayersUsing = 0;
        }

        ++numPlayersUsing;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, numPlayersUsing);
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType());
    }

    @Override
    public void closeInventory() {
        numPlayersUsing = 0;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, numPlayersUsing);
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType());
    }

    @Override
    public void invalidate() {
        super.invalidate();
        updateContainingBlockInfo();
        checkForAdjacentPresents();
    }
}
