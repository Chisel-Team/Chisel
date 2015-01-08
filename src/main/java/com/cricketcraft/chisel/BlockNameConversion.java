package com.cricketcraft.chisel;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import com.cricketcraft.chisel.utils.General;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockNameConversion {

	static final private HashMap<String, String> namingConversion = new HashMap<String, String>();

	public static class NameConversions {

		public final static int MAX = 5;

		protected static String conversion(String oldname, int step) {
			oldname = General.cleanTags(oldname);
			String newname = null;
			switch (step) {
			case 0: // lookup list
				return namingConversion.get(oldname);
			case 1: // remove Chisel:
				if (oldname.startsWith("Chisel:")) {
					newname = oldname.replaceAll("(?i)" + "Chisel:", "");
				}
				return newname;
			case 2: // rename "marbleSlab" -> "marble_slab", "redstone" ->
					// "redstone_block"
				newname = oldname.replaceAll("([a-z]+)([A-Z])", "$1_$2").toLowerCase();

				if (newname.startsWith("block_")) {
					newname = newname.replaceFirst("block_([a-z_]+)", "$1_block");
				} else if (newname.startsWith("snakestone_")) {
					newname = newname.replaceFirst("snakestone_([a-z_]+)", "$1_snakestone");
				}
				return newname;
			case 3: // rename "wood-dark-oak" -> "dark_oak_planks"
				newname = oldname.replaceAll("([a-z]+)([A-Z])", "$1_$2").toLowerCase().replace('-', '_');
				if (newname.contains("wood_")) {
					newname = newname.replaceFirst("wood_([a-z_]+)", "$1_planks");
				}
				return newname;
			case (MAX - 1): // ALWAYS last: rename "blockYxx" -> "yxx" (e.g.
							// "dirt" -> "dirt").
				// This can be dangerous in cases like "carpet" (a block) ->
				// "carpet" (NO block)
				return oldname.replaceAll("([a-z]+)([A-Z])", "$1_$2").toLowerCase().replaceFirst("block_", "");
			default:
				throw new IndexOutOfBoundsException("conversion got invalid parameter step=" + step);
			}
		}
	}

	static public void init() {
		namingConversion.put("blockCobble", "cobblestone");
		namingConversion.put("iron", "iron_block");
		namingConversion.put("gold", "gold_block");
		namingConversion.put("diamond", "diamond_block");
		namingConversion.put("lightstone", "glowstone");
		namingConversion.put("lapis", "lapis_block");
		namingConversion.put("emerald", "emerald_block");
		namingConversion.put("hellrock", "netherrack");
		namingConversion.put("stoneMoss", "mossy_cobblestone");
		namingConversion.put("stoneBrick", "stonebrick");
		namingConversion.put("fenceIron", "iron_bars");
		namingConversion.put("fantasy", "fantasyblock");
		namingConversion.put("carpetFloor", "carpet");
		namingConversion.put("temple", "templeblock");
		namingConversion.put("templeMossy", "mossy_templeblock");
		namingConversion.put("factory", "factoryblock");
		namingConversion.put("laboratory", "laboratoryblock");
		// "LightGray" is unfortunately converted to "light_gray"
		namingConversion.put("stainedGlassLightGray", "stained_glass_lightgray");
		namingConversion.put("stainedGlassPaneLightGray", "stained_glass_pane_lightgray");
		namingConversion.put("blockTemple", "templeblock");
		namingConversion.put("stoneBrick", "stonebricksmooth");
		namingConversion.put("blockTempleMossy", "mossy_templeblock");
		namingConversion.put("blockFactory", "factoryblock");
		namingConversion.put("blockFft", "fantasyblock");
		namingConversion.put("blockCarpetFloor", "carpet");
		namingConversion.put("blockLaboratory", "laboratoryblock");
	}

	public static Item findItem(String oldname) {
		FMLLog.getLogger().trace("[Chisel 2] findItem() START " + oldname);
		Item item = null;
		for (int i = 0; i < NameConversions.MAX && item == null; i++) {
			FMLLog.getLogger().trace("[Chisel 2] findItem()       Checking for " + NameConversions.conversion(oldname, i));
			item = GameRegistry.findItem(Chisel.MOD_ID, NameConversions.conversion(oldname, i));
		}

		return item;
	}

	public static Block findBlock(String oldname) {
		FMLLog.getLogger().trace("[Chisel 2] findBlock() START: " + oldname);
		Block block = null;
		for (int i = 0; i < NameConversions.MAX && block == null; i++) {
			FMLLog.getLogger().trace("[Chisel 2] findBlock()       Checking for " + NameConversions.conversion(oldname, i));
			block = GameRegistry.findBlock(Chisel.MOD_ID, NameConversions.conversion(oldname, i));
		}

		return block;
	}
}