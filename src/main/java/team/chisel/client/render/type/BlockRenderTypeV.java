package team.chisel.client.render.type;

import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.texture.ChiselTextureV;

/**
 * Block Render type for V variations
 */
@BlockRenderType("V")
public class BlockRenderTypeV implements IBlockRenderType {


    @Override
    public IChiselTexture<BlockRenderTypeV> makeTexture(TextureInfo info){
        return new ChiselTextureV(this, info);
    }

    @Override
    public IBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos) {
        return new BlockRenderContextPosition(pos);
    }

//    @BlockRenderType
//    public static final BlockRenderTypeV V4 = new BlockRenderTypeV(2, 2);
//    @BlockRenderType
//    public static final BlockRenderTypeV V9 = new BlockRenderTypeV(3, 3);
//    @BlockRenderType
//    public static final BlockRenderTypeV V16 = new BlockRenderTypeV(4, 4);
}
