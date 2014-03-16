package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.Chisel;


public class BlockEldritch extends BlockMarble {

	public BlockEldritch() {
		super();
	}
	
	@Override
	public int getRenderType() {
		return Chisel.RenderEldritchId;
	}

}
