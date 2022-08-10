package team.chisel.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.init.ChiselSounds;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SoundUtil {

    @SuppressWarnings("null")
    public static SoundEvent getSound(Player player, ItemStack chisel, @Nullable Block target) {
        if (target != null && !chisel.isEmpty()) {
            SoundEvent evt = ((IChiselItem) chisel.getItem()).getOverrideSound(player.getCommandSenderWorld(), player, chisel, target);
            if (evt != null) {
                return evt;
            }
        }
        return CarvingUtils.getChiselRegistry().getGroup(target != null ? target : Blocks.AIR).map(ICarvingGroup::getSound).orElse(ChiselSounds.fallback);
    }

    public static void playSound(Player player, ItemStack chisel, ItemStack source) {
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

    public static void playSound(Player player, ItemStack chisel, @Nullable Block target) {
        @Nonnull SoundEvent sound = getSound(player, chisel, target);
        playSound(player, player.blockPosition(), sound);
    }

    public static void playSound(Player player, BlockPos pos, SoundEvent sound) {
        playSound(player, pos, sound, SoundSource.BLOCKS);
    }

    public static void playSound(Player player, BlockPos pos, SoundEvent sound, SoundSource category) {
        Level world = player.getCommandSenderWorld();
        world.playSound(player, pos, sound, category, 0.3f + 0.7f * world.random.nextFloat(), 0.6f + 0.4f * world.random.nextFloat());
    }

}
