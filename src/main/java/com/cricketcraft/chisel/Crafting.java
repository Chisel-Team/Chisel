package com.cricketcraft.chisel;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.cricketcraft.chisel.config.Configurations;
import com.cricketcraft.chisel.init.ChiselBlocks;
import com.cricketcraft.chisel.init.ChiselItems;

import cpw.mods.fml.common.registry.GameRegistry;

public class Crafting {

	@Deprecated
	private static void init() {
		Block concreteRecipeBlock = Block.getBlockFromName(Configurations.config.get("tweaks", "concrete recipe block", "gravel",
				"Unlocalized name of the block that, when burned, will produce concrete (examples: lightgem, stone)").getString());
		if (concreteRecipeBlock == null)
			concreteRecipeBlock = Blocks.gravel;

		if (Configurations.featureEnabled("concrete"))
			FurnaceRecipes.smelting().func_151393_a(concreteRecipeBlock, new ItemStack(ChiselBlocks.concrete), 0.1F);
		if (Configurations.featureEnabled("sandstoneScribbles"))
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.sandstone_scribbles, 1), new Object[] { "X", 'X', new ItemStack(ChiselBlocks.sandstone, 1, 8), });
		for (int meta = 0; meta < 16; meta++) {
			if (Configurations.featureEnabled("marble"))
				GameRegistry.addRecipe(new ItemStack(ChiselBlocks.marble_slab, 6, 0), new Object[] { "***", '*', new ItemStack(ChiselBlocks.marble, 1, meta) });
			if (Configurations.featureEnabled("limestone"))
				GameRegistry.addRecipe(new ItemStack(ChiselBlocks.limestone_slab, 6, 0), new Object[] { "***", '*', new ItemStack(ChiselBlocks.limestone, 1, meta) });
			if (Configurations.featureEnabled("marblePillar"))
				GameRegistry.addRecipe(new ItemStack(ChiselBlocks.marble_pillar_slab, 6, 0), new Object[] { "***", '*', new ItemStack(ChiselBlocks.marble_pillar, 1, meta) });

			if (Configurations.featureEnabled("marblePillar"))
				GameRegistry.addRecipe(new ItemStack(ChiselBlocks.marble_pillar, 6), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.marble, 1, meta), });
			if (Configurations.featureEnabled("marble"))
				GameRegistry.addRecipe(new ItemStack(ChiselBlocks.marble, 4), new Object[] { "XX", "XX", 'X', new ItemStack(ChiselBlocks.marble_pillar, 1, meta), });

			if (Configurations.featureEnabled("icePillar"))
				GameRegistry.addRecipe(new ItemStack(ChiselBlocks.ice_pillar, 6, 1), new Object[] { "XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.ice, 1, meta), });
			if (Configurations.featureEnabled("ice"))
				GameRegistry.addRecipe(new ItemStack(ChiselBlocks.ice, 4, 1), new Object[] { "XX", "XX", 'X', new ItemStack(ChiselBlocks.ice_pillar, 1, meta), });
			if (Configurations.featureEnabled("sandstone"))
				GameRegistry.addRecipe(new ItemStack(Blocks.sandstone, 1, 1), new Object[] { "X", 'X', new ItemStack(ChiselBlocks.sandstone_scribbles, 1, meta), });

			if (Configurations.featureEnabled("carpet"))
				GameRegistry.addRecipe(new ItemStack(ChiselBlocks.carpet_block, 8, meta), new Object[] { "YYY", "YXY", "YYY", 'X', new ItemStack(Items.string, 1), 'Y', new ItemStack(Blocks.wool, 1, meta), });
			if (Configurations.featureEnabled("carpetFloor"))
				GameRegistry.addRecipe(new ItemStack(ChiselBlocks.carpet, 3, meta), new Object[] { "XX", 'X', new ItemStack(ChiselBlocks.carpet_block, 1, meta), });
		}

		// The following recipe is due to bugs with Chisel 1.5.1 to 1.5.6a
		if (Configurations.featureEnabled("sandstone"))
			GameRegistry.addRecipe(new ItemStack(Blocks.sandstone, 1, 0), new Object[] { "X", 'X', new ItemStack(ChiselBlocks.sandstone, 1, 0), });

		// The following recipe is due to bug with Chisel 1.5.6b
		if (Configurations.featureEnabled("sandstone"))
			GameRegistry.addRecipe(new ItemStack(Blocks.sandstone, 1, 1), new Object[] { "X", 'X', new ItemStack(ChiselBlocks.sandstone, 1, 1), });

		if (Configurations.featureEnabled("holystone"))
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.holystone, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.feather, 1) });
		if (Configurations.featureEnabled("grimstone"))
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.grimstone, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.coal, 1) });
		if (Configurations.featureEnabled("lavastone"))
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.lavastone, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.lava_bucket, 1) });
		if (Configurations.featureEnabled("waterstone"))
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.waterstone, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.water_bucket, 1) });
		if (Configurations.featureEnabled("fantasy")) {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.fantasyblock, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.gold_nugget, 1) });
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.fantasyblock2, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(ChiselBlocks.fantasyblock, 1), 'X', "dyeWhite" }));
		}
		if (Configurations.featureEnabled("futuristicArmorPlating"))
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.tyrian, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1) });
		if (Configurations.featureEnabled("templeBlock"))
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.templeblock, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.dye, 1, 4) });
		if (Configurations.featureEnabled("factory"))
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.factoryblock, Configurations.factoryBlockAmount, 0), new Object[] { "*X*", "X X", "*X*", '*', new ItemStack(Blocks.stone, 1), 'X',
					new ItemStack(Items.iron_ingot, 1) });
		if (Configurations.featureEnabled("voidstone")) {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstone, 8, 0), new Object[] { "oxo", "xyx", "oxo", 'x', new ItemStack(Blocks.stone, 1), 'y', new ItemStack(Items.ender_pearl, 1), 'o',
					new ItemStack(Blocks.obsidian, 1) });
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstone2, 8, 0), new Object[] { "oxo", "xyx", "oxo", 'x', new ItemStack(Blocks.stone, 1), 'y', new ItemStack(Items.ender_eye, 1), 'o',
					new ItemStack(Blocks.obsidian, 1) });
		}
		if (Configurations.featureEnabled("voidstonePillars")) {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstonePillar, 4, 0), new Object[] { "xx", "xx", 'x', new ItemStack(ChiselBlocks.voidstone, 1) });
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstonePillar2, 4, 0), new Object[] { "xx", "xx", 'x', new ItemStack(ChiselBlocks.voidstone2, 1) });
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.road_line, 8, 0), new Object[] { "wrw", "wrw", "wrw", ('w'), "dyeWhite", ('r'), Items.redstone }));

		if (Configurations.chiselRecipe) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.chisel, 1), new Object[] { " YY", " YY", "X  ", 'X', Items.stick, 'Y', Items.iron_ingot }));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.chisel, 1, 1), new Object[] { " YY", " YY", "x  ", 'x', "stickWood", 'Y', "ingotIron" }));
		} else {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.chisel, 1), new Object[] { " Y", "X ", 'X', Items.stick, 'Y', Items.iron_ingot }));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.chisel, 1, 1), new Object[] { "  y", "x  ", 'x', "stickWood", 'y', "ingotIron" }));
		}

		if (Configurations.featureEnabled("ballOfMoss"))
			GameRegistry.addRecipe(new ItemStack(ChiselItems.itemBallOMoss, 1), new Object[] { "XYX", "YXY", "XYX", 'X', Blocks.vine, 'Y', Items.stick });
		if (Configurations.featureEnabled("cloud"))
			GameRegistry.addRecipe(new ItemStack(ChiselItems.itemCloudInABottle, 1), new Object[] { "X X", "XYX", " X ", 'X', Blocks.glass, 'Y', Items.quartz });
		if (Configurations.featureEnabled("smashingrock"))
			GameRegistry.addShapelessRecipe(new ItemStack(ChiselItems.smashingRock, 16), new Object[] { new ItemStack(Items.stone_pickaxe), new ItemStack(Items.glass_bottle, 1),
					new ItemStack(Items.stone_shovel) });

		if (Configurations.featureEnabled("paperWall"))
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.paperwall, 8), new Object[] { "ppp", "psp", "ppp", ('p'), Items.paper, ('s'), "stickWood" }));

		String[] sGNames = new String[] { "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black" };
		for (int i = 0; i < 16; i++) {
			OreDictionary.registerOre("stainedClay" + sGNames[i].replaceAll(" ", ""), new ItemStack(Blocks.stained_hardened_clay, 1, i));
			OreDictionary.registerOre("blockWool" + sGNames[i].replaceAll(" ", ""), new ItemStack(Blocks.wool, 1, i));
			if (Configurations.featureEnabled("woolenClay"))
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ChiselBlocks.woolen_clay, 2, i), new Object[] { "blockWool" + sGNames[i].replaceAll(" ", ""),
						"stainedClay" + sGNames[i].replaceAll(" ", "") }));
		}
		if (Configurations.featureEnabled("laboratory"))
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.laboratoryblock, 8, 0), new Object[] { "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.quartz, 1) });

		if (Configurations.autoChisel)
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.autoChisel, 1), new Object[] { "XXX", "XYX", "XXX", Character.valueOf('X'), "plankWood", Character.valueOf('Y'),
					ChiselItems.chisel }));

		if (Configurations.featureEnabled("hexPlating"))
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.hexPlating, 8, 0), new Object[] { "XXX", "XYX", "XXX", 'X', "stone", 'Y', "blockCoal" }));
		if (Configurations.featureEnabled("bone"))
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.bone, 4), new Object[] { "XX", "XX", 'X', Items.bone });

		if (Configurations.featureEnabled("pumpkin")) {
			for (int x = 0; x < 16; x++) {
				GameRegistry.addShapelessRecipe(new ItemStack(ChiselBlocks.jackolantern[x]),
						new Object[] { new ItemStack(ChiselBlocks.pumpkin[x], 1), new ItemStack(Item.getItemFromBlock(Blocks.torch), 1) });
			}
		}

		if (Configurations.featureEnabled("technical")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.technical, Configurations.factoryBlockAmount, 0), new Object[] { "xyx", "yxy", "xyx", 'x', "stone", 'y',
					Items.iron_ingot }));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.technical2, Configurations.factoryBlockAmount, 0), new Object[] { "xyx", "yzy", "xyx", 'x', "stone", 'y', "ingotIron",
					'z', Blocks.glass }));
		}

		if (Configurations.featureEnabled("chest")) {
			for (int x = 0; x < 16; x++) {
				GameRegistry.addShapelessRecipe(new ItemStack(ChiselBlocks.present[x]), new Object[] { new ItemStack(Blocks.chest, 1), new ItemStack(Items.dye, 1, x) });
			}
		}

		if (Configurations.featureEnabled("warningSign")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.sign, 4, 0), new Object[] { "xxx", "xyx", "xxx", 'x', "stone", 'y', Items.sign }));
		}
		if (Configurations.featureEnabled("scorching")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.scorching, 8, 0), new Object[] { "xxx", "xyx", "xxx", 'x', "stone", 'y', Items.blaze_rod }));
		}
		if (Configurations.featureEnabled("coloredSand")) {
			for (int x = 0; x < 16; x++) {
				String[] dyes = { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.colored_sand, 1, x), new Object[] { "sss", "sds", "sss", 's', Blocks.sand, 'd', "dye" + dyes[x] }));
				GameRegistry.addSmelting(new ItemStack(ChiselBlocks.colored_sand, 1, x), new ItemStack(Blocks.stained_glass, 1, x), 0.3F);
			}
		}
	}
}
