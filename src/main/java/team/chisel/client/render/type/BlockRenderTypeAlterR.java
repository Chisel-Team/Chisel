package team.chisel.client.render.type;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.ctx.BlockRenderContextAlterR;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.texture.ChiselTextureAlterR;

@BlockRenderType("AR") /* Alernating Random */
public class BlockRenderTypeAlterR implements IBlockRenderType {

    @Override
    public ChiselTextureAlterR makeTexture(TextureInfo info) {
        return new ChiselTextureAlterR(this, info);
    }

    @Override
    public IBlockRenderContext getBlockRenderContext(IBlockState state, IBlockAccess world, BlockPos pos, IChiselTexture<?> tex) {
        return new BlockRenderContextAlterR(pos, (ChiselTextureAlterR) tex);
    }

    @Override
    public IBlockRenderContext getContextFromData(long data) {
        return new BlockRenderContextPosition(BlockPos.fromLong(data));
    }

    @Override
    public int getQuadsPerSide() {
        return 1;
    }

    @Override
    public int requiredTextures() {
        return 1;
    }
}
