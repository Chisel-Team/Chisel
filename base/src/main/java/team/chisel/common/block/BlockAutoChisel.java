package team.chisel.common.block;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.IItemHandler;
import team.chisel.common.init.ChiselTileEntities;

public class BlockAutoChisel extends Block implements EntityBlock {
    
    @SuppressWarnings("null")
    private static final VoxelShape COLLISION_SHAPE = Shapes.or(
            box(0, 0, 0, 16, 10, 16),
            box(0, 10, 0, 1, 16, 16),
            box(15, 10, 0, 16, 16, 16),
            box(0, 10, 0, 16, 16, 1),
            box(0, 10, 15, 16, 16, 16),
            box(0, 15, 0, 16, 16, 16));
    
    private static final VoxelShape SELECTION_SHAPE = Shapes.block();

    public BlockAutoChisel(Block.Properties properties) {
        super(properties);
//        setSoundType(SoundType.METAL);
//        setHardness(2.5f);
//        setHarvestLevel("pickaxe", 1);
//        setUnlocalizedName("chisel.autochisel");
//        setCreativeTab(ChiselTabs.tab);
//        useNeighborBrightness = true;
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof TileAutoChisel) {
                player.openMenu((MenuProvider) tileentity);
                // TODO maybe? player.addStat(Stats.INTERACT_WITH_AUTO_CHISEL);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasCustomHoverName()) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);

            if (tileentity instanceof TileAutoChisel) {
                ((TileAutoChisel) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }
    
    @Override
    @Deprecated
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);

        if (tileentity instanceof TileAutoChisel) {
            TileAutoChisel autochisel = (TileAutoChisel) tileentity;
            dumpItems(worldIn, pos, autochisel.getInputInv());
            dumpItems(worldIn, pos, autochisel.getOtherInv());
            dumpItems(worldIn, pos, autochisel.getOutputInv());
            worldIn.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    private void dumpItems(Level worldIn, BlockPos pos, IItemHandler inv) {
        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Containers.dropItemStack(worldIn, x, y, z, stack);
            }
        }
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SELECTION_SHAPE;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TileAutoChisel(ChiselTileEntities.AUTO_CHISEL_TE.get(), pPos, pState);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return BaseEntityBlock.createTickerHelper(pBlockEntityType, ChiselTileEntities.AUTO_CHISEL_TE.get(), ($, $$, $$$, be) -> be.tickCrafting());
    }
}
