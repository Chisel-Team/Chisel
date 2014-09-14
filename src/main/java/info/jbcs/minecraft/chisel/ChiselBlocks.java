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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

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
    public static BlockCarvable blockWoolenClay;
    public static BlockCarvable blockLaboratory;

    public static BlockSnakestone blockSnakestone;
    public static BlockSnakestone blockSandSnakestone;
    public static BlockSnakestoneObsidian blockObsidianSnakestone;

    public static BlockSpikes blockSpiketrap;

    // 1.7
    public static BlockCarvableGlass[] blockStainedGlass = new BlockCarvableGlass[4];
    public static BlockCarvablePane[] blockStainedGlassPane = new BlockCarvablePane[8];

    public static void load()
    {
        if(Configurations.featureEnabled("marble"))
        {
            blockMarble = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
            blockMarble.carverHelper.setChiselBlockName("Marble");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.0.desc"), 0, "marble");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.1.desc"), 1, "marble/a1-stoneornamental-marblebrick");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.2.desc"), 2, "marble/a1-stoneornamental-marbleclassicpanel");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.3.desc"), 3, "marble/a1-stoneornamental-marbleornate");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.4.desc"), 4, "marble/panel");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.5.desc"), 5, "marble/block");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.6.desc"), 6, "marble/terrain-pistonback-marblecreeperdark");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.7.desc"), 7, "marble/terrain-pistonback-marblecreeperlight");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.8.desc"), 8, "marble/a1-stoneornamental-marblecarved");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.9.desc"), 9, "marble/a1-stoneornamental-marblecarvedradial");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.10.desc"), 10, "marble/terrain-pistonback-marbledent");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.11.desc"), 11, "marble/terrain-pistonback-marbledent-small");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.12.desc"), 12, "marble/marble-bricks");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.13.desc"), 13, "marble/marble-arranged-bricks");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.14.desc"), 14, "marble/marble-fancy-bricks");
            blockMarble.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marble.15.desc"), 15, "marble/marble-blocks");
            blockMarble.carverHelper.register(blockMarble, "marble");
            OreDictionary.registerOre("blockMarble", blockMarble);
            Carving.chisel.registerOre("marble", "blockMarble");

            if(Chisel.multipartLoaded)
            {
                //    for(int i = 0; i < 16; i++)
                //        MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(blockMarble, i), "blockMarble" + i);
            }

            blockMarbleSlab = (BlockMarbleSlab) new BlockMarbleSlab(blockMarble).setHardness(2.0F).setResistance(10F);
            blockMarbleSlab.carverHelper.setChiselBlockName("Marble Slab");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.0.desc"), 0, "marble");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.1.desc"), 1, "marbleslab/a1-stoneornamental-marblebrick");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.2.desc"), 2, "marbleslab/a1-stoneornamental-marbleclassicpanel");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.3.desc"), 3, "marbleslab/a1-stoneornamental-marbleornate");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.4.desc"), 4, "marbleslab/a1-stoneornamental-marblepanel");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.5.desc"), 5, "marbleslab/terrain-pistonback-marble");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.6.desc"), 6, "marbleslab/terrain-pistonback-marblecreeperdark");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.7.desc"), 7, "marbleslab/terrain-pistonback-marblecreeperlight");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.8.desc"), 8, "marbleslab/a1-stoneornamental-marblecarved");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.9.desc"), 9, "marbleslab/a1-stoneornamental-marblecarvedradial");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.10.desc"), 10, "marbleslab/terrain-pistonback-marbledent");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.11.desc"), 11, "marbleslab/terrain-pistonback-marbledent-small");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.12.desc"), 12, "marbleslab/marble-bricks");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.13.desc"), 13, "marbleslab/marble-arranged-bricks");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.14.desc"), 14, "marbleslab/marble-fancy-bricks");
            blockMarbleSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleSlab.15.desc"), 15, "marbleslab/marble-blocks");
            blockMarbleSlab.carverHelper.register(blockMarbleSlab, "marbleSlab", ItemMarbleSlab.class);

            if(Configurations.featureEnabled("marblePillar"))
            {
                if(Configurations.oldPillars)
                {
                    blockMarblePillar = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
                    blockMarblePillar.carverHelper.setChiselBlockName("Marble Pillar");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.0.desc"), 0, "marblepillarold/column");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.1.desc"), 1, "marblepillarold/capstone");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.2.desc"), 2, "marblepillarold/base");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.3.desc"), 3, "marblepillarold/small");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.4.desc"), 4, "marblepillarold/pillar-carved");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.5.desc"), 5, "marblepillarold/a1-stoneornamental-marblegreek");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.6.desc"), 6, "marblepillarold/a1-stonepillar-greek");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.7.desc"), 7, "marblepillarold/a1-stonepillar-plain");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.8.desc"), 8, "marblepillarold/a1-stonepillar-greektopplain");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.9.desc"), 9, "marblepillarold/a1-stonepillar-plaintopplain");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.10.desc"), 10, "marblepillarold/a1-stonepillar-greekbottomplain");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.11.desc"), 11, "marblepillarold/a1-stonepillar-plainbottomplain");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.12.desc"), 12, "marblepillarold/a1-stonepillar-greektopgreek");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.13.desc"), 13, "marblepillarold/a1-stonepillar-plaintopgreek");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.14.desc"), 14, "marblepillarold/a1-stonepillar-greekbottomgreek");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarOld.15.desc"), 15, "marblepillarold/a1-stonepillar-plainbottomgreek");
                } else
                {
                    blockMarblePillar = (BlockCarvable) new BlockMarblePillar(Material.rock).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
                    blockMarblePillar.carverHelper.setChiselBlockName("Marble Pillar");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.0.desc"), 0, "marblepillar/pillar");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.1.desc"), 1, "marblepillar/default");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.2.desc"), 2, "marblepillar/simple");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.3.desc"), 3, "marblepillar/convex");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.4.desc"), 4, "marblepillar/rough");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.5.desc"), 5, "marblepillar/greekdecor");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.6.desc"), 6, "marblepillar/greekgreek");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.7.desc"), 7, "marblepillar/greekplain");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.8.desc"), 8, "marblepillar/plaindecor");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.9.desc"), 9, "marblepillar/plaingreek");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.10.desc"), 10, "marblepillar/plainplain");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.11.desc"), 11, "marblepillar/widedecor");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.12.desc"), 12, "marblepillar/widegreek");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.13.desc"), 13, "marblepillar/wideplain");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.14.desc"), 14, "marblepillar/carved");
                    blockMarblePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillar.15.desc"), 15, "marblepillar/ornamental");
                }
                blockMarblePillar.carverHelper.register(blockMarblePillar, "marblePillar");
                Carving.chisel.setGroupClass("marblePillar", "marble");

                blockMarblePillarSlab = (BlockMarbleSlab) new BlockMarbleSlab(blockMarblePillar).setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
                blockMarblePillarSlab.carverHelper.setChiselBlockName("Marble Pillar Slab");
                if(Configurations.oldPillars)
                {
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.0.desc"), 0, "marblepillarslabold/column");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.1.desc"), 1, "marblepillarslabold/capstone");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.2.desc"), 2, "marblepillarslabold/base");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.3.desc"), 3, "marblepillarslabold/small");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.4.desc"), 4, "marblepillarslabold/pillar-carved");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.5.desc"), 5, "marblepillarslabold/a1-stoneornamental-marblegreek");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.6.desc"), 6, "marblepillarslabold/a1-stonepillar-greek");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.7.desc"), 7, "marblepillarslabold/a1-stonepillar-plain");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.8.desc"), 8, "marblepillarslabold/a1-stonepillar-greektopplain");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.9.desc"), 9, "marblepillarslabold/a1-stonepillar-plaintopplain");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.10.desc"), 10, "marblepillarslabold/a1-stonepillar-greekbottomplain");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.11.desc"), 11, "marblepillarslabold/a1-stonepillar-plainbottomplain");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.12.desc"), 12, "marblepillarslabold/a1-stonepillar-greektopgreek");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.13.desc"), 13, "marblepillarslabold/a1-stonepillar-plaintopgreek");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.14.desc"), 14, "marblepillarslabold/a1-stonepillar-greekbottomgreek");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlabOld.15.desc"), 15, "marblepillarslabold/a1-stonepillar-plainbottomgreek");
                } else
                {
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.0.desc"), 0, "marblepillarslab/pillar");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.1.desc"), 1, "marblepillarslab/default");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.2.desc"), 2, "marblepillarslab/simple");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.3.desc"), 3, "marblepillarslab/convex");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.4.desc"), 4, "marblepillarslab/rough");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.5.desc"), 5, "marblepillarslab/greekdecor");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.6.desc"), 6, "marblepillarslab/greekgreek");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.7.desc"), 7, "marblepillarslab/greekplain");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.8.desc"), 8, "marblepillarslab/plaindecor");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.9.desc"), 9, "marblepillarslab/plaingreek");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.10.desc"), 10, "marblepillarslab/plainplain");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.11.desc"), 11, "marblepillarslab/widedecor");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.12.desc"), 12, "marblepillarslab/widegreek");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.13.desc"), 13, "marblepillarslab/wideplain");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.14.desc"), 14, "marblepillarslab/carved");
                    blockMarblePillarSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marblePillarSlab.15.desc"), 15, "marblepillarslab/ornamental");
                }
                blockMarblePillarSlab.carverHelper.register(blockMarblePillarSlab, "marblePillarSlab", ItemMarbleSlab.class);
            }

            BlockMarbleStairsMaker makerMarbleStairs = new BlockMarbleStairsMaker(blockMarble);
            makerMarbleStairs.carverHelper.setChiselBlockName("Marble Stairs");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.0.desc"), 0, "marble");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.6.desc"), 1, "marbleslab/a1-stoneornamental-marblebrick");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.2.desc"), 2, "marbleslab/a1-stoneornamental-marbleclassicpanel");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.3.desc"), 3, "marbleslab/a1-stoneornamental-marbleornate");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.4.desc"), 4, "marbleslab/a1-stoneornamental-marblepanel");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.5.desc"), 5, "marbleslab/terrain-pistonback-marble");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.6.desc"), 6, "marbleslab/terrain-pistonback-marblecreeperdark");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.7.desc"), 7, "marbleslab/terrain-pistonback-marblecreeperlight");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.8.desc"), 8, "marbleslab/a1-stoneornamental-marblecarved");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.9.desc"), 9, "marbleslab/a1-stoneornamental-marblecarvedradial");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.10.desc"), 10, "marbleslab/terrain-pistonback-marbledent");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.11.desc"), 11, "marbleslab/terrain-pistonback-marbledent-small");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.12.desc"), 12, "marbleslab/marble-bricks");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.13.desc"), 13, "marbleslab/marble-arranged-bricks");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.14.desc"), 14, "marbleslab/marble-fancy-bricks");
            makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.marbleStairs.15.desc"), 15, "marbleslab/marble-blocks");
            makerMarbleStairs.create("marbleStairs");
        }

        if(Configurations.featureEnabled("limestone"))
        {
            blockLimestone = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
            blockLimestone.carverHelper.setChiselBlockName("Limestone");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.0.desc"), 0, "limestone");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.1.desc"), 1, "limestone/terrain-cobbsmalltilelight");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.2.desc"), 2, "limestone/terrain-cob-frenchlight");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.3.desc"), 3, "limestone/terrain-cob-french2light");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.4.desc"), 4, "limestone/terrain-cobmoss-creepdungeonlight");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.5.desc"), 5, "limestone/terrain-cob-smallbricklight");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.6.desc"), 6, "limestone/terrain-mossysmalltilelight");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.7.desc"), 7, "limestone/terrain-pistonback-dungeon");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.8.desc"), 8, "limestone/terrain-pistonback-dungeonornate");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.9.desc"), 9, "limestone/terrain-pistonback-dungeonvent");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.10.desc"), 10, "limestone/terrain-pistonback-lightcreeper");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.11.desc"), 11, "limestone/terrain-pistonback-lightdent");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.12.desc"), 12, "limestone/terrain-pistonback-lightemboss");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.13.desc"), 13, "limestone/terrain-pistonback-lightfour");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.14.desc"), 14, "limestone/terrain-pistonback-lightmarker");
            blockLimestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestone.15.desc"), 15, "limestone/terrain-pistonback-lightpanel");
            blockLimestone.carverHelper.register(blockLimestone, "limestone");
            OreDictionary.registerOre("blockLimestone", blockLimestone);
            Carving.chisel.registerOre("limestone", "blockLimestone");

            blockLimestoneSlab = (BlockMarbleSlab) new BlockMarbleSlab(blockLimestone).setHardness(2.0F).setResistance(10F);
            blockLimestoneSlab.carverHelper.setChiselBlockName("Limestone Slab");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.0.desc"), 0, "limestone");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.1.desc"), 1, "limestone/terrain-cobbsmalltilelight");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.2.desc"), 2, "limestone/terrain-cob-frenchlight");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.3.desc"), 3, "limestone/terrain-cob-french2light");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.4.desc"), 4, "limestone/terrain-cobmoss-creepdungeonlight");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.5.desc"), 5, "limestone/terrain-cob-smallbricklight");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.6.desc"), 6, "limestone/terrain-mossysmalltilelight");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.7.desc"), 7, "limestone/terrain-pistonback-dungeon");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.8.desc"), 8, "limestone/terrain-pistonback-dungeonornate");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.9.desc"), 9, "limestone/terrain-pistonback-dungeonvent");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.10.desc"), 10, "limestone/terrain-pistonback-lightcreeper");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.11.desc"), 11, "limestone/terrain-pistonback-lightdent");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.12.desc"), 12, "limestone/terrain-pistonback-lightemboss");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.13.desc"), 13, "limestone/terrain-pistonback-lightfour");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.14.desc"), 14, "limestone/terrain-pistonback-lightmarker");
            blockLimestoneSlab.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneSlab.15.desc"), 15, "limestone/terrain-pistonback-lightpanel");
            blockLimestoneSlab.carverHelper.register(blockLimestoneSlab, "limestoneSlab", ItemMarbleSlab.class);

            BlockMarbleStairsMaker makerLimestoneStairs = new BlockMarbleStairsMaker(blockLimestone);
            makerLimestoneStairs.carverHelper.setChiselBlockName("Limestone Stairs");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.0.desc"), 0, "limestone");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.1.desc"), 1, "limestone/terrain-cobbsmalltilelight");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.2.desc"), 2, "limestone/terrain-cob-frenchlight");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.3.desc"), 3, "limestone/terrain-cob-french2light");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.4.desc"), 4, "limestone/terrain-cobmoss-creepdungeonlight");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.5.desc"), 5, "limestone/terrain-cob-smallbricklight");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.6.desc"), 6, "limestone/terrain-mossysmalltilelight");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.7.desc"), 7, "limestone/terrain-pistonback-dungeon");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.8.desc"), 8, "limestone/terrain-pistonback-dungeonornate");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.9.desc"), 9, "limestone/terrain-pistonback-dungeonvent");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.10.desc"), 10, "limestone/terrain-pistonback-lightcreeper");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.11.desc"), 11, "limestone/terrain-pistonback-lightdent");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.12.desc"), 12, "limestone/terrain-pistonback-lightemboss");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.13.desc"), 13, "limestone/terrain-pistonback-lightfour");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.14.desc"), 14, "limestone/terrain-pistonback-lightmarker");
            makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.limestoneStairs.15.desc"), 15, "limestone/terrain-pistonback-lightpanel");
            makerLimestoneStairs.create("limestoneStairs");
        }

        if(Configurations.featureEnabled("cobblestone"))
        {
            blockCobblestone = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("cobblestone", Blocks.cobblestone, 0, 0);
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.0.desc"), 1, "cobblestone/terrain-cobb-brickaligned");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.1.desc"), 2, "cobblestone/terrain-cob-detailedbrick");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.2.desc"), 3, "cobblestone/terrain-cob-smallbrick");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.3.desc"), 4, "cobblestone/terrain-cobblargetiledark");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.4.desc"), 5, "cobblestone/terrain-cobbsmalltile");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.5.desc"), 6, "cobblestone/terrain-cob-french");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.6.desc"), 7, "cobblestone/terrain-cob-french2");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.7.desc"), 8, "cobblestone/terrain-cobmoss-creepdungeon");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.8.desc"), 9, "cobblestone/terrain-mossysmalltiledark");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.9.desc"), 10, "cobblestone/terrain-pistonback-dungeontile");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.10.desc"), 11, "cobblestone/terrain-pistonback-darkcreeper");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.11.desc"), 12, "cobblestone/terrain-pistonback-darkdent");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.12.desc"), 13, "cobblestone/terrain-pistonback-darkemboss");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.13.desc"), 14, "cobblestone/terrain-pistonback-darkmarker");
            blockCobblestone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.cobblestone.14.desc"), 15, "cobblestone/terrain-pistonback-darkpanel");
            blockCobblestone.carverHelper.register(blockCobblestone, "cobblestone");
            Carving.chisel.registerOre("cobblestone", "blockCobble");
        }

        if(Configurations.featureEnabled("glass"))
        {
            blockGlass = (BlockCarvableGlass) new BlockCarvableGlass().setHardness(0.3F).setStepSound(Block.soundTypeGlass);
            Carving.chisel.addVariation("glass", Blocks.glass, 0, 0);
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.1.desc"), 1, "glass/terrain-glassbubble");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.2.desc"), 2, "glass/terrain-glass-chinese");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.3.desc"), 3, "glass/japanese");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.4.desc"), 4, "glass/terrain-glassdungeon");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.5.desc"), 5, "glass/terrain-glasslight");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.6.desc"), 6, "glass/terrain-glassnoborder");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.7.desc"), 7, "glass/terrain-glass-ornatesteel");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.8.desc"), 8, "glass/terrain-glass-screen");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.9.desc"), 9, "glass/terrain-glassshale");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.10.desc"), 10, "glass/terrain-glass-steelframe");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.11.desc"), 11, "glass/terrain-glassstone");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.12.desc"), 12, "glass/terrain-glassstreak");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.13.desc"), 13, "glass/terrain-glass-thickgrid");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.14.desc"), 14, "glass/terrain-glass-thingrid");
            blockGlass.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.glass.15.desc"), 15, "glass/a1-glasswindow-ironfencemodern");
            blockGlass.carverHelper.register(blockGlass, "glass");
            Carving.chisel.registerOre("glass", "blockGlass");
        }

        if(Configurations.featureEnabled("sandstone"))
        {
            blockSandstone = (BlockCarvable) new BlockCarvable().setStepSound(Block.soundTypeStone).setHardness(0.8F);
            Carving.chisel.addVariation("sandstone", Blocks.sandstone, 0, 0);
            Carving.chisel.addVariation("sandstone", Blocks.sandstone, 1, 1);
            Carving.chisel.addVariation("sandstone", Blocks.sandstone, 2, 2);
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.3.desc"), 3, "sandstone/faded");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.4.desc"), 4, "sandstone/column");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.5.desc"), 5, "sandstone/capstone");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.6.desc"), 6, "sandstone/small");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.7.desc"), 7, "sandstone/base");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.8.desc"), 8, "sandstone/smooth");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.9.desc"), 9, "sandstone/smooth-cap");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.10.desc"), 10, "sandstone/smooth-small");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.11.desc"), 11, "sandstone/smooth-base");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.12.desc"), 12, "sandstone/block");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.13.desc"), 13, "sandstone/blocks");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.14.desc"), 14, "sandstone/mosaic");
            blockSandstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstone.15.desc"), 15, "sandstone/horizontal-tiles");
            blockSandstone.carverHelper.register(blockSandstone, "sandstone");
            Carving.chisel.registerOre("sandstone", "blockSandstone");

            if(Configurations.featureEnabled("snakeSandstone"))
            {
                blockSandSnakestone = (BlockSnakestone) new BlockSnakestone("Chisel:snakestone/sandsnake/").setBlockName("snakestoneSand");
                GameRegistry.registerBlock(blockSandSnakestone, ItemCarvable.class, blockSandSnakestone.getUnlocalizedName());
                //TODO- eat me!
                //LanguageRegistry.addName(new ItemStack(blockSandSnakestone, 1, 1), "Sandstone snake block head");
                //LanguageRegistry.addName(new ItemStack(blockSandSnakestone, 1, 13), "Sandstone snake block body");
                Carving.chisel.addVariation("sandstone", blockSandSnakestone, 1, 16);
                Carving.chisel.addVariation("sandstone", blockSandSnakestone, 13, 17);
            }
        }

        if(Configurations.featureEnabled("sandstoneScribbles"))
        {
            blockSandstoneScribbles = (BlockCarvable) new BlockCarvable().setStepSound(Block.soundTypeStone).setHardness(0.8F);
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 0, "sandstone-scribbles/scribbles-0");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 1, "sandstone-scribbles/scribbles-1");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 2, "sandstone-scribbles/scribbles-2");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 3, "sandstone-scribbles/scribbles-3");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 4, "sandstone-scribbles/scribbles-4");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 5, "sandstone-scribbles/scribbles-5");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 6, "sandstone-scribbles/scribbles-6");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 7, "sandstone-scribbles/scribbles-7");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 8, "sandstone-scribbles/scribbles-8");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 9, "sandstone-scribbles/scribbles-9");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 10, "sandstone-scribbles/scribbles-10");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 11, "sandstone-scribbles/scribbles-11");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 12, "sandstone-scribbles/scribbles-12");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 13, "sandstone-scribbles/scribbles-13");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 14, "sandstone-scribbles/scribbles-14");
            blockSandstoneScribbles.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.sandstoneScribbles.desc"), 15, "sandstone-scribbles/scribbles-15");
            blockSandstoneScribbles.carverHelper.register(blockSandstoneScribbles, "sandstoneScribbles");
        }

        if(Configurations.featureEnabled("concrete"))
        {
            blockConcrete = (BlockConcrete) new BlockConcrete().setStepSound(Block.soundTypeStone).setHardness(0.5F);
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.0.desc"), 0, "concrete/default");
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.1.desc"), 1, "concrete/block");
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.2.desc"), 2, "concrete/doubleslab");
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.3.desc"), 3, "concrete/blocks");
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.4.desc"), 4, "concrete/weathered");
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.5.desc"), 5, "concrete/weathered-block");
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.6.desc"), 6, "concrete/weathered-doubleslab");
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.7.desc"), 7, "concrete/weathered-blocks");
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.8.desc"), 8, "concrete/weathered-half");
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.9.desc"), 9, "concrete/weathered-block-half");
            blockConcrete.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockConcrete.10.desc"), 10, "concrete/asphalt");
            blockConcrete.carverHelper.register(blockConcrete, "concrete");
            OreDictionary.registerOre("blockConcrete", blockConcrete);
            Carving.chisel.registerOre("concrete", "blockConcrete");
        }

        if(Configurations.featureEnabled("roadLine"))
        {
            blockRoadLine = (BlockRoadLine) new BlockRoadLine().setStepSound(Block.soundTypeStone).setHardness(0.01F).setBlockName("roadLine");
            GameRegistry.registerBlock(blockRoadLine, ItemCarvable.class, "roadLine");
            //TODO- flag
            //LanguageRegistry.addName(new ItemStack(blockRoadLine, 1, 0), "Road lines");
        }

        if(Configurations.featureEnabled("ironBlock"))
        {
            blockIron = (BlockBeaconBase) new BlockBeaconBase().setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal);
            Carving.chisel.addVariation("iron", Blocks.iron_block, 0, 0);
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.1.desc"), 1, "iron/terrain-iron-largeingot");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.2.desc"), 2, "iron/terrain-iron-smallingot");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.3.desc"), 3, "iron/terrain-iron-gears");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.4.desc"), 4, "iron/terrain-iron-brick");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.5.desc"), 5, "iron/terrain-iron-plates");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.6.desc"), 6, "iron/terrain-iron-rivets");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.7.desc"), 7, "iron/terrain-iron-coin-heads");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.8.desc"), 8, "iron/terrain-iron-coin-tails");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.9.desc"), 9, "iron/terrain-iron-crate-dark");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.10.desc"), 10, "iron/terrain-iron-crate-light");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.11.desc"), 11, "iron/terrain-iron-moon");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.12.desc"), 12, "iron/terrain-iron-space");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.13.desc"), 13, "iron/terrain-iron-spaceblack");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.14.desc"), 14, "iron/terrain-iron-vents");
            blockIron.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iron.15.desc"), 15, "iron/terrain-iron-simple");
            blockIron.carverHelper.register(blockIron, "iron");
            Carving.chisel.registerOre("iron", "blockIron");
        }

        if(Configurations.featureEnabled("goldBlock"))
        {
            blockGold = (BlockBeaconBase) new BlockBeaconBase().setHardness(3F).setResistance(10F).setStepSound(Block.soundTypeMetal);
            Carving.chisel.addVariation("gold", Blocks.gold_block, 0, 0);
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.1.desc"), 1, "gold/terrain-gold-largeingot");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.2.desc"), 2, "gold/terrain-gold-smallingot");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.3.desc"), 3, "gold/terrain-gold-brick");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.4.desc"), 4, "gold/terrain-gold-cart");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.5.desc"), 5, "gold/terrain-gold-coin-heads");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.6.desc"), 6, "gold/terrain-gold-coin-tails");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.7.desc"), 7, "gold/terrain-gold-crate-dark");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.8.desc"), 8, "gold/terrain-gold-crate-light");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.9.desc"), 9, "gold/terrain-gold-plates");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.10.desc"), 10, "gold/terrain-gold-rivets");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.11.desc"), 11, "gold/terrain-gold-star");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.12.desc"), 12, "gold/terrain-gold-space");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.13.desc"), 13, "gold/terrain-gold-spaceblack");
            blockGold.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.gold.14.desc"), 14, "gold/terrain-gold-simple");
            blockGold.carverHelper.register(blockGold, "gold");
            Carving.chisel.registerOre("gold", "blockGold");
        }

        if(Configurations.featureEnabled("diamondBlock"))
        {
            blockDiamond = (BlockBeaconBase) new BlockBeaconBase().setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal);
            Carving.chisel.addVariation("diamond", Blocks.diamond_block, 0, 0);
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.1.desc"), 1, "diamond/terrain-diamond-embossed");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.2.desc"), 2, "diamond/terrain-diamond-gem");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.3.desc"), 3, "diamond/terrain-diamond-cells");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.4.desc"), 4, "diamond/terrain-diamond-space");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.5.desc"), 5, "diamond/terrain-diamond-spaceblack");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.6.desc"), 6, "diamond/terrain-diamond-simple");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.7.desc"), 7, "diamond/a1-blockdiamond-bismuth");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.8.desc"), 8, "diamond/a1-blockdiamond-crushed");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.9.desc"), 9, "diamond/a1-blockdiamond-four");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.10.desc"), 10, "diamond/a1-blockdiamond-fourornate");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.11.desc"), 11, "diamond/a1-blockdiamond-zelda");
            blockDiamond.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.diamond.12.desc"), 12, "diamond/a1-blockdiamond-ornatelayer");
            blockDiamond.carverHelper.register(blockDiamond, "diamond");
            Carving.chisel.registerOre("diamond", "blockDiamond");
        }

        if(Configurations.featureEnabled("glowstone"))
        {
            blockLightstone = (BlockLightstoneCarvable) new BlockLightstoneCarvable().setHardness(0.3F).setLightLevel(1.0F).setStepSound(Block.soundTypeGlass);
            Carving.chisel.addVariation("lightstone", Blocks.glowstone, 0, 0);
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.1.desc"), 1, "lightstone/terrain-sulphur-cobble");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.2.desc"), 2, "lightstone/terrain-sulphur-corroded");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.3.desc"), 3, "lightstone/terrain-sulphur-glass");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.4.desc"), 4, "lightstone/terrain-sulphur-neon");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.5.desc"), 5, "lightstone/terrain-sulphur-ornate");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.6.desc"), 6, "lightstone/terrain-sulphur-rocky");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.7.desc"), 7, "lightstone/terrain-sulphur-shale");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.8.desc"), 8, "lightstone/terrain-sulphur-tile");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.9.desc"), 9, "lightstone/terrain-sulphur-weavelanternlight");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.10.desc"), 10, "lightstone/a1-glowstone-cobble");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.11.desc"), 11, "lightstone/a1-glowstone-growth");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.12.desc"), 12, "lightstone/a1-glowstone-layers");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.13.desc"), 13, "lightstone/a1-glowstone-tilecorroded");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.14.desc"), 14, "lightstone/glowstone-bismuth");
            blockLightstone.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lightstone.15.desc"), 15, "lightstone/glowstone-bismuth-panel");
            blockLightstone.carverHelper.register(blockLightstone, "lightstone");
            Carving.chisel.registerOre("lightstone", "blockGlowstone");
        }

        if(Configurations.featureEnabled("lapisBlock"))
        {
            blockLapis = (BlockCarvable) new BlockCarvable().setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("lapis", Blocks.lapis_block, 0, 0);
            blockLapis.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lapis.1.desc"), 1, "lapis/terrain-lapisblock-chunky");
            blockLapis.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lapis.2.desc"), 2, "lapis/terrain-lapisblock-panel");
            blockLapis.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lapis.3.desc"), 3, "lapis/terrain-lapisblock-zelda");
            blockLapis.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lapis.4.desc"), 4, "lapis/terrain-lapisornate");
            blockLapis.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lapis.5.desc"), 5, "lapis/terrain-lapistile");
            blockLapis.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lapis.6.desc"), 6, "lapis/a1-blocklapis-panel");
            blockLapis.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lapis.7.desc"), 7, "lapis/a1-blocklapis-smooth");
            blockLapis.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.lapis.8.desc"), 8, "lapis/a1-blocklapis-ornatelayer");
            blockLapis.carverHelper.register(blockLapis, "lapis");
            Carving.chisel.registerOre("lapis", "blockLapis");
        }

        if(Configurations.featureEnabled("emeraldBlock"))
        {
            blockEmerald = (BlockBeaconBase) new BlockBeaconBase().setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundTypeMetal);
            Carving.chisel.addVariation("emerald", Blocks.emerald_block, 0, 0);
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.1.desc"), 1, "emerald/a1-blockemerald-emeraldpanel");
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.2.desc"), 2, "emerald/a1-blockemerald-emeraldpanelclassic");
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.3.desc"), 3, "emerald/a1-blockemerald-emeraldsmooth");
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.4.desc"), 4, "emerald/a1-blockemerald-emeraldchunk");
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.5.desc"), 5, "emerald/a1-blockemerald-emeraldornatelayer");
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.6.desc"), 6, "emerald/a1-blockemerald-emeraldzelda");
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.7.desc"), 7, "emerald/a1-blockquartz-cell");
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.8.desc"), 8, "emerald/a1-blockquartz-cellbismuth");
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.9.desc"), 9, "emerald/a1-blockquartz-four");
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.10.desc"), 10, "emerald/a1-blockquartz-fourornate");
            blockEmerald.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.emerald.11.desc"), 11, "emerald/a1-blockquartz-ornate");
            blockEmerald.carverHelper.register(blockEmerald, "emerald");
            Carving.chisel.registerOre("emerald", "blockEmerald");
        }

        if(Configurations.featureEnabled("netherBrick"))
        {
            blockNetherBrick = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("netherBrick", Blocks.nether_brick, 0, 0);
            //blockNetherBrick.carverHelper.addVariation("Nether brick", 0, Blocks.nether_brick);
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.1.desc"), 1, "netherbrick/a1-netherbrick-brinstar");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.2.desc"), 2, "netherbrick/a1-netherbrick-classicspatter");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.3.desc"), 3, "netherbrick/a1-netherbrick-guts");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.4.desc"), 4, "netherbrick/a1-netherbrick-gutsdark");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.5.desc"), 5, "netherbrick/a1-netherbrick-gutssmall");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.6.desc"), 6, "netherbrick/a1-netherbrick-lavabrinstar");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.7.desc"), 7, "netherbrick/a1-netherbrick-lavabrown");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.8.desc"), 8, "netherbrick/a1-netherbrick-lavaobsidian");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.9.desc"), 9, "netherbrick/a1-netherbrick-lavastonedark");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.10.desc"), 10, "netherbrick/a1-netherbrick-meat");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.11.desc"), 11, "netherbrick/a1-netherbrick-meatred");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.12.desc"), 12, "netherbrick/a1-netherbrick-meatredsmall");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.13.desc"), 13, "netherbrick/a1-netherbrick-meatsmall");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.14.desc"), 14, "netherbrick/a1-netherbrick-red");
            blockNetherBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.netherBrick.15.desc"), 15, "netherbrick/a1-netherbrick-redsmall");
            blockNetherBrick.carverHelper.register(blockNetherBrick, "netherBrick");
            Carving.chisel.registerOre("netherBrick", "netherBrick");
        }

        if(Configurations.featureEnabled("netherRack"))
        {
            blockNetherrack = (BlockCarvable) new BlockCarvable().setHardness(0.4F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("hellrock", Blocks.netherrack, 0, 0);
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.1.desc"), 1, "netherrack/a1-netherrack-bloodgravel");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.2.desc"), 2, "netherrack/a1-netherrack-bloodrock");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.3.desc"), 3, "netherrack/a1-netherrack-bloodrockgrey");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.4.desc"), 4, "netherrack/a1-netherrack-brinstar");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.5.desc"), 5, "netherrack/a1-netherrack-brinstarshale");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.6.desc"), 6, "netherrack/a1-netherrack-classic");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.7.desc"), 7, "netherrack/a1-netherrack-classicspatter");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.8.desc"), 8, "netherrack/a1-netherrack-guts");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.9.desc"), 9, "netherrack/a1-netherrack-gutsdark");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.10.desc"), 10, "netherrack/a1-netherrack-meat");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.11.desc"), 11, "netherrack/a1-netherrack-meatred");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.12.desc"), 12, "netherrack/a1-netherrack-meatrock");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.13.desc"), 13, "netherrack/a1-netherrack-red");
            blockNetherrack.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.hellrock.14.desc"), 14, "netherrack/a1-netherrack-wells");
            blockNetherrack.carverHelper.register(blockNetherrack, "hellrock");
            Carving.chisel.registerOre("hellrock", "blockNetherrack");
        }

        if(Configurations.featureEnabled("cobblestoneMossy"))
        {
            blockCobblestoneMossy = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10.0F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("stoneMoss", Blocks.mossy_cobblestone, 0, 0);
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.1.desc"), 1, "cobblestonemossy/terrain-cobb-brickaligned");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.2.desc"), 2, "cobblestonemossy/terrain-cob-detailedbrick");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.3.desc"), 3, "cobblestonemossy/terrain-cob-smallbrick");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.4.desc"), 4, "cobblestonemossy/terrain-cobblargetiledark");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.5.desc"), 5, "cobblestonemossy/terrain-cobbsmalltile");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.6.desc"), 6, "cobblestonemossy/terrain-cob-french");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.7.desc"), 7, "cobblestonemossy/terrain-cob-french2");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.8.desc"), 8, "cobblestonemossy/terrain-cobmoss-creepdungeon");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.9.desc"), 9, "cobblestonemossy/terrain-mossysmalltiledark");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.10.desc"), 10, "cobblestonemossy/terrain-pistonback-dungeontile");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.11.desc"), 11, "cobblestonemossy/terrain-pistonback-darkcreeper");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.12.desc"), 12, "cobblestonemossy/terrain-pistonback-darkdent");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.13.desc"), 13, "cobblestonemossy/terrain-pistonback-darkemboss");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.14.desc"), 14, "cobblestonemossy/terrain-pistonback-darkmarker");
            blockCobblestoneMossy.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneMoss.15.desc"), 15, "cobblestonemossy/terrain-pistonback-darkpanel");
            blockCobblestoneMossy.carverHelper.register(blockCobblestoneMossy, "stoneMoss");
            Carving.chisel.registerOre("stoneMoss", "blockCobblestoneMossy");
        }

        if(Configurations.featureEnabled("stoneBrick"))
        {
            stoneBrick = (BlockCarvable) new BlockCarvable().setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundTypeStone);
            for(int i = 0; i < 4; i++)
            {
                if(i == 1)
                {
                    if(Configurations.allowMossy)
                        Carving.chisel.addVariation("stoneBrick", Blocks.stonebrick, i, i);
                } else
                    Carving.chisel.addVariation("stoneBrick", Blocks.stonebrick, i, i);
            }
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.4.desc"), 4, "stonebrick/smallbricks");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.5.desc"), 5, "stonebrick/largebricks");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.6.desc"), 6, "stonebrick/smallchaotic");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.7.desc"), 7, "stonebrick/chaoticbricks");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.8.desc"), 8, "stonebrick/chaotic");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.9.desc"), 9, "stonebrick/fancy");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.10.desc"), 10, "stonebrick/ornate");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.11.desc"), 11, "stonebrick/largeornate");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.12.desc"), 12, "stonebrick/panel-hard");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.13.desc"), 13, "stonebrick/sunken");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.14.desc"), 14, "stonebrick/ornatepanel");
            stoneBrick.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.stoneBrick.15.desc"), 15, "stonebrick/poison");
            stoneBrick.carverHelper.register(stoneBrick, "stoneBrick");
            Carving.chisel.registerOre("stoneBrick", "blockStoneBrick");
        }

        if(Configurations.featureEnabled("snakestone"))
        {
            blockSnakestone = (BlockSnakestone) new BlockSnakestone("Chisel:snakestone/snake/").setBlockName("snakestoneStone");
            GameRegistry.registerBlock(blockSnakestone, ItemCarvable.class, blockSnakestone.getUnlocalizedName());
            //LanguageRegistry.addName(new ItemStack(blockSnakestone, 1, 1), "Stone snake block head");
            //LanguageRegistry.addName(new ItemStack(blockSnakestone, 1, 13), "Stone snake block body");
            Carving.chisel.addVariation("stoneBrick", blockSnakestone, 1, 16);
            Carving.chisel.addVariation("stoneBrick", blockSnakestone, 13, 17);
        }

        if(Configurations.featureEnabled("dirt"))
        {
            blockDirt = (BlockCarvable) new BlockCarvable(Material.ground).setHardness(0.5F).setStepSound(Block.soundTypeGravel).setBlockName("dirt.default");
            Carving.chisel.addVariation("blockDirt", Blocks.dirt, 0, 0);
            blockDirt.carverHelper.setChiselBlockName("Dirt");
            //blockDirt.carverHelper.addVariation("Dirt", 0, Blocks.dirt);
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.0.desc"), 0, "dirt/bricks");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.1.desc"), 1, "dirt/netherbricks");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.2.desc"), 2, "dirt/bricks3");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.3.desc"), 3, "dirt/cobble");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.4.desc"), 4, "dirt/reinforced");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.5.desc"), 5, "dirt/dirt-reinforced");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.6.desc"), 6, "dirt/happy");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.7.desc"), 7, "dirt/bricks2");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.8.desc"), 8, "dirt/bricks+dirt2");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.9.desc"), 9, "dirt/hor");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.10.desc"), 10, "dirt/vert");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.11.desc"), 11, "dirt/layers");
            blockDirt.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockDirt.12.desc"), 12, "dirt/vertical");
            blockDirt.carverHelper.register(blockDirt, "blockDirt");
            blockDirt.setHarvestLevel("shovel", 0);
            OreDictionary.registerOre("blockDirt", blockDirt);
            Carving.chisel.registerOre("blockDirt", "blockDirt");
        }

        if(Configurations.featureEnabled("ice"))
        {
            blockIce = (BlockMarbleIce) new BlockMarbleIce().setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass);
            Carving.chisel.addVariation("ice", Blocks.ice, 0, 0);
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.1.desc"), 1, "ice/a1-ice-light");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.2.desc"), 2, "ice/a1-stonecobble-icecobble");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.3.desc"), 3, "ice/a1-netherbrick-ice");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.4.desc"), 4, "ice/a1-stonecobble-icebrick");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.5.desc"), 5, "ice/a1-stonecobble-icebricksmall");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.6.desc"), 6, "ice/a1-stonecobble-icedungeon");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.7.desc"), 7, "ice/a1-stonecobble-icefour");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.8.desc"), 8, "ice/a1-stonecobble-icefrench");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.9.desc"), 9, "ice/sunkentiles");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.10.desc"), 10, "ice/tiles");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.11.desc"), 11, "ice/a1-stonecobble-icepanel");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.12.desc"), 12, "ice/a1-stoneslab-ice");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.13.desc"), 13, "ice/zelda");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.14.desc"), 14, "ice/bismuth");
            blockIce.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.ice.15.desc"), 15, "ice/poison");
            blockIce.carverHelper.register(blockIce, "ice");
            Carving.chisel.registerOre("ice", "blockIce");

            if(Configurations.featureEnabled("icePillar"))
            {
                blockIcePillar = (BlockMarbleIce) new BlockMarbleIce().setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass);
                blockIcePillar.carverHelper.setChiselBlockName("Ice Pillar");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.0.desc"), 0, "icepillar/column");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.1.desc"), 1, "icepillar/capstone");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.2.desc"), 2, "icepillar/base");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.3.desc"), 3, "icepillar/small");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.4.desc"), 4, "icepillar/pillar-carved");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.5.desc"), 5, "icepillar/a1-stoneornamental-marblegreek");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.6.desc"), 6, "icepillar/a1-stonepillar-greek");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.7.desc"), 7, "icepillar/a1-stonepillar-plain");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.8.desc"), 8, "icepillar/a1-stonepillar-greektopplain");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.9.desc"), 9, "icepillar/a1-stonepillar-plaintopplain");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.10.desc"), 10, "icepillar/a1-stonepillar-greekbottomplain");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.11.desc"), 11, "icepillar/a1-stonepillar-plainbottomplain");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.12.desc"), 12, "icepillar/a1-stonepillar-greektopgreek");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.13.desc"), 13, "icepillar/a1-stonepillar-plaintopgreek");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.14.desc"), 14, "icepillar/a1-stonepillar-greekbottomgreek");
                blockIcePillar.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.icePillar.15.desc"), 15, "icepillar/a1-stonepillar-plainbottomgreek");
                blockIcePillar.carverHelper.register(blockIcePillar, "icePillar");
                Carving.chisel.setGroupClass("icePillar", "ice");
            }

            if(Configurations.featureEnabled("iceStairs"))
            {
                BlockMarbleStairsMaker makerIceStairs = new BlockMarbleStairsMaker(Blocks.ice);
                makerIceStairs.carverHelper.setChiselBlockName("Ice Stairs");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.0.desc"), 0, Blocks.ice);
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.1.desc"), 1, "ice/a1-ice-light");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.2.desc"), 2, "ice/a1-stonecobble-icecobble");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.3.desc"), 3, "ice/a1-netherbrick-ice");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.4.desc"), 4, "ice/a1-stonecobble-icebrick");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.5.desc"), 5, "ice/a1-stonecobble-icebricksmall");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.6.desc"), 6, "ice/a1-stonecobble-icedungeon");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.7.desc"), 7, "ice/a1-stonecobble-icefour");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.8.desc"), 8, "ice/a1-stonecobble-icefrench");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.9.desc"), 9, "ice/sunkentiles");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.10.desc"), 10, "ice/tiles");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.11.desc"), 11, "ice/a1-stonecobble-icepanel");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.12.desc"), 12, "ice/a1-stoneslab-ice");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.13.desc"), 13, "ice/zelda");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.14.desc"), 14, "ice/bismuth");
                makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.iceStairs.15.desc"), 15, "ice/poison");
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

        if(Configurations.featureEnabled("wood"))
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

        if(Configurations.featureEnabled("obsidian"))
        {
            blockObsidian = (BlockCarvable) new BlockCarvable().setHardness(50.0F).setResistance(2000.0F).setStepSound(Block.soundTypeStone);
            Carving.chisel.addVariation("obsidian", Blocks.obsidian, 0, 0);
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.1.desc"), 1, "obsidian/pillar");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.2.desc"), 2, "obsidian/pillar-quartz");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.3.desc"), 3, "obsidian/chiseled");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.4.desc"), 4, "obsidian/panel-shiny");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.5.desc"), 5, "obsidian/panel");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.6.desc"), 6, "obsidian/chunks");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.7.desc"), 7, "obsidian/growth");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.8.desc"), 8, "obsidian/crystal");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.9.desc"), 9, "obsidian/map-a");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.10.desc"), 10, "obsidian/map-b");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.11.desc"), 11, "obsidian/panel-light");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.12.desc"), 12, "obsidian/blocks");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.13.desc"), 13, "obsidian/tiles");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.14.desc"), 14, "obsidian/greek");
            blockObsidian.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.obsidian.15.desc"), 15, "obsidian/crate");
            blockObsidian.carverHelper.register(blockObsidian, "obsidian");
            Carving.chisel.registerOre("obsidian", "blockObsidian");
        }

        if(Configurations.featureEnabled("snakestoneObsidian"))
        {
            blockObsidianSnakestone = (BlockSnakestoneObsidian) new BlockSnakestoneObsidian("Chisel:snakestone/obsidian/").setBlockName("obsidianSnakestone").setHardness(50.0F).setResistance(2000.0F);
            GameRegistry.registerBlock(blockObsidianSnakestone, ItemCarvable.class, blockObsidianSnakestone.getUnlocalizedName());
            //LanguageRegistry.addName(new ItemStack(blockObsidianSnakestone, 1, 1), "Obsidian snakestone head");
            //LanguageRegistry.addName(new ItemStack(blockObsidianSnakestone, 1, 13), "Obsidian snakestone body");
            Carving.chisel.addVariation("obsidian", blockObsidianSnakestone, 1, 16);
            Carving.chisel.addVariation("obsidian", blockObsidianSnakestone, 13, 17);
        }

        if(Configurations.featureEnabled("ironBars"))
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

        if(Configurations.featureEnabled("glassPane"))
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

        if(Configurations.featureEnabled("redstoneBlock"))
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

        if(Configurations.featureEnabled("holystone"))
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

        if(Configurations.featureEnabled("lavastone"))
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

        if(Configurations.featureEnabled("fantasy"))
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

        if(Configurations.featureEnabled("carpet"))
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

        if(Configurations.featureEnabled("carpetFloor"))
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

        if(Configurations.featureEnabled("bookshelf"))
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

        if(Configurations.featureEnabled("futuristicArmorPlating"))
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


        if(Configurations.featureEnabled("templeBlock"))
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

            if(Configurations.featureEnabled("templeBlockMossy"))
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

        if(Configurations.featureEnabled("cloud"))
        {
            blockCloud = (BlockCloud) new BlockCloud().setHardness(0.2F).setLightOpacity(3).setStepSound(Block.soundTypeCloth);
            blockCloud.carverHelper.addVariation("Cloud block", 0, "cloud/cloud");
            blockCloud.carverHelper.register(blockCloud, "blockCloud");
            OreDictionary.registerOre("blockCloud", blockCloud);
            Carving.chisel.registerOre("blockCloud", "blockCloud");
        }

        if(Configurations.featureEnabled("factory"))
        {
            blockFactory = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Chisel.soundMetalFootstep);
            blockFactory.carverHelper.setChiselBlockName("blockFactory");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.0.desc"), 0, "factory/dots");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.1.desc"), 1, "factory/rust2");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.2.desc"), 2, "factory/rust");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.3.desc"), 3, "factory/platex");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.4.desc"), 4, "factory/wireframewhite");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.5.desc"), 5, "factory/wireframe");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.6.desc"), 6, "factory/hazard");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.7.desc"), 7, "factory/hazardorange");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.8.desc"), 8, "factory/circuit");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.9.desc"), 9, "factory/metalbox");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.10.desc"), 10, "factory/goldplate");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.11.desc"), 11, "factory/goldplating");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.12.desc"), 12, "factory/grinder");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.13.desc"), 13, "factory/plating");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.14.desc"), 14, "factory/rustplates");
            blockFactory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockFactory.15.desc"), 15, "factory/column");
            blockFactory.carverHelper.register(blockFactory, "blockFactory");
        }

        // 1.7! Let's go! Let's go-go!

        String[] sGNames = new String[]{
                "White", "Orange", "Magenta", "Light Blue",
                "Yellow", "Lime", "Pink", "Gray",
                "Light Gray", "Cyan", "Purple", "Blue",
                "Brown", "Green", "Red", "Black"
        };

        if(Configurations.featureEnabled("glassStained")) for(int i = 0; i < 16; i++)
        {
            String blockName = "chisel.stainedGlass" + sGNames[i].replaceAll(" ", "");
            String oreName = "stainedGlass" + sGNames[i].replaceAll(" ", "");
            String texName = "glassdyed/" + sGNames[i].toLowerCase().replaceAll(" ", "") + "-";
            int glassPrefix = (i & 3) << 2;
            int glassId = i >> 2;
            Carving.chisel.addVariation(blockName, Blocks.stained_glass, i, 0);
            if(glassPrefix == 0)
            {
                blockStainedGlass[glassId] = (BlockCarvableGlass) new BlockCarvableGlass().setStained(true).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setBlockName("Stained Glass");
                blockStainedGlass[glassId].carverHelper.registerBlock(blockStainedGlass[glassId], blockName);
                //blockStainedGlass[glassId].carverHelper.blockName = "Stained Glass";
            }
            blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " bubble glass", glassPrefix, texName + "bubble");
            blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " glass panel", glassPrefix + 1, texName + "panel");
            blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " fancy glass panel", glassPrefix + 2, texName + "panel-fancy");
            blockStainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " borderless glass", glassPrefix + 3, texName + "transparent");
            OreDictionary.registerOre(oreName, new ItemStack(Blocks.stained_glass, 1, i));
            Carving.chisel.registerOre(blockName, oreName);
            for(CarvableVariation cv : blockStainedGlass[glassId].carverHelper.variations)
            {
                if(cv.metadata < glassPrefix || cv.metadata >= glassPrefix + 4)
                    continue;
                blockStainedGlass[glassId].carverHelper.registerVariation(blockName, cv, blockStainedGlass[glassId], cv.metadata);
            }
        }

        if(Configurations.featureEnabled("glassStainedPane")) for(int i = 0; i < 16; i++)
        {
            String blockName = "chisel.stainedGlassPane" + sGNames[i].replaceAll(" ", "");
            String oreName = "stainedGlassPane" + sGNames[i].replaceAll(" ", "");
            String texName = "glasspanedyed/" + sGNames[i].toLowerCase().replaceAll(" ", "") + "-";
            Carving.chisel.addVariation(blockName, Blocks.stained_glass_pane, i, 0);
            int glassPrefix = (i & 1) << 3;
            int glassId = i >> 1;
            if(glassPrefix == 0)
            {
                blockStainedGlassPane[glassId] = (BlockCarvablePane) new BlockCarvablePane(Material.glass, false).setStained(true).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setBlockName("Stained Glass Pane");
                blockStainedGlassPane[glassId].carverHelper.registerBlock(blockStainedGlassPane[glassId], blockName);
                blockStainedGlassPane[glassId].carverHelper.blockName = "Stained Glass Pane";
            }
            blockStainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " bubble glass", glassPrefix, texName + "bubble");
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

        if(Configurations.featureEnabled("paperWall"))
        {
            blockPaperwall = (BlockCarvablePane) new BlockCarvablePane(Material.ground, true).setCreativeTab(Chisel.tabChisel).setHardness(0.5F).setResistance(10F);
            blockPaperwall.carverHelper.setChiselBlockName("Paper Wall");
            blockPaperwall.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.paperwall.0.desc"), 0, "paper/box");
            blockPaperwall.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.paperwall.1.desc"), 1, "paper/throughMiddle");
            blockPaperwall.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.paperwall.2.desc"), 2, "paper/cross");
            blockPaperwall.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.paperwall.3.desc"), 3, "paper/sixSections");
            blockPaperwall.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.paperwall.4.desc"), 4, "paper/vertical");
            blockPaperwall.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.paperwall.5.desc"), 5, "paper/horizontal");
            blockPaperwall.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.paperwall.6.desc"), 6, "paper/floral");
            blockPaperwall.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.paperwall.7.desc"), 7, "paper/plain");
            blockPaperwall.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.paperwall.8.desc"), 8, "paper/door");

            blockPaperwall.carverHelper.register(blockPaperwall, "blockPaperwall");

        }

        if(Configurations.featureEnabled("woolenClay"))
        {
            blockWoolenClay = (BlockCarvable) new BlockCarvable().setCreativeTab(Chisel.tabChisel).setHardness(2F).setResistance(10F);
            blockWoolenClay.carverHelper.setChiselBlockName("Woolen Clay");

            for(int i = 0; i < 16; i++)
                blockWoolenClay.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.woolenClay." + i + ".desc"), i, "woolenClay/" + sGNames[i].replaceAll(" ", "").toLowerCase());
            blockWoolenClay.carverHelper.register(blockWoolenClay, "blockWoolenClay");


        }

        if(Configurations.featureEnabled("laboratory"))
        {
            blockLaboratory = (BlockCarvable) new BlockCarvable().setHardness(2.0F).setResistance(10F).setStepSound(Chisel.soundMetalFootstep);
            blockLaboratory.carverHelper.setChiselBlockName("blockLaboratory");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.0.desc"), 0, "laboratory/wallpanel");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.1.desc"), 1, "laboratory/dottedpanel");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.2.desc"), 2, "laboratory/largewall");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.3.desc"), 3, "laboratory/roundel");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.4.desc"), 4, "laboratory/wallvents");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.5.desc"), 5, "laboratory/largetile");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.6.desc"), 6, "laboratory/smalltile");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.7.desc"), 7, "laboratory/floortile");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.8.desc"), 8, "laboratory/checkertile");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.9.desc"), 9, "laboratory/clearscreen");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.10.desc"),10, "laboratory/fuzzscreen");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.11.desc"),11,"laboratory/largesteel");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.12.desc"),12,"laboratory/smallsteel");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.13.desc"),13,"laboratory/directionright");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.14.desc"),14,"laboratory/directionleft");
            blockLaboratory.carverHelper.addVariation(StatCollector.translateToLocal("chisel.tile.blockLaboratory.15.desc"),15,"laboratory/infocon");
            blockLaboratory.carverHelper.register(blockLaboratory, "blockLaboratory");
        }

        Blocks.stone.setHarvestLevel("chisel", 0, 0);
    }

}
