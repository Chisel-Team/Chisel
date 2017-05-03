package team.chisel.client.util;

import com.google.common.collect.Lists;
import gnu.trove.map.hash.TLongObjectHashMap;
import lombok.Setter;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;
import team.chisel.api.render.IModelChisel;
import team.chisel.client.ClientUtil;
import team.chisel.client.render.ModelChisel;
import team.chisel.client.render.texture.MetadataSectionChisel;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static team.chisel.client.ClientUtil.getMetadata;

@MethodsReturnNonnullByDefault
public final class MetadataWrapper implements IBakedModel {

    private IBakedModel parent;

    public MetadataWrapper withParent(IBakedModel parent) {
        this.parent = parent;
        return this;
    }

    @SuppressWarnings({"ConstantConditions", "Convert2streamapi"})
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> parentQuads = parent.getQuads(state, side, rand);
        List<BakedQuad> filtered = Lists.newArrayList();
        if (parentQuads == null) return filtered;
        for (BakedQuad quad : parentQuads) {
            String type = getType(quad);
            if (type != null) {
                if (type.equals("no_shade")) {
                    BakedQuad newQuad = new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), quad.getSprite(), false, quad.getFormat());
                    filtered.add(newQuad);
                } else filtered.add(quad);
            }
        }
        return filtered;
    }

    private String getType(BakedQuad quad) {
        try {
            MetadataSectionChisel metadata = ClientUtil.getMetadata(quad.getSprite());
            if (metadata != null)
                return metadata.getGlowType();
        } catch (RuntimeException e) {
            // NO-OP
        }
        return null;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return parent.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return parent.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return parent.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return parent.getParticleTexture();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms() {
        return parent.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return parent.getOverrides();
    }
}
