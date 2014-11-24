package info.jbcs.minecraft.chisel.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockMarbleLamp extends BlockCarvable
{
    BlockMarbleLamp blockUnpowered;
    BlockMarbleLamp blockPowered;
    boolean powered;

    public BlockMarbleLamp()
    {
        super(Material.redstoneLight, false);

        powered = false;
        blockUnpowered = this;
        blockPowered = new BlockMarbleLamp(this);

        setHardness(0.3F);
    }

    public BlockMarbleLamp(BlockMarbleLamp unpoweredVersion)
    {
        super(Material.redstoneLight, false);

        carverHelper = unpoweredVersion.carverHelper;

        powered = true;
        blockUnpowered = unpoweredVersion;
        blockPowered = this;

        setLightLevel(1.0F);
        setHardness(0.3F);
    }


    public void checkPower(World world, int x, int y, int z)
    {
        if(world.isRemote) return;

        boolean isGettingPower = world.isBlockIndirectlyGettingPowered(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        if(powered && !isGettingPower)
        {
            world.setBlock(x, y, z, blockUnpowered, meta, 2);
        } else if(!powered && isGettingPower)
        {
            world.setBlock(x, y, z, blockPowered, meta, 2);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        checkPower(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        checkPower(world, x, y, z);
    }

/*
    @Override
	public void updateTick(World world, int x, int y, int z, Random rand){
        if (world.isRemote) return;

        if(powered && !world.isBlockIndirectlyGettingPowered(x, y, z)){
            int meta=world.getBlockMetadata(x, y, z);
            world.setBlock(x, y, z, Block.redstoneLampIdle.blockID, meta, 2);
        }
    }*/

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3)
    {
        return Item.getItemFromBlock(blockUnpowered);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(blockUnpowered, 1, world.getBlockMetadata(x, y, z));
    }

}
