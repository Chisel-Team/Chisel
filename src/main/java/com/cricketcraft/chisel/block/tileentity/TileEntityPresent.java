package com.cricketcraft.chisel.block.tileentity;

import java.util.Iterator;
import java.util.List;

import com.cricketcraft.chisel.block.BlockPresent;
import com.cricketcraft.chisel.inventory.ContainerPresent;
import com.cricketcraft.chisel.inventory.InventoryLargePresent;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityPresent extends TileEntityChest {

    public TileEntityPresent() {

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
        checkForAdjacentPresents();
        float f;

        if (!worldObj.isRemote && numPlayersUsing != 0 && (+xCoord + yCoord + zCoord) % 200 == 0) {
            numPlayersUsing = 0;
            f = 5.0F;
            List<?> list = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord - f, yCoord - f, zCoord - f, (xCoord + 1) + f, (yCoord + 1) + f, (zCoord + 1) + f));
            Iterator<?> iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityPlayer player = (EntityPlayer) iterator.next();

                if (player.openContainer instanceof ContainerPresent) {
                    IInventory inventory = ((ContainerPresent) player.openContainer).getLowerPresentInventory();

                    if (inventory == this || inventory instanceof InventoryLargePresent && ((InventoryLargePresent) inventory).isPartOfLargePresent(this)) {
                        ++numPlayersUsing;
                    }
                }
            }
        }

        prevLidAngle = lidAngle;
        f = 0.1F;
        double d2;

        if (numPlayersUsing > 0 && lidAngle == 0.0F && adjacentChestZNeg == null && adjacentChestXNeg == null) {
            double d1 = xCoord + 0.5D;
            d2 = xCoord + 0.5D;
            if (adjacentChestZPos != null) {
                d2 += 0.5D;
            }

            if (adjacentChestXPos != null) {
                d1 += 0.5D;
            }

            worldObj.playSoundEffect(d1, yCoord + 0.5D, d2, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F) {
            float f1 = lidAngle;

            if (numPlayersUsing > 0) {
                lidAngle += f;
            } else {
                lidAngle -= f;
            }

            if (lidAngle > 1.0F) {
                lidAngle = 1.0F;
            }

            float f2 = 0.5F;

            if (lidAngle < f2 && f1 >= f2 && adjacentChestZNeg == null && adjacentChestXNeg == null) {
                d2 = xCoord + 0.5D;
                double d0 = zCoord + 0.5D;

                if (adjacentChestZPos != null) {
                    d0 += 0.5D;
                }

                if (adjacentChestXPos != null) {
                    d2 += 0.5D;
                }

                worldObj.playSoundEffect(d2, yCoord + 0.5D, d0, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (lidAngle < 0.0F) {
                lidAngle = 0.0F;
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
        if (getBlockType() instanceof BlockPresent) {
            --numPlayersUsing;
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, numPlayersUsing);
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, getBlockType());
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        updateContainingBlockInfo();
        checkForAdjacentPresents();
    }
}
