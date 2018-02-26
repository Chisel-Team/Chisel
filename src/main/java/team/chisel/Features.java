package team.chisel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import team.chisel.api.block.BlockCreator;
import team.chisel.api.block.BlockProvider;
import team.chisel.api.block.ChiselBlockFactory;
import team.chisel.api.block.ICarvable;
import team.chisel.api.block.VariationData;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.client.handler.BlockSpeedHandler;
import team.chisel.client.sound.ChiselSoundTypes;
import team.chisel.common.block.BlockAutoChisel;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.BlockCarvableCarpet;
import team.chisel.common.block.ItemChiselBlock;
import team.chisel.common.block.TileAutoChisel;
import team.chisel.common.carving.Carving;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselBlocks;
import team.chisel.common.init.ChiselItems;
import team.chisel.common.init.ChiselSounds;
import team.chisel.common.item.ItemChisel;
import team.chisel.common.item.ItemChisel.ChiselType;
import team.chisel.common.item.ItemOffsetTool;
import team.chisel.common.util.GenerationHandler;
import team.chisel.common.util.GenerationHandler.WorldGenInfo;

@EventBusSubscriber
public enum Features {

    // @formatter:off
    ALUMINUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "blockAluminum", null, beaconBaseProvider)
                    .setParentFolder("metals/aluminum")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockAluminum")
                    .build(b -> b.setSoundType(SoundType.METAL).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockAluminum");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Aluminum");
        }
    },

    ANDESITE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stone = Blocks.STONE.getDefaultState();
            IProperty<BlockStone.EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("andesite", CarvingUtils.variationFor(stone.withProperty(prop, BlockStone.EnumType.ANDESITE), -21));
            Carving.chisel.addVariation("andesite", CarvingUtils.variationFor(stone.withProperty(prop, BlockStone.EnumType.ANDESITE_SMOOTH), -20));


            factory.newBlock(Material.ROCK, "andesite", provider)
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .addOreDict("stoneAndesite")
                    .addOreDict("stoneAndesitePolished")
                    .build(b -> b.setHardness(1.5F).setResistance(30.0F).setSoundType(SoundType.STONE));
        }
    },

    ANTIBLOCK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            /*BlockCreator<BlockCarvable> antiblockCreator = (mat, index, maxVariation, data) -> new BlockCarvable(mat, index, maxVariation, data) {
                @Override
                public int getPackedLightmapCoords(IBlockState state, IBlockAccess access, BlockPos pos) {
                    return 0xF000F0;
                }
            };*/

            factory.newBlock(Material.ROCK, "antiblock", provider) /*new ChiselBlockProvider<>(antiblockCreator, BlockCarvable.class)*/
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
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.antiblock, 8, 15), "SSS", "SGS", "SSS", 'S', "stone", 'G', "dustGlowstone");
        }
    },
    
    AUTOCHISEL {
        
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            String name = "auto_chisel";
            factory.getRegistry().register(new BlockAutoChisel().setRegistryName(name));
            GameRegistry.registerTileEntity(TileAutoChisel.class, Chisel.MOD_ID + ":" + name);
        }
        
        @Override
        void addItems(IForgeRegistry<Item> registry) {
            registry.register(new ItemBlock(ChiselBlocks.auto_chisel).setRegistryName(ChiselBlocks.auto_chisel.getRegistryName()));
        }
    },

    BASALT {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "basalt", provider) // REMAP!
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("raw").setOrder(-100)
                    .addOreDict("stoneBasalt")
                    .addOreDict("stoneBasaltPolished")
                    .build(b -> b.setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE));

        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry) {
            if (!Configurations.basaltSpecialGen) {
                GenerationHandler.INSTANCE.addGeneration(ChiselBlocks.basalt2.getDefaultState().withProperty(ChiselBlocks.basalt2.getMetaProp(), 7),
                        new WorldGenInfo(Configurations.basaltVeinAmount, 0, 32, 1, BlockMatcher.forBlock(Blocks.STONE)));
            }
        }
    },

    BLOOD_MAGIC("bloodmagic") {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "bloodMagic", new ChiselBlockProvider<>(BlockCarvable::new, BlockCarvable.class))
                    .newVariation("bloodRuneArranged")
                    .next("bloodRuneBricks")
                    .next("bloodRuneCarved")
                    .next("bloodRuneCarvedRadial")
                    .next("bloodRuneClassicPanel")
                    .next("bloodRuneTiles")
                    .build();
        }
    },

    BOOKSHELF {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            String[] woodTypes = new String[]{"Oak", "Spruce", "Birch", "Jungle", "Acacia", "DarkOak"};

            Carving.chisel.addVariation("bookshelf_oak", CarvingUtils.variationFor(Blocks.BOOKSHELF.getDefaultState(), -1));

            BlockCreator<BlockCarvable> bookshelfCreator = (mat, index, maxVariation, data) -> new BlockCarvable(mat, index, maxVariation, data) {
                @Override
                public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
                    Blocks.BOOKSHELF.getDrops(drops, world, pos, state, fortune);
                }

                @Override
                public float getEnchantPowerBonus(World world, BlockPos pos){
                    return 1;
                }
            };

            for (String woodType : woodTypes) {
                factory.newBlock(Material.WOOD, "bookshelf_" + woodType.toLowerCase(), new ChiselBlockProvider<>(bookshelfCreator, BlockCarvable.class))
                        .newVariation("rainbow")
                        .next("necromancer-novice")
                        .next("necromancer")
                        .next("redtomes")
                        .next("abandoned")
                        .next("hoarder")
                        .next("brim")
                        .next("historician")
                        .next("cans")
                        .next("papers")
                        .addOreDict("bookshelf")
                        .addOreDict("bookshelf" + woodType)
                        .build(b -> b.setSoundType(SoundType.WOOD).setHardness(1.5f));

            }
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            BlockCarvable[] bookshelves = new BlockCarvable[]{ChiselBlocks.bookshelf_spruce, ChiselBlocks.bookshelf_birch, ChiselBlocks.bookshelf_jungle, ChiselBlocks.bookshelf_acacia, ChiselBlocks.bookshelf_darkoak};
            Block[] stairs = new Block[]{Blocks.SPRUCE_STAIRS, Blocks.BIRCH_STAIRS, Blocks.JUNGLE_STAIRS, Blocks.ACACIA_STAIRS, Blocks.DARK_OAK_STAIRS};

            for (int c = 0; c < bookshelves.length; c++)
            {
                Features.addShapedRecipe(registry, "bookshelf" + c, new ItemStack(bookshelves[c], 1), "S S", "BBB", "S S", 'S', new ItemStack(stairs[c], 1), 'B', new ItemStack(Items.BOOK, 1));
            }
        }
    },

    BRICKS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("bricks", CarvingUtils.variationFor(Blocks.BRICK_BLOCK.getDefaultState(), -1));

            factory.newBlock(Material.ROCK, "bricks", provider) // REMAP!
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    //.next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .build(b -> b.setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE));
        }
    },

    BRONZE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.IRON, "blockBronze", null, beaconBaseProvider)
                    .setParentFolder("metals/bronze")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockBronze")
                    .build(b -> b.setSoundType(SoundType.METAL).setHardness(5.0F).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockBronze");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Bronze");
        }
    },

    BROWNSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "brownstone", new ChiselBlockProvider<>(creator, BlockCarvable.class))
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
                    .build(b -> { 
                        b.setSoundType(SoundType.STONE).setHardness(1.0F);
                        BlockSpeedHandler.speedupBlocks.add(b);
                    });
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            //FurnaceRecipes.instance().addSmelting(new ItemStack(Blocks.GRAVEL).getItem(), new ItemStack(ChiselBlocks.brownstone), 0.1F);

            addShapedRecipe(registry, new ItemStack(ChiselBlocks.brownstone, 4, 0), " S ", "SCS", " S ", 'S', "sandstone", 'C', new ItemStack(Items.CLAY_BALL, 1));
        }
    },

    /*CERTUS { TODO Retexture
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

    CARPET {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            IBlockState carpet = Blocks.CARPET.getDefaultState();
            IProperty<EnumDyeColor> prop = BlockCarpet.COLOR;

            for(int c = 0; c < dyeColors.length; c++)
            {
                Carving.chisel.addVariation("carpet_" + (dyeColors[c].toLowerCase()), CarvingUtils.variationFor(carpet.withProperty(prop, EnumDyeColor.byDyeDamage(c)), -1));


                factory.newBlock(Material.CARPET, "carpet_" + (dyeColors[c].toLowerCase()), new ChiselBlockProvider<>(BlockCarvableCarpet::new, BlockCarvableCarpet.class)).opaque(false)
                        .setParentFolder("carpet")
                        .newVariation("legacy_"+(dyeColors[c].toLowerCase()))
                        .next("llama_"+(dyeColors[c].toLowerCase()))
                        .build(b -> b.setSoundType(SoundType.CLOTH).setHardness(0.1F).setLightOpacity(0));
            }
        }
    },

    CHARCOAL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "block_charcoal", null, provider)
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("raw")
                    .addOreDict("blockCharcoal")
                    .build(b -> b.setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.STONE));
            
            CarvingUtils.addOreGroup("blockCharcoal");

        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            Features.addShapedRecipe(registry, "charcoal_uncraft", new ItemStack(Items.COAL, 9, 1), "X", 'X', "blockCharcoal");
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.block_charcoal2, 1, 1), "XXX", "XXX", "XXX", 'X', "charcoal");
        }
    },
    
    CHISEL {
        @Override
        void addItems(IForgeRegistry<Item> registry) {
            registry.register(new ItemChisel(ChiselType.IRON));
            registry.register(new ItemChisel(ChiselType.DIAMOND));
            registry.register(new ItemChisel(ChiselType.HITECH));

            registry.register(new ItemOffsetTool());
        }
        
        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry) {
            if (!Configurations.chiselRecipe) {
                Features.addShapedRecipe(registry, "chisel_iron", ChiselItems.chisel_iron, " x", "s ", 'x', "ingotIron", 's', "stickWood");
                Features.addShapedRecipe(registry, "chisel_diamond", ChiselItems.chisel_diamond, " x", "s ", 'x', "gemDiamond", 's', "stickWood");
            } else {
                Features.addShapedRecipe(registry, "chisel_iron", ChiselItems.chisel_iron, " xx", " xx", "s  ", 'x', "ingotIron", 's', "stickWood");
                Features.addShapedRecipe(registry, "chisel_diamond", ChiselItems.chisel_diamond, " xx", " xx", "s  ", 'x', "gemDiamond", 's', "stickWood");
            }
            Features.addShapelessRecipe(registry, "chisel_hitech", ChiselItems.chisel_hitech, ChiselItems.chisel_diamond, "dustRedstone", "ingotGold");

            Features.addShapedRecipe(registry, "offsettool", ChiselItems.offsettool, "-o", "|-", 'o', Items.ENDER_PEARL, '|', "stickWood", '-', "ingotIron");
        }
    },

    CLOUD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.CLOTH, "cloud", provider).opaque(false)
                    .newVariation("cloud")
                    .next("large")
                    .next("small")
                    .next("vertical")
                    .next("grid")
                    .build(b -> b.setSoundType(SoundType.CLOTH).setHardness(0.3F));
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.cloud, 32, 0), " S ", "S S", " S ", 'S', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
        }
    },

    COBALT {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.IRON, "blockCobalt", null, beaconBaseProvider)
                    .setParentFolder("metals/cobalt")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockCobalt")
                    .build(b -> b.setSoundType(SoundType.METAL).setHardness(5.0F).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockCobalt");
            
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Cobalt");
        }
    },

    COBBLESTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("cobblestone", CarvingUtils.variationFor(Blocks.COBBLESTONE.getDefaultState(), -20));

            factory.newBlock(Material.ROCK, "cobblestone", provider) // REMAP!
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("emboss")
                    .next("indent")
                    .next("marker")
                    .addOreDict("cobblestone")
                    .build(b -> b.setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE));
        }
    },

    COAL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "block_coal", null, provider)
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("raw")
                    .addOreDict("blockCoal")
                    .build(b -> b.setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.STONE));
            
            CarvingUtils.addOreGroup("blockCoal");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(Items.COAL, 9), "X", 'X', "blockCoal");
        }
    },

    COAL_COKE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "block_coal_coke", provider)
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("raw")
                    .addOreDict("blockFuelCoke")
                    .addOreDict("blockCoalCoke")
                    .build(b -> b.setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.STONE));

            CarvingUtils.addOreGroup("blockCoalCoke");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerOreUncraftRecipe(registry, "blockFuelCoke", "fuelCoke");
        }
    },

    COBBLESTONEMOSSY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("cobblestonemossy", CarvingUtils.variationFor(Blocks.MOSSY_COBBLESTONE.getDefaultState(), -1));

            factory.newBlock(Material.ROCK, "cobblestonemossy", provider)
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .addOreDict("blockMossy")
                    .build(b -> b.setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE));
        }
    },

    CONCRETE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            for (int i = 0; i < dyeColors.length; i++) {
                Carving.chisel.addVariation("concrete_" + (dyeColors[i].toLowerCase()), 
                        CarvingUtils.variationFor(Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.byDyeDamage(i)), -1));

                factory.newBlock(Material.ROCK, "concrete_" + (dyeColors[i].toLowerCase()), provider)
                        .setParentFolder("concrete_" + dyeColors[i].toLowerCase())
                        .newVariation("cracked")
                        .next("bricks-soft")
                        .next("bricks-cracked")
                        .next("bricks-triple")
                        .next("bricks-encased")
                        .next("braid")
                        .next("array")
                        .next("tiles-large")
                        .next("tiles-small")
                        .next("chaotic-medium")
                        .next("chaotic-small")
                        .next("dent")
                        .next("french-1")
                        .next("french-2")
                        .next("jellybean")
                        .next("layers")
                        .next("mosaic")
                        .next("ornate")
                        .next("panel")
                        .next("road")
                        .next("slanted")
                        .next("zag")
                        .next("circularct")
                        .next("weaver")
                        .next("bricks-solid")
                        .next("bricks-small")
                        .next("circular")
                        .next("tiles-medium")
                        .next("pillar")
                        .next("twisted")
                        .next("prism")
                        .next("bricks-chaotic")
                        .next("cuts")
                        .addOreDict("blockConcrete")
                        .addOreDict("blockConcrete"+dyeColors[i])
                        .build(b -> {
                            b.setSoundType(SoundType.STONE).setHardness(1.5F);
                            BlockSpeedHandler.speedupBlocks.add(b);
                        });
            }
            
            BlockSpeedHandler.speedupBlocks.add(Blocks.CONCRETE);
        }
    },

    COPPER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.IRON, "blockCopper", null, beaconBaseProvider)
                    .setParentFolder("metals/copper")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockCopper")
                    .build(b -> b.setSoundType(SoundType.METAL).setHardness(5.0F).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockCopper");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Copper");
        }
    },

    /*CRAG_ROCK { TODO Retexture
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
    }, //  */

    DIAMOND {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.IRON, "diamond", null, beaconBaseProvider)
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
                    .addOreDict("blockDiamond")
                    .build(b -> b.setSoundType(SoundType.METAL).setHardness(5.0F).setHarvestLevel("pickaxe", 2));
            
            CarvingUtils.addOreGroup("blockDiamond");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(Items.DIAMOND, 9), "X", 'X', "blockDiamond");
        }
    },

    DIORITE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stone = Blocks.STONE.getDefaultState();
            IProperty<BlockStone.EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("diorite", CarvingUtils.variationFor(stone.withProperty(prop, BlockStone.EnumType.DIORITE), -21));
            Carving.chisel.addVariation("diorite", CarvingUtils.variationFor(stone.withProperty(prop, BlockStone.EnumType.DIORITE_SMOOTH), -20));


            factory.newBlock(Material.ROCK, "diorite", provider)
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .addOreDict("stoneDiorite")
                    .addOreDict("stoneDioritePolished")
                    .build(b -> b.setHardness(1.5F).setResistance(30.0F).setSoundType(SoundType.STONE));
        }
    },

    DIRT {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("dirt", CarvingUtils.variationFor(Blocks.DIRT.getDefaultState(), -1));
            Carving.chisel.setVariationSound("dirt", ChiselSounds.dirt_chisel);
            factory.newBlock(Material.GROUND, "dirt", new ChiselBlockProvider<>(BlockCarvable::new, BlockCarvable.class))
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
                    .addOreDict("dirt")
                    .build(b -> b.setSoundType(SoundType.GROUND).setHardness(0.5F).setHarvestLevel("shovel", 0));
        }
    },

    ELECTRUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.IRON, "blockElectrum", null, beaconBaseProvider)
                    .setParentFolder("metals/electrum")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockElectrum")
                    .build(b -> b.setSoundType(SoundType.METAL).setHardness(5.0F).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockElectrum");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Electrum");
        }
    },

    EMERALD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.IRON, "emerald", null, beaconBaseProvider)
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
                    .addOreDict("blockEmerald")
                    .build(b -> b.setSoundType(SoundType.METAL).setHardness(5.0F).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockEmerald");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(Items.EMERALD, 9), "X", 'X', "blockEmerald");
        }
    },

    END_PURPUR {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState purpur_pillar = Blocks.PURPUR_PILLAR.getDefaultState();
            IProperty<Axis> prop = BlockRotatedPillar.AXIS;

            Carving.chisel.addVariation("purpur", CarvingUtils.variationFor(Blocks.PURPUR_BLOCK.getDefaultState(), -5));
            //Carving.chisel.addVariation("purpur", CarvingUtils.variationFor(purpur_pillar.withProperty(prop, Axis.X), -4));
            Carving.chisel.addVariation("purpur", CarvingUtils.variationFor(purpur_pillar.withProperty(prop, Axis.Y), -3));
            //Carving.chisel.addVariation("purpur", CarvingUtils.variationFor(purpur_pillar.withProperty(prop, Axis.Z), -2));
            //Carving.chisel.addVariation("purpur", CarvingUtils.variationFor(Blocks.purpur_double_slab.getDefaultState(), -1));

            factory.newBlock(Material.ROCK, "purpur", provider) // REMAP!
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    //.next("tiles-medium")
                    //.next("pillar")
                    .next("twisted")
                    .next("prism")
                    .build(b -> b.setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE));
        }
    },

    /*ENDER_PEARL_BLOCK { TODO Retexture
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
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            GameRegistry.addRecipe(new ItemStack(ChiselBlocks.ender_pearl_block), "SS", "SS", 'S', new ItemStack(Items.ENDER_PEARL));
        }
    },*/

    ENDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("endstone", CarvingUtils.variationFor(Blocks.END_STONE.getDefaultState(), -21));
            Carving.chisel.addVariation("endstone", CarvingUtils.variationFor(Blocks.END_BRICKS.getDefaultState(), -20));


            factory.newBlock(Material.ROCK, "endstone", provider) // REMAP!
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .addOreDict("endstone")
                    .build(b -> b.setHardness(3.0F).setResistance(15.0F).setSoundType(SoundType.STONE));
        }
    },

    FACTORY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.IRON, "factory", provider)
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
                    .build(b -> b.setSoundType(ChiselSoundTypes.METAL));
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.factory, Configurations.factoryBlockAmount, 0), "SXS", "X X", "SXS",
                    'X', new ItemStack(Blocks.STONE, 1),
                    'S', new ItemStack(Items.IRON_INGOT, 1));
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
                    .build(b -> b.setSoundType(SoundType.METAL));
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.futura, 8, 0), "SSS", "SGS", "SSS", 'S', "stone", 'G', "dustRedstone");
        }
    },

    GLASS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("glass", CarvingUtils.variationFor(Blocks.GLASS.getDefaultState(), -20));


            BlockCreator<BlockCarvable> glassCreator = (mat, index, maxVariation, data) -> new BlockCarvable(mat, index, maxVariation, data) {
                @Override
                public int quantityDropped(Random random) {
                    return 0;
                }

                @Override
                protected boolean canSilkHarvest() {
                    return true;
                }
            };

            factory.newBlock(Material.GLASS, "glass", new ChiselBlockProvider<>(glassCreator, BlockCarvable.class)).opaque(false)
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
                    .addOreDict("blockGlass")
                    .addOreDict("blockGlassColorless")
                    .build(b -> b.setSoundType(SoundType.GLASS).setHardness(0.3F));
        }
    },

    GLASSDYED {
        @SuppressWarnings("null")
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stainedGlass = Blocks.STAINED_GLASS.getDefaultState();
            IProperty<EnumDyeColor> prop = BlockStainedGlass.COLOR;

            for(int c = 0; c < dyeColors.length; c++)
            {
                final int i = c;

                BlockCreator<BlockCarvable> glassCreator = (mat, index, maxVariation, data) -> new BlockCarvable(mat, index, maxVariation, data) {
                    float[] beaconFloats = EnumDyeColor.byDyeDamage(i).getColorComponentValues();

                    @Override
                    public int quantityDropped(Random random) {
                        return 0;
                    }

                    @Override
                    protected boolean canSilkHarvest() {
                        return true;
                    }

                    @Override
                    public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {
                        return beaconFloats;
                    }
                };

                Carving.chisel.addVariation("glassdyed" + (dyeColors[c].toLowerCase()), CarvingUtils.variationFor(stainedGlass.withProperty(prop, EnumDyeColor.byDyeDamage(c)), -1));


                factory.newBlock(Material.GLASS, "glassdyed" + (dyeColors[c].toLowerCase()), new ChiselBlockProvider<>(glassCreator, BlockCarvable.class)).opaque(false)
                        .setParentFolder("glass_stained/"+dyeColors[c].toLowerCase())
                        .newVariation("panel")
                        .next("framed")
                        .next("framed_fancy")
                        .next("streaks")
                        .next("rough")
                        .next("brick")
                        .addOreDict("blockGlass")
                        .addOreDict("blockGlass"+dyeColors[c])
                        .build(b -> b.setSoundType(SoundType.GLASS).setHardness(0.3F));
            }
        }
    },

    /*GLASSPANE { TODO Remodel
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            //Carving.chisel.addVariation("glasspane", CarvingUtils.variationFor(Blocks.glass_pane.getDefaultState(), -1));
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
                    .build(b -> b.setQuantityDropped(0).setCanSilkHarvest(true).setSoundType(SoundType.GLASS));
        }
    },
    GLASSPANEDYED { TODO Remodel
        @Override
        void addBlocks(ChiselBlockFactory factory) {
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
                        .build(b -> b.setQuantityDropped(0).setCanSilkHarvest(true).setSoundType(SoundType.GLASS));
            }
        }
    },*/

    GLOWSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("glowstone", CarvingUtils.variationFor(Blocks.GLOWSTONE.getDefaultState(), -20));


            BlockCreator<BlockCarvable> glowstoneCreator = (mat, index, maxVariation, data) -> new BlockCarvable(mat, index, maxVariation, data) {
                @Override
                public int quantityDroppedWithBonus(int fortune, Random random) {
                    return MathHelper.clamp(this.quantityDropped(random) + random.nextInt(fortune + 1), 1, 4);
                }

                @Override
                public int quantityDropped(Random random) {
                    return 2 + random.nextInt(3);
                }

                @Override
                @Nullable
                public Item getItemDropped(IBlockState state, Random rand, int fortune) {
                    return Items.GLOWSTONE_DUST;
                }

                @Override
                public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
                    Random rand = world instanceof World ? ((World)world).rand : RANDOM;
                    drops.add(new ItemStack(Items.GLOWSTONE_DUST, this.quantityDropped(state, fortune, rand)));
                }
            };

            factory.newBlock(Material.GLASS, "glowstone", new ChiselBlockProvider<>(glowstoneCreator, BlockCarvable.class))
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bismuth")
                    .next("tiles-large-bismuth")
                    .next("tiles-medium-bismuth")
                    .next("neon")
                    .next("neon-panel")
                    .addOreDict("glowstone")
                    .build(b ->b.setLightLevel(1.0f).setHardness(0.3f).setResistance(1.5f).setSoundType(SoundType.GLASS));
        }
    },

    GOLD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.IRON, "blockGold", null, beaconBaseProvider)
                    .setParentFolder("metals/gold")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockGold")
                    .build(b-> b.setSoundType(SoundType.METAL).setHardness(3.0F).setHarvestLevel("pickaxe", 1));

            factory.newBlock(Material.IRON, "gold", null, beaconBaseProvider)
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
                    .addOreDict("blockGold")
                    .build(b-> b.setSoundType(SoundType.METAL).setHardness(3.0F).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockGold");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Gold");
        }
    },

    GRANITE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState stone = Blocks.STONE.getDefaultState();
            IProperty<BlockStone.EnumType> prop = BlockStone.VARIANT;
            Carving.chisel.addVariation("granite", CarvingUtils.variationFor(stone.withProperty(prop, BlockStone.EnumType.GRANITE), -21));
            Carving.chisel.addVariation("granite", CarvingUtils.variationFor(stone.withProperty(prop, BlockStone.EnumType.GRANITE_SMOOTH), -20));


            factory.newBlock(Material.ROCK, "granite", provider)
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .addOreDict("stoneGranite")
                    .addOreDict("stoneGranitePolished")
                    .build(b -> b.setHardness(1.5F).setResistance(30.0F).setSoundType(SoundType.STONE));
        }
    },

    HARDENED_CLAY {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("hardenedclay", CarvingUtils.variationFor(Blocks.HARDENED_CLAY.getDefaultState(), -1));


            factory.newBlock(Material.ROCK, "hardenedclay", provider) // REMAP
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .addOreDict("hardenedClay")
                    .build(b -> b.setHardness(1.25F).setResistance(7.0F).setSoundType(SoundType.STONE));
        }
    },

    /*HEX_PLATING {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "hexPlating", provider)
                    .newVariation("hexBase") //TODO: Colored+Glowy stuff
                    .next("hexNew")
                    .build();
        }
    },*/

    ICE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("ice", CarvingUtils.variationFor(Blocks.ICE.getDefaultState(), -1));

            BlockCreator<BlockCarvable> iceCreator = (mat, index, maxVariation, data) -> new BlockCarvable(mat, index, maxVariation, data) { {
                    this.slipperiness = 0.98F;
            } };

            factory.newBlock(Material.ICE, "ice", new ChiselBlockProvider<>(iceCreator, BlockCarvable.class)).opaque(false)
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .addOreDict("ice")
                    .addOreDict("blockIce")
                    .build(b -> b.setHardness(0.5F).setLightOpacity(3).setSoundType(SoundType.GLASS));

            factory.newBlock(Material.ICE, "icepillar", new ChiselBlockProvider<>(iceCreator, BlockCarvable.class)).opaque(false)
                    .setGroup("ice")
                    .newVariation("plainplain")
                    .next("plaingreek")
                    .next("greekplain")
                    .next("greekgreek")
                    .next("convexplain")
                    .next("carved")
                    .next("ornamental")
                    .addOreDict("ice")
                    .addOreDict("blockIce")
                    .build(b -> b.setHardness(0.5F).setLightOpacity(3).setSoundType(SoundType.GLASS));
        }
    },

    INVAR {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.IRON, "blockInvar", null, beaconBaseProvider)
                    .setParentFolder("metals/invar")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockInvar")
                    .build(b-> b.setSoundType(SoundType.METAL).setHardness(5.0f).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockInvar");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Invar");
        }
    },

    IRON {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.IRON, "blockIron", null, beaconBaseProvider)
                    .setParentFolder("metals/iron")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockIron")
                    .build(b-> b.setSoundType(SoundType.METAL).setHardness(5.0f).setHarvestLevel("pickaxe", 1));

            factory.newBlock(Material.IRON, "iron", null, beaconBaseProvider)
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
                    .addOreDict("blockIron")
                    .build(b-> b.setSoundType(SoundType.METAL).setHardness(5.0f).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockIron");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Iron");
        }
    },

    IRONPANE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            //Carving.chisel.addVariation("ironpane", Blocks.IRON_BARS.getDefaultState(), -1); TODO fix model
            factory.newBlock(Material.IRON, "ironpane", provider)
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
                    .build(b-> b.setSoundType(SoundType.METAL).setHardness(5.0f).setHarvestLevel("pickaxe", 1));
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
                    .next("directionleft")
                    .next("directionright")
                    .next("infocon")
                    .build(b -> b.setSoundType(ChiselSoundTypes.METAL));
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.laboratory, 8), "***", "*X*", "***", '*', new ItemStack(Blocks.STONE, 1), 'X', new ItemStack(Items.QUARTZ, 1));
        }
    },

    LAPIS {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "lapis", null, provider)
                    .newVariation("terrain-lapisblock-chunky")
                    .next("terrain-lapisblock-panel")
                    .next("terrain-lapisblock-zelda")
                    .next("terrain-lapisornate")
                    .next("terrain-lapistile")
                    .next("a1-blocklapis-panel")
                    .next("a1-blocklapis-smooth")
                    .next("a1-blocklapis-ornatelayer")
                    .next("masonryLapis")
                    .addOreDict("blockLapis")
                    .build(b-> b.setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE));
            
            CarvingUtils.addOreGroup("blockLapis");
        }
    },

    LAVASTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "lavastone", new ChiselBlockProvider<>(BlockCarvable::new, BlockCarvable.class))
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .build(b -> b.setHardness(4.0F).setResistance(50.0F).setSoundType(SoundType.STONE));
        }
        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.lavastone, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.STONE, 1), 'X', new ItemStack(Items.LAVA_BUCKET, 1));
        }
    },

    LEAD {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.IRON, "blockLead", null, beaconBaseProvider)
                    .setParentFolder("metals/lead")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockLead")
                    .build(b-> b.setSoundType(SoundType.METAL).setHardness(5.0f).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockLead");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Lead");
        }
    },

    /*LEAVES { TODO Potential omission candidate
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

    LIMESTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "limestone", provider) // REMAP!
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("raw").setOrder(-100)
                    .addOreDict("stoneLimestone")
                    .addOreDict("stoneLimestonePolished")
                    .build(b -> b.setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE));

        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry) {
            GenerationHandler.INSTANCE.addGeneration(ChiselBlocks.limestone2.getDefaultState().withProperty(ChiselBlocks.limestone2.getMetaProp(), 7),
                    new WorldGenInfo(Configurations.limestoneAmount, 32, 64, 1, BlockMatcher.forBlock(Blocks.STONE)));
        }
    },

    MARBLE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "marble", provider) // REMAP
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("raw").setOrder(-100)
                    .addOreDict("stoneMarble")
                    .addOreDict("stoneMarblePolished")
                    .build(b -> b.setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE));


        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry) {
            GenerationHandler.INSTANCE.addGeneration(ChiselBlocks.marble2.getDefaultState().withProperty(ChiselBlocks.marble2.getMetaProp(), 7),
                    new WorldGenInfo(Configurations.marbleAmount, 32, 64, 1, BlockMatcher.forBlock(Blocks.STONE)));
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
                        .addOreDict("stoneMarble")
                        .addOreDict("stoneMarblePolished")
                        .build(b -> b.setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE));
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
                        .addOreDict("stoneMarble")
                        .addOreDict("stoneMarblePolished")
                        .build(b -> b.setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE));
            }
        }
    },

    NETHERBRICK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("netherbrick", CarvingUtils.variationFor(Blocks.NETHER_BRICK.getDefaultState(), -1));
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
                    .build(b -> b.setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE));
        }
    },

    NETHERRACK {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("netherrack", CarvingUtils.variationFor(Blocks.NETHERRACK.getDefaultState(), -20));


            BlockCreator<BlockCarvable> netherrackCreator = (mat, index, maxVariation, data) -> new BlockCarvable(mat, index, maxVariation, data) {
                @Override
                public boolean isFireSource(@Nonnull World world, BlockPos pos, EnumFacing side) {
                    return side == EnumFacing.UP;
                }
            };

            factory.newBlock(Material.ROCK, "netherrack", new ChiselBlockProvider<>(netherrackCreator, BlockCarvable.class))
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
                    .addOreDict("netherrack")
                    .build(b -> b.setHardness(0.4F).setSoundType(SoundType.STONE));
        }
    },

    NICKEL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "blockNickel", null, beaconBaseProvider)
                    .setParentFolder("metals/nickel")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockNickel")
                    .build(b-> b.setSoundType(SoundType.METAL).setHardness(5.0f).setHarvestLevel("pickaxe", 1));

            CarvingUtils.addOreGroup("blockNickel");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Nickel");
        }
    },

    OBSIDIAN {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("obsidian", CarvingUtils.variationFor(Blocks.OBSIDIAN.getDefaultState(), -1));


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
                    .addOreDict("obsidian")
                    .build(b -> b.setHardness(50.0F).setResistance(2000.0F).setSoundType(SoundType.STONE).setHarvestLevel("pickaxe", 3));
        }
    },

    PAPER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.PLANTS, "paper", provider)
                    .newVariation("box")
                    .next("throughMiddle")
                    .next("cross")
                    .next("sixSections")
                    .next("vertical")
                    .next("horizontal")
                    .next("floral")
                    .next("plain")
                    .next("door")
                    .build(b -> b.setSoundType(SoundType.PLANT)
                            .setHardness(1.5f));
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.paper, 32), "ppp", "psp", "ppp", 'p', Items.PAPER, 's', "stickWood");
        }
    },

    PLANKS {
        @SuppressWarnings("null")
        @Override
        void addBlocks(ChiselBlockFactory factory)
        {
            IBlockState planks = Blocks.PLANKS.getDefaultState();
            IProperty<BlockPlanks.EnumType> prop = BlockPlanks.VARIANT;

            for (int c = 0; c < plank_names.length; c++)
            {
                String groupName = "planks-" + plank_names[c];
                Carving.chisel.addVariation(groupName, CarvingUtils.variationFor(planks.withProperty(prop, BlockPlanks.EnumType.byMetadata(c)), -1));
                Carving.chisel.setVariationSound(groupName, ChiselSounds.wood_chisel);

                factory.newBlock(Material.WOOD, "planks-" + plank_names[c], provider)
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
                        .addOreDict("plankWood")
                        .build(b -> b.setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD));

            }
        }
    },

    PLATINUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.IRON, "blockPlatinum", null, beaconBaseProvider)
                    .setParentFolder("metals/platinum")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockPlatinum")
                    .build(b-> b.setSoundType(SoundType.METAL).setHardness(5.0f).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockPlatinum");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Platinum");
        }
    },

    PRISMARINE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            IBlockState prismarine = Blocks.PRISMARINE.getDefaultState();
            IProperty<BlockPrismarine.EnumType> prop = BlockPrismarine.VARIANT;
            Carving.chisel.addVariation("prismarine", CarvingUtils.variationFor(prismarine.withProperty(prop, BlockPrismarine.EnumType.ROUGH), -3));
            Carving.chisel.addVariation("prismarine", CarvingUtils.variationFor(prismarine.withProperty(prop, BlockPrismarine.EnumType.BRICKS), -2));
            Carving.chisel.addVariation("prismarine", CarvingUtils.variationFor(prismarine.withProperty(prop, BlockPrismarine.EnumType.DARK), -1));


            factory.newBlock(Material.ROCK, "prismarine", provider) //REMAP!
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .addOreDict("prismarine")
                    .addOreDict("prismarineBrick")
                    .addOreDict("prismarineDark")
                    .build(b -> b.setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE));
        }
    },

    QUARTZ {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState quartzBlock = Blocks.QUARTZ_BLOCK.getDefaultState();
            IProperty<BlockQuartz.EnumType> prop = BlockQuartz.VARIANT;
            Carving.chisel.addVariation("quartz", CarvingUtils.variationFor(quartzBlock.withProperty(prop, BlockQuartz.EnumType.DEFAULT), -25));
            Carving.chisel.addVariation("quartz", CarvingUtils.variationFor(quartzBlock.withProperty(prop, BlockQuartz.EnumType.CHISELED), -24));
            //Carving.chisel.addVariation("quartz", CarvingUtils.variationFor(quartzBlock.withProperty(prop, BlockQuartz.EnumType.LINES_X), -23));
            Carving.chisel.addVariation("quartz", CarvingUtils.variationFor(quartzBlock.withProperty(prop, BlockQuartz.EnumType.LINES_Y), -22));
            //Carving.chisel.addVariation("quartz", CarvingUtils.variationFor(quartzBlock.withProperty(prop, BlockQuartz.EnumType.LINES_Z), -21));


            factory.newBlock(Material.ROCK, "quartz", provider)
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    //.next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .addOreDict("blockQuartz")
                    .build(b -> b.setHardness(0.8F).setResistance(4.0F).setSoundType(SoundType.STONE));
        }
    },

    REDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            Carving.chisel.addVariation("redstone", CarvingUtils.variationFor(Blocks.REDSTONE_BLOCK.getDefaultState(), -20));


            BlockCreator<BlockCarvable> redstoneCreator = (mat, index, maxVariation, data) -> new BlockCarvable(mat, index, maxVariation, data) {
                @Override
                public boolean canProvidePower(IBlockState state)
                {
                    return true;
                }

                @Override
                public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
                {
                    return 15;
                }
            };

            factory.newBlock(Material.IRON, "redstone", new ChiselBlockProvider<>(redstoneCreator, BlockCarvable.class))
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .addOreDict("blockRedstone")
                    .build(b->b.setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.METAL));
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(Items.REDSTONE, 9), "X", 'X', "blockRedstone");
        }
    },

    SANDSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState ss = Blocks.SANDSTONE.getDefaultState();
            IProperty<BlockSandStone.EnumType> prop = BlockSandStone.TYPE;
            Carving.chisel.addVariation("sandstoneyellow", CarvingUtils.variationFor(ss.withProperty(prop, BlockSandStone.EnumType.DEFAULT), -3));
            Carving.chisel.addVariation("sandstoneyellow", CarvingUtils.variationFor(ss.withProperty(prop, BlockSandStone.EnumType.SMOOTH), -2));
            Carving.chisel.addVariation("sandstoneyellow", CarvingUtils.variationFor(ss.withProperty(prop, BlockSandStone.EnumType.CHISELED), -1));

            factory.newBlock(Material.ROCK, "sandstoneyellow", provider) // REMAP!
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .addOreDict("sandstone")
                    .build(b -> b.setSoundType(SoundType.STONE).setHardness(0.8F));
        }
    },

    SANDSTONE_RED {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            IBlockState ss = Blocks.RED_SANDSTONE.getDefaultState();
            IProperty<BlockRedSandstone.EnumType> prop = BlockRedSandstone.TYPE;

            Carving.chisel.addVariation("sandstonered", CarvingUtils.variationFor(ss.withProperty(prop, BlockRedSandstone.EnumType.DEFAULT), -3));
            Carving.chisel.addVariation("sandstonered", CarvingUtils.variationFor(ss.withProperty(prop, BlockRedSandstone.EnumType.SMOOTH), -2));
            Carving.chisel.addVariation("sandstonered", CarvingUtils.variationFor(ss.withProperty(prop, BlockRedSandstone.EnumType.CHISELED), -1));

            factory.newBlock(Material.ROCK, "sandstonered", provider) // REMAP!
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .addOreDict("sandstone")
                    .build(b -> b.setSoundType(SoundType.STONE).setHardness(0.8F));
        }
    },

    SANDSTONE_SCRIBBLES {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "sandstone-scribbles", provider)
                    .setGroup("sandstoneyellow")
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
                    .addOreDict("sandstone")
                    .build(b -> b.setSoundType(SoundType.STONE).setHardness(0.8F));
        }
    },

    SILVER {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "blockSilver", null, beaconBaseProvider)
                    .setParentFolder("metals/silver")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockSilver")
                    .build(b -> b.setSoundType(SoundType.METAL).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockSilver");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Silver");
        }
    },

    STEEL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "blockSteel", null, beaconBaseProvider)
                    .setParentFolder("metals/steel")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockSteel")
                    .build(b -> b.setSoundType(SoundType.METAL).setHarvestLevel("pickaxe", 2));

            CarvingUtils.addOreGroup("blockSteel");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Steel");
        }
    },

    STONEBRICK {
        @Override
        void addBlocks(ChiselBlockFactory factory)
        {
            if (Configurations.chiselStoneToCobbleBricks) {
                Carving.chisel.addVariation("stonebrick", CarvingUtils.variationFor(Blocks.STONE.getDefaultState(), -100));
            }
            
            IBlockState stoneBricks = Blocks.STONEBRICK.getDefaultState();
            IProperty<BlockStoneBrick.EnumType> prop = BlockStoneBrick.VARIANT;
            Carving.chisel.addVariation("stonebrick", CarvingUtils.variationFor(stoneBricks.withProperty(prop, BlockStoneBrick.EnumType.DEFAULT), -26));
            if (Configurations.allowMossy) {
                Carving.chisel.addVariation("stonebrick", CarvingUtils.variationFor(stoneBricks.withProperty(prop, BlockStoneBrick.EnumType.MOSSY), -25));
            }
            Carving.chisel.addVariation("stonebrick", CarvingUtils.variationFor(stoneBricks.withProperty(prop, BlockStoneBrick.EnumType.CRACKED), -24));
            Carving.chisel.addVariation("stonebrick", CarvingUtils.variationFor(stoneBricks.withProperty(prop, BlockStoneBrick.EnumType.CHISELED), -23));

            // Carving.chisel.addVariation("stonebrick", CarvingUtils.variationFor(Blocks.double_stone_slab.getDefaultState().withProperty(BlockDoubleStoneSlab.VARIANT, BlockDoubleStoneSlab.EnumType.STONE), -21));

            factory.newBlock(Material.ROCK, "stonebrick", provider) // REMAP!
                    .setParentFolder("stone")
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("")
                    .next("bricks-small")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("largeornate")
                    .next("poison")
                    .next("sunken")
                    .addOreDict("stone")
                    .addOreDict("brickStone")
                    .addOreDict("bricksStone")
                    .build(b -> b.setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE));
        }
    },

    TECHNICAL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.IRON, "technical", provider)
                    .setGroup("factory")
                    .newVariation("scaffold").opaque(false)
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
                    .next("scaffoldTransparent").opaque(false)
                    .next("fanFastTransparent").opaque(false)
                    .next("fanStillTransparent").opaque(false)
                    .next("massiveFan")
                    .next("massiveHexPlating")
                    .build(b -> b.setSoundType(SoundType.METAL));

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
                    .build(b -> b.setSoundType(SoundType.METAL));
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
                    .build(b -> b.setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE));

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
                    .build(b -> b.setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE));
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.temple, 8), "***", "*X*", "***",
                    '*', new ItemStack(Blocks.STONE, 1),
                    'X', new ItemStack(Items.DYE, 1, 6));
        }
    },

    TIN {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "blockTin", null, beaconBaseProvider)
                    .setParentFolder("metals/tin")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockTin")
                    .build(b -> b.setSoundType(SoundType.METAL).setHarvestLevel("pickaxe", 1));

            CarvingUtils.addOreGroup("blockTin");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Tin");
        }
    },

    TYRIAN {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.IRON, "tyrian", provider)
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
                    .build(b -> b.setSoundType(SoundType.METAL));
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.tyrian, 32, 0), "SSS", "SXS", "SSS",
                    'S', new ItemStack(Blocks.STONE, 1),
                    'X', new ItemStack(Items.IRON_INGOT, 1));
        }
    },

    URANIUM {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            factory.newBlock(Material.ROCK, "blockUranium", null, beaconBaseProvider)
                    .setParentFolder("metals/uranium")
                    .newVariation("caution")
                    .next("crate")
                    .next("thermal")
                    .next("machine")
                    .next("badGreggy")
                    .next("bolted")
                    .next("scaffold")
                    .addOreDict("blockUranium")
                    .build(b -> b.setSoundType(SoundType.METAL).setHarvestLevel("pickaxe", 1));
            
            CarvingUtils.addOreGroup("blockUranium");
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            registerIngotUncraftRecipe(registry, "Uranium");
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
                    .build(b -> b.setHardness(1.5F).setResistance(20.0F).setSoundType(SoundType.STONE));
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.valentines, 4), "***", "*X*", "***",
                    '*', "stone",
                    'X', new ItemStack(Items.DYE, 1, 9));

            // Companion Cube, woo!
            Features.addShapedRecipe(registry, "companion_cube", new ItemStack(ChiselBlocks.valentines, 32, 9), "***", "*X*", "***",
                    '*', "stone",
                    'X', new ItemStack(Items.SKULL, 1, OreDictionary.WILDCARD_VALUE));
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
        void addRecipes(IForgeRegistry<IRecipe> registry)
        {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.voidstone, 16, 0), " E ", "OOO", " E ",
                    'E', new ItemStack(Items.ENDER_EYE),
                    'O', new ItemStack(Blocks.OBSIDIAN));

            Features.addShapedRecipe(registry, "voidstone2", new ItemStack(ChiselBlocks.voidstone, 48, 0), " P ", "PEP", " P ",
                    'E', new ItemStack(Items.ENDER_PEARL),
                    'P', new ItemStack(Blocks.PURPUR_BLOCK));
        }
    },

    WATERSTONE {
        @Override
        void addBlocks(ChiselBlockFactory factory) {
            factory.newBlock(Material.ROCK, "waterstone", new ChiselBlockProvider<>(BlockCarvable::new, BlockCarvable.class)).opaque(false)
                    .newVariation("cracked")
                    .next("bricks-soft")
                    .next("bricks-cracked")
                    .next("bricks-triple")
                    .next("bricks-encased")
                    .next("braid")
                    .next("array")
                    .next("tiles-large")
                    .next("tiles-small")
                    .next("chaotic-medium")
                    .next("chaotic-small")
                    .next("dent")
                    .next("french-1")
                    .next("french-2")
                    .next("jellybean")
                    .next("layers")
                    .next("mosaic")
                    .next("ornate")
                    .next("panel")
                    .next("road")
                    .next("slanted")
                    .next("zag")
                    .next("circularct")
                    .next("weaver")
                    .next("bricks-solid")
                    .next("bricks-small")
                    .next("circular")
                    .next("tiles-medium")
                    .next("pillar")
                    .next("twisted")
                    .next("prism")
                    .next("bricks-chaotic")
                    .next("cuts")
                    .build(b -> b.setHardness(4.0F).setResistance(50.0F).setSoundType(SoundType.STONE));
        }

        @Override
        void addRecipes(IForgeRegistry<IRecipe> registry) {
            addShapedRecipe(registry, new ItemStack(ChiselBlocks.waterstone, 8, 0), "***", "*X*", "***",
                    '*', new ItemStack(Blocks.STONE, 1),
                    'X', new ItemStack(Items.WATER_BUCKET, 1));
        }
    },

    WOOL {
        @Override
        void addBlocks(ChiselBlockFactory factory) {

            IBlockState wool = Blocks.WOOL.getDefaultState();
            IProperty<EnumDyeColor> prop = BlockColored.COLOR;

            for(int c = 0; c < dyeColors.length; c++)
            {
                Carving.chisel.addVariation("wool_" + (dyeColors[c].toLowerCase()), CarvingUtils.variationFor(wool.withProperty(prop, EnumDyeColor.byDyeDamage(c)), -1));


                factory.newBlock(Material.CLOTH, "wool_" + (dyeColors[c].toLowerCase()), provider)
                        .setParentFolder("wool")
                        .newVariation("legacy_"+(dyeColors[c].toLowerCase()))
                        .next("llama_"+(dyeColors[c].toLowerCase()))
                        .addOreDict("blockWool")
                        .build(b -> b.setSoundType(SoundType.CLOTH).setHardness(0.8F));
            }
        }
    };

    private static final String[] dyeColors =
            {
                    "Black",
                    "Red",
                    "Green",
                    "Brown",
                    "Blue",
                    "Purple",
                    "Cyan",
                    "LightGray",
                    "Gray",
                    "Pink",
                    "Lime",
                    "Yellow",
                    "LightBlue",
                    "Magenta",
                    "Orange",
                    "White"
            };

    //@formatter:on

    private static final String[] dyeOres = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow",
            "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };

    public static final String[] plank_names = { "oak", "spruce", "birch", "jungle", "acacia", "dark-oak" };

    private static final @Nonnull BlockCreator<BlockCarvable> creator = BlockCarvable::new;
    private static final @Nonnull ChiselBlockProvider<BlockCarvable> provider = new ChiselBlockProvider<>(creator, BlockCarvable.class);

    private static final @Nonnull BlockCreator<BlockCarvable> beaconBaseCreator = (mat, index, maxVariation, data) -> new BlockCarvable(mat, index, maxVariation, data) {
        @Override
        public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
            return true;
        }
    };

    private static final @Nonnull ChiselBlockProvider<BlockCarvable> beaconBaseProvider = new ChiselBlockProvider<>(beaconBaseCreator, BlockCarvable.class);

    @RequiredArgsConstructor
    @ParametersAreNonnullByDefault
    private static class ChiselBlockProvider<T extends Block & ICarvable> implements BlockProvider<T> {

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

    @SubscribeEvent
    public static void loadBlocks(RegistryEvent.Register<Block> event) {
        Chisel.logger.info("Loading blocks...");
        int num = 0;
        ChiselBlockFactory factory = ChiselBlockFactory.newFactory(event.getRegistry(), Chisel.MOD_ID);
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

    @SubscribeEvent
    public static void loadItems(RegistryEvent.Register<Item> event) {
        Chisel.logger.info("Loading items...");
        int num = 0;
        for (Features f : values()) {
            if (f.enabled()) {
                f.addItems(event.getRegistry());
                ++num;
            } else {
                logDisabled(f);
            }
        }
        Chisel.logger.info(num + " Feature's items loaded.");
    }

    @SubscribeEvent
    public static void loadRecipes(RegistryEvent.Register<IRecipe> event) {
        Chisel.logger.info("Loading recipes...");
        int num = 0;
        for (Features f : values()) {
            if (f.enabled()) {
                f.addRecipes(event.getRegistry());
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

    void addItems(IForgeRegistry<Item> registry) {
        ;
    }

    void addRecipes(IForgeRegistry<IRecipe> registry) {
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

    private static void registerSlabTop(@Nonnull Block bottom, Block top) {
        ResourceLocation block = Block.REGISTRY.getNameForObject(bottom);
        String name = block.getResourcePath() + "_top";
        // GameRegistry.registerBlock(top, ItemCarvableSlab.class, name); TODO
    }

    private static void registerIngotUncraftRecipe(IForgeRegistry<IRecipe> registry, String ore)
    {
        registerOreUncraftRecipe(registry, "block" + ore, "ingot" + ore);
    }

    private static void registerOreUncraftRecipe(IForgeRegistry<IRecipe> registry, String blockOre, String endOre)
    {
        if(OreDictionary.doesOreNameExist(endOre)) {
            List<ItemStack> oreList = OreDictionary.getOres(endOre);

            if(oreList.size() > 0)
            {
                ItemStack result = oreList.get(0);
                addShapedRecipe(registry, "uncraft_" + blockOre, new ItemStack(result.getItem(), 9, result.getItemDamage(), result.getTagCompound()), "X", 'X', blockOre);
            }
        }
    }
    
    private static final ResourceLocation RECIPE_GROUP = new ResourceLocation("", "");
    
    void addShapelessRecipe(IForgeRegistry<IRecipe> registry, ItemStack result, Object... ingredients) { 
        addShapelessRecipe(registry, Configurations.featureName(this), result, ingredients); 
    }
    void addShapelessRecipe(IForgeRegistry<IRecipe> registry, Block result, Object... ingredients) {
        addShapelessRecipe(registry, Configurations.featureName(this), result, ingredients); 
    }
    void addShapelessRecipe(IForgeRegistry<IRecipe> registry, Item result, Object... ingredients) {
        addShapelessRecipe(registry, Configurations.featureName(this), result, ingredients); 
    }
    
    private static void addShapelessRecipe(IForgeRegistry<IRecipe> registry, String name, ItemStack result, Object... ingredients) {
        registry.register(new ShapelessOreRecipe(RECIPE_GROUP, result, ingredients).setRegistryName(Chisel.MOD_ID, name));
    }  
    private static void addShapelessRecipe(IForgeRegistry<IRecipe> registry, String name, Block result, Object... ingredients) {
        registry.register(new ShapelessOreRecipe(RECIPE_GROUP, result, ingredients).setRegistryName(Chisel.MOD_ID, name));
    }
    private static void addShapelessRecipe(IForgeRegistry<IRecipe> registry, String name, Item result, Object... ingredients) {
        registry.register(new ShapelessOreRecipe(RECIPE_GROUP, result, ingredients).setRegistryName(Chisel.MOD_ID, name));
    }
    
    void addShapedRecipe(IForgeRegistry<IRecipe> registry, ItemStack result, Object... ingredients) { 
        addShapedRecipe(registry, Configurations.featureName(this), result, ingredients); 
    }
    void addShapedRecipe(IForgeRegistry<IRecipe> registry, Block result, Object... ingredients) {
        addShapedRecipe(registry, Configurations.featureName(this), result, ingredients); 
    }
    void addShapedRecipe(IForgeRegistry<IRecipe> registry, Item result, Object... ingredients) {
        addShapedRecipe(registry, Configurations.featureName(this), result, ingredients); 
    }
    
    private static void addShapedRecipe(IForgeRegistry<IRecipe> registry, String name, @Nonnull ItemStack result, Object... ingredients) {
        registry.register(new ShapedOreRecipe(RECIPE_GROUP, result, ingredients).setRegistryName(Chisel.MOD_ID, name));
    }
    private static void addShapedRecipe(IForgeRegistry<IRecipe> registry, String name, Block result, Object... ingredients) {
        registry.register(new ShapedOreRecipe(RECIPE_GROUP, result, ingredients).setRegistryName(Chisel.MOD_ID, name));
    }
    private static void addShapedRecipe(IForgeRegistry<IRecipe> registry, String name, Item result, Object... ingredients) {
        registry.register(new ShapedOreRecipe(RECIPE_GROUP, result, ingredients).setRegistryName(Chisel.MOD_ID, name));
    }
}
