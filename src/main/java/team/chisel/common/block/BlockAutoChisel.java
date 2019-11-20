package team.chisel.common.block;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import team.chisel.Chisel;
import team.chisel.common.init.ChiselTabs;

@ParametersAreNonnullByDefault
public class BlockAutoChisel extends Block {
    
    @SuppressWarnings("null")
    private static final VoxelShape COLLISION_SHAPE = VoxelShapes.or(
            makeCuboidShape(0, 0, 0, 1, 10, 1),
            makeCuboidShape(0, 10, 0, 1, 1, 1),
            makeCuboidShape(15, 10, 0, 1, 1, 1),
            makeCuboidShape(0, 10, 0, 1, 1, 1),
            makeCuboidShape(0, 10, 15, 1, 1, 1),
            makeCuboidShape(0, 15, 0, 1, 1, 1));
    
    private static final VoxelShape SELECTION_SHAPE = VoxelShapes.fullCube();

    public BlockAutoChisel() {
        super(Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(2.5f);
        setHarvestLevel("pickaxe", 1);
        setUnlocalizedName("chisel.autochisel");
        setCreativeTab(ChiselTabs.tab);
        useNeighborBrightness = true;
    }

    @Override
    public boolean hasTileEntity(@Nullable BlockState state) {
        return true;
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(@Nullable BlockState state, @Nullable IBlockReader world) {
        return new TileAutoChisel();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            player.openContainer(Chisel.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileAutoChisel) {
                ((TileAutoChisel) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    @Deprecated
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileAutoChisel) {
            TileAutoChisel autochisel = (TileAutoChisel) tileentity;
            dumpItems(worldIn, pos, autochisel.getInputInv());
            dumpItems(worldIn, pos, autochisel.getOtherInv());
            dumpItems(worldIn, pos, autochisel.getOutputInv());
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
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
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return COLLISION_SHAPE;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SELECTION_SHAPE;
    }
    
    @Override
    public boolean doesSideBlockRendering(@Nullable BlockState state, @Nullable IEnviromentBlockReader world, @Nullable BlockPos pos, @Nullable Direction face) {
        return face == Direction.DOWN;
    }
    
    @Override
    public boolean canRenderInLayer(@Nullable BlockState state, @Nullable BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
    }
}
