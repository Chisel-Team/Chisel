package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.CarvableHelper;
import info.jbcs.minecraft.chisel.CarvableVariation;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.api.Carvable;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockCarvable extends Block implements Carvable {
	public CarvableHelper carverHelper;

	public BlockCarvable() {
		this(Material.rock);
	}
	
	public BlockCarvable(Material m) {
		super(m);

		carverHelper = new CarvableHelper();

		setCreativeTab(Chisel.tabChisel);
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return carverHelper.getIcon(side, metadata);
	}

    @Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side){
    	return carverHelper.getIcon(world, x, y, z, side);
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
	public int getRenderType() {
		return Chisel.RenderCTMId;
	}

	@Override
	public CarvableVariation getVariation(int metadata) {
		return carverHelper.getVariation(metadata);
	}

}
