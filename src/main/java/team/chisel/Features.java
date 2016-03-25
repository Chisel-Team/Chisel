package team.chisel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import team.chisel.api.block.BlockCreator;
import team.chisel.api.block.BlockProvider;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.BlockCarvableBookshelf;
import team.chisel.common.block.BlockCarvablePane;
import team.chisel.common.block.ItemChiselBlock;
import team.chisel.common.carving.Carving;
import team.chisel.common.config.Configurations;

public enum Features {

    // @formatter:off
    ALUMINUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "aluminumblock", provider)
                    .setParentFolder("metals/aluminum")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    /*AMBER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "amber", provider)
                    .newVariation("amberblock")
                    .next("amberblock_top")
                    .build();
        }
    },// Not yet */

    ANDESITE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stone = Blocks.stone.getDefaultState();
            IProperty<EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("andesite", stone.withProperty(prop, EnumType.ANDESITE), -2);
            Carving.chisel.addVariation("andesite", stone.withProperty(prop, EnumType.ANDESITE_SMOOTH), -1);

            factory.newBlock(Material.rock, "andesite", provider)
                    .newVariation("andesitePillar")
                    .next("andesiteLBrick")
                    .next("andesiteOrnate")
                    .next("andesitePrismatic")
                    .next("andesiteTiles")
                    .next("andesiteDiagonalBricks")
                    .build();
        }
    },

//    ANIMATED {
//
//        @Override
//        void addBlocks(ChiselBlockFactory factory) {
//        }
//    },
//
//    ANIMATIONS {
//
//        @Override
//        void addBlocks(ChiselBlockFactory factory) {
//            factory.newBlock(Material.rock, "animations", provider).newVariation("archetype2").next("hades").next("hadesX32").next("shroud").next("strobe").build();
//        }
//    },

    ANTIBLOCK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "antiblock", provider)
                    .newVariation("black")
                    .next("red")
                    .next("green")
                    .next("brown")
                    .next("blue")
                    .next("purple")
                    .next("cyan")
                    .next("silver")
                    .next("gray")
                    .next("pink")
                    .next("lime")
                    .next("yellow")
                    .next("light_blue")
                    .next("magenta")
                    .next("orange")
                    .next("white")
                    .build();
        }
    },

    ARCANE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "arcane", provider)
                    .newVariation("ArcaneBorder")
                    .next("arcaneCrackAnim")
                    .next("arcaneMatrix")
                    .next("arcaneTile")
                    .next("bigBrick")
                    .next("BorderBrain")
                    .next("conduitGlowAnim")
                    .next("moonEngrave")
                    .next("moonGlowAnim")
                    .next("runes")
                    .next("runesGlow")
                    .next("runesGlowBase")
                    .next("runesGlowBase")
                    .next("runesGlowOverlay")
                    .next("runesGlowOverlay")
                    .next("singleRune")
                    .next("thaumcraftLogo")
                    .build();
        }
    },

//    AUTO_CHISEL {
//
//        @Override
//        void addBlocks(ChiselBlockFactory factory) {
//            factory.newBlock(Material.rock, "autoChisel", provider).newVariation("autoChisel").next("autoChisel16").next("autoChiselOld").next("autoChisel_automation").next("autoChisel_speed").next("autoChisel_stack").build();
//        }
//    },

    /*BIRDSTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "birdstone", provider).newVariation("birdstone-darkpanel").next("birdstone-dent").next("birdstone-emboss").next("birdstone-fourtile").next("birdstone-french-creep").next("birdstone-french").next("birdstone-french2").next("birdstone-marker").next("birdstone-ornate1").next("birdstone-ornate2").next("birdstone-rough").next("birdstone-smallbrick").next("birdstone-smallbroken").next("birdstone-smalltile").next("birdstone-smooth-creep").next("birdstone-smooth").build();
        }
    },*/

    BLOOD_MAGIC {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "bloodMagic", provider)
                    .newVariation("bloodRuneArranged")
                    .next("bloodRuneBricks")
                    .next("bloodRuneCarved")
                    .next("bloodRuneCarvedRadial")
                    .next("bloodRuneClassicPanel")
                    .next("bloodRuneTiles")
                    .next("RuneDiagonalBricks")
                    .build();
        }
    },

    BOOKSHELF {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("bookshelf", Blocks.bookshelf.getDefaultState(), -1);
            factory.newBlock(Material.wood, "bookshelf", new ChiselBlockProvider<>(BlockCarvableBookshelf::new, BlockCarvableBookshelf.class))
                    .newVariation("rainbow")
                    .next("necromancer-novice")
                    .next("necromancer")
                    .next("redtomes")
                    .next("abandoned")
                    .next("hoarder")
                    .next("brim")
                    .next("historician")
                    .build(b -> b.setSoundType(SoundType.WOOD)
                            .setHardness(1.5f));
        }
    },

    BRICK_CUSTOM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("brickCustom", Blocks.brick_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "brickCustom", provider)
                    .newVariation("large")
                    .next("mortarless")
                    .next("varied")
                    .next("aged")
                    .next("yellow")
                    .build();
        }
    },

    BRONZE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "bronze", provider)
                    .setParentFolder("metals/bronze")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    CARPET {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "carpet", provider)
                    .newVariation("black")
                    .next("red")
                    .next("green")
                    .next("brown")
                    .next("darkblue")
                    .next("purple")
                    .next("teal")
                    .next("grey")
                    .next("darkgrey")
                    .next("pink")
                    .next("lightgreen")
                    .next("yellow")
                    .next("lightblue")
                    .next("lily")
                    .next("orange")
                    .next("white")
                    .build();
        }
    },

    /*CERTUS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "certus", provider)
                    .setParentFolder("quartz/certus")
                    .newVariation("certusChiseled")
                    .next("certusPrismatic")
                    .next("certusPrismaticPattern")
                    .next("masonryCertus")
                    .build();
        }
    }, // There is no AE yet */

    CLOUD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "cloud", provider)
                    .newVariation("cloud")
                    .next("large")
                    .next("small")
                    .next("vertical")
                    .next("grid")
                    .build();
        }
    },

    COBALT {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "cobalt", provider)
                    .setParentFolder("metals/cobalt")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    COBBLESTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("cobblestone", Blocks.cobblestone.getDefaultState(), -1);
            factory.newBlock(Material.rock, "cobblestone", provider)
                    .newVariation("terrain-cobb-brickaligned")
                    .next("terrain-cob-detailedbrick")
                    .next("terrain-cob-smallbrick")
                    .next("terrain-cobblargetiledark")
                    .next("terrain-cobbsmalltile")
                    .next("terrain-cob-french")
                    .next("terrain-cob-french2")
                    .next("terrain-cobmoss-creepdungeon")
                    .next("terrain-mossysmalltiledark")
                    .next("terrain-pistonback-dungeontile")
                    .next("terrain-pistonback-darkcreeper")
                    .next("terrain-pistonback-darkdent")
                    .next("terrain-pistonback-darkemboss")
                    .next("terrain-pistonback-darkmarker")
                    .next("terrain-pistonback-darkpanel")
                    .build();
        }
    },

    COBBLESTONEMOSSY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("cobblestonemossy", Blocks.mossy_cobblestone.getDefaultState(), -1);
            factory.newBlock(Material.rock, "cobblestonemossy", provider)
                    .newVariation("terrain-cobb-brickaligned")
                    .next("terrain-cob-detailedbrick")
                    .next("terrain-cob-smallbrick")
                    .next("terrain-cobblargetiledark")
                    .next("terrain-cobbsmalltile")
                    .next("terrain-cob-french")
                    .next("terrain-cob-french2")
                    .next("terrain-cobmoss-creepdungeon")
                    .next("terrain-mossysmalltiledark")
                    .next("terrain-pistonback-dungeontile")
                    .next("terrain-pistonback-darkcreeper")
                    .next("terrain-pistonback-darkdent")
                    .next("terrain-pistonback-darkemboss")
                    .next("terrain-pistonback-darkmarker")
                    .next("terrain-pistonback-darkpanel")
                    .build();
        }
    },

    /*COLORED_SAND {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "coloredSand", provider)
                    .newVariation("black")
                    .next("blue")
                    .next("brown")
                    .next("cyan")
                    .next("gray")
                    .next("green")
                    .next("lightblue")
                    .next("lightgray")
                    .next("lime")
                    .next("magenta")
                    .next("orange")
                    .next("pink")
                    .next("purple")
                    .next("red")
                    .next("white")
                    .next("yellow")
                    .build();
        }
    },// Removed in 1.7 */

    CONCRETE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "concrete", provider)
                    .newVariation("default")
                    .next("block")
                    .next("doubleslab")
                    .next("blocks")
                    .next("weathered")
                    .next("weathered-block")
                    .next("weathered-doubleslab")
                    .next("weathered-blocks")
                    .next("weathered-half")
                    .next("weathered-block-half")
                    .next("asphalt")
                    .build();
        }
    },

    COPPER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "copper", provider)
                    .setParentFolder("metals/copper")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    /*CRAG_ROCK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "cragRock", provider)
                    .newVariation("terrain-cob-detailedbrick")
                    .next("terrain-cob-french")
                    .next("terrain-cob-french2")
                    .next("terrain-cob-smallbrick")
                    .next("terrain-cobb-brickaligned")
                    .next("terrain-cobblargetiledark")
                    .next("terrain-cobbsmalltile")
                    .next("terrain-cobmoss-creepdungeon")
                    .next("terrain-mossysmalltiledark")
                    .next("terrain-pistonback-darkcreeper")
                    .next("terrain-pistonback-darkdent")
                    .next("terrain-pistonback-darkemboss")
                    .next("terrain-pistonback-darkmarker")
                    .next("terrain-pistonback-darkpanel")
                    .next("terrain-pistonback-dungeontile")
                    .build();
        }
    }, // Pending retexture */

    /*DIAGONAL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "diagonal", provider)
                    .setParentFolder("prismarine/diagonal")
                    .newVariation("prismarineDiagonalAnim-0")
                    .next("prismarineDiagonalAnim-1")
                    .next("prismarineDiagonalAnim-2")
                    .next("prismarineDiagonalAnim-3")
                    .next("prismarineDiagonalAnim")
                    .build();
        }
    },// This shouldn't be here... I think*/

    DIAMOND {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("diamond", Blocks.diamond_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "diamond", provider)
                    .newVariation("terrain-diamond-embossed")
                    .next("terrain-diamond-gem")
                    .next("terrain-diamond-cells")
                    .next("terrain-diamond-space")
                    .next("terrain-diamond-spaceblack")
                    .next("terrain-diamond-simple")
                    .next("terrain-diamond-bismuth")
                    .next("terrain-diamond-crushed")
                    .next("terrain-diamond-four")
                    .next("terrain-diamond-fourornate")
                    .next("terrain-diamond-zelda")
                    .next("terrain-diamond-ornatelayer")
                    .build();
        }
    },

    DIORITE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stone = Blocks.stone.getDefaultState();
            IProperty<EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("diorite", stone.withProperty(prop, EnumType.DIORITE), -2);
            Carving.chisel.addVariation("diorite", stone.withProperty(prop, EnumType.DIORITE_SMOOTH), -1);

            factory.newBlock(Material.rock, "diorite", provider)
                    .newVariation("dioritePillar")
                    .next("dioriteLBrick")
                    .next("dioriteOrnate")
                    .next("dioritePrismatic")
                    .next("dioriteTiles")
                    .next("dioriteDiagonalBricks")
                    .build();
        }
    },

    DIRT {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("dirt", Blocks.dirt.getDefaultState(), -1);
            factory.newBlock(Material.rock, "dirt", provider)
                    .newVariation("bricks")
                    .next("netherbricks")
                    .next("bricks3")
                    .next("cobble")
                    .next("reinforcedCobbleDirt")
                    .next("reinforcedDirt")
                    .next("happy")
                    .next("bricks2")
                    .next("bricks+dirt2")
                    .next("hor")
                    .next("vert")
                    .next("layers")
                    .next("vertical")
                    .next("chunky")
                    .next("horizontal")
                    .next("plate")
                    .build();
        }
    },

    /*DREAMWOOD_PAPER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "dreamwood-paper", provider)
                    .newVariation("box")
                    .next("cross")
                    .next("door")
                    .next("floral")
                    .next("horizontal")
                    .next("plain")
                    .next("sixSections")
                    .next("throughMiddle")
                    .next("vertical")
                    .build();
        }
    },

    DREAMWOOD_PLANKS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "dreamwood-planks", provider)
                    .newVariation("blinds")
                    .next("chaotic-hor")
                    .next("chaotic")
                    .next("clean")
                    .next("crate-fancy")
                    .next("crate")
                    .next("crateex")
                    .next("double")
                    .setTextureLocation("dreamwood-planks/double-side")
                    .setTextureLocation("dreamwood-planks/double-top", Axis.Y)
                    .next("fancy")
                    .next("large")
                    .next("panel-nails")
                    .next("parquet")
                    .next("short")
                    .next("vertical-uneven")
                    .next("vertical")
                    .build();
        }
    },

    DREAMWOOD_RAW {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "dreamwood-raw", provider)
                    .newVariation("dreamwoodLogSide")
                    .next("dreamwoodLogTop")
                    .next("dreamwoodPileSide")
                    .next("dreamwoodPileTop")
                    .build();
        }
    }, // No dreamwood at this moment */

    ELECTRUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "electrum", provider)
                    .setParentFolder("metals/electrum")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    /*ELEMENTIUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "elementium", provider)
                    .setParentFolder("botania/elementium")
                    .newVariation("adv")
                    .next("bolted")
                    .next("caution")
                    .next("crate")
                    .next("egregious")
                    .next("elementiumEye")
                    .next("thermal")
                    .build();
        }
    }, // */

    EMERALD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("emerald", Blocks.emerald_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "emerald", provider)
                    .newVariation("panel")
                    .next("panelclassic")
                    .next("smooth")
                    .next("chunk")
                    .next("goldborder")
                    .next("zelda")
                    .next("cell")
                    .next("cellbismuth")
                    .next("four")
                    .next("fourornate")
                    .next("ornate")
                    .next("masonryEmerald")
                    .next("emeraldCircle")
                    .next("emeraldPrismatic")
                    .build();
        }
    },

    END_PURPUR {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "end_purpur", provider)
                    .newVariation("shulker")
                    .next("tilePurpur")
                    .next("tileBrokenPurpur")
                    .next("purpurPrismarine")
                    .next("purpurBricks")
                    .next("purpurCobble")
                    .next("arcanePurpur")
                    .next("purpurLargeTile")
                    .next("borderPurpur")
                    .next("purpurOrnate")
                    .next("masonryPurpur")
                    .build();
        }
    },

    ENDER_PEARL_BLOCK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "ender_pearl_block", provider)
                    .newVariation("resonantSolid")
                    .next("enderZelda")
                    .next("enderEye")
                    .next("resonantBricks")
                    .build();
        }
    },

    ENDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("endstone", Blocks.end_stone.getDefaultState(), -1);
            factory.newBlock(Material.rock, "endstone", provider)
                    .newVariation("chaoticBricks")
                    .next("CheckeredTile")
                    .next("enderCircuit")
                    .next("endFrenchBricks")
                    .next("endPillar")
                    .next("endStoneEtched")
                    .next("endPrismatic")
                    .next("endStoneChunk")
                    .next("EnderFrame")
                    .next("arcaneEndStone")
                    .next("framedEndStone")
                    .next("endStoneOrnate")
                    .next("endStoneLargeTile")
                    .next("enderDiagonalBrick")
                    .next("masonryEnder")
                    .build();
        }
    },

    FACTORY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "factory", provider)
                    .newVariation("dots")
                    .next("rust2")
                    .next("rust")
                    .next("platex")
                    .next("wireframewhite")
                    .next("wireframe")
                    .next("hazard")
                    .next("hazardorange")
                    .next("circuit")
                    .next("metalbox")
                    .next("goldplate")
                    .next("goldplating")
                    .next("grinder")
                    .next("plating")
                    .next("rustplates")
                    .next("column")
                    .next("frameblue")
                    .next("iceiceice")
                    .next("tilemosaic")
                    .next("vent")
                    .next("wireframeblue")
                    .build();
        }
    },

    FANTASY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "fantasy", provider)
                    .newVariation("brick")
                    .next("brick-faded")
                    .next("brick-wear")
                    .next("bricks")
                    .next("decor")
                    .next("decor-block")
                    .next("pillar")
                    .next("pillar-decorated")
                    .next("gold-decor-1")
                    .next("gold-decor-2")
                    .next("gold-decor-3")
                    .next("gold-decor-4")
                    .next("plate")
                    .next("block")
                    .next("bricks-chaotic")
                    .next("bricks-wear")
                    .build();
        }
    },

    FANTASY2 {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "fantasy2", provider)
                    .newVariation("brick")
                    .next("brick-faded")
                    .next("brick-wear")
                    .next("bricks")
                    .next("decor")
                    .next("decor-block")
                    .next("pillar")
                    .next("pillar-decorated")
                    .next("gold-decor-1")
                    .next("gold-decor-2")
                    .next("gold-decor-3")
                    .next("gold-decor-4")
                    .next("plate")
                    .next("block")
                    .next("bricks-chaotic")
                    .next("bricks-wear")
                    .build();
        }
    },

    //TODO: Futura - Where the fork did everything go

    FUTURA {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "futura", provider)
                    .newVariation("circuitPlate")
                    .build();
        }
    },

    GLASS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("glass", Blocks.glass.getDefaultState(), -1);
            factory.newBlock(Material.glass, "glass", provider)
                    .newVariation("terrain-glassbubble")
                    .next("terrain-glass-chinese")
                    .next("japanese")
                    .next("terrain-glassdungeon")
                    .next("terrain-glasslight")
                    .next("terrain-glassnoborder")
                    .next("terrain-glass-ornatesteel")
                    .next("terrain-glass-screen")
                    .next("terrain-glassshale")
                    .next("terrain-glass-steelframe")
                    .next("terrain-glassstone")
                    .next("terrain-glassstreak")
                    .next("terrain-glass-thickgrid")
                    .next("terrain-glass-thingrid")
                    .next("a1-glasswindow-ironfencemodern")
                    .next("chrono")
                    .build();
        }
    },

    GLASSDYED {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            for(int c = 0; c < dyeColors.length; c++)
            {
                factory.newBlock(Material.glass, "glassdyed"+dyeColors[c], provider)
                        .setParentFolder("glassdyed")
                        .newVariation(dyeColors[c]+"-bubble")
                        .next(dyeColors[c]+"-panel")
                        .next(dyeColors[c]+"-panel-fancy")
                        .next(dyeColors[c]+"-transparent")
                        .next(dyeColors[c]+"-forestry")
                        .build();
            }
        }
    },

    GLASSPANE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.glass, "glasspane", new ChiselBlockProvider<BlockCarvablePane>(new BlockCreator<BlockCarvablePane>() {
                @Override
                public BlockCarvablePane createBlock(Material mat, int index, int maxVariation, VariationData... data) {
                    return new BlockCarvablePane(mat, false, index, maxVariation, data);
                }
            }, BlockCarvablePane.class))
                    .newVariation("chinese")
                    .next("chinese2")
                    .next("japanese")
                    .next("japanese2")
                    .next("terrain-glass-screen")
                    .next("terrain-glassbubble")
                    .next("terrain-glassnoborder")
                    .next("terrain-glassstreak")
                    .build();
        }
    },

    GLASSPANEDYED {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            for(int c = 0; c < dyeColors.length; c++)
            {
                factory.newBlock(Material.glass, "glasspanedyed"+dyeColors[c], provider)
                        .setParentFolder("glasspanedyed")
                        .newVariation(dyeColors[c]+"-bubble")
                        .next(dyeColors[c]+"-panel")
                        .next(dyeColors[c]+"-panel-fancy")
                        .next(dyeColors[c]+"-transparent")
                        .next(dyeColors[c]+"-quad")
                        .next(dyeColors[c]+"-quad-fancy")
                        .build();
            }
        }
    },

    GOLD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("gold", Blocks.gold_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "gold", provider)
                    .newVariation("terrain-gold-largeingot")
                    .next("terrain-gold-smallingot")
                    .next("terrain-gold-brick")
                    .next("terrain-gold-cart")
                    .next("terrain-gold-coin-heads")
                    .next("terrain-gold-coin-tails")
                    .next("terrain-gold-crate-dark")
                    .next("terrain-gold-crate-light")
                    .next("terrain-gold-plates")
                    .next("terrain-gold-rivets")
                    .next("terrain-gold-star")
                    .next("terrain-gold-space")
                    .next("terrain-gold-spaceblack")
                    .next("terrain-gold-simple")
                    .next("goldEye")
                    .build();
        }
    },

    GRANITE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stone = Blocks.stone.getDefaultState();
            IProperty<EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("granite", stone.withProperty(prop, EnumType.GRANITE), -2);
            Carving.chisel.addVariation("granite", stone.withProperty(prop, EnumType.GRANITE_SMOOTH), -1);

            factory.newBlock(Material.rock, "granite", provider)
                    .newVariation("granitePillar")
                    .next("graniteLBrick")
                    .next("graniteOrnate")
                    .next("granitePrismatic")
                    .next("graniteTiles")
                    .next("graniteDiagonalBricks")
                    .build();
        }
    },

    GRIMSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "grimstone", provider)
                    .newVariation("grimstone")
                    .next("smooth")
                    .next("hate")
                    .next("chiseled")
                    .next("blocks")
                    .next("blocks-rough")
                    .next("brick")
                    .next("largebricks")
                    .next("platform")
                    .next("platform-tiles")
                    .next("construction")
                    .next("fancy-tiles")
                    .next("plate")
                    .next("plate-rough")
                    .next("flaky")
                    .next("chunks")
                    .next("roughblocks")
                    .next("tiles")
                    .build();
        }
    },

    HEX_PLATING {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "hexPlating", provider)
                    .newVariation("hexBase") //TODO: Colored+Glowy stuff
                    .next("hexNew")
                    .build();
        }
    },

    HOLYSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "holystone", provider)
                    .newVariation("holystone")
                    .next("smooth")
                    .next("love")
                    .next("chiseled")
                    .next("blocks")
                    .next("blocks-rough")
                    .next("brick")
                    .next("largebricks")
                    .next("platform")
                    .next("platform-tiles")
                    .next("construction")
                    .next("fancy-tiles")
                    .next("plate")
                    .next("plate-rough")
                    .build();
        }
    },

    ICE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("ice", Blocks.ice.getDefaultState(), -1);
            factory.newBlock(Material.rock, "ice", provider)
                    .newVariation("a1-ice-light")
                    .next("a1-stonecobble-icecobble")
                    .next("a1-netherbrick-ice")
                    .next("a1-stonecobble-icebrick")
                    .next("a1-stonecobble-icebricksmall")
                    .next("a1-stonecobble-icedungeon")
                    .next("a1-stonecobble-icefour")
                    .next("a1-stonecobble-icefrench")
                    .next("sunkentiles")
                    .next("tiles")
                    .next("a1-stonecobble-icepanel")
                    .next("a1-stoneslab-ice")
                    .next("zelda")
                    .next("bismuth")
                    .next("poison")
                    .next("scribbles")
                    .build();
        }
    },

    ICEPILLAR {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "icepillar", provider)
                    .newVariation("plainplain")
                    .next("plaingreek")
                    .next("greekplain")
                    .next("greekgreek")
                    .next("convexplain")
                    .next("carved")
                    .next("ornamental")
                    .build();
        }
    },

    INVAR {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "invar", provider)
                    .setParentFolder("metals/invar")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    IRON {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("iron", Blocks.iron_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "iron", provider)
                    .newVariation("terrain-iron-largeingot")
                    .next("terrain-iron-smallingot")
                    .next("terrain-iron-gears")
                    .next("terrain-iron-brick")
                    .next("terrain-iron-plates")
                    .next("terrain-iron-rivets")
                    .next("terrain-iron-coin-heads")
                    .next("terrain-iron-coin-tails")
                    .next("terrain-iron-crate-dark")
                    .next("terrain-iron-crate-light")
                    .next("terrain-iron-moon")
                    .next("terrain-iron-space")
                    .next("terrain-iron-spaceblack")
                    .next("terrain-iron-vents")
                    .next("terrain-iron-simple")
                    .build();
        }
    },

    IRONPANE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "ironpane", provider)
                    .newVariation("fenceIron")
                    .next("barbedwire")
                    .next("cage")
                    .next("fenceIronTop")
                    .next("terrain-glass-thickgrid")
                    .next("terrain-glass-thingrid")
                    .next("terrain-glass-ornatesteel")
                    .next("bars")
                    .next("spikes")
                    .next("a1-ironbars-ironclassicnew")
                    .next("a1-ironbars-ironfence")
                    .next("a1-ironbars-ironfencemodern")
                    .next("a1-ironbars-ironclassic")
                    .build();
        }
    },

    LABORATORY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "laboratory", provider)
                    .newVariation("wallpanel")
                    .next("dottedpanel")
                    .next("largewall")
                    .next("roundel")
                    .next("wallvents")
                    .next("largetile")
                    .next("smalltile")
                    .next("floortile")
                    .next("checkertile")
                    .next("clearscreen")
                    .next("fuzzscreen")
                    .next("largesteel")
                    .next("smallsteel")
                    .next("directionright")
                    .next("directionleft")
                    .next("infocon")
                    .build();
        }
    },

    LAPIS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("lapis", Blocks.lapis_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "lapis", provider)
                    .newVariation("terrain-lapisblock-chunky")
                    .next("terrain-lapisblock-panel")
                    .next("terrain-lapisblock-zelda")
                    .next("terrain-lapisornate")
                    .next("terrain-lapistile")
                    .next("a1-blocklapis-panel")
                    .next("a1-blocklapis-smooth")
                    .next("a1-blocklapis-ornatelayer")
                    .next("masonryLapis")
                    .build();
        }
    },

    LAVASTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "lavastone", provider)
                    .newVariation("cobble")
                    .next("black")
                    .next("tiles")
                    .next("chaotic")
                    .next("creeper")
                    .next("panel")
                    .next("panel-ornate")
                    .next("dark")
                    .build();
        }
    },

    LEAD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "lead", provider)
                    .setParentFolder("metals/lead")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    /*LEAVES {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "leaves", provider)
                    .newVariation("christmasBalls")
                    .next("christmasBalls_opaque")
                    .next("christmasLights")
                    .next("christmasLights_opaque")
                    .next("dead")
                    .next("dead_opaque")
                    .next("fancy")
                    .next("fancy_opaque")
                    .next("pinkpetal")
                    .next("pinkpetal_opaque")
                    .next("red_roses")
                    .next("red_roses_opaque")
                    .next("roses")
                    .next("roses_opaque")
                    .build();
        }
    },*/

    LIGHTSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "lightstone", provider)
                    .newVariation("terrain-sulphur-cobble")
                    .next("terrain-sulphur-corroded")
                    .next("terrain-sulphur-glass")
                    .next("terrain-sulphur-neon")
                    .next("terrain-sulphur-ornate")
                    .next("terrain-sulphur-rocky")
                    .next("terrain-sulphur-shale")
                    .next("terrain-sulphur-tile")
                    .next("terrain-sulphur-weavelanternlight")
                    .next("a1-glowstone-cobble")
                    .next("a1-glowstone-growth")
                    .next("a1-glowstone-layers")
                    .next("a1-glowstone-tilecorroded")
                    .next("glowstone-bismuth")
                    .next("glowstone-bismuth-panel")
                    .build();
        }
    },

    LIMESTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "limestone", provider)
                    .newVariation("limestone")
                    .next("terrain-cobbsmalltilelight")
                    .next("terrain-cob-frenchlight")
                    .next("terrain-cob-french2light")
                    .next("terrain-cobmoss-creepdungeonlight")
                    .next("terrain-cob-smallbricklight")
                    .next("terrain-mossysmalltilelight")
                    .next("terrain-pistonback-dungeon")
                    .next("terrain-pistonback-dungeonornate")
                    .next("terrain-pistonback-dungeonvent")
                    .next("terrain-pistonback-lightcreeper")
                    .next("terrain-pistonback-lightdent")
                    .next("terrain-pistonback-lightemboss")
                    .next("terrain-pistonback-lightfour")
                    .next("terrain-pistonback-lightmarker")
                    .next("terrain-pistonback-lightpanel")
                    .build();
        }
    },

    LINE_MARKING {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "line-marking", provider)
                    .newVariation("double-white-center")
                    .next("double-white-long")
                    .next("double-yellow-center")
                    .next("double-yellow-long")
                    .next("white-center")
                    .next("white-long")
                    .next("yellow-center")
                    .next("yellow-long")
                    .build();
        }
    },

    /*LIVINGROCK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "livingrock", provider)
                    .setParentFolder("botania/livingrock")
                    .newVariation("masonryLivingstone")
                    .build();
        }
    },

    LIVINGWOOD_PAPER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "livingwood-paper", provider)
                    .newVariation("box")
                    .next("cross")
                    .next("door")
                    .next("floral")
                    .next("horizontal")
                    .next("plain")
                    .next("sixSections")
                    .next("throughMiddle")
                    .next("vertical")
                    .build();
        }
    },

    LIVINGWOOD_PLANKS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "livingwood-planks", provider)
                    .newVariation("blinds")
                    .next("chaotic-hor")
                    .next("chaotic")
                    .next("clean")
                    .next("crate-fancy")
                    .next("crate")
                    .next("crateex")
                    .next("double")
                    .next("fancy")
                    .next("large")
                    .next("panel-nails")
                    .next("parquet")
                    .next("short")
                    .next("vertical-uneven")
                    .next("vertical")
                    .build();
        }
    },

    LIVINGWOOD_RAW {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "livingwood-raw", provider)
                    .newVariation("livingwoodLogSide")
                    .next("livingwoodLogTop")
                    .next("livingwoodPileSide")
                    .next("livingwoodPileTop")
                    .build();
        }
    },

    MANASTEEL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "manasteel", provider)
                    .newVariation("adv")
                    .next("bolted")
                    .next("card")
                    .next("caution")
                    .next("crate")
                    .next("egregious")
                    .next("elementiumEye-0-0")
                    .next("elementiumEye-0-1")
                    .next("elementiumEye-1-0")
                    .next("elementiumEye-1-1")
                    .next("elementiumEye-2-0")
                    .next("elementiumEye-2-1")
                    .next("thermal")
                    .build();
        }
    },*/

    MARBLE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "marble", provider)
                    .newVariation("raw")
                    .next("a1-stoneornamental-marblebrick")
                    .next("a1-stoneornamental-marbleclassicpanel")
                    .next("a1-stoneornamental-marbleornate")
                    .next("panel")
                    .next("block")
                    .next("terrain-pistonback-marblecreeperdark")
                    .next("terrain-pistonback-marblecreeperlight")
                    .next("a1-stoneornamental-marblecarved")
                    .next("a1-stoneornamental-marblecarvedradial")
                    .next("terrain-pistonback-marbledent")
                    .next("terrain-pistonback-marbledent-small")
                    .next("marble-bricks")
                    .next("marble-arranged-bricks")
                    .next("marble-fancy-bricks")
                    .next("marble-blocks")
                    .build();
        }
    },

    MARBLEPILLAR {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            if (Configurations.oldPillars) {
                factory.newBlock(Material.rock, "marblepillarold", provider)
                        .newVariation("column")
                        .next("capstone")
                        .next("base")
                        .next("small")
                        .next("pillar-carved")
                        .next("a1-stoneornamental-marblegreek")
                        .next("a1-stonepillar-greek")
                        .next("a1-stonepillar-plain")
                        .next("a1-stonepillar-greektopplain")
                        .next("a1-stonepillar-plaintopplain")
                        .next("a1-stonepillar-greekbottomplain")
                        .next("a1-stonepillar-plainbottomplain")
                        .next("a1-stonepillar-greektopgreek")
                        .next("a1-stonepillar-plaintopgreek")
                        .next("a1-stonepillar-greekbottomgreek")
                        .next("a1-stonepillar-plainbottomgreek")
                        .build();
            } else {
                factory.newBlock(Material.rock, "marblepillar", provider)
                        .newVariation("pillar")
                        .next("default")
                        .next("simple")
                        .next("convex")
                        .next("rough")
                        .next("greekdecor")
                        .next("greekgreek")
                        .next("greekplain")
                        .next("plaindecor")
                        .next("plaingreek")
                        .next("plainplain")
                        .next("widedecor")
                        .next("widegreek")
                        .next("wideplain")
                        .next("carved")
                        .next("ornamental")
                        .build();
            }
        }
    },

    /*MARBLEPILLARSLAB { //TODO: make a carpenters' blocks-esque system for this
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "marblepillarslab", provider)
                    .newVariation("carved")
                    .setTextureLocation("marblepillarslab/carved-side")
                    .setTextureLocation("marblepillarslab/carved-top", Axis.Y)
                    .next("convex")
                    .setTextureLocation("marblepillarslab/convex-side")
                    .setTextureLocation("marblepillarslab/convex-top", Axis.Y)
                    .next("default")
                    .setTextureLocation("marblepillarslab/default-side")
                    .setTextureLocation("marblepillarslab/default-top", Axis.Y)
                    .next("greekdecor")
                    .setTextureLocation("marblepillarslab/greekdecor-side")
                    .setTextureLocation("marblepillarslab/greekdecor-top", Axis.Y)
                    .next("greekgreek")
                    .setTextureLocation("marblepillarslab/greekgreek-side")
                    .setTextureLocation("marblepillarslab/greekgreek-top", Axis.Y)
                    .next("greekplain")
                    .setTextureLocation("marblepillarslab/greekplain-side")
                    .setTextureLocation("marblepillarslab/greekplain-top", Axis.Y)
                    .next("ornamental")
                    .setTextureLocation("marblepillarslab/ornamental-side")
                    .setTextureLocation("marblepillarslab/ornamental-top", Axis.Y)
                    .next("pillar")
                    .setTextureLocation("marblepillarslab/pillar-side")
                    .setTextureLocation("marblepillarslab/pillar-top", Axis.Y)
                    .next("plaindecor")
                    .setTextureLocation("marblepillarslab/plaindecor-side")
                    .setTextureLocation("marblepillarslab/plaindecor-top", Axis.Y)
                    .next("plaingreek")
                    .setTextureLocation("marblepillarslab/plaingreek-side")
                    .setTextureLocation("marblepillarslab/plaingreek-top", Axis.Y)
                    .next("plainplain")
                    .setTextureLocation("marblepillarslab/plainplain-side")
                    .setTextureLocation("marblepillarslab/plainplain-top", Axis.Y)
                    .next("rough")
                    .setTextureLocation("marblepillarslab/rough-side")
                    .setTextureLocation("marblepillarslab/rough-top", Axis.Y)
                    .next("simple")
                    .setTextureLocation("marblepillarslab/simple-side")
                    .setTextureLocation("marblepillarslab/simple-top", Axis.Y)
                    .next("widedecor")
                    .setTextureLocation("marblepillarslab/widedecor-side")
                    .setTextureLocation("marblepillarslab/widedecor-top", Axis.Y)
                    .next("widegreek")
                    .setTextureLocation("marblepillarslab/widegreek-side")
                    .setTextureLocation("marblepillarslab/widegreek-top", Axis.Y)
                    .next("wideplain")
                    .setTextureLocation("marblepillarslab/wideplain-side")
                    .setTextureLocation("marblepillarslab/wideplain-top", Axis.Y)
                    .build();
        }
    },

    MARBLEPILLARSLABOLD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "marblepillarslabold", provider)
                    .newVariation("a1-stoneornamental-marblegreek")
                    .setTextureLocation("marblepillarslabold/a1-stoneornamental-marblegreek-side")
                    .setTextureLocation("marblepillarslabold/a1-stoneornamental-marblegreek-top", Axis.Y)
                    .next("a1-stonepillar-greek")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-greek-side")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-greek-top", Axis.Y)
                    .next("a1-stonepillar-greekbottomgreek")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-greekbottomgreek-side")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-greekbottomgreek-top", Axis.Y)
                    .next("a1-stonepillar-greekbottomplain")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-greekbottomplain-side")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-greekbottomplain-top", Axis.Y)
                    .next("a1-stonepillar-greektopgreek")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-greektopgreek-side")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-greektopgreek-top", Axis.Y)
                    .next("a1-stonepillar-greektopplain")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-greektopplain-side")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-greektopplain-top", Axis.Y)
                    .next("a1-stonepillar-plain")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-plain-side")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-plain-top", Axis.Y)
                    .next("a1-stonepillar-plainbottomgreek")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-plainbottomgreek-side")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-plainbottomgreek-top", Axis.Y)
                    .next("a1-stonepillar-plainbottomplain")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-plainbottomplain-side")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-plainbottomplain-top", Axis.Y)
                    .next("a1-stonepillar-plaintopgreek")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-plaintopgreek-side")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-plaintopgreek-top", Axis.Y)
                    .next("a1-stonepillar-plaintopplain")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-plaintopplain-side")
                    .setTextureLocation("marblepillarslabold/a1-stonepillar-plaintopplain-top", Axis.Y)
                    .next("base")
                    .setTextureLocation("marblepillarslabold/base-side")
                    .setTextureLocation("marblepillarslabold/base-top", Axis.Y)
                    .next("capstone")
                    .setTextureLocation("marblepillarslabold/capstone-side")
                    .setTextureLocation("marblepillarslabold/capstone-top", Axis.Y)
                    .next("column")
                    .setTextureLocation("marblepillarslabold/column-side")
                    .setTextureLocation("marblepillarslabold/column-top", Axis.Y)
                    .next("pillar-carved")
                    .setTextureLocation("marblepillarslabold/pillar-carved-side")
                    .setTextureLocation("marblepillarslabold/pillar-carved-top", Axis.Y)
                    .next("small")
                    .setTextureLocation("marblepillarslabold/small-side")
                    .setTextureLocation("marblepillarslabold/small-top", Axis.Y)
                    .build();
        }
    },

    MARBLESLAB {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "marbleslab", provider)
                    .newVariation("a1-stoneornamental-marblebrick")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marblebrick-side")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marblebrick-top", Axis.Y)
                    .next("a1-stoneornamental-marblecarved")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marblecarved-side")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marblecarved-top", Axis.Y)
                    .next("a1-stoneornamental-marblecarvedradial")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marblecarvedradial-side")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marblecarvedradial-top", Axis.Y)
                    .next("a1-stoneornamental-marbleclassicpanel")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marbleclassicpanel-side")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marbleclassicpanel-top", Axis.Y)
                    .next("a1-stoneornamental-marbleornate")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marbleornate-side")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marbleornate-top", Axis.Y)
                    .next("a1-stoneornamental-marblepanel")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marblepanel-side")
                    .setTextureLocation("marbleslab/a1-stoneornamental-marblepanel-top", Axis.Y)
                    .next("marble-arranged-bricks")
                    .next("marble-blocks")
                    .next("marble-bricks")
                    .next("marble-fancy-bricks")
                    .setTextureLocation("marbleslab/marble-fancy-bricks-side")
                    .setTextureLocation("marbleslab/marble-fancy-bricks-top", Axis.Y)
                    .next("terrain-pistonback-marble")
                    .setTextureLocation("marbleslab/terrain-pistonback-marble-side")
                    .setTextureLocation("marbleslab/terrain-pistonback-marble-top", Axis.Y)
                    .next("terrain-pistonback-marblecreeperdark")
                    .setTextureLocation("marbleslab/terrain-pistonback-marblecreeperdark-side")
                    .setTextureLocation("marbleslab/terrain-pistonback-marblecreeperdark-top", Axis.Y)
                    .next("terrain-pistonback-marblecreeperlight")
                    .setTextureLocation("marbleslab/terrain-pistonback-marblecreeperlight-side")
                    .setTextureLocation("marbleslab/terrain-pistonback-marblecreeperlight-top", Axis.Y)
                    .next("terrain-pistonback-marbledent-small")
                    .setTextureLocation("marbleslab/terrain-pistonback-marbledent-small-side")
                    .setTextureLocation("marbleslab/terrain-pistonback-marbledent-small-top", Axis.Y)
                    .next("terrain-pistonback-marbledent")
                    .setTextureLocation("marbleslab/terrain-pistonback-marbledent-side")
                    .setTextureLocation("marbleslab/terrain-pistonback-marbledent-top", Axis.Y)
                    .build();
        }
    },//*/

    /*MAZESTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "mazestone", provider)
                    .newVariation("circular")
                    .next("cobbled")
                    .next("intricate")
                    .next("masonryMazestone")
                    .next("mazestoneDiagonals")
                    .next("prismatic")
                    .next("prismaticMazestone")
                    .build();
        }
    },// Twilight forest is not out yet for 1.9 */

    MILITARY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "military", provider)
                    .newVariation("imperialCamo")
                    .next("imperialCamoSecluded")
                    .next("imperialCautionOrange")
                    .next("imperialCautionWhite")
                    .next("imperialPlate")
                    .next("rebelCamo")
                    .next("rebelCamoSecluded")
                    .next("rebelCautionRed")
                    .next("rebelCautionWhite")
                    .next("rebelPlate")
                    .build();
        }
    },

    NETHERBRICK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("netherbrick", Blocks.nether_brick.getDefaultState(), -1);
            factory.newBlock(Material.rock, "netherbrick", provider)
                    .newVariation("a1-netherbrick-brinstar")
                    .next("a1-netherbrick-classicspatter")
                    .next("a1-netherbrick-guts")
                    .next("a1-netherbrick-gutsdark")
                    .next("a1-netherbrick-gutssmall")
                    .next("a1-netherbrick-lavabrinstar")
                    .next("a1-netherbrick-lavabrown")
                    .next("a1-netherbrick-lavaobsidian")
                    .next("a1-netherbrick-lavastonedark")
                    .next("a1-netherbrick-meat")
                    .next("a1-netherbrick-meatred")
                    .next("a1-netherbrick-meatredsmall")
                    .next("a1-netherbrick-meatsmall")
                    .next("a1-netherbrick-red")
                    .next("a1-netherbrick-redsmall")
                    .next("netherFancyBricks")
                    .build();
        }
    },

    NETHERRACK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("netherrack", Blocks.netherrack.getDefaultState(), -1);
            factory.newBlock(Material.rock, "netherrack", provider)
                    .newVariation("a1-netherrack-bloodgravel")
                    .next("a1-netherrack-bloodrock")
                    .next("a1-netherrack-bloodrockgrey")
                    .next("a1-netherrack-brinstar")
                    .next("a1-netherrack-brinstarshale")
                    .next("a1-netherrack-classic")
                    .next("a1-netherrack-classicspatter")
                    .next("a1-netherrack-guts")
                    .next("a1-netherrack-gutsdark")
                    .next("a1-netherrack-meat")
                    .next("a1-netherrack-meatred")
                    .next("a1-netherrack-meatrock")
                    .next("a1-netherrack-red")
                    .next("a1-netherrack-wells")
                    .build();
        }
    },

    NICKEL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "nickel", provider)
                    .setParentFolder("metals/nickel")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    OBSIDIAN {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("obsidian", Blocks.obsidian.getDefaultState(), -1);
            factory.newBlock(Material.rock, "obsidian", provider)
                    .newVariation("pillar")
                    .next("pillar-quartz")
                    .next("chiseled")
                    .next("panel-shiny")
                    .next("panel")
                    .next("chunks")
                    .next("growth")
                    .next("crystal")
                    .next("map-a")
                    .next("map-b")
                    .next("panel-light")
                    .next("blocks")
                    .next("tiles")
                    .next("greek")
                    .next("crate")
                    .build();
        }
    },

    PAPER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "paper", provider)
                    .newVariation("box")
                    .next("throughMiddle")
                    .next("cross")
                    .next("sixSections")
                    .next("vertical")
                    .next("horizontal")
                    .next("floral")
                    .next("plain")
                    .next("door")
                    .build();
        }
    },

//    PARTICLES {
//
//        @Override
//        void addBlocks(ChiselBlockFactory factory) {
//            factory.newBlock(Material.rock, "particles", provider).setParentFolder("grimstone").newVariation("star").build();
//        }
//    },

    PLANKS {
        private final String[] plank_names = { "oak", "spruce", "birch", "jungle", "acacia", "dark-oak" };

        @Override
        void addBlocks(ChiselBlockFactory factory)
        {
            for (String plank_name : plank_names)
            {
                factory.newBlock(Material.rock, "planks-" + plank_name, provider)
                        .newVariation("clean")
                        .next("short")
                        .next("fancy")
                        .next("panel-nails")
                        .next("double")
                        .next("crate")
                        .next("crate-fancy")
                        .next("large")
                        .next("vertical")
                        .next("vertical-uneven")
                        .next("parquet")
                        .next("blinds")
                        .next("crateex")
                        .next("chaotic-hor")
                        .next("chaotic")
                        .build();
            }
        }
    },

    PLATINUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "platinum", provider)
                    .setParentFolder("metals/platinum")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    PRESENT {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "present", provider)
                    .newVariation("presentChest0")
                    .next("presentChest1")
                    .next("presentChest2")
                    .next("presentChest3")
                    .next("presentChest4")
                    .next("presentChest5")
                    .next("presentChest6")
                    .next("presentChest7")
                    .next("presentChest8")
                    .next("presentChest9")
                    .next("presentChest10")
                    .next("presentChest11")
                    .next("presentChest12")
                    .next("presentChest13")
                    .next("presentChest14")
                    .next("presentChest15")
                    .build();
        }
    },

    PRISMARINE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "prismarine", provider)
                    .newVariation("prismarineCircular")
                    .next("prismarineBrick")
                    .next("masonryPrismarine")
                    .next("masonryPrismarineAnim")
                    .build();
        }
    },

    PUMPKIN {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("pumpkin", Blocks.pumpkin.getDefaultState(), -1);
            factory.newBlock(Material.rock, "pumpkin", provider)
                    .newVariation("pumpkin_face_off")
                    .next("pumpkin_face_on")
                    .next("pumpkin_face_1_off")
                    .next("pumpkin_face_1_on")
                    .next("pumpkin_face_2_off")
                    .next("pumpkin_face_2_on")
                    .next("pumpkin_face_3_off")
                    .next("pumpkin_face_3_on")
                    .next("pumpkin_face_4_off")
                    .next("pumpkin_face_4_on")
                    .next("pumpkin_face_5_off")
                    .next("pumpkin_face_5_on")
                    .next("pumpkin_face_6_off")
                    .next("pumpkin_face_6_on")
                    .next("pumpkin_face_7_off")
                    .next("pumpkin_face_7_on")
                    .next("pumpkin_face_8_off")
                    .next("pumpkin_face_8_on")
                    .next("pumpkin_face_9_off")
                    .next("pumpkin_face_9_on")
                    .next("pumpkin_face_10_off")
                    .next("pumpkin_face_10_on")
                    .next("pumpkin_face_11_off")
                    .next("pumpkin_face_11_on")
                    .next("pumpkin_face_12_off")
                    .next("pumpkin_face_12_on")
                    .next("pumpkin_face_13_off")
                    .next("pumpkin_face_13_on")
                    .next("pumpkin_face_14_off")
                    .next("pumpkin_face_14_on")
                    .next("pumpkin_face_15_off")
                    .next("pumpkin_face_15_on")
                    .next("pumpkin_face_16_off")
                    .next("pumpkin_face_16_on")
                    .next("pumpkin_face_17_off")
                    .next("pumpkin_face_17_on")
                    .build();
        }
    },

    QUARTZ {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "quartz", provider)
                    .newVariation("quartzChiseled")
                    .next("quartzPrismaticPattern")
                    .next("masonryQuartz")
                    .build();
        }
    },

    REDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("redstone", Blocks.redstone_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "redstone", provider)
                    .newVariation("smooth")
                    .next("block")
                    .next("blocks")
                    .next("bricks")
                    .next("smallbricks")
                    .next("smallchaotic")
                    .next("chiseled")
                    .next("ere")
                    .next("ornate-tiles")
                    .next("pillar")
                    .next("tiles")
                    .next("circuit")
                    .next("supaplex")
                    .next("a1-blockredstone-skullred")
                    .next("a1-blockredstone-redstonezelda")
                    .next("masonryRedstone")
                    .next("a1-blockredstone-redstonechunk")
                    .next("solid")
                    .build();
        }
    },

    /*REDSTONE_LAMP {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("redstoneLamp", Blocks.redstone_lamp.getDefaultState(), -1);
            factory.newBlock(Material.rock, "redstoneLamp", provider)
                    .newVariation("redstone_lamp_off")
                    .next("redstone_lamp_on")
                    .next("square-off")
                    .next("square-on")
                    .build();
        }
    },*/

    /*ROOFING {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "roofing", provider)
                    .newVariation("shingles")
                    .build();
        }
    },*/

    /*SANDSNAKE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "sandsnake", provider)
                    .setGroup("sandstone")
                    .setParentFolder("snakestone/sandsnake")
                    .newVariation("bot-tip")
                    .next("bot")
                    .next("crosssection")
                    .next("face-left")
                    .next("face-right")
                    .next("face")
                    .next("left-down")
                    .next("left-tip")
                    .next("left-up")
                    .next("right-down")
                    .next("right-tip")
                    .next("right-up")
                    .next("side")
                    .next("top-tip")
                    .next("top")
                    .build();
        }
    },*/

    SANDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState ss = Blocks.sandstone.getDefaultState();
            IProperty<BlockSandStone.EnumType> prop = BlockSandStone.TYPE;
            Carving.chisel.addVariation("sandstone", ss.withProperty(prop, BlockSandStone.EnumType.DEFAULT), -3);
            Carving.chisel.addVariation("sandstone", ss.withProperty(prop, BlockSandStone.EnumType.SMOOTH), -2);
            Carving.chisel.addVariation("sandstone", ss.withProperty(prop, BlockSandStone.EnumType.CHISELED), -1);

            factory.newBlock(Material.rock, "sandstone", provider)
                    .newVariation("a0-sandstonepreview-boxcreeper")
                    .next("base")
                    .next("block")
                    .next("blocks")
                    .next("capstone")
                    .next("column")
                    .next("faded")
                    .next("horizontal-tiles")
                    .next("mosaic")
                    .next("small")
                    .next("smooth-base")
                    .next("smooth-cap")
                    .next("smooth-small")
                    .next("smooth")
                    .next("terrain-sandstone-smoothglyph")
                    .next("terrain-sandstone-solidcobble")
                    .next("a0-sandstonepreview-smoothflat")
                    .next("terrain-sandstone-brickflat")
                    .build();
        }
    },

    SANDSTONE_SCRIBBLES {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "sandstone-scribbles", provider)
                    .setGroup("sandstone")
                    .newVariation("scribbles-0")
                    .next("scribbles-1")
                    .next("scribbles-2")
                    .next("scribbles-3")
                    .next("scribbles-4")
                    .next("scribbles-5")
                    .next("scribbles-6")
                    .next("scribbles-7")
                    .next("scribbles-8")
                    .next("scribbles-9")
                    .next("scribbles-10")
                    .next("scribbles-11")
                    .next("scribbles-12")
                    .next("scribbles-13")
                    .next("scribbles-14")
                    .next("scribbles-15")
                    .build();
        }
    },

    /*SHINGLES {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "shingles", provider)
                    .newVariation("Shingle1")
                    .next("Shingle2")
                    .next("Shingle3")
                    .next("Shingle4")
                    .next("Shingle5")
                    .next("Shingle6")
                    .build();
        }
    },*/

    SILVER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "silver", provider)
                    .setParentFolder("metals/silver")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    /*SNAKE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "snake", provider)
                    .setParentFolder("snakestone/snake")
                    .newVariation("bot-tip")
                    .next("bot")
                    .next("crosssection")
                    .next("face-left")
                    .next("face-right")
                    .next("face")
                    .next("left-down")
                    .next("left-tip")
                    .next("left-up")
                    .next("right-down")
                    .next("right-tip")
                    .next("right-up")
                    .next("side")
                    .next("top-tip")
                    .next("top")
                    .build();
        }
    },*/

    STEEL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "steel", provider)
                    .setParentFolder("metals/steel")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    STONEBRICK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("stonebrick", Blocks.stonebrick.getDefaultState(), -1);

            factory.newBlock(Material.rock, "stonebrick", provider)
                    .newVariation("chaotic")
                    .next("chaoticbricks")
                    .next("fancy")
                    .next("fullsmooth")
                    .next("largebricks")
                    .next("largeornate")
                    .next("ornate")
                    .next("ornatepanel")
                    .next("panel-hard")
                    .next("poison")
                    .next("roughbricks")
                    .next("smallbricks")
                    .next("smallchaotic")
                    .next("sunken")
                    .next("masonry2Blue")
                    .next("masonry2Both")
                    .next("masonry2Neutral")
                    .next("masonry2Red")
                    .build();
        }
    },

    /*TALLOW {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "tallow", provider)
                    .newVariation("faces")
                    .next("smooth")
                    .next("tallowblock")
                    .next("tallowblock_top")
                    .build();
        }
    },*/

    TECHNICAL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "technical", provider)
                    .newVariation("cables")
                    .next("cautiontape")
                    .next("fanFast")
                    .next("fanFastTransparent")
                    .next("fanMalfunction")
                    .next("fanStill")
                    .next("fanStillTransparent")
                    .next("grate")
                    .next("grateRusty")
                    .next("hexArmorPlating")
                    .next("industrialrelic")
                    .next("insulationv2")
                    .next("malfunctionFan")
                    .next("massiveFan")
                    .next("massiveHexPlating")
                    .next("old")
                    .next("pipesLarge")
                    .next("pipesSmall")
                    .next("rustyBoltedPlates")
                    .next("rustyCover")
                    .next("scaffold")
                    .next("scaffoldTransparent")
                    .next("spinningStuffAnim")
                    .next("vent")
                    .next("ventGlowing")
                    .build();

            factory.newBlock(Material.rock, "new", provider)
                    .setParentFolder("technical/new")
                    .newVariation("engineering")
                    .next("ExhaustPlating")
                    .next("MakeshiftPanels")
                    .next("MegaCell")
                    .next("Piping")
                    .next("scaffoldLarge")
                    .next("Sturdy")
                    .next("TapeDrive")
                    .next("weatheredGreenPanels")
                    .next("weatheredOrangePanels")
                    .build();
        }
    },

    TEMPLE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "temple", provider)
                    .newVariation("bricks-disarray")
                    .next("bricks-large")
                    .next("bricks-weared")
                    .next("bricks")
                    .next("cobble")
                    .next("column")
                    .next("ornate")
                    .next("plate-cracked")
                    .next("plate")
                    .next("smalltiles-light")
                    .next("smalltiles")
                    .next("stand-creeper")
                    .next("stand-mosaic")
                    .next("stand")
                    .next("tiles-light")
                    .next("tiles")
                    .build();
        }
    },

    TEMPLEMOSSY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "templemossy", provider)
                    .newVariation("bricks-disarray")
                    .next("bricks-large")
                    .next("bricks-weared")
                    .next("bricks")
                    .next("cobble")
                    .next("column")
                    .next("ornate")
                    .next("plate-cracked")
                    .next("plate")
                    .next("smalltiles-light")
                    .next("smalltiles")
                    .next("stand-creeper")
                    .next("stand-mosaic")
                    .next("stand")
                    .next("tiles-light")
                    .next("tiles")
                    .build();
        }
    },

    /*TERRASTEEL {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "terrasteel", provider).newVariation("adv").next("bolted").next("card").next("caution").next("crate").next("egregious").next("elementiumEye-0-0").next("elementiumEye-0-1").next("elementiumEye-1-0").next("elementiumEye-1-1").next("elementiumEye-2-0").next("elementiumEye-2-1").next("thermal").build();
        }
    },*/

    THAUMIUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "thaumium", provider)
                    .newVariation("bevel")
                    .next("chunks")
                    .next("lattice")
                    .next("ornate")
                    .next("planks")
                    .next("purplerunes")
                    .next("runes")
                    .next("small")
                    .next("thaumDiagonalBricks")
                    .next("thaumicEyeSegment")
                    .next("thaumiumBigBricks")
                    .next("thaumiumblock")
                    .next("totem")
                    .build();
        }
    },

    /*THIN_WOOD_ACACIA {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "thinWood-acacia", provider).newVariation("chaotic-spaced").next("chaotic").next("large-spaced").setTextureLocation("thinWood-acacia/large-spaced-side").setTextureLocation("thinWood-acacia/large-spaced-top", Axis.Y).next("large-spaced").next("large").next("normal-spaced").setTextureLocation("thinWood-acacia/normal-spaced-side").setTextureLocation("thinWood-acacia/normal-spaced-top", Axis.Y).next("normal-spaced").next("normal").setTextureLocation("thinWood-acacia/normal-side").setTextureLocation("thinWood-acacia/normal-top", Axis.Y).next("normal").build();
        }
    },

    THIN_WOOD_BIRCH {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "thinWood-birch", provider).newVariation("chaotic-spaced").next("chaotic").next("large-spaced").next("large").next("normal-spaced").next("normal").build();
        }
    },

    THIN_WOOD_DARK {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "thinWood-dark", provider).newVariation("chaotic-spaced").next("chaotic").next("large-spaced").next("large").next("normal-spaced").next("normal").build();
        }
    },

    THIN_WOOD_JUNGLE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "thinWood-jungle", provider).newVariation("chaotic-spaced").next("chaotic").next("large-spaced").next("large").next("normal-spaced").next("normal").build();
        }
    },

    THIN_WOOD_OAK {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "thinWood-oak", provider).newVariation("chaotic-spaced").next("chaotic").next("large-spaced").next("large").next("normal-spaced").next("normal").build();
        }
    },

    THIN_WOOD_SPRUCE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "thinWood-spruce", provider).newVariation("chaotic-spaced").next("chaotic").next("large-spaced").next("large").next("normal-spaced").next("normal").build();
        }
    },*/

    TIN {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "tin", provider)
                    .setParentFolder("metals/tin")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    TORCH {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "torch", provider)
                    .newVariation("torch1")
                    .next("torch2")
                    .next("torch3")
                    .next("torch4")
                    .next("torch5")
                    .next("torch6")
                    .next("torch7")
                    .next("torch8")
                    .next("torch9")
                    .next("torch10")
                    .build();
        }
    },

    TYRIAN {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "tyrian", provider)
                    .newVariation("black")
                    .next("black2")
                    .next("blueplating")
                    .next("chaotic")
                    .next("dent")
                    .next("diagonal")
                    .next("elaborate")
                    .next("opening")
                    .next("plate")
                    .next("plate-raw")
                    .next("platetiles")
                    .next("platform")
                    .next("routes")
                    .next("rust")
                    .next("shining")
                    .next("softplate")
                    .next("tyrian")
                    .build();
        }
    },

    URANIUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "uranium", provider)
                    .setParentFolder("metals/uranium")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();
        }
    },

    VALENTINES {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "valentines", provider)
                    .newVariation("1")
                    .next("2")
                    .next("3")
                    .next("4")
                    .next("5")
                    .next("6")
                    .next("7")
                    .next("8")
                    .next("9")
                    .next("companion")
                    .build();
        }
    },

    VOIDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "voidstone", provider)
                    .newVariation("bevel")
                    .next("eye")
                    .next("metalborder")
                    .next("quarters")
                    .next("raw")
                    .next("rune")
                    .next("skulls")
                    .next("smooth")
                    .build();

            factory.newBlock(Material.rock, "energizedVoidstone", provider)
                    .setParentFolder("voidstone/animated")
                    .newVariation("bevel")
                    .next("eye")
                    .next("metalborder")
                    .next("quarters")
                    .next("raw")
                    .next("rune")
                    .next("skulls")
                    .next("smooth")
                    .build();

            factory.newBlock(Material.rock, "voidstoneRunic", provider)
                    .setParentFolder("voidstone/runes")
                    .newVariation("black")
                    .next("blue")
                    .next("brown")
                    .next("cyan")
                    .next("gray")
                    .next("green")
                    .next("lightblue")
                    .next("lightgray")
                    .next("lime")
                    .next("magenta")
                    .next("orange")
                    .next("pink")
                    .next("purple")
                    .next("red")
                    .next("yellow")
                    .build();
        }
    },

    WARNING {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "warningSign", provider)
                    .setParentFolder("warning")
                    .newVariation("acid")
                    .next("bio")
                    .next("cryogenic")
                    .next("death")
                    .next("explosion")
                    .next("fall")
                    .next("falling")
                    .next("fire")
                    .next("generic")
                    .next("illuminati")
                    .next("noentry")
                    .next("oxygen")
                    .next("rad")
                    .next("sound")
                    .next("underconstruction")
                    .next("voltage")
                    .build();
        }
    },

    WATERSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "waterstone", provider)
                    .newVariation("cobble")
                    .next("black")
                    .next("tiles")
                    .next("chaotic")
                    .next("creeper")
                    .next("panel")
                    .next("panel-ornate")
                    .next("dark")
                    .build();
        }
    },

    /*WOOLEN_CLAY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "woolenClay", provider)
                    .newVariation("black")
                    .next("blue")
                    .next("brown")
                    .next("cyan")
                    .next("gray")
                    .next("green")
                    .next("lightblue")
                    .next("lightgray")
                    .next("lime")
                    .next("magenta")
                    .next("orange")
                    .next("pink")
                    .next("purple")
                    .next("red")
                    .next("white")
                    .next("yellow")
                    .build();
        }
    },*/;

    private static final String[] dyeColors = {"black",
            "red",
            "green",
            "brown",
            "blue",
            "purple",
            "cyan",
            "lightgray",
            "gray",
            "pink",
            "lime",
            "yellow",
            "lightblue",
            "magenta",
            "orange",
            "white"};

    //@formatter:on

    private static final String[] dyeOres = {"dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow",
            "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite"};

    private static final BlockCreator<BlockCarvable> creator = BlockCarvable::new;
    private static final ChiselBlockProvider<BlockCarvable> provider = new ChiselBlockProvider<>(creator, BlockCarvable.class);

    @RequiredArgsConstructor
    private static class ChiselBlockProvider<T extends Block & ICarvable> implements BlockProvider<T> {

        private final BlockCreator<T> creator;
        @Getter
        private final Class<T> blockClass;
        @Getter
        private final Class<? extends ItemBlock> itemClass = ItemChiselBlock.class;

        @Override
        public T createBlock(Material mat, int index, int maxVariation, VariationData... data) {
            return creator.createBlock(mat, index, maxVariation, data);
        }
    }

    ;

    static void init() {
        Chisel.logger.info("Starting init...");
        loadRecipes();
        Chisel.logger.info("Init finished.");
    }

    private static void loadBlocks() {
        Chisel.logger.info("Loading blocks...");
        int num = 0;
        ChiselBlockFactory factory = ChiselBlockFactory.newFactory(Chisel.MOD_ID);
        for (Features f : values()) {
            if (f.enabled()) {
                f.addBlocks(factory);
                ++num;
            } else {
                logDisabled(f);
            }
        }
        Chisel.logger.info(num + " Feature's blocks loaded.");
        Chisel.logger.info("Loading Tile Entities...");
        Chisel.proxy.registerTileEntities();
        Chisel.logger.info("Tile Entities loaded.");
    }

    private static void loadItems() {
        Chisel.logger.info("Loading items...");
        int num = 0;
        for (Features f : values()) {
            if (f.enabled()) {
                f.addItems();
                ++num;
            } else {
                logDisabled(f);
            }
        }
        Chisel.logger.info(num + " Feature's items loaded.");
    }

    private static void loadRecipes() {
        Chisel.logger.info("Loading recipes...");
        int num = 0;
        for (Features f : values()) {
            if (f.enabled()) {
                // if (f.needsMetaRecipes()) {
                // for (int i = 0; i < 16; i++) {
                // meta = i;
                // f.addRecipes();
                // }
                // meta = 0;
                // }
                // else {
                f.addRecipes();
                // }
                ++num;
            } else {
                logDisabled(f);
            }
        }
        Chisel.logger.info(num + " Feature's recipes loaded.");
    }

    private static void logDisabled(Features f) {
        if (!f.hasParentFeature() && f.parent != null) {
            Chisel.logger.info("Skipping feature {} as its parent feature {} was disabled.", Configurations.featureName(f), Configurations.featureName(f.parent));
        } else if (!f.hasRequiredMod() && f.getRequiredMod() != null) {
            Chisel.logger.info("Skipping feature {} as its required mod {} was missing.", Configurations.featureName(f), f.getRequiredMod());
        } else {
            Chisel.logger.info("Skipping feature {} as it was disabled in the config.", Configurations.featureName(f));
        }
    }

    public static boolean oneModdedFeatureLoaded() {
        for (Features f : values()) {
            if (f.hasRequiredMod()) {
                return true;
            }
        }
        return false;
    }

    static void preInit() {
        Chisel.logger.info("Starting pre-init...");
        loadBlocks();
        loadItems();
        Chisel.logger.info("Pre-init finished.");
    }

    private Features parent;

    private String requiredMod;

    private Features() {
        this(null, null);
    }

    private Features(Features parent) {
        this(null, parent);
    }

    private Features(String requiredMod) {
        this(requiredMod, null);
    }

    private Features(String requriedMod, Features parent) {
        this.requiredMod = requriedMod;
        this.parent = parent;
    }

    void addBlocks(ChiselBlockFactory factory) {
        ;
    }

    void addItems() {
        ;
    }

    void addRecipes() {
        ;
    }

    public boolean enabled() {
        return Configurations.featureEnabled(this) && hasRequiredMod() && hasParentFeature();
    }

    private final boolean hasParentFeature() {
        return parent == null || parent.enabled();
    }

    private final boolean hasRequiredMod() {
        return getRequiredMod() == null || Loader.isModLoaded(getRequiredMod());
    }

    private String getRequiredMod() {
        return requiredMod;
    }

    boolean needsMetaRecipes() {
        return false;
    }

    private static void registerSlabTop(Block bottom, Block top) {
        ResourceLocation block = Block.blockRegistry.getNameForObject(bottom);
        String name = block.getResourcePath() + "_top";
        // GameRegistry.registerBlock(top, ItemCarvableSlab.class, name); TODO
    }
}