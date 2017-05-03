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
        if (state != null && !state.getBlock().getUnlocalizedName().startsWith("chisel") && parent instanceof IModelChisel)
            System.out.println("oh no");
        List<BakedQuad> parentQuads = parent.getQuads(state, side, rand);
        List<BakedQuad> filtered = Lists.newArrayList();
        if (parentQuads == null) return filtered;
        for (BakedQuad quad : parentQuads) if (predicateQuad(quad)) filtered.add(quad);
        return filtered;
    }

    private boolean predicateQuad(BakedQuad quad) {
        try {
            MetadataSectionChisel metadata = ClientUtil.getMetadata(quad.getSprite());
            return metadata != null && metadata.isGlow();
        } catch (RuntimeException e) {
            return false;
        }
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
