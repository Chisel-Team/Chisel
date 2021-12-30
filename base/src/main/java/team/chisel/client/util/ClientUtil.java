package team.chisel.client.util;

import java.lang.reflect.Field;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Timer;
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

    public static final RenderType OFFSET_OVERLAY = RenderType.create("chisel_offset_overlay",
    		DefaultVertexFormat.POSITION_COLOR, GL11.GL_TRIANGLES, 256,
    		RenderType.CompositeState.builder()
    			.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
    			.setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
    			.createCompositeState(true));
}
