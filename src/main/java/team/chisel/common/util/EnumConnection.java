package team.chisel.common.util;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import team.chisel.client.render.ctm.CTM;
import team.chisel.common.block.BlockCarvable;

/**
 * Each of these represents a type connection that a ctm block has to another
 *
 * @author minecreatr
 */
public enum EnumConnection {

    DOWN(pos().down()),
    UP(pos().up()),
    NORTH(pos().north()),
    SOUTH(pos().south()),
    EAST(pos().east()),
    WEST(pos().west()),

    NORTH_EAST(pos().north().east()),
    NORTH_WEST(pos().north().west()),
    NORTH_UP(pos().north().up()),
    NORTH_DOWN(pos().north().down()),
    SOUTH_EAST(pos().south().east()),
    SOUTH_WEST(pos().south().west()),
    SOUTH_UP(pos().south().up()),
    SOUTH_DOWN(pos().south().down()),
    EAST_UP(pos().east().up()),
    EAST_DOWN(pos().east().down()),
    WEST_UP(pos().west().up()),
    WEST_DOWN(pos().west().down());

    public static final EnumConnection[] VALUES = values();
    
    private static final Map<BlockPos, EnumConnection> vecMap = Maps.newHashMap();
    
    private BlockPos positionVector;


    /**
     * Constructor for EnumConnection, represents a connection
     * @param positionVector The Vector of the relative location of the block that is connected
     */
    EnumConnection(BlockPos positionVector){
        this.positionVector = positionVector;
    }

    private static BlockPos pos(){
        return new BlockPos(0, 0, 0);
    }

    public static EnumConnection fromFacings(EnumFacing... facings) {
    	BlockPos vec = pos();
    	for (EnumFacing f : facings) {
    		vec = vec.offset(f);
    	}
    	return fromVec(vec);
    }
    
    public static EnumConnection fromVec(BlockPos vec) {
    	EnumConnection ret = vecMap.get(vec);
    	if (ret == null) {
    		for (EnumConnection c : VALUES) {
    			if (c.positionVector.equals(vec)) {
    				vecMap.put(vec, c);
    				ret = c;
    			}
    		}
    	}
    	return ret;
    }
    
    public IBlockState getBlockAt(BlockPos origin, IBlockAccess w){
        return w.getBlockState(origin.add(positionVector));
    }

    public boolean isValid(BlockPos origin, IBlockAccess w){
        if (w.getBlockState(origin).getBlock() instanceof BlockCarvable) {
            return CTM.areBlocksEqual(w.getBlockState(origin), getBlockAt(origin, w), ((BlockCarvable) w.getBlockState(origin).getBlock()).metaProperty);
        }
        else {
            return false;
        }
    }


}
