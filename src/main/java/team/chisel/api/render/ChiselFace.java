package team.chisel.api.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * Chisel Face, basicly a list of IChiselTexture's
 */
public class ChiselFace {

    private List<IChiselTexture> textureList;

    public ChiselFace(){
        this.textureList = new ArrayList<IChiselTexture>();
    }

    public ChiselFace(List<IChiselTexture> textureList){
        this.textureList = textureList;
    }

    public List<IChiselTexture> getTextureList(){
        return this.textureList;
    }

    public void addTexture(IChiselTexture texture){
        this.textureList.add(texture);
    }

    public void removeTexture(IChiselTexture texture){
        if (this.textureList.contains(texture)){
            this.textureList.remove(texture);
        }
    }

    public TextureAtlasSprite getParticle(){
        return textureList.get(0).getParticle();
    }

}
