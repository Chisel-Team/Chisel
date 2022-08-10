package team.chisel.common.carving;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IVariationRegistry;
import team.chisel.common.util.ItemSorter;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CarvingVariationRegistry implements IVariationRegistry {

    private final Map<ResourceLocation, ICarvingGroup> groups = new HashMap<>();

    public CarvingVariationRegistry() {

    }

    @Override
    public Optional<ICarvingGroup> getGroup(ResourceLocation id) {
        return Optional.ofNullable(groups.get(id));
    }

    @Override
    public Optional<ICarvingGroup> getGroup(Item item) {
        return groups.values().stream()
                .filter(g -> g.getItemTag().contains(item))
                .findFirst();
    }

    @Override
    public Optional<ICarvingGroup> getGroup(Block block) {
        return groups.values().stream()
                .filter(g -> g.getBlockTag().contains(block))
                .findFirst();
    }

    @Override
    public Optional<ICarvingVariation> getVariation(Block block) {
        return getGroup(block).map(g -> new ICarvingVariation() {

            @Override
            public Item getItem() {
                return block.asItem();
            }

            @Override
            public ICarvingGroup getGroup() {
                return g;
            }

            @Override
            public Block getBlock() {
                return block;
            }
        });
    }

    @Override
    public Optional<ICarvingVariation> getVariation(Item item) {
        return getGroup(item).map(g -> new ICarvingVariation() {

            @Override
            public Item getItem() {
                return item;
            }

            @Override
            public ICarvingGroup getGroup() {
                return g;
            }

            @Override
            public Block getBlock() {
                return Block.byItem(item);
            }
        });
    }

    @Override
    public List<ItemStack> getItemsForChiseling(ItemStack chiseled) {
        return getGroup(chiseled.getItem())
                .map(this::getItemsForChiseling)
                .orElse(Collections.emptyList());
    }

    @Override
    public List<ItemStack> getItemsForChiseling(ResourceLocation groupId) {
        ICarvingGroup group = groups.get(groupId);
        return group == null ? Collections.emptyList() : getItemsForChiseling(group);
    }

    private List<ItemStack> getItemsForChiseling(ICarvingGroup group) {
        return group.getItemTag().stream()
                .map(ItemStack::new)
                .sorted(Comparator.comparing(ItemStack::getItem, ItemSorter.variantOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ICarvingGroup> getGroups() {
        return ImmutableList.copyOf(groups.values());
    }

    @Override
    public void addGroup(ICarvingGroup group) {
        groups.put(group.getId(), group);
    }

    @Override
    public ICarvingGroup removeGroup(ResourceLocation groupName) {
        return groups.remove(groupName);
    }

    @Override
    public @Nullable ICarvingVariation removeVariation(BlockState state) {
        return null;
    }

    @Override
    public @Nullable ICarvingVariation removeVariation(BlockState state, ResourceLocation group) {
        return null;
    }

    @Override
    public @Nullable ICarvingVariation removeVariation(ItemStack stack) {
        return null;
    }

    @Override
    public @Nullable ICarvingVariation removeVariation(ItemStack stack, ResourceLocation group) {
        return null;
    }

    public List<ResourceLocation> getSortedGroups() {
        return groups.keySet().stream()
                .sorted(Comparator.comparing(ResourceLocation::getNamespace).thenComparing(ResourceLocation::getPath))
                .collect(Collectors.toList());
    }
}
