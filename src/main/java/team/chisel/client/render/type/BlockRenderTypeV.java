package team.chisel.client.render.type;

import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.texture.ChiselTextureV;

/**
 * Block Render type for V variations
 */
public class BlockRenderTypeV extends BlockRenderTypeSheet {

    public BlockRenderTypeV(int xSize, int ySize){
        super(xSize, ySize);
    }

    @Override
    public IChiselTexture<BlockRenderTypeV> makeTexture(EnumWorldBlockLayer layer, TextureSpriteCallback... sprites){
        return new ChiselTextureV(this, layer, sprites);
    }

    @BlockRenderType
    public static final BlockRenderTypeV V4 = new BlockRenderTypeV(2, 2);
    @BlockRenderType
    public static final BlockRenderTypeV V9 = new BlockRenderTypeV(3, 3);
    @BlockRenderType
    public static final BlockRenderTypeV V16 = new BlockRenderTypeV(4, 4);
}
