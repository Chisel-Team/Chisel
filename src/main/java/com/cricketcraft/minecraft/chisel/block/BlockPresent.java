package com.cricketcraft.minecraft.chisel.block;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.minecraft.chisel.Chisel;
import com.cricketcraft.minecraft.chisel.api.ICarvable;
import com.cricketcraft.minecraft.chisel.block.tileentity.TileEntityPresent;
import com.cricketcraft.minecraft.chisel.carving.CarvableHelper;
import com.cricketcraft.minecraft.chisel.carving.CarvableVariation;
import com.cricketcraft.minecraft.chisel.init.ModTabs;
import com.cricketcraft.minecraft.chisel.inventory.InventoryLargePresent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPresent extends BlockContainer implements ICarvable {
    private final Random random = new Random();
    public CarvableHelper carverHelper;
    private boolean isChristmas = true;

    public BlockPresent() {
        super(Material.wood);
        carverHelper = new CarvableHelper();
        setCreativeTab(ModTabs.tabChiselBlocks);
        setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0875F, 0.9365F);

        Calendar calendar = Calendar.getInstance();

        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 25 && calendar.get(5) <= 26) {
            isChristmas = true;//<--- this would have been false before and it lasts for two days :)
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

    public ResourceLocation getResourceSingle(int metadata) {
        if (isChristmas) {
            switch (metadata) {
                case 1:
                    return new ResourceLocation(Chisel.MOD_ID, "textures/blocks/present/red.png");
                default:
                    return null;
            }
        } else {
            switch (metadata) {
                case 1:
                    return new ResourceLocation(Chisel.MOD_ID, "textures/blocks/present/crate.png");
                default:
                    return null;
            }
        }
    }

    public ResourceLocation getResourceDouble(int metadata) {
        if (isChristmas) {
            switch (metadata) {
                case 1:
                    return new ResourceLocation(Chisel.MOD_ID, "textures/blocks/presentDouble/red.png");
                default:
                    return null;
            }
        } else {
            switch (metadata) {
                case 1:
                    return new ResourceLocation(Chisel.MOD_ID, "textures/blocks/presentDouble/crate.png");
                default:
                    return null;
            }
        }
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
        return new TileEntityPresent();
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

    @Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon("planks_oak");
    }

    public IInventory getInventory(World world, int x, int y, int z) {
        Object object = world.getTileEntity(x, y, z);

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