package team.chisel.compat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.carving.Carving;
import team.chisel.utils.RecipeUtil;

import com.google.common.collect.Maps;
import com.pahimar.ee3.api.exchange.RecipeRegistryProxy;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class Compatibility {

	public static String[] rockColorNames = { "gray", "lightgray", "brown", "tan", "reddish", "bluish", "greenish" };

	public static Map<Integer, String> tconMap = Maps.newHashMap();

	static {
		tconMap.put(0, "obsidian");
		tconMap.put(1, "sandstone");
		tconMap.put(2, "netherrack");
		tconMap.put(3, "stonebricksmooth");
		tconMap.put(12, "end_stone");
	}

	public static void init(FMLPostInitializationEvent event) {

		/* Proj Red */
		addSupport("ProjRed|Exploration", "projectred.exploration.stone", "marble", 0, 99);
		addSupport("ProjRed|Exploration", "projectred.exploration.stone", "marble", 1, 99);

		/* Bluepower */
		addSupport("bluepower", "marble", "marble", 0, 99);

		/* Artifice */
		addSupport("Artifice", "artifice.marble", "marble", 0, 99);
		addSupport("Artifice", "artifice.marble.slab", "marble_slab", 0, 99);
		for (String s : rockColorNames) {
			addSupport("Artifice", "artifice.limestone." + s, "limestone", 0, 99);
		}

		/* Mariculture */
		Block block = GameRegistry.findBlock("mariculture", "limestone");
		if (block != null) {
			for (int i = 0; i < 16; i++) {
				addSupport("limestone", block, i, 99);
			}
		}

		/* Geologica */
		addSupport("PFAAGeologica", "strongStone", "marble", 5, 99);
		addSupport("PFAAGeologica", "strongStoneSlab", "marbleSlab", 5, 99);
		addSupport("PFAAGeologica", "strongStoneBrickStairs.marble", "marbleStairs", 0, 99);
		addSupport("PFAAGeologica", "mediumStone", "limestone", 0, 99);
		addSupport("PFAAGeologica", "mediumStoneSlab", "limestoneSlab", 0, 99);
		addSupport("PFAAGeologica", "mediumStoneBrickStairs.limestone", "limestoneStairs", 0, 99);
		addSupport("PFAAGeologica", "strongStoneBrick", "stoneBrick", 3, 99);
		addSupport("PFAAGeologica", "strongCobble", "cobblestone", 3, 99);

		/* Thaumcraft TODO There is probably a cleaner way of doing this */
		if (Loader.isModLoaded("Thaumcraft")) {
			loadThaumcraftAspects();
		}

		for (Integer i : tconMap.keySet()) {
			addSupport("TConstruct", "decoration.multibrick", tconMap.get(i), i, 99);
			addSupport("TConstruct", "decoration.multibrickfancy", tconMap.get(i), i, 99);
		}

		addSupport("TConstruct", "decoration.multibrickfancy", "stonebricksmooth", 14, 99);
		addSupport("TConstruct", "decoration.multibrickfancy", "stonebricksmooth", 15, 99);

		addSupport("Botania", "endStoneBrick", "end_stone", 0, 0);
		addSupport("Botania", "endStoneBrick", "end_stone", 1, 0);

		//Andesite

		addSupport("Botania", "stone", "andesite", 0, 20);
		addSupport("Botania", "stone", "andesite", 4, 21);
		addSupport("Botania", "stone", "andesite", 8, 22);
		addSupport("Botania", "stone", "andesite", 12, 23);

		addSupport("ganyssurface", "18Stones", "andesite", 5, 24);
		addSupport("ganyssurface", "18Stones", "andesite", 6, 25);

		//Diorite

		addSupport("Botania", "stone", "diorite", 2, 20);
		addSupport("Botania", "stone", "diorite", 6, 21);
		addSupport("Botania", "stone", "diorite", 10, 22);
		addSupport("Botania", "stone", "diorite", 14, 23);

		addSupport("ganyssurface", "18Stones", "diorite", 1, 24);
		addSupport("ganyssurface", "18Stones", "diorite", 2, 25);

		//Granite

		addSupport("Botania", "stone", "granite", 3, 20);
		addSupport("Botania", "stone", "granite", 7, 21);
		addSupport("Botania", "stone", "granite", 11, 22);
		addSupport("Botania", "stone", "granite", 15, 23);

		addSupport("ganyssurface", "18Stones", "granite", 3, 24);
		addSupport("ganyssurface", "18Stones", "granite", 4, 25);

		//Prismarine

		addSupport("Botania", "prismarine", "prismarine", 0, 20);
		addSupport("Botania", "prismarine", "prismarine", 1, 21);
		addSupport("Botania", "prismarine", "prismarine", 2, 22);

		addSupport("ganyssurface", "prismarineBlocks", "prismarine", 0, 23);
		addSupport("ganyssurface", "prismarineBlocks", "prismarine", 1, 24);
		addSupport("ganyssurface", "prismarineBlocks", "prismarine", 2, 25);



		if (Loader.isModLoaded("EE3")) {
			loadEE3Values();
		}
	}


	public static void addSupport(String modname, String blockname, String name, int metadata, int order) {
		if (Loader.isModLoaded(modname) && GameRegistry.findBlock(modname, blockname) != null) {
			addSupport(name, GameRegistry.findBlock(modname, blockname), metadata, order);
		}
	}

	public static void addSupport(String name, Block block, int metadata, int order) {
		Carving.chisel.addVariation(name, block, metadata, order);
	}

	private static void loadThaumcraftAspects() {
		// ThaumcraftApi.registerObjectTag(new ItemStack(ChiselBlocks.cobblestone, 1, OreDictionary.WILDCARD_VALUE), (new AspectList()).add(Aspect.ENTROPY, 1).add(Aspect.EARTH, 1));
		// ThaumcraftApi.registerObjectTag(new ItemStack(ChiselBlocks.cobblestoneWall, 1, OreDictionary.WILDCARD_VALUE), (new AspectList()).add(Aspect.ENTROPY, 1).add(Aspect.EARTH, 1));
	}

	private static void loadEE3Values() {
		for (String groupName : Carving.chisel.getSortedGroupNames()){
			ICarvingGroup group = Carving.chisel.getGroup(groupName);
			List<ItemStack> baseStacks = new ArrayList<ItemStack>();
			for (ICarvingVariation variation : group.getVariations()){
				if (RecipeUtil.isCreatable(new ItemStack(variation.getBlock(), 1, variation.getBlockMeta()))){
					baseStacks.add(new ItemStack(variation.getBlock(), 1, variation.getBlockMeta()));
				}
			}
			for (ICarvingVariation variation : group.getVariations()){
				ItemStack stack = new ItemStack(variation.getBlock(), 1, variation.getBlockMeta());
				for (ItemStack baseStack : baseStacks) {
//					if (baseStack==null){
//						Chisel.logger.error("Base Stack is null");
//						if (stack!=null){
//							Chisel.logger.info("Non base for base stack is "+stack);
//						}
//						continue;
//					}
//					if (stack==null){
//						Chisel.logger.error("Non Base stack is null");
//						Chisel.logger.info("Base stack for null stack is "+baseStack);
//						continue;
//					}
					//Chisel.logger.info("Adding emc recipe for "+baseStack+" to "+stack);
					RecipeRegistryProxy.addRecipe(stack, Arrays.asList(baseStack));
				}
			}
		}
//		FMLLog.log(Chisel.MOD_ID, Level.INFO, "[Chisel 2] Oooh is that Equivalent Exchange I see?");
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselItems.ballomoss, 1), 16);
//
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.sand_snakestone, 1, 1), 8);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.sand_snakestone, 1, 13), 8);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.cloud, 1, 0), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.cloud, 1, 1), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.cloud, 1, 2), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.cloud, 1, 3), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.cloud, 1, 4), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glass_pane, 1, 1), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glass_pane, 1, 2), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glass_pane, 1, 3), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glass_pane, 1, 4), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glass_pane, 1, 12), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glass_pane, 1, 13), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glass_pane, 1, 14), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glass_pane, 1, 15), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.present, 1, 4), 80);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.road_line, 1, 0), 24);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.road_line, 1, 1), 24);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.road_line, 1, 2), 24);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.road_line, 1, 3), 24);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.technical2, 1, 0), 24);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.technical2, 1, 1), 24);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.technical2, 1, 2), 24);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.technical2, 1, 3), 24);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glass2, 1), 1);
//		EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.technical2, 1, 0), 32);
//
//		for (int x = 0; x < ChiselBlocks.pumpkin.length; x++) {
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.pumpkin[x], 1), 144);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.jackolantern[x], 1), 153);
//		}
//
//		for (int x = 0; x < ChiselBlocks.torches.length; x++) {
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.torches[x], 1), 9);
//		}
//
//		for (int x = 0; x < 10; x++) {
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.valentines, 1, x), 3);
//		}
//
//		for (int x = 0; x < 16; x++) {
//			if (x <= 10)
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.concrete, 1, x), 8);
//			if (x <= 14)
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.grimstone, 1, x), 5);
//			if (x < 14)
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.grimstone, 1, x), 7);
//			if (x < 12)
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.dirt, 1, x), 1);
//			if (x < 5) {
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.rebel, 1, x), 39);
//			}
//
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.fantasyblock, 1, x), 29444);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.fantasyblock, 1, x), 31444);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.limestone, 1, x), 1);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.marble, 1, x), 1);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.marble_pillar, 1, x), 8);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.marble_pillar_slab, 1, x), 4);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.sandstone_scribbles, 1, x), 4);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.stonebricksmooth, 1, x), 1);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.templeblock, 1, x), 108);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.mossy_templeblock, 1, x), 108);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glowstone, 1, x), 1536);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.ice, 1, x), 1);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.woolen_clay, 1, x), 153);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.factoryblock, 1, x), 32);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.hexPlating, 1, x), 37);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.gold_block, 1, x), 18432);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.iron_block, 1, x), 2304);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.laboratoryblock, 1, x), 33);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.technical, 1, x), 32);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.tyrian, 1, x), 33);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.hexLargePlating, 1, x), 37);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.circuits, 1, x), 37);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.factoryblock2, 1, x), 32);
//
//			if (x < 15)
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.warningSign, 1, x), 6);
//
//			if (x < 4)
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.factoryblock2, 1, x), 32);
//
//			for (int c = 0; c < ChiselBlocks.stainedGlass.length; c++) {
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.stainedGlass[c], 1, x), 1);
//			}
//
//			for (int c = 0; c < ChiselBlocks.stainedGlassPane.length; c++) {
//				if (x <= 13)
//					EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.stainedGlassPane[c], 1, x), 1);
//			}
//
//			if (x > 0) {
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.cobblestone, 1, x), 1);
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.mossy_cobblestone, 1, x), 1);
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.netherrack, 1, x), 1);
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.obsidian, 1, x), 64);
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.sandstone, 1, x), 4);
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.voidstonerunic, 1, x), 160);
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.glass, 1, x), 1);
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.redstone_block, 1, x), 288);
//				if (x <= 8)
//					EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.bookshelf, 1, x), 528);
//				if (x <= 12) {
//					EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.diamond_block, 1, x), 73728);
//					if (x != 12)
//						EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.emerald_block, 1, x), 73728);
//				}
//
//				for (int c = 0; c < ChiselBlocks.planks.length; c++) {
//					EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.planks[c], 1, x), 8);
//				}
//			}
//		}
//
//		for (int x = 0; x <= 6; x++) {
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.andesite, 1, x), 129);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.diorite, 1, x), 257);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.granite, 1, x), 513);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.lavastone, 1, x), 9);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.lavastone, 1, 7), 9);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.voidstone, 1, x), 160);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.voidstone, 1, 7), 160);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.voidstone2, 1, x), 256);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.voidstone2, 1, 7), 256);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.waterstone, 1, x), 1);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.ice_pillar, 1, x), 1);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.iron_bars, 1, x), 96);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.iron_bars, 1, 7), 96);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.iron_bars, 1, 8), 96);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.iron_bars, 1, 9), 96);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.paperwall, 1, x), 32);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.paperwall, 1, 7), 32);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.paperwall, 1, 8), 32);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.paperwall_block, 1, x), 32);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.paperwall_block, 1, 7), 32);
//			EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.paperwall_block, 1, 8), 32);
//			if (x > 0) {
//				EnergyValueRegistryProxy.addPreAssignedEnergyValue(new ItemStack(ChiselBlocks.brickCustom, 1, x), 256);
//			}
//		}
	}
}
