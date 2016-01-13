package team.chisel.client;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ClientUtil {

    public static final Random rand = new Random();

    public static void playSound(World world, int x, int y, int z, String sound) {
        Minecraft.getMinecraft().theWorld.playSound(x + 0.5, y + 0.5, z + 0.5, sound, 0.3f + 0.7f * rand.nextFloat(), 0.6f + 0.4f * rand.nextFloat(), true);
    }

    public static void addHitEffects(World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        state = block.getActualState(state, world, pos);

        if (block.getRenderType() != -1) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            double d0 = (double) i + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double) (f * 2.0F)) + (double) f + block.getBlockBoundsMinX();
            double d1 = (double) j + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double) (f * 2.0F)) + (double) f + block.getBlockBoundsMinY();
            double d2 = (double) k + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double) (f * 2.0F)) + (double) f + block.getBlockBoundsMinZ();

            if (side == EnumFacing.DOWN) {
                d1 = (double) j + block.getBlockBoundsMinY() - (double) f;
            }

            if (side == EnumFacing.UP) {
                d1 = (double) j + block.getBlockBoundsMaxY() + (double) f;
            }

            if (side == EnumFacing.NORTH) {
                d2 = (double) k + block.getBlockBoundsMinZ() - (double) f;
            }

            if (side == EnumFacing.SOUTH) {
                d2 = (double) k + block.getBlockBoundsMaxZ() + (double) f;
            }

            if (side == EnumFacing.WEST) {
                d0 = (double) i + block.getBlockBoundsMinX() - (double) f;
            }

            if (side == EnumFacing.EAST) {
                d0 = (double) i + block.getBlockBoundsMaxX() + (double) f;
            }

            ((EntityDiggingFX) Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), d0, d1, d2, 0.0D, 0.0D, 0.0D,
                    Block.getIdFromBlock(state.getBlock()))).func_174846_a(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F)
                    .setParticleIcon(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelFromBlockState(state, world, pos).getParticleTexture());
            ;
        }
    }

    public static void addDestroyEffects(World world, BlockPos pos, IBlockState state) {

        state = state.getBlock().getActualState(state, world, pos);
        int i = 4;

        for (int j = 0; j < i; ++j) {
            for (int k = 0; k < i; ++k) {
                for (int l = 0; l < i; ++l) {
                    double d0 = (double) pos.getX() + ((double) j + 0.5D) / (double) i;
                    double d1 = (double) pos.getY() + ((double) k + 0.5D) / (double) i;
                    double d2 = (double) pos.getZ() + ((double) l + 0.5D) / (double) i;
                    ((EntityDiggingFX) Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), d0, d1, d2, d0 - pos.getX() - 0.5D, d1 - pos.getY()
                            - 0.5D, d2 - pos.getZ() - 0.5D, Block.getIdFromBlock(state.getBlock()))).func_174846_a(pos).setParticleIcon(
                            Minecraft.getMinecraft().getBlockRendererDispatcher().getModelFromBlockState(state, world, pos).getParticleTexture());
                    ;
                }
            }
        }
    }
}
