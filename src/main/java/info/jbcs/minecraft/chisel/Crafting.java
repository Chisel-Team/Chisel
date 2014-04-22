package info.jbcs.minecraft.chisel;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;

public class Crafting
{


    public static void init()
    {
        Block concreteRecipeBlock = Block.getBlockFromName(Chisel.config.get("tweaks", "concrete recipe block", "gravel", "Unlocalized name of the block that, when burned, will produce concrete (examples: lightgem, stone)").getString());
        if(concreteRecipeBlock == null) concreteRecipeBlock = Blocks.gravel;

        FurnaceRecipes.smelting().func_151393_a(concreteRecipeBlock, new ItemStack(ChiselBlocks.blockConcrete), 0.1F);

        CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockSandstoneScribbles, 1), new Object[]{"X", 'X', new ItemStack(ChiselBlocks.blockSandstone, 1, 8),});
        for(int meta = 0; meta < 16; meta++)
        {
            CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockMarbleSlab, 6, 0), new Object[]{"***", '*', new ItemStack(ChiselBlocks.blockMarble, 1, meta)});
            CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockLimestoneSlab, 6, 0), new Object[]{"***", '*', new ItemStack(ChiselBlocks.blockLimestone, 1, meta)});
            CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockMarblePillarSlab, 6, 0), new Object[]{"***", '*', new ItemStack(ChiselBlocks.blockMarblePillar, 1, meta)});

            CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockMarblePillar, 6), new Object[]{"XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.blockMarble, 1, meta),});
            CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockMarble, 4), new Object[]{"XX", "XX", 'X', new ItemStack(ChiselBlocks.blockMarblePillar, 1, meta),});

            CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockIcePillar, 6), new Object[]{"XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.blockIce, 1, meta),});
            CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockIce, 4), new Object[]{"XX", "XX", 'X', new ItemStack(ChiselBlocks.blockIcePillar, 1, meta),});

            CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockSandstone, 1), new Object[]{"X", 'X', new ItemStack(ChiselBlocks.blockSandstoneScribbles, 1, meta),});

            CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockCarpet, 8, meta), new Object[]{"YYY", "YXY", "YYY", 'X', new ItemStack(Items.string, 1), 'Y', new ItemStack(Blocks.wool, 1, meta),});
            CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockCarpetFloor, 3, meta), new Object[]{"XX", 'X', new ItemStack(ChiselBlocks.blockCarpet, 1, meta),});
        }

        CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockHolystone, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.feather, 1)});
        CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockLavastone, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.lava_bucket, 1)});
        CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockFft, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.gold_nugget, 1)});
        CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockTyrian, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1)});
        CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockTemple, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.dye, 1, 4)});
        CraftingManager.getInstance().addRecipe(new ItemStack(ChiselBlocks.blockFactory, 32, 0), new Object[]{"*X*", "X X", "*X*", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1)});


        if(Chisel.config.get("general", "chiselAlternateRecipe", false, "Use alternative crafting recipe for the chisel").getBoolean(false))
        {
            CraftingManager.getInstance().addRecipe(new ItemStack(Chisel.chisel, 1), new Object[]{" YY", " YY", "X  ", 'X', Items.stick, 'Y', Items.iron_ingot});
        } else
        {
            CraftingManager.getInstance().addRecipe(new ItemStack(Chisel.chisel, 1), new Object[]{" Y", "X ", 'X', Items.stick, 'Y', Items.iron_ingot});
        }

        CraftingManager.getInstance().addRecipe(new ItemStack(Chisel.itemBallOMoss, 1), new Object[]{"XYX", "YXY", "XYX", 'X', Blocks.vine, 'Y', Items.stick});
        CraftingManager.getInstance().addRecipe(new ItemStack(Chisel.itemCloudInABottle, 1), new Object[]{"X X", "XYX", " X ", 'X', Blocks.glass, 'Y', Items.quartz});


    }
}
