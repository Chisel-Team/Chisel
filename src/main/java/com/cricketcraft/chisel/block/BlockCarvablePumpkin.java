package com.cricketcraft.chisel.block;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockCarvablePumpkin extends BlockPumpkin implements ICarvable{
    public CarvableHelper carverHelper;

    @SideOnly(Side.CLIENT)
    private IIcon top, face;

    private int blockMetadata;

    public BlockCarvablePumpkin(boolean isOn) {
        super(isOn);
        carverHelper = new CarvableHelper();
    }

    @SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta)
	{
        if(side == 0 || side == 1)
            return top;
        if(side != 3)
            return blockIcon;
        return face;
	}

    @Override
    public void registerBlockIcons(IIconRegister icon){
        System.out.println(carverHelper.getVariation(blockMetadata).texture);
        top = icon.registerIcon(Chisel.MOD_ID + ":pumpkin/pumpkin_top");
        face = icon.registerIcon(Chisel.MOD_ID + ":" + carverHelper.getVariation(blockMetadata).texture);
        this.blockIcon = icon.registerIcon(Chisel.MOD_ID + ":pumpkin/pumpkin_side");
    }

    @Override
    public CarvableVariation getVariation(int metadata) {
        blockMetadata = metadata;
        return carverHelper.getVariation(metadata);
    }

    /*@SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List subItems){
        for(int x = 0; x < 16; x++){
            subItems.add(new ItemStack(this, 1, x));
        }
    }*/
}
