package team.chisel.api.render;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;

public interface IModelChisel extends IModel {

    void load();

    Collection<IChiselTexture<?>> getChiselTextures();

    IBakedModel getModel(IBlockState state);

    IChiselFace getFace(EnumFacing facing);

    IChiselFace getDefaultFace();

    boolean ignoreStates();

    boolean canRenderInLayer(BlockRenderLayer layer);

}
