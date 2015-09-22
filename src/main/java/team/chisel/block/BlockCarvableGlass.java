package team.chisel.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import team.chisel.ctmlib.CTM;

import com.cricketcraft.chisel.api.ChiselTabs;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.api.carving.CarvableHelper;
import com.cricketcraft.chisel.api.carving.IVariationInfo;
import com.cricketcraft.chisel.api.rendering.ClientUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarvableGlass extends BlockGlass implements ICarvable {

	public CarvableHelper carverHelper;
	private boolean isAlpha = false;
	private CTM ctm = CTM.getInstance();

	public BlockCarvableGlass() {
		super(Material.glass, false);

		carverHelper = new CarvableHelper(this);
		setCreativeTab(ChiselTabs.tabOtherChiselBlocks);
	}

	public BlockCarvableGlass setStained(boolean a) {
		this.isAlpha = a;
		return this;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return true;
	}

	@Override
	public int getRenderType() {
		return ClientUtils.renderCTMId;
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return carverHelper.getIcon(side, metadata);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		ForgeDirection face = ForgeDirection.getOrientation(side).getOpposite();
		int meX = x + face.offsetX;
		int meY = y + face.offsetY;
		int meZ = z + face.offsetZ;
		Block block = world.getBlock(meX, meY, meZ);
		int meta = world.getBlockMetadata(meX, meY, meZ);
		Block otherBlock = ctm.getBlockOrFacade(world, x, y, z, face.ordinal());
		int otherMeta = ctm.getBlockOrFacadeMetadata(world, x, y, z, face.ordinal());
		return block != otherBlock || meta != otherMeta;
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		carverHelper.registerBlockIcons("Chisel", this, register);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		carverHelper.registerSubBlocks(this, tabs, list);
	}

	@Override
	public IVariationInfo getManager(IBlockAccess world, int x, int y, int z, int metadata) {
		return carverHelper.getVariation(metadata);
	}

	@Override
	public IVariationInfo getManager(int meta) {
		return carverHelper.getVariation(meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return isAlpha ? 1 : 0;
	}
}
