package team.chisel.common.block;

import java.util.List;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
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
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.RenderContextList;
import team.chisel.client.BlockFaceData;
import team.chisel.client.ClientUtil;
import team.chisel.common.init.ChiselTabs;
import team.chisel.common.util.PropertyAnyInteger;
import team.chisel.common.util.PropertyRenderContextList;

/**
 * Represents a Carvable (aka Chisilable) block
 */
public class BlockCarvable extends Block implements ICarvable {

    /**
     * The Property for the variation of this block
     */
    private final PropertyAnyInteger metaProp;

    public static final PropertyRenderContextList CTX_LIST = new PropertyRenderContextList();

    private final int index;

    @SideOnly(Side.CLIENT)
    private BlockFaceData blockFaceData;

    @Getter
    private final VariationData[] variations;

    private final BlockStateContainer realBlockState;

    private final int maxVariation;

    public BlockCarvable(Material material, int index, int max, VariationData... variations) {
        super(material);
        setCreativeTab(ChiselTabs.tab);
        this.index = index;
        this.variations = variations;
        this.maxVariation = max;
        this.metaProp = PropertyAnyInteger.create("variation", 0, max > index * 16 ? 15 : max % 16);
        this.realBlockState = createRealBlockState(metaProp);
        setupStates();
        Chisel.proxy.initiateFaceData(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockFaceData getBlockFaceData() {
        return this.blockFaceData;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setBlockFaceData(BlockFaceData data) {
        this.blockFaceData = data;
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
    public BlockStateContainer getBlockState() {
        return this.realBlockState;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override   
    public VariationData getVariationData(int meta) {
        return this.variations[MathHelper.clamp_int(meta, 0, this.variations.length - 1)];
    }

    private BlockStateContainer createRealBlockState(PropertyAnyInteger p) {
        return new ExtendedBlockState(this, new IProperty[] { p }, new IUnlistedProperty[] { CTX_LIST });
    }

    @Override
    public BlockStateContainer createBlockState() {
        return Blocks.AIR.getBlockState();
    }

    private void setupStates() {
        IBlockState state = getExtendedBlockState().withProperty(CTX_LIST, new RenderContextList()).withProperty(metaProp, 0);
        state = state.withProperty(metaProp, 0);
        this.setDefaultState(state);
    }

    public ExtendedBlockState getBaseExtendedState() {
        return (ExtendedBlockState) this.getBlockState();
    }

    public IExtendedBlockState getExtendedBlockState() {
        return (IExtendedBlockState) this.getBaseExtendedState().getBaseState();
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

    @SideOnly(Side.CLIENT)
    @Override
    public IBlockState getExtendedState(IBlockState stateIn, IBlockAccess w, BlockPos pos) {
        if (stateIn.getBlock() == null || stateIn.getMaterial() == Material.AIR) {
            return stateIn;
        }
        IExtendedBlockState state = (IExtendedBlockState) stateIn;
        List<IBlockRenderType> types = this.blockFaceData.getForMeta(getMetaFromState(state)).getTypesUsed();

        RenderContextList ctxList = new RenderContextList(types, w, pos);

        return state.withProperty(CTX_LIST, ctxList);
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
    public boolean canRenderInLayer(BlockRenderLayer layer) {
        return this.blockFaceData.isValid(layer);
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
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState other = blockAccess.getBlockState(pos.offset(side));
        if (other instanceof IExtendedBlockState) {
            other = ((IExtendedBlockState)other).getClean();
        }
        return ((IExtendedBlockState)blockState).getClean() != other;
    }
    
    @Override
    public boolean isVisuallyOpaque() {
        return true;
    }
}
