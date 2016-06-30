package team.chisel.common.worldgen;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;

import java.util.List;
import java.util.Random;

public class ChiselAllOfTheVillagesHandler {

    private static Random rng = new Random();

    private static final int frequency = 2500;
    private static long lastTimeChecked = Minecraft.getSystemTime()/frequency;
    private static int seed = rng.nextInt();

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void getVillageBlockID(BiomeEvent.GetVillageBlockID event)
    {
        IBlockState originalState = event.getOriginal();

        long time = Minecraft.getSystemTime()/frequency;

        if(time > (lastTimeChecked + 100))
        {
            Chisel.logger.info(time + " - " + lastTimeChecked + " = " + (int)(time - lastTimeChecked));

            seed = (int)(time%Integer.MAX_VALUE);
        }
        else
        {
            lastTimeChecked = Minecraft.getSystemTime()/frequency;
        }

        if (seed < 0)
        {
            seed *= -1;
        }

        if(!(originalState == null) && (!(originalState.getMaterial() == Material.AIR) || !originalState.getBlock().equals(Blocks.AIR)))
        {
            //Chisel.logger.info(event.getOriginal().getBlock().getUnlocalizedName() + " - " + time);

            if(Item.getItemFromBlock(originalState.getBlock()) != null)
            {
                ICarvingGroup group = CarvingUtils.getChiselRegistry().getGroup(originalState);

                if (!(group == null))
                {
                    List<ICarvingVariation> variations = group.getVariations();

                    if(!(variations == null))
                    {
                        Chisel.logger.info(time);

                        IBlockState newState = variations.get((seed % variations.size())).getBlockState();

                        event.setReplacement(newState);

                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
    }
}
