package com.cricketcraft.chisel.carving;

import java.util.Collections;
import java.util.List;

import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.google.common.collect.ImmutableList;
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
		return ImmutableList.copyOf(variations);
	}

	@Override
	public void addVariation(ICarvingVariation variation) {
		variations.add(variation);
		Collections.sort(variations);
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
