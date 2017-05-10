package team.chisel.api.render;

import java.util.Collection;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IModel;

public interface IModelChisel extends IModel {

    void load();

    Collection<IChiselTexture<?>> getChiselTextures();
    
    IChiselTexture<?> getTexture(String iconName);

    IChiselFace getFace(EnumFacing facing);

    IChiselFace getDefaultFace();

    boolean ignoreStates();

    boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer);

}
