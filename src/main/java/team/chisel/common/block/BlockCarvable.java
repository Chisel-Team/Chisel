package team.chisel.common.block;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.client.render.ModelChisel;
import team.chisel.client.render.ModelChiselBlock;
import team.chisel.client.util.ClientUtil;
import team.chisel.common.init.ChiselTabs;
import team.chisel.common.util.PropertyAnyInteger;

/**
 * Represents a Carvable (aka Chisilable) block
 */
@ParametersAreNonnullByDefault
public class BlockCarvable extends Block implements ICarvable {

    /**
     * The Property for the variation of this block
     */
    @Getter(onMethod = @__({@Override}))
    private final PropertyAnyInteger metaProp;

    private final int index;

    @Getter
    private final VariationData[] variations;

    private final int maxVariation;

    private final BlockStateContainer states;
    
    public BlockCarvable(Material material, int index, int max, VariationData... variations) {
        super(material);
        setCreativeTab(ChiselTabs.tab);
        this.index = index;
        this.variations = variations;
        this.maxVariation = max;
        this.metaProp = PropertyAnyInteger.create("variation", 0, max > index * 16 ? 15 : max % 16);
        this.states = new BlockStateContainer(this, metaProp);
        setDefaultState(getBlockState().getBaseState());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return Blocks.AIR.getBlockState();
    }

    @Override
    public BlockStateContainer getBlockState() {
        return states;
    }

//    @SideOnly(Side.CLIENT)
//    @Override
//    public IQuadMutator getQuadMutator(){
//        //todo finish this
//    }

    @Override
    public int getVariationIndex(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public int getTotalVariations() {
        return this.maxVariation + 1; // off-by-one
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @SuppressWarnings("null") // No type annotations
    @Override   
    public VariationData getVariationData(int meta) {
        return this.variations[MathHelper.clamp(meta, 0, this.variations.length - 1)];
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getBlockState().getBaseState().withProperty(metaProp, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(metaProp);
    }

    public String getIndexName() {
        if (index == 0) {
            return getUnlocalizedName();
        } else {
            return getUnlocalizedName() + index;
        }
    }

    public static BlockPos pos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        int curIndex = 0;
        for (VariationData var : this.variations) {
            if (var == null) {
                continue;
            }
            ItemStack stack = new ItemStack(item, 1, curIndex);
            curIndex++;
            // CTMBlockResources r = SubBlockUtil.getResources(sub);
            // setLore(stack, r.getLore());
            list.add(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager effectRenderer) {
        ClientUtil.addHitEffects(worldObj, target.getBlockPos(), target.sideHit);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager effectRenderer) {
        ClientUtil.addDestroyEffects(world, pos, world.getBlockState(pos));
        return true;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return super.canRenderInLayer(state, layer);
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.isOpaqueCube();
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return getVariationData(getMetaFromState(state)).opaque;
    }
    
    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return isOpaqueCube(state) ? 255 : 0;
    }
    
    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side) && blockState != blockAccess.getBlockState(pos.offset(side));
    }

    @Override
    public boolean causesSuffocation() {
        return true;
    }
}
