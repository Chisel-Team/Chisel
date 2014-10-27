package info.jbcs.minecraft.chisel;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import info.jbcs.minecraft.chisel.api.ChiselMode;
import info.jbcs.minecraft.chisel.carving.CarvingVariation;
import info.jbcs.minecraft.chisel.client.GeneralChiselClient;
import info.jbcs.minecraft.chisel.item.ItemChisel;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.HashMap;
import java.util.Random;

public class ChiselLeftClick
{

    HashMap<String, Long> chiselUseTime = new HashMap<String, Long>();
    HashMap<String, String> chiselUseLocation = new HashMap<String, String>();
    Random random = new Random();

    @SubscribeEvent
    public void onPlayerClick(PlayerInteractEvent event)
    {
        if(event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) return;
        if(!Configurations.enableChiseling) return;
        EntityPlayer player = event.entityPlayer;
        ItemStack stack = player.getHeldItem();
        if(stack == null || stack.getItem() != Chisel.chisel) return;

        World world = event.world;
        int x = event.x;
        int y = event.y;
        int z = event.z;

        Block block = world.getBlock(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);

        ItemStack chiselTarget = null;

        if(stack.stackTagCompound != null)
        {
            chiselTarget = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("chiselTarget"));
        }

        boolean chiselHasBlockInside = true;

        if(chiselTarget == null)
        {
            chiselHasBlockInside = false;

            Long useTime = chiselUseTime.get(player.getCommandSenderName());
            String loc = chiselUseLocation.get(player.getCommandSenderName());

            if(useTime != null && chiselUseLocation != null && loc.equals(x + "|" + y + "|" + z))
            {
                long cooldown = 20;
                long time = world.getWorldInfo().getWorldTotalTime();

                if(time > useTime - cooldown && time < useTime + cooldown) return; //noReplace = true;
            }

            CarvingVariation[] variations = ItemChisel.carving.getVariations(block, blockMeta);
            if(variations == null || variations.length < 2) return; //noReplace = true;
            else
            {
                int index = blockMeta + 1;
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

        Item target = chiselTarget.getItem();

        targetMeta = chiselTarget.getItemDamage();

        boolean match = ItemChisel.carving.isVariationOfSameClass(Block.getBlockFromItem(target), targetMeta, block, blockMeta);
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
            return; //noReplace = true;

        int updateValue = 1;

        if(!world.isRemote || chiselHasBlockInside)
        {
            world.setBlock(x, y, z, Block.getBlockFromItem(result), targetMeta, updateValue);
            world.markBlockForUpdate(x, y, z);
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
                    String sound = ItemChisel.carving.getVariationSound(result, chiselTarget.getItemDamage());
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
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event)
    {
        if(!Configurations.enableChiseling) return;
        EntityPlayer player = event.getPlayer();
        ItemStack stack = player.getHeldItem();
        if(stack == null || stack.getItem() != Chisel.chisel) return;

        event.setCanceled(true);
    }


    public static void setBlock(World world, int x, int y, int z, ChiselMode chiselMode, Block previousBlock, int previousMetadata, Block newBlock, int newMetadata, ForgeDirection direction)
    {
        if(chiselMode == ChiselMode.SINGLE)
        {
            world.setBlock(x, y, z, newBlock, newMetadata, 2);
        } else if(chiselMode == ChiselMode.CIRCLETHREE)
        {
            int radius = 1;
            for(int i = -radius; i <= radius; i++)
                for(int j = -radius; j <= radius; j++)
                {
                    //TODO facing stuff so this can be anywhere
                    if(world.getBlock(x + i, y, z + j) != null)
                    {
                        if(world.getBlock(x + i, y, z + j) == previousBlock && world.getBlockMetadata(x + i, y, z + j) == previousMetadata)
                        {
                            int shiftedX = x;
                            int shiftedY = y;
                            int shiftedZ = z;

                            world.setBlock(shiftedX, shiftedY, shiftedZ, newBlock, newMetadata, 2);
                        }
                    }
                }

        }

    }

}
