package com.cricketcraft.chisel.block;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.block.tileentity.TileEntityPresent;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.init.ModTabs;
import com.cricketcraft.chisel.inventory.InventoryLargePresent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

public class BlockPresent extends BlockChest implements ICarvable {
    private final Random random = new Random();
    private int type;
    public CarvableHelper carverHelper;
    public boolean isChristmas;

    public BlockPresent(int type) {
        super(1);
        this.type = type;
        carverHelper = new CarvableHelper();
        setCreativeTab(ModTabs.tabChiselBlocks);
        setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0875F, 0.9365F);

        Calendar calendar = Calendar.getInstance();

        if (calendar.get(2) + 1 == 12 || calendar.get(2) + 1 == 1) {
            isChristmas = true;//The Christmas Season lasts for december and january
        } else {
            isChristmas = false;
        }
    }

    public String getKindOfChest(int type){
        switch (type){
            case 0:
                return isChristmas? "textures/blocks/present/presentChest0" : "textures/blocks/present/chest0";
            case 1:
                return isChristmas? "textures/blocks/present/presentChest1" : "textures/blocks/present/chest1";
            case 2:
                return isChristmas? "textures/blocks/present/presentChest2" : "textures/blocks/present/chest2";
            case 3:
                return isChristmas? "textures/blocks/present/presentChest3" : "textures/blocks/present/chest3";
            case 4:
                return isChristmas? "textures/blocks/present/presentChest4" : "textures/blocks/present/chest4";
            case 5:
                return isChristmas? "textures/blocks/present/presentChest5" : "textures/blocks/present/chest5";
            case 6:
                return isChristmas? "textures/blocks/present/presentChest6" : "textures/blocks/present/chest6";
            case 7:
                return isChristmas? "textures/blocks/present/presentChest7" : "textures/blocks/present/chest7";
            case 8:
                return isChristmas? "textures/blocks/present/presentChest8" : "textures/blocks/present/chest8";
            case 9:
                return isChristmas? "textures/blocks/present/presentChest9" : "textures/blocks/present/chest9";
            case 10:
                return isChristmas? "textures/blocks/present/presentChest10" : "textures/blocks/present/chest10";
            case 11:
                return isChristmas? "textures/blocks/present/presentChest11" : "textures/blocks/present/chest11";
            case 12:
                return isChristmas? "textures/blocks/present/presentChest12" : "textures/blocks/present/chest12";
            case 13:
                return isChristmas? "textures/blocks/present/presentChest13" : "textures/blocks/present/chest13";
            case 14:
                return isChristmas? "textures/blocks/present/presentChest14" : "textures/blocks/present/chest14";
            case 15:
                return isChristmas? "textures/blocks/present/presentChest15" : "textures/blocks/present/chest15";
            default:
                return null;
        }
    }

    private static boolean isCatSittingOnMe(World world, int x, int y, int z) {
        Iterator iterator = world.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1)).iterator();
        EntityOcelot ocelot;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            Entity entity = (Entity) iterator.next();
            ocelot = (EntityOcelot) entity;
        } while (!ocelot.isSitting());

        return true;
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
    public int getRenderType() {
        return -1;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        Block front = world.getBlock(x, y, z - 1);
        Block back = world.getBlock(x, y, z + 1);
        Block left = world.getBlock(x - 1, y, z);
        Block right = world.getBlock(x + 1, y, z);
        byte metadata = 0;
        int rot = MathHelper.floor_double((entityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (rot == 0) {
            metadata = 2;
        }

        if (rot == 1) {
            metadata = 5;
        }

        if (rot == 2) {
            metadata = 3;
        }

        if (rot == 3) {
            metadata = 4;
        }

        if (front != this && back != this && left != this && right != this) {
            world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
        } else {
            if ((front == this || back == this) && (metadata == 4 || metadata == 5)) {
                if (front == this) {
                    world.setBlockMetadataWithNotify(x, y, z - 1, metadata, 3);
                } else {
                    world.setBlockMetadataWithNotify(x, y, z + 1, metadata, 3);
                }

                world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
            }

            if ((left == this || right == this) && (metadata == 4 || metadata == 5)) {
                if (left == this) {
                    world.setBlockMetadataWithNotify(x - 1, y, z, metadata, 3);
                } else {
                    world.setBlockMetadataWithNotify(x + 1, y, z, metadata, 3);
                }

                world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int l = 0;

        if (world.getBlock(x - 1, y, z) == this) {
            ++l;
        }

        if (world.getBlock(x + 1, y, z) == this) {
            ++l;
        }

        if (world.getBlock(x, y, z - 1) == this) {
            ++l;
        }

        if (world.getBlock(x, y, z + 1) == this) {
            ++l;
        }

        return l > 1 ? false : (areSurroundingBlocksMe(world, x, y - 1, z) ? false : (areSurroundingBlocksMe(world, x, y + 1, z) ? false : (areSurroundingBlocksMe(world, x, y, z - 1) ? false : !areSurroundingBlocksMe(world, x, y, z + 1))));
    }

    private boolean areSurroundingBlocksMe(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) != this ? false : (world.getBlock(x - 1, y, z) == this ? true : (world.getBlock(x + 1, y, z) == this ? true : (world.getBlock(x, y, z - 1) == this ? true : world.getBlock(x, y, z + 1) == this)));
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        super.onNeighborBlockChange(world, x, y, z, neighbor);
        TileEntityPresent tileEntityPresent = (TileEntityPresent) world.getTileEntity(x, y, z);

        if (tileEntityPresent != null) {
            tileEntityPresent.updateContainingBlockInfo();
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block present, int meta) {
        TileEntityPresent tileEntityPresent = (TileEntityPresent) world.getTileEntity(x, y, z);

        if (tileEntityPresent != null) {
            for (int c = 0; c < tileEntityPresent.getSizeInventory(); c++) {
                ItemStack itemStack = tileEntityPresent.getStackInSlot(c);

                if (itemStack != null) {
                    float f = random.nextFloat() * 0.8F + 0.1F;
                    float f1 = random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityItem;

                    for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemStack.stackSize > 0; world.spawnEntityInWorld(entityItem)) {
                        int stack = random.nextInt(21) + 10;

                        if (stack > itemStack.stackSize) {
                            stack = itemStack.stackSize;
                        }

                        itemStack.stackSize -= stack;
                        entityItem = new EntityItem(world, x + f, y + f1, z + f2, new ItemStack(itemStack.getItem(), stack, itemStack.getItemDamage()));
                        float motion = 0.05F;
                        entityItem.motionX = random.nextGaussian() * motion;
                        entityItem.motionY = random.nextGaussian() * motion + 0.2F;
                        entityItem.motionZ = random.nextGaussian() * motion;

                        if (itemStack.hasTagCompound()) {
                            entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                        }
                    }
                }
            }

            world.func_147453_f(x, y, z, this);
        }

        super.breakBlock(world, x, y, z, this, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float x1, float y1, float z1) {
        if (!world.isRemote) {
            player.openGui(Chisel.instance, 2, world, x, y, z);
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        if (world.getBlock(x, y, z - 1) == this) {
            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
        } else if (world.getBlock(x, y, z + 1) == this) {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        } else if (world.getBlock(x - 1, y, z) == this) {
            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
        } else if (world.getBlock(x + 1, y, z) == this) {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
        } else {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        this.changeMetadataBasedOffSpecs(world, x, y, z);
        Block back = world.getBlock(x, y, z - 1);
        Block front = world.getBlock(x, y, z + 1);
        Block left = world.getBlock(x - 1, y, z);
        Block right = world.getBlock(x + 1, y, z);

        if (back == this) {
            this.changeMetadataBasedOffSpecs(world, x, y, z - 1);
        }

        if (front == this) {
            this.changeMetadataBasedOffSpecs(world, x, y, z + 1);
        }

        if (left == this) {
            this.changeMetadataBasedOffSpecs(world, x - 1, y, z);
        }

        if (right == this) {
            this.changeMetadataBasedOffSpecs(world, x + 1, y, z);
        }
    }

    public void changeMetadataBasedOffSpecs(World world, int x, int y, int z) {
        if (!world.isRemote) {
            Block back = world.getBlock(x, y, z - 1);
            Block front = world.getBlock(x, y, z + 1);
            Block left = world.getBlock(x - 1, y, z);
            Block right = world.getBlock(x + 1, y, z);
            boolean flag = true, flag1;
            byte metadata;
            int x1, blockMeta, z1;
            Block backLeft, frontRight;

            if (front != this && back != this) {
                if (left != this && right != this) {
                    metadata = 3;

                    if (front.func_149730_j() && !back.func_149730_j()) {
                        metadata = 3;
                    }

                    if (back.func_149730_j() && !front.func_149730_j()) {
                        metadata = 2;
                    }

                    if (left.func_149730_j() && !right.func_149730_j()) {
                        metadata = 5;
                    }

                    if (right.func_149730_j() && !left.func_149730_j()) {
                        metadata = 4;
                    }
                } else {
                    x1 = back == this ? x - 1 : x + 1;
                    backLeft = world.getBlock(x1, y, z - 1);
                    z1 = back == this ? z - 1 : z + 1;
                    frontRight = world.getBlock(x + 1, y, z1);
                    metadata = 5;
                    flag1 = true;

                    if (back == this) {
                        blockMeta = world.getBlockMetadata(x, y, z - 1);
                    } else {
                        blockMeta = world.getBlockMetadata(x, y, z + 1);
                    }

                    if (blockMeta == 4) {
                        metadata = 4;
                    }

                    if ((left.func_149730_j() || backLeft.func_149730_j()) && !right.func_149730_j() && !frontRight.func_149730_j()) {
                        metadata = 5;
                    }

                    if ((right.func_149730_j() || frontRight.func_149730_j()) && !left.func_149730_j() && !backLeft.func_149730_j()) {
                        metadata = 4;
                    }
                }

                world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int par2) {
        return new TileEntityPresent(type);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int strength) {
        if (!canProvidePower()) {
            return 0;
        } else {
            int players = ((TileEntityPresent) world.getTileEntity(x, y, z)).numPlayersUsing;
            return MathHelper.clamp_int(players, 0, 15);
        }
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int strength) {
        return strength == 1 ? isProvidingWeakPower(world, x, y, z, strength) : 0;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int strength) {
        return Container.calcRedstoneFromInventory(getInventory(world, x, y, z));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon("planks_oak");
    }

    public IInventory getInventory(World world, int x, int y, int z) {
        Object object = (TileEntityPresent) world.getTileEntity(x, y, z);

        if (object == null) {
            return null;
        } else if (world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN)) {
            return null;
        } else if (isCatSittingOnMe(world, x, y, z)) {
            return null;
        } else if (world.getBlock(x - 1, y, z) == this && (world.isSideSolid(x - 1, y + 1, z, ForgeDirection.DOWN) || isCatSittingOnMe(world, x, y - 1, z))) {
            return null;
        } else if (world.getBlock(x + 1, y, z) == this && (world.isSideSolid(x + 1, y + 1, z, ForgeDirection.DOWN) || isCatSittingOnMe(world, x, y + 1, z))) {
            return null;
        } else if (world.getBlock(x, y, z - 1) == this && (world.isSideSolid(x, y + 1, z - 1, ForgeDirection.DOWN) || isCatSittingOnMe(world, x, y, z - 1))) {
            return null;
        } else if (world.getBlock(x, y, z + 1) == this && (world.isSideSolid(x, y + 1, z + 1, ForgeDirection.DOWN) || isCatSittingOnMe(world, x, y, z + 1))) {
            return null;
        } else {
            if (world.getBlock(x - 1, y, z) == this) {
                object = new InventoryLargePresent("container.chestDouble", (TileEntityPresent) world.getTileEntity(x - 1, y, z), (IInventory) object);
            }

            if (world.getBlock(x + 1, y, z) == this) {
                object = new InventoryLargePresent("container.chestDouble", (TileEntityPresent) world.getTileEntity(x + 1, y, z), (IInventory) object);
            }

            if (world.getBlock(x, y, z - 1) == this) {
                object = new InventoryLargePresent("container.chestDouble", (TileEntityPresent) world.getTileEntity(x, y, z - 1), (IInventory) object);
            }

            if (world.getBlock(x, y, z + 1) == this) {
                object = new InventoryLargePresent("container.chestDouble", (TileEntityPresent) world.getTileEntity(x, y, z + 1), (IInventory) object);
            }

            return (IInventory) object;
        }
    }

    @Override
    public CarvableVariation getVariation(int metadata) {
        return carverHelper.getVariation(metadata);
    }
}