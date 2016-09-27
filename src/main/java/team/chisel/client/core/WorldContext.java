package team.chisel.client.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Literally a class just to store an IBlockAccess and a BlockPos
 */
@ToString
@AllArgsConstructor
public class WorldContext {

    @Getter
    private IBlockAccess world;

    @Getter
    private BlockPos pos;

}
