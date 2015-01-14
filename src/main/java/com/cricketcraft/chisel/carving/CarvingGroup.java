package com.cricketcraft.chisel.carving;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class CarvingGroup {

	public CarvingGroup(String n) {
		name = n;
	}

	String name;
	String className;
	String sound;
	String oreName;

	List<CarvingVariation> variations = new ArrayList<CarvingVariation>();

	public List<CarvingVariation> getVariations() {
		return ImmutableList.copyOf(variations);
	}

	public String getName() {
		return name;
	}

	public String getOreName() {
		return oreName;
	}

}
