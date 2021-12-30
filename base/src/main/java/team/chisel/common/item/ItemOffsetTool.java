package team.chisel.common.item;

import static net.minecraft.util.Direction.DOWN;
import staticnet.minecraft.core.Directionn.EAST;
import static net.minecraft.util.Direction.NORTH;
import staticnet.minecraft.core.Directionn.SOUTH;
import static net.minecraft.util.Direction.UP;
import staticnet.minecraft.core.Directionn.WEST;

import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;

import lombok.ToString;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import com.mojang.math.Matrix4f;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
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
        public void write(CompoundTag tag) {
            tag.putShort("offset", (short) (offset.getX() << 8 | offset.getY() << 4 | offset.getZ()));
        }

        @Override
        public void read(CompoundTag tag) {
            short data = tag.getShort("offset");
            offset = new BlockPos((data >> 8) & 0xF, (data >> 4) & 0xF, data & 0xF);
        }

        void move(Direction dir) {
            offset = wrap(offset.relative(dir.getOpposite()));
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
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockState state = world.getBlockState(context.getClickedPos());
        if (world.isClientSide) {
            return canOffset(context.getLevel(), context.getClickedPos(), context.getItemInHand()) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else {
            ChunkDataBase<OffsetData> cd = PerChunkData.INSTANCE.getData(DATA_KEY);
            OffsetData data = cd.getDataForChunk(world.dimension(), world.getChunk(context.getClickedPos()).getPos());
            Vec3 hitVec = context.getClickLocation();
            data.move(getMoveDir(context.getClickedFace(), hitVec));
            PerChunkData.INSTANCE.chunkModified((LevelChunk) world.getChunk(context.getClickedPos()), DATA_KEY);
        }
        return super.useOn(context);
    }

    public Direction getMoveDir(Direction face, Vec3 hitVec) {
        Map<Double, Direction> map = Maps.newHashMap();
        if (face.getStepX() != 0) {
            fillMap(map, hitVec.z, hitVec.y, DOWN, UP, NORTH, SOUTH);
        } else if (face.getStepY() != 0) {
            fillMap(map, hitVec.x, hitVec.z, NORTH, SOUTH, WEST, EAST);
        } else if (face.getStepZ() != 0) {
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
        BlockHitResult target = event.getTarget();
        Player player = Minecraft.getInstance().player;

        if (canOffset(player.level, target.getBlockPos(), player.getMainHandItem()) || canOffset(player.level, target.getBlockPos(), player.getOffhandItem())) {
            
            Direction face = target.getDirection();
            BlockPos pos = target.getBlockPos();
            PoseStack ms = event.getMatrix();
            ms.pushPose();

            RenderSystem.disableLighting();
            RenderSystem.disableTexture();
            RenderSystem.depthMask(false);

            // Draw the X
            VertexConsumer linesBuf = event.getBuffers().getBuffer(RenderType.lines());

            float x = Math.max(0, face.getStepX());
            float y = Math.max(0, face.getStepY());
            float z = Math.max(0, face.getStepZ());
            
            Vec3 viewport = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

            ms.translate(-viewport.x, -viewport.y, -viewport.z);
            ms.translate(pos.getX(), pos.getY(), pos.getZ());
            Matrix4f mat = ms.last().pose();

            if (face.getStepX() != 0) {
            	linesBuf.vertex(mat, x, 0, 0).color(0, 0, 0, 1f).endVertex();
                linesBuf.vertex(mat, x, 1, 1).color(0, 0, 0, 1f).endVertex();
                linesBuf.vertex(mat, x, 1, 0).color(0, 0, 0, 1f).endVertex();
                linesBuf.vertex(mat, x, 0, 1).color(0, 0, 0, 1f).endVertex();
            } else if (face.getStepY() != 0) {
            	linesBuf.vertex(mat, 0, y, 0).color(0, 0, 0, 1f).endVertex();
            	linesBuf.vertex(mat, 1, y, 1).color(0, 0, 0, 1f).endVertex();
            	linesBuf.vertex(mat, 1, y, 0).color(0, 0, 0, 1f).endVertex();
            	linesBuf.vertex(mat, 0, y, 1).color(0, 0, 0, 1f).endVertex();
            } else {
            	linesBuf.vertex(mat, 0, 0, z).color(0, 0, 0, 1f).endVertex();
            	linesBuf.vertex(mat, 1, 1, z).color(0, 0, 0, 1f).endVertex();
            	linesBuf.vertex(mat, 1, 0, z).color(0, 0, 0, 1f).endVertex();
            	linesBuf.vertex(mat, 0, 1, z).color(0, 0, 0, 1f).endVertex();
            }

            Vec3 hit = target.getLocation();

            // Draw the triangle highlight
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.enablePolygonOffset();
            RenderSystem.polygonOffset(-1.0F, -10.0F);
            RenderSystem.disableCull();

            VertexConsumer buf = event.getBuffers().getBuffer(ClientUtil.OFFSET_OVERLAY);

            Direction moveDir = getMoveDir(face, hit.subtract(pos.getX(), pos.getY(), pos.getZ()));
            int clampedX = Math.max(0, moveDir.getStepX());
            int clampedY = Math.max(0, moveDir.getStepY());
            int clampedZ = Math.max(0, moveDir.getStepZ());
            boolean isX = moveDir.getStepX() != 0;
            boolean isY = moveDir.getStepY() != 0;
            boolean isZ = moveDir.getStepZ() != 0;
            float alpha = 0x55 / 255f;

            // Always draw the center point first, then draw the next two points.
            // Use either the move dir offset, or 0/1 if the move dir is not offset in this direction
            if (face.getStepX() != 0) {
                buf.vertex(mat, x, 0.5f, 0.5f).color(1, 1, 1, alpha).endVertex();
                buf.vertex(mat, x, isY ? clampedY : 0, isZ ? clampedZ : 0).color(1, 1, 1, alpha).endVertex();
                buf.vertex(mat, x, isY ? clampedY : 1, isZ ? clampedZ : 1).color(1, 1, 1, alpha).endVertex();
            } else if (face.getStepY() != 0) {
                buf.vertex(mat, 0.5f, y, 0.5f).color(1, 1, 1, alpha).endVertex();
                buf.vertex(mat, isX ? clampedX : 0, y, isZ ? clampedZ : 0).color(1, 1, 1, alpha).endVertex();
                buf.vertex(mat, isX ? clampedX : 1, y, isZ ? clampedZ : 1).color(1, 1, 1, alpha).endVertex();
            } else {
                buf.vertex(mat, 0.5f, 0.5f, z).color(1, 1, 1, alpha).endVertex();
                buf.vertex(mat, isX ? clampedX : 0, isY ? clampedY : 0, z).color(1, 1, 1, alpha).endVertex();
                buf.vertex(mat, isX ? clampedX : 1, isY ? clampedY : 1, z).color(1, 1, 1, alpha).endVertex();
            }

            ((MultiBufferSource.BufferSource)event.getBuffers()).endBatch(ClientUtil.OFFSET_OVERLAY);

            RenderSystem.disablePolygonOffset();
            RenderSystem.polygonOffset(0.0F, 0.0F);
            ms.popPose();
        }
    }

    private boolean canOffset(Level world, BlockPos pos, ItemStack stack) {
        BlockState state = world.getBlockState(pos);
        if (stack.isEmpty() || stack.getItem() != this) {
            return false;
        }
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(state);
//        if (!(model instanceof AbstractCTMBakedModel)) {
//            return false;
//        }
        return true;
    }
}
