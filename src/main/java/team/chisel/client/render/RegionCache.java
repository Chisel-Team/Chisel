package team.chisel.client.render;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

/**
 * Used by render state creation to avoid unnecessary block lookups through the world.
 */
@ParametersAreNonnullByDefault
public class RegionCache implements IBlockAccess {

    /*
     * XXX
     * 
     * These are required for future use, in case there is ever a need to have this region cache only store a certain area of the world.
     * 
     * Currently, this class is only used by CTM, which is limited to a very small subsection of the world, 
     * and thus the overhead of distance checking is unnecessary.
     */
    @SuppressWarnings("unused")
    private final BlockPos center;
    @SuppressWarnings("unused")
    private final int radius;
    
    private final IBlockAccess passthrough;
    private final Function<BlockPos, IBlockState> lookupFunc;
    private final Map<BlockPos, IBlockState> stateCache = new HashMap<>();

    public RegionCache(BlockPos center, int radius, IBlockAccess passthrough) {
        this.center = center;
        this.radius = radius;
        this.passthrough = passthrough;
        this.lookupFunc = passthrough::getBlockState;
    }

    @Override
    @Nullable
    public TileEntity getTileEntity(BlockPos pos) {
        return passthrough.getTileEntity(pos);
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        // In cases with direct passthroughs, these are never used by our code.
        // But in case something out there does use them, this will work
        return passthrough.getCombinedLight(pos, lightValue);
    }

    @SuppressWarnings("null")
    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return stateCache.computeIfAbsent(pos, lookupFunc);
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        IBlockState state = getBlockState(pos);
        return state.getBlock().isAir(state, this, pos);
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return passthrough.getBiome(pos);
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return passthrough.getStrongPower(pos, direction);
    }

    @Override
    public WorldType getWorldType() {
        return passthrough.getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        return passthrough.isSideSolid(pos, side, _default);
    }
}
