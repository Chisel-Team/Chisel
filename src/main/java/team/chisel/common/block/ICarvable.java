package team.chisel.common.block;

import team.chisel.common.block.subblocks.ISubBlock;


public interface ICarvable {

	ISubBlock getSubBlock(int meta);
	
	int getIndex();
}
