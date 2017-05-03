package team.chisel.client.render;

import lombok.SneakyThrows;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.client.ChiselExtendedState;
import team.chisel.client.util.MetadataWrapper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * @author WireSegal
 *         Created at 5:40 PM on 5/2/17.
 */
@SideOnly(Side.CLIENT)
public class GlowRenderer {
    private static final MethodHandle renderModel;

    static {
        Method m = ReflectionHelper.findMethod(RenderItem.class, null, new String[] { "renderModel", "func_175045_a", "a" }, IBakedModel.class, ItemStack.class);
        try {
            renderModel = MethodHandles.publicLookup().unreflect(m);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize Chisel MethodHandle for renderModel!");
        }
    }

    @SneakyThrows
    private static void renderModel(IBakedModel model, ItemStack stack) {
        renderModel.invokeExact(Minecraft.getMinecraft().getRenderItem(), model, stack);
    }

    private static final ThreadLocal<MetadataWrapper> wrapper = ThreadLocal.withInitial(MetadataWrapper::new);

    private static IBakedModel createNewModel(IBakedModel model) {
        return wrapper.get().withParent(model);
    }

    public static void glow(ItemStack stack, IBakedModel model) {
        IBakedModel newModel = createNewModel(model);
        float x = OpenGlHelper.lastBrightnessX;
        float y = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        if (!model.isGui3d()) GlStateManager.disableLighting();
        renderModel(newModel, stack);
        if (!model.isGui3d()) GlStateManager.enableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, x, y);
    }

    public static boolean glow(BlockModelRenderer blockModelRenderer, IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, VertexBuffer buf) {
        IBakedModel newModel = createNewModel(model);

        boolean prev = ForgeModContainer.forgeLightPipelineEnabled;
        ForgeModContainer.forgeLightPipelineEnabled = false;
        IBlockState wrapped = new ChiselExtendedState(state, world, pos) {
            @Override
            public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos) {
                return 0xF000F0;
            }
        };

        boolean ret = blockModelRenderer.renderModel(world, newModel, wrapped, pos, buf, true);
        ForgeModContainer.forgeLightPipelineEnabled = prev;
        return ret;
    }
}
