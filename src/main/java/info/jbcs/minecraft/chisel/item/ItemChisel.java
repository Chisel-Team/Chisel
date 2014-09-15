package info.jbcs.minecraft.chisel.item;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.api.ChiselMode;
import info.jbcs.minecraft.chisel.api.IChiselMode;
import info.jbcs.minecraft.chisel.carving.CarvableHelper;
import info.jbcs.minecraft.chisel.carving.Carving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class ItemChisel extends ItemTool implements IChiselMode
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
    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player)
    {
        return true;
    }

    @Override
    public ChiselMode getChiselMode(ItemStack itemStack)
    {
        return ChiselMode.values()[itemStack.stackTagCompound.getInteger("chiselMode")];
    }
}
