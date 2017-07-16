package team.chisel.common.carving;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;

public class GroupList implements Set<ICarvingGroup> {

	private class VariationWrapper {

		private @Nonnull ICarvingVariation v;

		private VariationWrapper(@Nonnull ICarvingVariation v) {
			this.v = v;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ICarvingVariation) {
				ICarvingVariation v2 = (ICarvingVariation) obj;
				ItemStack stack1 = v.getStack(), stack2 = v2.getStack();
				
				if (stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2)) {
                    return true;
				}
			} else if (obj instanceof VariationWrapper) {
				return equals(((VariationWrapper) obj).v);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return v.getStack().getItem().hashCode() ^ v.getStack().getItemDamage();
		}
	}

	private class StackKey implements ICarvingVariation {

		@Nonnull ItemStack stack;

		private StackKey(@Nonnull ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public Block getBlock() {
			return null;
		}

		@Override
		public IBlockState getBlockState() {
			return null;
		}

		@Override
		public @Nonnull ItemStack getStack() {
			return stack;
		}

		@Override
		public int getOrder() {
			return 0;
		}

		@Override
		public String toString() {
			return stack.toString();
		}
	}

	private Map<String, ICarvingGroup> groups = Maps.newHashMap();
	private Map<VariationWrapper, ICarvingGroup> lookup = Maps.newHashMap();
    private Map<IBlockState, ICarvingGroup> stateGroups = Maps.newIdentityHashMap();

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
            if (v == null) {
                continue;
            }
			if (!lookup.containsKey(new VariationWrapper(v))) {
				lookup.put(new VariationWrapper(v), group);
			}
			if (v.getBlockState() != null) {
			    stateGroups.put(v.getBlockState(), group);
			}
		}
		groups.put(key, group);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (o instanceof ICarvingGroup) {
		    Predicate<Entry<?, ICarvingGroup>> test = e -> e.getValue().getName().equals(((ICarvingGroup)o).getName());
			lookup.entrySet().removeIf(test);
			stateGroups.entrySet().removeIf(test);
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

	public ICarvingGroup getGroup(@Nonnull IBlockState state) {
		return stateGroups.get(state);
	}
	
	public ICarvingGroup getGroup(@Nonnull ItemStack stack) {
		return getGroup(new StackKey(stack));
	}
	
	public ICarvingGroup getGroup(@Nonnull ICarvingVariation variation) {
		return lookup.get(new VariationWrapper(variation));
	}

	@SuppressWarnings("deprecation")
    public void addVariation(String name, @Nonnull ICarvingVariation variation) {
		ICarvingGroup g = groups.get(name);
		if (g == null) {
			throw new NullPointerException("No group exists for name " + name);
		}
		g.addVariation(variation);
		lookup.put(new VariationWrapper(variation), g);
		if (variation.getBlockState() != null) {
		    stateGroups.put(variation.getBlockState(), g);
		}
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
	
	public ICarvingVariation removeVariation(@Nonnull ItemStack stack, String group) {
		return removeVariation(new StackKey(stack), group);
	}

	public ICarvingVariation removeVariation(IBlockState state, String group) {
	    ICarvingGroup carvingGroup = groups.get(group);
	    ICarvingVariation variation = null;
	    for (ICarvingVariation v : carvingGroup) {
	        if (v.getBlockState() == state) {
	            variation = v;
	        }
	    }
	    if (variation == null) {
	        return null;
	    }
		return removeVariation(variation, group);
	}

	@SuppressWarnings("deprecation")
    public ICarvingVariation removeVariation(@Nonnull ICarvingVariation variation, String group) {
		ICarvingGroup g = null;
		if (group != null) {
			g = groups.get(group);
			if (g == null) {
				throw new IllegalArgumentException("No such group " + group);
			}
		}
		List<VariationWrapper> toRemove = Lists.newArrayList();
		for (VariationWrapper vw : lookup.keySet()) {
			if ((g == null || lookup.get(vw).getName().equals(g.getName())) && vw.equals(new VariationWrapper(variation))) {
				lookup.get(vw).removeVariation(vw.v);
				toRemove.add(vw);
			}
		}
		for (VariationWrapper vw : toRemove) {
			lookup.remove(vw);
	        if (vw.v.getBlockState() != null) {
	            stateGroups.remove(vw.v.getBlockState());
	        }
		}
		return toRemove.isEmpty() ? null : toRemove.get(0).v;
	}

	@Override
	public String toString() {
		return groups.toString();
	}
}
