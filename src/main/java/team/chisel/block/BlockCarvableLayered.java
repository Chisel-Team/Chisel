package team.chisel.block;

import team.chisel.Chisel;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarvableLayered extends BlockCarvable {

	@SideOnly(Side.CLIENT)
	private IIcon baseTexture;
	private String baseTexLocation;

	public BlockCarvableLayered(Material mat, String baseTexLocation) {
		super(mat);
		this.baseTexLocation = baseTexLocation;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		super.registerBlockIcons(register);
		baseTexture = register.registerIcon(Chisel.MOD_ID + ":" + baseTexLocation);
	}

	public IIcon getBaseTex() {
		return baseTexture;
	}

	@Override
	public int getRenderType() {
		return Chisel.renderLayeredId;
	}
}
