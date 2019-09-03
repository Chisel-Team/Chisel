package team.chisel.client.util;

import java.lang.reflect.Field;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Throwables;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import team.chisel.Chisel;

@ParametersAreNonnullByDefault
public class ClientUtil {

    public static void addHitEffects(World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        state = state.getActualState(world, pos);

        if (state.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            AxisAlignedBB bb = state.getBoundingBox(world, pos);

            double d0 = (double) i + world.rand.nextDouble() * (bb.maxX - bb.minX - (double) (f * 2.0F)) + (double) f + bb.minX;
            double d1 = (double) j + world.rand.nextDouble() * (bb.maxY - bb.minY - (double) (f * 2.0F)) + (double) f + bb.minY;
            double d2 = (double) k + world.rand.nextDouble() * (bb.maxZ - bb.minZ - (double) (f * 2.0F)) + (double) f + bb.minZ;

            if (side == EnumFacing.DOWN) {
                d1 = (double) j + bb.minY - f;
            }

            if (side == EnumFacing.UP) {
                d1 = (double) j + bb.maxY + f;
            }

            if (side == EnumFacing.NORTH) {
                d2 = (double) k + bb.minZ - f;
            }

            if (side == EnumFacing.SOUTH) {
                d2 = (double) k + bb.maxZ + f;
            }

            if (side == EnumFacing.WEST) {
                d0 = (double) i + bb.minX - f;
            }

            if (side == EnumFacing.EAST) {
                d0 = (double) i + bb.maxX + f;
            }

            Particle fx = Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), d0, d1, d2, 0.0D, 0.0D, 0.0D,
                    Block.getIdFromBlock(state.getBlock()));

            if (fx != null) {
                ((ParticleDigging)fx).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F)
                        .setParticleTexture(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state).getParticleTexture());
            }
        }
    }

    public static void addDestroyEffects(World world, BlockPos pos, IBlockState state) {

        state = state.getActualState(world, pos);
        int i = 4;
        
        TextureAtlasSprite texture = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state).getParticleTexture();

        for (int j = 0; j < i; ++j) {
            for (int k = 0; k < i; ++k) {
                for (int l = 0; l < i; ++l) {
                    double d0 = (double) pos.getX() + ((double) j + 0.5D) / (double) i;
                    double d1 = (double) pos.getY() + ((double) k + 0.5D) / (double) i;
                    double d2 = (double) pos.getZ() + ((double) l + 0.5D) / (double) i;
                    Particle fx = Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), d0, d1, d2, d0 - pos.getX() - 0.5D,
                            d1 - pos.getY() - 0.5D, d2 - pos.getZ() - 0.5D, Block.getIdFromBlock(state.getBlock()));

                    if (fx != null) {
                        ((ParticleDigging) fx).setBlockPos(pos).setParticleTexture(texture);
                    }
                }
            }
        }
    }

    @Nullable
    private static final Field timerField = initTimer();

    @Nullable
    private static Field initTimer() {
        Field f = null;
        try {
            f = ReflectionHelper.findField(Minecraft.class, "field_71428_T", "timer", "Q");
            f.setAccessible(true);
        } catch (Exception e) {
            Chisel.logger.error("Failed to initialize timer reflection.");
            e.printStackTrace();
        }
        return f;
    }

    @SuppressWarnings("null")
    @Nullable
    public static Timer getTimer() {
        if (timerField == null) {
            return null;
        }
        try {
            return (Timer) timerField.get(Minecraft.getMinecraft());
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
