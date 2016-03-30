package team.chisel.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;

/**
 * Chisel Face, basically a list of IChiselTexture's
 */
public final class ChiselFace implements IChiselFace {

    private List<IChiselTexture<?>> textureList;
    private TextureAtlasSprite particle;
    
    private List<IChiselFace> childFaces;

    private BlockRenderLayer layer;

    public ChiselFace(ResourceLocation location) {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public ChiselFace(ResourceLocation location, BlockRenderLayer layer) {
        this(location);
        setLayer(layer);
    }

    public ChiselFace(List<IChiselTexture<?>> textureList, List<IChiselFace> childFaces) {
        this.textureList = textureList;
        this.childFaces = childFaces;
    }

    @Override
    public List<IChiselTexture<?>> getTextureList(){
        List<IChiselTexture<?>> list = new ArrayList<>();
        list.addAll(this.textureList);
        for (IChiselFace face : childFaces){
            list.addAll(face.getTextureList());
        }
        return list;
    }

    public void addTexture(IChiselTexture<?> texture){
        this.textureList.add(texture);
    }

    public void addChildFace(IChiselFace face){
        this.childFaces.add(face);
    }

    public boolean removeTexture(IChiselTexture<?> texture){
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

    public void setLayer(BlockRenderLayer layer){
        this.layer = layer;
    }

    @Override
    public BlockRenderLayer getLayer(){
        return this.layer;
    }
}
