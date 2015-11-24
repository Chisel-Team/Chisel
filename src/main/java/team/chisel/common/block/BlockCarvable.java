package team.chisel.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.client.render.BlockResources;
import team.chisel.client.render.IBlockResources;
import team.chisel.api.render.RenderType;
import team.chisel.client.render.ctm.CTM;
import team.chisel.common.CarvableBlocks;
import team.chisel.common.block.subblocks.ISubBlock;
import team.chisel.common.connections.CTMConnections;
import team.chisel.common.connections.EnumConnection;
import team.chisel.common.connections.PropertyCTMConnections;
import team.chisel.common.init.ChiselTabs;
import team.chisel.common.util.ChiselBlock;
import team.chisel.common.util.SubBlockUtil;
import team.chisel.common.variation.PropertyVariation;
import team.chisel.common.variation.Variation;

/**
 * Represents a Carvable (aka Chisilable) block
 */
public class BlockCarvable extends Block implements ICarvable {

    /**
     * The Property for the variation of this block
     */
    public PropertyVariation variation;

    public static final PropertyCTMConnections CONNECTIONS = new PropertyCTMConnections();
    
    /**
     * X Y and Z modules for coordinate variation
     */
    public static final IUnlistedProperty<Integer> XMODULES = Properties.toUnlisted(PropertyInteger.create("X Modules", 0, 4));
    public static final IUnlistedProperty<Integer> YMODULES = Properties.toUnlisted(PropertyInteger.create("V Section", 0, 4));
    public static final IUnlistedProperty<Integer> ZMODULES = Properties.toUnlisted(PropertyInteger.create("Z Section", 0, 4));

    private ChiselBlock type;

    /**
     * Array of all the block resources, each one represents a sub block
     */
    private ISubBlock[] subBlocks;

    private int index;

    private BlockState realBlockState;

    private final CTM ctm = CTM.getInstance();

    public BlockCarvable(ChiselBlock type, int index, PropertyVariation p){
        super(type.getData().material);
        this.subBlocks = new ISubBlock[type.getVariations().length];
        this.type = type;
        this.index = index;
        this.variation = p;
        this.realBlockState = createRealBlockState(p);
        setupStates();
        setUnlocalizedName(type.getData().name);
        setHardness(type.getData().hardness);

    }

    public BlockCarvable(CarvableBlocks type, int subBlocksAmount, int index, PropertyVariation p, boolean isBeaconBase) {
        this(Material.rock, type, subBlocksAmount, index, p, isBeaconBase);
    }

    public BlockCarvable(Material material, CarvableBlocks type, int subBlocksAmount, int index, PropertyVariation p, boolean isBeaconBase) {
        super(material);
        this.isBeaconBase = isBeaconBase;
        subBlocks = new ISubBlock[subBlocksAmount];
        this.type = type;
        this.index = index;
        this.variation = p;
        this.fullBlock = isOpaqueCube();
        this.realBlockState = createRealBlockState(p);
        setupStates();
        setResistance(10.0F);
        setHardness(2.0F);
        setCreativeTab(ChiselTabs.tab);
        setUnlocalizedName(type.getName());
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
	public ISubBlock getSubBlock(int meta) {
		return subBlocks[MathHelper.clamp_int(meta, 0, subBlocks.length)];
	}

	public int getTotalVariations() {
		return getType().getVariants().length - (16 * getIndex());
	}

	private BlockState createRealBlockState(PropertyVariation p) {
		return new ExtendedBlockState(this, new IProperty[] { p }, new IUnlistedProperty[] { CONNECTIONS });
	}

    @Override
    public BlockState createBlockState() {
        return Blocks.air.getBlockState();
    }

    private void setupStates() {
        Variation v = type.getVariants()[getIndex() * 16];
        this.setDefaultState(getExtendedBlockState()
        	.withProperty(CONNECTIONS, new CTMConnections())
        	.withProperty(variation, v));
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
        Variation v = Variation.fromMeta(type, meta, getIndex());
        return getBlockState().getBaseState().withProperty(variation, v);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return Variation.metaFromVariation(type, (Variation) state.getValue(variation));
    }

    /**
     * Add a sub block
     *
     * @param subBlock
     */
    public void addSubBlock(ISubBlock subBlock) {
        if (!hasSubBlock(subBlock)) {
            for (int i = 0; i < subBlocks.length; i++) {
                if (subBlocks[i] == null) {
                    subBlocks[i] = subBlock;
                    //Chisel.logger.info("Adding Sub Block "+subBlock.getName()+" to "+this.getName());
                    return;
                }
            }
        }
    }

    private boolean hasSubBlock(ISubBlock subBlock) {
        if (subBlock == null) {
            return true;
        }
        for (ISubBlock block : subBlocks) {
            if (block == null) {
                continue;
            }
            if (block.equals(subBlock)) {
                return true;
            }
        }
        return false;
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
     * Get the sub block from the variation
     *
     * @param v the variation
     * @return The sub block
     */
    public ISubBlock getSubBlock(Variation v) {
        //Chisel.logger.info("meta: "+Variation.metaFromVariation(type, v));
        ISubBlock block = subBlocks[Variation.metaFromVariation(type, v)];
        //Chisel.logger.info("Returning sub block "+block.getName());
        return block;
    }

    public ISubBlock[] allSubBlocks() {
        return this.subBlocks;
    }


    /**
     * Name used for texture path
     *
     * @return The Name
     */
    public String getName() {
        return this.type.getName();
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
     * Gets the type of block this is
     *
     * @return The Type
     */
    public CarvableBlocks getType() {
        return this.type;
    }

    @SideOnly(Side.CLIENT)
	@Override
	public IBlockState getExtendedState(IBlockState stateIn, IBlockAccess w, BlockPos pos) {
		if (stateIn.getBlock() == null || stateIn.getBlock().getMaterial() == Material.air) {
			return stateIn;
		}
		IExtendedBlockState state = (IExtendedBlockState) stateIn;
		Variation v = ((BlockCarvable) state.getBlock()).getType().getVariants()[state.getBlock().getMetaFromState(state)];
		IBlockResources res = SubBlockUtil.getResources(state.getBlock(), v);
		if (res.getType() == RenderType.V4 || res.getType() == RenderType.V9) {
			int variationSize = BlockResources.getVariationWidth(res.getType());
			int xModulus = Math.abs(pos.getX() % variationSize);
			int zModulus = Math.abs(pos.getZ() % variationSize);
			int yModules = Math.abs(pos.getY() % variationSize);
			return state.withProperty(XMODULES, xModulus).withProperty(YMODULES, yModules).withProperty(ZMODULES, zModulus);
		} else if (res.getType() == RenderType.NORMAL || res.getType() == RenderType.R9 || res.getType() == RenderType.R4 || res.getType() == RenderType.R16) {
			return stateIn;
		}
		List<EnumConnection> connections = new ArrayList<EnumConnection>();
		for (EnumConnection connection : EnumConnection.values()) {
			if (ctm.areBlocksEqual(state, connection.getBlockAt(pos, w), variation)) {
				connections.add(connection);
			}
		}

		return state.withProperty(CONNECTIONS, new CTMConnections(connections));
	}

    public static BlockPos pos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        return super.getMixedBrightnessForBlock(worldIn, pos);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        int curIndex = 0;
        for (ISubBlock sub : subBlocks) {
            if (sub == null) {
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
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean isOpaqueCube() {
        return type.getData().isOpaqueCube;
    }

    @Override
    public boolean isFullCube() {
        return true;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return true;
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return this.type.getData().beaconBase;
    }


    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
		type.getHandler().onEntityCollidedWithBlock(worldIn, pos, entityIn);
	}
}
