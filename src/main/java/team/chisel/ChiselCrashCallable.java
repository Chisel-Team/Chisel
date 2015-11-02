package team.chisel;

import cpw.mods.fml.common.ICrashCallable;

public class ChiselCrashCallable implements ICrashCallable {

	@Override
	public String call() throws Exception {
		return "Errors like \"[FML]: Unable to lookup ...\" are NOT the cause of this crash. "
				+ "You can safely ignore these errors. And update forge while you're at it.";
	}

	@Override
	public String getLabel() {
		return Chisel.MOD_NAME;
	}
}
