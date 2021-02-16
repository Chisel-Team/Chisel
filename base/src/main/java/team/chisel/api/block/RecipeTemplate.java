package team.chisel.api.block;

import javax.annotation.ParametersAreNonnullByDefault;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface RecipeTemplate {

    void accept(RegistrateRecipeProvider provider, Block block);
    
    static RecipeTemplate none() {
        return (provider, block) -> {};
    }
}
