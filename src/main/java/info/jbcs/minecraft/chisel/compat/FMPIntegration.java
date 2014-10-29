package info.jbcs.minecraft.chisel.compat;

import cpw.mods.fml.common.event.FMLInterModComms;
import info.jbcs.minecraft.chisel.compat.ModIntegration.IModIntegration;
import info.jbcs.minecraft.chisel.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class FMPIntegration implements IModIntegration{
    @Override
    public String getModId(){
        return "ForgeMicroblock";
    }

    @Override
    public void onInit(){
        registerMaterial(ModBlocks.marble);
        registerMaterial(ModBlocks.marblePillar);
        registerMaterial(ModBlocks.limestone);
        registerMaterial(ModBlocks.cobblestone);
        registerMaterial(ModBlocks.glass);
        registerMaterial(ModBlocks.sandstone, 3, 15);
        registerMaterial(ModBlocks.sandstoneScribbles);
        registerMaterial(ModBlocks.concrete, 0, 10);
        registerMaterial(ModBlocks.iron, 1, 15);
        registerMaterial(ModBlocks.gold, 1, 14);
        registerMaterial(ModBlocks.diamond, 1, 12);
        registerMaterial(ModBlocks.lightstone, 1, 15);
        registerMaterial(ModBlocks.lapis, 1, 8);
        registerMaterial(ModBlocks.emerald, 1, 11);
        registerMaterial(ModBlocks.netherBrick, 1, 15);
        registerMaterial(ModBlocks.netherrack, 1, 14);
        registerMaterial(ModBlocks.cobblestoneMossy, 1, 15);
        registerMaterial(ModBlocks.stoneBrick, 4, 15);
        registerMaterial(ModBlocks.dirt, 0, 12);
        registerMaterial(ModBlocks.ice, 1, 15);
        registerMaterial(ModBlocks.icePillar);
        registerMaterial(ModBlocks.obsidian, 1, 15);
        registerMaterial(ModBlocks.redstone, 1, 15);
        registerMaterial(ModBlocks.holystone, 0, 13);
        registerMaterial(ModBlocks.lavastone, 0, 6);
        registerMaterial(ModBlocks.fantasy);
        registerMaterial(ModBlocks.carpetFloor);
        registerMaterial(ModBlocks.tyrian);
        registerMaterial(ModBlocks.temple);
        registerMaterial(ModBlocks.templeMossy);
        registerMaterial(ModBlocks.cloud, 0, 0);
        registerMaterial(ModBlocks.factory);
        registerMaterial(ModBlocks.laboratory);
    }

    @Override
    public void onPostInit(){

    }

    private void registerMaterial(Block block){
        registerMaterial(block, 0, 15);
    }

    private void registerMaterial(Block block, int minMeta, int maxMeta){
        for(int c = minMeta; c <= maxMeta; c++){
            FMLInterModComms.sendMessage(getModId(), "microMaterial", new ItemStack(block, 1, c));
        }
    }
}
