package team.chisel.client.sound;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.SoundType;

@ParametersAreNonnullByDefault
public class ChiselSoundTypes {
    
    public static final SoundType METAL = new SoundType(1.0F, 1.0F, ChiselSoundEvents.METAL_DIG, ChiselSoundEvents.METAL_STEP, ChiselSoundEvents.METAL_DIG, ChiselSoundEvents.METAL_STEP, ChiselSoundEvents.METAL_DIG);

}
