package team.chisel.common.carving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.oredict.OreDictionary;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.init.ChiselSounds;

@ParametersAreNonnullByDefault
public class Carving implements ICarvingRegistry {

	private static class ItemStackWrapper {

		private ItemStack wrapped;

		private ItemStackWrapper(ItemStack stack) {
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
            return ItemStack.areItemStacksEqual(wrapped, other.wrapped) && ItemStack.areItemStackTagsEqual(wrapped, other.wrapped);
        }

	}

	GroupList groups = new GroupList();
	
	private final Multimap<String, ICarvingGroup> oreLookup = HashMultimap.create();

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
				if (stack.isItemEqual(v.getStack()) && (v.getStack().getTagCompound() == null || ItemStack.areItemStackTagsEqual(stack, v.getStack()))) {
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
    @Deprecated
    public @Nullable String getOreName(IBlockState state) {
        return null;
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

    public List<ItemStack> getItemsForChiseling(ICarvingGroup group) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		List<ICarvingVariation> variations = group.getVariations();
		List<ItemStackWrapper> found = Lists.newArrayList();

		if (!group.getVariations().isEmpty()) {
			for (ICarvingVariation v : variations) {
				addNewStackToList(v.getStack(), items, found);
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
        return groups.getGroup(state);
    }

	@SuppressWarnings("deprecation")
    @Override
	public @Nullable ICarvingGroup getGroup(ItemStack stack) {
	    ICarvingGroup group = groups.getGroup(stack);
	    List<ICarvingGroup> oreGroups = Arrays.stream(OreDictionary.getOreIDs(stack))
	                                          .mapToObj(OreDictionary::getOreName)
	                                          .map(oreLookup::get)
	                                          .flatMap(Collection::stream)
	                                          .filter(Objects::nonNull)
	                                          .collect(Collectors.toList());
	        
        if (oreGroups.isEmpty() || (group != null && oreGroups.get(0).getName().equals(group.getName()))) {
            return group;
        } else if (group == null && oreGroups.size() == 1) {
            return oreGroups.get(0);
        } else {
            if (group != null) {
                oreGroups.add(0, group);
            }
            String name = Joiner.on("+").join(oreGroups.stream().map(ICarvingGroup::getName).iterator());
            ICarvingGroup ret = CarvingUtils.getDefaultGroupFor(name);
            for (ICarvingGroup v : oreGroups) {
                v.getVariations().forEach(ret::addVariation);
            }
            return ret;
        }
	}

	@Override
	public @Nullable ICarvingGroup getGroup(String name) {
		return groups.getGroupByName(name);
	}

	@Override
	public @Nullable ICarvingGroup removeGroup(String groupName) {
		ICarvingGroup group = groups.getGroupByName(groupName);
		for (Collection<ICarvingGroup> oregroups : oreLookup.asMap().values()) {
		    oregroups.removeIf(g -> g.getName().equals(groupName));
		}
		return groups.remove(group) ? group : null;
	}

	@Override
	public @Nullable ICarvingVariation removeVariation(IBlockState state) {
        ICarvingGroup group = groups.getGroup(state);
        ICarvingVariation ret = null;
        while (group != null) {
            ret = removeVariation(state, group.getName());
            group = groups.getGroup(state);
        }
        return ret; // TODO return multiple?
    }

    @Override
    public @Nullable ICarvingVariation removeVariation(IBlockState state, String group) {
        return groups.removeVariation(state, group);
    }

    @Override
    public @Nullable ICarvingVariation removeVariation(ItemStack stack) {
        ICarvingGroup group = groups.getGroup(stack);
        ICarvingVariation ret = null;
        while (group != null) {
            ret = removeVariation(stack, group.getName());
            group = groups.getGroup(stack);
        }
        return ret; // TODO return multiple?
    }
    
    @Override
    @Nullable
    public ICarvingVariation removeVariation(ItemStack stack, String group) {
        return groups.removeVariation(stack, group);
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
	@Deprecated
	public void registerOre(String name, String oreName) {}

	@Override
	public void setVariationSound(String name, SoundEvent sound) {
		ICarvingGroup group = groups.getGroupByName(name);
		if (group != null) {
			group.setSound(sound);
		} else {
			throw new NullPointerException("Cannot set sound for group " + name + ", as it does not exist.");
		}
	}

	@Override
	public SoundEvent getVariationSound(IBlockState state) {
		ICarvingGroup group = groups.getGroup(state);
		return getSound(group);
	}

	@Override
	public SoundEvent getVariationSound(ItemStack stack) {
		ICarvingGroup group = groups.getGroup(stack);
		return getSound(group);
	}
	
	@Override
	public SoundEvent getVariationSound(@Nullable ICarvingVariation variation) {
	    ICarvingGroup group = variation == null ? null : groups.getGroup(variation);
	    return getSound(group);
	}

	private SoundEvent getSound(@Nullable ICarvingGroup group) {
	    SoundEvent sound = group == null ? null : group.getSound();
		return sound == null ? ChiselSounds.fallback : sound;
	}

	@Override
	public List<String> getSortedGroupNames() {
		List<String> names = new ArrayList<String>();
		names.addAll(groups.getNames());
		Collections.sort(names);
		return names;
	}
	
	@Override
	public void setOreName(ICarvingGroup group, String ore) {
	    oreLookup.put(ore, group);
	}
}
