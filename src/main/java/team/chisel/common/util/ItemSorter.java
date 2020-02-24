package team.chisel.common.util;

import java.util.Comparator;

import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class ItemSorter {
    
    public static <T extends IItemProvider> Comparator<T> byName(Comparator<ResourceLocation> child) {
        return Comparator.comparing(i -> i.asItem().getRegistryName(), child);
    }
    
    public static <T extends IItemProvider> Comparator<T> alphabetic() {
        return byName(alphabeticByName()); 
    }
    
    private static Comparator<ResourceLocation> alphabeticByName() {
        return Comparator.<ResourceLocation, String>comparing(r -> r.getNamespace()).thenComparing(r -> r.getPath());
    }
    
    public static <T extends IItemProvider> Comparator<T> alphabeticVanillaFirst() {
        return byName(vanillaFirst().thenComparing(alphabeticByName()));
    }
    
    public static <T extends IItemProvider> Comparator<T> variantOrder() {
        return byName(vanillaFirst().thenComparing(rawFirst()).thenComparing(alphabeticByName()));
    }

    private static Comparator<ResourceLocation> vanillaFirst() {
        return Comparator.comparing(r -> r.getNamespace(), (n1, n2) ->
                n1.equals("minecraft") && n2.equals("minecraft") ? 0 :
                n1.equals("minecraft") ? -1 :
                n2.equals("minecraft") ? 1 :
                0);
    }
    
    private static Comparator<ResourceLocation> rawFirst() {
        return Comparator.comparing(r -> r.getPath(), (n1, n2) ->
                n1.endsWith("/raw") && n2.endsWith("/raw") ? 0 :
                n1.endsWith("/raw") ? -1 :
                n2.endsWith("/raw") ? 1 :
                0);
    }
}
