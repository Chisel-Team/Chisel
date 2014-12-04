package info.jbcs.minecraft.chisel.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.block.tileentity.TileEntityAutoChisel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockAutoChisel extends BlockContainer {
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
        if (tile != null && tile instanceof TileEntityAutoChisel)
            player.openGui(Chisel.instance, 1, world, x, y, z);
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
    public void getSubBlocks(int metadata, CreativeTabs creativeTabs, List subItems){
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
