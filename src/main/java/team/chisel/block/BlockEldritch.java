package team.chisel.block;

import team.chisel.Chisel;

public class BlockEldritch extends BlockCarvable {

	public BlockEldritch() {
		super();
	}

	@Override
	public int getRenderType() {
		return Chisel.renderEldritchId;
	}

}
