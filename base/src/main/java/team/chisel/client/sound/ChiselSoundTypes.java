package team.chisel.client.sound;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.SoundType;
import team.chisel.common.init.ChiselSounds;

@ParametersAreNonnullByDefault
public class ChiselSoundTypes {
    
    public static final SoundType METAL = new SoundType(1.0F, 1.0F, ChiselSounds.metal_break, ChiselSounds.metal_step, ChiselSounds.metal_place, ChiselSounds.metal_hit, ChiselSounds.metal_fall);
    

}
