package team.chisel.common.item;

import static net.minecraft.util.Direction.DOWN;
import static net.minecraft.util.Direction.EAST;
import static net.minecraft.util.Direction.NORTH;
import static net.minecraft.util.Direction.SOUTH;
import static net.minecraft.util.Direction.UP;
import static net.minecraft.util.Direction.WEST;

import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import lombok.ToString;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import team.chisel.api.chunkdata.IOffsetData;
import team.chisel.client.util.ClientUtil;
import team.chisel.common.util.NBTSaveable;
import team.chisel.common.util.PerChunkData;
import team.chisel.common.util.PerChunkData.ChunkDataBase;

@ParametersAreNonnullByDefault
public class ItemOffsetTool extends Item {
    
    @ToString
    public static class OffsetData implements NBTSaveable, IOffsetData {

        private BlockPos offset = BlockPos.ZERO;

        @Override
        public void write(CompoundNBT tag) {
            tag.putShort("offset", (short) (offset.getX() << 8 | offset.getY() << 4 | offset.getZ()));
        }

        @Override
        public void read(CompoundNBT tag) {
            short data = tag.getShort("offset");
            offset = new BlockPos((data >> 8) & 0xF, (data >> 4) & 0xF, data & 0xF);
        }

        void move(Direction dir) {
            offset = wrap(offset.offset(dir.getOpposite()));
        }

        @Override
        public @Nonnull BlockPos getOffset() {
            return offset;
        }

        private int positiveModulo(int num, int denom) {
            return (num + denom) % denom;
        }
        
        private BlockPos wrap(BlockPos pos) {
            return new BlockPos(positiveModulo(pos.getX(), 16), positiveModulo(pos.getY(), 16), positiveModulo(pos.getZ(), 16));
        }
    }

    public static final String DATA_KEY = "offsettool";
//    private static final List<TextureType> validTypes = Lists.newArrayList(TextureType.V4, TextureType.V9 /* SOON, TextureType.V16 */);
    

    public ItemOffsetTool(Item.Properties properties) {
        super(properties);
        PerChunkData.INSTANCE.registerChunkData(DATA_KEY, new ChunkDataBase<OffsetData>(OffsetData.class, true));
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(this::onBlockHighlight));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockState state = world.getBlockState(context.getPos());
        if (world.isRemote) {
            return canOffset(context.getWorld(), context.getPos(), context.getItem()) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        } else {
            ChunkDataBase<OffsetData> cd = PerChunkData.INSTANCE.getData(DATA_KEY);
            OffsetData data = cd.getDataForChunk(world.getDimensionKey(), world.getChunk(context.getPos()).getPos());
            Vector3d hitVec = context.getHitVec();
            data.move(getMoveDir(context.getFace(), hitVec));
            PerChunkData.INSTANCE.chunkModified((Chunk) world.getChunk(context.getPos()), DATA_KEY);
        }
        return super.onItemUse(context);
    }

    public Direction getMoveDir(Direction face, Vector3d hitVec) {
        Map<Double, Direction> map = Maps.newHashMap();
        if (face.getXOffset() != 0) {
            fillMap(map, hitVec.z, hitVec.y, DOWN, UP, NORTH, SOUTH);
        } else if (face.getYOffset() != 0) {
            fillMap(map, hitVec.x, hitVec.z, NORTH, SOUTH, WEST, EAST);
        } else if (face.getZOffset() != 0) {
            fillMap(map, hitVec.x, hitVec.y, DOWN, UP, WEST, EAST);
        }
        List<Double> keys = Lists.newArrayList(map.keySet());
        Collections.sort(keys);
        return map.get(keys.get(0));
    }

    private void fillMap(Map<Double, Direction> map, double x, double y, Direction... dirs) {
        map.put(Line2D.ptLineDistSq(0, 0, 1, 0, x, y), dirs[0]);
        map.put(Line2D.ptLineDistSq(0, 1, 1, 1, x, y), dirs[1]);
        map.put(Line2D.ptLineDistSq(0, 0, 0, 1, x, y), dirs[2]);
        map.put(Line2D.ptLineDistSq(1, 0, 1, 1, x, y), dirs[3]);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onBlockHighlight(DrawHighlightEvent.HighlightBlock event) {
        BlockRayTraceResult target = event.getTarget();
        PlayerEntity player = Minecraft.getInstance().player;

        if (canOffset(player.world, target.getPos(), player.getHeldItemMainhand()) || canOffset(player.world, target.getPos(), player.getHeldItemOffhand())) {
            
            Direction face = target.getFace();
            BlockPos pos = target.getPos();
            MatrixStack ms = event.getMatrix();
            ms.push();

            RenderSystem.disableLighting();
            RenderSystem.disableTexture();
            RenderSystem.depthMask(false);

            // Draw the X
            IVertexBuilder linesBuf = event.getBuffers().getBuffer(RenderType.getLines());

            float x = Math.max(0, face.getXOffset());
            float y = Math.max(0, face.getYOffset());
            float z = Math.max(0, face.getZOffset());
            
            Vector3d viewport = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();

            ms.translate(-viewport.x, -viewport.y, -viewport.z);
            ms.translate(pos.getX(), pos.getY(), pos.getZ());
            Matrix4f mat = ms.getLast().getMatrix();

            if (face.getXOffset() != 0) {
            	linesBuf.pos(mat, x, 0, 0).color(0, 0, 0, 1f).endVertex();
                linesBuf.pos(mat, x, 1, 1).color(0, 0, 0, 1f).endVertex();
                linesBuf.pos(mat, x, 1, 0).color(0, 0, 0, 1f).endVertex();
                linesBuf.pos(mat, x, 0, 1).color(0, 0, 0, 1f).endVertex();
            } else if (face.getYOffset() != 0) {
            	linesBuf.pos(mat, 0, y, 0).color(0, 0, 0, 1f).endVertex();
            	linesBuf.pos(mat, 1, y, 1).color(0, 0, 0, 1f).endVertex();
            	linesBuf.pos(mat, 1, y, 0).color(0, 0, 0, 1f).endVertex();
            	linesBuf.pos(mat, 0, y, 1).color(0, 0, 0, 1f).endVertex();
            } else {
            	linesBuf.pos(mat, 0, 0, z).color(0, 0, 0, 1f).endVertex();
            	linesBuf.pos(mat, 1, 1, z).color(0, 0, 0, 1f).endVertex();
            	linesBuf.pos(mat, 1, 0, z).color(0, 0, 0, 1f).endVertex();
            	linesBuf.pos(mat, 0, 1, z).color(0, 0, 0, 1f).endVertex();
            }

            Vector3d hit = target.getHitVec();

            // Draw the triangle highlight
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.enablePolygonOffset();
            RenderSystem.polygonOffset(-1.0F, -10.0F);
            RenderSystem.disableCull();

            IVertexBuilder buf = event.getBuffers().getBuffer(ClientUtil.OFFSET_OVERLAY);

            Direction moveDir = getMoveDir(face, hit.subtract(pos.getX(), pos.getY(), pos.getZ()));
            int clampedX = Math.max(0, moveDir.getXOffset());
            int clampedY = Math.max(0, moveDir.getYOffset());
            int clampedZ = Math.max(0, moveDir.getZOffset());
            boolean isX = moveDir.getXOffset() != 0;
            boolean isY = moveDir.getYOffset() != 0;
            boolean isZ = moveDir.getZOffset() != 0;
            float alpha = 0x55 / 255f;

            // Always draw the center point first, then draw the next two points.
            // Use either the move dir offset, or 0/1 if the move dir is not offset in this direction
            if (face.getXOffset() != 0) {
                buf.pos(mat, x, 0.5f, 0.5f).color(1, 1, 1, alpha).endVertex();
                buf.pos(mat, x, isY ? clampedY : 0, isZ ? clampedZ : 0).color(1, 1, 1, alpha).endVertex();
                buf.pos(mat, x, isY ? clampedY : 1, isZ ? clampedZ : 1).color(1, 1, 1, alpha).endVertex();
            } else if (face.getYOffset() != 0) {
                buf.pos(mat, 0.5f, y, 0.5f).color(1, 1, 1, alpha).endVertex();
                buf.pos(mat, isX ? clampedX : 0, y, isZ ? clampedZ : 0).color(1, 1, 1, alpha).endVertex();
                buf.pos(mat, isX ? clampedX : 1, y, isZ ? clampedZ : 1).color(1, 1, 1, alpha).endVertex();
            } else {
                buf.pos(mat, 0.5f, 0.5f, z).color(1, 1, 1, alpha).endVertex();
                buf.pos(mat, isX ? clampedX : 0, isY ? clampedY : 0, z).color(1, 1, 1, alpha).endVertex();
                buf.pos(mat, isX ? clampedX : 1, isY ? clampedY : 1, z).color(1, 1, 1, alpha).endVertex();
            }

            ((IRenderTypeBuffer.Impl)event.getBuffers()).finish(ClientUtil.OFFSET_OVERLAY);

            RenderSystem.disablePolygonOffset();
            RenderSystem.polygonOffset(0.0F, 0.0F);
            ms.pop();
        }
    }

    private boolean canOffset(World world, BlockPos pos, ItemStack stack) {
        BlockState state = world.getBlockState(pos);
        if (stack.isEmpty() || stack.getItem() != this) {
            return false;
        }
        IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(state);
//        if (!(model instanceof AbstractCTMBakedModel)) {
//            return false;
//        }
        return true;
    }
}
