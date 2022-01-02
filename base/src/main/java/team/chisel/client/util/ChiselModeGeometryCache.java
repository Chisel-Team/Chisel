package team.chisel.client.util;

import javax.annotation.ParametersAreNonnullByDefault;

// ...TODO

public class ChiselModeGeometryCache {// implements IWorldEventListener {
//    private IChiselMode mode;
//    private BlockPos origin;
//    private Direction side;
//
//    private long[] cacheState = {};
//
//    private List<BlockPos> candidateCache = new ArrayList<>();
//    private AxisAlignedBB candidateBounds = new AxisAlignedBB(BlockPos.ZERO);
//    private DoubleList geometryCache = new DoubleArrayList();
//
//    public ChiselModeGeometryCache(IChiselMode mode, BlockPos origin, Direction side) {
//        this.mode = mode;
//        this.origin = origin;
//        this.side = side;
//        updateCache();
//    }
//
//    public void setMode(IChiselMode mode) {
//        if (this.mode != mode) {
//            this.mode = mode;
//            updateCache();
//        }
//    }
//
//    public void setOrigin(BlockPos origin) {
//        if (!this.origin.equals(origin)) {
//            this.origin = origin;
//            if (checkDirty()) {
//                updateCache();
//            }
//        }
//    }
//
//    public void setSide(Direction side) {
//        if (this.side != side) {
//            this.side = side;
//            if (checkDirty()) {
//                updateCache();
//            }
//        }
//    }
//
//    public int size() {
//        return candidateCache.size();
//    }
//
//    protected boolean checkDirty() {
//        return !Arrays.equals(cacheState, mode.getCacheState(origin, side));
//    }
//
//    protected void updateCache() {
//        BlockState state = Minecraft.getInstance().world.getBlockState(origin);
//        ICarvingGroup group = CarvingUtils.getChiselRegistry().getGroup(state);
//        if (group != null) {
//            this.candidateCache = Lists.newArrayList(mode.getCandidates(Minecraft.getInstance().player, origin, side));
//            this.candidateBounds = mode.getBounds(side).offset(origin);
//            this.cacheState = mode.getCacheState(origin, side);
//        } else {
//            this.candidateCache.clear();
//            this.candidateBounds = Block.FULL_BLOCK_AABB.offset(origin);
//            this.cacheState = new long[0];
//        }
//        draw(state);
//    }
//
//    private void draw(BlockState state) {
////        Pair<ResourceLocation, ResourceLocation> overlay = mode.getOverlayTex();
////        TextureMap map = Minecraft.getInstance().getTextureMapBlocks();
////        TextureInfo info = new TextureInfo(new TextureAtlasSprite[] {map.getAtlasSprite(overlay.getLeft().toString()), map.getAtlasSprite(overlay.getRight().toString())}, Optional.empty(), BlockRenderLayer.TRANSLUCENT);
////        ICTMTexture<?> tex = CTM_TYPE.makeTexture(info);
//
//        geometryCache.clear();
//        RegionCache world = new RegionCache(origin, 20, Minecraft.getInstance().world);
//        for (BlockPos pos : getCandidates()) {
//            AxisAlignedBB bb = state.getSelectedBoundingBox(Minecraft.getInstance().world, pos);
//            drawCulledBox(bb, world, pos);
//            // UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.POSITION_COLOR);
//            // float[] vpos = new float[]{ (float) bb.minX, (float) bb.minY, (float) bb.maxZ, (float) bb.maxX, (float) bb.minY, (float) bb.maxZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ,
//            // (float) bb.minX, (float) bb.maxY, (float) bb.maxZ };
//            // TextureAtlasSprite s = info.getSprites()[0];
//            // float[] uvs = new float[]{ s.getMinU(), s.getMaxV(), s.getMaxU(), s.getMaxV(), s.getMaxU(), s.getMinV(), s.getMinU(), s.getMinV() };
//            // for (int i = 0; i < 4; i++) {
//            // builder.put(0, vpos[i * 3], vpos[(i * 3) + 1], vpos[(i * 3) + 2]);
//            //// builder.put(1, uvs[i * 2], uvs[(i * 2) + 1]);
//            // builder.put(1, 1, 1, 1, 1);
//            // }
//            // builder.setTexture(s);
//            //// List<BakedQuad> quads = tex.transformQuad(builder.build(), null, 1);
//            //// for (BakedQuad q : quads) {
//            // buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
//            // buf.addVertexData(builder.build().getVertexData());
//            // Tessellator.getInstance().draw();
//            //// }
//        }
//    }
//
////    private void drawCulledBox(AxisAlignedBB bb, IBlockReader world, BlockPos pos) {
////        BlockState state = world.getBlockState(pos);
////        if (state.shouldSideBeRendered(world, pos, Direction.DOWN)) {
////            pos(bb.maxX, bb.minY, bb.minZ);
////            pos(bb.maxX, bb.minY, bb.maxZ);
////            pos(bb.minX, bb.minY, bb.maxZ);
////            pos(bb.minX, bb.minY, bb.minZ);
////        }
////        if (state.shouldSideBeRendered(world, pos, Direction.UP)) {
////            pos(bb.minX, bb.maxY, bb.maxZ);
////            pos(bb.maxX, bb.maxY, bb.maxZ);
////            pos(bb.maxX, bb.maxY, bb.minZ);
////            pos(bb.minX, bb.maxY, bb.minZ);
////        }
////        if (state.shouldSideBeRendered(world, pos, Direction.NORTH)) {
////            pos(bb.minX, bb.maxY, bb.minZ);
////            pos(bb.maxX, bb.maxY, bb.minZ);
////            pos(bb.maxX, bb.minY, bb.minZ);
////            pos(bb.minX, bb.minY, bb.minZ);
////        }
////        if (state.shouldSideBeRendered(world, pos, Direction.SOUTH)) {
////            pos(bb.minX, bb.minY, bb.maxZ);
////            pos(bb.maxX, bb.minY, bb.maxZ);
////            pos(bb.maxX, bb.maxY, bb.maxZ);
////            pos(bb.minX, bb.maxY, bb.maxZ);
////        }
////        if (state.shouldSideBeRendered(world, pos, Direction.WEST)) {
////            pos(bb.minX, bb.minY, bb.minZ);
////            pos(bb.minX, bb.minY, bb.maxZ);
////            pos(bb.minX, bb.maxY, bb.maxZ);
////            pos(bb.minX, bb.maxY, bb.minZ);
////        }
////        if (state.shouldSideBeRendered(world, pos, Direction.EAST)) {
////            pos(bb.maxX, bb.maxY, bb.minZ);
////            pos(bb.maxX, bb.maxY, bb.maxZ);
////            pos(bb.maxX, bb.minY, bb.maxZ);
////            pos(bb.maxX, bb.minY, bb.minZ);
////        }
////    }
//
//    private void pos(double x, double y, double z) {
//        geometryCache.add(x);
//        geometryCache.add(y);
//        geometryCache.add(z);
//    }
//
//    public Iterable<@NonnullType ? extends BlockPos> getCandidates() {
//        return candidateCache;
//    }
//
////    @Override
////    public void notifyBlockUpdate(Level worldIn, BlockPos pos, BlockState oldState, BlockState newState, int flags) {
////        checkRedraw(new AxisAlignedBB(pos));
////    }
////
////    @Override
////    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
////        checkRedraw(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
////    }
//
//    private void checkRedraw(AxisAlignedBB updateRange) {
//        if (updateRange.intersects(candidateBounds)) {
//            Minecraft.getInstance().execute(this::updateCache);
//        }
//    }
//
//    private float anim = 0;
//
//    public void draw() {
//        if (checkDirty()) {
//            updateCache();
//        }
//
//        Timer timer = ClientUtil.getTimer();
//        float c = 1;
//        float a = 0.2f;
//        if (timer != null) {
//            c = Math.round(( ((float) Math.sin(anim / 10)) / 2) + 0.5f);
//            a = Math.abs(2 * (float) Math.sin(anim / 10)) * 0.1f;
//            anim += timer.elapsedPartialTicks;
//        }
//
//        RenderSystem.pushMatrix();
//        BufferBuilder buf = Tessellator.getInstance().getBuffer();
//        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
//        for (int i = 0; i < geometryCache.size(); i += 3) {
//            buf.pos(geometryCache.get(i), geometryCache.get(i + 1), geometryCache.get(i + 2)).color(c, c, c, a).endVertex();
//        }
//        Tessellator.getInstance().draw();
//        RenderSystem.popMatrix();
//    }
}
