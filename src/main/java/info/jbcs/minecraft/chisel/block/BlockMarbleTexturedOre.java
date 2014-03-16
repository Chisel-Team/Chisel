package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.Carvable;
import info.jbcs.minecraft.chisel.CarvableHelper;
import info.jbcs.minecraft.chisel.CarvableVariation;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.utilities.BlockTexturedOre;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockMarbleTexturedOre extends BlockTexturedOre implements Carvable {
	public CarvableHelper carverHelper;

	public BlockMarbleTexturedOre(Material mat, String baseIcon) {
		super(mat, baseIcon);
		
		carverHelper = new CarvableHelper();

		setCreativeTab(Chisel.tabChisel);
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return carverHelper.getIcon(side, metadata);
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		super.registerBlockIcons(register);

		carverHelper.registerBlockIcons("Chisel",this,register);
	}

    @Override
	public void getSubBlocks(Item block, CreativeTabs tabs, List list){
		carverHelper.registerSubBlocks(this,tabs,list);
   }

	@Override
	public CarvableVariation getVariation(int metadata) {
		return carverHelper.getVariation(metadata);
	}
}
