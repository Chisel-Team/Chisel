package team.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.rendering.ClientUtils;
import team.chisel.api.rendering.IOffsetRendered;
import team.chisel.api.rendering.TextureType;

public class SubmapManagerAnyV extends SubmapManagerBase implements IOffsetRendered {

	private String texture;
	private int xSize, ySize;
	private IIcon[][] submap;

	public SubmapManagerAnyV(String texture, int xSize, int ySize) {
		this.texture = texture;
		this.xSize = xSize;
		this.ySize = ySize;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return submap[0][0];
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return TextureType.getVIcon(ClientUtils.wrap(submap), x, y, z, side);
	}

	@Override
	public void registerIcons(String modName, Block block, IIconRegister register) {
		this.submap = new IIcon[xSize][ySize];
		for (int i = 0; i < submap.length; i++) {
			for (int j = 0; j < submap[i].length; j++) {
				submap[i][j] = register.registerIcon(String.format("%s:%s-%d-%d", modName, texture, i, j));
			}
		}
	}

	@Override
	public boolean canOffset(IBlockAccess world, int x, int y, int z, int side) {
		return true;
	}
}
