package team.chisel.client.render.ctx;

public class BlockRenderContextSheet extends BlockRenderContextPosition {

    private int xSize, ySize;

    public BlockRenderContextSheet(int x, int y, int z, int xSize, int ySize) {
        super(x, y, z);
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public int getXSize() {
        return this.xSize;
    }

    public int getYSize() {
        return this.ySize;
    }
}
