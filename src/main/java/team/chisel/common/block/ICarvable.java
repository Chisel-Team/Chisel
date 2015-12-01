package team.chisel.common.block;

import team.chisel.api.block.VariationData;
import team.chisel.common.block.subblocks.ISubBlock;


public interface ICarvable {

	VariationData getVariationData(int meta);
	
	int getIndex();
}
