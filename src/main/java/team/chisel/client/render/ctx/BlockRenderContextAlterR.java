package team.chisel.client.render.ctx;

import javax.annotation.Nonnull;

import static team.chisel.client.ClientUtil.rand;

import lombok.Getter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.chisel.client.render.texture.ChiselTextureAlterR;

public class BlockRenderContextAlterR extends BlockRenderContextPosition {
    
    @Getter
    private final int texture;

    public BlockRenderContextAlterR(@Nonnull BlockPos pos, ChiselTextureAlterR tex) {
        super(pos);
        
        int num = 0;

        rand.setSeed(MathHelper.getPositionRandom(pos));
        rand.nextBoolean();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        num += rand.nextInt(2)*2;
        boolean type = true;

        // If even, switch boolean
        // If we have a set of multiple coords that are [x, y, z]
        // [5, 8, 9], [0, 0, 1], [9, 8, 8]...

        if(x % 2 == 0)
        {
            type = !type;
        }
        // Odd Even Odd
        // True False True

        if(y % 2 == 0)
        {
            type = !type;
        }
        // Even Even Even
        // False True False

        if(z % 2 == 0)
        {
            type = !type;
        }
        // Odd Odd Even
        // False True True

        num += type ? 0 : 1;
        
        this.texture = num;
    }
    
    @Override
    public long getCompressedData() {
        return texture;
    }
}
