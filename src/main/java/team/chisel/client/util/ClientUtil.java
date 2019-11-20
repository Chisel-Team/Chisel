package team.chisel.client.util;

import java.lang.reflect.Field;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import team.chisel.Chisel;

@ParametersAreNonnullByDefault
public class ClientUtil {

    @Nullable
    private static final Field timerField = initTimer();

    @Nullable
    private static Field initTimer() {
        Field f = null;
        try {
            f = ObfuscationReflectionHelper.findField(Minecraft.class, "field_71428_T");
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
            return (Timer) timerField.get(Minecraft.getInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
