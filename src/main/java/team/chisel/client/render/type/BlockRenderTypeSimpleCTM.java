package team.chisel.client.render.type;

import net.minecraft.util.BlockRenderLayer;
import team.chisel.api.render.*;
import team.chisel.client.render.texture.ChiselTextureSimpleCTM;

@BlockRenderType("SCTM")
public class BlockRenderTypeSimpleCTM extends BlockRenderTypeCTM {

    @Override
    public IChiselTexture<BlockRenderTypeSimpleCTM> makeTexture(BlockRenderLayer layer, TextureSpriteCallback... sprites) {
        return new ChiselTextureSimpleCTM(this, layer, sprites);
    }

    @Override
    public int getQuadsPerSide()
    {
        return 1;
    }
}
