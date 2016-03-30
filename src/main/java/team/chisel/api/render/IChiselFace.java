package team.chisel.api.render;

import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;

public interface IChiselFace {

    List<IChiselTexture<?>> getTextureList();

    TextureAtlasSprite getParticle();

    BlockRenderLayer getLayer();
}
