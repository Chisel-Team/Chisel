package com.cricketcraft.chisel.common.block;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.IFacade;
import com.cricketcraft.chisel.client.render.ctm.CTMModelRegistry;
import com.cricketcraft.chisel.common.block.subblocks.ISubBlock;
import com.cricketcraft.chisel.common.init.ChiselTabs;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

/**
 * Represents a Carvable (aka Chisilable) block
 */
public abstract class BlockCarvable extends Block{

    /**
     * The Property for the variation of this block
     */
    public static final PropertyInteger VARIATION = PropertyInteger.create("Variation", 0, 16);

    /**
     * These are used for connected textures
     */
    public static final IUnlistedProperty CONNECTED_DOWN = Properties.toUnlisted(PropertyBool.create("Connected Down"));
    public static final IUnlistedProperty CONNECTED_UP = Properties.toUnlisted(PropertyBool.create("Connected Up"));
    public static final IUnlistedProperty CONNECTED_NORTH = Properties.toUnlisted(PropertyBool.create("Connected North"));
    public static final IUnlistedProperty CONNECTED_SOUTH = Properties.toUnlisted(PropertyBool.create("Connected South"));
    public static final IUnlistedProperty CONNECTED_EAST = Properties.toUnlisted(PropertyBool.create("Connected East"));
    public static final IUnlistedProperty CONNECTED_WEST = Properties.toUnlisted(PropertyBool.create("Connected West"));
    /**
     * For connected textures corners 12
     */
    public static final IUnlistedProperty CONNECTED_NORTH_EAST = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_NORTH_WEST = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_NORTH_UP = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_NORTH_DOWN = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_SOUTH_EAST = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_SOUTH_WEST = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_SOUTH_UP = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_SOUTH_DOWN = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_EAST_UP = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_EAST_DOWN = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_WEST_UP = Properties.toUnlisted(PropertyBool.create("Connected North East"));
    public static final IUnlistedProperty CONNECTED_WEST_DOWN = Properties.toUnlisted(PropertyBool.create("Connected North East"));



    private String name;

    /**
     * Array of all the block resources, each one represents a sub block
     */
    private ISubBlock[] subBlocks = new ISubBlock[16];

    public BlockCarvable(){
        this("defaultChiselBlock");
    }

    public BlockCarvable(String name){
        this(Material.rock, name, false);
    }

    public BlockCarvable(Material material, String name, boolean isCTM){
        super(material);
        setupStates();
        setResistance(10.0F);
        setHardness(2.0F);
        setCreativeTab(ChiselTabs.tabOtherChiselBlocks);
        setUnlocalizedName(name);
        this.name=name;
        if (isCTM) {
            CTMModelRegistry.register(this);
        }
    }

    @Override
    public BlockState createBlockState(){
        return new ExtendedBlockState(this, new IProperty[]{VARIATION}, new IUnlistedProperty[]{CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST,
                CONNECTED_NORTH_EAST,CONNECTED_NORTH_WEST,CONNECTED_NORTH_UP,CONNECTED_NORTH_DOWN,CONNECTED_SOUTH_EAST,CONNECTED_SOUTH_WEST,
                CONNECTED_SOUTH_UP,CONNECTED_SOUTH_DOWN,CONNECTED_EAST_UP,CONNECTED_EAST_DOWN,CONNECTED_WEST_UP,CONNECTED_WEST_DOWN});
    }

    private void setupStates(){
        this.setDefaultState(getExtendedBlockState().withProperty(CONNECTED_DOWN, false).
                withProperty(CONNECTED_UP, false).withProperty(CONNECTED_NORTH, false).withProperty(CONNECTED_SOUTH, false).
                withProperty(CONNECTED_EAST, false).withProperty(CONNECTED_WEST, false).withProperty(CONNECTED_NORTH_EAST, false).
                withProperty(CONNECTED_NORTH_WEST, false).withProperty(CONNECTED_NORTH_UP, false).withProperty(CONNECTED_NORTH_DOWN, false).
                withProperty(CONNECTED_SOUTH_EAST, false).withProperty(CONNECTED_SOUTH_WEST, false).withProperty(CONNECTED_SOUTH_UP, false).
                withProperty(CONNECTED_SOUTH_DOWN, false).withProperty(CONNECTED_EAST_UP, false).withProperty(CONNECTED_EAST_DOWN, false).
                withProperty(CONNECTED_WEST_UP, false).withProperty(CONNECTED_WEST_DOWN, false).withProperty(VARIATION, 0));
    }

    public ExtendedBlockState getBaseExtendedState(){
        return (ExtendedBlockState)this.getBlockState();
    }

    public IExtendedBlockState getExtendedBlockState(){
        return (IExtendedBlockState)this.getBaseExtendedState().getBaseState();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return (Integer)state.getValue(VARIATION);
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return getBlockState().getBaseState().withProperty(VARIATION, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        return (Integer)state.getValue(VARIATION);
    }

    /**
     * Add a sub block
     * @param subBlock
     */
    public void addSubBlock(ISubBlock subBlock) {
        if (!hasSubBlock(subBlock)){
            for (int i=0;i<16;i++){
                if (subBlocks[i]==null){
                    subBlocks[i]=subBlock;
                    Chisel.logger.info("Adding Sub Block "+subBlock.getName()+" to "+this.getName());
                }
            }
        }
    }

    private boolean hasSubBlock(ISubBlock subBlock){
        if (subBlock==null){
            return true;
        }
        for (ISubBlock block : subBlocks) {
            if (block==null){
                continue;
            }
            if (block.equals(subBlock)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof BlockCarvable){
            BlockCarvable carv = (BlockCarvable)object;
            return carv.getUnlocalizedName().equals(this.getUnlocalizedName());
        }
        return false;
    }

    /**
     * Get the sub block from the "id"
     * @param i The "ID"
     * @return The sub block
     */
    public ISubBlock getSubBlock(int i){
        if (i>=subBlocks.length){
            return subBlocks[0];
        }
        if (subBlocks[i]==null){
            return subBlocks[0];
        }
        return subBlocks[i];
    }


    /**
     * Name used for texture path
     * @return The Name
     */
    public String getName(){
        return this.name;
    }



    public abstract void initSubBlocks();

    public abstract void preInitSubBlocks();


    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess w, BlockPos pos) {
        boolean up = false;
        boolean down = false;
        boolean north = false;
        boolean south = false;
        boolean east = false;
        boolean west = false;
        boolean north_east = false;
        boolean north_west = false;
        boolean north_up = false;
        boolean north_down = false;
        boolean south_east = false;
        boolean south_west = false;
        boolean south_up = false;
        boolean south_down =false;
        boolean east_up = false;
        boolean east_down = false;
        boolean west_up = false;
        boolean west_down = false;

        if (areBlocksEqual(state, w.getBlockState(pos.up()))){
            up=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.down()))){
            down=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.north()))){
            north=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.south()))){
            south=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.east()))){
            east=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.west()))){
            west=true;
        }

        if (areBlocksEqual(state, w.getBlockState(pos.north().east()))){
            north_east=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.north().west()))){
            north_west=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.north().up()))){
            north_up=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.north().down()))){
            north_down=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.south().east()))){
            south_east=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.south().west()))){
            south_west=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.south().up()))){
            south_up=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.south().down()))){
            south_down=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.east().up()))){
            east_up=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.east().down()))){
            east_down=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.west().up()))){
            west_up=true;
        }
        if (areBlocksEqual(state, w.getBlockState(pos.west().down()))){
            west_down=true;
        }


        return ((IExtendedBlockState)state).withProperty(CONNECTED_UP, up).withProperty(CONNECTED_DOWN, down).withProperty(CONNECTED_NORTH, north).
                withProperty(CONNECTED_SOUTH, south).withProperty(CONNECTED_EAST, east).withProperty(CONNECTED_WEST, west)
                .withProperty(CONNECTED_NORTH_EAST, north_east).withProperty(CONNECTED_NORTH_WEST, north_west).
                        withProperty(CONNECTED_NORTH_UP, north_up).withProperty(CONNECTED_NORTH_DOWN, north_down).
                        withProperty(CONNECTED_SOUTH_EAST, south_east).withProperty(CONNECTED_SOUTH_WEST, south_west).
                        withProperty(CONNECTED_SOUTH_UP, south_up).withProperty(CONNECTED_SOUTH_DOWN, south_down).
                        withProperty(CONNECTED_EAST_UP, east_up).withProperty(CONNECTED_EAST_DOWN, east_down).
                        withProperty(CONNECTED_WEST_UP, west_up).withProperty(CONNECTED_WEST_DOWN, west_down);
    }

    /**
     * Whether it is connected on the specified side
     * @param world The World
     * @param pos The Block pos
     * @param facing The Side
     * @return Whether it is connected
     */
    public static boolean isConnected(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return blockStatesEqual(getBlockOrFacade(world,pos, facing), getBlockOrFacade(world, pos(pos.getX() + facing.getFrontOffsetX(),
                pos.getY() + facing.getFrontOffsetY(), pos.getZ() + facing.getFrontOffsetZ()), facing));
    }

    /**
     * Whether it is connected on the specified side
     * @param world The World
     * @param x The Block x position
     * @param y The Block y position
     * @param z The Block z position
     * @param facing The Side
     * @return Whether it is connected
     */
    public static boolean isConnected(IBlockAccess world, int x, int y, int z, EnumFacing facing){
        return isConnected(world, pos(x, y, z), facing);
    }


    public static IBlockState getBlockOrFacade(IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof IFacade) {
            return((IFacade) state.getBlock()).getFacade(world, pos, side);
        }
        return state;
    }

    public static BlockPos pos(int x, int y, int z){
        return new BlockPos(x, y, z);
    }

    /**
     * Returns whether the two block states are equal to each other
     * @param state1 The First Block State
     * @param state2 The Second Block State
     * @return Whether they are equal
     */
    public static boolean blockStatesEqual(IBlockState state1, IBlockState state2){
        for (IProperty p : (ImmutableSet<IProperty>)state1.getProperties().keySet()){
            if (!state2.getProperties().containsKey(p)){
                return false;
            }
            if (state1.getValue(p)!=state2.getValue(p)){
                return false;
            }
        }
        if (state1.getBlock()==state2.getBlock()){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns whether the two blocks are equal ctm blocks
     * @param state1 First state
     * @param state2 Second state
     * @return Whether they are the same block
     */
    public static boolean areBlocksEqual(IBlockState state1, IBlockState state2){
        return (state1.getBlock()==state2.getBlock()&&state1.getValue(VARIATION)==state2.getValue(VARIATION));
    }
}
