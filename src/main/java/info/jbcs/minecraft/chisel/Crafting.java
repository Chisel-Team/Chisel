package info.jbcs.minecraft.chisel;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class Crafting
{


    public static void init()
    {
        Block concreteRecipeBlock = Block.getBlockFromName(Configurations.config.get("tweaks", "concrete recipe block", "gravel", "Unlocalized name of the block that, when burned, will produce concrete (examples: lightgem, stone)").getString());
        if(concreteRecipeBlock == null) concreteRecipeBlock = Blocks.gravel;

        FurnaceRecipes.smelting().func_151393_a(concreteRecipeBlock, new ItemStack(ChiselBlocks.blockConcrete), 0.1F);

        GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockSandstoneScribbles, 1), new Object[]{"X", 'X', new ItemStack(ChiselBlocks.blockSandstone, 1, 8),});
        for(int meta = 0; meta < 16; meta++)
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockMarbleSlab, 6, 0), new Object[]{"***", '*', new ItemStack(ChiselBlocks.blockMarble, 1, meta)});
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockLimestoneSlab, 6, 0), new Object[]{"***", '*', new ItemStack(ChiselBlocks.blockLimestone, 1, meta)});
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockMarblePillarSlab, 6, 0), new Object[]{"***", '*', new ItemStack(ChiselBlocks.blockMarblePillar, 1, meta)});

            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockMarblePillar, 6), new Object[]{"XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.blockMarble, 1, meta),});
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockMarble, 4), new Object[]{"XX", "XX", 'X', new ItemStack(ChiselBlocks.blockMarblePillar, 1, meta),});

            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockIcePillar, 6), new Object[]{"XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.blockIce, 1, meta),});
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockIce, 4), new Object[]{"XX", "XX", 'X', new ItemStack(ChiselBlocks.blockIcePillar, 1, meta),});

            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockSandstone, 1), new Object[]{"X", 'X', new ItemStack(ChiselBlocks.blockSandstoneScribbles, 1, meta),});

            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockCarpet, 8, meta), new Object[]{"YYY", "YXY", "YYY", 'X', new ItemStack(Items.string, 1), 'Y', new ItemStack(Blocks.wool, 1, meta),});
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockCarpetFloor, 3, meta), new Object[]{"XX", 'X', new ItemStack(ChiselBlocks.blockCarpet, 1, meta),});
        }

        GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockHolystone, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.feather, 1)});
        GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockLavastone, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.lava_bucket, 1)});
        GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockFft, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.gold_nugget, 1)});
        GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockTyrian, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1)});
        GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockTemple, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.dye, 1, 4)});
        GameRegistry.addRecipe(new ItemStack(ChiselBlocks.blockFactory, Configurations.factoryBlockAmount, 0), new Object[]{"*X*", "X X", "*X*", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1)});

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.blockRoadLine, 8, 0), new Object[]{"wrw", "wrw", "wrw", ('w'), "dyeWhite", ('r'), Items.redstone}));

        if(Configurations.config.get("general", "chiselAlternateRecipe", false, "Use alternative crafting recipe for the chisel").getBoolean(false))
        {
            GameRegistry.addRecipe(new ItemStack(Chisel.chisel, 1), new Object[]{" YY", " YY", "X  ", 'X', Items.stick, 'Y', Items.iron_ingot});
        } else
        {
            GameRegistry.addRecipe(new ItemStack(Chisel.chisel, 1), new Object[]{" Y", "X ", 'X', Items.stick, 'Y', Items.iron_ingot});
        }

        GameRegistry.addRecipe(new ItemStack(Chisel.itemBallOMoss, 1), new Object[]{"XYX", "YXY", "XYX", 'X', Blocks.vine, 'Y', Items.stick});
        GameRegistry.addRecipe(new ItemStack(Chisel.itemCloudInABottle, 1), new Object[]{"X X", "XYX", " X ", 'X', Blocks.glass, 'Y', Items.quartz});

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.blockPaperwall, 8), new Object[]{"ppp", "psp", "ppp", ('p'), Items.paper, ('s'), "stickWood"}));

        String[] sGNames = new String[]{
                "White", "Orange", "Magenta", "Light Blue",
                "Yellow", "Lime", "Pink", "Gray",
                "Light Gray", "Cyan", "Purple", "Blue",
                "Brown", "Green", "Red", "Black"
        };

        for(int i = 0; i < 16; i++)
        {
            OreDictionary.registerOre("stainedClay" + sGNames[i].replaceAll(" ", ""), new ItemStack(Blocks.stained_hardened_clay, 1, i));
            OreDictionary.registerOre("blockWool" + sGNames[i].replaceAll(" ", ""), new ItemStack(Blocks.wool, 1, i));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ChiselBlocks.blockWoolenClay, 2, i), new Object[] {"blockWool" + sGNames[i].replaceAll(" ", ""), "stainedClay" + sGNames[i].replaceAll(" ", "")}));
        }


    }
}
