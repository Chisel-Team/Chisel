package team.chisel.common.util.json;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.common.init.TextureTypeRegistry;
import team.chisel.common.util.CombinedChiselTexture;
import team.chisel.common.util.PossibleType;


/**
 * Raw version of IChiselTexture
 */
public class JsonTexture extends JsonObjectBase<PossibleType<IChiselTexture, CombinedChiselTexture>, TextureMap> {

    /**
     * The String for the type of texture
     */
    private String type;

    /**
     * The Actual path to the different png textures
     * Is an Array because some texture types need more than one
     * For example in CTM the first one is the plain block texture and the second is the special
     * ctm png
     */
    private String[] textures;

    /**
     * If this is the type COMBINED then these are the identifiers of the child textures
     */
    private String[] children;


    @Override
    public PossibleType<IChiselTexture, CombinedChiselTexture> create(TextureMap map) {

        if (isCombined()){
            if (!checkNull(textures)){
                throw new IllegalArgumentException("COMBINED texture type can not have any textures!");
            }
            if (checkNull(children)){
                throw new IllegalArgumentException("COMBINED texture type must have children textures!");
            }
            CombinedChiselTexture combined = new CombinedChiselTexture();
            for (String child : children){
                ResourceLocation loc = new ResourceLocation(child);
                combined.addTexture(loc);
            }
            return PossibleType.makeSecond(combined);
        }
        else {
            if (checkNull(textures)){
                throw new IllegalArgumentException("Texture must have at least one texture!");
            }
            if (!checkNull(children)){
                throw new IllegalArgumentException("Non Combined texture cannot have children!");
            }
            if (!TextureTypeRegistry.isValid(this.type)){
                throw new IllegalArgumentException("Texture Type "+this.type+" is not valid");
            }
            TextureAtlasSprite[] sprites = new TextureAtlasSprite[this.textures.length];
            for (int i = 0 ; i < this.textures.length ; i++){
                String tex = this.textures[i];
                sprites[i] = map.registerSprite(new ResourceLocation(tex));
            }
            IBlockRenderType type = TextureTypeRegistry.getType(this.type);
            IChiselTexture tex = type.makeTexture(sprites);
            return PossibleType.makeFirst(tex);
        }
    }

    private static boolean checkNull(Object[] array){
        return array == null || array.length == 0;
    }

    public boolean isCombined(){
        return this.type.equalsIgnoreCase("COMBINED");
    }
}
