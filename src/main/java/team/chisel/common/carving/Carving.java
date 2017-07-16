package team.chisel.common.carving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.api.carving.ICarvingVariation;

import com.google.common.collect.Lists;

@ParametersAreNonnullByDefault
public class Carving implements ICarvingRegistry {

	private static class ItemStackWrapper {

		private @Nullable ItemStack wrapped;

		private ItemStackWrapper(@Nullable ItemStack stack) {
			this.wrapped = stack;
		}

		@Override
		public boolean equals(@Nullable Object obj) {
			if (this == obj) {
				return true;
			} else if (obj == null) {
				return false;
			} else if (getClass() != obj.getClass()) {
				return false;
			}
			
			ItemStackWrapper other = (ItemStackWrapper) obj;
			if (wrapped == null) {
				if (other.wrapped != null) {
					return false;
				}
			} else {
				return ItemStack.areItemStacksEqual(wrapped, other.wrapped) && ItemStack.areItemStackTagsEqual(wrapped, other.wrapped);
			}
			return true;
		}

	}

	GroupList groups = new GroupList();

	public static final ICarvingRegistry chisel = new Carving();
	public static final Carving needle = new Carving();

	static {
		CarvingUtils.chisel = chisel;
	}

	public static void construct() {
	}

	private Carving() {
	}

	@Override
	public @Nullable ICarvingVariation getVariation(IBlockState state) {
		return getVariation(state, getGroup(state));
	}
	
	@Override
	public @Nullable ICarvingVariation getVariation(ItemStack stack) {
		return getVariation(stack, getGroup(stack));
    }

    private @Nullable ICarvingVariation getVariation(IBlockState state, @Nullable ICarvingGroup group) {
        if (group != null) {
            for (ICarvingVariation v : group.getVariations()) {
                if (v.getBlockState() != null && v.getBlockState().equals(state)) {
                    return v;
                }
            }
        }
        return null;
	}

	private @Nullable ICarvingVariation getVariation(ItemStack stack, @Nullable ICarvingGroup group) {
		if (group != null) {
			for (ICarvingVariation v : group.getVariations()) {
				if (stack.isItemEqual(v.getStack()) && ItemStack.areItemStackTagsEqual(stack, v.getStack())) {
					return v;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("null")
    @Override
	public List<ICarvingVariation> getGroupVariations(IBlockState state) {
		ICarvingGroup group = getGroup(state);
		if (group == null)
			return Collections.emptyList();

		return group.getVariations();
	}

    @Override
    public @Nullable String getOreName(IBlockState state) {
        ICarvingGroup group = getGroup(state);
        if (group == null)
            return null;

        return group.getOreName();
    }

	@SuppressWarnings("null")
    @Override
	public List<ItemStack> getItemsForChiseling(ItemStack chiseledItem) {
		ICarvingGroup group = null;
		
		group = getGroup(chiseledItem);
        if (group == null) {
            return Collections.emptyList();
        }

		return getItemsForChiseling(group);
	}

    @SuppressWarnings("null")
    public List<ItemStack> getItemsForChiseling(ICarvingGroup group) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		List<ICarvingVariation> variations = group.getVariations();
		List<ItemStackWrapper> found = Lists.newArrayList();

		if (!group.getVariations().isEmpty()) {
			for (ICarvingVariation v : variations) {
				addNewStackToList(v.getStack(), items, found);
			}
		}

		List<ItemStack> ores;
		String oreName = group.getOreName();
		if (oreName != null && ((ores = OreDictionary.getOres(oreName)) != null)) {
			for (ItemStack stack : ores) {
				addNewStackToList(stack, items, found);
			}
		}

		return items;
    }
	
	private void addNewStackToList(ItemStack stack, List<ItemStack> list, List<ItemStackWrapper> found) {
		ItemStackWrapper wrapper = new ItemStackWrapper(stack);
		if (!found.contains(wrapper)) {
			list.add(stack.copy());
			found.add(wrapper);
		}
	}

    @Override
    public @Nullable ICarvingGroup getGroup(IBlockState state) {
        ICarvingVariation variation = getVariation(state, groups.getGroup(state));
        ItemStack stack = variation == null ? new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state)) : variation.getStack();
        ICarvingGroup ore = getOreGroup(stack);
        return ore == null ? groups.getGroup(state) : ore;
    }

	@Override
	public @Nullable ICarvingGroup getGroup(ItemStack stack) {
		ICarvingGroup ore = getOreGroup(stack);
		return ore == null ? groups.getGroup(stack) : ore;
	}

	private @Nullable ICarvingGroup getOreGroup(@Nullable ItemStack stack) {
		if(stack != null)
		{
			int[] ids = OreDictionary.getOreIDs(stack);
			if (ids.length > 0) {
				for (int id : ids) {
					ICarvingGroup oreGroup = groups.getGroupByOre(OreDictionary.getOreName(id));
					if (oreGroup != null) {
						return oreGroup;
					}
				}
			}
		}
		return null;
	}

	@Override
	public @Nullable ICarvingGroup getGroup(String name) {
		return groups.getGroupByName(name);
	}

	@Override
	public @Nullable ICarvingGroup removeGroup(String groupName) {
		ICarvingGroup g = groups.getGroupByName(groupName);
		return groups.remove(g) ? g : null;
	}

	@Override
	public @Nullable ICarvingVariation removeVariation(IBlockState state) {
	    ICarvingGroup group = getGroup(state);
	    if (group == null) {
	        return null;
	    }
		return removeVariation(state, group.getName());
	}

	@Override
	public @Nullable ICarvingVariation removeVariation(IBlockState state, String group) {
		return groups.removeVariation(state, group);
	}

	@Override
	@Deprecated
	public void addVariation(String groupName, IBlockState state, int order) {
		ICarvingVariation variation = CarvingUtils.variationFor(state, order);
		addVariation(groupName, variation);
	}

	@Override
	public void addVariation(String groupName, ICarvingVariation variation) {
		ICarvingGroup group = groups.getGroupByName(groupName);

		if (group == null) {
			group = CarvingUtils.getDefaultGroupFor(groupName);
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
	public String getVariationSound(IBlockState state) {
		ICarvingGroup group = groups.getGroup(state);
		return getSound(group);
	}

	@Override
	public String getVariationSound(ItemStack stack) {
		ICarvingGroup group = groups.getGroup(stack);
		return getSound(group);
	}
	
	@Override
	public String getVariationSound(@Nullable ICarvingVariation variation) {
	    ICarvingGroup group = variation == null ? null : groups.getGroup(variation);
	    return getSound(group);
	}

	private String getSound(@Nullable ICarvingGroup group) {
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
