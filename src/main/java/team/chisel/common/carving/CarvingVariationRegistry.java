package team.chisel.common.carving;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.ICarvingVariationProvider;
import team.chisel.api.carving.IVariationRegistry;

@ParametersAreNonnullByDefault
public class CarvingVariationRegistry implements IVariationRegistry {
    
    private final ListMultimap<ResourceLocation, ICarvingVariationProvider> providers = ArrayListMultimap.create();
    private final ListMultimap<ResourceLocation, ICarvingVariation> resolvedVariations = ArrayListMultimap.create();
     
    public CarvingVariationRegistry() {
        
    }
    
    private void onServerStarting(FMLServerAboutToStartEvent event) {
        event.getServer().getResourceManager().addReloadListener(new ISelectiveResourceReloadListener() {
            
            @Override
            public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
                resolve();
            }
        });
    }
    
    private void resolve() {
        resolvedVariations.clear();
        providers.asMap().forEach((g, providers) -> providers.stream()
                .map(ICarvingVariationProvider::provide)
                .forEach(vars -> resolvedVariations.putAll(g, vars)));
    }

//    @Override
//    public Optional<ResourceLocation> getGroup(BlockState state) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public Optional<ResourceLocation> getGroup(ItemStack stack) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public Optional<ICarvingVariation> getVariation(BlockState state) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public Optional<ICarvingVariation> getVariation(ItemStack stack) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public List<ItemStack> getItemsForChiseling(ItemStack chiseled) {
//        // TODO Auto-generated method stub
//        return null;
//    }

    @Override
    public List<ItemStack> getItemsForChiseling(ResourceLocation group) {
        return resolvedVariations.get(group).stream()
                .map(ICarvingVariation::getStack)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceLocation> getSortedGroups() {
        return providers.keySet().stream()
                .sorted(Comparator.comparing(ResourceLocation::getNamespace).thenComparing(ResourceLocation::getPath))
                .collect(Collectors.toList());
    }

    @Override
    public void addVariations(ResourceLocation groupName, ICarvingVariationProvider provider) {
        providers.put(groupName, provider);
    }

    @Override
    public List<ICarvingVariation> removeGroup(ResourceLocation groupName) {
        providers.removeAll(groupName);
        return resolvedVariations.removeAll(groupName);
    }

//    @Override
//    @Nullable
//    public ICarvingVariation removeVariation(BlockState state) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    @Nullable
//    public ICarvingVariation removeVariation(BlockState state, ResourceLocation group) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    @Nullable
//    public ICarvingVariation removeVariation(ItemStack stack) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    @Nullable
//    public ICarvingVariation removeVariation(ItemStack stack, ResourceLocation group) {
//        // TODO Auto-generated method stub
//        return null;
//    }

}
