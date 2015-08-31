package team.chisel.api.rendering;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IShaderRenderItem {

    @SideOnly(Side.CLIENT)
    public IIcon getMaskTexture(ItemStack stack, EntityPlayer player);

    @SideOnly(Side.CLIENT)
    public float getMaskMultiplier(ItemStack stack, EntityPlayer player);

}
