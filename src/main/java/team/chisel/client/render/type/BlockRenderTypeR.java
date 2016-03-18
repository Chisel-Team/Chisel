package team.chisel.client.render.type;

import net.minecraft.util.BlockRenderLayer;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.texture.ChiselTextureR;

public class BlockRenderTypeR extends BlockRenderTypeSheet {

    protected BlockRenderTypeR(int xSize, int ySize) {
        super(xSize, ySize);
    }

    @Override
    public IChiselTexture<BlockRenderTypeR> makeTexture(BlockRenderLayer layer, TextureSpriteCallback... sprites) {
        return new ChiselTextureR(this, layer, sprites);
    }

    @BlockRenderType
    public static final BlockRenderTypeR R4 = new BlockRenderTypeR(2, 2);
    @BlockRenderType
    public static final BlockRenderTypeR R9 = new BlockRenderTypeR(3, 3);
    @BlockRenderType
    public static final BlockRenderTypeR R16 = new BlockRenderTypeR(4, 4);
}
