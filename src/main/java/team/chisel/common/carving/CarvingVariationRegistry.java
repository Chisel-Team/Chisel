package team.chisel.common.carving;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IVariationRegistry;
import team.chisel.common.util.ItemSorter;

@ParametersAreNonnullByDefault
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
                .filter(g -> g.getBlockTag().map(tag -> tag.contains(block)).orElse(false))
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
                return Block.getBlockFromItem(item);
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
        return group.getItemTag().getAllElements().stream()
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
    
    private void onServerStarting(FMLServerAboutToStartEvent event) {
        event.getServer().getResourceManager().addReloadListener(new ISelectiveResourceReloadListener() {
            
            @Override
            public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
//                resolve();
            }
        });
    }
//    @Override
//    public List<ResourceLocation> getSortedGroups() {
//        return groups.keySet().stream()
//                .sorted(Comparator.comparing(ResourceLocation::getNamespace).thenComparing(ResourceLocation::getPath))
//                .collect(Collectors.toList());
//    }
}
