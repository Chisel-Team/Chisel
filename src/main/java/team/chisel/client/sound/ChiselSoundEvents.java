package team.chisel.client.sound;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import team.chisel.Chisel;

@ParametersAreNonnullByDefault
public class ChiselSoundEvents {

    public static final SoundEvent METAL_DIG = new SoundEvent(new ResourceLocation(Chisel.MOD_ID, "dig.metal"));
    public static final SoundEvent METAL_STEP = new SoundEvent(new ResourceLocation(Chisel.MOD_ID, "dig.metal"));
}
