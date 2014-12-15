package com.cricketcraft.chisel.block;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.init.ModTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockCarvablePumpkin extends BlockPumpkin implements ICarvable{
    public CarvableHelper carverHelper;

    @SideOnly(Side.CLIENT)
    private IIcon[] face = new IIcon[16];
    @SideOnly(Side.CLIENT)
    private IIcon top, side;

    private boolean isJackolantern;

    public BlockCarvablePumpkin(boolean isOn) {
        super(isOn);
        setCreativeTab(ModTabs.tabChiselBlocks);
        carverHelper = new CarvableHelper();
        this.isJackolantern = isOn;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if(side == 1 || side == 0){
            return this.top;
        } else if(meta == 2 && side == 2){
            return this.face[meta];
        } else if(meta == 3 && side == 5){
            return this.face[meta];
        } else if(meta == 0 && side == 3){
            return this.face[meta];
        } else if(meta == 1 && side == 4){
            return this.face[meta];
        } else {
            return this.side;
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister icon){
        top = icon.registerIcon(Chisel.MOD_ID + ":pumpkin/pumpkin_top");
        side = icon.registerIcon(Chisel.MOD_ID + ":pumpkin/pumpkin_side");
        for(int x = 0; x < 16; x++){
            face[x] = icon.registerIcon(Chisel.MOD_ID + ":pumpkin/pumpkin_face_" + (x + 1) + (isJackolantern ? "_on" : "_off"));
        }
    }

    @Override
    public CarvableVariation getVariation(int metadata) {
        return carverHelper.getVariation(metadata);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List subItems){
        for(int x = 0; x < 16; x++){
            subItems.add(new ItemStack(this, 1, x));
        }
    }
}
