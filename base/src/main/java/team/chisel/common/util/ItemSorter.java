package team.chisel.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.Comparator;
import java.util.Objects;

@SuppressWarnings("unused")
public class ItemSorter {

    public static <T extends ItemLike> Comparator<T> byName(Comparator<ResourceLocation> child) {
        return Comparator.comparing(i -> i.asItem().getRegistryName(), child);
    }

    public static <T extends ItemLike> Comparator<T> alphabetic() {
        return byName(alphabeticByName());
    }

    private static Comparator<ResourceLocation> alphabeticByName() {
        return Comparator.comparing(ResourceLocation::getNamespace).thenComparing(ResourceLocation::getPath);
    }

    public static <T extends ItemLike> Comparator<T> alphabeticVanillaFirst() {
        return ItemSorter.<T>vanillaFirst().thenComparing(ItemSorter.byName(alphabeticByName()));
    }

    public static <T extends ItemLike> Comparator<T> variantOrder() {
        return ItemSorter.<T>vanillaFirst().thenComparing(byName(rawFirst().thenComparing(alphabeticByName())));
    }

    private static <T extends ItemLike> Comparator<T> vanillaFirst() {
        return (i1, i2) -> {
            String n1 = Objects.requireNonNull(i1.asItem().getRegistryName()).getNamespace();
            String n2 = Objects.requireNonNull(i2.asItem().getRegistryName()).getNamespace();
            if (n1.equals("minecraft") && n2.equals("minecraft")) {
                return Integer.compare(Item.getId(i1.asItem()), Item.getId(i2.asItem()));
            } else {
                return n1.equals("minecraft") ? -1 : n2.equals("minecraft") ? 1 : 0;
            }
        };
    }

    private static Comparator<ResourceLocation> rawFirst() {
        return Comparator.comparing(ResourceLocation::getPath, (n1, n2) ->
                n1.endsWith("/raw") && n2.endsWith("/raw") ? 0 :
                        n1.endsWith("/raw") ? -1 :
                                n2.endsWith("/raw") ? 1 :
                                        0);
    }
}
