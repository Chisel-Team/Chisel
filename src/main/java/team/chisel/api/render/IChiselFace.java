package team.chisel.api.render;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface IChiselFace {

    List<IChiselTexture<?>> getTextureList();

    @Nonnull TextureAtlasSprite getParticle();
}
