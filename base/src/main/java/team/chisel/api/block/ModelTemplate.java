package team.chisel.api.block;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;

@SuppressWarnings("unused")
public interface ModelTemplate {

    void accept(RegistrateBlockstateProvider provider, Block block);

    interface VariantModelTemplate extends ModelTemplate {

        @Override
        default void accept(RegistrateBlockstateProvider provider, Block block) {
            accept(provider, block, provider.getVariantBuilder(block));
        }

        void accept(RegistrateBlockstateProvider provider, Block block, VariantBlockStateBuilder builder);
    }

    interface MultipartModelTemplate extends ModelTemplate {

        @Override
        default void accept(RegistrateBlockstateProvider provider, Block block) {
            accept(provider, block, provider.getMultipartBuilder(block));
        }

        void accept(RegistrateBlockstateProvider provider, Block block, MultiPartBlockStateBuilder builder);
    }
}
