package team.chisel.client.render.type;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.client.render.ctx.BlockRenderContextSheet;

/**
 * Abstract block render type for blocks that need to keep track of x/y/z modules
 */
public abstract class BlockRenderTypeSheet implements IBlockRenderType {

    private int xSize, ySize;

    protected BlockRenderTypeSheet(int xSize, int ySize){
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public BlockRenderContextSheet getBlockRenderContext(IBlockAccess world, BlockPos pos){
        return new BlockRenderContextSheet(pos.getX(), pos.getY(), pos.getZ(), xSize, ySize);
    }
}
