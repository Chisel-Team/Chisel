package team.chisel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import team.chisel.api.block.BlockCreator;
import team.chisel.api.block.BlockProvider;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.api.block.VariationData;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.BlockCarvableBookshelf;
import team.chisel.common.block.ItemChiselBlock;
import team.chisel.common.carving.Carving;
import team.chisel.common.config.Configurations;

public enum Features {

    // @formatter:off
    ALUMINUM {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "aluminumblock", provider).setParentFolder("metals/aluminum")
            .newVariation("caution").next("crate")
            .next("thermal")
                .setTextureLocation("metals/aluminum/thermal-side")
                .setTextureLocation("metals/aluminum/thermal-top", EnumFacing.UP)
                .setTextureLocation("metals/aluminum/thermal-bottom", EnumFacing.DOWN)
            .next("badGreggy").next("machine").next("bolted").next("scaffold")    
            .build();
        }
    },

    AMBER {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "amber", provider).newVariation("amberblock").next("amberblock_top").build();
        }
    },

    ANDESITE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stone = Blocks.stone.getDefaultState();
            IProperty<EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("andesite", stone.withProperty(prop, EnumType.ANDESITE), -2);
            Carving.chisel.addVariation("andesite", stone.withProperty(prop, EnumType.ANDESITE_SMOOTH), -1);
            
            factory.newBlock(Material.rock, "andesite", provider)
                .newVariation("andesitePolished").next("andesitePillar").setTextureLocation("andesite/andesitePillar-side").setTextureLocation("andesite/andesitePillar-top", Axis.Y)
                .next("andesiteLBrick").next("andesiteOrnate").next("andesitePrismatic").next("andesiteTiles").next("andesitePrism").next("andesiteDiagonalBricks").build();
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
            .newVariation("black").next("blue").next("brown").next("cyan").next("gray").next("green").next("light_blue").next("lime")
            .next("magenta").next("orange").next("pink").next("purple").next("red").next("silver").next("white").next("yellow")
            .build();
        }
    },

    ARCANE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "arcane", provider)
            .newVariation("ArcaneBorder").next("arcaneCrackAnim").next("arcaneMatrix").next("arcaneTile").next("bigBrick").next("BorderBrain").next("conduitGlowAnim")
            .next("moonEngrave").next("moonGlowAnim").next("runes").next("runesGlow").next("runesGlowBase").next("runesGlowBase").next("runesGlowOverlay").next("runesGlowOverlay")
            .next("singleRune").next("thaumcraftLogo").build();
        }
    },

//    AUTO_CHISEL {
//
//        @Override
//        void addBlocks(ChiselBlockFactory factory) {
//            factory.newBlock(Material.rock, "autoChisel", provider).newVariation("autoChisel").next("autoChisel16").next("autoChiselOld").next("autoChisel_automation").next("autoChisel_speed").next("autoChisel_stack").build();
//        }
//    },

    BIRDSTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "birdstone", provider).newVariation("birdstone-darkpanel").next("birdstone-dent").next("birdstone-emboss").next("birdstone-fourtile").next("birdstone-french-creep").next("birdstone-french").next("birdstone-french2").next("birdstone-marker").next("birdstone-ornate1").next("birdstone-ornate2").next("birdstone-rough").next("birdstone-smallbrick").next("birdstone-smallbroken").next("birdstone-smalltile").next("birdstone-smooth-creep").next("birdstone-smooth").build();
        }
    },

    BLOOD_MAGIC {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "bloodMagic", provider).newVariation("BlankRune").next("bloodRuneArranged").next("bloodRuneBricks").next("bloodRuneBricksTop").next("bloodRuneCarved").next("bloodRuneCarvedRadial").next("bloodRuneClassicPanel").next("bloodRuneTiles").next("RuneDiagonalBricks-0").next("RuneDiagonalBricks-1").next("RuneDiagonalBricks-2").next("RuneDiagonalBricks-3").next("RuneDiagonalBricks").build();
        }
    },

    BOOKSHELF {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("bookshelf", Blocks.bookshelf.getDefaultState(), -1);
            factory.newBlock(Material.wood, "bookshelf", new ChiselBlockProvider<>(BlockCarvableBookshelf::new, BlockCarvableBookshelf.class))
                .newVariation("abandoned").next("brim").next("default").next("historician").next("hoarder").next("necromancer").next("necromancer-novice").next("rainbow").next("redtomes")
                .build(b -> b.setStepSound(Block.soundTypeWood).setHardness(1.5f));
        }
    },

    BRICK_CUSTOM {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("brickCustom", Blocks.brick_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "brickCustom", provider).newVariation("aged").next("aged").next("large").next("mortarless").next("varied").next("yellow").build();
        }
    },

    BRONZE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "bronze", provider).setParentFolder("metals/bronze").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/bronze/thermal-side").setTextureLocation("metals/bronze/thermal-top", Axis.Y).build();
        }
    },

    CARPET {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "carpet", provider).newVariation("black").next("brown").next("darkblue").next("darkgrey").next("green").next("grey").next("lightblue").next("lightgreen").next("lily").next("orange").next("pink").next("purple").next("red").next("teal").next("white").next("yellow").build();
        }
    },

    CERTUS {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "certus", provider).setParentFolder("quartz/certus").newVariation("certusChiseled").next("certusPrismatic").next("certusPrismaticPattern").next("masonryCertus").build();
        }
    },

    CLOUD {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "cloud", provider).newVariation("cloud").next("grid").next("large").next("small").next("vertical").build();
        }
    },

    COBALT {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "cobalt", provider).setParentFolder("metals/cobalt").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/cobalt/thermal-side").setTextureLocation("metals/cobalt/thermal-top", Axis.Y).build();
        }
    },

    COBBLESTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("cobblestone", Blocks.cobblestone.getDefaultState(), -1);
            factory.newBlock(Material.rock, "cobblestone", provider).newVariation("terrain-cob-detailedbrick").next("terrain-cob-french").next("terrain-cob-french2").next("terrain-cob-smallbrick").next("terrain-cobb-brickaligned").next("terrain-cobblargetiledark").next("terrain-cobbsmalltile").next("terrain-cobmoss-creepdungeon").next("terrain-mossysmalltiledark").next("terrain-pistonback-darkcreeper").next("terrain-pistonback-darkdent").next("terrain-pistonback-darkemboss").next("terrain-pistonback-darkmarker").next("terrain-pistonback-darkpanel").next("terrain-pistonback-dungeontile").build();
        }
    },

    COBBLESTONEMOSSY {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("cobblestonemossy", Blocks.mossy_cobblestone.getDefaultState(), -1);
            factory.newBlock(Material.rock, "cobblestonemossy", provider).newVariation("terrain-cob-detailedbrick").next("terrain-cob-french").next("terrain-cob-french2").next("terrain-cob-smallbrick").next("terrain-cobb-brickaligned").next("terrain-cobblargetiledark").next("terrain-cobbsmalltile").next("terrain-cobmoss-creepdungeon").next("terrain-mossysmalltiledark").next("terrain-pistonback-darkcreeper").next("terrain-pistonback-darkdent").next("terrain-pistonback-darkemboss").next("terrain-pistonback-darkmarker").next("terrain-pistonback-darkpanel").next("terrain-pistonback-dungeontile").build();
        }
    },

    COLORED_SAND {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "coloredSand", provider).newVariation("black").next("blue").next("brown").next("cyan").next("gray").next("green").next("lightblue").next("lightgray").next("lime").next("magenta").next("orange").next("pink").next("purple").next("red").next("white").next("yellow").build();
        }
    },

    CONCRETE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "concrete", provider).newVariation("asphalt").next("asphaltCracks").next("asphaltCracksWeathered").next("asphaltV2").next("asphaltWeathered").next("block").next("blocks").next("concrete").next("concreteCracked").next("concreteDmg").next("concreteMoldy").next("concreteMoldyCracked").next("concreteVines").next("concreteWeathered").next("default").next("doubleslab").setTextureLocation("concrete/doubleslab-side").setTextureLocation("concrete/doubleslab-top", Axis.Y).next("weathered-block-half-botom").next("weathered-block-half").setTextureLocation("concrete/weathered-block-half-side").setTextureLocation("concrete/weathered-block-half-top", Axis.Y).next("weathered-block").next("weathered-blocks").next("weathered-doubleslab").setTextureLocation("concrete/weathered-doubleslab-side").setTextureLocation("concrete/weathered-doubleslab-top", Axis.Y).next("weathered-half-bottom").next("weathered-half").setTextureLocation("concrete/weathered-half-side").setTextureLocation("concrete/weathered-half-top", Axis.Y).next("weathered").build();
        }
    },

    COPPER {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "copper", provider).setParentFolder("metals/copper").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/copper/thermal-side").setTextureLocation("metals/copper/thermal-top", Axis.Y).build();
        }
    },

    CRAG_ROCK {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "cragRock", provider).newVariation("terrain-cob-detailedbrick").next("terrain-cob-french").next("terrain-cob-french2").next("terrain-cob-smallbrick").next("terrain-cobb-brickaligned").next("terrain-cobblargetiledark").next("terrain-cobbsmalltile").next("terrain-cobmoss-creepdungeon").next("terrain-mossysmalltiledark").next("terrain-pistonback-darkcreeper").next("terrain-pistonback-darkdent").next("terrain-pistonback-darkemboss").next("terrain-pistonback-darkmarker").next("terrain-pistonback-darkpanel").next("terrain-pistonback-dungeontile").build();
        }
    },

    DIAGONAL {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "diagonal", provider).setParentFolder("prismarine/diagonal").newVariation("prismarineDiagonalAnim-0").next("prismarineDiagonalAnim-1").next("prismarineDiagonalAnim-2").next("prismarineDiagonalAnim-3").next("prismarineDiagonalAnim").build();
        }
    },

    DIAMOND {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("diamond", Blocks.diamond_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "diamond", provider).newVariation("terrain-diamond-bismuth").next("terrain-diamond-cells").next("terrain-diamond-crushed").next("terrain-diamond-embossed-bottom").next("terrain-diamond-embossed").setTextureLocation("diamond/terrain-diamond-embossed-side").setTextureLocation("diamond/terrain-diamond-embossed-top", Axis.Y).next("terrain-diamond-four").next("terrain-diamond-fourornate").next("terrain-diamond-gem-bottom").next("terrain-diamond-gem").setTextureLocation("diamond/terrain-diamond-gem-side").setTextureLocation("diamond/terrain-diamond-gem-top", Axis.Y).next("terrain-diamond-ornatelayer").next("terrain-diamond-simple-bottom").next("terrain-diamond-simple").setTextureLocation("diamond/terrain-diamond-simple-side").setTextureLocation("diamond/terrain-diamond-simple-top", Axis.Y).next("terrain-diamond-space").next("terrain-diamond-spaceblack").next("terrain-diamond-zelda").build();
        }
    },

    DIORITE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stone = Blocks.stone.getDefaultState();
            IProperty<EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("diorite", stone.withProperty(prop, EnumType.DIORITE), -2);
            Carving.chisel.addVariation("diorite", stone.withProperty(prop, EnumType.DIORITE_SMOOTH), -1);
            
            factory.newBlock(Material.rock, "diorite", provider).newVariation("dioriteDiagonalBricks").next("dioriteLBrick").next("dioriteOrnate")
            .next("dioritePillar").setTextureLocation("diorite/dioritePillar-side").setTextureLocation("diorite/dioritePillar-top", Axis.Y)
            .next("dioritePrism").next("dioritePrismatic").next("dioriteTiles").build();
        }
    },

    DIRT {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("dirt", Blocks.dirt.getDefaultState(), -1);
            factory.newBlock(Material.rock, "dirt", provider).newVariation("bricks+dirt2").setTextureLocation("dirt/bricks+dirt2-side").setTextureLocation("dirt/bricks+dirt2-top", Axis.Y).next("bricks").next("bricks2").next("bricks3").next("chunky").next("cobble").next("happy").next("hor").next("horizontal").next("layers").next("netherbricks").next("plate").next("reinforcedCobbleDirt").next("reinforcedDirt").next("vert").setTextureLocation("dirt/vert-side").setTextureLocation("dirt/vert-top", Axis.Y).next("vertical").setTextureLocation("dirt/vertical-side").setTextureLocation("dirt/vertical-top", Axis.Y).build();
        }
    },

    DREAMWOOD_PAPER {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "dreamwood-paper", provider).newVariation("box").next("cross").next("door").next("floral").next("horizontal").next("plain").next("sixSections").next("throughMiddle").next("vertical").build();
        }
    },

    DREAMWOOD_PLANKS {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "dreamwood-planks", provider).newVariation("blinds").next("chaotic-hor").next("chaotic").next("clean").next("crate-fancy").next("crate").next("crateex").next("double").setTextureLocation("dreamwood-planks/double-side").setTextureLocation("dreamwood-planks/double-top", Axis.Y).next("fancy").next("large").next("panel-nails").next("parquet").next("short").next("vertical-uneven").next("vertical").build();
        }
    },

    DREAMWOOD_RAW {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "dreamwood-raw", provider).newVariation("dreamwoodLogSide").next("dreamwoodLogTop").next("dreamwoodPileSide").next("dreamwoodPileTop").build();
        }
    },

    ELECTRUM {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "electrum", provider).setParentFolder("metals/electrum").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/electrum/thermal-side").setTextureLocation("metals/electrum/thermal-top", Axis.Y).build();
        }
    },

    ELEMENTIUM {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "elementium", provider).setParentFolder("botania/elementium").newVariation("adv").next("bolted").next("caution").next("crate").next("egregious").next("elementiumEye-0-0").next("elementiumEye-0-1").next("elementiumEye-1-0").next("elementiumEye-1-1").next("elementiumEye-2-0").next("elementiumEye-2-1").next("thermal").build();
        }
    },

    EMERALD {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("emerald", Blocks.emerald_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "emerald", provider).newVariation("cell").next("cellbismuth").next("chunk").next("emeraldCircle").next("emeraldPrismatic").next("four").next("fourornate").next("goldborder").next("masonryEmerald").next("ornate").next("panel").next("panelclassic").next("smooth").next("zelda").build();
        }
    },

    END_PURPUR {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "end_purpur", provider).newVariation("arcanePurpur").next("borderPurpur-0").next("borderPurpur-1").next("borderPurpur-2").next("borderPurpur-3").next("borderPurpur").next("masonryPurpur").next("prismaticPurpur").next("purpurBricks").next("purpurCobble").next("purpurLargeTile").next("purpurOrnate").next("purpurPrismarine").next("purpur_block").next("purpur_pillar").setTextureLocation("end_purpur/purpur_pillar-side").setTextureLocation("end_purpur/purpur_pillar-top", Axis.Y).next("shulker").setTextureLocation("end_purpur/shulker-side").setTextureLocation("end_purpur/shulker-top", Axis.Y).next("tileBrokenPurpur").next("tilePurpur").build();
        }
    },

    ENDER_PEARL_BLOCK {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "ender_pearl_block", provider).newVariation("enderEye-0-0").next("enderEye-0-1").next("enderEye-1-0").next("enderEye-1-1").next("enderEye-2-0").next("enderEye-2-1").next("enderEye").next("enderZelda").next("resonantBricks-0").next("resonantBricks-1").next("resonantBricks-2").next("resonantBricks-3").next("resonantBricks").next("resonantSolid").build();
        }
    },

    ENDSTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("endstone", Blocks.end_stone.getDefaultState(), -1);
            factory.newBlock(Material.rock, "endstone", provider).newVariation("arcaneEndStone").next("chaoticBricks").next("CheckeredTile").next("enderCircuit").next("enderDiagonalBrick-0").next("enderDiagonalBrick-1").next("enderDiagonalBrick-2").next("enderDiagonalBrick-3").next("enderDiagonalBrick").next("EnderFrame-0").next("EnderFrame-1").next("EnderFrame-2").next("EnderFrame-3").next("EnderFrame-4").next("EnderFrame-5").next("EnderFrame-6").next("EnderFrame-7").next("EnderFrame-8").next("EnderFrame").next("endFrenchBricks").next("endPillar").setTextureLocation("endstone/endPillar-side").setTextureLocation("endstone/endPillar-top", Axis.Y).next("endPrismatic").next("endStoneChunk").next("endStoneEtched").setTextureLocation("endstone/endStoneEtched-side").setTextureLocation("endstone/endStoneEtched-top", Axis.Y).next("endStoneLargeTile").next("endStoneOrnate").next("end_bricks").next("framedEndStone").next("masonryEnder").next("prismaticEndStone").build();
        }
    },

    FACTORY {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "factory", provider).newVariation("circuit").next("column").setTextureLocation("factory/column-side").setTextureLocation("factory/column-top", Axis.Y).next("dots").next("frameblue").next("goldplate").next("goldplating").next("grinder").next("hazard").next("hazardorange").next("iceiceice").next("metalbox").setTextureLocation("factory/metalbox-side").setTextureLocation("factory/metalbox-top", Axis.Y).next("platex").next("plating").next("rust").next("rust2").next("rustplates").next("tilemosaic").next("vent").setTextureLocation("factory/vent-side").setTextureLocation("factory/vent-top", Axis.Y).next("wireframe").next("wireframeblue").next("wireframewhite").build();
        }
    },

    FANTASY {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "fantasy", provider).newVariation("block").next("brick-faded").setTextureLocation("fantasy/brick-faded-side").setTextureLocation("fantasy/brick-faded-top", Axis.Y).next("brick").setTextureLocation("fantasy/brick-side").setTextureLocation("fantasy/brick-top", Axis.Y).next("brick-wear").setTextureLocation("fantasy/brick-wear-side").setTextureLocation("fantasy/brick-wear-top", Axis.Y).next("bricks-chaotic").next("bricks").setTextureLocation("fantasy/bricks-side").setTextureLocation("fantasy/bricks-top", Axis.Y).next("bricks-wear").setTextureLocation("fantasy/bricks-wear-side").setTextureLocation("fantasy/bricks-wear-top", Axis.Y).next("decor-block").setTextureLocation("fantasy/decor-block-side").setTextureLocation("fantasy/decor-block-top", Axis.Y).next("decor").setTextureLocation("fantasy/decor-side").setTextureLocation("fantasy/decor-top", Axis.Y).next("gold-decor-1").setTextureLocation("fantasy/gold-decor-1-side").setTextureLocation("fantasy/gold-decor-1-top", Axis.Y).next("gold-decor-2").setTextureLocation("fantasy/gold-decor-2-side").setTextureLocation("fantasy/gold-decor-2-top", Axis.Y).next("gold-decor-3").setTextureLocation("fantasy/gold-decor-3-side").setTextureLocation("fantasy/gold-decor-3-top", Axis.Y).next("gold-decor-4").setTextureLocation("fantasy/gold-decor-4-side").setTextureLocation("fantasy/gold-decor-4-top", Axis.Y).next("pillar-decorated").setTextureLocation("fantasy/pillar-decorated-side").setTextureLocation("fantasy/pillar-decorated-top", Axis.Y).next("pillar").setTextureLocation("fantasy/pillar-side").setTextureLocation("fantasy/pillar-top", Axis.Y).next("plate").setTextureLocation("fantasy/plate-side").setTextureLocation("fantasy/plate-top", Axis.Y).build();
        }
    },

    FANTASY2 {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "fantasy2", provider).newVariation("block").next("brick-faded").setTextureLocation("fantasy2/brick-faded-side").setTextureLocation("fantasy2/brick-faded-top", Axis.Y).next("brick").setTextureLocation("fantasy2/brick-side").setTextureLocation("fantasy2/brick-top", Axis.Y).next("brick-wear").setTextureLocation("fantasy2/brick-wear-side").setTextureLocation("fantasy2/brick-wear-top", Axis.Y).next("bricks-chaotic").next("bricks").setTextureLocation("fantasy2/bricks-side").setTextureLocation("fantasy2/bricks-top", Axis.Y).next("bricks-wear").setTextureLocation("fantasy2/bricks-wear-side").setTextureLocation("fantasy2/bricks-wear-top", Axis.Y).next("decor-block").setTextureLocation("fantasy2/decor-block-side").setTextureLocation("fantasy2/decor-block-top", Axis.Y).next("decor").setTextureLocation("fantasy2/decor-side").setTextureLocation("fantasy2/decor-top", Axis.Y).next("gold-decor-1").setTextureLocation("fantasy2/gold-decor-1-side").setTextureLocation("fantasy2/gold-decor-1-top", Axis.Y).next("gold-decor-2").setTextureLocation("fantasy2/gold-decor-2-side").setTextureLocation("fantasy2/gold-decor-2-top", Axis.Y).next("gold-decor-3").setTextureLocation("fantasy2/gold-decor-3-side").setTextureLocation("fantasy2/gold-decor-3-top", Axis.Y).next("gold-decor-4").setTextureLocation("fantasy2/gold-decor-4-side").setTextureLocation("fantasy2/gold-decor-4-top", Axis.Y).next("pillar-decorated").setTextureLocation("fantasy2/pillar-decorated-side").setTextureLocation("fantasy2/pillar-decorated-top", Axis.Y).next("pillar").setTextureLocation("fantasy2/pillar-side").setTextureLocation("fantasy2/pillar-top", Axis.Y).next("plate").setTextureLocation("fantasy2/plate-side").setTextureLocation("fantasy2/plate-top", Axis.Y).build();
        }
    },

    FUTURA {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "futura", provider).newVariation("circuitPlate").build();
        }
    },

    GLASS {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("glass", Blocks.glass.getDefaultState(), -1);
            factory.newBlock(Material.rock, "glass", provider).newVariation("a1-glasswindow-ironfencemodern").next("chrono").next("japanese").next("terrain-glass-chinese").next("terrain-glass-ornatesteel").next("terrain-glass-ornatesteel-old").next("terrain-glass-screen").next("terrain-glass-steelframe").next("terrain-glass-thickgrid").next("terrain-glass-thingrid").next("terrain-glassbubble").next("terrain-glassdungeon").next("terrain-glasslight").next("terrain-glassnoborder").next("terrain-glassshale").next("terrain-glassstone").next("terrain-glassstreak").build();
        }
    },

    GLASSDYED {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "glassdyed", provider).newVariation("black-bubble").next("black-forestry").next("black-panel-fancy").next("black-panel").next("black-transparent").next("blue-bubble").next("blue-forestry").next("blue-panel-fancy").next("blue-panel").next("blue-transparent").next("brown-bubble").next("brown-forestry").next("brown-panel-fancy").next("brown-panel").next("brown-transparent").next("cyan-bubble").next("cyan-forestry").next("cyan-panel-fancy").next("cyan-panel").next("cyan-transparent").next("gray-bubble").next("gray-forestry").next("gray-panel-fancy").next("gray-panel").next("gray-transparent").next("green-bubble").next("green-forestry").next("green-panel-fancy").next("green-panel").next("green-transparent").next("lightblue-bubble").next("lightblue-forestry").next("lightblue-panel-fancy").next("lightblue-panel").next("lightblue-transparent").next("lightgray-bubble").next("lightgray-forestry").next("lightgray-panel-fancy").next("lightgray-panel").next("lightgray-transparent").next("lime-bubble").next("lime-forestry").next("lime-panel-fancy").next("lime-panel").next("lime-transparent").next("magenta-bubble").next("magenta-forestry").next("magenta-panel-fancy").next("magenta-panel").next("magenta-transparent").next("orange-bubble").next("orange-forestry").next("orange-panel-fancy").next("orange-panel").next("orange-transparent").next("pink-bubble").next("pink-forestry").next("pink-panel-fancy").next("pink-panel").next("pink-transparent").next("purple-bubble").next("purple-forestry").next("purple-panel-fancy").next("purple-panel").next("purple-transparent").next("red-bubble").next("red-forestry").next("red-panel-fancy").next("red-panel").next("red-transparent").next("white-bubble").next("white-forestry").next("white-panel-fancy").next("white-panel").next("white-transparent").next("yellow-bubble").next("yellow-forestry").next("yellow-panel-fancy").next("yellow-panel").next("yellow-transparent").build();
        }
    },

    GLASSPANE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "glasspane", provider).newVariation("chinese").setTextureLocation("glasspane/chinese-side").setTextureLocation("glasspane/chinese-top", Axis.Y).next("chinese2").setTextureLocation("glasspane/chinese2-side").setTextureLocation("glasspane/chinese2-top", Axis.Y).next("japanese").setTextureLocation("glasspane/japanese-side").setTextureLocation("glasspane/japanese-top", Axis.Y).next("japanese2").setTextureLocation("glasspane/japanese2-side").setTextureLocation("glasspane/japanese2-top", Axis.Y).next("terrain-glass-screen").next("terrain-glassbubble").setTextureLocation("glasspane/terrain-glassbubble-side").setTextureLocation("glasspane/terrain-glassbubble-top", Axis.Y).next("terrain-glassnoborder").setTextureLocation("glasspane/terrain-glassnoborder-side").setTextureLocation("glasspane/terrain-glassnoborder-top", Axis.Y).next("terrain-glassstreak").setTextureLocation("glasspane/terrain-glassstreak-side").setTextureLocation("glasspane/terrain-glassstreak-top", Axis.Y).build();
        }
    },

    GLASSPANEDYED {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "glasspanedyed", provider).newVariation("black-bubble").setTextureLocation("glasspanedyed/black-bubble-side").setTextureLocation("glasspanedyed/black-bubble-top", Axis.Y).next("black-panel-fancy").setTextureLocation("glasspanedyed/black-panel-fancy-side").setTextureLocation("glasspanedyed/black-panel-fancy-top", Axis.Y).next("black-panel").setTextureLocation("glasspanedyed/black-panel-side").setTextureLocation("glasspanedyed/black-panel-top", Axis.Y).next("black-quad-fancy").setTextureLocation("glasspanedyed/black-quad-fancy-side").setTextureLocation("glasspanedyed/black-quad-fancy-top", Axis.Y).next("black-quad").setTextureLocation("glasspanedyed/black-quad-side").setTextureLocation("glasspanedyed/black-quad-top", Axis.Y).next("black-transparent").setTextureLocation("glasspanedyed/black-transparent-side").setTextureLocation("glasspanedyed/black-transparent-top", Axis.Y).next("blue-bubble").setTextureLocation("glasspanedyed/blue-bubble-side").setTextureLocation("glasspanedyed/blue-bubble-top", Axis.Y).next("blue-panel-fancy").setTextureLocation("glasspanedyed/blue-panel-fancy-side").setTextureLocation("glasspanedyed/blue-panel-fancy-top", Axis.Y).next("blue-panel").setTextureLocation("glasspanedyed/blue-panel-side").setTextureLocation("glasspanedyed/blue-panel-top", Axis.Y).next("blue-quad-fancy").setTextureLocation("glasspanedyed/blue-quad-fancy-side").setTextureLocation("glasspanedyed/blue-quad-fancy-top", Axis.Y).next("blue-quad").setTextureLocation("glasspanedyed/blue-quad-side").setTextureLocation("glasspanedyed/blue-quad-top", Axis.Y).next("blue-transparent").setTextureLocation("glasspanedyed/blue-transparent-side").setTextureLocation("glasspanedyed/blue-transparent-top", Axis.Y).next("brown-bubble").setTextureLocation("glasspanedyed/brown-bubble-side").setTextureLocation("glasspanedyed/brown-bubble-top", Axis.Y).next("brown-panel-fancy").setTextureLocation("glasspanedyed/brown-panel-fancy-side").setTextureLocation("glasspanedyed/brown-panel-fancy-top", Axis.Y).next("brown-panel").setTextureLocation("glasspanedyed/brown-panel-side").setTextureLocation("glasspanedyed/brown-panel-top", Axis.Y).next("brown-quad-fancy").setTextureLocation("glasspanedyed/brown-quad-fancy-side").setTextureLocation("glasspanedyed/brown-quad-fancy-top", Axis.Y).next("brown-quad").setTextureLocation("glasspanedyed/brown-quad-side").setTextureLocation("glasspanedyed/brown-quad-top", Axis.Y).next("brown-transparent").setTextureLocation("glasspanedyed/brown-transparent-side").setTextureLocation("glasspanedyed/brown-transparent-top", Axis.Y).next("cyan-bubble").setTextureLocation("glasspanedyed/cyan-bubble-side").setTextureLocation("glasspanedyed/cyan-bubble-top", Axis.Y).next("cyan-panel-fancy").setTextureLocation("glasspanedyed/cyan-panel-fancy-side").setTextureLocation("glasspanedyed/cyan-panel-fancy-top", Axis.Y).next("cyan-panel").setTextureLocation("glasspanedyed/cyan-panel-side").setTextureLocation("glasspanedyed/cyan-panel-top", Axis.Y).next("cyan-quad-fancy").setTextureLocation("glasspanedyed/cyan-quad-fancy-side").setTextureLocation("glasspanedyed/cyan-quad-fancy-top", Axis.Y).next("cyan-quad").setTextureLocation("glasspanedyed/cyan-quad-side").setTextureLocation("glasspanedyed/cyan-quad-top", Axis.Y).next("cyan-transparent").setTextureLocation("glasspanedyed/cyan-transparent-side").setTextureLocation("glasspanedyed/cyan-transparent-top", Axis.Y).next("gray-bubble").setTextureLocation("glasspanedyed/gray-bubble-side").setTextureLocation("glasspanedyed/gray-bubble-top", Axis.Y).next("gray-panel-fancy").setTextureLocation("glasspanedyed/gray-panel-fancy-side").setTextureLocation("glasspanedyed/gray-panel-fancy-top", Axis.Y).next("gray-panel").setTextureLocation("glasspanedyed/gray-panel-side").setTextureLocation("glasspanedyed/gray-panel-top", Axis.Y).next("gray-quad-fancy").setTextureLocation("glasspanedyed/gray-quad-fancy-side").setTextureLocation("glasspanedyed/gray-quad-fancy-top", Axis.Y).next("gray-quad").setTextureLocation("glasspanedyed/gray-quad-side").setTextureLocation("glasspanedyed/gray-quad-top", Axis.Y).next("gray-transparent").setTextureLocation("glasspanedyed/gray-transparent-side").setTextureLocation("glasspanedyed/gray-transparent-top", Axis.Y).next("green-bubble").setTextureLocation("glasspanedyed/green-bubble-side").setTextureLocation("glasspanedyed/green-bubble-top", Axis.Y).next("green-panel-fancy").setTextureLocation("glasspanedyed/green-panel-fancy-side").setTextureLocation("glasspanedyed/green-panel-fancy-top", Axis.Y).next("green-panel").setTextureLocation("glasspanedyed/green-panel-side").setTextureLocation("glasspanedyed/green-panel-top", Axis.Y).next("green-quad-fancy").setTextureLocation("glasspanedyed/green-quad-fancy-side").setTextureLocation("glasspanedyed/green-quad-fancy-top", Axis.Y).next("green-quad").setTextureLocation("glasspanedyed/green-quad-side").setTextureLocation("glasspanedyed/green-quad-top", Axis.Y).next("green-transparent").setTextureLocation("glasspanedyed/green-transparent-side").setTextureLocation("glasspanedyed/green-transparent-top", Axis.Y).next("lightblue-bubble").setTextureLocation("glasspanedyed/lightblue-bubble-side").setTextureLocation("glasspanedyed/lightblue-bubble-top", Axis.Y).next("lightblue-panel-fancy").setTextureLocation("glasspanedyed/lightblue-panel-fancy-side").setTextureLocation("glasspanedyed/lightblue-panel-fancy-top", Axis.Y).next("lightblue-panel").setTextureLocation("glasspanedyed/lightblue-panel-side").setTextureLocation("glasspanedyed/lightblue-panel-top", Axis.Y).next("lightblue-quad-fancy").setTextureLocation("glasspanedyed/lightblue-quad-fancy-side").setTextureLocation("glasspanedyed/lightblue-quad-fancy-top", Axis.Y).next("lightblue-quad").setTextureLocation("glasspanedyed/lightblue-quad-side").setTextureLocation("glasspanedyed/lightblue-quad-top", Axis.Y).next("lightblue-transparent").setTextureLocation("glasspanedyed/lightblue-transparent-side").setTextureLocation("glasspanedyed/lightblue-transparent-top", Axis.Y).next("lightgray-bubble").setTextureLocation("glasspanedyed/lightgray-bubble-side").setTextureLocation("glasspanedyed/lightgray-bubble-top", Axis.Y).next("lightgray-panel-fancy").setTextureLocation("glasspanedyed/lightgray-panel-fancy-side").setTextureLocation("glasspanedyed/lightgray-panel-fancy-top", Axis.Y).next("lightgray-panel").setTextureLocation("glasspanedyed/lightgray-panel-side").setTextureLocation("glasspanedyed/lightgray-panel-top", Axis.Y).next("lightgray-quad-fancy").setTextureLocation("glasspanedyed/lightgray-quad-fancy-side").setTextureLocation("glasspanedyed/lightgray-quad-fancy-top", Axis.Y).next("lightgray-quad").setTextureLocation("glasspanedyed/lightgray-quad-side").setTextureLocation("glasspanedyed/lightgray-quad-top", Axis.Y).next("lightgray-transparent").setTextureLocation("glasspanedyed/lightgray-transparent-side").setTextureLocation("glasspanedyed/lightgray-transparent-top", Axis.Y).next("lime-bubble").setTextureLocation("glasspanedyed/lime-bubble-side").setTextureLocation("glasspanedyed/lime-bubble-top", Axis.Y).next("lime-panel-fancy").setTextureLocation("glasspanedyed/lime-panel-fancy-side").setTextureLocation("glasspanedyed/lime-panel-fancy-top", Axis.Y).next("lime-panel").setTextureLocation("glasspanedyed/lime-panel-side").setTextureLocation("glasspanedyed/lime-panel-top", Axis.Y).next("lime-quad-fancy").setTextureLocation("glasspanedyed/lime-quad-fancy-side").setTextureLocation("glasspanedyed/lime-quad-fancy-top", Axis.Y).next("lime-quad").setTextureLocation("glasspanedyed/lime-quad-side").setTextureLocation("glasspanedyed/lime-quad-top", Axis.Y).next("lime-transparent").setTextureLocation("glasspanedyed/lime-transparent-side").setTextureLocation("glasspanedyed/lime-transparent-top", Axis.Y).next("magenta-bubble").setTextureLocation("glasspanedyed/magenta-bubble-side").setTextureLocation("glasspanedyed/magenta-bubble-top", Axis.Y).next("magenta-panel-fancy").setTextureLocation("glasspanedyed/magenta-panel-fancy-side").setTextureLocation("glasspanedyed/magenta-panel-fancy-top", Axis.Y).next("magenta-panel").setTextureLocation("glasspanedyed/magenta-panel-side").setTextureLocation("glasspanedyed/magenta-panel-top", Axis.Y).next("magenta-quad-fancy").setTextureLocation("glasspanedyed/magenta-quad-fancy-side").setTextureLocation("glasspanedyed/magenta-quad-fancy-top", Axis.Y).next("magenta-quad").setTextureLocation("glasspanedyed/magenta-quad-side").setTextureLocation("glasspanedyed/magenta-quad-top", Axis.Y).next("magenta-transparent").setTextureLocation("glasspanedyed/magenta-transparent-side").setTextureLocation("glasspanedyed/magenta-transparent-top", Axis.Y).next("orange-bubble").setTextureLocation("glasspanedyed/orange-bubble-side").setTextureLocation("glasspanedyed/orange-bubble-top", Axis.Y).next("orange-panel-fancy").setTextureLocation("glasspanedyed/orange-panel-fancy-side").setTextureLocation("glasspanedyed/orange-panel-fancy-top", Axis.Y).next("orange-panel").setTextureLocation("glasspanedyed/orange-panel-side").setTextureLocation("glasspanedyed/orange-panel-top", Axis.Y).next("orange-quad-fancy").setTextureLocation("glasspanedyed/orange-quad-fancy-side").setTextureLocation("glasspanedyed/orange-quad-fancy-top", Axis.Y).next("orange-quad").setTextureLocation("glasspanedyed/orange-quad-side").setTextureLocation("glasspanedyed/orange-quad-top", Axis.Y).next("orange-transparent").setTextureLocation("glasspanedyed/orange-transparent-side").setTextureLocation("glasspanedyed/orange-transparent-top", Axis.Y).next("pink-bubble").setTextureLocation("glasspanedyed/pink-bubble-side").setTextureLocation("glasspanedyed/pink-bubble-top", Axis.Y).next("pink-panel-fancy").setTextureLocation("glasspanedyed/pink-panel-fancy-side").setTextureLocation("glasspanedyed/pink-panel-fancy-top", Axis.Y).next("pink-panel").setTextureLocation("glasspanedyed/pink-panel-side").setTextureLocation("glasspanedyed/pink-panel-top", Axis.Y).next("pink-quad-fancy").setTextureLocation("glasspanedyed/pink-quad-fancy-side").setTextureLocation("glasspanedyed/pink-quad-fancy-top", Axis.Y).next("pink-quad").setTextureLocation("glasspanedyed/pink-quad-side").setTextureLocation("glasspanedyed/pink-quad-top", Axis.Y).next("pink-transparent").setTextureLocation("glasspanedyed/pink-transparent-side").setTextureLocation("glasspanedyed/pink-transparent-top", Axis.Y).next("purple-bubble").setTextureLocation("glasspanedyed/purple-bubble-side").setTextureLocation("glasspanedyed/purple-bubble-top", Axis.Y).next("purple-panel-fancy").setTextureLocation("glasspanedyed/purple-panel-fancy-side").setTextureLocation("glasspanedyed/purple-panel-fancy-top", Axis.Y).next("purple-panel").setTextureLocation("glasspanedyed/purple-panel-side").setTextureLocation("glasspanedyed/purple-panel-top", Axis.Y).next("purple-quad-fancy").setTextureLocation("glasspanedyed/purple-quad-fancy-side").setTextureLocation("glasspanedyed/purple-quad-fancy-top", Axis.Y).next("purple-quad").setTextureLocation("glasspanedyed/purple-quad-side").setTextureLocation("glasspanedyed/purple-quad-top", Axis.Y).next("purple-transparent").setTextureLocation("glasspanedyed/purple-transparent-side").setTextureLocation("glasspanedyed/purple-transparent-top", Axis.Y).next("red-bubble").setTextureLocation("glasspanedyed/red-bubble-side").setTextureLocation("glasspanedyed/red-bubble-top", Axis.Y).next("red-panel-fancy").setTextureLocation("glasspanedyed/red-panel-fancy-side").setTextureLocation("glasspanedyed/red-panel-fancy-top", Axis.Y).next("red-panel").setTextureLocation("glasspanedyed/red-panel-side").setTextureLocation("glasspanedyed/red-panel-top", Axis.Y).next("red-quad-fancy").setTextureLocation("glasspanedyed/red-quad-fancy-side").setTextureLocation("glasspanedyed/red-quad-fancy-top", Axis.Y).next("red-quad").setTextureLocation("glasspanedyed/red-quad-side").setTextureLocation("glasspanedyed/red-quad-top", Axis.Y).next("red-transparent").setTextureLocation("glasspanedyed/red-transparent-side").setTextureLocation("glasspanedyed/red-transparent-top", Axis.Y).next("white-bubble").setTextureLocation("glasspanedyed/white-bubble-side").setTextureLocation("glasspanedyed/white-bubble-top", Axis.Y).next("white-panel-fancy").setTextureLocation("glasspanedyed/white-panel-fancy-side").setTextureLocation("glasspanedyed/white-panel-fancy-top", Axis.Y).next("white-panel").setTextureLocation("glasspanedyed/white-panel-side").setTextureLocation("glasspanedyed/white-panel-top", Axis.Y).next("white-quad-fancy").setTextureLocation("glasspanedyed/white-quad-fancy-side").setTextureLocation("glasspanedyed/white-quad-fancy-top", Axis.Y).next("white-quad").setTextureLocation("glasspanedyed/white-quad-side").setTextureLocation("glasspanedyed/white-quad-top", Axis.Y).next("white-transparent").setTextureLocation("glasspanedyed/white-transparent-side").setTextureLocation("glasspanedyed/white-transparent-top", Axis.Y).next("yellow-bubble").setTextureLocation("glasspanedyed/yellow-bubble-side").setTextureLocation("glasspanedyed/yellow-bubble-top", Axis.Y).next("yellow-panel-fancy").setTextureLocation("glasspanedyed/yellow-panel-fancy-side").setTextureLocation("glasspanedyed/yellow-panel-fancy-top", Axis.Y).next("yellow-panel").setTextureLocation("glasspanedyed/yellow-panel-side").setTextureLocation("glasspanedyed/yellow-panel-top", Axis.Y).next("yellow-quad-fancy").setTextureLocation("glasspanedyed/yellow-quad-fancy-side").setTextureLocation("glasspanedyed/yellow-quad-fancy-top", Axis.Y).next("yellow-quad").setTextureLocation("glasspanedyed/yellow-quad-side").setTextureLocation("glasspanedyed/yellow-quad-top", Axis.Y).next("yellow-transparent").setTextureLocation("glasspanedyed/yellow-transparent-side").setTextureLocation("glasspanedyed/yellow-transparent-top", Axis.Y).build();
        }
    },

    GOLD {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("gold", Blocks.gold_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "gold", provider).newVariation("goldEye-0-0").next("goldEye-0-1").next("goldEye-1-0").next("goldEye-1-1").next("goldEye-2-0").next("goldEye-2-1").next("goldEye").next("terrain-gold-brick-bottom").next("terrain-gold-brick").setTextureLocation("gold/terrain-gold-brick-side").setTextureLocation("gold/terrain-gold-brick-top", Axis.Y).next("terrain-gold-cart-bottom").next("terrain-gold-cart").setTextureLocation("gold/terrain-gold-cart-side").setTextureLocation("gold/terrain-gold-cart-top", Axis.Y).next("terrain-gold-coin-heads-bottom").next("terrain-gold-coin-heads").setTextureLocation("gold/terrain-gold-coin-heads-side").setTextureLocation("gold/terrain-gold-coin-heads-top", Axis.Y).next("terrain-gold-coin-tails-bottom").next("terrain-gold-coin-tails").setTextureLocation("gold/terrain-gold-coin-tails-side").setTextureLocation("gold/terrain-gold-coin-tails-top", Axis.Y).next("terrain-gold-crate-dark-bottom").next("terrain-gold-crate-dark").setTextureLocation("gold/terrain-gold-crate-dark-side").setTextureLocation("gold/terrain-gold-crate-dark-top", Axis.Y).next("terrain-gold-crate-light-bottom").next("terrain-gold-crate-light").setTextureLocation("gold/terrain-gold-crate-light-side").setTextureLocation("gold/terrain-gold-crate-light-top", Axis.Y).next("terrain-gold-largeingot-bottom").next("terrain-gold-largeingot").setTextureLocation("gold/terrain-gold-largeingot-side").setTextureLocation("gold/terrain-gold-largeingot-top", Axis.Y).next("terrain-gold-plates-bottom").next("terrain-gold-plates").setTextureLocation("gold/terrain-gold-plates-side").setTextureLocation("gold/terrain-gold-plates-top", Axis.Y).next("terrain-gold-rivets-bottom").next("terrain-gold-rivets").setTextureLocation("gold/terrain-gold-rivets-side").setTextureLocation("gold/terrain-gold-rivets-top", Axis.Y).next("terrain-gold-simple-bottom").next("terrain-gold-simple").setTextureLocation("gold/terrain-gold-simple-side").setTextureLocation("gold/terrain-gold-simple-top", Axis.Y).next("terrain-gold-smallingot-bottom").next("terrain-gold-smallingot").setTextureLocation("gold/terrain-gold-smallingot-side").setTextureLocation("gold/terrain-gold-smallingot-top", Axis.Y).next("terrain-gold-space-bottom").next("terrain-gold-space").setTextureLocation("gold/terrain-gold-space-side").setTextureLocation("gold/terrain-gold-space-top", Axis.Y).next("terrain-gold-spaceblack-bottom").next("terrain-gold-spaceblack").setTextureLocation("gold/terrain-gold-spaceblack-side").setTextureLocation("gold/terrain-gold-spaceblack-top", Axis.Y).next("terrain-gold-star-bottom").next("terrain-gold-star").setTextureLocation("gold/terrain-gold-star-side").setTextureLocation("gold/terrain-gold-star-top", Axis.Y).build();
        }
    },

    GRANITE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stone = Blocks.stone.getDefaultState();
            IProperty<EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("granite", stone.withProperty(prop, EnumType.GRANITE), -2);
            Carving.chisel.addVariation("granite", stone.withProperty(prop, EnumType.GRANITE_SMOOTH), -1);
            
            factory.newBlock(Material.rock, "granite", provider).newVariation("graniteDiagonalBricks").next("graniteLBrick").next("graniteOrnate")
            .next("granitePillar").setTextureLocation("granite/granitePillar-side").setTextureLocation("granite/granitePillar-top", Axis.Y)
            .next("granitePrism").next("granitePrismatic").next("graniteTiles").build();
        }
    },

    GRIMSTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "grimstone", provider).newVariation("blocks-rough").next("blocks").next("brick").next("chiseled").setTextureLocation("grimstone/chiseled-side").setTextureLocation("grimstone/chiseled-top", Axis.Y).next("chunks").next("construction").next("fancy-tiles").next("flaky").next("grimstone").next("hate").next("largebricks").next("plate").next("plate-rough").next("platform-bottom").next("platform-tiles-bottom").next("platform-tiles").setTextureLocation("grimstone/platform-tiles-side").setTextureLocation("grimstone/platform-tiles-top", Axis.Y).next("platform").setTextureLocation("grimstone/platform-side").setTextureLocation("grimstone/platform-top", Axis.Y).next("roughblocks").next("smooth").next("tiles").build();
        }
    },

    HEX_PLATING {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "hexPlating", provider).newVariation("hexAnim").next("hexBase").next("hexNew").next("hexOverlay").build();
        }
    },

    HOLYSTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "holystone", provider).newVariation("blocks-rough").next("blocks").next("brick").next("chiseled").setTextureLocation("holystone/chiseled-side").setTextureLocation("holystone/chiseled-top", Axis.Y).next("construction").next("fancy-tiles").next("holystone").next("largebricks").next("love").next("plate").next("plate-rough").next("platform-bottom").next("platform-tiles-bottom").next("platform-tiles").setTextureLocation("holystone/platform-tiles-side").setTextureLocation("holystone/platform-tiles-top", Axis.Y).next("platform").setTextureLocation("holystone/platform-side").setTextureLocation("holystone/platform-top", Axis.Y).next("smooth").build();
        }
    },

    ICE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("ice", Blocks.ice.getDefaultState(), -1);
            factory.newBlock(Material.rock, "ice", provider).newVariation("a1-ice-light").next("a1-netherbrick-ice").next("a1-stonecobble-icebrick").next("a1-stonecobble-icebricksmall").next("a1-stonecobble-icecobble").next("a1-stonecobble-icedungeon").setTextureLocation("ice/a1-stonecobble-icedungeon-side").setTextureLocation("ice/a1-stonecobble-icedungeon-top", Axis.Y).next("a1-stonecobble-icefour").next("a1-stonecobble-icefrench").next("a1-stonecobble-icepanel").next("a1-stoneslab-ice").setTextureLocation("ice/a1-stoneslab-ice-side").setTextureLocation("ice/a1-stoneslab-ice-top", Axis.Y).next("bismuth").next("poison").next("scribbles").setTextureLocation("ice/scribbles-side").setTextureLocation("ice/scribbles-top", Axis.Y).next("sunkentiles").next("tiles").next("zelda").build();
        }
    },

    ICEPILLAR {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "icepillar", provider).newVariation("carved").setTextureLocation("icepillar/carved-side").setTextureLocation("icepillar/carved-top", Axis.Y).next("convexplain").setTextureLocation("icepillar/convexplain-side").setTextureLocation("icepillar/convexplain-top", Axis.Y).next("greekgreek").setTextureLocation("icepillar/greekgreek-side").setTextureLocation("icepillar/greekgreek-top", Axis.Y).next("greekplain").setTextureLocation("icepillar/greekplain-side").setTextureLocation("icepillar/greekplain-top", Axis.Y).next("ornamental").setTextureLocation("icepillar/ornamental-side").setTextureLocation("icepillar/ornamental-top", Axis.Y).next("plaingreek").setTextureLocation("icepillar/plaingreek-side").setTextureLocation("icepillar/plaingreek-top", Axis.Y).next("plainplain").setTextureLocation("icepillar/plainplain-side").setTextureLocation("icepillar/plainplain-top", Axis.Y).build();
        }
    },

    INVAR {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "invar", provider).setParentFolder("metals/invar").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/invar/thermal-side").setTextureLocation("metals/invar/thermal-top", Axis.Y).build();
        }
    },

    IRON {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("iron", Blocks.iron_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "iron", provider).newVariation("terrain-iron-brick-bottom").next("terrain-iron-brick").setTextureLocation("iron/terrain-iron-brick-side").setTextureLocation("iron/terrain-iron-brick-top", Axis.Y).next("terrain-iron-coin-heads-bottom").next("terrain-iron-coin-heads").setTextureLocation("iron/terrain-iron-coin-heads-side").setTextureLocation("iron/terrain-iron-coin-heads-top", Axis.Y).next("terrain-iron-coin-tails-bottom").next("terrain-iron-coin-tails").setTextureLocation("iron/terrain-iron-coin-tails-side").setTextureLocation("iron/terrain-iron-coin-tails-top", Axis.Y).next("terrain-iron-crate-dark-bottom").next("terrain-iron-crate-dark").setTextureLocation("iron/terrain-iron-crate-dark-side").setTextureLocation("iron/terrain-iron-crate-dark-top", Axis.Y).next("terrain-iron-crate-light-bottom").next("terrain-iron-crate-light").setTextureLocation("iron/terrain-iron-crate-light-side").setTextureLocation("iron/terrain-iron-crate-light-top", Axis.Y).next("terrain-iron-gears").setTextureLocation("iron/terrain-iron-gears-side").setTextureLocation("iron/terrain-iron-gears-top", Axis.Y).next("terrain-iron-largeingot-bottom").next("terrain-iron-largeingot").setTextureLocation("iron/terrain-iron-largeingot-side").setTextureLocation("iron/terrain-iron-largeingot-top", Axis.Y).next("terrain-iron-moon-bottom").next("terrain-iron-moon").setTextureLocation("iron/terrain-iron-moon-side").setTextureLocation("iron/terrain-iron-moon-top", Axis.Y).next("terrain-iron-plates-bottom").next("terrain-iron-plates").setTextureLocation("iron/terrain-iron-plates-side").setTextureLocation("iron/terrain-iron-plates-top", Axis.Y).next("terrain-iron-rivets-bottom").next("terrain-iron-rivets").setTextureLocation("iron/terrain-iron-rivets-side").setTextureLocation("iron/terrain-iron-rivets-top", Axis.Y).next("terrain-iron-simple-bottom").next("terrain-iron-simple").setTextureLocation("iron/terrain-iron-simple-side").setTextureLocation("iron/terrain-iron-simple-top", Axis.Y).next("terrain-iron-smallingot-bottom").next("terrain-iron-smallingot").setTextureLocation("iron/terrain-iron-smallingot-side").setTextureLocation("iron/terrain-iron-smallingot-top", Axis.Y).next("terrain-iron-space").next("terrain-iron-spaceblack").next("terrain-iron-vents").setTextureLocation("iron/terrain-iron-vents-side").setTextureLocation("iron/terrain-iron-vents-top", Axis.Y).build();
        }
    },

    IRONPANE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "ironpane", provider).newVariation("a1-ironbars-ironclassic").next("a1-ironbars-ironclassicnew").next("a1-ironbars-ironfence").next("a1-ironbars-ironfencemodern").next("barbedwire-bottom").next("barbedwire").setTextureLocation("ironpane/barbedwire-side").setTextureLocation("ironpane/barbedwire-top", Axis.Y).next("bars-bottom").next("bars").setTextureLocation("ironpane/bars-side").setTextureLocation("ironpane/bars-top", Axis.Y).next("cage").setTextureLocation("ironpane/cage-side").setTextureLocation("ironpane/cage-top", Axis.Y).next("fenceIron-bottom").next("fenceIron").setTextureLocation("ironpane/fenceIron-side").setTextureLocation("ironpane/fenceIron-top", Axis.Y).next("fenceIron").next("fenceIronTop-bottom").next("fenceIronTop").setTextureLocation("ironpane/fenceIronTop-side").setTextureLocation("ironpane/fenceIronTop-top", Axis.Y).next("spikes-bottom").next("spikes").setTextureLocation("ironpane/spikes-side").setTextureLocation("ironpane/spikes-top", Axis.Y).next("terrain-glass-ornatesteel").setTextureLocation("ironpane/terrain-glass-ornatesteel-side").setTextureLocation("ironpane/terrain-glass-ornatesteel-top", Axis.Y).next("terrain-glass-thickgrid").next("terrain-glass-thingrid").setTextureLocation("ironpane/terrain-glass-thingrid-side").setTextureLocation("ironpane/terrain-glass-thingrid-top", Axis.Y).build();
        }
    },

    LABORATORY {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "laboratory", provider).newVariation("checkertile").next("clearscreen").next("directionleft").next("directionright").next("dottedpanel").setTextureLocation("laboratory/dottedpanel-side").setTextureLocation("laboratory/dottedpanel-top", Axis.Y).next("floortile").next("fuzzscreen").next("infocon").next("largesteel").setTextureLocation("laboratory/largesteel-side").setTextureLocation("laboratory/largesteel-top", Axis.Y).next("largetile").next("largewall").next("roundel").next("smallsteel").setTextureLocation("laboratory/smallsteel-side").setTextureLocation("laboratory/smallsteel-top", Axis.Y).next("smalltile").next("wallpanel").setTextureLocation("laboratory/wallpanel-side").setTextureLocation("laboratory/wallpanel-top", Axis.Y).next("wallvents").build();
        }
    },

    LAPIS {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("lapis", Blocks.lapis_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "lapis", provider).newVariation("a1-blocklapis-ornatelayer").next("a1-blocklapis-panel").next("a1-blocklapis-smooth").next("masonryLapis").next("terrain-lapisblock-chunky").next("terrain-lapisblock-panel").next("terrain-lapisblock-zelda").next("terrain-lapisornate").next("terrain-lapistile").build();
        }
    },

    LAVASTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "lavastone", provider).newVariation("black").next("black1").next("chaotic").next("cobble").next("creeper").next("dark").next("lava_flow").next("panel-ornate").next("panel").next("tiles").build();
        }
    },

    LEAD {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "lead", provider).setParentFolder("metals/lead").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/lead/thermal-side").setTextureLocation("metals/lead/thermal-top", Axis.Y).build();
        }
    },

    LEAVES {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "leaves", provider).newVariation("christmasBalls").next("christmasBalls_opaque").next("christmasLights").next("christmasLights_opaque").next("dead").next("dead_opaque").next("fancy").next("fancy_opaque").next("pinkpetal").next("pinkpetal_opaque").next("red_roses").next("red_roses_opaque").next("roses").next("roses_opaque").build();
        }
    },

    LIGHTSTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "lightstone", provider).newVariation("a1-glowstone-cobble").next("a1-glowstone-growth").next("a1-glowstone-layers").next("a1-glowstone-tilecorroded").next("glowstone-bismuth-panel").next("glowstone-bismuth").next("terrain-sulphur-cobble").next("terrain-sulphur-corroded").next("terrain-sulphur-glass").next("terrain-sulphur-neon").next("terrain-sulphur-ornate").next("terrain-sulphur-rocky").next("terrain-sulphur-shale").next("terrain-sulphur-tile").next("terrain-sulphur-weavelanternlight").build();
        }
    },

    LIMESTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "limestone", provider).newVariation("terrain-cob-french2light").next("terrain-cob-frenchlight").next("terrain-cob-smallbricklight").next("terrain-cobbsmalltilelight").next("terrain-cobmoss-creepdungeonlight").next("terrain-mossysmalltilelight").next("terrain-pistonback-dungeon").next("terrain-pistonback-dungeonornate").next("terrain-pistonback-dungeonvent").next("terrain-pistonback-lightcreeper").next("terrain-pistonback-lightdent").next("terrain-pistonback-lightemboss").next("terrain-pistonback-lightfour").next("terrain-pistonback-lightmarker").next("terrain-pistonback-lightpanel").build();
        }
    },

    LINE_MARKING {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "line-marking", provider).newVariation("double-white-center").next("double-white-long").next("double-yellow-center").next("double-yellow-long").next("white-center").next("white-long").next("yellow-center").next("yellow-long").build();
        }
    },

    LIVINGROCK {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "livingrock", provider).setParentFolder("botania/livingrock").newVariation("masonryLivingstone").build();
        }
    },

    LIVINGWOOD_PAPER {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "livingwood-paper", provider).newVariation("box").next("cross").next("door").next("floral").next("horizontal").next("plain").next("sixSections").next("throughMiddle").next("vertical").build();
        }
    },

    LIVINGWOOD_PLANKS {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "livingwood-planks", provider).newVariation("blinds").next("chaotic-hor").next("chaotic").next("clean").next("crate-fancy").next("crate").next("crateex").next("double").setTextureLocation("livingwood-planks/double-side").setTextureLocation("livingwood-planks/double-top", Axis.Y).next("fancy").next("large").next("panel-nails").next("parquet").next("short").next("vertical-uneven").next("vertical").build();
        }
    },

    LIVINGWOOD_RAW {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "livingwood-raw", provider).newVariation("livingwoodLogSide").next("livingwoodLogTop").next("livingwoodPileSide").next("livingwoodPileTop").build();
        }
    },

    MANASTEEL {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "manasteel", provider).newVariation("adv").next("bolted").next("card").next("caution").next("crate").next("egregious").next("elementiumEye-0-0").next("elementiumEye-0-1").next("elementiumEye-1-0").next("elementiumEye-1-1").next("elementiumEye-2-0").next("elementiumEye-2-1").next("thermal").build();
        }
    },

    MARBLE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "marble", provider).newVariation("a1-stoneornamental-marblebrick").setTextureLocation("marble/a1-stoneornamental-marblebrick-side").setTextureLocation("marble/a1-stoneornamental-marblebrick-top", Axis.Y).next("a1-stoneornamental-marblecarved").next("a1-stoneornamental-marblecarvedradial").next("a1-stoneornamental-marbleclassicpanel").next("a1-stoneornamental-marbleornate").next("block").next("marble-arranged-bricks").next("marble-blocks").next("marble-bricks").next("marble-fancy-bricks").next("panel").next("raw").next("terrain-pistonback-marblecreeperdark").setTextureLocation("marble/terrain-pistonback-marblecreeperdark-side").setTextureLocation("marble/terrain-pistonback-marblecreeperdark-top", Axis.Y).next("terrain-pistonback-marblecreeperlight").setTextureLocation("marble/terrain-pistonback-marblecreeperlight-side").setTextureLocation("marble/terrain-pistonback-marblecreeperlight-top", Axis.Y).next("terrain-pistonback-marbledent").next("terrain-pistonback-marbledent-small").build();
        }
    },

    MARBLEPILLAR {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            if (Configurations.oldPillars) {
                factory.newBlock(Material.rock, "marblepillarold", provider).newVariation("a1-stoneornamental-marblegreek").setTextureLocation("marblepillarold/a1-stoneornamental-marblegreek-side").setTextureLocation("marblepillarold/a1-stoneornamental-marblegreek-top", Axis.Y).next("a1-stonepillar-greek").setTextureLocation("marblepillarold/a1-stonepillar-greek-side").setTextureLocation("marblepillarold/a1-stonepillar-greek-top", Axis.Y).next("a1-stonepillar-greekbottomgreek").setTextureLocation("marblepillarold/a1-stonepillar-greekbottomgreek-side").setTextureLocation("marblepillarold/a1-stonepillar-greekbottomgreek-top", Axis.Y).next("a1-stonepillar-greekbottomplain").setTextureLocation("marblepillarold/a1-stonepillar-greekbottomplain-side").setTextureLocation("marblepillarold/a1-stonepillar-greekbottomplain-top", Axis.Y).next("a1-stonepillar-greektopgreek").setTextureLocation("marblepillarold/a1-stonepillar-greektopgreek-side").setTextureLocation("marblepillarold/a1-stonepillar-greektopgreek-top", Axis.Y).next("a1-stonepillar-greektopplain").setTextureLocation("marblepillarold/a1-stonepillar-greektopplain-side").setTextureLocation("marblepillarold/a1-stonepillar-greektopplain-top", Axis.Y).next("a1-stonepillar-plain").setTextureLocation("marblepillarold/a1-stonepillar-plain-side").setTextureLocation("marblepillarold/a1-stonepillar-plain-top", Axis.Y).next("a1-stonepillar-plainbottomgreek").setTextureLocation("marblepillarold/a1-stonepillar-plainbottomgreek-side").setTextureLocation("marblepillarold/a1-stonepillar-plainbottomgreek-top", Axis.Y).next("a1-stonepillar-plainbottomplain").setTextureLocation("marblepillarold/a1-stonepillar-plainbottomplain-side").setTextureLocation("marblepillarold/a1-stonepillar-plainbottomplain-top", Axis.Y).next("a1-stonepillar-plaintopgreek").setTextureLocation("marblepillarold/a1-stonepillar-plaintopgreek-side").setTextureLocation("marblepillarold/a1-stonepillar-plaintopgreek-top", Axis.Y).next("a1-stonepillar-plaintopplain").setTextureLocation("marblepillarold/a1-stonepillar-plaintopplain-side").setTextureLocation("marblepillarold/a1-stonepillar-plaintopplain-top", Axis.Y).next("base").setTextureLocation("marblepillarold/base-side").setTextureLocation("marblepillarold/base-top", Axis.Y).next("capstone").setTextureLocation("marblepillarold/capstone-side").setTextureLocation("marblepillarold/capstone-top", Axis.Y).next("column").setTextureLocation("marblepillarold/column-side").setTextureLocation("marblepillarold/column-top", Axis.Y).next("pillar-carved").setTextureLocation("marblepillarold/pillar-carved-side").setTextureLocation("marblepillarold/pillar-carved-top", Axis.Y).next("small").setTextureLocation("marblepillarold/small-side").setTextureLocation("marblepillarold/small-top", Axis.Y).build();
            } else {
                factory.newBlock(Material.rock, "marblepillar", provider).newVariation("carved").next("convex").next("default").next("greekdecor").next("greekgreek").next("greekplain").next("ornamental").next("pillar").next("plaindecor").next("plaingreek").next("rough").next("simple").next("widedecor").next("widegreek").next("wideplain").build();
            }
        }
    },

    MARBLEPILLARSLAB {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "marblepillarslab", provider).newVariation("carved").setTextureLocation("marblepillarslab/carved-side").setTextureLocation("marblepillarslab/carved-top", Axis.Y).next("convex").setTextureLocation("marblepillarslab/convex-side").setTextureLocation("marblepillarslab/convex-top", Axis.Y).next("default").setTextureLocation("marblepillarslab/default-side").setTextureLocation("marblepillarslab/default-top", Axis.Y).next("greekdecor").setTextureLocation("marblepillarslab/greekdecor-side").setTextureLocation("marblepillarslab/greekdecor-top", Axis.Y).next("greekgreek").setTextureLocation("marblepillarslab/greekgreek-side").setTextureLocation("marblepillarslab/greekgreek-top", Axis.Y).next("greekplain").setTextureLocation("marblepillarslab/greekplain-side").setTextureLocation("marblepillarslab/greekplain-top", Axis.Y).next("ornamental").setTextureLocation("marblepillarslab/ornamental-side").setTextureLocation("marblepillarslab/ornamental-top", Axis.Y).next("pillar").setTextureLocation("marblepillarslab/pillar-side").setTextureLocation("marblepillarslab/pillar-top", Axis.Y).next("plaindecor").setTextureLocation("marblepillarslab/plaindecor-side").setTextureLocation("marblepillarslab/plaindecor-top", Axis.Y).next("plaingreek").setTextureLocation("marblepillarslab/plaingreek-side").setTextureLocation("marblepillarslab/plaingreek-top", Axis.Y).next("plainplain").setTextureLocation("marblepillarslab/plainplain-side").setTextureLocation("marblepillarslab/plainplain-top", Axis.Y).next("rough").setTextureLocation("marblepillarslab/rough-side").setTextureLocation("marblepillarslab/rough-top", Axis.Y).next("simple").setTextureLocation("marblepillarslab/simple-side").setTextureLocation("marblepillarslab/simple-top", Axis.Y).next("widedecor").setTextureLocation("marblepillarslab/widedecor-side").setTextureLocation("marblepillarslab/widedecor-top", Axis.Y).next("widegreek").setTextureLocation("marblepillarslab/widegreek-side").setTextureLocation("marblepillarslab/widegreek-top", Axis.Y).next("wideplain").setTextureLocation("marblepillarslab/wideplain-side").setTextureLocation("marblepillarslab/wideplain-top", Axis.Y).build();
        }
    },

    MARBLEPILLARSLABOLD {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "marblepillarslabold", provider).newVariation("a1-stoneornamental-marblegreek").setTextureLocation("marblepillarslabold/a1-stoneornamental-marblegreek-side").setTextureLocation("marblepillarslabold/a1-stoneornamental-marblegreek-top", Axis.Y).next("a1-stonepillar-greek").setTextureLocation("marblepillarslabold/a1-stonepillar-greek-side").setTextureLocation("marblepillarslabold/a1-stonepillar-greek-top", Axis.Y).next("a1-stonepillar-greekbottomgreek").setTextureLocation("marblepillarslabold/a1-stonepillar-greekbottomgreek-side").setTextureLocation("marblepillarslabold/a1-stonepillar-greekbottomgreek-top", Axis.Y).next("a1-stonepillar-greekbottomplain").setTextureLocation("marblepillarslabold/a1-stonepillar-greekbottomplain-side").setTextureLocation("marblepillarslabold/a1-stonepillar-greekbottomplain-top", Axis.Y).next("a1-stonepillar-greektopgreek").setTextureLocation("marblepillarslabold/a1-stonepillar-greektopgreek-side").setTextureLocation("marblepillarslabold/a1-stonepillar-greektopgreek-top", Axis.Y).next("a1-stonepillar-greektopplain").setTextureLocation("marblepillarslabold/a1-stonepillar-greektopplain-side").setTextureLocation("marblepillarslabold/a1-stonepillar-greektopplain-top", Axis.Y).next("a1-stonepillar-plain").setTextureLocation("marblepillarslabold/a1-stonepillar-plain-side").setTextureLocation("marblepillarslabold/a1-stonepillar-plain-top", Axis.Y).next("a1-stonepillar-plainbottomgreek").setTextureLocation("marblepillarslabold/a1-stonepillar-plainbottomgreek-side").setTextureLocation("marblepillarslabold/a1-stonepillar-plainbottomgreek-top", Axis.Y).next("a1-stonepillar-plainbottomplain").setTextureLocation("marblepillarslabold/a1-stonepillar-plainbottomplain-side").setTextureLocation("marblepillarslabold/a1-stonepillar-plainbottomplain-top", Axis.Y).next("a1-stonepillar-plaintopgreek").setTextureLocation("marblepillarslabold/a1-stonepillar-plaintopgreek-side").setTextureLocation("marblepillarslabold/a1-stonepillar-plaintopgreek-top", Axis.Y).next("a1-stonepillar-plaintopplain").setTextureLocation("marblepillarslabold/a1-stonepillar-plaintopplain-side").setTextureLocation("marblepillarslabold/a1-stonepillar-plaintopplain-top", Axis.Y).next("base").setTextureLocation("marblepillarslabold/base-side").setTextureLocation("marblepillarslabold/base-top", Axis.Y).next("capstone").setTextureLocation("marblepillarslabold/capstone-side").setTextureLocation("marblepillarslabold/capstone-top", Axis.Y).next("column").setTextureLocation("marblepillarslabold/column-side").setTextureLocation("marblepillarslabold/column-top", Axis.Y).next("pillar-carved").setTextureLocation("marblepillarslabold/pillar-carved-side").setTextureLocation("marblepillarslabold/pillar-carved-top", Axis.Y).next("small").setTextureLocation("marblepillarslabold/small-side").setTextureLocation("marblepillarslabold/small-top", Axis.Y).build();
        }
    },

    MARBLESLAB {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "marbleslab", provider).newVariation("a1-stoneornamental-marblebrick").setTextureLocation("marbleslab/a1-stoneornamental-marblebrick-side").setTextureLocation("marbleslab/a1-stoneornamental-marblebrick-top", Axis.Y).next("a1-stoneornamental-marblecarved").setTextureLocation("marbleslab/a1-stoneornamental-marblecarved-side").setTextureLocation("marbleslab/a1-stoneornamental-marblecarved-top", Axis.Y).next("a1-stoneornamental-marblecarvedradial").setTextureLocation("marbleslab/a1-stoneornamental-marblecarvedradial-side").setTextureLocation("marbleslab/a1-stoneornamental-marblecarvedradial-top", Axis.Y).next("a1-stoneornamental-marbleclassicpanel").setTextureLocation("marbleslab/a1-stoneornamental-marbleclassicpanel-side").setTextureLocation("marbleslab/a1-stoneornamental-marbleclassicpanel-top", Axis.Y).next("a1-stoneornamental-marbleornate").setTextureLocation("marbleslab/a1-stoneornamental-marbleornate-side").setTextureLocation("marbleslab/a1-stoneornamental-marbleornate-top", Axis.Y).next("a1-stoneornamental-marblepanel").setTextureLocation("marbleslab/a1-stoneornamental-marblepanel-side").setTextureLocation("marbleslab/a1-stoneornamental-marblepanel-top", Axis.Y).next("marble-arranged-bricks").next("marble-blocks").next("marble-bricks").next("marble-fancy-bricks").setTextureLocation("marbleslab/marble-fancy-bricks-side").setTextureLocation("marbleslab/marble-fancy-bricks-top", Axis.Y).next("terrain-pistonback-marble").setTextureLocation("marbleslab/terrain-pistonback-marble-side").setTextureLocation("marbleslab/terrain-pistonback-marble-top", Axis.Y).next("terrain-pistonback-marblecreeperdark").setTextureLocation("marbleslab/terrain-pistonback-marblecreeperdark-side").setTextureLocation("marbleslab/terrain-pistonback-marblecreeperdark-top", Axis.Y).next("terrain-pistonback-marblecreeperlight").setTextureLocation("marbleslab/terrain-pistonback-marblecreeperlight-side").setTextureLocation("marbleslab/terrain-pistonback-marblecreeperlight-top", Axis.Y).next("terrain-pistonback-marbledent-small").setTextureLocation("marbleslab/terrain-pistonback-marbledent-small-side").setTextureLocation("marbleslab/terrain-pistonback-marbledent-small-top", Axis.Y).next("terrain-pistonback-marbledent").setTextureLocation("marbleslab/terrain-pistonback-marbledent-side").setTextureLocation("marbleslab/terrain-pistonback-marbledent-top", Axis.Y).build();
        }
    },

    MAZESTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "mazestone", provider).newVariation("circular").next("cobbled").next("intricate").next("masonryMazestone").next("mazestoneDiagonals-0").next("mazestoneDiagonals-1").next("mazestoneDiagonals-2").next("mazestoneDiagonals-3").next("mazestoneDiagonals").next("prismatic").next("prismaticMazestone").build();
        }
    },

    MECHANICAL2 {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "mechanical2", provider).newVariation("gear").next("hex").next("vent-glow").next("vent2").build();
        }
    },

    MILITARY {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "military", provider).newVariation("imperialCamo").next("imperialCamoSecluded").next("imperialCautionOrange").next("imperialCautionWhite").next("imperialPlate").next("rebelCamo").next("rebelCamoSecluded").next("rebelCautionRed").next("rebelCautionWhite").next("rebelPlate").build();
        }
    },

    NETHERBRICK {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("netherbrick", Blocks.nether_brick.getDefaultState(), -1);
            factory.newBlock(Material.rock, "netherbrick", provider).newVariation("a1-netherbrick-brinstar").next("a1-netherbrick-classicspatter").next("a1-netherbrick-guts").next("a1-netherbrick-gutsdark").next("a1-netherbrick-gutssmall").next("a1-netherbrick-lavabrinstar").next("a1-netherbrick-lavabrown").next("a1-netherbrick-lavaobsidian").next("a1-netherbrick-lavastonedark").next("a1-netherbrick-meat").next("a1-netherbrick-meatred").next("a1-netherbrick-meatredsmall").next("a1-netherbrick-meatsmall").next("a1-netherbrick-red").next("a1-netherbrick-redsmall").next("netherFancyBricks").build();
        }
    },

    NETHERRACK {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("netherrack", Blocks.netherrack.getDefaultState(), -1);
            factory.newBlock(Material.rock, "netherrack", provider).newVariation("a1-netherrack-bloodgravel").next("a1-netherrack-bloodrock").next("a1-netherrack-bloodrockgrey").next("a1-netherrack-brinstar").next("a1-netherrack-brinstarshale").next("a1-netherrack-classic").next("a1-netherrack-classicspatter").next("a1-netherrack-guts").next("a1-netherrack-gutsdark").next("a1-netherrack-meat").next("a1-netherrack-meatred").next("a1-netherrack-meatrock").next("a1-netherrack-red").next("a1-netherrack-wells").build();
        }
    },

    NEW {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "new", provider).setParentFolder("technical/new").newVariation("engineering-0").next("engineering-1").next("engineering-2").next("engineering-3").next("engineering").next("ExhaustPlating").next("MakeshiftPanels").next("MegaCell-bottom").next("MegaCell").setTextureLocation("technical/new/MegaCell-side").setTextureLocation("technical/new/MegaCell-top", Axis.Y).next("OldeTimeyServerAnim").next("Piping").next("scaffoldLarge-0").next("scaffoldLarge-1").next("scaffoldLarge-2").next("scaffoldLarge-3").next("scaffoldLarge").next("Sturdy").next("TapeDrive").next("weatheredGreenPanels").next("weatheredOrangePanels").build();
        }
    },

    NICKEL {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "nickel", provider).setParentFolder("metals/nickel").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/nickel/thermal-side").setTextureLocation("metals/nickel/thermal-top", Axis.Y).build();
        }
    },

    OBSIDIAN {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("obsidian", Blocks.obsidian.getDefaultState(), -1);
            factory.newBlock(Material.rock, "obsidian", provider).newVariation("blocks").next("chiseled").setTextureLocation("obsidian/chiseled-side").setTextureLocation("obsidian/chiseled-top", Axis.Y).next("chunks").next("crate-bottom").next("crate").setTextureLocation("obsidian/crate-side").setTextureLocation("obsidian/crate-top", Axis.Y).next("crystal").next("greek").setTextureLocation("obsidian/greek-side").setTextureLocation("obsidian/greek-top", Axis.Y).next("growth").next("map-a").next("map-b").next("panel-light").next("panel-shiny").next("panel").next("pillar-quartz").setTextureLocation("obsidian/pillar-quartz-side").setTextureLocation("obsidian/pillar-quartz-top", Axis.Y).next("pillar").setTextureLocation("obsidian/pillar-side").setTextureLocation("obsidian/pillar-top", Axis.Y).next("tiles").build();
        }
    },

    PAPER {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "paper", provider).newVariation("box").next("cross").next("door").next("floral").next("horizontal").next("plain").next("sixSections").next("throughMiddle").next("vertical").build();
        }
    },

//    PARTICLES {
//
//        @Override
//        void addBlocks(ChiselBlockFactory factory) {
//            factory.newBlock(Material.rock, "particles", provider).setParentFolder("grimstone").newVariation("star").build();
//        }
//    },

    PLANKS_ACACIA {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "planks-acacia", provider).newVariation("blinds").next("chaotic-hor").next("chaotic").next("clean").next("crate-fancy").next("crate").next("crateex").next("double").setTextureLocation("planks-acacia/double-side").setTextureLocation("planks-acacia/double-top", Axis.Y).next("fancy").next("large").next("panel-nails").next("parquet").next("short").next("vertical-uneven").next("vertical").build();
        }
    },

    PLANKS_BIRCH {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "planks-birch", provider).newVariation("blinds").next("chaotic-hor").next("chaotic").next("clean").next("crate-fancy").next("crate").next("crateex").next("double").setTextureLocation("planks-birch/double-side").setTextureLocation("planks-birch/double-top", Axis.Y).next("fancy").next("large").next("panel-nails").next("parquet").next("short").next("vertical-uneven").next("vertical").build();
        }
    },

    PLANKS_DARK_OAK {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "planks-dark-oak", provider).newVariation("blinds").next("chaotic-hor").next("chaotic").next("clean").next("crate-fancy").next("crate").next("crateex").next("double").setTextureLocation("planks-dark-oak/double-side").setTextureLocation("planks-dark-oak/double-top", Axis.Y).next("fancy").next("large").next("panel-nails").next("parquet").next("short").next("vertical-uneven").next("vertical").build();
        }
    },

    PLANKS_JUNGLE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "planks-jungle", provider).newVariation("blinds").next("chaotic-hor").next("chaotic").next("clean").next("crate-fancy").next("crate").next("crateex").next("double").setTextureLocation("planks-jungle/double-side").setTextureLocation("planks-jungle/double-top", Axis.Y).next("fancy").next("large").next("panel-nails").next("parquet").next("short").next("vertical-uneven").next("vertical").build();
        }
    },

    PLANKS_OAK {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "planks-oak", provider).newVariation("blinds").next("chaotic-hor").next("chaotic").next("clean").next("crate-fancy").next("crate").next("crateex").next("double").setTextureLocation("planks-oak/double-side").setTextureLocation("planks-oak/double-top", Axis.Y).next("fancy").next("large").next("panel-nails").next("parquet").next("short").next("vertical-uneven").next("vertical").build();
        }
    },

    PLANKS_SPRUCE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "planks-spruce", provider).newVariation("blinds").next("chaotic-hor").next("chaotic").next("clean").next("crate-fancy").next("crate").next("crateex").next("double").setTextureLocation("planks-spruce/double-side").setTextureLocation("planks-spruce/double-top", Axis.Y).next("fancy").next("large").next("panel-nails").next("parquet").next("short").next("vertical-uneven").next("vertical").build();
        }
    },

    PLATINUM {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "platinum", provider).setParentFolder("metals/platinum").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/platinum/thermal-side").setTextureLocation("metals/platinum/thermal-top", Axis.Y).build();
        }
    },

    PRESENT {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "present", provider).newVariation("presentChest0").next("presentChest1").next("presentChest10").next("presentChest11").next("presentChest12").next("presentChest13").next("presentChest14").next("presentChest15").next("presentChest2").next("presentChest3").next("presentChest4").next("presentChest5").next("presentChest6").next("presentChest7").next("presentChest8").next("presentChest9").next("red").build();
        }
    },

    PRISMARINE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "prismarine", provider).newVariation("masonryPrismarine").next("masonryPrismarineAnim").next("prismarineBrick").next("prismarineCircular").next("prismarine_bricks").next("prismarine_dark").next("prismarine_rough").build();
        }
    },

    PUMPKIN {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("pumpkin", Blocks.pumpkin.getDefaultState(), -1);
            factory.newBlock(Material.rock, "pumpkin", provider).newVariation("pumpkin_face_10_off").next("pumpkin_face_10_on").next("pumpkin_face_11_off").next("pumpkin_face_11_on").next("pumpkin_face_12_off").next("pumpkin_face_12_on").next("pumpkin_face_13_off").next("pumpkin_face_13_on").next("pumpkin_face_14_off").next("pumpkin_face_14_on").next("pumpkin_face_15_off").next("pumpkin_face_15_on").next("pumpkin_face_16_off").next("pumpkin_face_16_on").next("pumpkin_face_17_off").next("pumpkin_face_17_on").next("pumpkin_face_1_off").next("pumpkin_face_1_on").next("pumpkin_face_2_off").next("pumpkin_face_2_on").next("pumpkin_face_3_off").next("pumpkin_face_3_on").next("pumpkin_face_4_off").next("pumpkin_face_4_on").next("pumpkin_face_5_off").next("pumpkin_face_5_on").next("pumpkin_face_6_off").next("pumpkin_face_6_on").next("pumpkin_face_7_off").next("pumpkin_face_7_on").next("pumpkin_face_8_off").next("pumpkin_face_8_on").next("pumpkin_face_9_off").next("pumpkin_face_9_on").next("pumpkin_face_off").next("pumpkin_face_on").next("pumpkin_side").next("pumpkin_top").build();
        }
    },

    QUARTZ {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "quartz", provider).newVariation("masonryQuartz").next("quartzChiseled").next("quartzPrismatic").next("quartzPrismaticPattern").build();
        }
    },

    REDSTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("redstone", Blocks.redstone_block.getDefaultState(), -1);
            factory.newBlock(Material.rock, "redstone", provider).newVariation("a1-blockredstone-redstonechunk").next("a1-blockredstone-redstonezelda").next("a1-blockredstone-skullred").next("block").next("blocks").next("bricks").next("chiseled").setTextureLocation("redstone/chiseled-side").setTextureLocation("redstone/chiseled-top", Axis.Y).next("circuit").next("ere").next("masonryRedstone").next("ornate-tiles").next("pillar").setTextureLocation("redstone/pillar-side").setTextureLocation("redstone/pillar-top", Axis.Y).next("smallbricks").next("smallchaotic").next("smooth").next("solid").next("supaplex").next("tiles").build();
        }
    },

    REDSTONE_LAMP {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("redstoneLamp", Blocks.redstone_lamp.getDefaultState(), -1);
            factory.newBlock(Material.rock, "redstoneLamp", provider).newVariation("redstone_lamp_off").next("redstone_lamp_on").next("square-off").next("square-on").build();
        }
    },

    ROOFING {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "roofing", provider).newVariation("shingles").build();
        }
    },

    SANDSNAKE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "sandsnake", provider).setGroup("sandstone").setParentFolder("snakestone/sandsnake")
            .newVariation("bot-tip").next("bot").next("crosssection").next("face-left").next("face-right").next("face").next("left-down").next("left-tip")
            .next("left-up").next("right-down").next("right-tip").next("right-up").next("side").next("top-tip").next("top").build();
        }
    },

    SANDSTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState ss = Blocks.sandstone.getDefaultState();
            IProperty<BlockSandStone.EnumType> prop = BlockSandStone.TYPE;
            Carving.chisel.addVariation("sandstone", ss.withProperty(prop, BlockSandStone.EnumType.DEFAULT), -3);
            Carving.chisel.addVariation("sandstone", ss.withProperty(prop, BlockSandStone.EnumType.SMOOTH), -2);
            Carving.chisel.addVariation("sandstone", ss.withProperty(prop, BlockSandStone.EnumType.CHISELED), -1);
            
            factory.newBlock(Material.rock, "sandstone", provider).newVariation("a0-sandstonepreview-boxcreeper-bottom")
            .next("a0-sandstonepreview-boxcreeper").setTextureLocation("sandstone/a0-sandstonepreview-boxcreeper-side").setTextureLocation("sandstone/a0-sandstonepreview-boxcreeper-top", Axis.Y)
            .next("base").setTextureLocation("sandstone/base-side").setTextureLocation("sandstone/base-top", Axis.Y)
            .next("block").next("blocks").next("capstone").setTextureLocation("sandstone/capstone-side").setTextureLocation("sandstone/capstone-top", Axis.Y)
            .next("column").setTextureLocation("sandstone/column-side").setTextureLocation("sandstone/column-top", Axis.Y).next("faded-bottom")
            .next("faded").setTextureLocation("sandstone/faded-side").setTextureLocation("sandstone/faded-top", Axis.Y)
            .next("horizontal-tiles").setTextureLocation("sandstone/horizontal-tiles-side").setTextureLocation("sandstone/horizontal-tiles-top", Axis.Y)
            .next("mosaic").next("small").setTextureLocation("sandstone/small-side").setTextureLocation("sandstone/small-top", Axis.Y)
            .next("smooth-base").setTextureLocation("sandstone/smooth-base-side").setTextureLocation("sandstone/smooth-base-top", Axis.Y)
            .next("smooth-cap").setTextureLocation("sandstone/smooth-cap-side").setTextureLocation("sandstone/smooth-cap-top", Axis.Y)
            .next("smooth-small").setTextureLocation("sandstone/smooth-small-side").setTextureLocation("sandstone/smooth-small-top", Axis.Y)
            .next("smooth").next("terrain-sandstone-smoothglyph-bottom")
            .next("terrain-sandstone-smoothglyph").setTextureLocation("sandstone/terrain-sandstone-smoothglyph-side").setTextureLocation("sandstone/terrain-sandstone-smoothglyph-top", Axis.Y)
            .next("terrain-sandstone-solidcobble-bottom")
            .next("terrain-sandstone-solidcobble").setTextureLocation("sandstone/terrain-sandstone-solidcobble-side").setTextureLocation("sandstone/terrain-sandstone-solidcobble-top", Axis.Y)
            .next("a0-sandstonepreview-smoothflat-bottom")
            .next("a0-sandstonepreview-smoothflat").setTextureLocation("sandstone2/a0-sandstonepreview-smoothflat-side").setTextureLocation("sandstone2/a0-sandstonepreview-smoothflat-top", Axis.Y)
            .next("terrain-sandstone-brickflat-bottom")
            .next("terrain-sandstone-brickflat").setTextureLocation("sandstone2/terrain-sandstone-brickflat-side").setTextureLocation("sandstone2/terrain-sandstone-brickflat-top", Axis.Y)
            .build();
        }
    },

    SANDSTONE_SCRIBBLES {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "sandstone-scribbles", provider).setGroup("sandstone")
            .newVariation("scribbles-0").setTextureLocation("sandstone-scribbles/scribbles-0-side").setTextureLocation("sandstone-scribbles/scribbles-0-top", Axis.Y)
            .next("scribbles-1").setTextureLocation("sandstone-scribbles/scribbles-1-side").setTextureLocation("sandstone-scribbles/scribbles-1-top", Axis.Y)
            .next("scribbles-10").setTextureLocation("sandstone-scribbles/scribbles-10-side").setTextureLocation("sandstone-scribbles/scribbles-10-top", Axis.Y)
            .next("scribbles-11").setTextureLocation("sandstone-scribbles/scribbles-11-side").setTextureLocation("sandstone-scribbles/scribbles-11-top", Axis.Y)
            .next("scribbles-12").setTextureLocation("sandstone-scribbles/scribbles-12-side").setTextureLocation("sandstone-scribbles/scribbles-12-top", Axis.Y)
            .next("scribbles-13").setTextureLocation("sandstone-scribbles/scribbles-13-side").setTextureLocation("sandstone-scribbles/scribbles-13-top", Axis.Y)
            .next("scribbles-14").setTextureLocation("sandstone-scribbles/scribbles-14-side").setTextureLocation("sandstone-scribbles/scribbles-14-top", Axis.Y)
            .next("scribbles-15").setTextureLocation("sandstone-scribbles/scribbles-15-side").setTextureLocation("sandstone-scribbles/scribbles-15-top", Axis.Y)
            .next("scribbles-2").setTextureLocation("sandstone-scribbles/scribbles-2-side").setTextureLocation("sandstone-scribbles/scribbles-2-top", Axis.Y)
            .next("scribbles-3").setTextureLocation("sandstone-scribbles/scribbles-3-side").setTextureLocation("sandstone-scribbles/scribbles-3-top", Axis.Y)
            .next("scribbles-4").setTextureLocation("sandstone-scribbles/scribbles-4-side").setTextureLocation("sandstone-scribbles/scribbles-4-top", Axis.Y)
            .next("scribbles-5").setTextureLocation("sandstone-scribbles/scribbles-5-side").setTextureLocation("sandstone-scribbles/scribbles-5-top", Axis.Y)
            .next("scribbles-6").setTextureLocation("sandstone-scribbles/scribbles-6-side").setTextureLocation("sandstone-scribbles/scribbles-6-top", Axis.Y)
            .next("scribbles-7").setTextureLocation("sandstone-scribbles/scribbles-7-side").setTextureLocation("sandstone-scribbles/scribbles-7-top", Axis.Y)
            .next("scribbles-8").setTextureLocation("sandstone-scribbles/scribbles-8-side").setTextureLocation("sandstone-scribbles/scribbles-8-top", Axis.Y)
            .next("scribbles-9").setTextureLocation("sandstone-scribbles/scribbles-9-side").setTextureLocation("sandstone-scribbles/scribbles-9-top", Axis.Y)
            .build();
        }
    },

    SHINGLES {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "shingles", provider).newVariation("Shingle1").next("Shingle2").next("Shingle3").next("Shingle4").next("Shingle5").next("Shingle6").build();
        }
    },

    SILVER {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "silver", provider).setParentFolder("metals/silver").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/silver/thermal-side").setTextureLocation("metals/silver/thermal-top", Axis.Y).build();
        }
    },

    SNAKE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "snake", provider).setParentFolder("snakestone/snake").newVariation("bot-tip").next("bot").next("crosssection").next("face-left").next("face-right").next("face").next("left-down").next("left-tip").next("left-up").next("right-down").next("right-tip").next("right-up").next("side").next("top-tip").next("top").build();
        }
    },

    STEEL {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "steel", provider).setParentFolder("metals/steel").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/steel/thermal-side").setTextureLocation("metals/steel/thermal-top", Axis.Y).build();
        }
    },

    STONEBRICK {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("stonebrick", Blocks.stonebrick.getDefaultState(), -1);
            
            factory.newBlock(Material.rock, "stonebrick", provider).newVariation("chaotic").next("chaoticbricks").next("fancy").next("fullsmooth")
                .next("largebricks").next("largeornate").next("ornate").next("ornatepanel").next("panel-hard").next("poison").next("roughbricks")
                .next("smallbricks").next("smallchaotic").next("sunken").next("masonBricksFelsic").next("masonBricksMafic").next("masonBricksMixed")
                .next("masonBricksPlain").next("masonry2Blue").next("masonry2Both").next("masonry2Neutral").next("masonry2Red").next("masonryFelsic")
                .next("masonryMafic").next("masonryMixed").next("masonryPlain").build();
        }
    },

    TALLOW {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "tallow", provider).newVariation("faces").next("smooth").next("tallowblock").next("tallowblock_top").build();
        }
    },

    TECHNICAL {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "technical", provider).newVariation("cables").next("cautiontape").next("fanFast").setTextureLocation("technical/fanFast-side").setTextureLocation("technical/fanFast-top", Axis.Y).next("fanFastTransparent").setTextureLocation("technical/fanFastTransparent-side").setTextureLocation("technical/fanFastTransparent-top", Axis.Y).next("fanMalfunction").setTextureLocation("technical/fanMalfunction-side").setTextureLocation("technical/fanMalfunction-top", Axis.Y).next("fanStill").setTextureLocation("technical/fanStill-side").setTextureLocation("technical/fanStill-top", Axis.Y).next("fanStillTransparent").setTextureLocation("technical/fanStillTransparent-side").setTextureLocation("technical/fanStillTransparent-top", Axis.Y).next("grate").next("grateRusty").next("hexArmorPlating").next("industrialrelic").next("insulationv2").next("malfunctionFan").setTextureLocation("technical/malfunctionFan-side").setTextureLocation("technical/malfunctionFan-top", Axis.Y).next("massiveFan").next("massiveHexPlating").next("old").next("pipesLarge").next("pipesSmall").next("rustyBoltedPlates").next("rustyCover").next("scaffold").next("scaffoldTransparent").next("spinningStuffAnim").next("vent").next("ventGlowing").next("wallPads").next("wires").build();
        }
    },

    TEMPLE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "temple", provider).newVariation("bricks-disarray").next("bricks-large").next("bricks-weared").next("bricks").next("cobble").next("column").setTextureLocation("temple/column-side").setTextureLocation("temple/column-top", Axis.Y).next("ornate").next("plate-cracked").next("plate").next("smalltiles-light").next("smalltiles").next("stand-creeper").setTextureLocation("temple/stand-creeper-side").setTextureLocation("temple/stand-creeper-top", Axis.Y).next("stand-mosaic").setTextureLocation("temple/stand-mosaic-side").setTextureLocation("temple/stand-mosaic-top", Axis.Y).next("stand").setTextureLocation("temple/stand-side").setTextureLocation("temple/stand-top", Axis.Y).next("temple").setTextureLocation("temple/temple-side").setTextureLocation("temple/temple-top", Axis.Y).next("tiles-light").next("tiles").build();
        }
    },

    TEMPLEMOSSY {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "templemossy", provider).newVariation("bricks-disarray").next("bricks-large").next("bricks-weared").next("bricks").next("cobble").next("column").setTextureLocation("templemossy/column-side").setTextureLocation("templemossy/column-top", Axis.Y).next("ornate").next("plate-cracked").next("plate").next("smalltiles-light").next("smalltiles").next("stand-creeper").setTextureLocation("templemossy/stand-creeper-side").setTextureLocation("templemossy/stand-creeper-top", Axis.Y).next("stand-mosaic").setTextureLocation("templemossy/stand-mosaic-side").setTextureLocation("templemossy/stand-mosaic-top", Axis.Y).next("stand").setTextureLocation("templemossy/stand-side").setTextureLocation("templemossy/stand-top", Axis.Y).next("temple").setTextureLocation("templemossy/temple-side").setTextureLocation("templemossy/temple-top", Axis.Y).next("tiles-light").next("tiles").build();
        }
    },

    TERRASTEEL {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "terrasteel", provider).newVariation("adv").next("bolted").next("card").next("caution").next("crate").next("egregious").next("elementiumEye-0-0").next("elementiumEye-0-1").next("elementiumEye-1-0").next("elementiumEye-1-1").next("elementiumEye-2-0").next("elementiumEye-2-1").next("thermal").build();
        }
    },

    THAUMIUM {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "thaumium", provider).newVariation("bevel").next("chunks").next("lattice").next("ornate").next("planks").next("purplerunes").next("runes").next("small").next("thaumDiagonalBricks-0").next("thaumDiagonalBricks-1").next("thaumDiagonalBricks-2").next("thaumDiagonalBricks-3").next("thaumDiagonalBricks").next("thaumicEyeSegment-0-0").next("thaumicEyeSegment-0-1").next("thaumicEyeSegment-1-0").next("thaumicEyeSegment-1-1").next("thaumicEyeSegment-2-0").next("thaumicEyeSegment-2-1").next("thaumicEyeSegment").next("thaumiumBigBricks").next("thaumiumblock").next("totem").build();
        }
    },

    THIN_WOOD_ACACIA {

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
    },

    TIN {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "tin", provider).setParentFolder("metals/tin").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/tin/thermal-side").setTextureLocation("metals/tin/thermal-top", Axis.Y).build();
        }
    },

    TORCH {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "torch", provider).newVariation("torch1").next("torch10").next("torch2").next("torch3").next("torch4").next("torch5").next("torch6").next("torch7").next("torch8").next("torch9").build();
        }
    },

    TYRIAN {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "tyrian", provider).newVariation("black").next("black2").next("blueplating").next("chaotic").next("dent").next("diagonal").next("elaborate").next("opening").next("plate").next("plate-raw").next("platetiles").next("platform").next("routes").next("rust").next("shining").next("softplate").next("tyrian").build();
        }
    },

    URANIUM {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "uranium", provider).setParentFolder("metals/uranium").newVariation("badGreggy").next("bolted").next("caution").next("crate").next("machine").next("scaffold").next("thermal-bottom").next("thermal").setTextureLocation("metals/uranium/thermal-side").setTextureLocation("metals/uranium/thermal-top", Axis.Y).build();
        }
    },

    VALENTINES {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "valentines", provider).newVariation("1").next("2").next("3").next("4").next("5").next("6").next("7").next("8").next("9").next("companion").build();
        }
    },

    VOIDSTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "voidstone", provider)
                .newVariation("bevel").next("eye").next("metalborder").next("quarters")
                .next("raw").next("rune").next("skulls").next("smooth").build();
                
            factory.newBlock(Material.rock, "energizedVoidstone", provider)
                .setParentFolder("voidstone/animated")
                .newVariation("bevel").next("eye").next("metalborder").next("quarters")
                .next("raw").next("rune").next("skulls").next("smooth").build();
                
            factory.newBlock(Material.rock, "voidstoneRunic", provider)
                .setParentFolder("voidstone/runes")
                .newVariation("black").next("blue").next("brown").next("cyan").next("gray")
                .next("green").next("lightblue").next("lightgray").next("lime").next("magenta")
                .next("orange").next("pink").next("purple").next("red").next("yellow").build();
        }
    },

    WARNING {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "warningSign", provider).setParentFolder("warning").newVariation("acid").next("bio").next("cryogenic").next("death").next("explosion").next("fall").next("falling").next("fire").next("generic").next("illuminati").next("noentry").next("oxygen").next("rad").next("sound").next("underconstruction").next("voltage").build();
        }
    },

    WATERSTONE {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "waterstone", provider).newVariation("black").next("chaotic").next("cobble").next("creeper").next("panel").next("panel-ornate").next("tiles").next("water_flow").build();
        }
    },

    WOOLEN_CLAY {

        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.rock, "woolenClay", provider).newVariation("black").next("blue").next("brown").next("cyan").next("gray").next("green").next("lightblue").next("lightgray").next("lime").next("magenta").next("orange").next("pink").next("purple").next("red").next("white").next("yellow").build();
        }
    },
    ;
    
    //@formatter:on

    private static final String[] dyeOres = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow",
            "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };

    private static final BlockCreator<BlockCarvable> creator = BlockCarvable::new;
    private static final ChiselBlockProvider<BlockCarvable> provider = new ChiselBlockProvider<>(creator, BlockCarvable.class);
    
    @RequiredArgsConstructor
    private static class ChiselBlockProvider<T extends BlockCarvable> implements BlockProvider<T> {

        private final BlockCreator<T> creator;
        @Getter
        private final Class<T> blockClass;
        @Getter
        private final Class<? extends ItemBlock> itemClass = ItemChiselBlock.class;

        @Override
        public T createBlock(Material mat, int index, int maxVariation, VariationData... data) {
            return creator.createBlock(mat, index, maxVariation, data);
        }
    };

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