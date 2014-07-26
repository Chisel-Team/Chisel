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
    Random random = new Random();
    public Carving carving;

    private static final HashSet<String> toolSet = new HashSet<String>();

    public ItemChisel(Carving c)
    {
        super(1, ToolMaterial.IRON, CarvableHelper.getChiselBlockSet());

        setMaxStackSize(1);
        setMaxDamage(500);
        efficiencyOnProperMaterial = 100f;
        setUnlocalizedName("chisel");

        toolSet.add("chisel");
        this.carving = c;
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

    HashMap<String, Long> chiselUseTime = new HashMap<String, Long>();
    HashMap<String, String> chiselUseLocation = new HashMap<String, String>();

    @Override
    public boolean onBlockStartBreak(ItemStack stack, final int x, final int y, final int z, EntityPlayer player)
    {
        if(!Configurations.enableChiseling) return true;

        World world = player.worldObj;
        Block block = world.getBlock(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);

        if(!ForgeHooks.isToolEffective(stack, block, blockMeta))
            return false;

        ItemStack chiselTarget = null;

        if(stack.stackTagCompound != null)
        {
            chiselTarget = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("chiselTarget"));
        }

        boolean chiselHasBlockInside = true;

        boolean noReplace = false;

        if(chiselTarget == null)
        {
            chiselHasBlockInside = false;

            Long useTime = chiselUseTime.get(player.getCommandSenderName());
            String loc = chiselUseLocation.get(player.getCommandSenderName());

            if(useTime != null && chiselUseLocation != null && loc.equals(x + "|" + y + "|" + z))
            {
                long cooldown = 20;
                long time = world.getWorldInfo().getWorldTotalTime();

                if(time>useTime-cooldown && time<useTime+cooldown) noReplace = true;
            }

            CarvingVariation[] variations = carving.getVariations(block, blockMeta);
            if(variations == null || variations.length < 2) noReplace = true;
            else
            {
                int index = random.nextInt(variations.length - 1);
                while(variations[index].block.equals(block) && variations[index].damage == blockMeta)
                {
                    index++;
                    if(index >= variations.length) index = 0;
                }
                CarvingVariation var = variations[index];
                chiselTarget = new ItemStack(var.block, 1, var.damage);
            }
        }
        Item result = null;
        int targetMeta = 0;

        if(!noReplace)
        {
            Item target = chiselTarget.getItem();

            targetMeta = chiselTarget.getItemDamage();

            boolean match = carving.isVariationOfSameClass(Block.getBlockFromItem(target), targetMeta, block, blockMeta);
            result = target;

			/* special case: stone can be carved to cobble and bricks */
            if(Configurations.chiselStoneToCobbleBricks)
            {
                if(!match && block.equals(Blocks.stone) && Block.getBlockFromItem(target).equals(ChiselBlocks.blockCobblestone))
                    match = true;
                if(!match && block.equals(Blocks.stone) && Block.getBlockFromItem(target).equals(ChiselBlocks.stoneBrick))
                    match = true;
            }
            if(!match)
                noReplace = true;
        }

        int updateValue = 2;

        if(noReplace)
        {
            result = Item.getItemFromBlock(block);
            targetMeta = blockMeta;
        }

        if(!world.isRemote || chiselHasBlockInside)
        {
            world.setBlock(x, y, z, Block.getBlockFromItem(result), targetMeta, updateValue);
        }

        switch(FMLCommonHandler.instance().getEffectiveSide())
        {
            case SERVER:
                chiselUseTime.put(player.getCommandSenderName(), world.getWorldInfo().getWorldTotalTime());
                chiselUseLocation.put(player.getCommandSenderName(), x + "|" + y + "|" + z);

                try
                {
                    //TODO chisel left click thingy
                    // Packet packet = Chisel.packet.create(Packets.CHISELED).writeInt(x).writeInt(y).writeInt(z);
                    // Chisel.packet.sendToAllAround(packet, new TargetPoint(player.dimension, x, y, z, 30.0f));
                } catch(Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case CLIENT:
                if(chiselHasBlockInside)
                {
                    String sound = carving.getVariationSound(result, chiselTarget.getItemDamage());
                    GeneralChiselClient.spawnChiselEffect(x, y, z, sound);
                }
                break;

            default:
                break;
        }

        stack.damageItem(1, player);
        if(stack.stackSize == 0)
        {
            player.inventory.mainInventory[player.inventory.currentItem] = chiselHasBlockInside ? chiselTarget : null;
        }

        return true;
    }


}
