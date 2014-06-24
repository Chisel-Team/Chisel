package info.jbcs.minecraft.chisel;

import cpw.mods.fml.common.registry.GameRegistry;
import info.jbcs.minecraft.chisel.block.*;
import info.jbcs.minecraft.chisel.carving.CarvableHelper;
import info.jbcs.minecraft.chisel.carving.CarvableVariation;
import info.jbcs.minecraft.chisel.carving.Carving;
import info.jbcs.minecraft.chisel.item.ItemCarvable;
import info.jbcs.minecraft.chisel.item.ItemMarbleSlab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ChiselBlocks
{
    public static BlockCarvable blockMarble;
    public static BlockCarvable blockMarblePillar;
    public static BlockCarvable blockLimestone;
    public static BlockMarbleSlab blockMarbleSlab;
    public static BlockMarbleSlab blockLimestoneSlab;
    public static BlockMarbleSlab blockMarblePillarSlab;
    public static BlockCarvable blockCobblestone;
    public static BlockMarbleWall cobblestoneWall;
    public static BlockCarvableGlass blockGlass;
    public static BlockCarvablePane blockPaneGlass;
    public static BlockCarvable blockSandstone;
    public static BlockCarvable blockSandstoneScribbles;
    public static BlockConcrete blockConcrete;
    public static BlockRoadLine blockRoadLine;
    public static BlockCarvable blockIron;
    public static BlockCarvable blockGold;
    public static BlockCarvable blockDiamond;
    public static BlockLightstoneCarvable blockLightstone;
    public static BlockCarvable blockLapis;
    public static BlockCarvable blockEmerald;
    public static BlockCarvable blockNetherBrick;
    public static BlockCarvable blockNetherrack;
    public static BlockCarvable blockCobblestoneMossy;
    public static BlockCarvable stoneBrick;
    public static BlockMarbleStairs blockMarbleStairs;
    public static BlockMarbleStairs blockLimestoneStairs;
    public static BlockCarvablePane blockPaneIron;
    public static BlockMarbleIce blockIce;
    public static BlockMarbleIce blockIcePillar;
    public static BlockMarbleIceStairs blockIceStairs;
    public static BlockCarvable[] blockPlanks = new BlockCarvable[6];
    public static BlockCarvable blockObsidian;
    public static BlockCarvablePowered blockRedstone;
    public static BlockHolystone blockHolystone;
    public static BlockLavastone blockLavastone;
    public static BlockCarvable blockFft;
    public static BlockCarvable blockCarpet;
    public static BlockMarbleCarpet blockCarpetFloor;
    public static BlockCarvable blockBookshelf;
    public static BlockCarvable blockTyrian;
    public static BlockCarvable blockDirt;
    public static BlockCloud blockCloud;
    public static BlockCarvable blockTemple;
    public static BlockCarvable blockTempleMossy;
    public static BlockCarvable blockFactory;
    public static BlockCarvablePane blockPaperwall;

    public static BlockSnakestone blockSnakestone;
    public static BlockSnakestone blockSandSnakestone;
    public static BlockSnakestoneObsidian blockObsidianSnakestone;

    public static BlockSpikes blockSpiketrap;

    // 1.7
    public static BlockCarvableGlass[] blockStainedGlass = new BlockCarvableGlass[4];
    public static BlockCarvablePane[] blockStainedGlassPane = new BlockCarvablePane[8];

    public static void load()
    {
        if(Chisel.featureEnabled("marble"))
        {
            blockMarble = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
            blockMarble.carverHelper.setChiselBlockName("Marble");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.0.desc"), 0, "marble");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.1.desc"), 1, "marble/a1-stoneornamental-marblebrick");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.2.desc"), 2, "marble/a1-stoneornamental-marbleclassicpanel");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.3.desc"), 3, "marble/a1-stoneornamental-marbleornate");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.4.desc"), 4, "marble/panel");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.5.desc"), 5, "marble/block");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.6.desc"), 6, "marble/terrain-pistonback-marblecreeperdark");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.7.desc"), 7, "marble/terrain-pistonback-marblecreeperlight");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.8.desc"), 8, "marble/a1-stoneornamental-marblecarved");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.9.desc"), 9, "marble/a1-stoneornamental-marblecarvedradial");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.10.desc"), 10, "marble/terrain-pistonback-marbledent");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.11.desc"), 11, "marble/terrain-pistonback-marbledent-small");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.12.desc"), 12, "marble/marble-bricks");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.13.desc"), 13, "marble/marble-arranged-bricks");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.14.desc"), 14, "marble/marble-fancy-bricks");
            blockMarble.carverHelper.addVariation(I18n.format("tile.marbleChisel.15.desc"), 15, "marble/marble-blocks");
            blockMarble.carverHelper.register(blockMarble, "marbleChisel");
            OreDictionary.registerOre("blockMarble", blockMarble);
            Carving.chisel.registerOre("marble", "blockMarble");

            if(Chisel.multipartLoaded)
            {
                //    for(int i = 0; i < 16; i++)
                //        MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(blockMarble, i), "blockMarble" + i);
            }

            blockMarbleSlab = (BlockMarbleSlab) new BlockMarbleSlab(blockMarble).setHardness(2.0F).setResistance(10F);
            blockMarbleSlab.carverHelper.setChiselBlockName("Marble Slab");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.0.desc"), 0, "marble");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.1.desc"), 1, "marbleslab/a1-stoneornamental-marblebrick");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.2.desc"), 2, "marbleslab/a1-stoneornamental-marbleclassicpanel");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.3.desc"), 3, "marbleslab/a1-stoneornamental-marbleornate");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.4.desc"), 4, "marbleslab/a1-stoneornamental-marblepanel");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.5.desc"), 5, "marbleslab/terrain-pistonback-marble");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.6.desc"), 6, "marbleslab/terrain-pistonback-marblecreeperdark");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.7.desc"), 7, "marbleslab/terrain-pistonback-marblecreeperlight");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.8.desc"), 8, "marbleslab/a1-stoneornamental-marblecarved");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.9.desc"), 9, "marbleslab/a1-stoneornamental-marblecarvedradial");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.10.desc"), 10, "marbleslab/terrain-pistonback-marbledent");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.11.desc"), 11, "marbleslab/terrain-pistonback-marbledent-small");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.12.desc"), 12, "marbleslab/marble-bricks");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.13.desc"), 13, "marbleslab/marble-arranged-bricks");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.14.desc"), 14, "marbleslab/marble-fancy-bricks");
            blockMarbleSlab.carverHelper.addVariation(I18n.format("tile.marbleSlab.15.desc"), 15, "marbleslab/marble-blocks");
            blockMarbleSlab.carverHelper.register(blockMarbleSlab, "marbleSlab", ItemMarbleSlab.class);

            if(Chisel.featureEnabled("marblePillar"))
            {
                if(Chisel.oldPillars)
                {
                    blockMarblePillar = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
                    blockMarblePillar.carverHelper.setChiselBlockName("Marble Pillar");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.0.desc"), 0, "marblepillarold/column");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.1.desc"), 1, "marblepillarold/capstone");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.2.desc"), 2, "marblepillarold/base");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.3.desc"), 3, "marblepillarold/small");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.4.desc"), 4, "marblepillarold/pillar-carved");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.5.desc"), 5, "marblepillarold/a1-stoneornamental-marblegreek");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.6.desc"), 6, "marblepillarold/a1-stonepillar-greek");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.7.desc"), 7, "marblepillarold/a1-stonepillar-plain");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.8.desc"), 8, "marblepillarold/a1-stonepillar-greektopplain");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.9.desc"), 9, "marblepillarold/a1-stonepillar-plaintopplain");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.10.desc"), 10, "marblepillarold/a1-stonepillar-greekbottomplain");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.11.desc"), 11, "marblepillarold/a1-stonepillar-plainbottomplain");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.12.desc"), 12, "marblepillarold/a1-stonepillar-greektopgreek");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.13.desc"), 13, "marblepillarold/a1-stonepillar-plaintopgreek");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.14.desc"), 14, "marblepillarold/a1-stonepillar-greekbottomgreek");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillarOld.15.desc"), 15, "marblepillarold/a1-stonepillar-plainbottomgreek");
                } else
                {
                    blockMarblePillar = (BlockCarvable) new BlockMarblePillar(Material.rock).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
                    blockMarblePillar.carverHelper.setChiselBlockName("Marble Pillar");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.0.desc"), 0, "marblepillar/pillar");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.1.desc"), 1, "marblepillar/default");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.2.desc"), 2, "marblepillar/simple");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.3.desc"), 3, "marblepillar/convex");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.4.desc"), 4, "marblepillar/rough");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.5.desc"), 5, "marblepillar/greekdecor");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.6.desc"), 6, "marblepillar/greekgreek");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.7.desc"), 7, "marblepillar/greekplain");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.8.desc"), 8, "marblepillar/plaindecor");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.9.desc"), 9, "marblepillar/plaingreek");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.10.desc"), 10, "marblepillar/plainplain");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.11.desc"), 11, "marblepillar/widedecor");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.12.desc"), 12, "marblepillar/widegreek");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.13.desc"), 13, "marblepillar/wideplain");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.14.desc"), 14, "marblepillar/carved");
                    blockMarblePillar.carverHelper.addVariation(I18n.format("tile.marblePillar.15.desc"), 15, "marblepillar/ornamental");
                }
                blockMarblePillar.carverHelper.register(blockMarblePillar, "marblePillar");
                Carving.chisel.setGroupClass("marblePillar", "marble");

                blockMarblePillarSlab = (BlockMarbleSlab) new BlockMarbleSlab(blockMarblePillar).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
                blockMarblePillarSlab.carverHelper.setChiselBlockName("Marble Pillar Slab");
                if(Chisel.oldPillars)
                {
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.0.desc"), 0, "marblepillarslabold/column");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.1.desc"), 1, "marblepillarslabold/capstone");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.2.desc"), 2, "marblepillarslabold/base");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.3.desc"), 3, "marblepillarslabold/small");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.4.desc"), 4, "marblepillarslabold/pillar-carved");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.5.desc"), 5, "marblepillarslabold/a1-stoneornamental-marblegreek");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.6.desc"), 6, "marblepillarslabold/a1-stonepillar-greek");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.7.desc"), 7, "marblepillarslabold/a1-stonepillar-plain");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.8.desc"), 8, "marblepillarslabold/a1-stonepillar-greektopplain");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.9.desc"), 9, "marblepillarslabold/a1-stonepillar-plaintopplain");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.10.desc"), 10, "marblepillarslabold/a1-stonepillar-greekbottomplain");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.11.desc"), 11, "marblepillarslabold/a1-stonepillar-plainbottomplain");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.12.desc"), 12, "marblepillarslabold/a1-stonepillar-greektopgreek");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.13.desc"), 13, "marblepillarslabold/a1-stonepillar-plaintopgreek");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.14.desc"), 14, "marblepillarslabold/a1-stonepillar-greekbottomgreek");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlabOld.15.desc"), 15, "marblepillarslabold/a1-stonepillar-plainbottomgreek");
                } else
                {
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.0.desc"), 0, "marblepillarslab/pillar");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.1.desc"), 1, "marblepillarslab/default");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.2.desc"), 2, "marblepillarslab/simple");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.3.desc"), 3, "marblepillarslab/convex");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.4.desc"), 4, "marblepillarslab/rough");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.5.desc"), 5, "marblepillarslab/greekdecor");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.6.desc"), 6, "marblepillarslab/greekgreek");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.7.desc"), 7, "marblepillarslab/greekplain");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.8.desc"), 8, "marblepillarslab/plaindecor");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.9.desc"), 9, "marblepillarslab/plaingreek");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.10.desc"), 10, "marblepillarslab/plainplain");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.11.desc"), 11, "marblepillarslab/widedecor");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.12.desc"), 12, "marblepillarslab/widegreek");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.13.desc"), 13, "marblepillarslab/wideplain");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.14.desc"), 14, "marblepillarslab/carved");
                    blockMarblePillarSlab.carverHelper.addVariation(I18n.format("tile.marblePillarSlab.15.desc"), 15, "marblepillarslab/ornamental");
                }
                blockMarblePillarSlab.carverHelper.register(blockMarblePillarSlab, "marblePillarSlab", ItemMarbleSlab.class);
            }

            BlockMarbleStairsMaker makerMarbleStairs = new BlockMarbleStairsMaker(blockMarble);
            makerMarbleStairs.carverHelper.setChiselBlockName("Marble Stairs");
            makerMarbleStairs.carverHelper.addVariation("Raw marble stairs", 0, "marble");
            makerMarbleStairs.carverHelper.addVariation("Marble brick stairs", 1, "marbleslab/a1-stoneornamental-marblebrick");
            makerMarbleStairs.carverHelper.addVariation("Classic marble panel stairs", 2, "marbleslab/a1-stoneornamental-marbleclassicpanel");
            makerMarbleStairs.carverHelper.addVariation("Ornate marble panel stairs", 3, "marbleslab/a1-stoneornamental-marbleornate");
            makerMarbleStairs.carverHelper.addVariation("Marble panel stairs", 4, "marbleslab/a1-stoneornamental-marblepanel");
            makerMarbleStairs.carverHelper.addVariation("Marble block stairs", 5, "marbleslab/terrain-pistonback-marble");
            makerMarbleStairs.carverHelper.addVariation("Dark creeper marble stairs", 6, "marbleslab/terrain-pistonback-marblecreeperdark");
            makerMarbleStairs.carverHelper.addVariation("Light creeper marble stairs", 7, "marbleslab/terrain-pistonback-marblecreeperlight");
            makerMarbleStairs.carverHelper.addVariation("Carved marble stairs", 8, "marbleslab/a1-stoneornamental-marblecarved");
            makerMarbleStairs.carverHelper.addVariation("Radial carved marble stairs", 9, "marbleslab/a1-stoneornamental-marblecarvedradial");
            makerMarbleStairs.carverHelper.addVariation("Marble stairs with dent", 10, "marbleslab/terrain-pistonback-marbledent");
            makerMarbleStairs.carverHelper.addVariation("Marble stairs with large dent ", 11, "marbleslab/terrain-pistonback-marbledent-small");
            makerMarbleStairs.carverHelper.addVariation("Marble tiles stairs", 12, "marbleslab/marble-bricks");
            makerMarbleStairs.carverHelper.addVariation("Arranged marble tiles stairs", 13, "marbleslab/marble-arranged-bricks");
            makerMarbleStairs.carverHelper.addVariation("Fancy marble tiles stairs", 14, "marbleslab/marble-fancy-bricks");
            makerMarbleStairs.carverHelper.addVariation("Marble blocks stairs", 15, "marbleslab/marble-blocks");
            makerMarbleStairs.create("chisel.marbleStairs");
        }

        if(Chisel.featureEnabled("limestone"))
        {
            blockLimestone = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
            blockLimestone.carverHelper.setChiselBlockName("Limestone");
            blockLimestone.carverHelper.addVariation("Limestone", 0, "limestone");
            blockLimestone.carverHelper.addVariation("Small limestone tiles", 1, "limestone/terrain-cobbsmalltilelight");
            blockLimestone.carverHelper.addVariation("French limestone tiles", 2, "limestone/terrain-cob-frenchlight");
            blockLimestone.carverHelper.addVariation("French limestone tiles", 3, "limestone/terrain-cob-french2light");
            blockLimestone.carverHelper.addVariation("Creeper limestone tiles", 4, "limestone/terrain-cobmoss-creepdungeonlight");
            blockLimestone.carverHelper.addVariation("Small limestone bricks", 5, "limestone/terrain-cob-smallbricklight");
            blockLimestone.carverHelper.addVariation("Damaged limestone tiles", 6, "limestone/terrain-mossysmalltilelight");
            blockLimestone.carverHelper.addVariation("Smooth limestone", 7, "limestone/terrain-pistonback-dungeon");
            blockLimestone.carverHelper.addVariation("Limestone with ornate panel", 8, "limestone/terrain-pistonback-dungeonornate");
            blockLimestone.carverHelper.addVariation("Limestone with ornate panel", 9, "limestone/terrain-pistonback-dungeonvent");
            blockLimestone.carverHelper.addVariation("Limestone with creeper panel", 10, "limestone/terrain-pistonback-lightcreeper");
            blockLimestone.carverHelper.addVariation("Limestone with dent", 11, "limestone/terrain-pistonback-lightdent");
            blockLimestone.carverHelper.addVariation("Limestone with panel", 12, "limestone/terrain-pistonback-lightemboss");
            blockLimestone.carverHelper.addVariation("Large limestone tiles", 13, "limestone/terrain-pistonback-lightfour");
            blockLimestone.carverHelper.addVariation("Limestone with light panel", 14, "limestone/terrain-pistonback-lightmarker");
            blockLimestone.carverHelper.addVariation("Limestone with dark panel", 15, "limestone/terrain-pistonback-lightpanel");
            blockLimestone.carverHelper.register(blockLimestone, "limestone");
            OreDictionary.registerOre("blockLimestone", blockLimestone);
            Carving.chisel.registerOre("limestone", "blockLimestone");

            blockLimestoneSlab = (BlockMarbleSlab) new BlockMarbleSlab(blockLimestone).setHardness(2.0F).setResistance(10F);
            blockLimestoneSlab.carverHelper.setChiselBlockName("Limestone Slab");
            blockLimestoneSlab.carverHelper.addVariation("Limestone slab", 0, "limestone");
            blockLimestoneSlab.carverHelper.addVariation("Small limestone tiles slab", 1, "limestone/terrain-cobbsmalltilelight");
            blockLimestoneSlab.carverHelper.addVariation("French limestone tiles slab", 2, "limestone/terrain-cob-frenchlight");
            blockLimestoneSlab.carverHelper.addVariation("French limestone tiles slab", 3, "limestone/terrain-cob-french2light");
            blockLimestoneSlab.carverHelper.addVariation("Creeper limestone tiles slab", 4, "limestone/terrain-cobmoss-creepdungeonlight");
            blockLimestoneSlab.carverHelper.addVariation("Small limestone bricks slab", 5, "limestone/terrain-cob-smallbricklight");
            blockLimestoneSlab.carverHelper.addVariation("Damaged limestone tiles slab", 6, "limestone/terrain-mossysmalltilelight");
            blockLimestoneSlab.carverHelper.addVariation("Smooth limestone slab", 7, "limestone/terrain-pistonback-dungeon");
            blockLimestoneSlab.carverHelper.addVariation("Limestone slab with ornate panel", 8, "limestone/terrain-pistonback-dungeonornate");
            blockLimestoneSlab.carverHelper.addVariation("Limestone slab with ornate panel", 9, "limestone/terrain-pistonback-dungeonvent");
            blockLimestoneSlab.carverHelper.addVariation("Limestone slab with creeper panel", 10, "limestone/terrain-pistonback-lightcreeper");
            blockLimestoneSlab.carverHelper.addVariation("Limestone slab with dent", 11, "limestone/terrain-pistonback-lightdent");
            blockLimestoneSlab.carverHelper.addVariation("Limestone slab with panel", 12, "limestone/terrain-pistonback-lightemboss");
            blockLimestoneSlab.carverHelper.addVariation("Large limestone tiles slab", 13, "limestone/terrain-pistonback-lightfour");
            blockLimestoneSlab.carverHelper.addVariation("Limestone slab with light panel", 14, "limestone/terrain-pistonback-lightmarker");
            blockLimestoneSlab.carverHelper.addVariation("Limestone slab with dark panel", 15, "limestone/terrain-pistonback-lightpanel");
            blockLimestoneSlab.carverHelper.register(blockLimestoneSlab, "limestoneSlab", ItemMarbleSlab.class);

            BlockMarbleStairsMaker makerLimestoneStairs = new BlockMarbleStairsMaker(blockLimestone);
            makerLimestoneStairs.carverHelper.setChiselBlockName("Limestone Stairs");
            makerLimestoneStairs.carverHelper.addVariation("Limestone stairs", 0, "limestone");
            makerLimestoneStairs.carverHelper.addVariation("Small limestone tiles stairs", 1, "limestone/terrain-cobbsmalltilelight");
            makerLimestoneStairs.carverHelper.addVariation("French limestone tiles stairs", 2, "limestone/terrain-cob-frenchlight");
            makerLimestoneStairs.carverHelper.addVariation("French limestone tiles stairs", 3, "limestone/terrain-cob-french2light");
            makerLimestoneStairs.carverHelper.addVariation("Creeper limestone tiles stairs", 4, "limestone/terrain-cobmoss-creepdungeonlight");
            makerLimestoneStairs.carverHelper.addVariation("Small limestone bricks stairs", 5, "limestone/terrain-cob-smallbricklight");
            makerLimestoneStairs.carverHelper.addVariation("Damaged limestone tiles stairs", 6, "limestone/terrain-mossysmalltilelight");
            makerLimestoneStairs.carverHelper.addVariation("Smooth limestone stairs", 7, "limestone/terrain-pistonback-dungeon");
            makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with ornate panel", 8, "limestone/terrain-pistonback-dungeonornate");
            makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with ornate panel", 9, "limestone/terrain-pistonback-dungeonvent");
            makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with creeper panel", 10, "limestone/terrain-pistonback-lightcreeper");
            makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with dent", 11, "limestone/terrain-pistonback-lightdent");
            makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with panel", 12, "limestone/terrain-pistonback-lightemboss");
            makerLimestoneStairs.carverHelper.addVariation("Large limestone tiles stairs", 13, "limestone/terrain-pistonback-lightfour");
            makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with light panel", 14, "limestone/terrain-pistonback-lightmarker");
            makerLimestoneStairs.carverHelper.addVariation("Limestone stairs with dark panel", 15, "limestone/terrain-pistonback-lightpanel");
            makerLimestoneStairs.create("chisel.limestoneStairs");
        }

        if(Chisel.featureEnabled("cobblestone"))
        {
            blockCobblestone = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("cobblestone", Blocks.cobblestone, 0, 0);
            blockCobblestone.carverHelper.addVariation("Aligned cobblestone bricks", 1, "cobblestone/terrain-cobb-brickaligned");
            blockCobblestone.carverHelper.addVariation("Detailed cobblestone bricks", 2, "cobblestone/terrain-cob-detailedbrick");
            blockCobblestone.carverHelper.addVariation("Small cobblestone bricks", 3, "cobblestone/terrain-cob-smallbrick");
            blockCobblestone.carverHelper.addVariation("Large cobblestone tiles", 4, "cobblestone/terrain-cobblargetiledark");
            blockCobblestone.carverHelper.addVariation("Small cobblestone tiles", 5, "cobblestone/terrain-cobbsmalltile");
            blockCobblestone.carverHelper.addVariation("French cobblestone tiles", 6, "cobblestone/terrain-cob-french");
            blockCobblestone.carverHelper.addVariation("French cobblestone tiles", 7, "cobblestone/terrain-cob-french2");
            blockCobblestone.carverHelper.addVariation("Creeper cobblestone tiles", 8, "cobblestone/terrain-cobmoss-creepdungeon");
            blockCobblestone.carverHelper.addVariation("Damaged cobblestone tiles", 9, "cobblestone/terrain-mossysmalltiledark");
            blockCobblestone.carverHelper.addVariation("Huge cobblestone tiles", 10, "cobblestone/terrain-pistonback-dungeontile");
            blockCobblestone.carverHelper.addVariation("Cobblestone with creeper panel", 11, "cobblestone/terrain-pistonback-darkcreeper");
            blockCobblestone.carverHelper.addVariation("Cobblestone with dent", 12, "cobblestone/terrain-pistonback-darkdent");
            blockCobblestone.carverHelper.addVariation("Cobblestone with panel", 13, "cobblestone/terrain-pistonback-darkemboss");
            blockCobblestone.carverHelper.addVariation("Cobblestone with light panel", 14, "cobblestone/terrain-pistonback-darkmarker");
            blockCobblestone.carverHelper.addVariation("Cobblestone with dark panel", 15, "cobblestone/terrain-pistonback-darkpanel");
            blockCobblestone.carverHelper.register(blockCobblestone, "cobblestone");
            Carving.chisel.registerOre("cobblestone", "blockCobble");
        }

        if(Chisel.featureEnabled("glass"))
        {
            blockGlass = (BlockCarvableGlass) new BlockCarvableGlass().setHardness(0.3F).setStepSound(Block.soundTypeGlass);
            Carving.chisel.addVariation("glass", Blocks.glass, 0, 0);
            blockGlass.carverHelper.addVariation("Bubble glass", 1, "glass/terrain-glassbubble");
            blockGlass.carverHelper.addVariation("Chinese glass", 2, "glass/terrain-glass-chinese");
            blockGlass.carverHelper.addVariation("Japanese(?) glass", 3, "glass/japanese");
            blockGlass.carverHelper.addVariation("Dungeon glass", 4, "glass/terrain-glassdungeon");
            blockGlass.carverHelper.addVariation("Light glass", 5, "glass/terrain-glasslight");
            blockGlass.carverHelper.addVariation("Borderless glass", 6, "glass/terrain-glassnoborder");
            blockGlass.carverHelper.addVariation("Ornate steel glass", 7, "glass/terrain-glass-ornatesteel");
            blockGlass.carverHelper.addVariation("Screen", 8, "glass/terrain-glass-screen");
            blockGlass.carverHelper.addVariation("Shale glass", 9, "glass/terrain-glassshale");
            blockGlass.carverHelper.addVariation("Steel frame glass", 10, "glass/terrain-glass-steelframe");
            blockGlass.carverHelper.addVariation("Stone frame glass", 11, "glass/terrain-glassstone");
            blockGlass.carverHelper.addVariation("Streak glass", 12, "glass/terrain-glassstreak");
            blockGlass.carverHelper.addVariation("Thick grid glass", 13, "glass/terrain-glass-thickgrid");
            blockGlass.carverHelper.addVariation("Thin grid glass", 14, "glass/terrain-glass-thingrid");
            blockGlass.carverHelper.addVariation("Modern Iron Fence", 15, "glass/a1-glasswindow-ironfencemodern");
            blockGlass.carverHelper.register(blockGlass, "glass");
            Carving.chisel.registerOre("glass", "blockGlass");
        }

        if(Chisel.featureEnabled("sandstone"))
        {
            blockSandstone = (BlockCarvable) new BlockCarvable().setStepSound(Block.soundTypeStone).setHardness(0.8F);
            Carving.chisel.addVariation("sandstone", Blocks.sandstone, 0, 0);
            blockSandstone.carverHelper.addVariation("Chiseled Sandstone", 1, Blocks.sandstone, 1);
            blockSandstone.carverHelper.addVariation("Smooth Sandstone", 2, Blocks.sandstone, 2);
            blockSandstone.carverHelper.addVariation("Faded sandstone", 3, "sandstone/faded");
            blockSandstone.carverHelper.addVariation("Sandstone pillar", 4, "sandstone/column");
            blockSandstone.carverHelper.addVariation("Sandstone pillar capstone", 5, "sandstone/capstone");
            blockSandstone.carverHelper.addVariation("Small sandstone pillar ", 6, "sandstone/small");
            blockSandstone.carverHelper.addVariation("Sandstone pillar base", 7, "sandstone/base");
            blockSandstone.carverHelper.addVariation("Smooth & flat sandstone", 8, "sandstone/smooth");
            blockSandstone.carverHelper.addVariation("Smooth sandstone pillar capstone", 9, "sandstone/smooth-cap");
            blockSandstone.carverHelper.addVariation("Small smooth sandstone pillar", 10, "sandstone/smooth-small");
            blockSandstone.carverHelper.addVariation("Smooth sandstone pillar base", 11, "sandstone/smooth-base");
            blockSandstone.carverHelper.addVariation("Sandstone block", 12, "sandstone/block");
            blockSandstone.carverHelper.addVariation("Small sandstone blocks", 13, "sandstone/blocks");
            blockSandstone.carverHelper.addVariation("Sandstone mosaic", 14, "sandstone/mosaic");
            blockSandstone.carverHelper.addVariation("Stacked sandstone tiles", 15, "sandstone/horizontal-tiles");
            blockSandstone.carverHelper.register(blockSandstone, "sandstone");
            Carving.chisel.registerOre("sandstone", "blockSandstone");

            if(Chisel.featureEnabled("snakeSandstone"))
            {
                blockSandSnakestone = (BlockSnakestone) new BlockSnakestone("Chisel:snakestone/sandsnake/").setBlockName("sandSnakestone");
                GameRegistry.registerBlock(blockSandSnakestone, ItemCarvable.class, blockSandSnakestone.getUnlocalizedName());
                //TODO- eat me!
                //LanguageRegistry.addName(new ItemStack(blockSandSnakestone, 1, 1), "Sandstone snake block head");
                //LanguageRegistry.addName(new ItemStack(blockSandSnakestone, 1, 13), "Sandstone snake block body");
                Carving.chisel.addVariation("sandstone", blockSandSnakestone, 1, 16);
                Carving.chisel.addVariation("sandstone", blockSandSnakestone, 13, 17);
            }
        }

        if(Chisel.featureEnabled("sandstoneScribbles"))
        {
            blockSandstoneScribbles = (BlockCarvable) new BlockCarvable().setStepSound(Block.soundTypeStone).setHardness(0.8F);
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 0, "sandstone-scribbles/scribbles-0");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 1, "sandstone-scribbles/scribbles-1");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 2, "sandstone-scribbles/scribbles-2");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 3, "sandstone-scribbles/scribbles-3");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 4, "sandstone-scribbles/scribbles-4");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 5, "sandstone-scribbles/scribbles-5");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 6, "sandstone-scribbles/scribbles-6");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 7, "sandstone-scribbles/scribbles-7");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 8, "sandstone-scribbles/scribbles-8");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 9, "sandstone-scribbles/scribbles-9");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 10, "sandstone-scribbles/scribbles-10");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 11, "sandstone-scribbles/scribbles-11");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 12, "sandstone-scribbles/scribbles-12");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 13, "sandstone-scribbles/scribbles-13");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 14, "sandstone-scribbles/scribbles-14");
            blockSandstoneScribbles.carverHelper.addVariation("Sandstone scribbles", 15, "sandstone-scribbles/scribbles-15");
            blockSandstoneScribbles.carverHelper.register(blockSandstoneScribbles, "sandstoneScribbles");
        }

        if(Chisel.featureEnabled("concrete"))
        {
            blockConcrete = (BlockConcrete) new BlockConcrete().setStepSound(Block.soundTypeStone).setHardness(0.5F);
            blockConcrete.carverHelper.addVariation("Concrete", 0, "concrete/default");
            blockConcrete.carverHelper.addVariation("Concrete block", 1, "concrete/block");
            blockConcrete.carverHelper.addVariation("Concrete double slab", 2, "concrete/doubleslab");
            blockConcrete.carverHelper.addVariation("Small concrete blocks", 3, "concrete/blocks");
            blockConcrete.carverHelper.addVariation("Weathered concrete", 4, "concrete/weathered");
            blockConcrete.carverHelper.addVariation("Weathered concrete block", 5, "concrete/weathered-block");
            blockConcrete.carverHelper.addVariation("Weathered concrete double slab", 6, "concrete/weathered-doubleslab");
            blockConcrete.carverHelper.addVariation("Small weathered blocks", 7, "concrete/weathered-blocks");
            blockConcrete.carverHelper.addVariation("Partly weathered concrete", 8, "concrete/weathered-half");
            blockConcrete.carverHelper.addVariation("Partly weathered concrete block", 9, "concrete/weathered-block-half");
            blockConcrete.carverHelper.addVariation("Asphalt", 10, "concrete/asphalt");
            blockConcrete.carverHelper.register(blockConcrete, "concrete");
            OreDictionary.registerOre("blockConcrete", blockConcrete);
            Carving.chisel.registerOre("concrete", "blockConcrete");
        }

        if(Chisel.featureEnabled("roadLine"))
        {
            blockRoadLine = (BlockRoadLine) new BlockRoadLine().setStepSound(Block.soundTypeStone).setHardness(0.01F).setBlockName("roadLine");
            GameRegistry.registerBlock(blockRoadLine, ItemCarvable.class, "roadLine");
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRoadLine, 8, 0), new Object[]{"wrw", "wrw", "wrw", ('w'), "dyeWhite", ('r'), Items.redstone}));
            //TODO- flag
            //LanguageRegistry.addName(new ItemStack(blockRoadLine, 1, 0), "Road lines");
        }

        if(Chisel.featureEnabled("ironBlock"))
        {
            blockIron = (BlockBeaconBase) new BlockBeaconBase().setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal);
            Carving.chisel.addVariation("iron", Blocks.iron_block, 0, 0);
            blockIron.carverHelper.addVariation("Large iron ingots", 1, "iron/terrain-iron-largeingot");
            blockIron.carverHelper.addVariation("Small iron ingots", 2, "iron/terrain-iron-smallingot");
            blockIron.carverHelper.addVariation("Iron gears", 3, "iron/terrain-iron-gears");
            blockIron.carverHelper.addVariation("Iron bricks", 4, "iron/terrain-iron-brick");
            blockIron.carverHelper.addVariation("Iron plates", 5, "iron/terrain-iron-plates");
            blockIron.carverHelper.addVariation("Iron plates with rivets", 6, "iron/terrain-iron-rivets");
            blockIron.carverHelper.addVariation("Iron coin stack heads up", 7, "iron/terrain-iron-coin-heads");
            blockIron.carverHelper.addVariation("Iron coin stack heads down", 8, "iron/terrain-iron-coin-tails");
            blockIron.carverHelper.addVariation("Dark iron crate", 9, "iron/terrain-iron-crate-dark");
            blockIron.carverHelper.addVariation("Light iron crate", 10, "iron/terrain-iron-crate-light");
            blockIron.carverHelper.addVariation("Iron block with moon decoration", 11, "iron/terrain-iron-moon");
            blockIron.carverHelper.addVariation("Iron moon in purple obsidian", 12, "iron/terrain-iron-space");
            blockIron.carverHelper.addVariation("Iron moon in obsidian", 13, "iron/terrain-iron-spaceblack");
            blockIron.carverHelper.addVariation("Iron vents", 14, "iron/terrain-iron-vents");
            blockIron.carverHelper.addVariation("Iron block simple", 15, "iron/terrain-iron-simple");
            blockIron.carverHelper.register(blockIron, "iron");
            Carving.chisel.registerOre("iron", "blockIron");
        }

        if(Chisel.featureEnabled("goldBlock"))
        {
            blockGold = (BlockBeaconBase) new BlockBeaconBase().setHardness(3F).setResistance(10F).setStepSound(Block.soundTypeMetal);
            Carving.chisel.addVariation("gold", Blocks.gold_block, 0, 0);
            blockGold.carverHelper.addVariation("Large golden ingots", 1, "gold/terrain-gold-largeingot");
            blockGold.carverHelper.addVariation("Small golden ingots", 2, "gold/terrain-gold-smallingot");
            blockGold.carverHelper.addVariation("Golden bricks", 3, "gold/terrain-gold-brick");
            blockGold.carverHelper.addVariation("Gold cart", 4, "gold/terrain-gold-cart");
            blockGold.carverHelper.addVariation("Golden coin stack heads up", 5, "gold/terrain-gold-coin-heads");
            blockGold.carverHelper.addVariation("Golden coin stack heads down", 6, "gold/terrain-gold-coin-tails");
            blockGold.carverHelper.addVariation("Dark gold crate", 7, "gold/terrain-gold-crate-dark");
            blockGold.carverHelper.addVariation("Light gold crate", 8, "gold/terrain-gold-crate-light");
            blockGold.carverHelper.addVariation("Golden plates", 9, "gold/terrain-gold-plates");
            blockGold.carverHelper.addVariation("Iron plates with rivets", 10, "gold/terrain-gold-rivets");
            blockGold.carverHelper.addVariation("Gold block with star decoration", 11, "gold/terrain-gold-star");
            blockGold.carverHelper.addVariation("Golden star in purple obsidian", 12, "gold/terrain-gold-space");
            blockGold.carverHelper.addVariation("Golden star in obsidian", 13, "gold/terrain-gold-spaceblack");
            blockGold.carverHelper.addVariation("Gold block simple", 14, "gold/terrain-gold-simple");
            blockGold.carverHelper.register(blockGold, "gold");
            Carving.chisel.registerOre("gold", "blockGold");
        }

        if(Chisel.featureEnabled("diamondBlock"))
        {
            blockDiamond = (BlockBeaconBase) new BlockBeaconBase().setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal);
            Carving.chisel.addVariation("diamond", Blocks.diamond_block, 0, 0);
            blockDiamond.carverHelper.addVariation("Embossed diamond block", 1, "diamond/terrain-diamond-embossed");
            blockDiamond.carverHelper.addVariation("Diamond block with panel", 2, "diamond/terrain-diamond-gem");
            blockDiamond.carverHelper.addVariation("Diamond cells", 3, "diamond/terrain-diamond-cells");
            blockDiamond.carverHelper.addVariation("Diamonds in purple obsidian", 4, "diamond/terrain-diamond-space");
            blockDiamond.carverHelper.addVariation("Diamonds in obsidian", 5, "diamond/terrain-diamond-spaceblack");
            blockDiamond.carverHelper.addVariation("Diamond block simple", 6, "diamond/terrain-diamond-simple");
            blockDiamond.carverHelper.addVariation("Bismuth", 7, "diamond/a1-blockdiamond-bismuth");
            blockDiamond.carverHelper.addVariation("Crushed diamond", 8, "diamond/a1-blockdiamond-crushed");
            blockDiamond.carverHelper.addVariation("Small diamond blocks", 9, "diamond/a1-blockdiamond-four");
            blockDiamond.carverHelper.addVariation("Small ornate diamond blocks", 10, "diamond/a1-blockdiamond-fourornate");
            blockDiamond.carverHelper.addVariation("Zelda diamond block", 11, "diamond/a1-blockdiamond-zelda");
            blockDiamond.carverHelper.addVariation("Diamond block with ornate layer", 12, "diamond/a1-blockdiamond-ornatelayer");
            blockDiamond.carverHelper.register(blockDiamond, "diamond");
            Carving.chisel.registerOre("diamond", "blockDiamond");
        }

        if(Chisel.featureEnabled("glowstone"))
        {
            blockLightstone = (BlockLightstoneCarvable) new BlockLightstoneCarvable().setHardness(0.3F).setLightLevel(1.0F).setStepSound(Block.soundTypeGlass);
            Carving.chisel.addVariation("lightstone", Blocks.glowstone, 0, 0);
            blockLightstone.carverHelper.addVariation("Cobble glowstone block", 1, "lightstone/terrain-sulphur-cobble");
            blockLightstone.carverHelper.addVariation("Corroded glowstone blocks", 2, "lightstone/terrain-sulphur-corroded");
            blockLightstone.carverHelper.addVariation("Glowstone blocks with glass", 3, "lightstone/terrain-sulphur-glass");
            blockLightstone.carverHelper.addVariation("Neon glowstone", 4, "lightstone/terrain-sulphur-neon");
            blockLightstone.carverHelper.addVariation("Ornate glowstone blocks", 5, "lightstone/terrain-sulphur-ornate");
            blockLightstone.carverHelper.addVariation("Rocky glowstone", 6, "lightstone/terrain-sulphur-rocky");
            blockLightstone.carverHelper.addVariation("Shale-shaped glowstone", 7, "lightstone/terrain-sulphur-shale");
            blockLightstone.carverHelper.addVariation("Glowstone tiles", 8, "lightstone/terrain-sulphur-tile");
            blockLightstone.carverHelper.addVariation("Fancy glowstone latern", 9, "lightstone/terrain-sulphur-weavelanternlight");
            blockLightstone.carverHelper.addVariation("Crumbling glowstone block", 10, "lightstone/a1-glowstone-cobble");
            blockLightstone.carverHelper.addVariation("Organic glowstone growth block", 11, "lightstone/a1-glowstone-growth");
            blockLightstone.carverHelper.addVariation("Glowstone layers", 12, "lightstone/a1-glowstone-layers");
            blockLightstone.carverHelper.addVariation("Corroded glowstone tiles", 13, "lightstone/a1-glowstone-tilecorroded");
            blockLightstone.carverHelper.addVariation("Glowstone bismuth", 14, "lightstone/glowstone-bismuth");
            blockLightstone.carverHelper.addVariation("Glowstone bismuth panel", 15, "lightstone/glowstone-bismuth-panel");
            blockLightstone.carverHelper.register(blockLightstone, "lightstone");
            Carving.chisel.registerOre("lightstone", "blockGlowstone");
        }

        if(Chisel.featureEnabled("lapisBlock"))
        {
            blockLapis = (BlockCarvable) new BlockCarvable().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("lapis", Blocks.lapis_block, 0, 0);
            blockLapis.carverHelper.addVariation("Chunky lapis block", 1, "lapis/terrain-lapisblock-chunky");
            blockLapis.carverHelper.addVariation("Dark lapis block", 2, "lapis/terrain-lapisblock-panel");
            blockLapis.carverHelper.addVariation("Zelda lapis block", 3, "lapis/terrain-lapisblock-zelda");
            blockLapis.carverHelper.addVariation("Ornate lapis block", 4, "lapis/terrain-lapisornate");
            blockLapis.carverHelper.addVariation("Lapis tile", 5, "lapis/terrain-lapistile");
            blockLapis.carverHelper.addVariation("Lapis panel", 6, "lapis/a1-blocklapis-panel");
            blockLapis.carverHelper.addVariation("Smooth lapis", 7, "lapis/a1-blocklapis-smooth");
            blockLapis.carverHelper.addVariation("Lapis with ornate layer", 8, "lapis/a1-blocklapis-ornatelayer");
            blockLapis.carverHelper.register(blockLapis, "lapis");
            Carving.chisel.registerOre("lapis", "blockLapis");
        }

        if(Chisel.featureEnabled("emeraldBlock"))
        {
            blockEmerald = (BlockBeaconBase) new BlockBeaconBase().setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal);
            Carving.chisel.addVariation("emerald", Blocks.emerald_block, 0, 0);
            blockEmerald.carverHelper.addVariation("Emerald panel", 1, "emerald/a1-blockemerald-emeraldpanel");
            blockEmerald.carverHelper.addVariation("Classic emerald panel", 2, "emerald/a1-blockemerald-emeraldpanelclassic");
            blockEmerald.carverHelper.addVariation("Smooth emerald", 3, "emerald/a1-blockemerald-emeraldsmooth");
            blockEmerald.carverHelper.addVariation("Emerald chunk", 4, "emerald/a1-blockemerald-emeraldchunk");
            blockEmerald.carverHelper.addVariation("Emerald with ornate layer", 5, "emerald/a1-blockemerald-emeraldornatelayer");
            blockEmerald.carverHelper.addVariation("Zelda emerald block", 6, "emerald/a1-blockemerald-emeraldzelda");
            blockEmerald.carverHelper.addVariation("Emerald cell", 7, "emerald/a1-blockquartz-cell");
            blockEmerald.carverHelper.addVariation("Emerald bismuth", 8, "emerald/a1-blockquartz-cellbismuth");
            blockEmerald.carverHelper.addVariation("Small emerald blocks", 9, "emerald/a1-blockquartz-four");
            blockEmerald.carverHelper.addVariation("Small ornate emerald blocks", 10, "emerald/a1-blockquartz-fourornate");
            blockEmerald.carverHelper.addVariation("Ornate emerald block", 11, "emerald/a1-blockquartz-ornate");
            blockEmerald.carverHelper.register(blockEmerald, "emerald");
            Carving.chisel.registerOre("emerald", "blockEmerald");
        }

        if(Chisel.featureEnabled("netherBrick"))
        {
            blockNetherBrick = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("netherBrick", Blocks.nether_brick, 0, 0);
            //blockNetherBrick.carverHelper.addVariation("Nether brick", 0, Blocks.nether_brick);
            blockNetherBrick.carverHelper.addVariation("Blue nether brick", 1, "netherbrick/a1-netherbrick-brinstar");
            blockNetherBrick.carverHelper.addVariation("Spattered nether brick", 2, "netherbrick/a1-netherbrick-classicspatter");
            blockNetherBrick.carverHelper.addVariation("Nether brick made of guts", 3, "netherbrick/a1-netherbrick-guts");
            blockNetherBrick.carverHelper.addVariation("Dark nether brick made of guts", 4, "netherbrick/a1-netherbrick-gutsdark");
            blockNetherBrick.carverHelper.addVariation("Small nether brick made of guts", 5, "netherbrick/a1-netherbrick-gutssmall");
            blockNetherBrick.carverHelper.addVariation("Blue nether brick with lava", 6, "netherbrick/a1-netherbrick-lavabrinstar");
            blockNetherBrick.carverHelper.addVariation("Brown nether brick", 7, "netherbrick/a1-netherbrick-lavabrown");
            blockNetherBrick.carverHelper.addVariation("Obsidian nether brick", 8, "netherbrick/a1-netherbrick-lavaobsidian");
            blockNetherBrick.carverHelper.addVariation("Stone nether brick", 9, "netherbrick/a1-netherbrick-lavastonedark");
            blockNetherBrick.carverHelper.addVariation("Nether brick made of meat", 10, "netherbrick/a1-netherbrick-meat");
            blockNetherBrick.carverHelper.addVariation("Red nether brick made of meat", 11, "netherbrick/a1-netherbrick-meatred");
            blockNetherBrick.carverHelper.addVariation("Small nether brick made of meat", 12, "netherbrick/a1-netherbrick-meatredsmall");
            blockNetherBrick.carverHelper.addVariation("Small red nether brick made of meat", 13, "netherbrick/a1-netherbrick-meatsmall");
            blockNetherBrick.carverHelper.addVariation("Red nether brick", 14, "netherbrick/a1-netherbrick-red");
            blockNetherBrick.carverHelper.addVariation("Small red nether brick", 15, "netherbrick/a1-netherbrick-redsmall");
            blockNetherBrick.carverHelper.register(blockNetherBrick, "netherBrick");
            Carving.chisel.registerOre("netherBrick", "netherBrick");
        }

        if(Chisel.featureEnabled("netherRack"))
        {
            blockNetherrack = (BlockCarvable) new BlockCarvable().setHardness(0.4F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("hellrock", Blocks.netherrack, 0, 0);
            blockNetherrack.carverHelper.addVariation("Nethegravel with blood", 1, "netherrack/a1-netherrack-bloodgravel");
            blockNetherrack.carverHelper.addVariation("Netherrack with blood", 2, "netherrack/a1-netherrack-bloodrock");
            blockNetherrack.carverHelper.addVariation("Darker netherrack with blood", 3, "netherrack/a1-netherrack-bloodrockgrey");
            blockNetherrack.carverHelper.addVariation("Blue netherrack", 4, "netherrack/a1-netherrack-brinstar");
            blockNetherrack.carverHelper.addVariation("Shale blue netherrack", 5, "netherrack/a1-netherrack-brinstarshale");
            blockNetherrack.carverHelper.addVariation("Classic netherrack", 6, "netherrack/a1-netherrack-classic");
            blockNetherrack.carverHelper.addVariation("Spattered netherrack", 7, "netherrack/a1-netherrack-classicspatter");
            blockNetherrack.carverHelper.addVariation("Netherrack made of guts", 8, "netherrack/a1-netherrack-guts");
            blockNetherrack.carverHelper.addVariation("Dark netherrack made of guts", 9, "netherrack/a1-netherrack-gutsdark");
            blockNetherrack.carverHelper.addVariation("Netherrack made of meat", 10, "netherrack/a1-netherrack-meat");
            blockNetherrack.carverHelper.addVariation("Red netherrack made of meat", 11, "netherrack/a1-netherrack-meatred");
            blockNetherrack.carverHelper.addVariation("Netherrack made of smaller meat chunks", 12, "netherrack/a1-netherrack-meatrock");
            blockNetherrack.carverHelper.addVariation("Dark red netherrack", 13, "netherrack/a1-netherrack-red");
            blockNetherrack.carverHelper.addVariation("Netherrack with lava flowing", 14, "netherrack/a1-netherrack-wells");
            blockNetherrack.carverHelper.register(blockNetherrack, "hellrock");
            Carving.chisel.registerOre("hellrock", "blockNetherrack");
        }

        if(Chisel.featureEnabled("cobblestoneMossy"))
        {
            blockCobblestoneMossy = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("stoneMoss", Blocks.mossy_cobblestone, 0, 0);
            blockCobblestoneMossy.carverHelper.addVariation("Aligned mossy cobblestone bricks", 1, "cobblestonemossy/terrain-cobb-brickaligned");
            blockCobblestoneMossy.carverHelper.addVariation("Detailed mossy cobblestone bricks", 2, "cobblestonemossy/terrain-cob-detailedbrick");
            blockCobblestoneMossy.carverHelper.addVariation("Small mossy cobblestone bricks", 3, "cobblestonemossy/terrain-cob-smallbrick");
            blockCobblestoneMossy.carverHelper.addVariation("Large mossy cobblestone tiles", 4, "cobblestonemossy/terrain-cobblargetiledark");
            blockCobblestoneMossy.carverHelper.addVariation("Small mossy cobblestone tiles", 5, "cobblestonemossy/terrain-cobbsmalltile");
            blockCobblestoneMossy.carverHelper.addVariation("French mossy cobblestone tiles", 6, "cobblestonemossy/terrain-cob-french");
            blockCobblestoneMossy.carverHelper.addVariation("French mossy cobblestone tiles", 7, "cobblestonemossy/terrain-cob-french2");
            blockCobblestoneMossy.carverHelper.addVariation("Creeper mossy cobblestone tiles", 8, "cobblestonemossy/terrain-cobmoss-creepdungeon");
            blockCobblestoneMossy.carverHelper.addVariation("Damaged mossy cobblestone tiles", 9, "cobblestonemossy/terrain-mossysmalltiledark");
            blockCobblestoneMossy.carverHelper.addVariation("Huge mossy cobblestone tiles", 10, "cobblestonemossy/terrain-pistonback-dungeontile");
            blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone with creeper panel", 11, "cobblestonemossy/terrain-pistonback-darkcreeper");
            blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone with dent", 12, "cobblestonemossy/terrain-pistonback-darkdent");
            blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone with panel", 13, "cobblestonemossy/terrain-pistonback-darkemboss");
            blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone with light panel", 14, "cobblestonemossy/terrain-pistonback-darkmarker");
            blockCobblestoneMossy.carverHelper.addVariation("Mossy cobblestone with dark panel", 15, "cobblestonemossy/terrain-pistonback-darkpanel");
            blockCobblestoneMossy.carverHelper.register(blockCobblestoneMossy, "stoneMoss");
            Carving.chisel.registerOre("stoneMoss", "blockCobblestoneMossy");
        }

        if(Chisel.featureEnabled("stoneBrick"))
        {
            stoneBrick = (BlockCarvable) new BlockCarvable().setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypeStone);
            for(int i = 0; i < 4; i++)
                Carving.chisel.addVariation("stoneBrick", Blocks.stonebrick, i, i);
            stoneBrick.carverHelper.addVariation("Small stone bricks", 4, "stonebrick/smallbricks");
            stoneBrick.carverHelper.addVariation("Wide stone bricks", 5, "stonebrick/largebricks");
            stoneBrick.carverHelper.addVariation("Small disordered stone bricks", 6, "stonebrick/smallchaotic");
            stoneBrick.carverHelper.addVariation("Disordered stone bricks", 7, "stonebrick/chaoticbricks");
            stoneBrick.carverHelper.addVariation("Disordered stone panels", 8, "stonebrick/chaotic");
            stoneBrick.carverHelper.addVariation("Stone bricks in a fancy arrangement", 9, "stonebrick/fancy");
            stoneBrick.carverHelper.addVariation("Ornate stone brick tiles", 10, "stonebrick/ornate");
            stoneBrick.carverHelper.addVariation("Large ornate stone brick tiles", 11, "stonebrick/largeornate");
            stoneBrick.carverHelper.addVariation("Stone panel", 12, "stonebrick/panel-hard");
            stoneBrick.carverHelper.addVariation("Sunken stone panel", 13, "stonebrick/sunken");
            stoneBrick.carverHelper.addVariation("Ornate stone panel", 14, "stonebrick/ornatepanel");
            stoneBrick.carverHelper.addVariation("Poison stone brick", 15, "stonebrick/poison");
            stoneBrick.carverHelper.register(stoneBrick, "stoneBrick");
            Carving.chisel.registerOre("stoneBrick", "blockStoneBrick");
        }

        if(Chisel.featureEnabled("snakestone"))
        {
            blockSnakestone = (BlockSnakestone) new BlockSnakestone("Chisel:snakestone/snake/").setBlockName("snakestone");
            GameRegistry.registerBlock(blockSnakestone, ItemCarvable.class, blockSnakestone.getUnlocalizedName());
            //LanguageRegistry.addName(new ItemStack(blockSnakestone, 1, 1), "Stone snake block head");
            //LanguageRegistry.addName(new ItemStack(blockSnakestone, 1, 13), "Stone snake block body");
            Carving.chisel.addVariation("stoneBrick", blockSnakestone, 1, 16);
            Carving.chisel.addVariation("stoneBrick", blockSnakestone, 13, 17);
        }

        if(Chisel.featureEnabled("dirt"))
        {
            blockDirt = (BlockCarvable) new BlockCarvable(Material.ground).setHardness(0.5F).setStepSound(Block.soundTypeGravel).setBlockName("dirt.default");
            Carving.chisel.addVariation("blockDirt", Blocks.dirt, 0, 0);
            blockDirt.carverHelper.setChiselBlockName("Dirt");
            //blockDirt.carverHelper.addVariation("Dirt", 0, Blocks.dirt);
            blockDirt.carverHelper.addVariation("Dirt bricks in disarray", 0, "dirt/bricks");
            blockDirt.carverHelper.addVariation("Dirt bricks imitating nether brick design", 1, "dirt/netherbricks");
            blockDirt.carverHelper.addVariation("Dirt bricks", 2, "dirt/bricks3");
            blockDirt.carverHelper.addVariation("Cobbledirt", 3, "dirt/cobble");
            blockDirt.carverHelper.addVariation("Reinforced cobbledirt", 4, "dirt/reinforced");
            blockDirt.carverHelper.addVariation("Reinforced dirt", 5, "dirt/dirt-reinforced");
            blockDirt.carverHelper.addVariation("Happy dirt", 6, "dirt/happy");
            blockDirt.carverHelper.addVariation("Large dirt bricks", 7, "dirt/bricks2");
            blockDirt.carverHelper.addVariation("Large dirt bricks on top of dirt", 8, "dirt/bricks+dirt2");
            blockDirt.carverHelper.addVariation("Horizontal dirt", 9, "dirt/hor");
            blockDirt.carverHelper.addVariation("Vertical dirt", 10, "dirt/vert");
            blockDirt.carverHelper.addVariation("Dirt layers", 11, "dirt/layers");
            blockDirt.carverHelper.addVariation("Crumbling dirt", 12, "dirt/vertical");
            blockDirt.carverHelper.register(blockDirt, "blockDirt");
            blockDirt.setHarvestLevel("shovel", 0);
            OreDictionary.registerOre("blockDirt", blockDirt);
            Carving.chisel.registerOre("blockDirt", "blockDirt");
        }

        if(Chisel.featureEnabled("ice"))
        {
            blockIce = (BlockMarbleIce) new BlockMarbleIce().setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass);
            Carving.chisel.addVariation("ice", Blocks.ice, 0, 0);
            blockIce.carverHelper.addVariation("Rough ice block", 1, "ice/a1-ice-light");
            blockIce.carverHelper.addVariation("Cobbleice", 2, "ice/a1-stonecobble-icecobble");
            blockIce.carverHelper.addVariation("Large rough ice bricks", 3, "ice/a1-netherbrick-ice");
            blockIce.carverHelper.addVariation("Large ice bricks", 4, "ice/a1-stonecobble-icebrick");
            blockIce.carverHelper.addVariation("Small ice bricks", 5, "ice/a1-stonecobble-icebricksmall");
            blockIce.carverHelper.addVariation("Fancy glass wall", 6, "ice/a1-stonecobble-icedungeon");
            blockIce.carverHelper.addVariation("Large ice tiles", 7, "ice/a1-stonecobble-icefour");
            blockIce.carverHelper.addVariation("Fancy ice tiles", 8, "ice/a1-stonecobble-icefrench");
            blockIce.carverHelper.addVariation("Sunken ice tiles", 9, "ice/sunkentiles");
            blockIce.carverHelper.addVariation("Disordered ice tiles", 10, "ice/tiles");
            blockIce.carverHelper.addVariation("Ice panel", 11, "ice/a1-stonecobble-icepanel");
            blockIce.carverHelper.addVariation("Ice double slab", 12, "ice/a1-stoneslab-ice");
            blockIce.carverHelper.addVariation("Ice Zelda block", 13, "ice/zelda");
            blockIce.carverHelper.addVariation("Ice Bismuth block", 14, "ice/bismuth");
            blockIce.carverHelper.addVariation("Ice Poison block", 15, "ice/poison");
            blockIce.carverHelper.register(blockIce, "ice");
            Carving.chisel.registerOre("ice", "blockIce");

            if(Chisel.featureEnabled("icePillar"))
            {
                blockIcePillar = (BlockMarbleIce) new BlockMarbleIce().setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass);
                blockIcePillar.carverHelper.setChiselBlockName("Ice Pillar");
                blockIcePillar.carverHelper.addVariation("Ice pillar", 0, "icepillar/column");
                blockIcePillar.carverHelper.addVariation("Ice pillar capstone", 1, "icepillar/capstone");
                blockIcePillar.carverHelper.addVariation("Ice pillar base", 2, "icepillar/base");
                blockIcePillar.carverHelper.addVariation("Small ice pillar", 3, "icepillar/small");
                blockIcePillar.carverHelper.addVariation("Carved ice pillar", 4, "icepillar/pillar-carved");
                blockIcePillar.carverHelper.addVariation("Ornamental ice pillar", 5, "icepillar/a1-stoneornamental-marblegreek");
                blockIcePillar.carverHelper.addVariation("Greek ice pillar", 6, "icepillar/a1-stonepillar-greek");
                blockIcePillar.carverHelper.addVariation("Plain ice pillar", 7, "icepillar/a1-stonepillar-plain");
                blockIcePillar.carverHelper.addVariation("Greek ice pillar capstone", 8, "icepillar/a1-stonepillar-greektopplain");
                blockIcePillar.carverHelper.addVariation("Plain ice pillar capstone", 9, "icepillar/a1-stonepillar-plaintopplain");
                blockIcePillar.carverHelper.addVariation("Greek ice pillar base", 10, "icepillar/a1-stonepillar-greekbottomplain");
                blockIcePillar.carverHelper.addVariation("Plain ice pillar base", 11, "icepillar/a1-stonepillar-plainbottomplain");
                blockIcePillar.carverHelper.addVariation("Greek ice pillar ornate capstone", 12, "icepillar/a1-stonepillar-greektopgreek");
                blockIcePillar.carverHelper.addVariation("Plain ice pillar ornate capstone", 13, "icepillar/a1-stonepillar-plaintopgreek");
                blockIcePillar.carverHelper.addVariation("Greek ice pillar ornate base", 14, "icepillar/a1-stonepillar-greekbottomgreek");
                blockIcePillar.carverHelper.addVariation("Plain ice pillar ornate base", 15, "icepillar/a1-stonepillar-plainbottomgreek");
                blockIcePillar.carverHelper.register(blockIcePillar, "icePillar");
                Carving.chisel.setGroupClass("icePillar", "ice");
            }

            if(Chisel.featureEnabled("iceStairs"))
            {
                BlockMarbleStairsMaker makerIceStairs = new BlockMarbleStairsMaker(Blocks.ice);
                makerIceStairs.carverHelper.setChiselBlockName("Ice Stairs");
                makerIceStairs.carverHelper.addVariation("Ice stairs", 0, Blocks.ice);
                makerIceStairs.carverHelper.addVariation("Rough ice stairs", 1, "ice/a1-ice-light");
                makerIceStairs.carverHelper.addVariation("Cobbleice stairs", 2, "ice/a1-stonecobble-icecobble");
                makerIceStairs.carverHelper.addVariation("Large rough ice brick stairs", 3, "ice/a1-netherbrick-ice");
                makerIceStairs.carverHelper.addVariation("Large ice brick stairs", 4, "ice/a1-stonecobble-icebrick");
                makerIceStairs.carverHelper.addVariation("Small ice brick stairs", 5, "ice/a1-stonecobble-icebricksmall");
                makerIceStairs.carverHelper.addVariation("Fancy ice wall stairs", 6, "ice/a1-stonecobble-icedungeon");
                makerIceStairs.carverHelper.addVariation("Large ice tile stairs", 7, "ice/a1-stonecobble-icefour");
                makerIceStairs.carverHelper.addVariation("Fancy ice tile stairs", 8, "ice/a1-stonecobble-icefrench");
                makerIceStairs.carverHelper.addVariation("Sunken ice tile stairs", 9, "ice/sunkentiles");
                makerIceStairs.carverHelper.addVariation("Disordered ice tile stairs", 10, "ice/tiles");
                makerIceStairs.carverHelper.addVariation("Ice panel stairs", 11, "ice/a1-stonecobble-icepanel");
                makerIceStairs.carverHelper.addVariation("Ice double slab stairs", 12, "ice/a1-stoneslab-ice");
                makerIceStairs.carverHelper.addVariation("Ice Zelda stairs", 13, "ice/zelda");
                makerIceStairs.carverHelper.addVariation("Ice bismuth stairs", 14, "ice/bismuth");
                makerIceStairs.carverHelper.addVariation("Ice poison stairs", 15, "ice/poison");
                makerIceStairs.create(new BlockMarbleStairsMakerCreator()
                {
                    @Override
                    public BlockMarbleStairs create(Block block, int meta, CarvableHelper helper)
                    {
                        return new BlockMarbleIceStairs(block, meta, helper);
                    }
                }, "chisel.iceStairs");
            }
        }

        if(Chisel.featureEnabled("wood"))
        {
            String[] plank_names = {"oak", "spruce", "birch", "jungle", "acacia", "dark-oak"};
            String[] plank_ucnames = {"Oak", "Spruce", "Birch", "Jungle", "Acacia", "Dark Oak"};
            for(int i = 0; i < 6; i++)
            {
                String n = plank_names[i];
                String u = plank_ucnames[i];

                blockPlanks[i] = (BlockCarvable) (new BlockCarvable()).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood);
                blockPlanks[i].carverHelper.setChiselBlockName(u + " Wood Planks");
                blockPlanks[i].carverHelper.addVariation("Smooth " + n + " wood planks", 1, "planks-" + n + "/clean");
                blockPlanks[i].carverHelper.addVariation("Short " + n + " wood planks", 2, "planks-" + n + "/short");
                blockPlanks[i].carverHelper.addVariation("Fancy " + n + " wood plank arrangement", 6, "planks-" + n + "/fancy");
                blockPlanks[i].carverHelper.addVariation(u + " wood panel", 8, "planks-" + n + "/panel-nails");
                blockPlanks[i].carverHelper.addVariation(u + " wood double slab", 9, "planks-" + n + "/double");
                blockPlanks[i].carverHelper.addVariation(u + " wood crate", 10, "planks-" + n + "/crate");
                blockPlanks[i].carverHelper.addVariation("Fancy " + n + " wood crate", 11, "planks-" + n + "/crate-fancy");
                blockPlanks[i].carverHelper.addVariation("Large long " + n + " wood planks", 13, "planks-" + n + "/large");
                if(i < 4)
                { // TODO: We lack textures for these!
                    blockPlanks[i].carverHelper.addVariation("Vertical " + n + " wood planks", 3, "planks-" + n + "/vertical");
                    blockPlanks[i].carverHelper.addVariation("Vertical uneven " + n + " wood planks", 4, "planks-" + n + "/vertical-uneven");
                    blockPlanks[i].carverHelper.addVariation(u + " wood parquet", 5, "planks-" + n + "/parquet");
                    blockPlanks[i].carverHelper.addVariation(u + " wood plank blinds", 7, "planks-" + n + "/blinds");
                    blockPlanks[i].carverHelper.addVariation(u + " wood scaffold", 12, "planks-" + n + "/crateex");
                    blockPlanks[i].carverHelper.addVariation(u + " wood planks in disarray", 14, "planks-" + n + "/chaotic-hor");
                    blockPlanks[i].carverHelper.addVariation("Vertical " + n + " wood planks in disarray", 15, "planks-" + n + "/chaotic");
                }
                blockPlanks[i].carverHelper.register(blockPlanks[i], "wood-" + n);
                Carving.chisel.addVariation("wood-" + n, Blocks.planks, i, 0);
                Blocks.planks.setHarvestLevel("chisel", 0, i);
                blockPlanks[i].setHarvestLevel("axe", 0);

                Carving.chisel.setVariationSound("wood-" + n, "chisel:chisel-wood");
            }
        }

        if(Chisel.featureEnabled("obsidian"))
        {
            blockObsidian = (BlockCarvable) new BlockCarvable().setHardness(50.0F).setResistance(2000.0F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("obsidian", Blocks.obsidian, 0, 0);
            blockObsidian.carverHelper.addVariation("Large obsidian pillar", 1, "obsidian/pillar");
            blockObsidian.carverHelper.addVariation("Obsidian pillar", 2, "obsidian/pillar-quartz");
            blockObsidian.carverHelper.addVariation("Chiseled obsidian", 3, "obsidian/chiseled");
            blockObsidian.carverHelper.addVariation("Shiny obsidian panel", 4, "obsidian/panel-shiny");
            blockObsidian.carverHelper.addVariation("Obsidian panel", 5, "obsidian/panel");
            blockObsidian.carverHelper.addVariation("Organic-looking obsidian chunks", 6, "obsidian/chunks");
            blockObsidian.carverHelper.addVariation("Organic-looking obsidian growth", 7, "obsidian/growth");
            blockObsidian.carverHelper.addVariation("Obsidian crystal", 8, "obsidian/crystal");
            blockObsidian.carverHelper.addVariation("Obsidian panel with an ancient map on it", 9, "obsidian/map-a");
            blockObsidian.carverHelper.addVariation("Obsidian panel with a map of some weird region on it", 10, "obsidian/map-b");
            blockObsidian.carverHelper.addVariation("Bright obsidian panel", 11, "obsidian/panel-light");
            blockObsidian.carverHelper.addVariation("Obsidian blocks", 12, "obsidian/blocks");
            blockObsidian.carverHelper.addVariation("Obsidian tiles", 13, "obsidian/tiles");
            blockObsidian.carverHelper.addVariation("Light obsidian blocks with Greek decor", 14, "obsidian/greek");
            blockObsidian.carverHelper.addVariation("Small obsidian blocks inside an oak wood crate", 15, "obsidian/crate");
            blockObsidian.carverHelper.register(blockObsidian, "obsidian");
            Carving.chisel.registerOre("obsidian", "blockObsidian");
        }

        if(Chisel.featureEnabled("snakestoneObsidian"))
        {
            blockObsidianSnakestone = (BlockSnakestoneObsidian) new BlockSnakestoneObsidian("Chisel:snakestone/obsidian/").setBlockName("obsidianSnakestone").setHardness(50.0F).setResistance(2000.0F);
            GameRegistry.registerBlock(blockObsidianSnakestone, ItemCarvable.class, blockObsidianSnakestone.getUnlocalizedName());
            //LanguageRegistry.addName(new ItemStack(blockObsidianSnakestone, 1, 1), "Obsidian snakestone head");
            //LanguageRegistry.addName(new ItemStack(blockObsidianSnakestone, 1, 13), "Obsidian snakestone body");
            Carving.chisel.addVariation("obsidian", blockObsidianSnakestone, 1, 16);
            Carving.chisel.addVariation("obsidian", blockObsidianSnakestone, 13, 17);
        }

        if(Chisel.featureEnabled("ironBars"))
        {
            blockPaneIron = (BlockCarvablePane) new BlockCarvablePane(Material.iron, true).setHardness(0.3F).setStepSound(Block.soundTypeMetal);
            Carving.chisel.addVariation("fenceIron", Blocks.iron_bars, 0, 0);
            blockPaneIron.carverHelper.addVariation("Iron bars without frame", 1, "ironpane/fenceIron");
            blockPaneIron.carverHelper.addVariation("Menacing iron bars", 2, "ironpane/barbedwire");
            blockPaneIron.carverHelper.addVariation("Iron cage bars", 3, "ironpane/cage");
            blockPaneIron.carverHelper.addVariation("Menacing iron spikes", 4, "ironpane/fenceIronTop");
            blockPaneIron.carverHelper.addVariation("Thick iron grid", 5, "ironpane/terrain-glass-thickgrid");
            blockPaneIron.carverHelper.addVariation("Thin iron grid", 6, "ironpane/terrain-glass-thingrid");
            blockPaneIron.carverHelper.addVariation("Ornate iron pane fence", 7, "ironpane/terrain-glass-ornatesteel");
            blockPaneIron.carverHelper.addVariation("Vertical iron bars", 8, "ironpane/bars");
            blockPaneIron.carverHelper.addVariation("Iron spikes", 9, "ironpane/spikes");
            blockPaneIron.carverHelper.register(blockPaneIron, "fenceIron");
        }

        if(Chisel.featureEnabled("glassPane"))
        {
            blockPaneGlass = (BlockCarvablePane) new BlockCarvablePane(Material.glass, false).setHardness(0.3F).setStepSound(Block.soundTypeGlass);
            Carving.chisel.addVariation("glassPane", Blocks.glass_pane, 0, 0);
            blockPaneGlass.carverHelper.addVariation("Bubble glass pane", 1, "glasspane/terrain-glassbubble");
            blockPaneGlass.carverHelper.addVariation("Borderless glass pane", 2, "glasspane/terrain-glassnoborder");
            blockPaneGlass.carverHelper.addVariation("Screen pane", 3, "glasspane/terrain-glass-screen");
            blockPaneGlass.carverHelper.addVariation("Streak glass pane", 4, "glasspane/terrain-glassstreak");
            blockPaneGlass.carverHelper.addVariation("Chinese glass pane", 12, "glasspane/chinese");
            blockPaneGlass.carverHelper.addVariation("Chinese glass pane with golden frame", 13, "glasspane/chinese2");
            blockPaneGlass.carverHelper.addVariation("Japanese glass pane", 14, "glasspane/japanese");
            blockPaneGlass.carverHelper.addVariation("Ornate japanese glass pane", 15, "glasspane/japanese2");
            blockPaneGlass.carverHelper.register(blockPaneGlass, "glassPane");
        }

        if(Chisel.featureEnabled("redstoneBlock"))
        {
            blockRedstone = (BlockCarvablePowered) (new BlockCarvablePowered(Material.iron)).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal);
            Carving.chisel.addVariation("blockRedstone", Blocks.redstone_block, 0, 0);
            blockRedstone.carverHelper.addVariation("Smooth redstone", 1, "redstone/smooth");
            blockRedstone.carverHelper.addVariation("Large redstone block", 2, "redstone/block");
            blockRedstone.carverHelper.addVariation("Small redstone blocks", 3, "redstone/blocks");
            blockRedstone.carverHelper.addVariation("Redstone bricks", 4, "redstone/bricks");
            blockRedstone.carverHelper.addVariation("Small redstone bricks", 5, "redstone/smallbricks");
            blockRedstone.carverHelper.addVariation("Chaotic redstone bricks", 6, "redstone/smallchaotic");
            blockRedstone.carverHelper.addVariation("Chiseled redstone", 7, "redstone/chiseled");
            blockRedstone.carverHelper.addVariation("Redstone Greek decoration", 8, "redstone/ere");
            blockRedstone.carverHelper.addVariation("Ornate redstone tiles", 9, "redstone/ornate-tiles");
            blockRedstone.carverHelper.addVariation("Redstone pillar", 10, "redstone/pillar");
            blockRedstone.carverHelper.addVariation("Redstone tiles", 11, "redstone/tiles");
            blockRedstone.carverHelper.addVariation("Redstone circuit", 12, "redstone/circuit");
            blockRedstone.carverHelper.addVariation("Redstone supaplex circuit", 13, "redstone/supaplex");
            blockRedstone.carverHelper.addVariation("Redstone skulls", 14, "redstone/a1-blockredstone-skullred");
            blockRedstone.carverHelper.addVariation("Redstone Zelda block", 15, "redstone/a1-blockredstone-redstonezelda");
            blockRedstone.carverHelper.register(blockRedstone, "blockRedstone");
            Carving.chisel.registerOre("blockRedstone", "blockRedstone");
        }

        if(Chisel.featureEnabled("holystone"))
        {
            blockHolystone = (BlockHolystone) new BlockHolystone(Material.rock).setHardness(2.0F).setResistance(10F).setStepSound(Chisel.soundHolystoneFootstep);
            blockHolystone.carverHelper.addVariation("Holystone", 0, "holystone/holystone");
            blockHolystone.carverHelper.addVariation("Smooth holystone", 1, "holystone/smooth");
            blockHolystone.carverHelper.addVariation("Mysterious holystone symbol", 2, "holystone/love");
            blockHolystone.carverHelper.addVariation("Chiseled holystone", 3, "holystone/chiseled");
            blockHolystone.carverHelper.addVariation("Holystone blocks", 4, "holystone/blocks");
            blockHolystone.carverHelper.addVariation("Rough holystone blocks", 5, "holystone/blocks-rough");
            blockHolystone.carverHelper.addVariation("Holystone bricks", 6, "holystone/brick");
            blockHolystone.carverHelper.addVariation("Large holystone bricks", 7, "holystone/largebricks");
            blockHolystone.carverHelper.addVariation("Holystone platform", 8, "holystone/platform");
            blockHolystone.carverHelper.addVariation("Holystone platform tiles", 9, "holystone/platform-tiles");
            blockHolystone.carverHelper.addVariation("Fancy holystone construction", 10, "holystone/construction");
            blockHolystone.carverHelper.addVariation("Fancy holystone tiles", 11, "holystone/fancy-tiles");
            blockHolystone.carverHelper.addVariation("Smooth holystone plate", 12, "holystone/plate");
            blockHolystone.carverHelper.addVariation("Holystone plate", 13, "holystone/plate-rough");
            blockHolystone.carverHelper.register(blockHolystone, "blockHolystone");
            OreDictionary.registerOre("blockHolystone", blockHolystone);
            Carving.chisel.registerOre("blockHolystone", "blockHolystone");
        }

        if(Chisel.featureEnabled("lavastone"))
        {
            blockLavastone = (BlockLavastone) new BlockLavastone(Material.rock, "lava_flow").setHardness(2.0F).setResistance(10F);
            blockLavastone.carverHelper.addVariation("Lavastone", 0, "lavastone/cobble");
            blockLavastone.carverHelper.addVariation("Black lavastone", 1, "lavastone/black");
            blockLavastone.carverHelper.addVariation("Lavastone tiles", 2, "lavastone/tiles");
            blockLavastone.carverHelper.addVariation("Chaotic lavastone bricks", 3, "lavastone/chaotic");
            blockLavastone.carverHelper.addVariation("Lava creeper in tiles", 4, "lavastone/creeper");
            blockLavastone.carverHelper.addVariation("Lava panel", 5, "lavastone/panel");
            blockLavastone.carverHelper.addVariation("Ornate lava panel", 6, "lavastone/panel-ornate");
            blockLavastone.carverHelper.register(blockLavastone, "blockLavastone");
            OreDictionary.registerOre("blockLavastone", blockLavastone);
            Carving.chisel.registerOre("blockLavastone", "blockLavastone");
        }

        if(Chisel.featureEnabled("fantasy"))
        {
            blockFft = (BlockCarvable) new BlockCarvable(Material.rock).setHardness(2.0F).setResistance(10F);
            blockFft.carverHelper.setChiselBlockName("Fantasy Block");
            blockFft.carverHelper.addVariation("Fantasy brick", 0, "fft/brick");
            blockFft.carverHelper.addVariation("Faded fantasy brick", 1, "fft/brick-faded");
            blockFft.carverHelper.addVariation("Weared fantasy brick", 2, "fft/brick-wear");
            blockFft.carverHelper.addVariation("Damaged fantasy bricks", 3, "fft/bricks");
            blockFft.carverHelper.addVariation("Fantasy decoration", 4, "fft/decor");
            blockFft.carverHelper.addVariation("Fantasy decoration block", 5, "fft/decor-block");
            blockFft.carverHelper.addVariation("Fantasy pillar", 6, "fft/pillar");
            blockFft.carverHelper.addVariation("Fantasy pillar decoration", 7, "fft/pillar-decorated");
            blockFft.carverHelper.addVariation("Fantasy gold snake decoration", 8, "fft/gold-decor-1");
            blockFft.carverHelper.addVariation("Fantasy gold noise decoration", 9, "fft/gold-decor-2");
            blockFft.carverHelper.addVariation("Fantasy gold engravings decoration", 10, "fft/gold-decor-3");
            blockFft.carverHelper.addVariation("Fantasy gold chains decoration", 11, "fft/gold-decor-4");
            blockFft.carverHelper.addVariation("Fantasy plate decoration", 12, "fft/plate");
            blockFft.carverHelper.addVariation("Fantasy block", 13, "fft/block");
            blockFft.carverHelper.addVariation("Fantasy bricks in disarray", 14, "fft/bricks-chaotic");
            blockFft.carverHelper.addVariation("Weared fantasy bricks", 15, "fft/bricks-wear");
            blockFft.carverHelper.register(blockFft, "blockFft");
            OreDictionary.registerOre("blockFft", blockFft);
            Carving.chisel.registerOre("blockFft", "blockFft");
        }

        if(Chisel.featureEnabled("carpet"))
        {
            blockCarpet = (BlockCarvable) new BlockCarvable(Material.cloth).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeCloth);
            blockCarpet.carverHelper.setChiselBlockName("Carpet Block");
            blockCarpet.carverHelper.addVariation("White carpet block", 0, "carpet/white");
            blockCarpet.carverHelper.addVariation("Orange carpet block", 1, "carpet/orange");
            blockCarpet.carverHelper.addVariation("Magenta carpet block", 2, "carpet/lily");
            blockCarpet.carverHelper.addVariation("Light blue carpet block", 3, "carpet/lightblue");
            blockCarpet.carverHelper.addVariation("Yellow carpet block", 4, "carpet/yellow");
            blockCarpet.carverHelper.addVariation("Light green carpet block", 5, "carpet/lightgreen");
            blockCarpet.carverHelper.addVariation("Pink carpet block", 6, "carpet/pink");
            blockCarpet.carverHelper.addVariation("Dark grey carpet block", 7, "carpet/darkgrey");
            blockCarpet.carverHelper.addVariation("Grey carpet block", 8, "carpet/grey");
            blockCarpet.carverHelper.addVariation("Teal carpet block", 9, "carpet/teal");
            blockCarpet.carverHelper.addVariation("Purple carpet block", 10, "carpet/purple");
            blockCarpet.carverHelper.addVariation("Dark blue carpet block", 11, "carpet/darkblue");
            blockCarpet.carverHelper.addVariation("Brown carpet block", 12, "carpet/brown");
            blockCarpet.carverHelper.addVariation("Green carpet block", 13, "carpet/green");
            blockCarpet.carverHelper.addVariation("Red carpet block", 14, "carpet/red");
            blockCarpet.carverHelper.addVariation("Black carpet block", 15, "carpet/black");
            blockCarpet.carverHelper.forbidChiseling = true;
            blockCarpet.carverHelper.register(blockCarpet, "blockCarpet");
            OreDictionary.registerOre("blockCarpet", blockCarpet);
            Carving.chisel.registerOre("blockCarpet", "blockCarpet");
        }

        if(Chisel.featureEnabled("carpetFloor"))
        {
            blockCarpetFloor = (BlockMarbleCarpet) new BlockMarbleCarpet(Material.cloth).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeCloth);
            blockCarpetFloor.carverHelper.setChiselBlockName("Carpet");
            blockCarpetFloor.carverHelper.addVariation("White carpet", 0, "carpet/white");
            blockCarpetFloor.carverHelper.addVariation("Orange carpet", 1, "carpet/orange");
            blockCarpetFloor.carverHelper.addVariation("Magenta carpet", 2, "carpet/lily");
            blockCarpetFloor.carverHelper.addVariation("Light blue carpet", 3, "carpet/lightblue");
            blockCarpetFloor.carverHelper.addVariation("Yellow carpet", 4, "carpet/yellow");
            blockCarpetFloor.carverHelper.addVariation("Light green carpet", 5, "carpet/lightgreen");
            blockCarpetFloor.carverHelper.addVariation("Pink carpet", 6, "carpet/pink");
            blockCarpetFloor.carverHelper.addVariation("Dark grey carpet", 7, "carpet/darkgrey");
            blockCarpetFloor.carverHelper.addVariation("Grey carpet", 8, "carpet/grey");
            blockCarpetFloor.carverHelper.addVariation("Teal carpet", 9, "carpet/teal");
            blockCarpetFloor.carverHelper.addVariation("Purple carpet", 10, "carpet/purple");
            blockCarpetFloor.carverHelper.addVariation("Dark blue carpet", 11, "carpet/darkblue");
            blockCarpetFloor.carverHelper.addVariation("Brown carpet", 12, "carpet/brown");
            blockCarpetFloor.carverHelper.addVariation("Green carpet", 13, "carpet/green");
            blockCarpetFloor.carverHelper.addVariation("Red carpet", 14, "carpet/red");
            blockCarpetFloor.carverHelper.addVariation("Black carpet", 15, "carpet/black");
            blockCarpetFloor.carverHelper.forbidChiseling = true;
            blockCarpetFloor.carverHelper.register(blockCarpetFloor, "blockCarpetFloor");

            for(int i = 0; i < 16; i++)
            {
                String group = "carpet." + i;

                Carving.needle.addVariation(group, Blocks.carpet, i, 0);
                Carving.needle.addVariation(group, blockCarpetFloor, i, 2);
                Carving.needle.addVariation(group, blockCarpet, i, 1);
            }
        }

        if(Chisel.featureEnabled("bookshelf"))
        {
            blockBookshelf = (BlockCarvable) new BlockMarbleBookshelf().setHardness(1.5F).setStepSound(Block.soundTypeWood);
            Carving.chisel.addVariation("blockBookshelf", Blocks.bookshelf, 0, 0);
            blockBookshelf.carverHelper.addVariation("Bookshelf with rainbow colored books", 1, "bookshelf/rainbow");
            blockBookshelf.carverHelper.addVariation("Necromancer novice's bookshelf", 2, "bookshelf/necromancer-novice");
            blockBookshelf.carverHelper.addVariation("Necromancer's bookshelf", 3, "bookshelf/necromancer");
            blockBookshelf.carverHelper.addVariation("Bookshelf with red tomes", 4, "bookshelf/redtomes");
            blockBookshelf.carverHelper.addVariation("Abandoned bookshelf", 5, "bookshelf/abandoned");
            blockBookshelf.carverHelper.addVariation("Hoarder's bookshelf", 6, "bookshelf/hoarder");
            blockBookshelf.carverHelper.addVariation("Bookshelf filled to brim with boring pastel books", 7, "bookshelf/brim");
            blockBookshelf.carverHelper.addVariation("Historician's bookshelf", 8, "bookshelf/historician");
            blockBookshelf.carverHelper.register(blockBookshelf, "blockBookshelf");
            blockBookshelf.setHarvestLevel("axe", 0);
            Carving.chisel.registerOre("blockBookshelf", "blockBookshelf");
        }

        if(Chisel.featureEnabled("futuristicArmorPlating"))
        {
            blockTyrian = (BlockCarvable) new BlockCarvable(Material.iron).setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal);
            blockTyrian.carverHelper.setChiselBlockName("Futuristic Armor Plating Block");
            blockTyrian.carverHelper.addVariation("Futuristic armor plating block", 0, "tyrian/shining");
            blockTyrian.carverHelper.addVariation("Bleak futuristic armor plating block", 1, "tyrian/tyrian");
            blockTyrian.carverHelper.addVariation("Purple futuristic armor plating block", 2, "tyrian/chaotic");
            blockTyrian.carverHelper.addVariation("Faded purple futuristic armor plating block", 3, "tyrian/softplate");
            blockTyrian.carverHelper.addVariation("Rusted futuristic armor plating block", 4, "tyrian/rust");
            blockTyrian.carverHelper.addVariation("Elaborate futuristic armor plating block", 5, "tyrian/elaborate");
            blockTyrian.carverHelper.addVariation("Futuristic armor plating block with many seams", 6, "tyrian/routes");
            blockTyrian.carverHelper.addVariation("Futuristic platform block", 7, "tyrian/platform");
            blockTyrian.carverHelper.addVariation("Futuristic armor plating tiles", 8, "tyrian/platetiles");
            blockTyrian.carverHelper.addVariation("Diagonal futuristic armor plating block", 9, "tyrian/diagonal");
            blockTyrian.carverHelper.addVariation("Futuristic armor plating block with dent", 10, "tyrian/dent");
            blockTyrian.carverHelper.addVariation("Blue futuristic armor plating block", 11, "tyrian/blueplating");
            blockTyrian.carverHelper.addVariation("Black futuristic armor plating block", 12, "tyrian/black");
            blockTyrian.carverHelper.addVariation("Black futuristic armor plating tiles", 13, "tyrian/black2");
            blockTyrian.carverHelper.addVariation("Black futuristic armor plating block with an opening", 14, "tyrian/opening");
            blockTyrian.carverHelper.addVariation("Futuristic armor plating with shining metal bits", 15, "tyrian/plate");
            blockTyrian.carverHelper.register(blockTyrian, "blockTyrian");
            OreDictionary.registerOre("blockTyrian", blockTyrian);
            Carving.chisel.registerOre("blockTyrian", "blockTyrian");
        }



        if(Chisel.featureEnabled("templeBlock"))
        {
            blockTemple = (BlockCarvable) new BlockEldritch().setHardness(2.0F).setResistance(10F).setStepSound(Chisel.soundTempleFootstep);
            blockTemple.carverHelper.setChiselBlockName("Temple Block");
            blockTemple.carverHelper.addVariation("Temple cobblestone", 0, "temple/cobble");
            blockTemple.carverHelper.addVariation("Orante temple block", 1, "temple/ornate");
            blockTemple.carverHelper.addVariation("Temple plate", 2, "temple/plate");
            blockTemple.carverHelper.addVariation("Cracked temple plate", 3, "temple/plate-cracked");
            blockTemple.carverHelper.addVariation("Temple bricks", 4, "temple/bricks");
            blockTemple.carverHelper.addVariation("Large temple bricks", 5, "temple/bricks-large");
            blockTemple.carverHelper.addVariation("Weared temple bricks", 6, "temple/bricks-weared");
            blockTemple.carverHelper.addVariation("Temple bricks in disarray", 7, "temple/bricks-disarray");
            blockTemple.carverHelper.addVariation("Temple column", 8, "temple/column");
            blockTemple.carverHelper.addVariation("Temple stand", 9, "temple/stand");
            blockTemple.carverHelper.addVariation("Temple mosaic stand", 10, "temple/stand-mosaic");
            blockTemple.carverHelper.addVariation("Temple creeper stand", 11, "temple/stand-creeper");
            blockTemple.carverHelper.addVariation("Temple tiles", 12, "temple/tiles");
            blockTemple.carverHelper.addVariation("Small temple tiles", 13, "temple/smalltiles");
            blockTemple.carverHelper.addVariation("Light temple tiles", 14, "temple/tiles-light");
            blockTemple.carverHelper.addVariation("Small light temple tiles", 15, "temple/smalltiles-light");
            blockTemple.carverHelper.register(blockTemple, "blockTemple");

            if(Chisel.featureEnabled("templeBlockMossy"))
            {
                blockTempleMossy = (BlockCarvable) new BlockEldritch().setHardness(2.0F).setResistance(10F).setStepSound(Chisel.soundTempleFootstep);
                blockTempleMossy.carverHelper.setChiselBlockName("Mossy Temple Block");
                blockTempleMossy.carverHelper.addVariation("Mossy temple cobblestone", 0, "templemossy/cobble");
                blockTempleMossy.carverHelper.addVariation("Orante mossy temple block", 1, "templemossy/ornate");
                blockTempleMossy.carverHelper.addVariation("Mossy temple plate", 2, "templemossy/plate");
                blockTempleMossy.carverHelper.addVariation("Cracked mossy temple plate", 3, "templemossy/plate-cracked");
                blockTempleMossy.carverHelper.addVariation("Mossy temple bricks", 4, "templemossy/bricks");
                blockTempleMossy.carverHelper.addVariation("Large mossy temple bricks", 5, "templemossy/bricks-large");
                blockTempleMossy.carverHelper.addVariation("Weared mossy temple bricks", 6, "templemossy/bricks-weared");
                blockTempleMossy.carverHelper.addVariation("Mossy temple bricks in disarray", 7, "templemossy/bricks-disarray");
                blockTempleMossy.carverHelper.addVariation("Mossy temple column", 8, "templemossy/column");
                blockTempleMossy.carverHelper.addVariation("Mossy temple stand", 9, "templemossy/stand");
                blockTempleMossy.carverHelper.addVariation("Mossy temple mosaic stand", 10, "templemossy/stand-mosaic");
                blockTempleMossy.carverHelper.addVariation("Mossy temple creeper stand", 11, "templemossy/stand-creeper");
                blockTempleMossy.carverHelper.addVariation("Mossy temple tiles", 12, "templemossy/tiles");
                blockTempleMossy.carverHelper.addVariation("Small mossy temple tiles", 13, "templemossy/smalltiles");
                blockTempleMossy.carverHelper.addVariation("Light mossy temple tiles", 14, "templemossy/tiles-light");
                blockTempleMossy.carverHelper.addVariation("Small light mossy  temple tiles", 15, "templemossy/smalltiles-light");
                blockTempleMossy.carverHelper.register(blockTempleMossy, "blockTempleMossy");
            }
        }

        if(Chisel.featureEnabled("cloud"))
        {
            blockCloud = (BlockCloud) new BlockCloud().setHardness(0.2F).setLightOpacity(3).setStepSound(Block.soundTypeCloth);
            blockCloud.carverHelper.addVariation("Cloud block", 0, "cloud/cloud");
            blockCloud.carverHelper.register(blockCloud, "blockCloud");
            OreDictionary.registerOre("blockCloud", blockCloud);
            Carving.chisel.registerOre("blockCloud", "blockCloud");
        }

        if(Chisel.featureEnabled("factory"))
        {
            blockFactory = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Chisel.soundMetalFootstep);
            blockFactory.carverHelper.setChiselBlockName("blockFactory");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.0.desc"), 0, "factory/dots");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.1.desc"), 1, "factory/rust2");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.2.desc"), 2, "factory/rust");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.3.desc"), 3, "factory/platex");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.4.desc"), 4, "factory/wireframewhite");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.5.desc"), 5, "factory/wireframe");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.6.desc"), 6, "factory/hazard");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.7.desc"), 7, "factory/hazardorange");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.8.desc"), 8, "factory/circuit");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.9.desc"), 9, "factory/metalbox");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.10.desc"), 10, "factory/goldplate");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.11.desc"), 11, "factory/goldplating");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.12.desc"), 12, "factory/grinder");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.13.desc"), 13, "factory/plating");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.14.desc"), 14, "factory/rustplates");
            blockFactory.carverHelper.addVariation(I18n.format("tile.blockFactory.15.desc"), 15, "factory/column");
            blockFactory.carverHelper.register(blockFactory, "blockFactory");
        }

        // 1.7! Let's go! Let's go-go!

        String[] sGNames = new String[]{
                "White", "Orange", "Magenta", "Light Blue",
                "Yellow", "Lime", "Pink", "Gray",
                "Light Gray", "Cyan", "Purple", "Blue",
                "Brown", "Green", "Red", "Black"
        };

        if(Chisel.featureEnabled("glassStained")) for(int i = 0; i < 16; i++)
        {
            String blockName = "chisel.stainedGlass" + sGNames[i].replaceAll(" ", "");
            String oreName = "stainedGlass" + sGNames[i].replaceAll(" ", "");
            String texName = "glassdyed/" + sGNames[i].toLowerCase().replaceAll(" ", "") + "-";
            int glassPrefix = (i & 3) << 2;
            int glassId = i >> 2;
            if(glassPrefix == 0)
            {
                blockStainedGlass[glassId] = (BlockCarvableGlass) new BlockCarvableGlass().setStained(true).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setBlockName("Stained Glass");
                blockStainedGlass[glassId].carverHelper.registerBlock(blockStainedGlass[glassId], blockName);
                blockStainedGlass[glassId].carverHelper.blockName = "Stained Glass";
            }
            blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " bubble glass", glassPrefix + 0, texName + "bubble");
            blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " glass panel", glassPrefix + 1, texName + "panel");
            blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " fancy glass panel", glassPrefix + 2, texName + "panel-fancy");
            blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " borderless glass", glassPrefix + 3, texName + "transparent");
            OreDictionary.registerOre(oreName, new ItemStack(Blocks.stained_glass, 1, i));
            Carving.chisel.registerOre(blockName, oreName);
            for(CarvableVariation cv : blockStainedGlass[glassId].carverHelper.variations)
            {
                if(cv.metadata < glassPrefix || cv.metadata >= glassPrefix + 4) continue;
                blockStainedGlass[glassId].carverHelper.registerVariation(blockName, cv, blockStainedGlass[glassId], cv.metadata);
            }
        }

        if(Chisel.featureEnabled("glassStainedPane")) for(int i = 0; i < 16; i++)
        {
            String blockName = "chisel.stainedGlassPane" + sGNames[i].replaceAll(" ", "");
            String oreName = "stainedGlassPane" + sGNames[i].replaceAll(" ", "");
            String texName = "glasspanedyed/" + sGNames[i].toLowerCase().replaceAll(" ", "") + "-";
            int glassPrefix = (i & 1) << 3;
            int glassId = i >> 1;
            if(glassPrefix == 0)
            {
                blockStainedGlassPane[glassId] = (BlockCarvablePane) new BlockCarvablePane(Material.glass, false).setStained(true).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setBlockName("Stained Glass Pane");
                blockStainedGlassPane[glassId].carverHelper.registerBlock(blockStainedGlassPane[glassId], blockName);
                blockStainedGlassPane[glassId].carverHelper.blockName = "Stained Glass Pane";
            }
            blockStainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " bubble glass", glassPrefix + 0, texName + "bubble");
            blockStainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " glass panel", glassPrefix + 1, texName + "panel");
            blockStainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " fancy glass panel", glassPrefix + 2, texName + "panel-fancy");
            blockStainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " borderless glass", glassPrefix + 3, texName + "transparent");
            blockStainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " quadrant glass", glassPrefix + 4, texName + "quad");
            blockStainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " fancy quadrant glass", glassPrefix + 5, texName + "quad-fancy");
            OreDictionary.registerOre(oreName, new ItemStack(Blocks.stained_glass_pane, 1, i));
            Carving.chisel.registerOre(blockName, oreName);
            for(CarvableVariation cv : blockStainedGlassPane[glassId].carverHelper.variations)
            {
                if(cv.metadata < glassPrefix || cv.metadata >= glassPrefix + 8)
                    continue;
                blockStainedGlassPane[glassId].carverHelper.registerVariation(blockName, cv, blockStainedGlassPane[glassId], cv.metadata);
            }
        }

        if(Chisel.featureEnabled("paperWall"))
        {
            //blockPaperwall = (BlockCarvablePane) new BlockCarvablePane(Material.ground, true).setCreativeTab(Chisel.tabChisel).setHardness(2.0F).setResistance(10F);
            //blockFactory.carverHelper.setChiselBlockName("Paperwall");
            //blockFactory.carverHelper.addVariation("Basic paperwall", 0, "paper/basic");


            //blockTempleMossy.carverHelper.register(blockPaperwall, "paperwall");

        }

        Blocks.stone.setHarvestLevel("chisel", 0, 0);
    }

}
