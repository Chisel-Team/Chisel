package team.chisel.common.init;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Locale;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.ObjectHolderRegistry;
import team.chisel.Chisel;

@ObjectHolder(Chisel.MOD_ID)
@ParametersAreNonnullByDefault
@SuppressWarnings("null")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChiselSounds {
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private static @interface SoundName {
        String value();
    }

    @SoundName("block.metal.break")
    public static final SoundEvent metal_break = null;
    
    @SoundName("block.metal.fall")
    public static final SoundEvent metal_fall = null;
    
    @SoundName("block.metal.hit")
    public static final SoundEvent metal_hit = null;
    
    @SoundName("block.metal.place")
    public static final SoundEvent metal_place = null;
    
    @SoundName("block.metal.step")
    public static final SoundEvent metal_step = null;

    
    @SoundName("chisel.fallback")
    public static final SoundEvent fallback = null;
    
    @SoundName("chisel.wood")
    public static final SoundEvent wood_chisel = null;
    
    @SoundName("chisel.dirt")
    public static final SoundEvent dirt_chisel = null;
    
    public static void init() {}

    static {
        for (Field f : ChiselSounds.class.getDeclaredFields()) {
            if (f.isAnnotationPresent(SoundName.class)) {
                ForgeRegistries.SOUND_EVENTS.register(new SoundEvent(new ResourceLocation(Chisel.MOD_ID, f.getAnnotation(SoundName.class).value())).setRegistryName(f.getName().toLowerCase(Locale.ROOT)));
            }
        }
        
        ObjectHolderRegistry.INSTANCE.applyObjectHolders();
    }
}
