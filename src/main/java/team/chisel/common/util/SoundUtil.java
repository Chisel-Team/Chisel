package team.chisel.common.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;

@ParametersAreNonnullByDefault
public class SoundUtil {
    
    @SuppressWarnings("null")
    public static SoundEvent getSound(EntityPlayer player, ItemStack chisel, @Nullable IBlockState target) {
        if (target != null && !chisel.isEmpty()) {
            SoundEvent evt = ((IChiselItem)chisel.getItem()).getOverrideSound(player.getEntityWorld(), player, chisel, target);
            if (evt != null) {
                return evt;
            }
        }
        return CarvingUtils.getChiselRegistry().getVariationSound(target != null ? target : Blocks.AIR.getDefaultState());
    }
    
    public static void playSound(EntityPlayer player, ItemStack chisel, ItemStack source) {
        ICarvingVariation v = CarvingUtils.getChiselRegistry().getVariation(source);
        IBlockState state = v == null ? null : v.getBlockState();
        if (state == null) {
            if (source.getItem() instanceof ItemBlock) {
                state = ((ItemBlock) source.getItem()).getBlock().getStateFromMeta(source.getItem().getMetadata(source.getItemDamage()));
            } else {
                state = Blocks.STONE.getDefaultState(); // fallback
            }
        }
        playSound(player, chisel, state);
    }

    public static void playSound(EntityPlayer player, ItemStack chisel, @Nullable IBlockState target) {
        @Nonnull SoundEvent sound = getSound(player, chisel, target);
        playSound(player, player.getPosition(), sound);
    }

    public static void playSound(EntityPlayer player, BlockPos pos, SoundEvent sound) {
        playSound(player, pos, sound, SoundCategory.BLOCKS);
    }
    
    public static void playSound(EntityPlayer player, BlockPos pos, SoundEvent sound, SoundCategory category) {
        World world = player.getEntityWorld();
        world.playSound(player, pos, sound, category, 0.3f + 0.7f * world.rand.nextFloat(), 0.6f + 0.4f * world.rand.nextFloat());
    }

}
