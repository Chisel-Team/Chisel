package team.chisel.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;
import team.chisel.api.block.ChiselBlockData;
import team.chisel.api.block.ClientVariationData;
import team.chisel.api.block.VariationData;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.RenderContextList;
import team.chisel.client.BlockFaceData;
import team.chisel.common.init.ChiselTabs;
import team.chisel.common.util.PropertyRenderContextList;

/**
 * Represents a Carvable (aka Chisilable) block
 */
public class BlockCarvable extends Block implements ICarvable {

    /**
     * The Property for the variation of this block
     */
    public PropertyInteger metaProp;

    public static final PropertyRenderContextList CTX_LIST = new PropertyRenderContextList();

    private ChiselBlockData data;

    @SideOnly(Side.CLIENT)
    private BlockFaceData blockFaceData;

    private int index;

    private BlockState realBlockState;

    private int metaVariations;

    public BlockCarvable(ChiselBlockData data, int index){
        super(data.material);
        setCreativeTab(ChiselTabs.tab);
        this.data = data;
        this.index = index;
        int max;
        if (data.variations.length >= 16){
            max = data.variations.length % 16;
        }
        else {
            max = data.variations.length;
        }
        this.metaVariations = max;
        this.metaProp = PropertyInteger.create("Variation", 0, max);
        this.realBlockState = createRealBlockState(metaProp);
        setupStates();
        setUnlocalizedName(data.name);
        setHardness(data.hardness);
        Chisel.proxy.initiateFaceData(this);
    }

    @SideOnly(Side.CLIENT)
    public BlockFaceData getBlockFaceData(){
        return this.blockFaceData;
    }

    @SideOnly(Side.CLIENT)
    public void setBlockFaceData(BlockFaceData data){
        this.blockFaceData = data;
    }

    public int getTotalMetaVariations(){
        return this.metaVariations;
    }

    @Override
    public BlockState getBlockState() {
        return this.realBlockState;
    }

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public VariationData getVariationData(int meta) {
		return this.data.variations[MathHelper.clamp_int(meta, 0, this.data.variations.length)];
	}

	public int getTotalVariations() {
		return this.data.variations.length - (16 * getIndex());
	}

	private BlockState createRealBlockState(PropertyInteger p) {
		return new ExtendedBlockState(this, new IProperty[] { p }, new IUnlistedProperty[] { CTX_LIST });
	}

    @Override
    public BlockState createBlockState() {
        return Blocks.air.getBlockState();
    }

    private void setupStates() {
        this.setDefaultState(getExtendedBlockState()
        	.withProperty(CTX_LIST, new RenderContextList())
        	.withProperty(metaProp, 0));
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


    @Override
    public boolean equals(Object object) {
        if (object instanceof BlockCarvable) {
            BlockCarvable carv = (BlockCarvable) object;
            return carv.getUnlocalizedName().equals(this.getUnlocalizedName());
        }
        return false;
    }



    /**
     * Name used for texture path
     *
     * @return The Name
     */
    public String getName() {
        return this.data.name;
    }

    public String getIndexName(){
        if (index == 0){
            return getName();
        }
        else {
            return getName()+index;
        }
    }

    /**
     * Gets the variation data for this block
     *
     * @return The Data
     */
    public ChiselBlockData getBlockData() {
        return this.data;
    }

    @SideOnly(Side.CLIENT)
	@Override
	public IBlockState getExtendedState(IBlockState stateIn, IBlockAccess w, BlockPos pos) {
		if (stateIn.getBlock() == null || stateIn.getBlock().getMaterial() == Material.air) {
			return stateIn;
		}
		IExtendedBlockState state = (IExtendedBlockState) stateIn;
        List<IBlockRenderType> types = this.blockFaceData.getForMeta(state.getValue(metaProp)).getTypesUsed();

        RenderContextList ctxList = new RenderContextList(types, w, pos);

		return state.withProperty(CTX_LIST, ctxList);
	}

    public static BlockPos pos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        return getVariationData(world.getBlockState(pos).getValue(metaProp)).light;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        int curIndex = 0;
        for (VariationData var : this.data.variations) {
            if (var == null) {
                continue;
            }
            ItemStack stack = new ItemStack(item, 1, curIndex);
            curIndex++;
            //CTMBlockResources r = SubBlockUtil.getResources(sub);
            //setLore(stack, r.getLore());
            list.add(stack);
        }
    }

    @Override
    public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon){
        return getVariationData(world.getBlockState(pos).getValue(metaProp)).beaconBase;
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos){
        return getVariationData(worldIn.getBlockState(pos).getValue(metaProp)).hardness;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean isOpaqueCube() {
        if (this.data != null) {
            return this.data.isOpaqueCube;
        }
        else {
            return true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInLayer(EnumWorldBlockLayer layer){
        return this.blockFaceData.isValid(layer);
    }
    @Override
    public boolean isFullBlock(){
        return isOpaqueCube();
    }

    @Override
    public boolean isFullCube() {
        return true;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return true;
    }
}
