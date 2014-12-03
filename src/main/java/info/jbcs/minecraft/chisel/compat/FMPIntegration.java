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
        registerMaterial(ModBlocks.limestone);
        registerMaterial(ModBlocks.cobblestone, 1, 15);
        registerMaterial(ModBlocks.glass, 1, 15);
        registerMaterial(ModBlocks.sandstone, 3, 15);
        registerMaterial(ModBlocks.sandSnakestone, 1, 1);
        registerMaterial(ModBlocks.sandSnakestone, 13, 13);
        registerMaterial(ModBlocks.sandstoneScribbles);
        registerMaterial(ModBlocks.concrete, 0, 10);
        registerMaterial(ModBlocks.iron, 1, 15);
        registerMaterial(ModBlocks.gold, 1, 14);
        registerMaterial(ModBlocks.diamond, 1, 12);
        registerMaterial(ModBlocks.lightstone, 1, 15); //This is glowstone
        registerMaterial(ModBlocks.lapis, 1, 8);
        registerMaterial(ModBlocks.emerald, 1, 11);
        registerMaterial(ModBlocks.netherBrick, 1, 5);
        registerMaterial(ModBlocks.netherrack, 1, 14);
        registerMaterial(ModBlocks.cobblestoneMossy, 1, 15);
        registerMaterial(ModBlocks.stoneBrick, 4, 15);
        registerMaterial(ModBlocks.snakestone, 1, 1);
        registerMaterial(ModBlocks.snakestone, 13, 13);
        registerMaterial(ModBlocks.dirt, 0, 3);
        registerMaterial(ModBlocks.dirt, 6, 7);
        registerMaterial(ModBlocks.dirt, 11, 11);
        registerMaterial(ModBlocks.ice, 1, 15);
        registerMaterial(ModBlocks.icePillar);
        for(int x = 0; x < 4; x++){
            registerMaterial(ModBlocks.planks[x], 1, 15); // Oak, Spruce, Birch and Jungle
        }
        //Accacia
        registerMaterial(ModBlocks.planks[4], 1, 1);
        registerMaterial(ModBlocks.planks[4], 2, 2);
        registerMaterial(ModBlocks.planks[4], 6, 6);
        registerMaterial(ModBlocks.planks[4], 8, 11);
        registerMaterial(ModBlocks.planks[4], 13, 13);

        //Dark Oak
        registerMaterial(ModBlocks.planks[5], 1, 1);
        registerMaterial(ModBlocks.planks[5], 2, 2);
        registerMaterial(ModBlocks.planks[5], 6, 6);
        registerMaterial(ModBlocks.planks[5], 8, 11);
        registerMaterial(ModBlocks.planks[5], 13, 13);

        registerMaterial(ModBlocks.obsidian, 1, 15);
        registerMaterial(ModBlocks.obsidianSnakestone, 1, 1);
        registerMaterial(ModBlocks.obsidianSnakestone, 13, 13);
        registerMaterial(ModBlocks.redstone, 1, 15);
        registerMaterial(ModBlocks.holystone, 0, 13);
        registerMaterial(ModBlocks.carpet);
        registerMaterial(ModBlocks.lavastone, 0, 6);
        registerMaterial(ModBlocks.fantasy, 0, 1);
        registerMaterial(ModBlocks.fantasy, 3, 14);
        registerMaterial(ModBlocks.tyrian);
        registerMaterial(ModBlocks.temple, 1, 2);
        registerMaterial(ModBlocks.temple, 4, 15);
        registerMaterial(ModBlocks.templeMossy, 1, 2);
        registerMaterial(ModBlocks.templeMossy, 4, 15);
        registerMaterial(ModBlocks.factory, 0, 12);
        registerMaterial(ModBlocks.factory, 14, 14);
        registerMaterial(ModBlocks.stainedGlass[0]); //White
        registerMaterial(ModBlocks.stainedGlass[4]); //Yellow
        registerMaterial(ModBlocks.stainedGlass[8]); //Light Gray
        registerMaterial(ModBlocks.stainedGlass[12]); //Brown
        registerMaterial(ModBlocks.woolenClay);
        registerMaterial(ModBlocks.laboratory, 2, 3);
        registerMaterial(ModBlocks.laboratory, 5, 12);
        registerMaterial(ModBlocks.voidstone, 0, 3);
        registerMaterial(ModBlocks.voidstone, 12, 15);
        registerMaterial(ModBlocks.waterstone);
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
