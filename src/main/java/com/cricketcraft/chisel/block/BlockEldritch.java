package com.cricketcraft.chisel.block;

import com.cricketcraft.chisel.Chisel;

public class BlockEldritch extends BlockCarvable {

	public BlockEldritch() {
		super();
	}

	@Override
	public int getRenderType() {
		return Chisel.RenderEldritchId;
	}

}
