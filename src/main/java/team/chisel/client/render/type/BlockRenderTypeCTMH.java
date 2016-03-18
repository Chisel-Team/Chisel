package team.chisel.client.render.type;

import net.minecraft.util.BlockRenderLayer;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.texture.ChiselTextureCTMH;

@BlockRenderType("CTMH")
public class BlockRenderTypeCTMH extends BlockRenderTypeCTM {

    @Override
    public IChiselTexture<BlockRenderTypeCTMH> makeTexture(BlockRenderLayer layer, TextureSpriteCallback... sprites) {
        return new ChiselTextureCTMH(this, layer, sprites);
    }
    
    @Override
    public int getQuadsPerSide() {
        return 1;
    }
}