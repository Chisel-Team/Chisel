package team.chisel.common.block;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.api.carving.CarvingUtils;

/**
 * Represents a Carvable (aka Chiselable) block
 */
@ParametersAreNonnullByDefault
public class BlockCarvable extends Block implements ICarvable {

    @Getter(onMethod = @__({@Override}))
    private final VariationData variation;

    private boolean dragonProof = false;

    public BlockCarvable(Block.Properties properties, VariationData variation) {
        super(properties);
        this.variation = variation;
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return super.canRenderInLayer(state, layer);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        return super.isSideInvisible(state, adjacentBlockState, side) && state != adjacentBlockState;
    }

    public Block setDragonProof() {
        dragonProof = true;
        return this;
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        if (entity instanceof EnderDragonEntity) {
            return !dragonProof;
        } else {
            return super.canEntityDestroy(state, world, pos, entity);
        }
    }
    
    @Override
    public String getTranslationKey() {
        return CarvingUtils.getChiselRegistry().getGroup(variation.getGroup()).orElseThrow(IllegalStateException::new).getTranslationKey();
    }
}
