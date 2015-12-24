package team.chisel.common.util;

import net.minecraft.util.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyBlockPos implements IUnlistedProperty<BlockPos> {

    @Override
    public String getName() {
        return "pos";
    }

    @Override
    public boolean isValid(BlockPos var) {
        return true;
    }

    @Override
    public Class<BlockPos> getType() {
        return BlockPos.class;
    }

    @Override
    public String valueToString(BlockPos pos) {
        return pos.toString();
    }
}
