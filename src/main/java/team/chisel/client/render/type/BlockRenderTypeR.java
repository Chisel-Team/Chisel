package team.chisel.client.render.type;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.texture.ChiselTextureR;

@BlockRenderType("R")
public class BlockRenderTypeR implements IBlockRenderType {

    @Override
    public IChiselTexture<BlockRenderTypeR> makeTexture(TextureInfo info) {
        return new ChiselTextureR(this, info);
    }

    @Override
    public IBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos) {
        return new BlockRenderContextPosition(pos);
    }
    
    @Override
    public IBlockRenderContext getContextFromData(long data) {
        return new BlockRenderContextPosition(BlockPos.fromLong(data));
    }

//    @BlockRenderType
//    public static final BlockRenderTypeR R4 = new BlockRenderTypeR(2, 2);
//    @BlockRenderType
//    public static final BlockRenderTypeR R9 = new BlockRenderTypeR(3, 3);
//    @BlockRenderType
//    public static final BlockRenderTypeR R16 = new BlockRenderTypeR(4, 4);
}
