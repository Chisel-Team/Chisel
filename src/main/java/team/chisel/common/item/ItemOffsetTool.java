package team.chisel.common.item;

import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import static net.minecraft.util.EnumFacing.*;

import lombok.ToString;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.api.chunkdata.IOffsetData;
import team.chisel.common.init.ChiselTabs;
import team.chisel.common.util.NBTSaveable;
import team.chisel.common.util.PerChunkData;
import team.chisel.common.util.PerChunkData.ChunkDataBase;

public class ItemOffsetTool extends Item {
    
    @ToString
    public static class OffsetData implements NBTSaveable, IOffsetData {

        private @Nonnull BlockPos offset = BlockPos.ORIGIN;

        @Override
        public void write(NBTTagCompound tag) {
            tag.setShort("offset", (short) (offset.getX() << 8 | offset.getY() << 4 | offset.getZ()));
        }

        @Override
        public void read(NBTTagCompound tag) {
            short data = tag.getShort("offset");
            offset = new BlockPos((data >> 8) & 0xF, (data >> 4) & 0xF, data & 0xF);
        }

        void move(@Nonnull EnumFacing dir) {
            offset = wrap(offset.offset(dir.getOpposite()));
        }

        @Override
        public @Nonnull BlockPos getOffset() {
            return offset;
        }

        private int positiveModulo(int num, int denom) {
            return (num + denom) % denom;
        }
        
        private @Nonnull BlockPos wrap(@Nonnull BlockPos pos) {
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
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof ICarvable) {
            if (world.isRemote) {
                return canOffset(player, world, pos, hand, facing) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
            } else {
                ChunkDataBase<OffsetData> cd = PerChunkData.INSTANCE.getData(DATA_KEY);
                OffsetData data = cd.getDataForChunk(world.provider.getDimension(), world.getChunkFromBlockCoords(pos).getChunkCoordIntPair());
                data.move(getMoveDir(facing, hitX, hitY, hitZ));
                PerChunkData.INSTANCE.chunkModified(world.getChunkFromBlockCoords(pos), DATA_KEY);
            }
        }
        return super.onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    public EnumFacing getMoveDir(EnumFacing face, double xCoord, double yCoord, double zCoord) {
        Map<Double, EnumFacing> map = Maps.newHashMap();
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

    private void fillMap(Map<Double, EnumFacing> map, double x, double y, EnumFacing... dirs) {
        map.put(Line2D.ptLineDistSq(0, 0, 1, 0, x, y), dirs[0]);
        map.put(Line2D.ptLineDistSq(0, 1, 1, 1, x, y), dirs[1]);
        map.put(Line2D.ptLineDistSq(0, 0, 0, 1, x, y), dirs[2]);
        map.put(Line2D.ptLineDistSq(1, 0, 1, 1, x, y), dirs[3]);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        RayTraceResult mop = event.getTarget();
        EntityPlayer player = event.getPlayer();

        if (mop.typeOfHit == Type.BLOCK && (canOffset(player, player.world, mop.getBlockPos(), EnumHand.MAIN_HAND, mop.sideHit) || canOffset(player, player.world, mop.getBlockPos(), EnumHand.OFF_HAND, mop.sideHit))) {

            EnumFacing face = mop.sideHit;
            BlockPos pos = mop.getBlockPos();
            VertexBuffer buf = Tessellator.getInstance().getBuffer();
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

            EnumFacing moveDir = getMoveDir(face, hit.xCoord - pos.getX(), hit.yCoord - pos.getY(), hit.zCoord - pos.getZ());
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

    private boolean canOffset(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        if (player.getHeldItem(hand) != null && player.getHeldItem(hand).getItem() == this && state.getBlock() instanceof ICarvable) {
            ICarvable carvable = (ICarvable) state.getBlock();
//            IVariationInfo info = carvable.getManager(player.worldObj.getBlockMetadata(x, y, z));
//            if (info.getManager() instanceof IOffsetRendered) {
//                return ((IOffsetRendered) info.getManager()).canOffset(world, x, y, z, side);
//            } else if (carvable instanceof IOffsetRendered) {
//                return ((IOffsetRendered) carvable).canOffset(world, x, y, z, side);
//            }
//            return validTypes.contains(info.getType());
            return true;
        }
        return false;
    }
}
