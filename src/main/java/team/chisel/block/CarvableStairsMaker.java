package team.chisel.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import team.chisel.carving.Carving;
import team.chisel.item.ItemCarvable;

import com.cricketcraft.chisel.api.ChiselTabs;
import com.cricketcraft.chisel.api.carving.CarvableHelper;
import com.cricketcraft.chisel.api.carving.CarvingUtils;

import cpw.mods.fml.common.registry.GameRegistry;

public class CarvableStairsMaker {

	public final CarvableHelper carverHelper;
	private Block blockBase;

	public CarvableStairsMaker(Block base) {
		blockBase = base;
		carverHelper = new CarvableHelper(base);
	}

	public void create(String name, Block[] blocks, int size) {
		create(null, name, blocks, size);
	}

	public void create(IStairsCreator creator, String name, Block[] blocks, int size) {
		for (int i = 0; i < size / 2; i++) {
			String n = name + "." + i;
			blocks[i] = creator == null ? new BlockCarvableStairs(blockBase, i * 2, carverHelper) : creator.create(blockBase, i * 2, carverHelper);

			blocks[i].setBlockName("chisel." + n).setCreativeTab(ChiselTabs.tabStairChiselBlocks);
			GameRegistry.registerBlock(blocks[i], ItemCarvable.class, n);

			for (int meta = 0; meta < 2 && i * 2 + meta < carverHelper.infoList.size(); meta++) {
				Carving.chisel.addVariation(name, CarvingUtils.getDefaultVariationFor(blocks[i], meta * 8, i));
				GameRegistry.addRecipe(new ItemStack(blocks[i], 4, meta * 8), "*  ", "** ", "***", '*', new ItemStack(blockBase, 1, i * 2 + meta));
			}
		}
	}
}
