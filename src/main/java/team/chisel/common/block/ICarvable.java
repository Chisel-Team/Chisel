package team.chisel.common.block;

import team.chisel.api.block.VariationData;


public interface ICarvable {

	VariationData getVariationData(int meta);
	
	int getIndex();
}
