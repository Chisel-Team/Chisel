package team.chisel.api.block;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.world.level.block.Block;

public interface RecipeTemplate {

    static RecipeTemplate none() {
        return (provider, block) -> {
        };
    }

    void accept(RegistrateRecipeProvider provider, Block block);
}
