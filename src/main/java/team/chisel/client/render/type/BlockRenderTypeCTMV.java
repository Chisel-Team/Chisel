package team.chisel.client.render.type;

import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.ctx.CTMVBlockRenderContext;
import team.chisel.client.render.texture.ChiselTextureCTMV;

@BlockRenderType("CTMV")
public class BlockRenderTypeCTMV implements IBlockRenderType {

    @Override
    public IChiselTexture<BlockRenderTypeCTMV> makeTexture(BlockRenderLayer layer, TextureSpriteCallback... sprites) {
        return new ChiselTextureCTMV(this, layer, sprites);
    }
    
    @Override
    public CTMVBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos) {
        return new CTMVBlockRenderContext(world, pos);
    }
    
    @Override
    public int requiredTextures() {
        return 2;
    }
}