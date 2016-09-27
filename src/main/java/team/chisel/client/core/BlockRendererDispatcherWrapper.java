package team.chisel.client.core;

import lombok.experimental.Delegate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;
import team.chisel.api.block.ICarvable;

public class BlockRendererDispatcherWrapper extends BlockRendererDispatcher {

    private interface RenderBlock {
        boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, VertexBuffer worldRendererIn);
    }

    @Delegate(excludes = RenderBlock.class)
    private BlockRendererDispatcher parent;

    public BlockRendererDispatcherWrapper(BlockRendererDispatcher parent){
        super(null, null);
        this.parent = parent;
    }

    @Override
    public boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, VertexBuffer worldRendererIn){
        //todo Some sort of registry instead of just ICarvable
        if (state.getBlock() instanceof ICarvable){
            IExtendedBlockState extendedState = ((IExtendedBlockState) state).withProperty(
                    PropertyWorldContext.INSTANCE, new WorldContext(blockAccess, pos));
            return parent.renderBlock(extendedState, pos, blockAccess, worldRendererIn);
        }
        return parent.renderBlock(state, pos, blockAccess, worldRendererIn);
    }

}
