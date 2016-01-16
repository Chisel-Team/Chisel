package team.chisel.client.render.type;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
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
    public IChiselTexture<BlockRenderTypeCTMV> makeTexture(EnumWorldBlockLayer layer, TextureSpriteCallback... sprites) {
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