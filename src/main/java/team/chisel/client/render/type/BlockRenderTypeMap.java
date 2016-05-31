package team.chisel.client.render.type;

import lombok.RequiredArgsConstructor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.texture.ChiselTextureMap;
import team.chisel.client.render.texture.ChiselTextureMap.MapType;


@RequiredArgsConstructor
public class BlockRenderTypeMap implements IBlockRenderType {

    private final MapType type;
    
    @Override
    public ChiselTextureMap makeTexture(TextureInfo info) {
        return new ChiselTextureMap(this, info, type);
    }
    
    @Override
    public IBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos) {
        return new BlockRenderContextPosition(pos);
    }
    
    @Override
    public IBlockRenderContext getContextFromData(long data) {
        return new BlockRenderContextPosition(BlockPos.fromLong(data));
    }
    
    @BlockRenderType("R")
    public static final BlockRenderTypeMap R = new BlockRenderTypeMap(MapType.RANDOM);
    
    @BlockRenderType("V")
    public static final BlockRenderTypeMap V = new BlockRenderTypeMap(MapType.PATTERNED);
}
