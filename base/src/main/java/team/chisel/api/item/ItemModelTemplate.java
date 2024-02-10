package team.chisel.api.item;

import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.world.item.BlockItem;

public interface ItemModelTemplate {
    void accept(RegistrateItemModelProvider prov, BlockItem item);
}
