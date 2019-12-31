package team.chisel.common.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.init.ChiselSounds;

@ParametersAreNonnullByDefault
public class SoundUtil {
    
    @SuppressWarnings("null")
    public static SoundEvent getSound(PlayerEntity player, ItemStack chisel, @Nullable Block target) {
        if (target != null && !chisel.isEmpty()) {
            SoundEvent evt = ((IChiselItem)chisel.getItem()).getOverrideSound(player.getEntityWorld(), player, chisel, target);
            if (evt != null) {
                return evt;
            }
        }
        return CarvingUtils.getChiselRegistry().getGroup(target != null ? target.getBlock() : Blocks.AIR).map(ICarvingGroup::getSound).orElse(ChiselSounds.fallback);
    }
    
    public static void playSound(PlayerEntity player, ItemStack chisel, ItemStack source) {
        ICarvingVariation v = CarvingUtils.getChiselRegistry().getVariation(source.getItem()).orElse(null);
        Block block = v == null ? null : v.getBlock();
        if (block == null) {
            if (source.getItem() instanceof BlockItem) {
                block = ((BlockItem) source.getItem()).getBlock();
            } else {
                block = Blocks.STONE; // fallback
            }
        }
        playSound(player, chisel, block);
    }

    public static void playSound(PlayerEntity player, ItemStack chisel, @Nullable Block target) {
        @Nonnull SoundEvent sound = getSound(player, chisel, target);
        playSound(player, player.getPosition(), sound);
    }

    public static void playSound(PlayerEntity player, BlockPos pos, SoundEvent sound) {
        playSound(player, pos, sound, SoundCategory.BLOCKS);
    }
    
    public static void playSound(PlayerEntity player, BlockPos pos, SoundEvent sound, SoundCategory category) {
        World world = player.getEntityWorld();
        world.playSound(player, pos, sound, category, 0.3f + 0.7f * world.rand.nextFloat(), 0.6f + 0.4f * world.rand.nextFloat());
    }

}
