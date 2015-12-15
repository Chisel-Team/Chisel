package team.chisel.api.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumWorldBlockLayer;

/**
 * Chisel Face, basicly a list of IChiselTexture's
 */
public class ChiselFace {

    private List<IChiselTexture> textureList;

    private List<ChiselFace> childFaces;

    private EnumWorldBlockLayer layer;

    public ChiselFace(){
        this.textureList = new ArrayList<IChiselTexture>();
        this.childFaces = new ArrayList<ChiselFace>();
        this.layer = EnumWorldBlockLayer.SOLID;
    }

    public ChiselFace(List<IChiselTexture> textureList, List<ChiselFace> childFaces, EnumWorldBlockLayer layer){
        this.textureList = textureList;
        this.childFaces = childFaces;
        this.layer = layer;
    }

    public List<IChiselTexture> getTextureList(){
        List<IChiselTexture> list = new ArrayList<IChiselTexture>();
        list.addAll(this.textureList);
        for (ChiselFace face : childFaces){
            list.addAll(face.getTextureList());
        }
        return list;
    }

    public void addTexture(IChiselTexture texture){
        this.textureList.add(texture);
    }

    public void addChildFace(ChiselFace face){
        this.childFaces.add(face);
    }

    public boolean removeTexture(IChiselTexture texture){
        return this.textureList.remove(texture);
    }

    public boolean removeChildFace(ChiselFace face){
        return this.childFaces.remove(face);
    }

    public TextureAtlasSprite getParticle(){
        if (textureList.get(0) != null) {
            return textureList.get(0).getParticle();
        }
        else {
            return childFaces.get(0).getParticle();
        }
    }

    public void setLayer(EnumWorldBlockLayer layer){
        this.layer = layer;
    }

    public EnumWorldBlockLayer getLayer(){
        return this.layer;
    }

}
