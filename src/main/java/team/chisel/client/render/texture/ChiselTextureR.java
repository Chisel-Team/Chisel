package team.chisel.client.render.texture;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.ctm.Submap;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.type.BlockRenderTypeR;

import com.google.gson.JsonObject;

/**
 * Texture for R texture types
 */
public class ChiselTextureR extends AbstractChiselTexture<BlockRenderTypeR> {

    private int xSize;

    private int ySize;

    private static final Random rand = new Random();

    public ChiselTextureR(BlockRenderTypeR type, TextureInfo info) {
        super(type, info);

        if (!this.info.isPresent()){
            throw new RuntimeException("Texture type V must have texture height and width or size associated with it!");
        }
        else {
            JsonObject object = this.info.get();
            if (object.has("width") && object.has("height")){
                if (object.get("width").isJsonPrimitive() && object.get("width").getAsJsonPrimitive().isNumber()){
                    this.xSize = object.get("width").getAsInt();
                }
                else {
                    throw new RuntimeException("Width must be a number!");
                }
                if (object.get("height").isJsonPrimitive() && object.get("height").getAsJsonPrimitive().isNumber()){
                    this.xSize = object.get("height").getAsInt();
                }
                else {
                    throw new RuntimeException("Height must be a number!");
                }
            }
            else if (object.has("size")){
                if (object.get("size").isJsonPrimitive() && object.get("size").getAsJsonPrimitive().isNumber()){
                    this.xSize = object.get("size").getAsInt();
                    this.ySize = object.get("size").getAsInt();
                }
                else {
                    throw new RuntimeException("Size must be a number!");
                }
            }
            else {
                throw new RuntimeException("Texture type V must have texture height and width or size associated with it!");
            }
        }
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        EnumFacing side = quad.getFace();
        
        BlockPos pos = context == null ? new BlockPos(0, 0, 0) : ((BlockRenderContextPosition) context).getPosition();
        rand.setSeed(MathHelper.getPositionRandom(pos) + side.ordinal());
        rand.nextBoolean();

        int w = xSize;
        int h = ySize;
        float intervalX = 16f / w;
        float intervalY = 16f / h;

        int unitsAcross = rand.nextInt(w) + 1;
        int unitsDown = rand.nextInt(h) + 1;

        float maxU = unitsAcross * intervalX;
        float maxV = unitsDown * intervalY;
        ISubmap uvs = new Submap(intervalX, intervalY, maxU - intervalX, maxV - intervalY);

        return Collections.singletonList(Quad.from(quad).transformUVs(sprites[0].getSprite(), uvs).setFullbright(fullbright).rebake());
    }
}
