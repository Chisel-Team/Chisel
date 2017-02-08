package team.chisel.client.render.type;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.texture.ChiselTextureCTM;

@BlockRenderType("CTM")
public class BlockRenderTypeCTM implements IBlockRenderType {

    @Override
    public IChiselTexture<? extends BlockRenderTypeCTM> makeTexture(TextureInfo info) {
      return new ChiselTextureCTM(this, info);
    }

    @Override
    public CTMBlockRenderContext getBlockRenderContext(IBlockState state, IBlockAccess world, BlockPos pos, IChiselTexture<?> tex) {
        return new CTMBlockRenderContext(state, world, pos);
    }

    @Override
    public int getQuadsPerSide() {
        return 4;
    }

    @Override
    public int requiredTextures() {
        return 2;
    }

    @Override
    public IBlockRenderContext getContextFromData(long data){
        return new CTMBlockRenderContext(data);
    }
}
