package com.cricketcraft.chisel.block;

import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.init.ModTabs;

public class BlockCarvableTorch extends BlockTorch implements ICarvable
{
    public CarvableHelper carverHelper;
    private String texLocation;

    public BlockCarvableTorch()
    {
        super();

        carverHelper = new CarvableHelper();

        setCreativeTab(ModTabs.tabChiselBlocks);
    }
	@Override
	public CarvableVariation getVariation(int metadata) {
		return carverHelper.getVariation(metadata);
	}
	@Override
    public void registerBlockIcons(IIconRegister icon){
        this.blockIcon = icon.registerIcon(Chisel.MOD_ID + ":torch/"+ texLocation);
    }
	public void setInformation(String textureLocation){
        this.texLocation = textureLocation;
    }
}
