package team.chisel.client.render.type;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.ctx.CTMHBlockRenderContext;
import team.chisel.client.render.texture.ChiselTextureCTMH;

@BlockRenderType("CTMH")
public class BlockRenderTypeCTMH extends BlockRenderTypeCTM {

    @Override
    public IChiselTexture makeTexture(EnumWorldBlockLayer layer, TextureSpriteCallback... sprites) {
        return new ChiselTextureCTMH(this, layer, sprites);
    }

    @Override
    public CTMBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos) {
        return new CTMHBlockRenderContext(world, pos);
    }
}