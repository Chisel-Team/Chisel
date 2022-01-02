package team.chisel.common.block;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;

/**
 * Represents a Carvable (aka Chiselable) Pane
 */
public class BlockCarvablePane extends IronBarsBlock implements ICarvable {
    @Getter(onMethod = @__({@Override}))
    private final VariationData variation;

    private boolean dragonProof = false;

    public BlockCarvablePane(BlockBehaviour.Properties properties, VariationData variation) {
        super(properties);
        this.variation = variation;
    }

    
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return super.skipRendering(state, adjacentBlockState, side) && state != adjacentBlockState;
    }

    public Block setDragonProof() {
        dragonProof = true;
        return this;
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
        if (entity instanceof EnderDragon) {
            return !dragonProof;
        } else {
            return super.canEntityDestroy(state, world, pos, entity);
        }
    }
}
