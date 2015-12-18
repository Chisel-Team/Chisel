package team.chisel.client.render.ctx;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import team.chisel.client.render.ctm.CTM;

public class CTMHBlockRenderContext extends CTMBlockRenderContext {

    public CTMHBlockRenderContext(IBlockAccess world, BlockPos pos) {
        super(world, pos);
    }

    @Override
    protected CTM createCTM() {
        return new CTM() {

            @Override
            public boolean isConnected(IBlockAccess world, BlockPos current, BlockPos connection, EnumFacing dir, IBlockState state) {
                if (dir.getAxis().isVertical()) {
                    return false;
                } else if (current.getY() != connection.getY()) {
                    return false;
                } else {
                    return super.isConnected(world, current, connection, dir, state);
                }
            }
        };
    }
}
