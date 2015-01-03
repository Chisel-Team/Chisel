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

    private String faceLocation;

    public BlockCarvablePumpkin(boolean isOn) {
        super(isOn);
        if(isOn)
            setLightLevel(10.0F);
        carverHelper = new CarvableHelper();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? top : (side == 0 ? top : (meta == 2 && side == 2 ? face : (meta == 3 && side == 5 ? face : (meta == 0 && side == 3 ? face : (meta == 1 && side == 4 ? face : this.blockIcon)))));
    }

    @Override
    public void registerBlockIcons(IIconRegister icon){
        top = icon.registerIcon(Chisel.MOD_ID + ":pumpkin/pumpkin_top");
        face = icon.registerIcon(Chisel.MOD_ID + ":" + faceLocation);
        this.blockIcon = icon.registerIcon(Chisel.MOD_ID + ":pumpkin/pumpkin_side");
    }

    @Override
    public CarvableVariation getVariation(int metadata) {
        return carverHelper.getVariation(metadata);
    }

    public void setInformation(String textureLocation){
        this.faceLocation = textureLocation;
    }
}
