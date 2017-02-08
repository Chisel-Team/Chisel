package team.chisel.client.render.type;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.texture.ChiselTextureNormal;

/**
 * Normal Block Render Type
 */
@BlockRenderType("NORMAL")
public class BlockRenderTypeNormal implements IBlockRenderType {

    @Override
    public IChiselTexture<BlockRenderTypeNormal> makeTexture(TextureInfo info){
        return new ChiselTextureNormal(this, info);
    }

    @Override
    public IBlockRenderContext getBlockRenderContext(IBlockState state, IBlockAccess world, BlockPos pos, IChiselTexture<?> tex){
        return null;
    }

    @Override
    public IBlockRenderContext getContextFromData(long data){
        return null;
    }
}
