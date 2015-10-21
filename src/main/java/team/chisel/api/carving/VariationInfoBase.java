package team.chisel.api.carving;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import team.chisel.api.rendering.TextureType;
import team.chisel.ctmlib.ISubmapManager;

public class VariationInfoBase implements IVariationInfo {

	private ICarvingVariation variation;
	private String unlocDesc;
	private ISubmapManager manager;
	private TextureType type;

	public VariationInfoBase(ICarvingVariation variation, String unlocDesc, ISubmapManager manager) {
		this(variation, unlocDesc, manager, TextureType.CUSTOM);
	}

	public VariationInfoBase(ICarvingVariation variation, String unlocDesc, ISubmapManager manager, TextureType type) {
		this.variation = variation;
		this.unlocDesc = unlocDesc;
		this.manager = manager;
		this.type = type;
	}

	@Override
	public ICarvingVariation getVariation() {
		return variation;
	}

	@Override
	public String getDescription() {
		return StatCollector.translateToLocal(unlocDesc);
	}

	@Override
	public TextureType getType() {
		return type;
	}
	
	@Override
	public ISubmapManager getManager() {
		return manager;
	}

	// ISubmapManager delegation

	@Override
	public IIcon getIcon(int side, int meta) {
		return manager.getIcon(side, meta);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return manager.getIcon(world, x, y, z, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, Block block, IIconRegister register) {
		manager.registerIcons(modName, block, register);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, Block block, IBlockAccess world) {
		return manager.createRenderContext(rendererOld, block, world);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void preRenderSide(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		manager.preRenderSide(renderer, world, x, y, z, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void postRenderSide(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		manager.postRenderSide(renderer, world, x, y, z, side);
	}
}
