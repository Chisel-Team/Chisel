package team.chisel.client.render;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.base.Throwables;

import lombok.SneakyThrows;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.api.render.IModelChisel;
import team.chisel.client.ClientUtil;
import team.chisel.client.render.texture.MetadataSectionChisel;
import team.chisel.common.Reference;

/**
 * Chisel's model registry
 */
public enum ChiselModelRegistry implements Reference {
    
    INSTANCE;

    public <T extends Block & ICarvable> void register(@Nonnull T block) {
        ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
            
            @Override
            protected @Nonnull ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                VariationData data = block.getVariationData(block.getVariationIndex(state));
                String name = block.getRegistryName().getResourcePath();
                while (Character.isDigit(name.charAt(name.length() - 1))) {
                    name = name.substring(0, name.length() - 1);
                }
                return new ModelResourceLocation(new ResourceLocation("chisel", "default"), data.path);
            }
        });
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block), new ItemMeshDefinition() {
            
            @Override
            @SuppressWarnings("unchecked")
            public @Nonnull ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
                T block = (T) Block.getBlockFromItem(stack.getItem());
                VariationData data = block.getVariationData(stack.getItemDamage());
                String name = block.getRegistryName().getResourcePath();
                while (Character.isDigit(name.charAt(name.length() - 1))) {
                    name = name.substring(0, name.length() - 1);
                }
                return new ModelResourceLocation(new ResourceLocation("chisel", "default"), data.path);
            }
        });
        // Nope
        ModelLoader.registerItemVariants(Item.getItemFromBlock(block));
    }
    
    private static final Class<?> multipartModelClass;
    static {
        try {
            multipartModelClass = Class.forName("net.minecraftforge.client.model.ModelLoader$MultipartModel");
        } catch (ClassNotFoundException e) {
            throw Throwables.propagate(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    @SubscribeEvent(priority = EventPriority.LOWEST) // low priority to capture all event-registered models
    @SneakyThrows
    public void onModelBake(ModelBakeEvent event) {
        Map<ModelResourceLocation, IModel> stateModels = ReflectionHelper.getPrivateValue(ModelLoader.class, event.getModelLoader(), "stateModels");
        for (ModelResourceLocation mrl : event.getModelRegistry().getKeys()) {
            IModel model = stateModels.get(mrl);
            if (model != null && !(model instanceof IModelChisel) && Collections.disjoint(model.getDependencies(), ModelLoaderChisel.parsedLocations)) {
                Collection<ResourceLocation> textures = model.getTextures();
                // FORGE WHY
                if (multipartModelClass.isAssignableFrom(model.getClass())) {
                    Field _partModels = multipartModelClass.getDeclaredField("partModels");
                    _partModels.setAccessible(true);
                    Map<?, IModel> partModels = (Map<?, IModel>) _partModels.get(model);
                    textures = partModels.values().stream().map(m -> m.getTextures()).flatMap(Collection::stream).collect(Collectors.toList());
                }
                for (ResourceLocation tex : textures) {
                    MetadataSectionChisel meta = null;
                    try {
                        meta = ClientUtil.getMetadata(ClientUtil.spriteToAbsolute(tex));
                    } catch (IOException e) {} // Fallthrough
                    if (meta != null) {
                        event.getModelRegistry().putObject(mrl, wrap(model, event.getModelRegistry().getObject(mrl)));
                        break;
                    }
                }
            }
        }
    }

    private @Nonnull IBakedModel wrap(IModel model, IBakedModel object) {
        ModelChisel modelchisel = new ModelChisel(null, model, Collections.emptyMap());
        modelchisel.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, rl -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(rl.toString()));
        modelchisel.setBakedparent(object);
        return new ModelChiselBlock(modelchisel);
    }
}
