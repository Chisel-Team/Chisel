package team.chisel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.*;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
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
import team.chisel.common.init.ChiselBlocks;

public enum Features {

    // @formatter:off
    ALUMINUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockAluminum", provider)
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
            factory.newBlock(Material.ROCK, "amber", provider)
                    .newVariation("amberblock")
                    .next("amberblock_top")
                    .build();
        }
    },// Not yet */

    ANDESITE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stone = Blocks.STONE.getDefaultState();
            IProperty<EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("andesite", stone.withProperty(prop, EnumType.ANDESITE), -2);
            Carving.chisel.addVariation("andesite", stone.withProperty(prop, EnumType.ANDESITE_SMOOTH), -1);

            factory.newBlock(Material.ROCK, "andesite", provider)
                    .newVariation("andesitePillar")
                    .next("andesiteLBrick")
                    .next("andesiteOrnate")
                    .next("andesitePrismatic")
                    .next("andesiteTiles")
                    .next("andesiteDiagonalBricks")
                    .build();
        }
    },

    ANTIBLOCK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "antiblock", provider)
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

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.antiblock, 8, 15), "SSS", "SGS", "SSS", 'S', "stone", 'G', "dustGlowstone"));
        }
    },

    ARCANE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "arcane", provider)
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
                    .next("thaumcraftLogo")
                    .build();
        }
    },

    BLOOD_MAGIC {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "bloodMagic", provider)
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
            Carving.chisel.addVariation("bookshelf", Blocks.BOOKSHELF.getDefaultState(), -1);
            factory.newBlock(Material.WOOD, "bookshelf", new ChiselBlockProvider<>(BlockCarvableBookshelf::new, BlockCarvableBookshelf.class))
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
            Carving.chisel.addVariation("brickCustom", Blocks.BRICK_BLOCK.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "brickCustom", provider)
                    .newVariation("large")
                    .next("mortarless")
                    .next("varied")
                    .next("aged")
                    .next("yellow")
                    .build();
        } // TODO change names from brickCustom to bricks, after retexturing
    },

    BRONZE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockBronze", provider)
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
            factory.newBlock(Material.ROCK, "carpet", provider)
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
        } // TODO have these chiseled from individual colored wools and carpets
    },

    /*CERTUS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "certus", provider)
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
            factory.newBlock(Material.ROCK, "cloud", provider)
                    .newVariation("cloud")
                    .next("large")
                    .next("small")
                    .next("vertical")
                    .next("grid")
                    .build();
        }

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.cloud, 32, 0), " S ", "S S", " S ", 'S', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)));
        }
    },

    COBALT {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockCobalt", provider)
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
            Carving.chisel.addVariation("cobblestone", Blocks.COBBLESTONE.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "cobblestone", provider)
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
            Carving.chisel.addVariation("cobblestonemossy", Blocks.MOSSY_COBBLESTONE.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "cobblestonemossy", provider)
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

    CONCRETE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "concrete", provider)
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

        @Override
        void addRecipes() {
            FurnaceRecipes.instance().addSmelting(new ItemStack(Blocks.GRAVEL).getItem(), new ItemStack(ChiselBlocks.concrete), 0.1F);
        }
    },

    COPPER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockCopper", provider)
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
            factory.newBlock(Material.ROCK, "cragROCK", provider)
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

    DIAMOND {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("diamond", Blocks.DIAMOND_BLOCK.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "diamond", provider)
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
            IBlockState stone = Blocks.STONE.getDefaultState();
            IProperty<EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("diorite", stone.withProperty(prop, EnumType.DIORITE), -2);
            Carving.chisel.addVariation("diorite", stone.withProperty(prop, EnumType.DIORITE_SMOOTH), -1);

            factory.newBlock(Material.ROCK, "diorite", provider)
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
            Carving.chisel.addVariation("dirt", Blocks.DIRT.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "dirt", provider)
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
            factory.newBlock(Material.ROCK, "dreamwood-paper", provider)
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
            factory.newBlock(Material.ROCK, "dreamwood-planks", provider)
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
            factory.newBlock(Material.ROCK, "dreamwood-raw", provider)
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
            factory.newBlock(Material.ROCK, "blockElectrum", provider)
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

    EMERALD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("emerald", Blocks.EMERALD_BLOCK.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "emerald", provider)
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
            IBlockState purpur_pillar = Blocks.PURPUR_PILLAR.getDefaultState();
            IProperty<Axis> prop = BlockRotatedPillar.AXIS;
            Carving.chisel.addVariation("end_purpur", Blocks.PURPUR_BLOCK.getDefaultState(), -5);
            //Carving.chisel.addVariation("end_purpur", purpur_pillar.withProperty(prop, Axis.X), -4);
            Carving.chisel.addVariation("end_purpur", purpur_pillar.withProperty(prop, Axis.Y), -3);
            //Carving.chisel.addVariation("end_purpur", purpur_pillar.withProperty(prop, Axis.Z), -2);
            //Carving.chisel.addVariation("end_purpur", Blocks.purpur_double_slab.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "end_purpur", provider)
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
            factory.newBlock(Material.ROCK, "ender_pearl_block", provider)
                    .newVariation("resonantSolid")
                    .next("enderZelda")
                    .next("enderEye")
                    .next("resonantBricks")
                    .build();
        }

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.ender_pearl_block), "SS", "SS", 'S', new ItemStack(Items.ENDER_PEARL));
        }
    },

    ENDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("endstone", Blocks.END_STONE.getDefaultState(), -2);
            Carving.chisel.addVariation("endstone", Blocks.END_BRICKS.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "endstone", provider)
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
            factory.newBlock(Material.ROCK, "factory", provider)
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

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.factory, 32, 0), "SXS", "X X", "SXS",
                    'X', new ItemStack(Blocks.STONE, 1),
                    'S', new ItemStack(Items.IRON_INGOT, 1));
        }
    },

    FANTASY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "fantasy", provider)
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

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.fantasy, 16), "SSS", "SXS", "SSS", 'S', new ItemStack(Blocks.STONE), 'X', new ItemStack(Items.DYE, 1, 13));
        }
    },

    FANTASY2 {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "fantasy2", provider)
                    .setGroup("fantasy")
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

    FUTURA {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "futura", provider)
                    .newVariation("screenMetallic")
                    .next("screenCyan")
                    .next("controller")
                    .next("wavy")
                    .next("controllerPurple")
                    .next("uberWavy")
                    .build();
        }

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.futura, 8, 0), "SSS", "SGS", "SSS", 'S', "stone", 'G', "dustRedstone"));
        }
    },

    GLASS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("glass", Blocks.GLASS.getDefaultState(), -1);
            factory.newBlock(Material.GLASS, "glass", provider)
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
            IBlockState stainedGlass = Blocks.STAINED_GLASS.getDefaultState();
            IProperty<EnumDyeColor> prop = BlockStainedGlass.COLOR;

            for(int c = 0; c < dyeColors.length; c++)
            {
                Carving.chisel.addVariation("glassdyed"+dyeColors[c], stainedGlass.withProperty(prop, EnumDyeColor.byDyeDamage(c)), -1);
                factory.newBlock(Material.GLASS, "glassdyed"+dyeColors[c], provider)
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
            //Carving.chisel.addVariation("glasspane", Blocks.glass_pane.getDefaultState(), -1);

            factory.newBlock(Material.GLASS, "glasspane", new ChiselBlockProvider<BlockCarvablePane>(new BlockCreator<BlockCarvablePane>()
            {
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

            IBlockState stainedGlassPane = Blocks.STAINED_GLASS_PANE.getDefaultState();
            IProperty<EnumDyeColor> prop = BlockStainedGlassPane.COLOR;

            for(int c = 0; c < dyeColors.length; c++)
            {
                //Carving.chisel.addVariation("glasspanedyed"+dyeColors[c], stainedGlassPane.withProperty(prop, EnumDyeColor.byDyeDamage(c)), -1);
                factory.newBlock(Material.GLASS, "glasspanedyed"+dyeColors[c], provider)
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
            Carving.chisel.addVariation("blockGold", Blocks.GOLD_BLOCK.getDefaultState(), -1);

            factory.newBlock(Material.ROCK, "blockGold", provider)
                    .setParentFolder("metals/gold")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();

            factory.newBlock(Material.ROCK, "gold", provider)
                    .setGroup("blockGold")
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
            IBlockState stone = Blocks.STONE.getDefaultState();
            IProperty<EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("granite", stone.withProperty(prop, EnumType.GRANITE), -2);
            Carving.chisel.addVariation("granite", stone.withProperty(prop, EnumType.GRANITE_SMOOTH), -1);

            factory.newBlock(Material.ROCK, "granite", provider)
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
            factory.newBlock(Material.ROCK, "grimstone", provider)
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

        @Override
        void addRecipes() {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.grimstone, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.STONE, 1), 'X', new ItemStack(Items.COAL, 1));
        }
    },

    HEX_PLATING {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "hexPlating", provider)
                    .newVariation("hexBase") //TODO: Colored+Glowy stuff
                    .next("hexNew")
                    .build();
        }
    },

    HOLYSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "holystone", provider)
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

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.holystone, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.STONE, 1), 'X', new ItemStack(Items.GOLD_NUGGET, 1));
        }
    },

    ICE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("ice", Blocks.ICE.getDefaultState(), -1);
            factory.newBlock(Material.ICE, "ice", provider)
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
            factory.newBlock(Material.ROCK, "icepillar", provider)
                    .setGroup("ice")
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
            factory.newBlock(Material.ROCK, "blockInvar", provider)
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
            Carving.chisel.addVariation("blockIron", Blocks.IRON_BLOCK.getDefaultState(), -1);

            factory.newBlock(Material.ROCK, "blockIron", provider)
                    .setParentFolder("metals/iron")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .build();

            factory.newBlock(Material.ROCK, "iron", provider)
                    .setGroup("blockIron")
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
            Carving.chisel.addVariation("ironpane", Blocks.IRON_BARS.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "ironpane", provider)
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
            factory.newBlock(Material.ROCK, "laboratory", provider)
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

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.laboratory, 8), "***", "*X*", "***", '*', new ItemStack(Blocks.STONE, 1), 'X', new ItemStack(Items.QUARTZ, 1));
        }
    },

    LAPIS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("lapis", Blocks.LAPIS_BLOCK.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "lapis", provider)
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
            factory.newBlock(Material.ROCK, "lavastone", provider)
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

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.lavastone, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.STONE, 1), 'X', new ItemStack(Items.LAVA_BUCKET, 1));
        }
    },

    LEAD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockLead", provider)
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
            factory.newBlock(Material.ROCK, "leaves", provider)
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
            Carving.chisel.addVariation("lightstone", Blocks.GLOWSTONE.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "lightstone", provider)
                    .newVariation("terrain-sulphur-cobble")
                    .next("terrain-sulphur-corroded")
                    .next("terrain-sulphur-glass")
                    .next("terrain-sulphur-neon")
                    .next("terrain-sulphur-ornate")
                    .next("terrain-sulphur-ROCKy")
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
            factory.newBlock(Material.ROCK, "limestone", provider)
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
            factory.newBlock(Material.ROCK, "line_marking", provider)
                    .setParentFolder("line-marking")
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

        @Override
        void addRecipes()
        {

        }
    },

    /*LIVINGROCK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "livingROCK", provider)
                    .setParentFolder("botania/livingROCK")
                    .newVariation("masonryLivingstone")
                    .build();
        }
    },

    LIVINGWOOD_PAPER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "livingwood-paper", provider)
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
            factory.newBlock(Material.ROCK, "livingwood-planks", provider)
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
            factory.newBlock(Material.ROCK, "livingwood-raw", provider)
                    .newVariation("livingwoodLogSide")
                    .next("livingwoodLogTop")
                    .next("livingwoodPileSide")
                    .next("livingwoodPileTop")
                    .build();
        }
    },*/

    MARBLE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "marble", provider)
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
                factory.newBlock(Material.ROCK, "marblepillarold", provider)
                        .setGroup("marble")
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
                factory.newBlock(Material.ROCK, "marblepillar", provider)
                        .setGroup("marble")
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

    /*MAZESTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "mazestone", provider)
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
            factory.newBlock(Material.ROCK, "military", provider)
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

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.military, 32), "xyx", "yzy", "xyx", 'x', "stone", 'y', Items.IRON_INGOT, 'z', Items.GOLD_NUGGET));
        }
    },

    NETHERBRICK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("netherbrick", Blocks.NETHER_BRICK.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "netherbrick", provider)
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
            Carving.chisel.addVariation("netherrack", Blocks.NETHERRACK.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "netherrack", provider)
                    .newVariation("a1-netherrack-bloodgravel")
                    .next("a1-netherrack-bloodROCK")
                    .next("a1-netherrack-bloodROCKgrey")
                    .next("a1-netherrack-brinstar")
                    .next("a1-netherrack-brinstarshale")
                    .next("a1-netherrack-classic")
                    .next("a1-netherrack-classicspatter")
                    .next("a1-netherrack-guts")
                    .next("a1-netherrack-gutsdark")
                    .next("a1-netherrack-meat")
                    .next("a1-netherrack-meatred")
                    .next("a1-netherrack-meatROCK")
                    .next("a1-netherrack-red")
                    .next("a1-netherrack-wells")
                    .build();
        }
    },

    NICKEL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockNickel", provider)
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
            Carving.chisel.addVariation("obsidian", Blocks.OBSIDIAN.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "obsidian", provider)
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
            factory.newBlock(Material.ROCK, "paper", provider)
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

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.paper, 32), "ppp", "psp", "ppp", 'p', Items.PAPER, 's', "stickWood"));
        }
    },

    PLANKS {
        private final String[] plank_names = { "oak", "spruce", "birch", "jungle", "acacia", "dark-oak" };

        @Override
        void addBlocks(ChiselBlockFactory factory)
        {
            IBlockState planks = Blocks.PLANKS.getDefaultState();
            IProperty<BlockPlanks.EnumType> prop = BlockPlanks.VARIANT;

            for (int c = 0; c < plank_names.length; c++)
            {
                Carving.chisel.addVariation("planks-" + plank_names[c], planks.withProperty(prop, BlockPlanks.EnumType.byMetadata(c)), -1);

                factory.newBlock(Material.ROCK, "planks-" + plank_names[c], provider)
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
            factory.newBlock(Material.ROCK, "blockPlatinum", provider)
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
            factory.newBlock(Material.ROCK, "present", provider)
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

        @Override
        void addRecipes()
        {
            // TODO TEs. Or maybe remove wholesale
        }
    },

    PRISMARINE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            IBlockState prismarine = Blocks.PRISMARINE.getDefaultState();
            IProperty<BlockPrismarine.EnumType> prop = BlockPrismarine.VARIANT;
            Carving.chisel.addVariation("prismarine", prismarine.withProperty(prop, BlockPrismarine.EnumType.ROUGH), -3);
            Carving.chisel.addVariation("prismarine", prismarine.withProperty(prop, BlockPrismarine.EnumType.BRICKS), -2);
            Carving.chisel.addVariation("prismarine", prismarine.withProperty(prop, BlockPrismarine.EnumType.DARK), -1);

            factory.newBlock(Material.ROCK, "prismarine", provider)
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
            Carving.chisel.addVariation("pumpkin", Blocks.PUMPKIN.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "pumpkin", provider)
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
    }, // TODO Rotations

    QUARTZ {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState quartzBlock = Blocks.QUARTZ_BLOCK.getDefaultState();
            IProperty<BlockQuartz.EnumType> prop = BlockQuartz.VARIANT;
            Carving.chisel.addVariation("quartz", quartzBlock.withProperty(prop, BlockQuartz.EnumType.DEFAULT), -5);
            Carving.chisel.addVariation("quartz", quartzBlock.withProperty(prop, BlockQuartz.EnumType.CHISELED), -4);
            //Carving.chisel.addVariation("quartz", quartzBlock.withProperty(prop, BlockQuartz.EnumType.LINES_X), -3);
            Carving.chisel.addVariation("quartz", quartzBlock.withProperty(prop, BlockQuartz.EnumType.LINES_Y), -2);
            //Carving.chisel.addVariation("quartz", quartzBlock.withProperty(prop, BlockQuartz.EnumType.LINES_Z), -1);

            factory.newBlock(Material.ROCK, "quartz", provider)
                    .newVariation("quartzChiseled")
                    .next("quartzPrismaticPattern")
                    .next("masonryQuartz")
                    .build();
        }
    },

    REDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("redstone", Blocks.REDSTONE_BLOCK.getDefaultState(), -1);
            factory.newBlock(Material.ROCK, "redstone", provider)
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

    SANDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState ss = Blocks.SANDSTONE.getDefaultState();
            IProperty<BlockSandStone.EnumType> prop = BlockSandStone.TYPE;
            Carving.chisel.addVariation("sandstone", ss.withProperty(prop, BlockSandStone.EnumType.DEFAULT), -3);
            Carving.chisel.addVariation("sandstone", ss.withProperty(prop, BlockSandStone.EnumType.SMOOTH), -2);
            Carving.chisel.addVariation("sandstone", ss.withProperty(prop, BlockSandStone.EnumType.CHISELED), -1);

            factory.newBlock(Material.ROCK, "sandstone", provider)
                    .newVariation("terrain-sandstone-smoothglyph")
                    .next("terrain-sandstone-solidcobble")
                    .next("a0-sandstonepreview-boxcreeper")
                    .next("faded")
                    .next("column")
                    .next("capstone")
                    .next("small")
                    .next("base")
                    .next("smooth")
                    .next("smooth-cap")
                    .next("smooth-small")
                    .next("smooth-base")
                    .next("block")
                    .next("blocks")
                    .next("mosaic")
                    .next("horizontal-tiles")
                    .next("a0-sandstonepreview-smoothflat")
                    .next("terrain-sandstone-brickflat")
                    .build();
        }
    },

    SANDSTONE_SCRIBBLES {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "sandstone-scribbles", provider)
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

    SILVER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockSilver", provider)
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

    STEEL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockSteel", provider)
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
        void addBlocks(ChiselBlockFactory factory)
        {
            IBlockState stoneBricks = Blocks.STONEBRICK.getDefaultState();
            IProperty<BlockStoneBrick.EnumType> prop = BlockStoneBrick.VARIANT;
            Carving.chisel.addVariation("stonebrick", stoneBricks.withProperty(prop, BlockStoneBrick.EnumType.DEFAULT), -6);
            Carving.chisel.addVariation("stonebrick", stoneBricks.withProperty(prop, BlockStoneBrick.EnumType.MOSSY), -5);
            Carving.chisel.addVariation("stonebrick", stoneBricks.withProperty(prop, BlockStoneBrick.EnumType.CRACKED), -4);
            Carving.chisel.addVariation("stonebrick", stoneBricks.withProperty(prop, BlockStoneBrick.EnumType.CHISELED), -3);

            Carving.chisel.addVariation("stonebrick", Blocks.STONE.getDefaultState(), -2);
            // Carving.chisel.addVariation("stonebrick", Blocks.double_stone_slab.getDefaultState().withProperty(BlockDoubleStoneSlab.VARIANT, BlockDoubleStoneSlab.EnumType.STONE), -1);

            factory.newBlock(Material.ROCK, "stonebrick", provider)
                    .newVariation("masonry2Blue")
                    .next("masonry2Both")
                    .next("masonry2Neutral")
                    .next("masonry2Red")
                    .next("smallbricks")
                    .next("largebricks")
                    .next("smallchaotic")
                    .next("chaoticbricks")
                    .next("chaotic")
                    .next("fancy")
                    .next("ornate")
                    .next("largeornate")
                    .next("panel-hard")
                    .next("sunken")
                    .next("ornatepanel")
                    .next("poison")
                    .next("roughbricks")
                    .build();
        }
    },

    /*TALLOW {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "tallow", provider)
                    .newVariation("faces")
                    .next("smooth")
                    .next("tallowblock")
                    .next("tallowblock_top")
                    .build();
        }
    }, Thaumcraft */

    TECHNICAL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.IRON, "technical", provider)
                    .setGroup("factory")
                    .newVariation("scaffold")
                    .next("cautiontape")
                    .next("industrialrelic")
                    .next("pipesLarge")
                    .next("fanFast")
                    .next("pipesSmall")
                    .next("fanStill")
                    .next("vent")
                    .next("ventGlowing")
                    .next("insulationv2")
                    .next("spinningStuffAnim")
                    .next("cables")
                    .next("rustyBoltedPlates")
                    .next("grate")
                    .next("malfunctionFan")
                    .next("grateRusty")
                    .next("scaffoldTransparent")
                    .next("fanFastTransparent")
                    .next("fanStillTransparent")
                    .next("massiveFan")
                    .next("massiveHexPlating")
                    .build();

            factory.newBlock(Material.IRON, "technicalNew", provider)
                    .setGroup("factory")
                    .setParentFolder("technical/new")
                    .newVariation("weatheredGreenPanels")
                    .next("weatheredOrangePanels")
                    .next("Sturdy")
                    .next("MegaCell")
                    .next("ExhaustPlating")
                    .next("MakeshiftPanels")
                    .next("engineering")
                    .next("scaffoldLarge")
                    .next("Piping")
                    //TODO Retexture .next("TapeDrive")
                    .build();
        }
    },

    TEMPLE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "temple", provider)
                    .newVariation("cobble")
                    .next("ornate")
                    .next("plate")
                    .next("plate-cracked")
                    .next("bricks")
                    .next("bricks-large")
                    .next("bricks-weared")
                    .next("bricks-disarray")
                    .next("column")
                    .next("stand")
                    .next("tiles")
                    .next("smalltiles")
                    .next("tiles-light")
                    .next("smalltiles-light")
                    .next("stand-creeper")
                    .next("stand-mosaic")
                    .build();

            factory.newBlock(Material.ROCK, "templemossy", provider)
                    .setGroup("temple")
                    .newVariation("cobble")
                    .next("ornate")
                    .next("plate")
                    .next("plate-cracked")
                    .next("bricks")
                    .next("bricks-large")
                    .next("bricks-weared")
                    .next("bricks-disarray")
                    .next("column")
                    .next("stand")
                    .next("tiles")
                    .next("smalltiles")
                    .next("tiles-light")
                    .next("smalltiles-light")
                    .next("stand-creeper")
                    .next("stand-mosaic")
                    .build();
        }

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.temple, 8), "***", "*X*", "***",
                    '*', new ItemStack(Blocks.STONE, 1),
                    'X', new ItemStack(Items.DYE, 1, 6)));
        }
    },

    THAUMIUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockThaumium", provider)
                    .setParentFolder("thaumium")
                    .newVariation("ornate")
                    .next("totem")
                    .next("thaumiumBigBricks")
                    .next("small")
                    .next("lattice")
                    .next("planks")
                    .next("thaumDiagonalBricks")
                    .next("thaumicEyeSegment")
                    .build();
        }
    },

    TIN {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockTin", provider)
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
            factory.newBlock(Material.ROCK, "torch", provider)
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
        } // TODO Torch Logic (Walls and such)
    },

    TYRIAN {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "tyrian", provider)
                    .newVariation("shining")
                    .next("tyrian")
                    .next("chaotic")
                    .next("softplate")
                    .next("rust")
                    .next("elaborate")
                    .next("routes")
                    .next("platform")
                    .next("platetiles")
                    .next("diagonal")
                    .next("dent")
                    .next("blueplating")
                    .next("black")
                    .next("black2")
                    .next("opening")
                    .next("plate")
                    .build();
        }

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.factory, 32, 0), "SXS", "X X", "SXS",
                    'S', new ItemStack(Blocks.STONE, 1),
                    'X', new ItemStack(Items.IRON_INGOT, 1));
        }
    },

    URANIUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "blockUranium", provider)
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
            factory.newBlock(Material.ROCK, "valentines", provider)
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

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.valentines, 4, 0), "***", "*X*", "***",
                    '*', "stone",
                    'X', new ItemStack(Items.DYE, 1, 9)));

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.valentines, 32, 0), "***", "*X*", "***",
                    '*', "stone",
                    'X', new ItemStack(Items.SKULL, 1, OreDictionary.WILDCARD_VALUE)));
        }
    },

    VOIDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "voidstone", provider)
                    .newVariation("raw")
                    .next("quarters")
                    .next("smooth")
                    .next("skulls")
                    .next("rune")
                    .next("metalborder")
                    .next("eye")
                    .next("bevel")
                    .build();

            factory.newBlock(Material.ROCK, "energizedVoidstone", provider)
                    .setGroup("voidstone")
                    .setParentFolder("voidstone/animated")
                    .newVariation("raw")
                    .next("quarters")
                    .next("smooth")
                    .next("skulls")
                    .next("rune")
                    .next("metalborder")
                    .next("eye")
                    .next("bevel")
                    .build();

            factory.newBlock(Material.ROCK, "voidstoneRunic", provider)
                    .setParentFolder("voidstone/runes")
                    .setGroup("voidstone")
                    .newVariation("black")
                    .next("red")
                    .next("green")
                    .next("brown")
                    .next("blue")
                    .next("purple")
                    .next("cyan")
                    .next("lightgray")
                    .next("gray")
                    .next("pink")
                    .next("lime")
                    .next("yellow")
                    .next("lightblue")
                    .next("magenta")
                    .next("orange")
                    //.next("white")
                    .build();
        }

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstone, 8, 0), "EOE", "OEO", "EOE",
                    'E', new ItemStack(Items.ENDER_EYE),
                    'O', new ItemStack(Blocks.OBSIDIAN));

            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstone, 16, 0), " P ", "PEP", " P ",
                    'E', new ItemStack(Items.ENDER_PEARL),
                    'P', new ItemStack(Blocks.PURPUR_BLOCK));
        }
    },

    WARNING {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "warningSign", provider)
                    .setParentFolder("warningSign")
                    .newVariation("rad")
                    .next("bio")
                    .next("fire")
                    .next("explosion")
                    .next("death")
                    .next("falling")
                    .next("fall")
                    .next("voltage")
                    .next("generic")
                    .next("acid")
                    .next("underconstruction")
                    .next("sound")
                    .next("noentry")
                    .next("cryogenic")
                    .next("oxygen")
                    .next("illuminati")
                    .build();
        }

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.warningSign, 4, 0), "xxx", "xyx", "xxx",
                    'x', "stone",
                    'y', new ItemStack(Items.SIGN)));
        }
    },

    WATERSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "waterstone", provider)
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

        @Override
        void addRecipes()
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.waterstone, 8, 0), "***", "*X*", "***",
                    '*', new ItemStack(Blocks.STONE, 1),
                    'X', new ItemStack(Items.WATER_BUCKET, 1));
        }
    };

    private static final String[] dyeColors =
            {
                    "black",
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
                    "white"
            };

    //@formatter:on

    private static final String[] dyeOres = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow",
            "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };

    private static final BlockCreator<BlockCarvable> creator = BlockCarvable::new;
    private static final ChiselBlockProvider<BlockCarvable> provider = new ChiselBlockProvider<>(creator, BlockCarvable.class);

    @RequiredArgsConstructor private static class ChiselBlockProvider<T extends Block & ICarvable> implements BlockProvider<T> {

        private final BlockCreator<T> creator;
        @Getter(onMethod = @__(@Override))
        private final Class<T> blockClass;

        @Override public T createBlock(Material mat, int index, int maxVariation, VariationData... data) {
            return creator.createBlock(mat, index, maxVariation, data);
        }
        
        @Override
        public ItemBlock createItemBlock(T block) {
            return (ItemBlock) new ItemChiselBlock(block).setRegistryName(block.getRegistryName());
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
        ResourceLocation block = Block.REGISTRY.getNameForObject(bottom);
        String name = block.getResourcePath() + "_top";
        // GameRegistry.registerBlock(top, ItemCarvableSlab.class, name); TODO
    }
}
