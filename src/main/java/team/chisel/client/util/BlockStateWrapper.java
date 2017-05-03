package team.chisel.client.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * @author WireSegal
 *         Created at 9:03 PM on 5/2/17.
 */
public class BlockStateWrapper implements IBlockState {
    private IBlockState parent;

    public BlockStateWrapper withParent(IBlockState parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public Collection<IProperty<?>> getPropertyNames() {
        return parent.getPropertyNames();
    }

    @Override
    public <T extends Comparable<T>> T getValue(IProperty<T> property) {
        return parent.getValue(property);
    }

    @Override
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
        return parent.withProperty(property, value);
    }

    @Override
    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
        return parent.cycleProperty(property);
    }

    @Override
    public ImmutableMap<IProperty<?>, Comparable<?>> getProperties() {
        return parent.getProperties();
    }

    @Override
    public Block getBlock() {
        return parent.getBlock();
    }

    @Override
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param) {
        return parent.onBlockEventReceived(worldIn, pos, id, param);
    }

    @Override
    public void neighborChanged(World worldIn, BlockPos pos, Block blockIn) {
        parent.neighborChanged(worldIn, pos, blockIn);
    }

    @Override
    public Material getMaterial() {
        return parent.getMaterial();
    }

    @Override
    public boolean isFullBlock() {
        return parent.isFullBlock();
    }

    @Override
    public boolean canEntitySpawn(Entity entityIn) {
        return parent.canEntitySpawn(entityIn);
    }

    @Override
    public int getLightOpacity() {
        return parent.getLightOpacity();
    }

    @Override
    public int getLightOpacity(IBlockAccess world, BlockPos pos) {
        return parent.getLightOpacity(world, pos);
    }

    @Override
    public int getLightValue() {
        return parent.getLightValue();
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        return parent.getLightValue(world, pos);
    }

    @Override
    public boolean isTranslucent() {
        return parent.isTranslucent();
    }

    @Override
    public boolean useNeighborBrightness() {
        return parent.useNeighborBrightness();
    }

    @Override
    public MapColor getMapColor() {
        return parent.getMapColor();
    }

    @Override
    public IBlockState withRotation(Rotation rot) {
        return parent.withRotation(rot);
    }

    @Override
    public IBlockState withMirror(Mirror mirrorIn) {
        return parent.withMirror(mirrorIn);
    }

    @Override
    public boolean isFullCube() {
        return parent.isFullCube();
    }

    @Override
    public EnumBlockRenderType getRenderType() {
        return parent.getRenderType();
    }

    @Override
    public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos) {
        return parent.getPackedLightmapCoords(source, pos);
    }

    @Override
    public float getAmbientOcclusionLightValue() {
        return parent.getAmbientOcclusionLightValue();
    }

    @Override
    public boolean isBlockNormalCube() {
        return parent.isBlockNormalCube();
    }

    @Override
    public boolean isNormalCube() {
        return parent.isNormalCube();
    }

    @Override
    public boolean canProvidePower() {
        return parent.canProvidePower();
    }

    @Override
    public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return parent.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return parent.hasComparatorInputOverride();
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return parent.getComparatorInputOverride(worldIn, pos);
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos) {
        return parent.getBlockHardness(worldIn, pos);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos) {
        return parent.getPlayerRelativeBlockHardness(player, worldIn, pos);
    }

    @Override
    public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return parent.getStrongPower(blockAccess, pos, side);
    }

    @Override
    public EnumPushReaction getMobilityFlag() {
        return parent.getMobilityFlag();
    }

    @Override
    public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos) {
        return parent.getActualState(blockAccess, pos);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return parent.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing) {
        return parent.shouldSideBeRendered(blockAccess, pos, facing);
    }

    @Override
    public boolean isOpaqueCube() {
        return parent.isOpaqueCube();
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos) {
        return parent.getCollisionBoundingBox(worldIn, pos);
    }

    @Override
    public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB p_185908_3_, List<AxisAlignedBB> p_185908_4_, @Nullable Entity p_185908_5_) {
        parent.addCollisionBoxToList(worldIn, pos, p_185908_3_, p_185908_4_, p_185908_5_);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos) {
        return parent.getBoundingBox(blockAccess, pos);
    }

    @Override
    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return parent.collisionRayTrace(worldIn, pos, start, end);
    }

    @Override
    public boolean isFullyOpaque() {
        return parent.isFullyOpaque();
    }

    @Override
    public boolean doesSideBlockRendering(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return parent.doesSideBlockRendering(world, pos, side);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return parent.isSideSolid(world, pos, side);
    }
}
