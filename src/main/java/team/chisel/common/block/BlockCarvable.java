package team.chisel.common.block;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;

/**
 * Represents a Carvable (aka Chiselable) block
 */
public class BlockCarvable extends Block implements ICarvable {

    @Getter(onMethod = @__({@Override}))
    private final VariationData variation;
    
    private boolean dragonProof = false;

    public BlockCarvable(Block.Properties properties, VariationData variation) {
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

    @Override
    public String getDescriptionId() {
        return variation.getGroup().getTranslationKey();
    }
}
