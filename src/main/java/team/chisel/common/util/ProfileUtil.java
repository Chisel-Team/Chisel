package team.chisel.common.util;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;

public class ProfileUtil {
    
    /** Will never be "on" so calls to it will short-circuit */
    private static final Profiler dummyProfiler = new Profiler();
    
    private static ThreadLocal<Profiler> profiler = ThreadLocal.withInitial(() -> {
        if (Thread.currentThread().getId() == 1) {
            return Minecraft.getMinecraft().mcProfiler;
        } else {
            return dummyProfiler;
        }
    });
    
    public static void start(@Nonnull String section) {
        profiler.get().startSection(section);
    }
    
    public static void end() {
        profiler.get().endSection();
    }
    
    public static void endAndStart(@Nonnull String section) {
        profiler.get().endStartSection(section);
    }
}
