package info.jbcs.minecraft.chisel;

import cpw.mods.fml.common.registry.GameRegistry;
import info.jbcs.minecraft.chisel.init.ModBlocks;
import info.jbcs.minecraft.chisel.init.ModItems;
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

        FurnaceRecipes.smelting().func_151393_a(concreteRecipeBlock, new ItemStack(ModBlocks.concrete), 0.1F);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.sandstoneScribbles, 1), new Object[]{"X", 'X', new ItemStack(ModBlocks.sandstone, 1, 8),});
        for(int meta = 0; meta < 16; meta++)
        {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.marbleSlab, 6, 0), new Object[]{"***", '*', new ItemStack(ModBlocks.marble, 1, meta)});
            GameRegistry.addRecipe(new ItemStack(ModBlocks.limestoneSlab, 6, 0), new Object[]{"***", '*', new ItemStack(ModBlocks.limestone, 1, meta)});
            GameRegistry.addRecipe(new ItemStack(ModBlocks.marblePillarSlab, 6, 0), new Object[]{"***", '*', new ItemStack(ModBlocks.marblePillar, 1, meta)});

            GameRegistry.addRecipe(new ItemStack(ModBlocks.marblePillar, 6), new Object[]{"XX", "XX", "XX", 'X', new ItemStack(ModBlocks.marble, 1, meta),});
            GameRegistry.addRecipe(new ItemStack(ModBlocks.marble, 4), new Object[]{"XX", "XX", 'X', new ItemStack(ModBlocks.marblePillar, 1, meta),});

            GameRegistry.addRecipe(new ItemStack(ModBlocks.icePillar, 6, 1), new Object[]{"XX", "XX", "XX", 'X', new ItemStack(ModBlocks.ice, 1, meta),});
            GameRegistry.addRecipe(new ItemStack(ModBlocks.ice, 4, 1), new Object[]{"XX", "XX", 'X', new ItemStack(ModBlocks.icePillar, 1, meta),});

            GameRegistry.addRecipe(new ItemStack(Blocks.sandstone, 1, 1), new Object[]{"X", 'X', new ItemStack(ModBlocks.sandstoneScribbles, 1, meta),});

            GameRegistry.addRecipe(new ItemStack(ModBlocks.carpet, 8, meta), new Object[]{"YYY", "YXY", "YYY", 'X', new ItemStack(Items.string, 1), 'Y', new ItemStack(Blocks.wool, 1, meta),});
            GameRegistry.addRecipe(new ItemStack(ModBlocks.carpetFloor, 3, meta), new Object[]{"XX", 'X', new ItemStack(ModBlocks.carpet, 1, meta),});
        }

        // The following recipe is due to bugs with Chisel 1.5.1 to 1.5.6a
        GameRegistry.addRecipe(new ItemStack(Blocks.sandstone, 1, 0), new Object[]{"X", 'X', new ItemStack(ModBlocks.sandstone, 1, 0),});

        // The following recipe is due to bug with Chisel 1.5.6b
        GameRegistry.addRecipe(new ItemStack(Blocks.sandstone, 1, 1), new Object[]{"X", 'X', new ItemStack(ModBlocks.sandstone, 1, 1),});


        GameRegistry.addRecipe(new ItemStack(ModBlocks.holystone, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.feather, 1)});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.lavastone, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.lava_bucket, 1)});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.fantasy, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.gold_nugget, 1)});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.tyrian, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1)});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.temple, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.dye, 1, 4)});
        GameRegistry.addRecipe(new ItemStack(ModBlocks.factory, Configurations.factoryBlockAmount, 0), new Object[]{"*X*", "X X", "*X*", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1)});

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.roadLine, 8, 0), new Object[]{"wrw", "wrw", "wrw", ('w'), "dyeWhite", ('r'), Items.redstone}));

        if(Configurations.chiselRecipe)
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.chisel, 1), new Object[]{" YY", " YY", "X  ", 'X', Items.stick, 'Y', Items.iron_ingot}));
        } else
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.chisel, 1), new Object[]{" Y", "X ", 'X', Items.stick, 'Y', Items.iron_ingot}));
        }

        if(Configurations.featureEnabled("ballOfMoss"))
            GameRegistry.addRecipe(new ItemStack(ModItems.itemBallOMoss, 1), new Object[]{"XYX", "YXY", "XYX", 'X', Blocks.vine, 'Y', Items.stick});
        if(Configurations.featureEnabled("cloud"))
            GameRegistry.addRecipe(new ItemStack(ModItems.itemCloudInABottle, 1), new Object[]{"X X", "XYX", " X ", 'X', Blocks.glass, 'Y', Items.quartz});
        if (Configurations.featureEnabled("smashingrock"))
            GameRegistry.addShapelessRecipe(new ItemStack(ModItems.smashingRock, 16), new Object[]{new ItemStack(Items.stone_pickaxe), new ItemStack(Items.glass_bottle, 1), new ItemStack(Items.stone_shovel)});

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.paperWall, 8), new Object[]{"ppp", "psp", "ppp", ('p'), Items.paper, ('s'), "stickWood"}));

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
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.woolenClay, 2, i), new Object[]{"blockWool" + sGNames[i].replaceAll(" ", ""), "stainedClay" + sGNames[i].replaceAll(" ", "")}));
        }

        GameRegistry.addRecipe(new ItemStack(ModBlocks.laboratory, 8, 0), new Object[]{"***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.quartz, 1)});

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoChisel, 1),new Object[]{"XXX", "XYX", "XXX", Character.valueOf('X'), "plankWood", Character.valueOf('Y'), ModItems.chisel}));
    }
}
