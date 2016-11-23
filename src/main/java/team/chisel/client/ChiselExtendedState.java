package team.chisel.client;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import lombok.Getter;
import lombok.experimental.Delegate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.RenderContextList;
import team.chisel.client.render.ModelChisel;

@ParametersAreNonnullByDefault
public class ChiselExtendedState extends BlockStateBase implements IExtendedBlockState {

    interface Exclusions {
        public <T extends Comparable<T>> T getValue(IProperty<T> property);
        
        public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value);
        
        public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property);
    }
    
    @Delegate(excludes = Exclusions.class)
    private final IBlockState wrapped;
    private final IBlockState clean;
    
    private final boolean extended;
    private final @Nullable IExtendedBlockState extState;
    
    @Getter
    private final IBlockAccess world;
    @Getter
    private final BlockPos pos;
    
    private @Nullable RenderContextList ctxCache;
    
    @SuppressWarnings("null")
    public ChiselExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        this.wrapped = state;
        this.world = world;
        this.pos = pos;
        
        this.extended = wrapped instanceof IExtendedBlockState;
        if (extended) {
            extState = (IExtendedBlockState) wrapped;
            clean = extState.getClean();
        } else {
            extState = null;
            clean = wrapped;
        }
    }

    public ChiselExtendedState(IBlockState state, ChiselExtendedState parent) {
        this(state, parent.world, parent.pos);
    }
    
    @SuppressWarnings("null")
    public RenderContextList getContextList(IBlockState state, ModelChisel model) {
        if (ctxCache == null) {
            ctxCache = new RenderContextList(state, model.getChiselTextures().stream().map(IChiselTexture::getType).collect(Collectors.toList()), world, pos);
        }
        return ctxCache;
    }

    @Override
    public @Nullable Collection<IUnlistedProperty<?>> getUnlistedNames() {
        return extended ? extState.getUnlistedNames() : Collections.emptyList();
    }

    @Override
    public @Nullable <V> V getValue(@Nullable IUnlistedProperty<V> property) {
        return extended ? extState.getValue(property) : null;
    }

    @Override
    public <V> IExtendedBlockState withProperty(@Nullable IUnlistedProperty<V> property, @Nullable V value) {
        return extended ? new ChiselExtendedState(extState.withProperty(property, value), this) : this;
    }

    @Override
    public @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> getUnlistedProperties() {
        return extended ? extState.getUnlistedProperties() : ImmutableMap.of();
    }

    @Override
    public IBlockState getClean() {
        return clean;
    }
    
    // Lombok chokes on these for some reason

    @Override
    public <T extends Comparable<T>> T getValue(IProperty<T> property) {
        return wrapped.getValue(property);
    }

    @Override
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
        return new ChiselExtendedState(wrapped.withProperty(property, value), this);
    }

    @Override
    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
        return new ChiselExtendedState(wrapped.cycleProperty(property), this);
    }
    
}
