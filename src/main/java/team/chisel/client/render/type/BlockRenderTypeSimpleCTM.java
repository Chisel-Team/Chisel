package team.chisel.client.render.type;

import team.chisel.client.render.texture.ChiselTextureSimpleCTM;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.type.TextureTypeCTM;

@TextureType("SCTM")
public class BlockRenderTypeSimpleCTM extends TextureTypeCTM {

    @Override
    public ICTMTexture<? extends TextureTypeCTM> makeTexture(TextureInfo info) {
        return new ChiselTextureSimpleCTM(this, info);
    }

    @Override
    public int getQuadsPerSide() {
        return 1;
    }

    @Override
    public int requiredTextures() {
        return 1;
    }
}
