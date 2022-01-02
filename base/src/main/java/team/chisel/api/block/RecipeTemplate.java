package team.chisel.api.block;

import javax.annotation.ParametersAreNonnullByDefault;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;

import net.minecraft.world.level.block.Block;

public interface RecipeTemplate {

    void accept(RegistrateRecipeProvider provider, Block block);
    
    static RecipeTemplate none() {
        return (provider, block) -> {};
    }
}
