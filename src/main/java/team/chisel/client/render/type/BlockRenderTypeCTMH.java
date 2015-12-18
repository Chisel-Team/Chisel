package team.chisel.client.render.type;

import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.texture.ChiselTextureCTMH;

@BlockRenderType("CTMH")
public class BlockRenderTypeCTMH extends BlockRenderTypeCTM {

    @Override
    public IChiselTexture makeTexture(EnumWorldBlockLayer layer, TextureSpriteCallback... sprites) {
        return new ChiselTextureCTMH(this, layer, sprites);
    }
}