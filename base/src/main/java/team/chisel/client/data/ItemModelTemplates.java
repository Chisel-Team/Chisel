package team.chisel.client.data;

import net.minecraft.resources.ResourceLocation;
import team.chisel.api.item.ItemModelTemplate;

public class ItemModelTemplates {

    public static ItemModelTemplate generated() {
        return (prov, item) -> {
            prov.withExistingParent("item/" + prov.name(() -> item), new ResourceLocation(prov.modid(() -> item), "block/" + prov.name(() -> item)));
        };
    }

    public static ItemModelTemplate texture() {
        return (prov, item) -> {
            prov.withExistingParent("item/" + prov.name(() -> item), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(() -> item));
        };
    }

    public static ItemModelTemplate pane() {
        return (prov, item) -> {
            prov.withExistingParent("item/" + prov.name(() -> item), new ResourceLocation("item/generated")).texture("layer0", "block/" + prov.name(() -> item).replaceFirst("pane", ""));
        };
    }
}
