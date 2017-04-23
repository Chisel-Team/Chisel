package team.chisel.client.render.texture;

import java.lang.reflect.Type;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import lombok.Getter;
import lombok.ToString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.common.init.TextureTypeRegistry;

public abstract class MetadataSectionChisel implements IMetadataSection {
    
    public static final @Nonnull String SECTION_NAME = "ctm";
    
    public abstract int getVersion();
    
    public abstract IBlockRenderType getType();
    
    public abstract BlockRenderLayer getLayer();
    
    public abstract ResourceLocation[] getAdditionalTextures();
    
    @ToString
    @Getter
    public static class V1 extends MetadataSectionChisel {
        
        private IBlockRenderType type;
        private BlockRenderLayer layer;
        private ResourceLocation[] additionalTextures;

        @Override
        public int getVersion() {
            return 1;
        }

        public static MetadataSectionChisel fromJson(JsonObject obj) {
            V1 ret = new V1();

            if (obj.has("type")) {
                JsonElement typeEle = obj.get("type");
                if (typeEle.isJsonPrimitive() && typeEle.getAsJsonPrimitive().isString()) {
                    ret.type = TextureTypeRegistry.getType(typeEle.getAsString());
                    if (ret.type == null) {
                        Chisel.logger.error("Invalid render type given: {}", typeEle);
                    }
                }
            }

            if (obj.has("layer")) {
                JsonElement layerEle = obj.get("layer");
                if (layerEle.isJsonPrimitive() && layerEle.getAsJsonPrimitive().isString()) {
                    try {
                        ret.layer = BlockRenderLayer.valueOf(layerEle.getAsString());
                    } catch (IllegalArgumentException e) {
                        Chisel.logger.error("Invalid block layer given: {}", layerEle);
                        ret.layer = BlockRenderLayer.SOLID;
                    }
                }
            }

            if (obj.has("textures")) {
                JsonElement texturesEle = obj.get("textures");
                if (texturesEle.isJsonArray()) {
                    JsonArray texturesArr = texturesEle.getAsJsonArray();
                    ret.additionalTextures = new ResourceLocation[texturesArr.size()];
                    TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
                    for (int i = 0; i < texturesArr.size(); i++) {
                        JsonElement e = texturesArr.get(i);
                        if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isString()) {
                            ret.additionalTextures[i] = new ResourceLocation(e.getAsString());
                        }
                    }
                }
            }
            return ret;
        }
    }
    
    public static class Serializer implements IMetadataSectionSerializer<MetadataSectionChisel> {

        @Override
        public MetadataSectionChisel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                if (obj.has("ctm_version")) {
                    JsonElement version = obj.get("ctm_version");
                    if (version.isJsonPrimitive() && version.getAsJsonPrimitive().isNumber()) {
                        switch (version.getAsInt()) {
                        case 1:
                            return V1.fromJson(obj);
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public @Nonnull String getSectionName() {
            return SECTION_NAME;
        }
    }

}
