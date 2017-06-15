package team.chisel.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.IChiselFace;

/**
 * Chisel Face, basically a list of IChiselTexture's
 */
public final class ChiselFace implements IChiselFace {

    private List<ICTMTexture<?>> textureList;
    private TextureAtlasSprite particle;
    
    private List<IChiselFace> childFaces;

    public ChiselFace(ResourceLocation location) {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public ChiselFace(List<ICTMTexture<?>> textureList, List<IChiselFace> childFaces) {
        this.textureList = textureList;
        this.childFaces = childFaces;
    }

    @Override
    public List<ICTMTexture<?>> getTextureList(){
        List<ICTMTexture<?>> list = new ArrayList<>();
        list.addAll(this.textureList);
        for (IChiselFace face : childFaces){
            list.addAll(face.getTextureList());
        }
        return list;
    }

    public void addTexture(ICTMTexture<?> texture){
        this.textureList.add(texture);
    }

    public void addChildFace(IChiselFace face){
        this.childFaces.add(face);
    }

    public boolean removeTexture(ICTMTexture<?> texture){
        return this.textureList.remove(texture);
    }

    public boolean removeChildFace(IChiselFace face){
        return this.childFaces.remove(face);
    }

    @Override
    public TextureAtlasSprite getParticle() {
        if (particle == null) {
            if (textureList.get(0) != null) {
                particle = textureList.get(0).getParticle();
            } else {
                particle = childFaces.get(0).getParticle();
            }
        }
        return particle;
    }
    
    public void setParticle(TextureAtlasSprite sprite) {
        particle = sprite;
    }
}
