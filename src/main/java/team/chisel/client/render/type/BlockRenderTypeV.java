package team.chisel.client.render.type;

import net.minecraft.util.EnumWorldBlockLayer;
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
    public IChiselTexture makeTexture(EnumWorldBlockLayer layer, TextureSpriteCallback... sprites){
        return new ChiselTextureV(this, layer, sprites);
    }
}
