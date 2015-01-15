package com.cricketcraft.chisel.carving;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.block.Block;

import org.apache.commons.lang3.tuple.Pair;

import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.google.common.collect.Maps;

public class GroupList implements Set<ICarvingGroup> {

	private class VariationWrapper {

		private ICarvingVariation v;

		private VariationWrapper(ICarvingVariation v) {
			this.v = v;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ICarvingVariation) {
				ICarvingVariation v2 = (ICarvingVariation) obj;
				return v.getBlock() == v2.getBlock() && (v.getBlockMeta() == v2.getBlockMeta() || v.getItemMeta() == v2.getItemMeta());
			} else if (obj instanceof VariationWrapper) {
				return equals(((VariationWrapper) obj).v);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return v.getBlock().hashCode();
		}
	}

	private class VariationKey implements ICarvingVariation {

		Pair<Block, Integer> data;

		private VariationKey(Block block, int blockMeta) {
			data = Pair.of(block, blockMeta);
		}

		@Override
		public Block getBlock() {
			return data.getLeft();
		}

		@Override
		public int getBlockMeta() {
			return data.getRight();
		}

		@Override
		public int getItemMeta() {
			return data.getRight();
		}

		@Override
		public int getOrder() {
			return 0;
		}

		@Override
		public String toString() {
			return data.toString();
		}
	}

	private HashMap<String, ICarvingGroup> groups = Maps.newHashMap();
	private HashMap<VariationWrapper, ICarvingGroup> lookup = Maps.newHashMap();

	@Override
	public int size() {
		return groups.size();
	}

	@Override
	public boolean isEmpty() {
		return groups.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof ICarvingGroup) {
			return groups.containsKey(((ICarvingGroup) o).getName());
		}
		return false;
	}

	@Override
	public Iterator<ICarvingGroup> iterator() {
		return groups.values().iterator();
	}

	@Override
	public Object[] toArray() {
		return groups.values().toArray();
	}

	@Override
	public <T> T[] toArray(T[] arr) {
		return groups.values().toArray(arr);
	}

	@Override
	public boolean add(ICarvingGroup group) {
		String key = group.getName();
		if (groups.containsKey(key)) {
			return false;
		}
		for (ICarvingVariation v : group.getVariations()) {
			ICarvingGroup g = lookup.get(v);
			if (g == null) {
				lookup.put(new VariationWrapper(v), group);
			}
		}
		groups.put(key, group);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (o instanceof ICarvingGroup) {
			return groups.remove(((ICarvingGroup) o).getName()) != null;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!(o instanceof ICarvingGroup)) {
				return false;
			}
			if (!groups.containsKey(((ICarvingGroup) o).getName())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends ICarvingGroup> c) {
		boolean ret = false;
		for (ICarvingGroup g : c) {
			ret |= add(g);
		}
		return ret;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean ret = false;
		for (Object o : c) {
			ret |= remove(o);
		}
		return ret;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException(); // not using it, not gonna bother
	}

	@Override
	public void clear() {
		groups.clear();
	}

	public ICarvingGroup getGroup(Block block, int metadata) {
		return lookup.get(new VariationWrapper(new VariationKey(block, metadata)));
	}

	public void addVariation(String name, ICarvingVariation variation) {
		ICarvingGroup g = groups.get(name);
		if (g == null) {
			throw new NullPointerException("No group exists for name " + name);
		}
		g.addVariation(variation);
		lookup.put(new VariationWrapper(variation), g);
	}

	public ICarvingGroup getGroupByName(String groupName) {
		return groups.get(groupName);
	}

	public ICarvingGroup getGroupByOre(String oreName) {
		for (ICarvingGroup group : groups.values()) {
			if (oreName.equals(group.getOreName())) {
				return group;
			}
		}
		return null;
	}

	public Collection<? extends String> getNames() {
		return groups.keySet();
	}

	@Override
	public String toString() {
		return groups.toString();
	}
}
