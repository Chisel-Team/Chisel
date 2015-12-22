package team.chisel.client.render.type;

import lombok.Getter;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.client.render.ctx.BlockRenderContextPosition;

/**
 * Abstract block render type for blocks that need to keep track of x/y/z modules
 */
public abstract class BlockRenderTypeSheet implements IBlockRenderType {

    @Getter
    private final int xSize, ySize;

    public BlockRenderTypeSheet(int xSize, int ySize){
        this.xSize = xSize;
        this.ySize = ySize;
    }
    
    @Override
    public IBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos) {
        return new BlockRenderContextPosition(pos);
    }
}
