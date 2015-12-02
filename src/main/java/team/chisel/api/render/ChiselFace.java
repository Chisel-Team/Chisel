package team.chisel.api.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Chisel Face, basicly a list of IChiselTexture's
 */
public class ChiselFace {

    private List<IChiselTexture<? extends IBlockRenderContext>> textureList;

    public ChiselFace(){
        this.textureList = new ArrayList<IChiselTexture<? extends IBlockRenderContext>>();
    }

    public ChiselFace(List<IChiselTexture<? extends IBlockRenderContext>> textureList){
        this.textureList = textureList;
    }

    public List<IChiselTexture<? extends IBlockRenderContext>> getTextureList(){
        return this.textureList;
    }

    public void addTexture(IChiselTexture<? extends IBlockRenderContext> texture){
        this.textureList.add(texture);
    }

    public void removeTexture(IChiselTexture<? extends IBlockRenderContext> texture){
        if (this.textureList.contains(texture)){
            this.textureList.remove(texture);
        }
    }

    public TextureAtlasSprite getParticle(){
        return textureList.get(0).getParticle();
    }

}
