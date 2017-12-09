package team.chisel.client.gui;

import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableSet;

import lombok.Getter;
import net.minecraft.util.math.BlockPos;

@Getter 
public enum PreviewType {
    PANEL(16, generateBetween(0, 1, 0, 2, 3, 0)),
    
    @SuppressWarnings("null")
    HOLLOW(16, ArrayUtils.removeElement(generateBetween(0, 1, 0, 2, 3, 0), new BlockPos(1, 2, 0))),
    
    PLUS(20,
        new BlockPos(1, 1, 0),
        new BlockPos(1, 2, 0),
        new BlockPos(2, 2, 0),
        new BlockPos(0, 2, 0),
        new BlockPos(1, 3, 0)
    ),

    SINGLE(50, new BlockPos(1, 2, 0)),
    
    ;
    
    private static @Nonnull BlockPos[] generateBetween(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        BlockPos[] ret = new BlockPos[(maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)];
        int i = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    ret[i++] = new BlockPos(x, y, z);
                }
            }
        }
        return ret;
    }
    
    private float scale;
    private Set<BlockPos> positions;
    
    private PreviewType(float scale, @Nonnull BlockPos... positions) {
        this.scale = scale;
        this.positions = ImmutableSet.copyOf(positions);
    }
    
    @Override
    public String toString() {
        return "container.chisel.hitech.preview." + name().toLowerCase(Locale.US);
    }
}