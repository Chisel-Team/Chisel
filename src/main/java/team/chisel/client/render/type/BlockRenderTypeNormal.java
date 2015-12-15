package team.chisel.client.render.type;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.texture.ChiselTextureNormal;

/**
 * Normal Block Render Type
 */
@BlockRenderType("NORMAL")
public class BlockRenderTypeNormal implements IBlockRenderType {

    @Override
    public IChiselTexture makeTexture(TextureSpriteCallback... sprites){
        return new ChiselTextureNormal(this, sprites);
    }

    @Override
    public IBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos){
        return null;
    }

    @Override
    public int getQuadsPerSide(){
        return 1;
    }
}
