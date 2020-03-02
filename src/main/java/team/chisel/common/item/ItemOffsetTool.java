package team.chisel.common.item;

import static net.minecraft.util.Direction.*;

import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

import lombok.ToString;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import team.chisel.api.block.ICarvable;
import team.chisel.api.chunkdata.IOffsetData;
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
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockState state = world.getBlockState(context.getPos());
        if (state.getBlock() instanceof ICarvable) {
            if (world.isRemote) {
                return canOffset(context.getWorld(), context.getPos(), context.getItem()) ? ActionResultType.SUCCESS : ActionResultType.PASS;
            } else {
                ChunkDataBase<OffsetData> cd = PerChunkData.INSTANCE.getData(DATA_KEY);
                OffsetData data = cd.getDataForChunk(world.getDimension().getType(), world.getChunk(context.getPos()).getPos());
                Vec3d hitVec = context.getHitVec();
                data.move(getMoveDir(context.getFace(), hitVec));
                PerChunkData.INSTANCE.chunkModified((Chunk) world.getChunk(context.getPos()), DATA_KEY);
            }
        }
        return super.onItemUse(context);
    }

    public Direction getMoveDir(Direction face, Vec3d hitVec) {
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
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        if (event.getTarget().getType() != Type.BLOCK) return;
        BlockRayTraceResult target = (BlockRayTraceResult) event.getTarget();
        PlayerEntity player = Minecraft.getInstance().player;

        if (canOffset(player.world, target.getPos(), player.getHeldItemMainhand()) || canOffset(player.world, target.getPos(), player.getHeldItemOffhand())) {
            
            Direction face = target.getFace();
            BlockPos pos = target.getPos();
            BufferBuilder buf = Tessellator.getInstance().getBuffer();
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.disableTexture();
            GlStateManager.depthMask(false);

            // Draw the X
            buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

            double x = Math.max(0, face.getXOffset());
            double y = Math.max(0, face.getYOffset());
            double z = Math.max(0, face.getZOffset());

            GlStateManager.translated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);
            GlStateManager.translated(pos.getX(), pos.getY(), pos.getZ());
            GlStateManager.color3f(0, 0, 0);

            if (face.getXOffset() != 0) {
                buf.pos(x, 0, 0).endVertex();
                buf.pos(x, 1, 1).endVertex();
                buf.pos(x, 1, 0).endVertex();
                buf.pos(x, 0, 1).endVertex();
            } else if (face.getYOffset() != 0) {
                buf.pos(0, y, 0).endVertex();
                buf.pos(1, y, 1).endVertex();
                buf.pos(1, y, 0).endVertex();
                buf.pos(0, y, 1).endVertex();
            } else {
                buf.pos(0, 0, z).endVertex();
                buf.pos(1, 1, z).endVertex();
                buf.pos(1, 0, z).endVertex();
                buf.pos(0, 1, z).endVertex();
            }

            Tessellator.getInstance().draw();

            Vec3d hit = target.getHitVec();

            // Draw the triangle highlight
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enablePolygonOffset();
            GlStateManager.polygonOffset(-1.0F, -10.0F);
            GlStateManager.disableCull();

            buf.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
            
            GlStateManager.color4f(1, 1, 1, 0x55 / 255f);

            Direction moveDir = getMoveDir(face, hit.subtract(pos.getX(), pos.getY(), pos.getZ()));
            int clampedX = Math.max(0, moveDir.getXOffset());
            int clampedY = Math.max(0, moveDir.getYOffset());
            int clampedZ = Math.max(0, moveDir.getZOffset());
            boolean isX = moveDir.getXOffset() != 0;
            boolean isY = moveDir.getYOffset() != 0;
            boolean isZ = moveDir.getZOffset() != 0;

            // Always draw the center point first, then draw the next two points.
            // Use either the move dir offset, or 0/1 if the move dir is not offset in this direction
            if (face.getXOffset() != 0) {
                buf.pos(x, 0.5, 0.5).endVertex();
                buf.pos(x, isY ? clampedY : 0, isZ ? clampedZ : 0).endVertex();
                buf.pos(x, isY ? clampedY : 1, isZ ? clampedZ : 1).endVertex();
            } else if (face.getYOffset() != 0) {
                buf.pos(0.5, y, 0.5).endVertex();
                buf.pos(isX ? clampedX : 0, y, isZ ? clampedZ : 0).endVertex();
                buf.pos(isX ? clampedX : 1, y, isZ ? clampedZ : 1).endVertex();
            } else {
                buf.pos(0.5, 0.5, z).endVertex();
                buf.pos(isX ? clampedX : 0, isY ? clampedY : 0, z).endVertex();
                buf.pos(isX ? clampedX : 1, isY ? clampedY : 1, z).endVertex();
            }
            Tessellator.getInstance().draw();
            
            GlStateManager.disablePolygonOffset();
            GlStateManager.polygonOffset(0.0F, 0.0F);
            GlStateManager.popMatrix();
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
