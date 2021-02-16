package team.chisel.common.util;

import java.util.Comparator;

import net.minecraft.item.Item;
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
        return ItemSorter.<T>vanillaFirst().thenComparing(ItemSorter.<T>byName(alphabeticByName()));
    }
    
    public static <T extends IItemProvider> Comparator<T> variantOrder() {
        return ItemSorter.<T>vanillaFirst().thenComparing(byName(rawFirst().thenComparing(alphabeticByName())));
    }

    private static <T extends IItemProvider> Comparator<T> vanillaFirst() {
        return (i1, i2) -> {
            String n1 = i1.asItem().getRegistryName().getNamespace();
            String n2 = i2.asItem().getRegistryName().getNamespace();
            if (n1.equals("minecraft") && n2.equals("minecraft")) {
                return Integer.compare(Item.getIdFromItem(i1.asItem()), Item.getIdFromItem(i2.asItem()));
            } else {
                return n1.equals("minecraft") ? -1 : n2.equals("minecraft") ? 1 : 0;
            }
        };
    }
    
    private static Comparator<ResourceLocation> rawFirst() {
        return Comparator.comparing(r -> r.getPath(), (n1, n2) ->
                n1.endsWith("/raw") && n2.endsWith("/raw") ? 0 :
                n1.endsWith("/raw") ? -1 :
                n2.endsWith("/raw") ? 1 :
                0);
    }
}
