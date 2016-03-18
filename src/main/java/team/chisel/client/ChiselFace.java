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

    private List<IChiselFace> childFaces;

    private ResourceLocation location;

    private BlockRenderLayer layer;

    public ChiselFace(ResourceLocation location) {
        this(new ArrayList<>(), new ArrayList<>(), location);
    }

    public ChiselFace(ResourceLocation location, BlockRenderLayer layer) {
        this(location);
        setLayer(layer);
    }

    public ChiselFace(List<IChiselTexture<?>> textureList, List<IChiselFace> childFaces, ResourceLocation location) {
        this.textureList = textureList;
        this.childFaces = childFaces;
        this.location = location;
    }

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

    public TextureAtlasSprite getParticle(){
        if (textureList.get(0) != null) {
            return textureList.get(0).getParticle();
        }
        else {
            return childFaces.get(0).getParticle();
        }
    }

    public ResourceLocation getLocation(){
        return this.location;
    }


    public void setLayer(BlockRenderLayer layer){
        this.layer = layer;
    }

    public BlockRenderLayer getLayer(){
        return this.layer;
    }

}
