package info.jbcs.minecraft.chisel;

import net.minecraft.block.Block.SoundType;

public class StepSoundEx extends SoundType
{
    String stepName;
    String placeName;

    public StepSoundEx(String name, String stepName, String placeName, float volume)
    {
        super(name, volume, 1.0f);

        this.stepName = stepName;
        this.placeName = placeName;
    }

    @Override
    public String getBreakSound()
    {
        return this.soundName;
    }
/*
    @Override
	public String getStepSound() {
		return stepName;
	}

	@Override
	public String getPlaceSound() {
		return placeName;
	}
*/
}
