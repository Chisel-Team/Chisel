package com.cricketcraft.chisel.carving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingRegistry;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;

public class Carving implements ICarvingRegistry {

	GroupList groups = new GroupList();

	public static final Carving chisel = new Carving();
	public static final Carving needle = new Carving();

	private Carving() {
	}

	@Override
	public ICarvingVariation getVariation(Block block, int metadata) {
		ICarvingGroup g = getGroup(block, metadata);
		if (g != null) {
			for (ICarvingVariation v : g.getVariations()) {
				if (v.getBlock() == block && v.getBlockMeta() == metadata) {
					return v;
				}
			}
		}
		return null;
	}

	@Override
	public List<ICarvingVariation> getGroupVariations(Block block, int metadata) {
		ICarvingGroup group = getGroup(block, metadata);
		if (group == null)
			return null;

		return group.getVariations();
	}

	@Override
	public String getOreName(Block block, int metadata) {
		ICarvingGroup group = getGroup(block, metadata);
		if (group == null)
			return null;

		return group.getOreName();
	}

	@Override
	public List<ItemStack> getItemsForChiseling(ItemStack chiseledItem) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		int damage = chiseledItem.getItemDamage();

		ICarvingGroup group = getGroup(Block.getBlockFromItem(chiseledItem.getItem()), damage);
		if (group == null)
			return items;

		HashMap<String, Integer> mapping = new HashMap<String, Integer>();

		List<ICarvingVariation> variations = group.getVariations();

		if (!group.getVariations().isEmpty()) {
			for (ICarvingVariation v : variations) {

				String key = Block.getIdFromBlock(v.getBlock()) + "|" + v.getItemMeta();
				if (mapping.containsKey(key))
					continue;
				mapping.put(key, 1);

				items.add(new ItemStack(v.getBlock(), 1, v.getItemMeta()));
			}
		}

		ArrayList<ItemStack> ores;
		String oreName = group.getOreName();
		if (oreName != null && ((ores = OreDictionary.getOres(oreName)) != null)) {
			for (ItemStack stack : ores) {

				String key = Item.getIdFromItem(stack.getItem()) + "|" + stack.getItemDamage();
				if (mapping.containsKey(key))
					continue;
				mapping.put(key, 2);

				items.add(stack);
			}
		}

		return items;
	}

	@Override
	public ICarvingGroup getGroup(Block block, int metadata) {
		int[] ids = OreDictionary.getOreIDs(new ItemStack(block, metadata));
		if (ids.length > 0) {
			for (int id : ids) {
				ICarvingGroup oreGroup = groups.getGroupByOre(OreDictionary.getOreName(id));
				if (oreGroup != null) {
					return oreGroup;
				}
			}
		}

		return groups.getGroup(block, metadata);
	}

	@Override
	public ICarvingGroup getGroup(String name) {
		return groups.getGroupByName(name);
	}

	@Override
	public ICarvingGroup removeGroup(String groupName) {
		ICarvingGroup g = groups.getGroupByName(groupName);
		return groups.remove(g) ? g : null;
	}

	@Override
	public void addVariation(String groupName, Block block, int metadata, int order) {
		if (block == null) {
			throw new NullPointerException("Cannot add variation in group " + groupName + " for null block.");
		}

		ICarvingVariation variation = new CarvingVariation(block, metadata, order);
		addVariation(groupName, variation);
	}

	@Override
	public void addVariation(String groupName, ICarvingVariation variation) {
		if (groupName == null) {
			throw new NullPointerException("Cannot add variation to null group name.");
		} else if (variation == null) {
			throw new NullPointerException("Cannot add variation in group " + groupName + " for null variation.");
		}

		ICarvingGroup group = groups.getGroupByName(groupName);

		if (group == null) {
			group = new CarvingGroup(groupName);
			addGroup(group);
		}

		groups.addVariation(groupName, variation);
	}

	@Override
	public void addGroup(ICarvingGroup group) {
		groups.add(group);
	}

	@Override
	public void registerOre(String name, String oreName) {
		ICarvingGroup group = groups.getGroupByName(name);
		if (group != null) {
			group.setOreName(oreName);
		} else {
			throw new NullPointerException("Cannot register ore name for group " + name + ", as it does not exist.");
		}
	}

	@Override
	public void setVariationSound(String name, String sound) {
		ICarvingGroup group = groups.getGroupByName(name);
		if (group != null) {
			group.setSound(sound);
		} else {
			throw new NullPointerException("Cannot set sound for group " + name + ", as it does not exist.");
		}
	}

	@Override
	public String getVariationSound(Block block, int metadata) {
		return getVariationSound(Item.getItemFromBlock(block), metadata);
	}

	@Override
	public String getVariationSound(Item item, int metadata) {
		ICarvingGroup group = groups.getGroup(Block.getBlockFromItem(item), metadata);
		String sound = group == null ? null : group.getSound();
		return sound == null ? Chisel.MOD_ID + ":chisel.fallback" : sound;
	}

	@Override
	public List<String> getSortedGroupNames() {
		List<String> names = new ArrayList<String>();
		names.addAll(groups.getNames());
		Collections.sort(names);
		return names;
	}
}
