package team.chisel.client.render.type;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.client.render.ctx.BlockRenderContextAlterR;
import team.chisel.client.render.texture.ChiselTextureAlterR;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.texture.ITextureType;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.ctx.TextureContextPosition;

@TextureType("AR") /* Alernating Random */
public class BlockRenderTypeAlterR implements ITextureType {

    @Override
    public ChiselTextureAlterR makeTexture(TextureInfo info) {
        return new ChiselTextureAlterR(this, info);
    }

    @Override
    public ITextureContext getBlockRenderContext(BlockState state, IBlockAccess world, BlockPos pos, ICTMTexture<?> tex) {
        return new BlockRenderContextAlterR(pos, (ChiselTextureAlterR) tex);
    }

    @Override
    public ITextureContext getContextFromData(long data) {
        return new TextureContextPosition(BlockPos.fromLong(data));
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
