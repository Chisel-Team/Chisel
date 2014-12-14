package com.cricketcraft.minecraft.chisel.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.cricketcraft.minecraft.chisel.Chisel;
import com.cricketcraft.minecraft.chisel.block.tileentity.TileEntityAutoChisel;

public class ItemUpgrade extends Item{

    public IIcon[] icons = new IIcon[6];

    public ItemUpgrade(String unlocalizedName) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int size, float hitX, float hitY, float hitZ){
        if(world.isRemote) return false;
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile != null && tile instanceof TileEntityAutoChisel){
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4){
        list.add(StatCollector.translateToLocal(this.getUnlocalizedName(itemStack) + ".desc"));
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        if (meta > 3)
            meta = 0;

        return this.icons[meta];
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        for (int i = 0; i < 3; i ++) {
            this.icons[i] = reg.registerIcon(Chisel.MOD_ID + ":upgradeItem_" + i);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 3; i ++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 0:
                return this.getUnlocalizedName() + "_speed";
            case 1:
                return this.getUnlocalizedName() + "_automation";
            case 2:
                return this.getUnlocalizedName() + "_stack";
            default:
                return this.getUnlocalizedName();
        }
    }
}
