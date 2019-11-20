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

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.ToString;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.api.block.ICarvable;
import team.chisel.api.chunkdata.IOffsetData;
import team.chisel.common.init.ChiselTabs;
import team.chisel.common.util.NBTSaveable;
import team.chisel.common.util.PerChunkData;
import team.chisel.common.util.PerChunkData.ChunkDataBase;
import team.chisel.ctm.client.model.AbstractCTMBakedModel;

@ParametersAreNonnullByDefault
public class ItemOffsetTool extends Item {
    
    @ToString
    public static class OffsetData implements NBTSaveable, IOffsetData {

        private BlockPos offset = BlockPos.ORIGIN;

        @Override
        public void write(CompoundNBT tag) {
            tag.setShort("offset", (short) (offset.getX() << 8 | offset.getY() << 4 | offset.getZ()));
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
    

    public ItemOffsetTool() {
        super();
        setCreativeTab(ChiselTabs.tab);
        setUnlocalizedName("chisel.offsettool");
        setRegistryName("offsettool");
        setFull3D();
        PerChunkData.INSTANCE.registerChunkData(DATA_KEY, new ChunkDataBase<OffsetData>(OffsetData.class, true));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public EnumActionResult onItemUse(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof ICarvable) {
            if (world.isRemote) {
                return canOffset(player, world, pos, hand, facing) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
            } else {
                ChunkDataBase<OffsetData> cd = PerChunkData.INSTANCE.getData(DATA_KEY);
                OffsetData data = cd.getDataForChunk(world.provider.getDimension(), world.getChunkFromBlockCoords(pos).getPos());
                data.move(getMoveDir(facing, hitX, hitY, hitZ));
                PerChunkData.INSTANCE.chunkModified(world.getChunkFromBlockCoords(pos), DATA_KEY);
            }
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    public Direction getMoveDir(Direction face, double xCoord, double yCoord, double zCoord) {
        Map<Double, Direction> map = Maps.newHashMap();
        if (face.getFrontOffsetX() != 0) {
            fillMap(map, zCoord, yCoord, DOWN, UP, NORTH, SOUTH);
        } else if (face.getFrontOffsetY() != 0) {
            fillMap(map, xCoord, zCoord, NORTH, SOUTH, WEST, EAST);
        } else if (face.getFrontOffsetZ() != 0) {
            fillMap(map, xCoord, yCoord, DOWN, UP, WEST, EAST);
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
    @SideOnly(Side.CLIENT)
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        RayTraceResult mop = event.getTarget();
        PlayerEntity player = event.getPlayer();

        if (mop.typeOfHit == Type.BLOCK && (canOffset(player, player.world, mop.getBlockPos(), Hand.MAIN_HAND, mop.sideHit) || canOffset(player, player.world, mop.getBlockPos(), Hand.OFF_HAND, mop.sideHit))) {

            Direction face = mop.sideHit;
            BlockPos pos = mop.getBlockPos();
            BufferBuilder buf = Tessellator.getInstance().getBuffer();
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);

            // Draw the X
            buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

            double px = -(player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks());
            double py = -(player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks());
            double pz = -(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks());
            double x = Math.max(0, face.getFrontOffsetX()) + (0.01 * face.getFrontOffsetX());
            double y = Math.max(0, face.getFrontOffsetY()) + (0.01 * face.getFrontOffsetY());
            double z = Math.max(0, face.getFrontOffsetZ()) + (0.01 * face.getFrontOffsetZ());

            GlStateManager.translate(px, py, pz);
            GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
            GlStateManager.color(0, 0, 0);

            if (face.getFrontOffsetX() != 0) {
                buf.pos(x, 0, 0).endVertex();
                buf.pos(x, 1, 1).endVertex();
                buf.pos(x, 1, 0).endVertex();
                buf.pos(x, 0, 1).endVertex();
            } else if (face.getFrontOffsetY() != 0) {
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

            Vec3d hit = mop.hitVec;

            // Draw the triangle highlight
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableCull();

            buf.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
            
            GlStateManager.color(1, 1, 1, 0x55 / 255f);

            Direction moveDir = getMoveDir(face, hit.x - pos.getX(), hit.y - pos.getY(), hit.z - pos.getZ());
            int clampedX = Math.max(0, moveDir.getFrontOffsetX());
            int clampedY = Math.max(0, moveDir.getFrontOffsetY());
            int clampedZ = Math.max(0, moveDir.getFrontOffsetZ());
            boolean isX = moveDir.getFrontOffsetX() != 0;
            boolean isY = moveDir.getFrontOffsetY() != 0;
            boolean isZ = moveDir.getFrontOffsetZ() != 0;

            // Always draw the center point first, then draw the next two points.
            // Use either the move dir offset, or 0/1 if the move dir is not offset in this direction
            if (face.getFrontOffsetX() != 0) {
                buf.pos(x, 0.5, 0.5).endVertex();
                buf.pos(x, isY ? clampedY : 0, isZ ? clampedZ : 0).endVertex();
                buf.pos(x, isY ? clampedY : 1, isZ ? clampedZ : 1).endVertex();
            } else if (face.getFrontOffsetY() != 0) {
                buf.pos(0.5, y, 0.5).endVertex();
                buf.pos(isX ? clampedX : 0, y, isZ ? clampedZ : 0).endVertex();
                buf.pos(isX ? clampedX : 1, y, isZ ? clampedZ : 1).endVertex();
            } else {
                buf.pos(0.5, 0.5, z).endVertex();
                buf.pos(isX ? clampedX : 0, isY ? clampedY : 0, z).endVertex();
                buf.pos(isX ? clampedX : 1, isY ? clampedY : 1, z).endVertex();
            }
            Tessellator.getInstance().draw();
            
            GlStateManager.popMatrix();
        }
    }

    private boolean canOffset(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction side) {
        BlockState state = world.getBlockState(pos);
        if (player.getHeldItem(hand).isEmpty() || player.getHeldItem(hand).getItem() != this) {
            return false;
        }
        IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
        if (!(model instanceof AbstractCTMBakedModel)) {
            return false;
        }
        return true;
    }
}
