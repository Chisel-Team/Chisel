package team.chisel.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.util.NonnullType;
import team.chisel.ctm.client.util.RegionCache;

@ParametersAreNonnullByDefault
public class ChiselModeGeometryCache implements IWorldEventListener {
    
    private IChiselMode mode;
    private BlockPos origin;
    private EnumFacing side;
    
    private List<BlockPos> candidateCache = new ArrayList<>();
    private AxisAlignedBB candidateBounds = new AxisAlignedBB(BlockPos.ORIGIN);
    private TDoubleList geometryCache = new TDoubleArrayList();
    
    public ChiselModeGeometryCache(IChiselMode mode, BlockPos origin, EnumFacing side) {
        this.mode = mode;
        this.origin = origin;
        this.side = side;
        updateCache();
    }
    
    private void updateCache() {
        IBlockState state = Minecraft.getMinecraft().world.getBlockState(origin);
        this.candidateCache = Lists.newArrayList(mode.getCandidates(Minecraft.getMinecraft().player, origin, side)).stream().filter(pos -> Minecraft.getMinecraft().world.getBlockState(pos) == state).collect(Collectors.toList());
        this.candidateBounds = mode.getBounds(side).offset(origin);
        draw(Tessellator.getInstance().getBuffer(), state);
    }
    
    private void draw(VertexBuffer buf, IBlockState state) {
//        Pair<ResourceLocation, ResourceLocation> overlay = mode.getOverlayTex();
//        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
//        TextureInfo info = new TextureInfo(new TextureAtlasSprite[] {map.getAtlasSprite(overlay.getLeft().toString()), map.getAtlasSprite(overlay.getRight().toString())}, Optional.empty(), BlockRenderLayer.TRANSLUCENT);
//        ICTMTexture<?> tex = CTM_TYPE.makeTexture(info);

        geometryCache.clear();
        RegionCache world = new RegionCache(origin, 20, Minecraft.getMinecraft().world);
        for (BlockPos pos : getCandidates()) {
            AxisAlignedBB bb = state.getSelectedBoundingBox(Minecraft.getMinecraft().world, pos);
            drawCulledBox(buf, bb, world, pos);
            // UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.POSITION_COLOR);
            // float[] vpos = new float[]{ (float) bb.minX, (float) bb.minY, (float) bb.maxZ, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ,
            // (float) bb.minX, (float) bb.maxY, (float) bb.maxZ };
            // TextureAtlasSprite s = info.getSprites()[0];
            // float[] uvs = new float[]{ s.getMinU(), s.getMaxV(), s.getMaxU(), s.getMaxV(), s.getMaxU(), s.getMinV(), s.getMinU(), s.getMinV() };
            // for (int i = 0; i < 4; i++) {
            // builder.put(0, vpos[i * 3], vpos[(i * 3) + 1], vpos[(i * 3) + 2]);
            //// builder.put(1, uvs[i * 2], uvs[(i * 2) + 1]);
            // builder.put(1, 1, 1, 1, 1);
            // }
            // builder.setTexture(s);
            //// List<BakedQuad> quads = tex.transformQuad(builder.build(), null, 1);
            //// for (BakedQuad q : quads) {
            // buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            // buf.addVertexData(builder.build().getVertexData());
            // Tessellator.getInstance().draw();
            //// }
        }
    }

    private void drawCulledBox(VertexBuffer buf, AxisAlignedBB bb, IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.shouldSideBeRendered(world, pos, EnumFacing.DOWN)) {
            pos(bb.maxX, bb.minY, bb.minZ);
            pos(bb.maxX, bb.minY, bb.maxZ);
            pos(bb.minX, bb.minY, bb.maxZ);
            pos(bb.minX, bb.minY, bb.minZ);
        }
        if (state.shouldSideBeRendered(world, pos, EnumFacing.UP)) {
            pos(bb.minX, bb.maxY, bb.maxZ);
            pos(bb.maxX, bb.maxY, bb.maxZ);
            pos(bb.maxX, bb.maxY, bb.minZ);
            pos(bb.minX, bb.maxY, bb.minZ);
        }
        if (state.shouldSideBeRendered(world, pos, EnumFacing.NORTH)) {
            pos(bb.minX, bb.maxY, bb.minZ);
            pos(bb.maxX, bb.maxY, bb.minZ);
            pos(bb.maxX, bb.minY, bb.minZ);
            pos(bb.minX, bb.minY, bb.minZ);
        }
        if (state.shouldSideBeRendered(world, pos, EnumFacing.SOUTH)) {
            pos(bb.minX, bb.minY, bb.maxZ);
            pos(bb.maxX, bb.minY, bb.maxZ);
            pos(bb.maxX, bb.maxY, bb.maxZ);
            pos(bb.minX, bb.maxY, bb.maxZ);
        }
        if (state.shouldSideBeRendered(world, pos, EnumFacing.WEST)) {
            pos(bb.minX, bb.minY, bb.minZ);
            pos(bb.minX, bb.minY, bb.maxZ);
            pos(bb.minX, bb.maxY, bb.maxZ);
            pos(bb.minX, bb.maxY, bb.minZ);
        }
        if (state.shouldSideBeRendered(world, pos, EnumFacing.EAST)) {
            pos(bb.maxX, bb.maxY, bb.minZ);
            pos(bb.maxX, bb.maxY, bb.maxZ);
            pos(bb.maxX, bb.minY, bb.maxZ);
            pos(bb.maxX, bb.minY, bb.minZ);
        }
    }
    
    private void pos(double x, double y, double z) {
        geometryCache.add(x);
        geometryCache.add(y);
        geometryCache.add(z);
    }
    
    public void setMode(IChiselMode mode) {
        if (mode != this.mode) {
            this.mode = mode;
            updateCache();
        }
    }
    
    public void setOrigin(BlockPos origin) {
        if (!origin.equals(this.origin)) {
            this.origin = origin;
            updateCache();
        }
    }
    
    public void setSide(EnumFacing side) {
        if (side != this.side) {
            this.side = side;
            updateCache();
        }
    }
    
    public Iterable<@NonnullType ? extends BlockPos> getCandidates() {
        return candidateCache;
    }
    
    @Override
    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
        checkRedraw(new AxisAlignedBB(pos));
    }

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
        checkRedraw(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
    }
    
    private void checkRedraw(AxisAlignedBB updateRange) {
        if (updateRange.intersectsWith(candidateBounds)) {
            Minecraft.getMinecraft().addScheduledTask(this::updateCache);
        }
    }
    
    public void draw() {
        Timer timer = ClientUtil.getTimer();
        float c = 1;
        float a = 0.2f;
        if (timer != null) {
            c = Math.round(( ((float) Math.sin((Minecraft.getMinecraft().world.getTotalWorldTime() + timer.renderPartialTicks) / 5)) / 2) + 0.5f);
            a = Math.abs(2 * (float) Math.sin(((Minecraft.getMinecraft().world.getTotalWorldTime() + timer.renderPartialTicks) / 5))) * 0.1f;
        }
        
        GlStateManager.pushMatrix();
        VertexBuffer buf = Tessellator.getInstance().getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < geometryCache.size(); i += 3) {
            buf.pos(geometryCache.get(i), geometryCache.get(i + 1), geometryCache.get(i + 2)).color(c, c, c, a).endVertex();
        }
        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();
    }

    /* == Dummy Impls == */

    @Override
    public void notifyLightSet(BlockPos pos) {}

    @Override
    public void playSoundToAllNearExcept(@Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch) {}

    @Override
    public void playRecord(SoundEvent soundIn, BlockPos pos) {}

    @Override
    public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {}

    @Override
    public void onEntityAdded(Entity entityIn) {}

    @Override
    public void onEntityRemoved(Entity entityIn) {}

    @Override
    public void broadcastSound(int soundID, BlockPos pos, int data) {}

    @Override
    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {}

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {}

}
