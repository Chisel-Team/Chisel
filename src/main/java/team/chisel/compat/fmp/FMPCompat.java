package team.chisel.compat.fmp;

import team.chisel.init.ChiselBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartConverter;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

import com.google.common.collect.Lists;

public class FMPCompat implements IPartFactory, IPartConverter {

	public void init() {
		MultiPartRegistry.registerConverter(this);
		MultiPartRegistry.registerParts(this, new String[] { "chisel_torch" });
	}

	@Override
	public Iterable<Block> blockTypes() {
		return Lists.newArrayList(ChiselBlocks.torches);
	}

	@Override
	public TMultiPart convert(World world, BlockCoord bc) {
		Block block = world.getBlock(bc.x, bc.y, bc.z);
		for (int i = 0; i < ChiselBlocks.torches.length; i++) {
			if (block == ChiselBlocks.torches[i]) {
				return new PartChiselTorch(i, world.getBlockMetadata(bc.x, bc.y, bc.z));
			}
		}
		return null;
	}

	@Override
	public TMultiPart createPart(String type, boolean client) {
		if (type.equals("chisel_torch")) {
			return new PartChiselTorch();
		}
		return null;
	}
}
