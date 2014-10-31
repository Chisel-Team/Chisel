package info.jbcs.minecraft.chisel.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.api.ICarvable;
import info.jbcs.minecraft.chisel.carving.CarvableHelper;
import info.jbcs.minecraft.chisel.carving.CarvableVariation;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockCarvablePumpkin extends BlockPumpkin implements ICarvable{
    public CarvableHelper carverHelper;

    @SideOnly(Side.CLIENT)
    private IIcon top;
    @SideOnly(Side.CLIENT)
    private IIcon face;
    @SideOnly(Side.CLIENT)
    private IIcon side;
    private int sideVal, faceVal;

    public BlockCarvablePumpkin(boolean isOn, int sideTexture, int faceTexture) {
        super(isOn);
        sideVal = sideTexture;
        faceVal = faceTexture;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        //if(side != 0 || side != 1){
          //  if(side == ForgeDirection.)

        return null;
    }

    @Override
    public void registerBlockIcons(IIconRegister icon){
        top = icon.registerIcon(this.getTextureName() + "_top_" + sideVal);
        side = icon.registerIcon(this.getTextureName() + "_side_" + sideVal);
        face = icon.registerIcon(this.getTextureName() + "_face_" + faceVal);
    }

    @Override
    public CarvableVariation getVariation(int metadata) {
        return carverHelper.getVariation(metadata);
    }
}
