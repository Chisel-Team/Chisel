package team.chisel.api.block;

import javax.annotation.ParametersAreNonnullByDefault;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface ModelTemplate {

    void accept(RegistrateBlockstateProvider provider, Block block);
    
    public interface VariantModelTemplate extends ModelTemplate {
        
        @Override
        default void accept(RegistrateBlockstateProvider provider, Block block) {
            accept(provider, block, provider.getVariantBuilder(block));
        }
        
        void accept(RegistrateBlockstateProvider provider, Block block, VariantBlockStateBuilder builder);
    }
    
    public interface MultipartModelTemplate extends ModelTemplate {
        
        @Override
        default void accept(RegistrateBlockstateProvider provider, Block block) {
            accept(provider, block, provider.getMultipartBuilder(block));
        }
        
        void accept(RegistrateBlockstateProvider provider, Block block, MultiPartBlockStateBuilder builder);
    }
}
