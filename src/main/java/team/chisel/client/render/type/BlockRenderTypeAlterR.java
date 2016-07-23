package team.chisel.client.render.type;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.*;
import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.texture.ChiselTextureAlterR;
import team.chisel.client.render.texture.ChiselTextureMap;
import team.chisel.client.render.texture.ChiselTextureSimpleCTM;

import com.google.common.base.Optional;

@BlockRenderType("AR") /* Alernating Random */
public class BlockRenderTypeAlterR implements IBlockRenderType {

    @Override
    public ChiselTextureAlterR makeTexture(TextureInfo info) {
        return new ChiselTextureAlterR(this, info);
    }

    @Override
    public IBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos) {
        return new BlockRenderContextPosition(pos);
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
