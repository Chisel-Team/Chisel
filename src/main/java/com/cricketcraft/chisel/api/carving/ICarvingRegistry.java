package com.cricketcraft.chisel.api.carving;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Represents a registry of {@link ICarvingGroup}s
 * <p>
 * To obtain chisel's instance of this class, use {@link CarvingUtils#getChiselRegistry()}
 */
public interface ICarvingRegistry {

	/* Getters */

	ICarvingGroup getGroup(Block block, int meta);

	ICarvingGroup getGroup(String name);

	ICarvingVariation getVariation(Block block, int meta);

	List<ICarvingVariation> getGroupVariations(Block block, int meta);

	String getOreName(Block block, int meta);

	List<ItemStack> getItemsForChiseling(ItemStack chiseled);

	public String getVariationSound(Block block, int metadata);

	public String getVariationSound(Item item, int metadata);

	List<String> getSortedGroupNames();

	/* Setters */

	void addVariation(String groupName, Block block, int metadata, int order);

	void addVariation(String groupName, ICarvingVariation variation);

	void addGroup(ICarvingGroup group);
	
	ICarvingGroup removeGroup(String groupName);

	void registerOre(String groupName, String oreName);

	void setVariationSound(String name, String sound);
}
