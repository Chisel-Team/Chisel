package team.chisel.client.render.type;

import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.texture.ChiselTextureCTMV;

@BlockRenderType("CTMV")
public class BlockRenderTypeCTMV extends BlockRenderTypeCTM {

    @Override
    public IChiselTexture<BlockRenderTypeCTMV> makeTexture(EnumWorldBlockLayer layer, TextureSpriteCallback... sprites) {
        return new ChiselTextureCTMV(this, layer, sprites);
    }
}