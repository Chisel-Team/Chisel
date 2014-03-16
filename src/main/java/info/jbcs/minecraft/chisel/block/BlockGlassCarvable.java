package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.Carvable;
import info.jbcs.minecraft.chisel.CarvableHelper;
import info.jbcs.minecraft.chisel.CarvableVariation;
import info.jbcs.minecraft.chisel.Chisel;

import java.util.List;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockGlassCarvable extends BlockGlass implements Carvable {
	public CarvableHelper carverHelper;

	public BlockGlassCarvable() {
		super(Material.glass, false);

		carverHelper = new CarvableHelper();

		setCreativeTab(Chisel.tabChisel);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return Chisel.RenderCTMId;
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
		carverHelper.registerBlockIcons("Chisel",this,register);
	}

    @Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list){
		carverHelper.registerSubBlocks(this,tabs,list);
    }

	@Override
	public CarvableVariation getVariation(int metadata) {
		return carverHelper.getVariation(metadata);
	}
    

}
