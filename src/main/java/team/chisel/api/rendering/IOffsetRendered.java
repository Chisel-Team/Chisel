package team.chisel.api.rendering;

import team.chisel.ctmlib.ISubmapManager;
import net.minecraft.world.IBlockAccess;

/**
 * Implement this on blocks or {@link ISubmapManager}s which respond to the offset tool, and will offset their textures based on that data.
 * 
 * Not needed for blocks which use the standard v* texture types.
 */
public interface IOffsetRendered {

	boolean canOffset(IBlockAccess world, int x, int y, int z, int side);

}
