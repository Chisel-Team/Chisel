package team.chisel.client.render;

import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.base.Optional;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;

@ToString(of = { "vertPos", "vertUv" })
public class Quad {
    
    @Value
    public static class Vertex {
        Vector3f pos;
        Vector2f uvs;
    }
    
    @RequiredArgsConstructor
    @ToString
    public static class UVs {

        private static final TextureAtlasSprite BASE = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TextureMap.LOCATION_MISSING_TEXTURE.toString());
        private static final float BASE_WIDTH = BASE.getMaxU() - BASE.getMinU(), BASE_HEIGHT = BASE.getMaxV() - BASE.getMinV();
        
        @Getter
        private final TextureAtlasSprite sprite;
                
        @Getter
        final float minU, minV, maxU, maxV;
        
        private UVs(Vector2f... data) {
            this(BASE, data);
        }
        
        private UVs(TextureAtlasSprite sprite, Vector2f... data) {
            this.sprite = sprite;
            
            float minU = Float.MAX_VALUE;
            float minV = Float.MAX_VALUE;
            float maxU = 0, maxV = 0;
            for (Vector2f v : data) {
                minU = Math.min(minU, v.x);
                minV = Math.min(minV, v.y);
                maxU = Math.max(maxU, v.x);
                maxV = Math.max(maxV, v.y);
            }
            this.minU = minU;
            this.minV = minV;
            this.maxU = maxU;
            this.maxV = maxV;
        }

        public UVs transform(TextureAtlasSprite other) {
            float scaleU = BASE_WIDTH / (other.getMaxU() - other.getMinU());
            float scaleV = BASE_HEIGHT / (other.getMaxV() - other.getMinV());

            float startU = (minU - BASE.getMinU()) * scaleU;
            float startV = (minV - BASE.getMinV()) * scaleV;
            float endU = (BASE.getMaxU() - maxU) * scaleU;
            float endV = (BASE.getMaxV() - maxV) * scaleV;

            return new UVs(other, startU + other.getMinU(), startV + other.getMinV(), other.getMaxU() - endU, other.getMaxV() - endV);
        }

        public UVs normalize() {
            return new UVs(sprite, Quad.normalize(sprite.getMinU(), sprite.getMaxU(), this.minU), Quad.normalize(sprite.getMinV(), sprite.getMaxV(), this.minV), Quad.normalize(sprite.getMinU(),
                    sprite.getMaxU(), this.maxU), Quad.normalize(sprite.getMinV(), sprite.getMaxV(), this.maxV));
        }

        public UVs relativize() {
            return new UVs(sprite, Quad.lerp(sprite.getMinU(), sprite.getMaxU(), this.minU), Quad.lerp(sprite.getMinV(), sprite.getMaxV(), this.minV), Quad.lerp(sprite.getMinU(), sprite.getMaxU(),
                    this.maxU), Quad.lerp(sprite.getMinV(), sprite.getMaxV(), this.maxV));
        }

        public Vector2f[] vectorize() {
            return new Vector2f[]{ new Vector2f(minU, minV), new Vector2f(minU, maxV), new Vector2f(maxU, maxV), new Vector2f(maxU, minV) };
        }
    }

    private final Vector3f[] vertPos;
    private final Vector2f[] vertUv;
    
    @Getter
    private final UVs uvs;
    
    private final Builder builder;
    
    private Quad(Vector3f[] verts, Vector2f[] uvs, Builder builder) {
        this(verts, new UVs(uvs), builder);
    }
    
    private Quad(Vector3f[] verts, UVs uvs, Builder builder) {
        this.vertPos = verts;
        this.vertUv = uvs.vectorize();
        this.uvs = uvs;
        this.builder = builder;
    }

    public void compute() {

    }

    public Quad[] subdivide(int count) {
        List<Quad> rects = Lists.newArrayList();

        Pair<Quad, Optional<Quad>> firstDivide = divide(this, false);
        rects.add(firstDivide.getLeft());
        if (firstDivide.getRight().isPresent()) {
            Quad split = firstDivide.getRight().get();
            rects.add(split);
            Pair<Quad, Optional<Quad>> secondDivide = divide(split, true);
            rects.add(secondDivide.getLeft());
            if (secondDivide.getRight().isPresent()) {
                rects.add(secondDivide.getRight().get());
            }
        }

        return rects.toArray(new Quad[rects.size()]);
    }
    
    @Nullable
    private Pair<Quad, Optional<Quad>> divide(Quad quad, boolean vertical) {
        float min, max;
        UVs uvs = quad.getUvs().normalize();
        if (vertical) {
            min = uvs.minV;
            max = uvs.maxV;
        } else {
            min = uvs.minU;
            max = uvs.maxU;
        }
        if (min < 0.5 && max > 0.5) {
            UVs first = new UVs(uvs.getSprite(), uvs.minU, uvs.minV, vertical ? uvs.maxU : 0.5f, vertical ? 0.5f : uvs.maxV);
            UVs second = new UVs(uvs.getSprite(), vertical ? uvs.minU : 0.5f, vertical ? 0.5f : uvs.minV, uvs.maxU, uvs.maxV);

            int firstIndex = 0;
            for (int i = 0; i < vertUv.length; i++) {
                if (vertUv[i].y == quad.getUvs().minV && vertUv[i].x == quad.getUvs().minU) {
                    firstIndex = i;
                    break;
                }
            }
            
            float f = (0.5f - min) / (max - min);

            Vector3f[] firstQuad = new Vector3f[4];
            Vector3f[] secondQuad = new Vector3f[4];
            for (int i = 0; i < 4; i++) {
                int idx = (firstIndex + i) % 4;
                firstQuad[i] = new Vector3f(vertPos[idx]);
                secondQuad[i] = new Vector3f(vertPos[idx]);
            }
            
            // TODO This is completly wrong...
            if (vertical) {
                firstQuad[1].y = lerp(firstQuad[1].y, firstQuad[0].y, f);
                firstQuad[2].y = lerp(firstQuad[2].y, firstQuad[3].y, f);
                secondQuad[1].y = lerp(secondQuad[1].y, secondQuad[0].y, f);
                secondQuad[2].y = lerp(secondQuad[2].y, secondQuad[3].y, f);
            } else {
                firstQuad[2].x = lerp(firstQuad[2].x, firstQuad[1].x, f);
                firstQuad[3].x = lerp(firstQuad[3].x, firstQuad[0].x, f);
                secondQuad[2].x = lerp(secondQuad[2].x, secondQuad[1].x, f);
                secondQuad[3].x = lerp(secondQuad[3].x, secondQuad[0].x, f);
            }

            return Pair.of(new Quad(firstQuad, first.relativize(), builder), Optional.of(new Quad(secondQuad, second.relativize(), builder)));
        } else {
            return Pair.of(quad, Optional.absent());
        }
    }
    
    private static float lerp(float a, float b, float f) {
        float ret = (a * (1 - f)) + (b * f);
        return ret;
    }
    
    private static float normalize(float min, float max, float x) {
        float ret = (x - min) / (max - min);
        return ret;
    }
    
    public BakedQuad rebake() {
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(this.builder.vertexFormat);
        builder.setQuadOrientation(this.builder.quadOrientation);
        builder.setQuadTint(this.builder.quadTint);

        for (int v = 0; v < 4; v++) {
            for (int i = 0; i < this.builder.vertexFormat.getElementCount(); i++) {
                VertexFormatElement ele = this.builder.vertexFormat.getElement(i);
                switch (ele.getUsage()) {
                case UV:
                    Vector2f uv = vertUv[v];
                    builder.put(i, uv.x, uv.y, 0, 1);
                    break;
                case POSITION:
                    Vector3f p = vertPos[v];
                    builder.put(i, p.x, p.y, p.z, 1);
                    break;
                default:
                    builder.put(i, this.builder.data.get(ele.getUsage()).get(v));
                }
            }
        }

        BakedQuad q = builder.build();
        Quad test = from(q, this.builder.vertexFormat);
        return q;
    }
    
    public Quad transformUVs(TextureAtlasSprite sprite) {
        UVs uvs = getUvs().transform(sprite);
        return new Quad(vertPos, uvs, this.builder);
    }
    
    public static Quad from(BakedQuad baked, VertexFormat fmt) {
        Builder b = new Builder(fmt);
        baked.pipe(b);
        return b.build();
    }
    
    @RequiredArgsConstructor
    public static class Builder implements IVertexConsumer {

        @Getter
        private final VertexFormat vertexFormat;

        @Setter
        private int quadTint;

        @Setter
        private EnumFacing quadOrientation;

        @Override
        public void setQuadColored() {}

        private ListMultimap<EnumUsage, float[]> data = MultimapBuilder.enumKeys(EnumUsage.class).arrayListValues().build();
        
        @Override
        public void put(int element, float... data) {
            float[] copy = new float[data.length];
            System.arraycopy(data, 0, copy, 0, data.length);
            VertexFormatElement ele = vertexFormat.getElement(element);
            this.data.put(ele.getUsage(), copy);
        }
        
        public Quad build() {
            Vector3f[] verts = fromData(data.get(EnumUsage.POSITION), 3); 
            Vector2f[] uvs = fromData(data.get(EnumUsage.UV), 2);
            return new Quad(verts, uvs, this);
        }

        @SuppressWarnings("unchecked")
        private <T extends Vector> T[] fromData(List<float[]> data, int size) {
            Vector[] ret = size == 2 ? new Vector2f[data.size()] : new Vector3f[data.size()];
            for (int i = 0; i < data.size(); i++) {
                ret[i] = size == 2 ? new Vector2f(data.get(i)[0], data.get(i)[1]) : new Vector3f(data.get(i)[0], data.get(i)[1], data.get(i)[2]);
            }
            return (T[]) ret;
        }
    }
}
