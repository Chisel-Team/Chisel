package team.chisel.client.render;

import java.util.List;

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

import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.ctm.ISubmap;

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

    private static final TextureAtlasSprite BASE = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TextureMap.LOCATION_MISSING_TEXTURE.toString());
    
    @RequiredArgsConstructor
    @ToString
    public class UVs {
        
        @Getter
        final float minU, minV, maxU, maxV;
        
        @Getter
        private final TextureAtlasSprite sprite;
        
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

        public UVs transform(TextureAtlasSprite other, ISubmap submap) {
            UVs normal = normalize();
            submap = submap.normalize();
            
            float width = normal.maxU - normal.minU;
            float height = normal.maxV - normal.minV;
            
            float minU = submap.getXOffset();
            float minV = submap.getYOffset();
            minU += normal.minU * submap.getWidth();
            minV += normal.minV * submap.getHeight();
            
            float maxU = minU + (width * submap.getWidth());
            float maxV = minV + (height * submap.getHeight());
            
            return new UVs(minU, minV, maxU, maxV, other).relativize();
        }

        UVs normalizeQuadrant() {
            UVs normal = normalize();
            float minU = normal.minU, minV = normal.minV;
            float maxU = normal.maxU, maxV = normal.maxV;

            int quadrant = normal.getQuadrant();
            float minUInterp = quadrant == 1 || quadrant == 2 ? 0.5f : 0; 
            float minVInterp = quadrant < 2 ? 0.5f : 0; 
            float maxUInterp = quadrant == 0 || quadrant == 3 ? 0.5f : 1;
            float maxVInterp = quadrant > 1 ? 0.5f : 1;
            
            minU = Quad.normalize(minUInterp, maxUInterp, minU);
            minV = Quad.normalize(minVInterp, maxVInterp, minV);
            maxU = Quad.normalize(minUInterp, maxUInterp, maxU);
            maxV = Quad.normalize(minVInterp, maxVInterp, maxV);
            
            normal = new UVs(minU, minV, maxU, maxV, sprite);
            return normal.relativize();
        }
        
        public UVs normalize() {
            return new UVs(Quad.normalize(sprite.getMinU(), sprite.getMaxU(), this.minU), Quad.normalize(sprite.getMinV(), sprite.getMaxV(), this.minV), Quad.normalize(sprite.getMinU(),
                    sprite.getMaxU(), this.maxU), Quad.normalize(sprite.getMinV(), sprite.getMaxV(), this.maxV), sprite);
        }

        public UVs relativize() {
            return relativize(sprite);
        }

        public UVs relativize(TextureAtlasSprite sprite) {
            return new UVs(lerp(sprite.getMinU(), sprite.getMaxU(), this.minU), lerp(sprite.getMinV(), sprite.getMaxV(), this.minV), lerp(sprite.getMinU(), sprite.getMaxU(), this.maxU), lerp(
                    sprite.getMinV(), sprite.getMaxV(), this.maxV), sprite);
        }

        public Vector2f[] vectorize() {
            return new Vector2f[]{ new Vector2f(minU, minV), new Vector2f(minU, maxV), new Vector2f(maxU, maxV), new Vector2f(maxU, minV) };
        }
        
        public int getQuadrant() {
            if (maxU <= 0.5f) {
                if (maxV <= 0.5f) {
                    return 3;
                } else {
                    return 0;
                }
            } else {
                if (maxV <= 0.5f) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }
    }

    private final Vector3f[] vertPos;
    private final Vector2f[] vertUv;
        
    // Technically nonfinal, but treated as such except in constructor
    @Getter
    private UVs uvs;
    
    private final Builder builder;
    
    private Quad(Vector3f[] verts, Vector2f[] uvs, Builder builder) {
        this.vertPos = verts;
        this.vertUv = uvs;
        this.builder = builder;
        this.uvs = new UVs(uvs);
    }
    
    private Quad(Vector3f[] verts, UVs uvs, Builder builder) {
        this(verts, uvs.vectorize(), builder);
        this.uvs = new UVs(uvs.getSprite(), vertUv);
    }

    public void compute() {

    }

    public Quad[] subdivide(int count) {
        List<Quad> rects = Lists.newArrayList();

        Pair<Quad, Quad> firstDivide = divide(false);
        Pair<Quad, Quad> secondDivide = firstDivide.getLeft().divide(true);
        rects.add(secondDivide.getLeft());

        if (firstDivide.getRight() != null) {
            Pair<Quad, Quad> thirdDivide = firstDivide.getRight().divide(true);
            rects.add(thirdDivide.getLeft());
            rects.add(thirdDivide.getRight());
        } else {
            rects.add(null);
            rects.add(null);
        }

        rects.add(secondDivide.getRight());

        return rects.toArray(new Quad[rects.size()]);
    }
    
    @Nullable
    private Pair<Quad, Quad> divide(boolean vertical) {
        float min, max;
        UVs uvs = getUvs().normalize();
        if (vertical) {
            min = uvs.minV;
            max = uvs.maxV;
        } else {
            min = uvs.minU;
            max = uvs.maxU;
        }
        if (min < 0.5 && max > 0.5) {
            UVs first = new UVs(vertical ? uvs.minU : 0.5f, vertical ? 0.5f : uvs.minV, uvs.maxU, uvs.maxV, uvs.getSprite());
            UVs second = new UVs(uvs.minU, uvs.minV, vertical ? uvs.maxU : 0.5f, vertical ? 0.5f : uvs.maxV, uvs.getSprite());
                        
            int firstIndex = 0;
            for (int i = 0; i < vertUv.length; i++) {
                if (vertUv[i].y == getUvs().minV && vertUv[i].x == getUvs().minU) {
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
            
            int i1 = 0;
            int i2 = vertical ? 1 : 3;
            int j1 = vertical ? 3 : 1;
            int j2 = 2;
            
            firstQuad[i1].x = lerp(firstQuad[i1].x, firstQuad[i2].x, f);
            firstQuad[i1].y = lerp(firstQuad[i1].y, firstQuad[i2].y, f);
            firstQuad[i1].z = lerp(firstQuad[i1].z, firstQuad[i2].z, f);
            firstQuad[j1].x = lerp(firstQuad[j1].x, firstQuad[j2].x, f);
            firstQuad[j1].y = lerp(firstQuad[j1].y, firstQuad[j2].y, f);
            firstQuad[j1].z = lerp(firstQuad[j1].z, firstQuad[j2].z, f);
            
            secondQuad[i2].x = lerp(secondQuad[i1].x, secondQuad[i2].x, f);
            secondQuad[i2].y = lerp(secondQuad[i1].y, secondQuad[i2].y, f);
            secondQuad[i2].z = lerp(secondQuad[i1].z, secondQuad[i2].z, f);
            secondQuad[j2].x = lerp(secondQuad[j1].x, secondQuad[j2].x, f);
            secondQuad[j2].y = lerp(secondQuad[j1].y, secondQuad[j2].y, f);
            secondQuad[j2].z = lerp(secondQuad[j1].z, secondQuad[j2].z, f);

            Quad q1 = new Quad(firstQuad, first.relativize(), builder);
            Quad q2 = new Quad(secondQuad, second.relativize(), builder);
            return Pair.of(q1, q2);
        } else {
            return Pair.of(this, null);
        }
    }
    
    static float lerp(float a, float b, float f) {
        float ret = (a * (1 - f)) + (b * f);
        return ret;
    }
    
    static float normalize(float min, float max, float x) {
        float ret = (x - min) / (max - min);
        return ret;
    }

    public Quad derotate() {
        Vector2f[] uvs = new Vector2f[4];
        int start = 0;
        for (int i = 0; i < 4; i++) {
            if (vertUv[i].x <= getUvs().minU && vertUv[i].y <= getUvs().minV) {
                start = i;
                break;
            }
        }

        for (int i = 0; i < 4; i++) {
            TextureAtlasSprite s = getUvs().getSprite();
            Vector2f normalized = new Vector2f(normalize(s.getMinU(), s.getMaxU(), vertUv[i].x), normalize(s.getMinV(), s.getMaxV(), vertUv[i].y));
            Vector2f uv;
            switch (start) {
            case 1:
                uv = new Vector2f(normalized.y, 1 - normalized.x);
                break;
            case 2:
                uv = new Vector2f(1 - normalized.x, 1 - normalized.y);
                break;
            case 3:
                uv = new Vector2f(1 - normalized.y, normalized.x);
                break;
            default:
                uv = new Vector2f(normalized.x, normalized.y);
                break;
            }
            uvs[i] = uv;
        }

        return new Quad(vertPos, new UVs(getUvs().getSprite(), uvs).relativize(), builder);
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

        return builder.build();
    }
    
    public Quad transformUVs(TextureAtlasSprite sprite) {
        return transformUVs(sprite, CTM.FULL_TEXTURE.normalize());
    }
    
    public Quad transformUVs(TextureAtlasSprite sprite, ISubmap submap) {
        return new Quad(vertPos, getUvs().transform(sprite, submap), builder);
    }
    
    public Quad grow() {
        return new Quad(vertPos, getUvs().normalizeQuadrant(), builder);
    }
    
    public static Quad from(BakedQuad baked, VertexFormat fmt) {
        Builder b = new Builder(fmt);
        baked.pipe(b);
        return b.build().derotate(); // for now we will ignore rotated UVs
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
