package team.chisel.common.util;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Strings;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.IChiselMode;
import team.chisel.client.gui.PreviewType;
import team.chisel.common.carving.ChiselModeRegistry;
import team.chisel.common.item.ChiselMode;

@ParametersAreNonnullByDefault
public class NBTUtil {

    private static final String KEY_TAG = "chiseldata";
    private static final String KEY_TARGET = "target";
    private static final String KEY_PREVIEW_TYPE = "preview";
    private static final String KEY_SELECTION_SLOT = "selectslot";
    private static final String KEY_TARGET_SLOT = "targetslot";
    private static final String KEY_ROTATE = "rotate";
    private static final String KEY_MODE = "mode";

    @SuppressWarnings("null")
    public static CompoundNBT getTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new CompoundNBT());
        }
        // Warning suppressed: tag is guaranteed to be set from above code
        return stack.getTagCompound();
    }

    public static CompoundNBT getChiselTag(ItemStack stack) {
        CompoundNBT tag = getTag(stack);
        if (!tag.hasKey(KEY_TAG)) {
            tag.setTag(KEY_TAG, new CompoundNBT());
        }
        return tag.getCompoundTag(KEY_TAG);
    }
    
    public static void setChiselTag(ItemStack stack, CompoundNBT tag) {
        getTag(stack).setTag(KEY_TAG, tag);
    }

    public static ItemStack getChiselTarget(ItemStack stack) {
        return new ItemStack(getChiselTag(stack).getCompoundTag(KEY_TARGET));
    }

    public static void setChiselTarget(ItemStack chisel, ItemStack target) {
        getChiselTag(chisel).setTag(KEY_TARGET, target.writeToNBT(new CompoundNBT()));
    }
    
    @SuppressWarnings("null") // Can't use type annotations with JSR
    public static PreviewType getHitechType(ItemStack stack) {
        return PreviewType.values()[getChiselTag(stack).getInteger(KEY_PREVIEW_TYPE)];
    }

    public static void setHitechType(ItemStack stack, int type) {
        getChiselTag(stack).setInteger(KEY_PREVIEW_TYPE, type);
    }
    
    public static int getHitechSelection(ItemStack stack) {
        CompoundNBT tag = getChiselTag(stack);
        return tag.hasKey(KEY_SELECTION_SLOT) ? tag.getInteger(KEY_SELECTION_SLOT) : -1;
    }

    public static void setHitechSelection(ItemStack chisel, int slot) {
        getChiselTag(chisel).setInteger(KEY_SELECTION_SLOT, slot);
    }
    
    public static int getHitechTarget(ItemStack stack) {
        CompoundNBT tag = getChiselTag(stack);
        return tag.hasKey(KEY_TARGET_SLOT) ? tag.getInteger(KEY_TARGET_SLOT) : -1;
    }

    public static void setHitechTarget(ItemStack chisel, int slot) {
        getChiselTag(chisel).setInteger(KEY_TARGET_SLOT, slot);
    }
    
    public static boolean getHitechRotate(ItemStack stack) {
        return getChiselTag(stack).getBoolean(KEY_ROTATE);
    }

    public static void setHitechRotate(ItemStack chisel, boolean rotate) {
        getChiselTag(chisel).setBoolean(KEY_ROTATE, rotate);
    }

    public static IChiselMode getChiselMode(@Nonnull ItemStack chisel) {
        String mode = getChiselTag(chisel).getString(KEY_MODE);
        return Optional.ofNullable(CarvingUtils.getModeRegistry().getModeByName(mode))
                .orElse(ChiselMode.SINGLE);
    }

    public static void setChiselMode(@Nonnull ItemStack chisel, @Nonnull IChiselMode mode) {
        getChiselTag(chisel).setString(KEY_MODE, mode.name());
    }
}
