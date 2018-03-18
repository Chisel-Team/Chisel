package team.chisel.common.block;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import team.chisel.Chisel;
import team.chisel.common.init.ChiselTabs;

@ParametersAreNonnullByDefault
public class BlockAutoChisel extends Block {
    
    private static final ImmutableList<AxisAlignedBB> BOXES = ImmutableList.<AxisAlignedBB>builder()
            .add(new AxisAlignedBB(0, 0, 0, 1, 10/16f, 1))
            .add(new AxisAlignedBB(0, 10/16f, 0, 1/16f, 1, 1))
            .add(new AxisAlignedBB(15/16f, 10/16f, 0, 1, 1, 1))
            .add(new AxisAlignedBB(0, 10/16f, 0, 1, 1, 1/16f))
            .add(new AxisAlignedBB(0, 10/16f, 15/16f, 1, 1, 1))
            .add(new AxisAlignedBB(0, 15/16f, 0, 1, 1, 1))
            .build();

    public BlockAutoChisel() {
        super(Material.IRON);
        setHardness(2.5f);
        setHarvestLevel("pickaxe", 1);
        setUnlocalizedName("chisel.autochisel");
        setCreativeTab(ChiselTabs.tab);
        useNeighborBrightness = true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAutoChisel();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            playerIn.openGui(Chisel.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileAutoChisel) {
                ((TileAutoChisel) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileAutoChisel) {
            TileAutoChisel autochisel = (TileAutoChisel) tileentity;
            dumpItems(worldIn, pos, autochisel.getInputInv());
            dumpItems(worldIn, pos, autochisel.getOtherInv());
            dumpItems(worldIn, pos, autochisel.getOutputInv());
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    private void dumpItems(World worldIn, BlockPos pos, IItemHandler inv) {
        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                InventoryHelper.spawnItemStack(worldIn, x, y, z, stack);
            }
        }
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        BOXES.forEach(bb -> addCollisionBoxToList(pos, entityBox, collidingBoxes, bb));
    }
    
    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN;
    }
    
    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
    }
}
