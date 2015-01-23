package com.cricketcraft.chisel.carving;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.Block;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.google.common.collect.Lists;

public class CarvingGroup implements ICarvingGroup {

	private String name;
	private String sound;
	private String oreName;

	private List<ICarvingVariation> variations = Lists.newArrayList();

	public CarvingGroup(String name) {
		this.name = name;
	}

	public List<ICarvingVariation> getVariations() {
		return Lists.newArrayList(variations);
	}

	@Override
	public void addVariation(ICarvingVariation variation) {
		variations.add(variation);
		Collections.sort(variations, new Comparator<ICarvingVariation>() {

			@Override
			public int compare(ICarvingVariation o1, ICarvingVariation o2) {
				return CarvingUtils.compare(o1, o2);
			}
		});
	}
	
	@Override
	public ICarvingVariation removeVariation(Block block, int metadata) {
		ICarvingVariation toRemove = null;
		for (ICarvingVariation v : variations) {
			if (v.getBlock() == block && v.getBlockMeta() == metadata) {
				toRemove = v;
			}
		}
		return toRemove == null ? null : variations.remove(toRemove) ? toRemove : null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSound() {
		return sound;
	}

	@Override
	public void setSound(String sound) {
		this.sound = sound;
	}

	@Override
	public String getOreName() {
		return oreName;
	}

	@Override
	public void setOreName(String oreName) {
		this.oreName = oreName;
	}
}
