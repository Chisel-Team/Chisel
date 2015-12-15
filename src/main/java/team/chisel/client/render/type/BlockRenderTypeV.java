package team.chisel.client.render.type;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.texture.ChiselTextureV;

/**
 * Block Render type for V variations
 */
public class BlockRenderTypeV extends BlockRenderTypeWithModules {

    public BlockRenderTypeV(int variationSize){
        super(variationSize);
    }

    @Override
    public IChiselTexture makeTexture(TextureSpriteCallback... sprites){
        return new ChiselTextureV(this, sprites);
    }

    @Override
    public int getQuadsPerSide(){
        return 1;
    }
}
