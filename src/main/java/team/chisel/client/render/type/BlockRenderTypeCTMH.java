package team.chisel.client.render.type;

import net.minecraft.util.BlockRenderLayer;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.texture.ChiselTextureCTMH;

@BlockRenderType("CTMH")
public class BlockRenderTypeCTMH extends BlockRenderTypeCTM {

    @Override
    public IChiselTexture<BlockRenderTypeCTMH> makeTexture(TextureInfo info) {
        return new ChiselTextureCTMH(this, info);
    }
    
    @Override
    public int getQuadsPerSide() {
        return 1;
    }
}