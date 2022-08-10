package team.chisel.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import team.chisel.Chisel;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class ClientUtil {

    public static final RenderType OFFSET_OVERLAY = RenderType.create("chisel_offset_overlay",
            DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES, 256,
            false, true,
            RenderType.CompositeState.builder()
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
                    .createCompositeState(true));
    @Nullable
    private static final Field timerField = initTimer();

    @Nullable
    private static Field initTimer() {
        Field f = null;
        try {
            f = ObfuscationReflectionHelper.findField(Minecraft.class, "timer");
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
