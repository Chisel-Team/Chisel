package com.chiselmod.chisel.common.block;

import com.chiselmod.chisel.Chisel;
import com.chiselmod.chisel.client.render.BlockResources;
import com.chiselmod.chisel.client.render.ctm.CTMModelRegistry;
import com.chiselmod.chisel.common.block.subblocks.ISubBlock;
import com.chiselmod.chisel.common.block.subblocks.SubBlock;
import com.chiselmod.chisel.common.init.ChiselTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

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
    public static final PropertyBool CONNECTED_DOWN = PropertyBool.create("Connected Down");
    public static final PropertyBool CONNECTED_UP = PropertyBool.create("Connected Up");
    public static final PropertyBool CONNECTED_NORTH = PropertyBool.create("Connected North");
    public static final PropertyBool CONNECTED_SOUTH = PropertyBool.create("Connected South");
    public static final PropertyBool CONNECTED_EAST = PropertyBool.create("Connected East");
    public static final PropertyBool CONNECTED_WEST = PropertyBool.create("Connected West");

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

    private void setupStates(){
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(VARIATION, 0).withProperty(CONNECTED_DOWN, false).
                withProperty(CONNECTED_UP, false).withProperty(CONNECTED_NORTH, false).withProperty(CONNECTED_SOUTH, false).
                withProperty(CONNECTED_EAST, false).withProperty(CONNECTED_WEST, false));
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

    @Override
    public BlockState createBlockState(){
        return new BlockState(this, VARIATION, CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST);
    }

    public abstract void initSubBlocks();

    public abstract void preInitSubBlocks();


    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess w, BlockPos pos) {
        boolean up = false;
        boolean down = false;
        boolean north = false;
        boolean south = false;
        boolean east = false;
        boolean west = false;

        if (w.getBlockState(pos.up()).getBlock()==this){
            up=true;
        }
        if (w.getBlockState(pos.down()).getBlock()==this){
            down=true;
        }
        if (w.getBlockState(pos.north()).getBlock()==this){
            north=true;
        }
        if (w.getBlockState(pos.south()).getBlock()==this){
            south=true;
        }
        if (w.getBlockState(pos.east()).getBlock()==this){
            east=true;
        }
        if (w.getBlockState(pos.west()).getBlock()==this){
            west=true;
        }
        return state.withProperty(CONNECTED_UP, up).withProperty(CONNECTED_DOWN, down).withProperty(CONNECTED_NORTH, north).
                withProperty(CONNECTED_SOUTH, south).withProperty(CONNECTED_EAST, east).withProperty(CONNECTED_WEST, west);
    }
}
