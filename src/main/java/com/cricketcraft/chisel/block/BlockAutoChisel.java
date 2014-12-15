package com.cricketcraft.chisel.block;

import java.util.List;
import java.util.Random;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.init.ModBlocks;
import com.cricketcraft.chisel.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAutoChisel extends BlockContainer{
    public BlockAutoChisel() {
        super(Material.rock);
        setHardness(1F);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float x1, float y1, float z1) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (world.isRemote)
            return true;

        if (tile != null && tile instanceof TileEntityAutoChisel && !player.isSneaking()){
            player.openGui(Chisel.instance, 1, world, x, y, z);
        }

        if(player.isSneaking() && tile!= null && tile instanceof TileEntityAutoChisel){
            TileEntityAutoChisel autoChisel = (TileEntityAutoChisel) tile;
            System.out.println(player.getHeldItem());
            if(player.getItemInUse() == new ItemStack(ModItems.upgrade, 1, 0)){
                //If they are using the speed upgrade
                world.setBlock(x, y, z, ModBlocks.autoChisel, 1, 3);
            } else if(player.getItemInUse() == new ItemStack(ModItems.upgrade, 1, 1)){
                //If they are using the automation upgrade
                world.setBlock(x, y, z, ModBlocks.autoChisel, 2, 3);
            } else if(player.getItemInUse() == new ItemStack(ModItems.upgrade, 1, 2)){
                //If they are using the stack upgrade
                world.setBlock(x, y, z, ModBlocks.autoChisel, 3, 3);
            }
        }

        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public int damageDropped(int metadata){
        return metadata;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List subItems){
        for(int x = 0; x < 4; x++){
            subItems.add(new ItemStack(this, 1, x));
        }
    }

    private void dropItems(World world, int x, int y, int z) {
        Random random = new Random();
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (!(tileEntity instanceof TileEntityAutoChisel)) {
            return;
        }

        TileEntityAutoChisel inventory = (TileEntityAutoChisel) world.getTileEntity(x, y, z);

        for (int c = 0; c < inventory.getSizeInventory(); c++) {
            ItemStack stack = inventory.getStackInSlot(c);

            if (stack != null && stack.stackSize > 0) {
                float rx = random.nextFloat() * 0.8F + 0.1F;
                float ry = random.nextFloat() * 0.8F + 0.1F;
                float rz = random.nextFloat() * 0.8F + 0.1F;
                EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, stack);

                if (stack.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = random.nextGaussian() * factor;
                entityItem.motionY = random.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = random.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                stack.stackSize = 0;
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityAutoChisel();
    }
}
