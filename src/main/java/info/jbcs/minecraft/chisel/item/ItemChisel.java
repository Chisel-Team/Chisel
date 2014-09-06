package info.jbcs.minecraft.chisel.item;

import cpw.mods.fml.common.FMLCommonHandler;
import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.ChiselBlocks;
import info.jbcs.minecraft.chisel.Configurations;
import info.jbcs.minecraft.chisel.carving.CarvableHelper;
import info.jbcs.minecraft.chisel.carving.Carving;
import info.jbcs.minecraft.chisel.carving.CarvingVariation;
import info.jbcs.minecraft.chisel.client.GeneralChiselClient;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ItemChisel extends ItemTool
{
    public static Carving carving = Carving.chisel;
    private static final HashSet<String> toolSet = new HashSet<String>();

    public ItemChisel(Carving c)
    {
        super(1, ToolMaterial.IRON, CarvableHelper.getChiselBlockSet());

        setMaxStackSize(1);
        setMaxDamage(500);
        efficiencyOnProperMaterial = 100f;
        setUnlocalizedName("chisel");

        toolSet.add("chisel");
        //this.carving = c;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack)
    {
        return toolSet;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.openGui(Chisel.instance, 0, world, 0, 0, 0);

        return itemstack;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
        return true;
    }
}
