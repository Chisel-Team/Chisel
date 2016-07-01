package team.chisel.common.carving;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.SneakyThrows;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;

import com.google.common.collect.Lists;
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
				ItemStack stack1 = v.getStack(), stack2 = v2.getStack();
				if (stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2)) {
					return true;
				} else if (v.getBlock() != null && v.getBlock() != Blocks.AIR) {
					return v.getBlockState().equals(v2.getBlockState());
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

	private static final Method stackedBlockMethod = ReflectionHelper.findMethod(Block.class, null, new String[]{"createStackedBlock","func_180643_i"}, IBlockState.class);

	private class BlockKey implements ICarvingVariation {

		private IBlockState state;

		private BlockKey(IBlockState state) {
			this.state = state;
		}

		@Override
		public Block getBlock() {
			return state.getBlock();
		}

		@Override
		public IBlockState getBlockState() {
			return state;
		}

		@SneakyThrows
		@Override
		public ItemStack getStack() {
			return (ItemStack) stackedBlockMethod.invoke(getBlock(), state);
		}

		@Override
		public int getOrder() {
			return 0;
		}

		@Override
		public String toString() {
			return state.toString();
		}
	}

	private class StackKey implements ICarvingVariation {

		ItemStack stack;
		Block block;

		private StackKey(ItemStack stack) {
			this.stack = stack;
			this.block = Block.getBlockFromItem(stack.getItem());
		}

		@Override
		public Block getBlock() {
			return block;
		}

		@Override
		public IBlockState getBlockState() {
			return block.getDefaultState();
		}

		@Override
		public ItemStack getStack() {
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
			List<VariationWrapper> toRemove = Lists.newArrayList();
			for (VariationWrapper v : lookup.keySet()) {
				if (lookup.get(v).getName().equals(((ICarvingGroup) o).getName())) {
					toRemove.add(v);
				}
			}
			for (VariationWrapper v : toRemove) {
				lookup.remove(v);
			}
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

	public ICarvingGroup getGroup(IBlockState state) {
		return getGroup(new BlockKey(state));
	}
	
	public ICarvingGroup getGroup(ItemStack stack) {
		return getGroup(new StackKey(stack));
	}
	
	public ICarvingGroup getGroup(ICarvingVariation variation) {
		return lookup.get(new VariationWrapper(variation));
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
	
	public ICarvingVariation removeVariation(ItemStack stack, String group) {
		return removeVariation(new StackKey(stack), group);
	}

	public ICarvingVariation removeVariation(IBlockState state, String group) {
		return removeVariation(new BlockKey(state), group);
	}

	public ICarvingVariation removeVariation(ICarvingVariation variation, String group) {
		ICarvingGroup g = null;
		if (group != null) {
			g = groups.get(group);
			if (g == null) {
				throw new IllegalArgumentException("No such group " + group);
			}
			groups.remove(g.getName());
		}
		List<VariationWrapper> toRemove = Lists.newArrayList();
		for (VariationWrapper v : lookup.keySet()) {
			if ((g == null || lookup.get(v).getName().equals(g.getName())) && v.equals(new VariationWrapper(variation))) {
				lookup.get(v).removeVariation(v.v);
				toRemove.add(v);
			}
		}
		for (VariationWrapper v : toRemove) {
			lookup.remove(v);
		}
		return toRemove.isEmpty() ? null : toRemove.get(0).v;
	}

	@Override
	public String toString() {
		return groups.toString();
	}
}
