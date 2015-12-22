package team.chisel.client.render.type;

import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.texture.ChiselTextureR;

public class BlockRenderTypeR extends BlockRenderTypeSheet {

    protected BlockRenderTypeR(int xSize, int ySize) {
        super(xSize, ySize);
    }

    @Override
    public IChiselTexture<BlockRenderTypeR> makeTexture(EnumWorldBlockLayer layer, TextureSpriteCallback... sprites) {
        return new ChiselTextureR(this, layer, sprites);
    }

    @BlockRenderType("R4")
    public static class R4 extends BlockRenderTypeR {

        public R4() {
            super(2, 2);
        }
    }
}
