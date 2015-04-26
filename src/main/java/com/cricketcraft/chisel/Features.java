package com.cricketcraft.chisel;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.cricketcraft.chisel.api.carving.CarvableHelper;
import com.cricketcraft.chisel.api.carving.CarvingUtils.SimpleCarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.cricketcraft.chisel.api.carving.IVariationInfo;
import com.cricketcraft.chisel.block.*;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.client.render.SubmapManagerAntiblock;
import com.cricketcraft.chisel.client.render.SubmapManagerCarpetFloor;
import com.cricketcraft.chisel.client.render.SubmapManagerFakeController;
import com.cricketcraft.chisel.client.render.SubmapManagerSlab;
import com.cricketcraft.chisel.compat.fmp.ItemBlockChiselTorchPart;
import com.cricketcraft.chisel.config.Configurations;
import com.cricketcraft.chisel.entity.EntityBallOMoss;
import com.cricketcraft.chisel.entity.EntityCloudInABottle;
import com.cricketcraft.chisel.entity.EntitySmashingRock;
import com.cricketcraft.chisel.init.ChiselBlocks;
import com.cricketcraft.chisel.init.ChiselItems;
import com.cricketcraft.chisel.init.ChiselTabs;
import com.cricketcraft.chisel.item.ItemBallOMoss;
import com.cricketcraft.chisel.item.ItemBlockPresent;
import com.cricketcraft.chisel.item.ItemCarvable;
import com.cricketcraft.chisel.item.ItemCarvablePumpkin;
import com.cricketcraft.chisel.item.ItemCarvableSlab;
import com.cricketcraft.chisel.item.ItemCloudInABottle;
import com.cricketcraft.chisel.item.ItemSmashingRock;
import com.cricketcraft.chisel.item.ItemUpgrade;
import com.cricketcraft.chisel.item.chisel.ItemChisel;
import com.cricketcraft.chisel.item.chisel.ItemChisel.ChiselType;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import static com.cricketcraft.chisel.init.ChiselBlocks.*;
import static com.cricketcraft.chisel.utils.General.sGNames;

public enum Features {

	AE_CERTUS_QUARTZ("appliedenergistics2") {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("AECertusQuartz", GameRegistry.findBlock("appliedenergistics2", "tile.BlockQuartz"), 0, 0);
			Carving.chisel.addVariation("AECertusQuartz", GameRegistry.findBlock("appliedenergistics2", "tile.BlockQuartzPillar"), 0, 1);
			Carving.chisel.addVariation("AECertusQuartz", GameRegistry.findBlock("appliedenergistics2", "tile.BlockQuartzChiseled"), 0, 2);
			Carving.chisel.registerOre("AECertusQuartz", "AECertusQuartz");
		}
	},

	AE_SKY_STONE("appliedenergistics2") {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("AESkyStone", GameRegistry.findBlock("appliedenergistics2", "tile.BlockSkyStone"), 1, 0);
			Carving.chisel.addVariation("AESkyStone", GameRegistry.findBlock("appliedenergistics2", "tile.BlockSkyStone"), 2, 1);
			Carving.chisel.addVariation("AESkyStone", GameRegistry.findBlock("appliedenergistics2", "tile.BlockSkyStone"), 3, 2);
			Carving.chisel.registerOre("AESkyStone", "AESkyStone");
		}
	},

	ALUMINUM {

		@Override
		void addBlocks() {
			BlockCarvable aluminum = (BlockCarvable) new BlockBeaconBase(Material.iron).setStepSound(Block.soundTypeMetal).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setHardness(5F)
					.setResistance(10F);
			aluminum.carverHelper.addVariation("tile.metalOre.0.desc", 0, "metals/aluminum/caution");
			aluminum.carverHelper.addVariation("tile.metalOre.1.desc", 1, "metals/aluminum/crate");
			aluminum.carverHelper.addVariation("tile.metalOre.2.desc", 2, "metals/aluminum/thermal");
			aluminum.carverHelper.addVariation("tile.metalOre.3.desc", 3, "metals/aluminum/adv");
			aluminum.carverHelper.addVariation("tile.metalOre.4.desc", 4, "metals/aluminum/egregious");
			aluminum.carverHelper.addVariation("tile.metalOre.5.desc", 5, "metals/aluminum/bolted");
			aluminum.carverHelper.registerAll(aluminum, "aluminumblock");
			Carving.chisel.registerOre("aluminumblock", "blockAluminum");
		}
	},

	AMBER("Thaumcraft") {

		@Override
		void addBlocks() {
			BlockCarvable amber = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("amber", GameRegistry.findBlock("Thaumcraft", "blockCosmeticOpaque"), 0, 0);
			Carving.chisel.addVariation("amber", GameRegistry.findBlock("Thaumcraft", "blockCosmeticOpaque"), 1, 1);
			amber.carverHelper.registerAll(amber, "amber");
			Carving.chisel.registerOre("amber", "amber");
		}
	},

	ANDESITE {

		@Override
		void addBlocks() {
			BlockCarvable andesite = (BlockCarvable) new BlockCarvable(Material.rock).setHardness(2.0F).setResistance(10.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks);

			andesite.carverHelper.addVariation("tile.andesite.0.desc", 0, "andesite/andesite");
			andesite.carverHelper.addVariation("tile.andesite.1.desc", 1, "andesite/andesitePolished");
            andesite.carverHelper.addVariation("tile.andesite.2.desc", 2, "andesite/andesitePillar");
            andesite.carverHelper.addVariation("tile.andesite.3.desc", 3, "andesite/andesiteLBrick");
            andesite.carverHelper.addVariation("tile.andesite.4.desc", 4, "andesite/andesiteOrnate");
            andesite.carverHelper.addVariation("tile.andesite.5.desc", 5, "andesite/andesitePrism");
            andesite.carverHelper.addVariation("tile.andesite.6.desc", 6, "andesite/andesiteTiles");
			andesite.carverHelper.registerAll(andesite, "andesite");
			Carving.chisel.registerOre("andesite", "blockAndesite");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(andesite, 2), diorite, "cobblestone"));
		}
	},

	ANTIBLOCK {

		@Override
		void addBlocks() {
			BlockCarvable antiBlock = (BlockCarvable) new BlockCarvableAntiBlock().setCreativeTab(ChiselTabs.tabOtherChiselBlocks);

			if (!Configurations.allowChiselCrossColors) {
				antiBlock.carverHelper.forbidChiseling = true;
			}

			for (int i = 0; i < 16; i++) {
				antiBlock.carverHelper.addVariation("tile.antiBlock." + ItemDye.field_150921_b[i] + ".desc", i, new SubmapManagerAntiblock(ItemDye.field_150921_b[i]));
			}

			antiBlock.carverHelper.registerAll(antiBlock, "antiBlock");
			OreDictionary.registerOre("antiBlock", antiBlock);
		}

		@Override
		void addRecipes() {
			if (meta == 0) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.antiBlock, 8, 15), "SSS", "SGS", "SSS", 'S', "stone", 'G', "dustGlowstone"));
			}
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.antiBlock, 8, meta), "BBB", "BdB", "BBB",
					'd', dyeOres[meta], 'B', new ItemStack(ChiselBlocks.antiBlock, 1, OreDictionary.WILDCARD_VALUE)));
		}

		@Override
		boolean needsMetaRecipes() {
			return true;
		}
	},

	ARCANE("Thaumcraft") {

		@Override
		void addBlocks() {
			BlockCarvable arcane = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("arcane", GameRegistry.findBlock("Thaumcraft", "blockCosmeticSolid"), 6, 0);
			Carving.chisel.addVariation("arcane", GameRegistry.findBlock("Thaumcraft", "blockCosmeticSolid"), 7, 1);
			arcane.carverHelper.addVariation("tile.arcane.0.desc", 0, "arcane/moonEngrave");
			arcane.carverHelper.addVariation("tile.arcane.1.desc", 1, "arcane/moonGlowAnim");
			arcane.carverHelper.addVariation("tile.arcane.2.desc", 2, "arcane/arcaneTile");
			arcane.carverHelper.addVariation("tile.arcane.3.desc", 3, "arcane/runes");
			arcane.carverHelper.addVariation("tile.arcane.4.desc", 4, "arcane/runesGlow");
			arcane.carverHelper.addVariation("tile.arcane.5.desc", 5, "arcane/bigBrick");
			arcane.carverHelper.addVariation("tile.arcane.6.desc", 6, "arcane/conduitGlowAnim");
            arcane.carverHelper.addVariation("tile.arcane.7.desc", 7, "arcane/BorderBrain");
            arcane.carverHelper.addVariation("tile.arcane.8.desc", 8, "arcane/ArcaneBorder");
            arcane.carverHelper.addVariation("tile.arcane.9.desc", 9, "arcane/arcaneMatrix");
            arcane.carverHelper.addVariation("tile.arcane.10.desc", 10, "arcane/thaumcraftLogo");
            arcane.carverHelper.addVariation("tile.arcane.11.desc", 11, "arcane/arcaneCrackAnim");
			arcane.carverHelper.registerAll(arcane, "arcane");
			Carving.chisel.registerOre("arcane", "arcane");
		}
	},

	AUTO_CHISEL {

		@Override
		void addBlocks() {
			Block autoChisel = new BlockAutoChisel().setBlockTextureName(Chisel.MOD_ID + ":autoChisel/autoChisel").setCreativeTab(ChiselTabs.tabChisel).setBlockName("chisel.autoChisel");
			GameRegistry.registerBlock(autoChisel, "autoChisel");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.autoChisel, 1), new Object[] { "XXX", " Y ", "YYY", Character.valueOf('X'), Blocks.stone_slab,
					Character.valueOf('Y'), "ingotIron" }));
		}
	},

	AUTO_CHISEL_UPGRADES(AUTO_CHISEL) {

		@Override
		void addItems() {
			ItemUpgrade upgrade = (ItemUpgrade) new ItemUpgrade("upgrade").setCreativeTab(ChiselTabs.tabChisel);
			GameRegistry.registerItem(upgrade, "upgrade");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.upgrade, 1, 0), new Object[] { "IEI", "EUE", "RRR", 'I', "ingotIron", 'E', Items.emerald, 'R', Items.redstone, 'U',
					Items.sugar }));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.upgrade, 1, 1), new Object[] { "IEI", "EUE", "RRR", 'I', "ingotIron", 'E', Items.emerald, 'R', Items.redstone, 'U',
					Blocks.hopper }));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.upgrade, 1, 2), new Object[] { "IEI", "EUE", "RRR", 'I', "ingotIron", 'E', Items.emerald, 'R', Items.redstone, 'U',
					Blocks.crafting_table }));
		}
	},

	BALL_OF_MOSS {

		@Override
		void addItems() {
			ItemBallOMoss ballomoss = (ItemBallOMoss) new ItemBallOMoss().setTextureName("Chisel:ballomoss").setCreativeTab(ChiselTabs.tabChisel);
			EntityRegistry.registerModEntity(EntityBallOMoss.class, "BallOMoss", 2, Chisel.instance, 40, 1, true);
			GameRegistry.registerItem(ballomoss, "ballomoss");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselItems.ballomoss, 1), "XYX", "YXY", "XYX", 'X', Blocks.vine, 'Y', Items.stick);
		}
	},

	BLOOD_RUNE("AWWayofTime") {

		@Override
		void addBlocks() {
			BlockCarvable bloodRune = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("bloodRune", GameRegistry.findBlock("AWWayofTime", "AlchemicalWizardrybloodRune"), 0, 0);
			bloodRune.carverHelper.addVariation("tile.bloodRune.0.desc", 0, "bloodMagic/bloodRuneArranged");
			bloodRune.carverHelper.addVariation("tile.bloodRune.1.desc", 1, "bloodMagic/bloodRuneBricks");
			bloodRune.carverHelper.addVariation("tile.bloodRune.2.desc", 2, "bloodMagic/bloodRuneCarved");
			bloodRune.carverHelper.addVariation("tile.bloodRune.3.desc", 3, "bloodMagic/bloodRuneCarvedRadial");
			bloodRune.carverHelper.addVariation("tile.bloodRune.4.desc", 4, "bloodMagic/bloodRuneClassicPanel");
			bloodRune.carverHelper.addVariation("tile.bloodRune.5.desc", 5, "bloodMagic/bloodRuneTiles");
			bloodRune.carverHelper.registerAll(bloodRune, "bloodRune");
			Carving.chisel.registerOre("bloodRune", "bloodRune");
		}
	},

	BLOOD_BLOCK("AWWayofTime") {

		@Override
		void addBlocks() {
			BlockCarvable bloodBrick = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("bloodBrick", GameRegistry.findBlock("AWWayofTime", "largeBloodStoneBrick"), 0, 0);
			Carving.chisel.addVariation("bloodBrick", GameRegistry.findBlock("AWWayofTime", "bloodStoneBrick"), 0, 1);
			bloodBrick.carverHelper.registerAll(bloodBrick, "bloodBrick");
			Carving.chisel.registerOre("bloodBrick", "bloodBrick");
		}
	},

	BOOKSHELF {

		@Override
		void addBlocks() {
			BlockCarvable bookshelf = (BlockCarvable) new BlockCarvableBookshelf().setHardness(1.5F).setCreativeTab(ChiselTabs.tabWoodChiselBlocks).setStepSound(Block.soundTypeWood);
			Carving.chisel.addVariation("bookshelf", Blocks.bookshelf, 0, 0);
			bookshelf.carverHelper.addVariation("tile.bookshelf.1.desc", 1, "bookshelf/rainbow");
			bookshelf.carverHelper.addVariation("tile.bookshelf.2.desc", 2, "bookshelf/necromancer-novice");
			bookshelf.carverHelper.addVariation("tile.bookshelf.3.desc", 3, "bookshelf/necromancer");
			bookshelf.carverHelper.addVariation("tile.bookshelf.4.desc", 4, "bookshelf/redtomes");
			bookshelf.carverHelper.addVariation("tile.bookshelf.5.desc", 5, "bookshelf/abandoned");
			bookshelf.carverHelper.addVariation("tile.bookshelf.6.desc", 6, "bookshelf/hoarder");
			bookshelf.carverHelper.addVariation("tile.bookshelf.7.desc", 7, "bookshelf/brim");
			bookshelf.carverHelper.addVariation("tile.bookshelf.8.desc", 8, "bookshelf/historician");
			bookshelf.carverHelper.registerAll(bookshelf, "bookshelf");
			bookshelf.setHarvestLevel("axe", 0);
			Carving.chisel.registerOre("bookshelf", "bookshelf");
		}
	},

	BRICK_CUSTOM {

		@Override
		void addBlocks() {
			BlockCarvable brickCustom = (BlockCarvable) new BlockCarvable(Material.rock).setStepSound(Block.soundTypeStone).setCreativeTab(ChiselTabs.tabStoneChiselBlocks);
			Carving.chisel.addVariation("brickCustom", Blocks.brick_block, 0, 0);
			brickCustom.carverHelper.addVariation("tile.brickCustom.1.desc", 1, "brickCustom/large");
			brickCustom.carverHelper.addVariation("tile.brickCustom.2.desc", 2, "brickCustom/mortarless");
			brickCustom.carverHelper.addVariation("tile.brickCustom.3.desc", 3, "brickCustom/varied");
			// brickCustom.carverHelper.addVariation("tile.brickCustom.4.desc",
			// 4, "brickCustom/cracked");
			brickCustom.carverHelper.addVariation("tile.brickCustom.5.desc", 5, "brickCustom/aged");
			brickCustom.carverHelper.addVariation("tile.brickCustom.6.desc", 6, "brickCustom/yellow");
			brickCustom.carverHelper.registerAll(brickCustom, "brickCustom");
			Carving.chisel.registerOre("brickCustom", "brickCustom");
		}
	},

	BRONZE {

		@Override
		void addBlocks() {
			BlockCarvable bronze = (BlockCarvable) new BlockBeaconBase(Material.iron).setStepSound(Block.soundTypeMetal).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setHardness(5F)
					.setResistance(10F);
			bronze.carverHelper.addVariation("tile.metalOre.0.desc", 0, "metals/bronze/caution");
			bronze.carverHelper.addVariation("tile.metalOre.1.desc", 1, "metals/bronze/crate");
			bronze.carverHelper.addVariation("tile.metalOre.2.desc", 2, "metals/bronze/thermal");
			bronze.carverHelper.addVariation("tile.metalOre.3.desc", 3, "metals/bronze/adv");
			bronze.carverHelper.addVariation("tile.metalOre.4.desc", 4, "metals/bronze/egregious");
			bronze.carverHelper.addVariation("tile.metalOre.5.desc", 5, "metals/bronze/bolted");
			bronze.carverHelper.registerAll(bronze, "bronzeblock");
			Carving.chisel.registerOre("bronzeblock", "blockBronze");
		}
	},

	CARPET {

		@Override
		void addBlocks() {
			BlockCarvable carpet_block = (BlockCarvable) new BlockCarvable(Material.cloth).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Block.soundTypeCloth);

			carpet_block.carverHelper.addVariation("tile.carpet_block.0.desc", 0, "carpet/white");
			carpet_block.carverHelper.addVariation("tile.carpet_block.1.desc", 1, "carpet/orange");
			carpet_block.carverHelper.addVariation("tile.carpet_block.2.desc", 2, "carpet/lily");
			carpet_block.carverHelper.addVariation("tile.carpet_block.3.desc", 3, "carpet/lightblue");
			carpet_block.carverHelper.addVariation("tile.carpet_block.4.desc", 4, "carpet/yellow");
			carpet_block.carverHelper.addVariation("tile.carpet_block.5.desc", 5, "carpet/lightgreen");
			carpet_block.carverHelper.addVariation("tile.carpet_block.6.desc", 6, "carpet/pink");
			carpet_block.carverHelper.addVariation("tile.carpet_block.7.desc", 7, "carpet/darkgrey");
			carpet_block.carverHelper.addVariation("tile.carpet_block.8.desc", 8, "carpet/grey");
			carpet_block.carverHelper.addVariation("tile.carpet_block.9.desc", 9, "carpet/teal");
			carpet_block.carverHelper.addVariation("tile.carpet_block.10.desc", 10, "carpet/purple");
			carpet_block.carverHelper.addVariation("tile.carpet_block.11.desc", 11, "carpet/darkblue");
			carpet_block.carverHelper.addVariation("tile.carpet_block.12.desc", 12, "carpet/brown");
			carpet_block.carverHelper.addVariation("tile.carpet_block.13.desc", 13, "carpet/green");
			carpet_block.carverHelper.addVariation("tile.carpet_block.14.desc", 14, "carpet/red");
			carpet_block.carverHelper.addVariation("tile.carpet_block.15.desc", 15, "carpet/black");
			if (!Configurations.allowChiselCrossColors) {
				carpet_block.carverHelper.forbidChiseling = true;
			}
			carpet_block.carverHelper.registerAll(carpet_block, "carpet_block");
			OreDictionary.registerOre("blockCarpet", carpet_block);
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.carpet_block, 8, meta), "YYY", "YXY", "YYY", 'X', new ItemStack(Items.string, 1), 'Y', new ItemStack(Blocks.wool, 1, meta));
		}

		@Override
		boolean needsMetaRecipes() {
			return true;
		}
	},

	CARPET_FLOOR {

		@Override
		void addBlocks() {
			BlockCarvableCarpet carpet = (BlockCarvableCarpet) new BlockCarvableCarpet(Material.cloth).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.1F).setStepSound(Block.soundTypeCloth)
					.setLightOpacity(0).setStepSound(Block.soundTypeCloth);

			carpet.carverHelper.addVariation("tile.carpet.0.desc", 0, new SubmapManagerCarpetFloor("white"));
			carpet.carverHelper.addVariation("tile.carpet.1.desc", 1, new SubmapManagerCarpetFloor("orange"));
			carpet.carverHelper.addVariation("tile.carpet.2.desc", 2, new SubmapManagerCarpetFloor("lily"));
			carpet.carverHelper.addVariation("tile.carpet.3.desc", 3, new SubmapManagerCarpetFloor("lightblue"));
			carpet.carverHelper.addVariation("tile.carpet.4.desc", 4, new SubmapManagerCarpetFloor("yellow"));
			carpet.carverHelper.addVariation("tile.carpet.5.desc", 5, new SubmapManagerCarpetFloor("lightgreen"));
			carpet.carverHelper.addVariation("tile.carpet.6.desc", 6, new SubmapManagerCarpetFloor("pink"));
			carpet.carverHelper.addVariation("tile.carpet.7.desc", 7, new SubmapManagerCarpetFloor("darkgrey"));
			carpet.carverHelper.addVariation("tile.carpet.8.desc", 8, new SubmapManagerCarpetFloor("grey"));
			carpet.carverHelper.addVariation("tile.carpet.9.desc", 9, new SubmapManagerCarpetFloor("teal"));
			carpet.carverHelper.addVariation("tile.carpet.10.desc", 10, new SubmapManagerCarpetFloor("purple"));
			carpet.carverHelper.addVariation("tile.carpet.11.desc", 11, new SubmapManagerCarpetFloor("darkblue"));
			carpet.carverHelper.addVariation("tile.carpet.12.desc", 12, new SubmapManagerCarpetFloor("brown"));
			carpet.carverHelper.addVariation("tile.carpet.13.desc", 13, new SubmapManagerCarpetFloor("green"));
			carpet.carverHelper.addVariation("tile.carpet.14.desc", 14, new SubmapManagerCarpetFloor("red"));
			carpet.carverHelper.addVariation("tile.carpet.15.desc", 15, new SubmapManagerCarpetFloor("black"));
			if (!Configurations.allowChiselCrossColors) {
				carpet.carverHelper.forbidChiseling = true;
			}
			carpet.carverHelper.registerAll(carpet, "carpet");

			// for (int i = 0; i < 16; i++) {
			// String group = "carpet." + i;

			// TODO needle stuff
			// Carving.needle.addVariation(group, Blocks.carpet, i, 0);
			// Carving.needle.addVariation(group, carpet, i, 2);
			// Carving.needle.addVariation(group, carpet_block, i, 1);
			// }

			OreDictionary.registerOre("carpet", carpet);
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.carpet, 3, meta), "XX", 'X', new ItemStack(ChiselBlocks.carpet_block, 1, meta));
		}

		@Override
		boolean needsMetaRecipes() {
			return true;
		}
	},

	CHISEL {

		@Override
		void addItems() {
			ItemChisel chisel = (ItemChisel) new ItemChisel(ChiselType.IRON).setCreativeTab(ChiselTabs.tabChisel);
			ItemChisel diamondChisel = (ItemChisel) new ItemChisel(ChiselType.DIAMOND).setCreativeTab(ChiselTabs.tabChisel);
			ItemChisel obsidianChisel = (ItemChisel) new ItemChisel(ChiselType.OBSIDIAN).setCreativeTab(ChiselTabs.tabChisel);
			GameRegistry.registerItem(chisel, "chisel");
			GameRegistry.registerItem(diamondChisel, "diamondChisel");
			GameRegistry.registerItem(obsidianChisel, "obsidianChisel");
		}

		@Override
		void addRecipes() {
			if (Configurations.chiselRecipe) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.chisel), " YY", " YY", "X  ", 'X', "stickWood", 'Y', "ingotIron"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.diamondChisel), " YY", " YY", "x  ", 'x', "stickWood", 'Y', "gemDiamond"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.obsidianChisel), " YY", " YY", "x  ", 'x', "stickWood", 'Y', Blocks.obsidian));
			} else {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.chisel), " Y", "X ", 'X', "stickWood", 'Y', "ingotIron"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.diamondChisel), " Y", "X ", 'X', "stickWood", 'Y', "gemDiamond"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.obsidianChisel), " Y", "X ", 'X', "stickWood", 'Y', Blocks.obsidian));
			}
		}
	},

	CLOUD {

		@Override
		void addBlocks() {
			BlockCarvable cloud = (BlockCloud) new BlockCloud().setHardness(0.2F).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setLightOpacity(3).setStepSound(Block.soundTypeCloth);
			cloud.carverHelper.addVariation("tile.cloud.0.desc", 0, "cloud/cloud");
			cloud.carverHelper.addVariation("tile.cloud.1.desc", 1, "cloud/large");
			cloud.carverHelper.addVariation("tile.cloud.2.desc", 2, "cloud/small");
			cloud.carverHelper.addVariation("tile.cloud.3.desc", 3, "cloud/vertical");
			cloud.carverHelper.addVariation("tile.cloud.4.desc", 4, "cloud/grid");
			cloud.carverHelper.registerAll(cloud, "cloud");
			OreDictionary.registerOre("cloud", cloud);
			Carving.chisel.registerOre("cloud", "cloud");
		}

		@Override
		void addItems() {
			ItemCloudInABottle itemCloudInABottle = (ItemCloudInABottle) new ItemCloudInABottle().setTextureName("Chisel:cloudinabottle-x").setCreativeTab(ChiselTabs.tabChisel);
			EntityRegistry.registerModEntity(EntityCloudInABottle.class, "CloudInABottle", 1, Chisel.instance, 40, 1, true);
			GameRegistry.registerItem(itemCloudInABottle, "cloudinabottle");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselItems.cloudinabottle, 1), "X X", "XYX", " X ", 'X', Blocks.glass, 'Y', Items.quartz);
		}
	},

	COBBLESTONE {

		@Override
		void addBlocks() {
			BlockCarvable cobblestone = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("cobblestone", Blocks.cobblestone, 0, 0);
			cobblestone.carverHelper.addVariation("tile.cobblestone.0.desc", 1, "cobblestone/terrain-cobb-brickaligned");
			cobblestone.carverHelper.addVariation("tile.cobblestone.1.desc", 2, "cobblestone/terrain-cob-detailedbrick");
			cobblestone.carverHelper.addVariation("tile.cobblestone.2.desc", 3, "cobblestone/terrain-cob-smallbrick");
			cobblestone.carverHelper.addVariation("tile.cobblestone.3.desc", 4, "cobblestone/terrain-cobblargetiledark");
			cobblestone.carverHelper.addVariation("tile.cobblestone.4.desc", 5, "cobblestone/terrain-cobbsmalltile");
			cobblestone.carverHelper.addVariation("tile.cobblestone.5.desc", 6, "cobblestone/terrain-cob-french");
			cobblestone.carverHelper.addVariation("tile.cobblestone.6.desc", 7, "cobblestone/terrain-cob-french2");
			cobblestone.carverHelper.addVariation("tile.cobblestone.7.desc", 8, "cobblestone/terrain-cobmoss-creepdungeon");
			cobblestone.carverHelper.addVariation("tile.cobblestone.8.desc", 9, "cobblestone/terrain-mossysmalltiledark");
			cobblestone.carverHelper.addVariation("tile.cobblestone.9.desc", 10, "cobblestone/terrain-pistonback-dungeontile");
			cobblestone.carverHelper.addVariation("tile.cobblestone.10.desc", 11, "cobblestone/terrain-pistonback-darkcreeper");
			cobblestone.carverHelper.addVariation("tile.cobblestone.11.desc", 12, "cobblestone/terrain-pistonback-darkdent");
			cobblestone.carverHelper.addVariation("tile.cobblestone.12.desc", 13, "cobblestone/terrain-pistonback-darkemboss");
			cobblestone.carverHelper.addVariation("tile.cobblestone.13.desc", 14, "cobblestone/terrain-pistonback-darkmarker");
			cobblestone.carverHelper.addVariation("tile.cobblestone.14.desc", 15, "cobblestone/terrain-pistonback-darkpanel");
			cobblestone.carverHelper.registerAll(cobblestone, "cobblestone");
			Carving.chisel.registerOre("cobblestone", "cobblestone");
		}
	},

	COBBLESTONE_MOSSY {

		@Override
		void addBlocks() {

			BlockCarvable mossy_cobblestone = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10.0F)
					.setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("mossy_cobblestone", Blocks.mossy_cobblestone, 0, 0);
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.1.desc", 1, "cobblestonemossy/terrain-cobb-brickaligned");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.2.desc", 2, "cobblestonemossy/terrain-cob-detailedbrick");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.3.desc", 3, "cobblestonemossy/terrain-cob-smallbrick");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.4.desc", 4, "cobblestonemossy/terrain-cobblargetiledark");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.5.desc", 5, "cobblestonemossy/terrain-cobbsmalltile");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.6.desc", 6, "cobblestonemossy/terrain-cob-french");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.7.desc", 7, "cobblestonemossy/terrain-cob-french2");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.8.desc", 8, "cobblestonemossy/terrain-cobmoss-creepdungeon");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.9.desc", 9, "cobblestonemossy/terrain-mossysmalltiledark");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.10.desc", 10, "cobblestonemossy/terrain-pistonback-dungeontile");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.11.desc", 11, "cobblestonemossy/terrain-pistonback-darkcreeper");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.12.desc", 12, "cobblestonemossy/terrain-pistonback-darkdent");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.13.desc", 13, "cobblestonemossy/terrain-pistonback-darkemboss");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.14.desc", 14, "cobblestonemossy/terrain-pistonback-darkmarker");
			mossy_cobblestone.carverHelper.addVariation("tile.stoneMoss.15.desc", 15, "cobblestonemossy/terrain-pistonback-darkpanel");
			mossy_cobblestone.carverHelper.registerAll(mossy_cobblestone, "mossy_cobblestone");
			Carving.chisel.registerOre("mossy_cobblestone", "mossy_cobblestone");
		}
	},

	CONCRETE {

		@Override
		void addBlocks() {
			BlockCarvable concrete = (BlockConcrete) new BlockConcrete().setStepSound(Block.soundTypeStone).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(0.5F);
			concrete.carverHelper.addVariation("tile.concrete.0.desc", 0, "concrete/default");
			concrete.carverHelper.addVariation("tile.concrete.1.desc", 1, "concrete/block");
			concrete.carverHelper.addVariation("tile.concrete.2.desc", 2, "concrete/doubleslab");
			concrete.carverHelper.addVariation("tile.concrete.3.desc", 3, "concrete/blocks");
			concrete.carverHelper.addVariation("tile.concrete.4.desc", 4, "concrete/weathered");
			concrete.carverHelper.addVariation("tile.concrete.5.desc", 5, "concrete/weathered-block");
			concrete.carverHelper.addVariation("tile.concrete.6.desc", 6, "concrete/weathered-doubleslab");
			concrete.carverHelper.addVariation("tile.concrete.7.desc", 7, "concrete/weathered-blocks");
			concrete.carverHelper.addVariation("tile.concrete.8.desc", 8, "concrete/weathered-half");
			concrete.carverHelper.addVariation("tile.concrete.9.desc", 9, "concrete/weathered-block-half");
			concrete.carverHelper.addVariation("tile.concrete.10.desc", 10, "concrete/asphalt");
			concrete.carverHelper.registerAll(concrete, "concrete");
			OreDictionary.registerOre("concrete", concrete);
			Carving.chisel.registerOre("concrete", "concrete");
		}

		@Override
		void addRecipes() {
			Block concreteRecipeBlock = Block.getBlockFromName(Configurations.config.get("tweaks", "concrete recipe block", "gravel",
					"Unlocalized name of the block that, when burned, will produce concrete (examples: lightgem, stone)").getString());
			if (concreteRecipeBlock == null)
				concreteRecipeBlock = Blocks.gravel;

			FurnaceRecipes.smelting().func_151393_a(concreteRecipeBlock, new ItemStack(ChiselBlocks.concrete), 0.1F);
		}
	},

	COPPER
	{

		@Override
		void addBlocks() {
			BlockCarvable copper = (BlockCarvable) new BlockBeaconBase(Material.iron).setStepSound(Block.soundTypeMetal).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setHardness(5F)
					.setResistance(10F);
			copper.carverHelper.addVariation("tile.metalOre.0.desc", 0, "metals/copper/caution");
			copper.carverHelper.addVariation("tile.metalOre.1.desc", 1, "metals/copper/crate");
			copper.carverHelper.addVariation("tile.metalOre.2.desc", 2, "metals/copper/thermal");
			copper.carverHelper.addVariation("tile.metalOre.3.desc", 3, "metals/copper/adv");
			copper.carverHelper.addVariation("tile.metalOre.4.desc", 4, "metals/copper/egregious");
			copper.carverHelper.addVariation("tile.metalOre.5.desc", 5, "metals/copper/bolted");
			copper.carverHelper.registerAll(copper, "copperblock");
			Carving.chisel.registerOre("copperblock", "blockCopper");
		}
	},

	DIAMOND_BLOCK {

		@Override
		void addBlocks() {

			BlockCarvable diamond_block = (BlockBeaconBase) new BlockBeaconBase().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("diamond_block", Blocks.diamond_block, 0, 0);
			diamond_block.carverHelper.addVariation("tile.diamond.1.desc", 1, "diamond/terrain-diamond-embossed");
			diamond_block.carverHelper.addVariation("tile.diamond.2.desc", 2, "diamond/terrain-diamond-gem");
			diamond_block.carverHelper.addVariation("tile.diamond.3.desc", 3, "diamond/terrain-diamond-cells");
			diamond_block.carverHelper.addVariation("tile.diamond.4.desc", 4, "diamond/terrain-diamond-space");
			diamond_block.carverHelper.addVariation("tile.diamond.5.desc", 5, "diamond/terrain-diamond-spaceblack");
			diamond_block.carverHelper.addVariation("tile.diamond.6.desc", 6, "diamond/terrain-diamond-simple");
			diamond_block.carverHelper.addVariation("tile.diamond.7.desc", 7, "diamond/terrain-diamond-bismuth");
			diamond_block.carverHelper.addVariation("tile.diamond.8.desc", 8, "diamond/terrain-diamond-crushed");
			diamond_block.carverHelper.addVariation("tile.diamond.9.desc", 9, "diamond/terrain-diamond-four");
			diamond_block.carverHelper.addVariation("tile.diamond.10.desc", 10, "diamond/terrain-diamond-fourornate");
			diamond_block.carverHelper.addVariation("tile.diamond.11.desc", 11, "diamond/terrain-diamond-zelda");
			diamond_block.carverHelper.addVariation("tile.diamond.12.desc", 12, "diamond/terrain-diamond-ornatelayer");
            diamond_block.carverHelper.registerAll(diamond_block, "diamond_block");
			Carving.chisel.registerOre("diamond_block", "diamond");
		}
	},

	DIORITE {

		@Override
		void addBlocks() {
			BlockCarvable diorite = (BlockCarvable) new BlockCarvable(Material.rock).setHardness(2.0F).setResistance(10.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks);

			diorite.carverHelper.addVariation("tile.diorite.0.desc", 0, "diorite/diorite");
			diorite.carverHelper.addVariation("tile.diorite.1.desc", 1, "diorite/dioritePolished");
			diorite.carverHelper.addVariation("tile.diorite.2.desc", 2, "diorite/dioritePillar");
            diorite.carverHelper.addVariation("tile.diorite.3.desc", 3, "diorite/dioriteLBrick");
            diorite.carverHelper.addVariation("tile.diorite.4.desc", 4, "diorite/dioriteOrnate");
            diorite.carverHelper.addVariation("tile.diorite.5.desc", 5, "diorite/dioritePrism");
            diorite.carverHelper.addVariation("tile.diorite.6.desc", 6, "diorite/dioriteTiles");
			diorite.carverHelper.registerAll(diorite, "diorite");
			Carving.chisel.registerOre("diorite", "diorite");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(diorite, 2), "cq", "qc", 'c', "cobblestone", 'q', "gemQuartz"));
		}
	},

	DIRT {

		@Override
		void addBlocks() {
			BlockCarvable dirt = (BlockCarvable) new BlockCarvable(Material.ground).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setStepSound(Block.soundTypeGravel)
					.setBlockName("dirt.default");
			Carving.chisel.addVariation("dirt", Blocks.dirt, 0, 0);

			// dirt.carverHelper.addVariation("Dirt", 0, Blocks.dirt);
			dirt.carverHelper.addVariation("tile.dirt.0.desc", 0, "dirt/bricks");
			dirt.carverHelper.addVariation("tile.dirt.1.desc", 1, "dirt/netherbricks");
			dirt.carverHelper.addVariation("tile.dirt.2.desc", 2, "dirt/bricks3");
			dirt.carverHelper.addVariation("tile.dirt.3.desc", 3, "dirt/cobble");
			dirt.carverHelper.addVariation("tile.dirt.4.desc", 4, "dirt/reinforcedCobbleDirt");
			dirt.carverHelper.addVariation("tile.dirt.5.desc", 5, "dirt/reinforcedDirt");
			dirt.carverHelper.addVariation("tile.dirt.6.desc", 6, "dirt/happy");
			dirt.carverHelper.addVariation("tile.dirt.7.desc", 7, "dirt/bricks2");
			dirt.carverHelper.addVariation("tile.dirt.8.desc", 8, "dirt/bricks+dirt2");
			dirt.carverHelper.addVariation("tile.dirt.9.desc", 9, "dirt/hor");
			dirt.carverHelper.addVariation("tile.dirt.10.desc", 10, "dirt/vert");
			dirt.carverHelper.addVariation("tile.dirt.11.desc", 11, "dirt/layers");
			dirt.carverHelper.addVariation("tile.dirt.12.desc", 12, "dirt/vertical");
			dirt.carverHelper.registerAll(dirt, "dirt");
			dirt.setHarvestLevel("shovel", 0);
			OreDictionary.registerOre("dirt", dirt);
			Carving.chisel.registerOre("dirt", "dirt");
		}
	},

	EMERALD_BLOCK {

		@Override
		void addBlocks() {
			BlockCarvable emerald_block = (BlockBeaconBase) new BlockBeaconBase().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(5.0F).setResistance(10.0F)
					.setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("emerald_block", Blocks.emerald_block, 0, 0);
			emerald_block.carverHelper.addVariation("tile.emerald.1.desc", 1, "emerald/panel");
			emerald_block.carverHelper.addVariation("tile.emerald.2.desc", 2, "emerald/panelclassic");
			emerald_block.carverHelper.addVariation("tile.emerald.3.desc", 3, "emerald/smooth");
			emerald_block.carverHelper.addVariation("tile.emerald.4.desc", 4, "emerald/chunk");
			emerald_block.carverHelper.addVariation("tile.emerald.5.desc", 5, "emerald/goldborder");
			emerald_block.carverHelper.addVariation("tile.emerald.6.desc", 6, "emerald/zelda");
			emerald_block.carverHelper.addVariation("tile.emerald.7.desc", 7, "emerald/cell");
			emerald_block.carverHelper.addVariation("tile.emerald.8.desc", 8, "emerald/cellbismuth");
			emerald_block.carverHelper.addVariation("tile.emerald.9.desc", 9, "emerald/four");
			emerald_block.carverHelper.addVariation("tile.emerald.10.desc", 10, "emerald/fourornate");
			emerald_block.carverHelper.addVariation("tile.emerald.11.desc", 11, "emerald/ornate");
			emerald_block.carverHelper.registerAll(emerald_block, "emerald_block");
			Carving.chisel.registerOre("emerald_block", "emerald");
		}
	},

    END_STONE {

        @Override
        void addBlocks() {
            //BlockCarvable end_stone = (BlockCarvable) new BlockCarvable(Material.rock).setHardness(2.0F).setResistance(10.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks);
            Carving.chisel.addVariation("end_stone", Blocks.end_stone , 0, 0);
            //end_stone.carverHelper.registerAll(emerald_block, "endStone");
            Carving.chisel.registerOre("end_stone", "end_stone");
        }
    },

	FACTORY {

		@Override
		void addBlocks() {
			BlockCarvable factoryblock = (BlockCarvable) new BlockCarvable(Material.iron).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Chisel.soundMetalFootstep);

			factoryblock.carverHelper.addVariation("tile.factory.0.desc", 0, "factory/dots");
			factoryblock.carverHelper.addVariation("tile.factory.1.desc", 1, "factory/rust2");
			factoryblock.carverHelper.addVariation("tile.factory.2.desc", 2, "factory/rust");
			factoryblock.carverHelper.addVariation("tile.factory.3.desc", 3, "factory/platex");
			factoryblock.carverHelper.addVariation("tile.factory.4.desc", 4, "factory/wireframewhite");
			factoryblock.carverHelper.addVariation("tile.factory.5.desc", 5, "factory/wireframe");
			factoryblock.carverHelper.addVariation("tile.factory.6.desc", 6, "factory/hazard");
			factoryblock.carverHelper.addVariation("tile.factory.7.desc", 7, "factory/hazardorange");
			factoryblock.carverHelper.addVariation("tile.factory.8.desc", 8, "factory/circuit");
			factoryblock.carverHelper.addVariation("tile.factory.9.desc", 9, "factory/metalbox");
			factoryblock.carverHelper.addVariation("tile.factory.10.desc", 10, "factory/goldplate");
			factoryblock.carverHelper.addVariation("tile.factory.11.desc", 11, "factory/goldplating");
			factoryblock.carverHelper.addVariation("tile.factory.12.desc", 12, "factory/grinder");
			factoryblock.carverHelper.addVariation("tile.factory.13.desc", 13, "factory/plating");
			factoryblock.carverHelper.addVariation("tile.factory.14.desc", 14, "factory/rustplates");
			factoryblock.carverHelper.addVariation("tile.factory.15.desc", 15, "factory/column");
			factoryblock.carverHelper.registerAll(factoryblock, "factoryblock");
			Carving.chisel.registerOre("factoryblock", "factoryblock");

			BlockCarvable factoryblock2 = (BlockCarvable) new BlockCarvable(Material.iron).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Chisel.soundMetalFootstep);
			factoryblock2.carverHelper.addVariation("tile.factory2.0.desc", 0, "factory/iceiceice");
			factoryblock2.carverHelper.addVariation("tile.factory2.1.desc", 1, "factory/vent");
			factoryblock2.carverHelper.addVariation("tile.factory2.2.desc", 2, "factory/tilemosaic");
			factoryblock2.carverHelper.addVariation("tile.factory2.3.desc", 3, "factory/wireframeblue");
			factoryblock2.carverHelper.registerAll(factoryblock2, "factoryblock2");
			factoryblock2.carverHelper.registerVariations("factoryblock", factoryblock2);
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.factoryblock, Configurations.factoryBlockAmount, 0), new Object[] { "*X*", "X X", "*X*", '*', new ItemStack(Blocks.stone, 1), 'X',
					new ItemStack(Items.iron_ingot, 1) });
		}
	},

	FANTASY {

		@Override
		void addBlocks() {
			BlockCarvable fantasyblock = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F);

			fantasyblock.carverHelper.addVariation("tile.fantasyblock.0.desc", 0, "fantasy/brick");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.1.desc", 1, "fantasy/brick-faded");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.2.desc", 2, "fantasy/brick-wear");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.3.desc", 3, "fantasy/bricks");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.4.desc", 4, "fantasy/decor");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.5.desc", 5, "fantasy/decor-block");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.6.desc", 6, "fantasy/pillar");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.7.desc", 7, "fantasy/pillar-decorated");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.8.desc", 8, "fantasy/gold-decor-1");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.9.desc", 9, "fantasy/gold-decor-2");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.10.desc", 10, "fantasy/gold-decor-3");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.11.desc", 11, "fantasy/gold-decor-4");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.12.desc", 12, "fantasy/plate");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.13.desc", 13, "fantasy/block");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.14.desc", 14, "fantasy/bricks-chaotic");
			fantasyblock.carverHelper.addVariation("tile.fantasyblock.15.desc", 15, "fantasy/bricks-wear");
			fantasyblock.carverHelper.registerAll(fantasyblock, "fantasyblock");
			Carving.chisel.registerOre("fantasyblock", "fantasy");

			BlockCarvable fantasyblock2 = (BlockCarvable) new BlockCarvable().setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F);
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.0.desc", 0, "fantasy2/brick");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.1.desc", 1, "fantasy2/brick-faded");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.2.desc", 2, "fantasy2/brick-wear");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.3.desc", 3, "fantasy2/bricks");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.4.desc", 4, "fantasy2/decor");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.5.desc", 5, "fantasy2/decor-block");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.6.desc", 6, "fantasy2/pillar");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.7.desc", 7, "fantasy2/pillar-decorated");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.8.desc", 8, "fantasy2/gold-decor-1");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.9.desc", 9, "fantasy2/gold-decor-2");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.10.desc", 10, "fantasy2/gold-decor-3");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.11.desc", 11, "fantasy2/gold-decor-4");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.12.desc", 12, "fantasy2/plate");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.13.desc", 13, "fantasy2/block");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.14.desc", 14, "fantasy2/bricks-chaotic");
			fantasyblock2.carverHelper.addVariation("tile.fantasyblock2.15.desc", 15, "fantasy2/bricks-wear");
			fantasyblock2.carverHelper.registerAll(fantasyblock2, "fantasyblock2");
			Carving.chisel.registerOre("fantasyblock2", "fantasy2");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.fantasyblock, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.gold_nugget, 1));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.fantasyblock2, 8, 0), "***", "*X*", "***", '*', new ItemStack(ChiselBlocks.fantasyblock, 1), 'X', "dyeWhite"));
		}
	},

	FUTURA {

		// TODO Overlay is WIP
		@Override
		void addBlocks() {
			BlockCarvable futura = (BlockCarvable) new BlockCarvable(Material.rock/*
																				 * , "futura/screenOverlay-ctm"
																				 */).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10F);
			futura.carverHelper.addVariation("tile.futura.0.desc", 0, "futura/WIP/screenMetallicWIP");
			futura.carverHelper.addVariation("tile.futura.1.desc", 1, "futura/WIP/screenCyanWIP");
			futura.carverHelper.addVariation("tile.futura.2.desc", 2, "futura/WIP/controllerWIP", new SubmapManagerFakeController());
			futura.carverHelper.addVariation("tile.futura.3.desc", 3, "futura/WIP/wavyWIP");
			futura.carverHelper.registerAll(futura, "futura");

            BlockCarvable circuits = (BlockCarvable) new BlockCarvableGlow("animations/strobe").setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10.0F);
            for (int i = 0; i < 16; i++) {
                circuits.carverHelper.addVariation("tile.futuraCircuit." + ItemDye.field_150921_b[i] + ".desc", i, "futura/circuitPlate");
            }

            // Looks like an AE Controller, feels like one, even lags like one!
            BlockSnakestone FakeAE = (BlockSnakestone) new BlockSnakestone("Chisel:futura/controllerHack/").setBlockName("chisel.fakeController").setCreativeTab(ChiselTabs.tabMetalChiselBlocks);
            GameRegistry.registerBlock(FakeAE, ItemCarvable.class, "FakeAE");
            Carving.chisel.addVariation("futura", FakeAE, 1, 67);
            Carving.chisel.addVariation("futura", FakeAE, 13, 68);

            circuits.carverHelper.registerBlock(circuits, "futuraCircuit");
            circuits.carverHelper.registerVariations("futura", circuits);
            Carving.chisel.registerOre("futura", "futura");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.futura, 8, 0), "SBS", "BGB", "SBS", 'S', "stone", 'G', "dustRedstone", 'B', new ItemStack(Blocks.stonebrick, 1)));
		}
	},

	GLASS {

		@Override
		void addBlocks() {
			BlockCarvableGlass glass = (BlockCarvableGlass) new BlockCarvableGlass().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.3F).setStepSound(Block.soundTypeGlass);
			Carving.chisel.addVariation("glass", Blocks.glass, 0, 0);
			glass.carverHelper.addVariation("tile.glass.1.desc", 1, "glass/terrain-glassbubble");
			glass.carverHelper.addVariation("tile.glass.2.desc", 2, "glass/terrain-glass-chinese");
			glass.carverHelper.addVariation("tile.glass.3.desc", 3, "glass/japanese");
			glass.carverHelper.addVariation("tile.glass.4.desc", 4, "glass/terrain-glassdungeon");
			glass.carverHelper.addVariation("tile.glass.5.desc", 5, "glass/terrain-glasslight");
			glass.carverHelper.addVariation("tile.glass.6.desc", 6, "glass/terrain-glassnoborder");
			glass.carverHelper.addVariation("tile.glass.7.desc", 7, "glass/terrain-glass-ornatesteel");
			glass.carverHelper.addVariation("tile.glass.8.desc", 8, "glass/terrain-glass-screen");
			glass.carverHelper.addVariation("tile.glass.9.desc", 9, "glass/terrain-glassshale");
			glass.carverHelper.addVariation("tile.glass.10.desc", 10, "glass/terrain-glass-steelframe");
			glass.carverHelper.addVariation("tile.glass.11.desc", 11, "glass/terrain-glassstone");
			glass.carverHelper.addVariation("tile.glass.12.desc", 12, "glass/terrain-glassstreak");
			glass.carverHelper.addVariation("tile.glass.13.desc", 13, "glass/terrain-glass-thickgrid");
			glass.carverHelper.addVariation("tile.glass.14.desc", 14, "glass/terrain-glass-thingrid");
			glass.carverHelper.addVariation("tile.glass.15.desc", 15, "glass/a1-glasswindow-ironfencemodern");
			glass.carverHelper.registerAll(glass, "glass");
			Carving.chisel.registerOre("glass", "glass");

			BlockCarvableGlass glass2 = (BlockCarvableGlass) new BlockCarvableGlass().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.3F).setStepSound(Block.soundTypeGlass);
			glass2.carverHelper.addVariation("tile.glass2.0.desc", 0, "glass/chrono");
            glass2.carverHelper.registerBlock(glass2, "glass2");
            glass2.carverHelper.registerVariations("glass", glass2);
		}
	},

	GLASS_PANE {

		@Override
		void addBlocks() {
			BlockCarvablePane glass_pane = (BlockCarvablePane) new BlockCarvablePane(Material.glass, false).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.3F)
					.setStepSound(Block.soundTypeGlass);
			Carving.chisel.addVariation("glass_pane", Blocks.glass_pane, 0, 0);
			glass_pane.carverHelper.addVariation("tile.glass_pane.1.desc", 1, "glasspane/terrain-glassbubble");
			glass_pane.carverHelper.addVariation("tile.glass_pane.2.desc", 2, "glasspane/terrain-glassnoborder");
			glass_pane.carverHelper.addVariation("tile.glass_pane.3.desc", 3, "glasspane/terrain-glass-screen");
			glass_pane.carverHelper.addVariation("tile.glass_pane.4.desc", 4, "glasspane/terrain-glassstreak");
			glass_pane.carverHelper.addVariation("tile.glass_pane.12.desc", 12, "glasspane/chinese");
			glass_pane.carverHelper.addVariation("tile.glass_pane.13.desc", 13, "glasspane/chinese2");
			glass_pane.carverHelper.addVariation("tile.glass_pane.14.desc", 14, "glasspane/japanese");
			glass_pane.carverHelper.addVariation("tile.glass_pane.15.desc", 15, "glasspane/japanese2");
			glass_pane.carverHelper.registerAll(glass_pane, "glass_pane");
			Carving.chisel.registerOre("glass_pane", "glass_pane");
		}
	},

	GLASS_STAINED {

		@Override
		void addBlocks() {
			for (int i = 0; i < 16; i++) {
				final String blockName = "stained_glass_" + sGNames[i].replaceAll(" ", "").toLowerCase();
				String oreName = "stainedGlass" + sGNames[i].replaceAll(" ", "");
				String texName = "glassdyed/" + sGNames[i].toLowerCase().replaceAll(" ", "") + "-";
				int glassPrefix = (i & 3) << 2;
				int glassId = i >> 2;
				Carving.chisel.addVariation(blockName, Blocks.stained_glass, i, 0);
				if (glassPrefix == 0) {
					stainedGlass[glassId] = (BlockCarvableGlass) new BlockCarvableGlass().setStained(true).setHardness(0.3F).setStepSound(Block.soundTypeGlass).setBlockName("Stained Glass");
					stainedGlass[glassId].carverHelper.registerBlock(stainedGlass[glassId], blockName);
				}
				stainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " bubble glass", glassPrefix, texName + "bubble");
				stainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " glass panel", glassPrefix + 1, texName + "panel");
				stainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " fancy glass panel", glassPrefix + 2, texName + "panel-fancy");
				stainedGlass[glassId].carverHelper.addVariation(sGNames[i] + " borderless glass", glassPrefix + 3, texName + "transparent");
				OreDictionary.registerOre(oreName, new ItemStack(Blocks.stained_glass, 1, i));
				Carving.chisel.registerOre(blockName, oreName);
				for (IVariationInfo info : stainedGlass[glassId].carverHelper.infoList) {
					if (info.getVariation().getBlockMeta() < glassPrefix || info.getVariation().getBlockMeta() >= glassPrefix + 4)
						continue;
					stainedGlass[glassId].carverHelper.registerVariation(blockName, info);
				}
			}
		}
	},

	GLASS_STAINED_PANE {

		@Override
		void addBlocks() {
			for (int i = 0; i < 16; i++) {
				final String blockName = "stained_glass_pane_" + sGNames[i].replaceAll(" ", "").toLowerCase();
				String oreName = "stainedGlassPane" + sGNames[i].replaceAll(" ", "");
				String texName = "glasspanedyed/" + sGNames[i].toLowerCase().replaceAll(" ", "") + "-";
				Carving.chisel.addVariation(blockName, Blocks.stained_glass_pane, i, 0);
				int glassPrefix = (i & 1) << 3;
				int glassId = i >> 1;
				if (glassPrefix == 0) {
					stainedGlassPane[glassId] = (BlockCarvablePane) new BlockCarvablePane(Material.glass, true).setStained(true).setHardness(0.3F).setStepSound(Block.soundTypeGlass)
							.setBlockName("Stained Glass Pane").setCreativeTab(ChiselTabs.tabOtherChiselBlocks);
					stainedGlassPane[glassId].carverHelper.registerBlock(stainedGlassPane[glassId], blockName);
				}
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " bubble glass", glassPrefix, texName + "bubble");
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " glass panel", glassPrefix + 1, texName + "panel");
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " fancy glass panel", glassPrefix + 2, texName + "panel-fancy");
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " borderless glass", glassPrefix + 3, texName + "transparent");
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " quadrant glass", glassPrefix + 4, texName + "quad");
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " fancy quadrant glass", glassPrefix + 5, texName + "quad-fancy");
				OreDictionary.registerOre(oreName, new ItemStack(Blocks.stained_glass_pane, 1, i));
				Carving.chisel.registerOre(blockName, oreName);
				for (IVariationInfo info : stainedGlassPane[glassId].carverHelper.infoList) {
					if (info.getVariation().getBlockMeta() < glassPrefix || info.getVariation().getBlockMeta() >= glassPrefix + 8)
						continue;
					stainedGlassPane[glassId].carverHelper.registerVariation(blockName, info);
				}
			}
		}
	},

	GLOWSTONE {

		@Override
		void addBlocks() {
			BlockCarvableGlowstone glowstone = (BlockCarvableGlowstone) new BlockCarvableGlowstone().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.3F).setLightLevel(1.0F)
					.setStepSound(Block.soundTypeGlass);
			Carving.chisel.addVariation("glowstone", Blocks.glowstone, 0, 0);
			glowstone.carverHelper.addVariation("tile.lightstone.1.desc", 1, "lightstone/terrain-sulphur-cobble");
			glowstone.carverHelper.addVariation("tile.lightstone.2.desc", 2, "lightstone/terrain-sulphur-corroded");
			glowstone.carverHelper.addVariation("tile.lightstone.3.desc", 3, "lightstone/terrain-sulphur-glass");
			glowstone.carverHelper.addVariation("tile.lightstone.4.desc", 4, "lightstone/terrain-sulphur-neon");
			glowstone.carverHelper.addVariation("tile.lightstone.5.desc", 5, "lightstone/terrain-sulphur-ornate");
			glowstone.carverHelper.addVariation("tile.lightstone.6.desc", 6, "lightstone/terrain-sulphur-rocky");
			glowstone.carverHelper.addVariation("tile.lightstone.7.desc", 7, "lightstone/terrain-sulphur-shale");
			glowstone.carverHelper.addVariation("tile.lightstone.8.desc", 8, "lightstone/terrain-sulphur-tile");
			glowstone.carverHelper.addVariation("tile.lightstone.9.desc", 9, "lightstone/terrain-sulphur-weavelanternlight");
			glowstone.carverHelper.addVariation("tile.lightstone.10.desc", 10, "lightstone/a1-glowstone-cobble");
			glowstone.carverHelper.addVariation("tile.lightstone.11.desc", 11, "lightstone/a1-glowstone-growth");
			glowstone.carverHelper.addVariation("tile.lightstone.12.desc", 12, "lightstone/a1-glowstone-layers");
			glowstone.carverHelper.addVariation("tile.lightstone.13.desc", 13, "lightstone/a1-glowstone-tilecorroded");
			glowstone.carverHelper.addVariation("tile.lightstone.14.desc", 14, "lightstone/glowstone-bismuth");
			glowstone.carverHelper.addVariation("tile.lightstone.15.desc", 15, "lightstone/glowstone-bismuth-panel");
			glowstone.carverHelper.registerAll(glowstone, "glowstone");
			Carving.chisel.registerOre("glowstone", "glowstone");
		}
	},

	GOLD_BLOCK {

		@Override
		void addBlocks() {
			BlockCarvable gold_block = (BlockBeaconBase) new BlockBeaconBase().setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(3F).setResistance(10F).setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("gold_block", Blocks.gold_block, 0, 0);
			gold_block.carverHelper.addVariation("tile.gold.1.desc", 1, "gold/terrain-gold-largeingot");
			gold_block.carverHelper.addVariation("tile.gold.2.desc", 2, "gold/terrain-gold-smallingot");
			gold_block.carverHelper.addVariation("tile.gold.3.desc", 3, "gold/terrain-gold-brick");
			gold_block.carverHelper.addVariation("tile.gold.4.desc", 4, "gold/terrain-gold-cart");
			gold_block.carverHelper.addVariation("tile.gold.5.desc", 5, "gold/terrain-gold-coin-heads");
			gold_block.carverHelper.addVariation("tile.gold.6.desc", 6, "gold/terrain-gold-coin-tails");
			gold_block.carverHelper.addVariation("tile.gold.7.desc", 7, "gold/terrain-gold-crate-dark");
			gold_block.carverHelper.addVariation("tile.gold.8.desc", 8, "gold/terrain-gold-crate-light");
			gold_block.carverHelper.addVariation("tile.gold.9.desc", 9, "gold/terrain-gold-plates");
			gold_block.carverHelper.addVariation("tile.gold.10.desc", 10, "gold/terrain-gold-rivets");
			gold_block.carverHelper.addVariation("tile.gold.11.desc", 11, "gold/terrain-gold-star");
			gold_block.carverHelper.addVariation("tile.gold.12.desc", 12, "gold/terrain-gold-space");
			gold_block.carverHelper.addVariation("tile.gold.13.desc", 13, "gold/terrain-gold-spaceblack");
			gold_block.carverHelper.addVariation("tile.gold.14.desc", 14, "gold/terrain-gold-simple");

            BlockCarvable gold2 = (BlockCarvable) new BlockBeaconBase(Material.iron).setStepSound(Block.soundTypeMetal).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setHardness(5F)
                    .setResistance(10F);
            gold2.carverHelper.addVariation("tile.metalOre.0.desc", 0, "metals/gold/caution");
            gold2.carverHelper.addVariation("tile.metalOre.1.desc", 1, "metals/gold/crate");
            gold2.carverHelper.addVariation("tile.metalOre.2.desc", 2, "metals/gold/thermal");
            gold2.carverHelper.addVariation("tile.metalOre.3.desc", 3, "metals/gold/adv");
            gold2.carverHelper.addVariation("tile.metalOre.4.desc", 4, "metals/gold/egregious");
            gold2.carverHelper.addVariation("tile.metalOre.5.desc", 5, "metals/gold/bolted");
            gold2.carverHelper.registerBlock(gold2, "gold2");
            gold2.carverHelper.registerVariations("gold_block", gold2);

            gold_block.carverHelper.registerAll(gold_block, "gold_block");
			Carving.chisel.registerOre("gold_block", "blockGold");
		}
	},

	GRANITE {

		@Override
		void addBlocks() {
			BlockCarvable granite = (BlockCarvable) new BlockCarvable(Material.rock).setHardness(2.0F).setResistance(10.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks);

			granite.carverHelper.addVariation("tile.granite.0.desc", 0, "granite/granite");
			granite.carverHelper.addVariation("tile.granite.1.desc", 1, "granite/granitePolished");
			granite.carverHelper.addVariation("tile.granite.2.desc", 2, "granite/granitePillar");
            granite.carverHelper.addVariation("tile.granite.3.desc", 3, "granite/graniteLBrick");
            granite.carverHelper.addVariation("tile.granite.4.desc", 4, "granite/graniteOrnate");
            granite.carverHelper.addVariation("tile.granite.5.desc", 5, "granite/granitePrism");
            granite.carverHelper.addVariation("tile.granite.6.desc", 6, "granite/graniteTiles");
			granite.carverHelper.registerAll(granite, "granite");
			Carving.chisel.registerOre("granite", "granite");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapelessOreRecipe(granite, diorite, "gemQuartz"));
		}
	},

	GRIMSTONE {

		@Override
		void addBlocks() {
			BlockCarvable grimstone = (BlockGrimstone) new BlockGrimstone(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F);
			grimstone.carverHelper.addVariation("tile.grimstone.0.desc", 0, "grimstone/grimstone");
			grimstone.carverHelper.addVariation("tile.grimstone.1.desc", 1, "grimstone/smooth");
			grimstone.carverHelper.addVariation("tile.grimstone.2.desc", 2, "grimstone/hate");
			grimstone.carverHelper.addVariation("tile.grimstone.3.desc", 3, "grimstone/chiseled");
			grimstone.carverHelper.addVariation("tile.grimstone.4.desc", 4, "grimstone/blocks");
			grimstone.carverHelper.addVariation("tile.grimstone.5.desc", 5, "grimstone/blocks-rough");
			grimstone.carverHelper.addVariation("tile.grimstone.6.desc", 6, "grimstone/brick");
			grimstone.carverHelper.addVariation("tile.grimstone.7.desc", 7, "grimstone/largebricks");
			grimstone.carverHelper.addVariation("tile.grimstone.8.desc", 8, "grimstone/platform");
			grimstone.carverHelper.addVariation("tile.grimstone.9.desc", 9, "grimstone/platform-tiles");
			grimstone.carverHelper.addVariation("tile.grimstone.10.desc", 10, "grimstone/construction");
			grimstone.carverHelper.addVariation("tile.grimstone.11.desc", 11, "grimstone/fancy-tiles");
			grimstone.carverHelper.addVariation("tile.grimstone.12.desc", 12, "grimstone/plate");
			grimstone.carverHelper.addVariation("tile.grimstone.13.desc", 13, "grimstone/plate-rough");
			grimstone.carverHelper.addVariation("tile.grimstone.14.desc", 14, "grimstone/flaky");
			grimstone.carverHelper.registerAll(grimstone, "grimstone");
			Carving.chisel.registerOre("grimstone", "grimstone");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.grimstone, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.coal, 1));
		}
	},

	HEX_PLATING {

		@Override
		void addBlocks() {
            BlockCarvable hexPlating = (BlockCarvable) new BlockCarvableGlow("animations/shroud").setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10.0F);
            for (int i = 0; i < 16; i++) {
                hexPlating.carverHelper.addVariation("tile.hexPlating." + ItemDye.field_150921_b[i] + ".desc", i, "hexPlating/hexBase");
            }
            hexPlating.carverHelper.registerAll(hexPlating, "hexPlating");
            Carving.chisel.registerOre("hexPlating", "hexPlating");

            BlockCarvable hexLargePlating = (BlockCarvable) new BlockCarvableGlow("animations/shroud").setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10.0F);
            for (int i = 0; i < 16; i++) {
                hexLargePlating.carverHelper.addVariation("tile.hexPlating." + ItemDye.field_150921_b[i] + ".desc", i, "hexPlating/hexNew");
            }

            hexLargePlating.carverHelper.registerBlock(hexLargePlating, "hexLargePlating");
            hexLargePlating.carverHelper.registerVariations("hexPlating", hexLargePlating);
            Carving.chisel.registerOre("hexPlating", "hexPlating");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.hexPlating, 8, 0), "XXX", "XYX", "XXX", 'X', "stone", 'Y', "blockCoal"));
		}
	},

	HOLYSTONE {

		@Override
		void addBlocks() {
			BlockCarvable holystone = (BlockHolystone) new BlockHolystone(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Chisel.soundHolystoneFootstep);
			holystone.carverHelper.addVariation("tile.holystone.0.desc", 0, "holystone/holystone");
			holystone.carverHelper.addVariation("tile.holystone.1.desc", 1, "holystone/smooth");
			holystone.carverHelper.addVariation("tile.holystone.2.desc", 2, "holystone/love");
			holystone.carverHelper.addVariation("tile.holystone.3.desc", 3, "holystone/chiseled");
			holystone.carverHelper.addVariation("tile.holystone.4.desc", 4, "holystone/blocks");
			holystone.carverHelper.addVariation("tile.holystone.5.desc", 5, "holystone/blocks-rough");
			holystone.carverHelper.addVariation("tile.holystone.6.desc", 6, "holystone/brick");
			holystone.carverHelper.addVariation("tile.holystone.7.desc", 7, "holystone/largebricks");
			holystone.carverHelper.addVariation("tile.holystone.8.desc", 8, "holystone/platform");
			holystone.carverHelper.addVariation("tile.holystone.9.desc", 9, "holystone/platform-tiles");
			holystone.carverHelper.addVariation("tile.holystone.10.desc", 10, "holystone/construction");
			holystone.carverHelper.addVariation("tile.holystone.11.desc", 11, "holystone/fancy-tiles");
			holystone.carverHelper.addVariation("tile.holystone.12.desc", 12, "holystone/plate");
			holystone.carverHelper.addVariation("tile.holystone.13.desc", 13, "holystone/plate-rough");
			holystone.carverHelper.registerAll(holystone, "holystone");
			OreDictionary.registerOre("holystone", holystone);
			Carving.chisel.registerOre("holystone", "holystone");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.holystone, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.feather, 1));
		}
	},

	ICE {

		@Override
		void addBlocks() {
			BlockCarvableIce ice = (BlockCarvableIce) new BlockCarvableIce().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass);
			Carving.chisel.addVariation("ice", Blocks.ice, 0, 0);
			ice.carverHelper.addVariation("tile.ice.1.desc", 1, "ice/a1-ice-light");
			ice.carverHelper.addVariation("tile.ice.2.desc", 2, "ice/a1-stonecobble-icecobble");
			ice.carverHelper.addVariation("tile.ice.3.desc", 3, "ice/a1-netherbrick-ice");
			ice.carverHelper.addVariation("tile.ice.4.desc", 4, "ice/a1-stonecobble-icebrick");
			ice.carverHelper.addVariation("tile.ice.5.desc", 5, "ice/a1-stonecobble-icebricksmall");
			ice.carverHelper.addVariation("tile.ice.6.desc", 6, "ice/a1-stonecobble-icedungeon");
			ice.carverHelper.addVariation("tile.ice.7.desc", 7, "ice/a1-stonecobble-icefour");
			ice.carverHelper.addVariation("tile.ice.8.desc", 8, "ice/a1-stonecobble-icefrench");
			ice.carverHelper.addVariation("tile.ice.9.desc", 9, "ice/sunkentiles");
			ice.carverHelper.addVariation("tile.ice.10.desc", 10, "ice/tiles");
			ice.carverHelper.addVariation("tile.ice.11.desc", 11, "ice/a1-stonecobble-icepanel");
			ice.carverHelper.addVariation("tile.ice.12.desc", 12, "ice/a1-stoneslab-ice");
			ice.carverHelper.addVariation("tile.ice.13.desc", 13, "ice/zelda");
			ice.carverHelper.addVariation("tile.ice.14.desc", 14, "ice/bismuth");
			ice.carverHelper.addVariation("tile.ice.15.desc", 15, "ice/poison");
			ice.carverHelper.registerAll(ice, "ice");
			Carving.chisel.registerOre("ice", "ice");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.ice, 4, 1), "XX", "XX", 'X', new ItemStack(ChiselBlocks.ice_pillar, 1, OreDictionary.WILDCARD_VALUE));
		}
	},

	ICE_PILLAR(ICE) {

		@Override
		void addBlocks() {
			BlockCarvableIcePillar ice_pillar = (BlockCarvableIcePillar) new BlockCarvableIcePillar(Material.ice).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setLightOpacity(3)
					.setStepSound(Block.soundTypeGlass);

			ice_pillar.carverHelper.addVariation("tile.icePillar.0.desc", 0, "icepillar/plainplain");
			ice_pillar.carverHelper.addVariation("tile.icePillar.1.desc", 1, "icepillar/plaingreek");
			ice_pillar.carverHelper.addVariation("tile.icePillar.2.desc", 2, "icepillar/greekplain");
			ice_pillar.carverHelper.addVariation("tile.icePillar.3.desc", 3, "icepillar/greekgreek");
			ice_pillar.carverHelper.addVariation("tile.icePillar.4.desc", 4, "icepillar/convexplain");
			ice_pillar.carverHelper.addVariation("tile.icePillar.5.desc", 5, "icepillar/carved");
			ice_pillar.carverHelper.addVariation("tile.icePillar.6.desc", 6, "icepillar/ornamental");
			ice_pillar.carverHelper.registerAll(ice_pillar, "ice_pillar");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.ice_pillar, 6, 0), "XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.ice, 1, OreDictionary.WILDCARD_VALUE));
		}
	},

	ICE_STAIRS(ICE) {

		@Override
		void addBlocks() {
			CarvableStairsMaker makerIceStairs = new CarvableStairsMaker(Blocks.ice);

			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.0.desc", 0, Blocks.ice);
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.1.desc", 1, "ice/a1-ice-light");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.2.desc", 2, "ice/a1-stonecobble-icecobble");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.3.desc", 3, "ice/a1-netherbrick-ice");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.4.desc", 4, "ice/a1-stonecobble-icebrick");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.5.desc", 5, "ice/a1-stonecobble-icebricksmall");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.6.desc", 6, "ice/a1-stonecobble-icedungeon");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.7.desc", 7, "ice/a1-stonecobble-icefour");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.8.desc", 8, "ice/a1-stonecobble-icefrench");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.9.desc", 9, "ice/sunkentiles");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.10.desc", 10, "ice/tiles");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.11.desc", 11, "ice/a1-stonecobble-icepanel");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.12.desc", 12, "ice/a1-stoneslab-ice");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.13.desc", 13, "ice/zelda");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.14.desc", 14, "ice/bismuth");
			makerIceStairs.carverHelper.addVariation("tile.ice_stairs.15.desc", 15, "ice/poison");
			makerIceStairs.create(new IStairsCreator() {

				@Override
				public BlockCarvableStairs create(Block block, int meta, CarvableHelper helper) {
					return new BlockCarvableIceStairs(block, meta, helper);
				}
			}, "ice_stairs", ChiselBlocks.iceStairs);
			Carving.chisel.registerOre("ice_stairs", "iceStairs");
		}
	},

	IRON_BARS {

		@Override
		void addBlocks() {
			BlockCarvablePane iron_bars = (BlockCarvablePane) new BlockCarvablePane(Material.iron, true).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.3F)
					.setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("iron_bars", Blocks.iron_bars, 0, 0);
			iron_bars.carverHelper.addVariation("tile.iron_bars.1.desc", 1, "ironpane/fenceIron");
			iron_bars.carverHelper.addVariation("tile.iron_bars.2.desc", 2, "ironpane/barbedwire");
			iron_bars.carverHelper.addVariation("tile.iron_bars.3.desc", 3, "ironpane/cage");
			iron_bars.carverHelper.addVariation("tile.iron_bars.4.desc", 4, "ironpane/fenceIronTop");
			iron_bars.carverHelper.addVariation("tile.iron_bars.5.desc", 5, "ironpane/terrain-glass-thickgrid");
			iron_bars.carverHelper.addVariation("tile.iron_bars.6.desc", 6, "ironpane/terrain-glass-thingrid");
			iron_bars.carverHelper.addVariation("tile.iron_bars.7.desc", 7, "ironpane/terrain-glass-ornatesteel");
			iron_bars.carverHelper.addVariation("tile.iron_bars.8.desc", 8, "ironpane/bars");
			iron_bars.carverHelper.addVariation("tile.iron_bars.9.desc", 9, "ironpane/spikes");
			iron_bars.carverHelper.registerAll(iron_bars, "iron_bars");
			Carving.chisel.registerOre("iron_bars", "iron_bars");
		}
	},

	IRON_BLOCK {

		@Override
		void addBlocks() {

			BlockCarvable iron_block = (BlockBeaconBase) new BlockBeaconBase().setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("iron_block", Blocks.iron_block, 0, 0);
			iron_block.carverHelper.addVariation("tile.iron.1.desc", 1, "iron/terrain-iron-largeingot");
			iron_block.carverHelper.addVariation("tile.iron.2.desc", 2, "iron/terrain-iron-smallingot");
			iron_block.carverHelper.addVariation("tile.iron.3.desc", 3, "iron/terrain-iron-gears");
			iron_block.carverHelper.addVariation("tile.iron.4.desc", 4, "iron/terrain-iron-brick");
			iron_block.carverHelper.addVariation("tile.iron.5.desc", 5, "iron/terrain-iron-plates");
			iron_block.carverHelper.addVariation("tile.iron.6.desc", 6, "iron/terrain-iron-rivets");
			iron_block.carverHelper.addVariation("tile.iron.7.desc", 7, "iron/terrain-iron-coin-heads");
			iron_block.carverHelper.addVariation("tile.iron.8.desc", 8, "iron/terrain-iron-coin-tails");
			iron_block.carverHelper.addVariation("tile.iron.9.desc", 9, "iron/terrain-iron-crate-dark");
			iron_block.carverHelper.addVariation("tile.iron.10.desc", 10, "iron/terrain-iron-crate-light");
			iron_block.carverHelper.addVariation("tile.iron.11.desc", 11, "iron/terrain-iron-moon");
			iron_block.carverHelper.addVariation("tile.iron.12.desc", 12, "iron/terrain-iron-space");
			iron_block.carverHelper.addVariation("tile.iron.13.desc", 13, "iron/terrain-iron-spaceblack");
			iron_block.carverHelper.addVariation("tile.iron.14.desc", 14, "iron/terrain-iron-vents");
			iron_block.carverHelper.addVariation("tile.iron.15.desc", 15, "iron/terrain-iron-simple");

            BlockCarvable iron2 = (BlockCarvable) new BlockBeaconBase(Material.iron).setStepSound(Block.soundTypeMetal).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setHardness(5F)
                    .setResistance(10F);
            iron2.carverHelper.addVariation("tile.metalOre.0.desc", 0, "metals/iron/caution");
            iron2.carverHelper.addVariation("tile.metalOre.1.desc", 1, "metals/iron/crate");
            iron2.carverHelper.addVariation("tile.metalOre.2.desc", 2, "metals/iron/thermal");
            iron2.carverHelper.addVariation("tile.metalOre.3.desc", 3, "metals/iron/adv");
            iron2.carverHelper.addVariation("tile.metalOre.4.desc", 4, "metals/iron/egregious");
            iron2.carverHelper.addVariation("tile.metalOre.5.desc", 5, "metals/iron/bolted");
            iron2.carverHelper.registerBlock(iron2, "iron2");
            iron2.carverHelper.registerVariations("iron_block", iron2);

            iron_block.carverHelper.registerAll(iron_block, "iron_block");
			Carving.chisel.registerOre("iron_block", "blockIron");
		}
	},

	JACKOLANTERN {

		@Override
		void addBlocks() {
			for (int metadata = 0; metadata < 16; metadata++) {
				jackolantern[metadata] = (BlockCarvablePumpkin) new BlockCarvablePumpkin(true).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(1.0F).setBlockName("litpumpkin")
						.setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setLightLevel(1.0f);
				jackolantern[metadata].setInformation("pumpkin/pumpkin_face_" + (metadata + 1) + "_on");
				GameRegistry.registerBlock(jackolantern[metadata], ItemCarvablePumpkin.class, ("jackolantern" + (metadata + 1)));
				Carving.chisel.addVariation("jackolantern", jackolantern[metadata], 0, (metadata + 1));
			}
			Carving.chisel.addVariation("jackolantern", Blocks.lit_pumpkin, 0, 0);
			Carving.chisel.registerOre("jackolantern", "jackolantern");
		}
	},

	LABORATORY {

		@Override
		void addBlocks() {
			BlockCarvable laboratoryblock = (BlockCarvable) new BlockCarvable(Material.iron).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Chisel.soundMetalFootstep);

			laboratoryblock.carverHelper.addVariation("tile.laboratory.0.desc", 0, "laboratory/wallpanel");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.1.desc", 1, "laboratory/dottedpanel");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.2.desc", 2, "laboratory/largewall");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.3.desc", 3, "laboratory/roundel");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.4.desc", 4, "laboratory/wallvents");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.5.desc", 5, "laboratory/largetile");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.6.desc", 6, "laboratory/smalltile");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.7.desc", 7, "laboratory/floortile");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.8.desc", 8, "laboratory/checkertile");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.9.desc", 9, "laboratory/clearscreen");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.10.desc", 10, "laboratory/fuzzscreen");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.11.desc", 11, "laboratory/largesteel");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.12.desc", 12, "laboratory/smallsteel");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.13.desc", 13, "laboratory/directionright");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.14.desc", 14, "laboratory/directionleft");
			laboratoryblock.carverHelper.addVariation("tile.laboratory.15.desc", 15, "laboratory/infocon");
			laboratoryblock.carverHelper.registerAll(laboratoryblock, "laboratoryblock");
			Carving.chisel.registerOre("laboratoryblock", "laboratoryblock");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.laboratoryblock, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.quartz, 1));
		}
	},

	LAPIS_BLOCK {

		@Override
		void addBlocks() {
			BlockCarvable lapis_block = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(3F).setResistance(5F)
					.setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("lapis_block", Blocks.lapis_block, 0, 0);
			lapis_block.carverHelper.addVariation("tile.lapis.1.desc", 1, "lapis/terrain-lapisblock-chunky");
			lapis_block.carverHelper.addVariation("tile.lapis.2.desc", 2, "lapis/terrain-lapisblock-panel");
			lapis_block.carverHelper.addVariation("tile.lapis.3.desc", 3, "lapis/terrain-lapisblock-zelda");
			lapis_block.carverHelper.addVariation("tile.lapis.4.desc", 4, "lapis/terrain-lapisornate");
			lapis_block.carverHelper.addVariation("tile.lapis.5.desc", 5, "lapis/terrain-lapistile");
			lapis_block.carverHelper.addVariation("tile.lapis.6.desc", 6, "lapis/a1-blocklapis-panel");
			lapis_block.carverHelper.addVariation("tile.lapis.7.desc", 7, "lapis/a1-blocklapis-smooth");
			lapis_block.carverHelper.addVariation("tile.lapis.8.desc", 8, "lapis/a1-blocklapis-ornatelayer");

            lapis_block.carverHelper.registerAll(lapis_block, "lapis_block");
			Carving.chisel.registerOre("lapis_block", "lapis");
		}
	},

	LAVASTONE {

		@Override
		void addBlocks() {
			BlockLavastone lavastone = (BlockLavastone) new BlockLavastone(Material.rock, "lava_flow").setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F);
			lavastone.carverHelper.addVariation("tile.lavastone.0.desc", 0, "lavastone/cobble");
			lavastone.carverHelper.addVariation("tile.lavastone.1.desc", 1, "lavastone/black");
			lavastone.carverHelper.addVariation("tile.lavastone.2.desc", 2, "lavastone/tiles");
			lavastone.carverHelper.addVariation("tile.lavastone.3.desc", 3, "lavastone/chaotic");
			lavastone.carverHelper.addVariation("tile.lavastone.4.desc", 4, "lavastone/creeper");
			lavastone.carverHelper.addVariation("tile.lavastone.5.desc", 5, "lavastone/panel");
			lavastone.carverHelper.addVariation("tile.lavastone.6.desc", 6, "lavastone/panel-ornate");
			lavastone.carverHelper.addVariation("tile.lavastone.7.desc", 7, "lavastone/dark");
			lavastone.carverHelper.registerAll(lavastone, "lavastone");
			OreDictionary.registerOre("lavastone", lavastone);
			Carving.chisel.registerOre("lavastone", "lavastone");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.lavastone, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.lava_bucket, 1));
		}
	},

	LEAD {

		@Override
		void addBlocks() {
			BlockCarvable lead = (BlockCarvable) new BlockBeaconBase(Material.iron).setStepSound(Block.soundTypeMetal).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setHardness(5F)
					.setResistance(10F);
			lead.carverHelper.addVariation("tile.metalOre.0.desc", 0, "metals/lead/caution");
			lead.carverHelper.addVariation("tile.metalOre.1.desc", 1, "metals/lead/crate");
			lead.carverHelper.addVariation("tile.metalOre.2.desc", 2, "metals/lead/thermal");
			lead.carverHelper.addVariation("tile.metalOre.3.desc", 3, "metals/lead/adv");
			lead.carverHelper.addVariation("tile.metalOre.4.desc", 4, "metals/lead/egregious");
			lead.carverHelper.addVariation("tile.metalOre.5.desc", 5, "metals/lead/bolted");
			lead.carverHelper.registerAll(lead, "leadblock");
			Carving.chisel.registerOre("leadblock", "blockLead");
		}
	},

	LEAVES {

		@Override
		void addBlocks() {
			BlockLeaf leaves = (BlockLeaf) new BlockLeaf(Material.leaves).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.2F).setStepSound(Block.soundTypeGrass);
			Carving.chisel.addGroup(new SimpleCarvingGroup("leaves") {

				public List<ICarvingVariation> getVariations() {
					if (Configurations.chiselBackToVanillaLeaves) {
						return super.getVariations();
					} else {
						List<ICarvingVariation> ret = Lists.newArrayList();
						for (ICarvingVariation v : super.getVariations()) {
							if (v.getBlock() != Blocks.leaves && v.getBlock() != Blocks.leaves2) {
								ret.add(v);
							}
						}
						return ret;
					}
				}
			});
			Carving.chisel.addVariation("leaves", Blocks.leaves, 0, 0);
			Carving.chisel.addVariation("leaves", Blocks.leaves, 1, 0);
			Carving.chisel.addVariation("leaves", Blocks.leaves, 2, 0);
			Carving.chisel.addVariation("leaves", Blocks.leaves, 3, 0);
			Carving.chisel.addVariation("leaves", Blocks.leaves2, 0, 0);
			Carving.chisel.addVariation("leaves", Blocks.leaves2, 1, 0);
			if (Configurations.fancy) {
				leaves.carverHelper.addVariation("tile.leaves.6.desc", 6, "leaves/dead");
				leaves.carverHelper.addVariation("tile.leaves.7.desc", 7, "leaves/fancy");
				leaves.carverHelper.addVariation("tile.leaves.8.desc", 8, "leaves/pinkpetal");
				leaves.carverHelper.addVariation("tile.leaves.9.desc", 9, "leaves/red_roses");
				leaves.carverHelper.addVariation("tile.leaves.10.desc", 10, "leaves/roses");
				leaves.carverHelper.addVariation("tile.leaves.11.desc", 11, "leaves/christmasBalls");
				leaves.carverHelper.addVariation("tile.leaves.12.desc", 12, "leaves/christmasLights");
			} else {
				leaves.carverHelper.addVariation("tile.leaves.6.desc", 6, "leaves/dead_opaque");
				leaves.carverHelper.addVariation("tile.leaves.7.desc", 7, "leaves/fancy_opaque");
				leaves.carverHelper.addVariation("tile.leaves.8.desc", 8, "leaves/pinkpetal_opaque");
				leaves.carverHelper.addVariation("tile.leaves.9.desc", 9, "leaves/red_roses_opaque");
				leaves.carverHelper.addVariation("tile.leaves.10.desc", 10, "leaves/roses_opaque");
				leaves.carverHelper.addVariation("tile.leaves.11.desc", 11, "leaves/christmasBalls_opaque");
				leaves.carverHelper.addVariation("tile.leaves.12.desc", 12, "leaves/christmasLights_opaque");
			}

			leaves.carverHelper.registerAll(leaves, "leaves");
			Carving.chisel.registerOre("leaves", "leaves");
		}
	},

	LIMESTONE {

		@Override
		void addBlocks() {
			BlockCarvable limestone = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Block.soundTypeStone);

			limestone.carverHelper.addVariation("tile.limestone.0.desc", 0, "limestone");
			limestone.carverHelper.addVariation("tile.limestone.1.desc", 1, "limestone/terrain-cobbsmalltilelight");
			limestone.carverHelper.addVariation("tile.limestone.2.desc", 2, "limestone/terrain-cob-frenchlight");
			limestone.carverHelper.addVariation("tile.limestone.3.desc", 3, "limestone/terrain-cob-french2light");
			limestone.carverHelper.addVariation("tile.limestone.4.desc", 4, "limestone/terrain-cobmoss-creepdungeonlight");
			limestone.carverHelper.addVariation("tile.limestone.5.desc", 5, "limestone/terrain-cob-smallbricklight");
			limestone.carverHelper.addVariation("tile.limestone.6.desc", 6, "limestone/terrain-mossysmalltilelight");
			limestone.carverHelper.addVariation("tile.limestone.7.desc", 7, "limestone/terrain-pistonback-dungeon");
			limestone.carverHelper.addVariation("tile.limestone.8.desc", 8, "limestone/terrain-pistonback-dungeonornate");
			limestone.carverHelper.addVariation("tile.limestone.9.desc", 9, "limestone/terrain-pistonback-dungeonvent");
			limestone.carverHelper.addVariation("tile.limestone.10.desc", 10, "limestone/terrain-pistonback-lightcreeper");
			limestone.carverHelper.addVariation("tile.limestone.11.desc", 11, "limestone/terrain-pistonback-lightdent");
			limestone.carverHelper.addVariation("tile.limestone.12.desc", 12, "limestone/terrain-pistonback-lightemboss");
			limestone.carverHelper.addVariation("tile.limestone.13.desc", 13, "limestone/terrain-pistonback-lightfour");
			limestone.carverHelper.addVariation("tile.limestone.14.desc", 14, "limestone/terrain-pistonback-lightmarker");
			limestone.carverHelper.addVariation("tile.limestone.15.desc", 15, "limestone/terrain-pistonback-lightpanel");
			limestone.carverHelper.registerAll(limestone, "limestone");
			OreDictionary.registerOre("limestone", limestone);
			Carving.chisel.registerOre("limestone", "limestone");

			BlockCarvableSlab limestone_slab = (BlockCarvableSlab) new BlockCarvableSlab(limestone).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F);

			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.0.desc", 0, "limestone");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.1.desc", 1, "limestone/terrain-cobbsmalltilelight");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.2.desc", 2, "limestone/terrain-cob-frenchlight");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.3.desc", 3, "limestone/terrain-cob-french2light");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.4.desc", 4, "limestone/terrain-cobmoss-creepdungeonlight");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.5.desc", 5, "limestone/terrain-cob-smallbricklight");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.6.desc", 6, "limestone/terrain-mossysmalltilelight");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.7.desc", 7, "limestone/terrain-pistonback-dungeon");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.8.desc", 8, "limestone/terrain-pistonback-dungeonornate");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.9.desc", 9, "limestone/terrain-pistonback-dungeonvent");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.10.desc", 10, "limestone/terrain-pistonback-lightcreeper");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.11.desc", 11, "limestone/terrain-pistonback-lightdent");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.12.desc", 12, "limestone/terrain-pistonback-lightemboss");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.13.desc", 13, "limestone/terrain-pistonback-lightfour");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.14.desc", 14, "limestone/terrain-pistonback-lightmarker");
			limestone_slab.carverHelper.addVariation("tile.limestoneSlab.15.desc", 15, "limestone/terrain-pistonback-lightpanel");
			limestone_slab.carverHelper.registerAll(limestone_slab, "limestone_slab", ItemCarvableSlab.class);
			registerSlabTop(limestone_slab, limestone_slab.top);
			Carving.chisel.registerOre("limestone_slab", "limestone_slab");

			CarvableStairsMaker makerLimestoneStairs = new CarvableStairsMaker(limestone);

			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.0.desc", 0, "limestone");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.1.desc", 1, "limestone/terrain-cobbsmalltilelight");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.2.desc", 2, "limestone/terrain-cob-frenchlight");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.3.desc", 3, "limestone/terrain-cob-french2light");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.4.desc", 4, "limestone/terrain-cobmoss-creepdungeonlight");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.5.desc", 5, "limestone/terrain-cob-smallbricklight");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.6.desc", 6, "limestone/terrain-mossysmalltilelight");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.7.desc", 7, "limestone/terrain-pistonback-dungeon");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.8.desc", 8, "limestone/terrain-pistonback-dungeonornate");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.9.desc", 9, "limestone/terrain-pistonback-dungeonvent");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.10.desc", 10, "limestone/terrain-pistonback-lightcreeper");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.11.desc", 11, "limestone/terrain-pistonback-lightdent");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.12.desc", 12, "limestone/terrain-pistonback-lightemboss");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.13.desc", 13, "limestone/terrain-pistonback-lightfour");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.14.desc", 14, "limestone/terrain-pistonback-lightmarker");
			makerLimestoneStairs.carverHelper.addVariation("tile.limestoneStairs.15.desc", 15, "limestone/terrain-pistonback-lightpanel");
			makerLimestoneStairs.create("limestone_stairs", ChiselBlocks.limestoneStairs);
			Carving.chisel.registerOre("limestone_stairs", "limestone_stairs");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.limestone_slab, 6, 0), "***", '*', new ItemStack(ChiselBlocks.limestone, 1, OreDictionary.WILDCARD_VALUE));
		}
	},

	MARBLE {

		@Override
		void addBlocks() {
			BlockCarvable marble = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Block.soundTypeStone);

			marble.carverHelper.addVariation("tile.marble.0.desc", 0, "marble");
			marble.carverHelper.addVariation("tile.marble.1.desc", 1, "marble/a1-stoneornamental-marblebrick");
			marble.carverHelper.addVariation("tile.marble.2.desc", 2, "marble/a1-stoneornamental-marbleclassicpanel");
			marble.carverHelper.addVariation("tile.marble.3.desc", 3, "marble/a1-stoneornamental-marbleornate");
			marble.carverHelper.addVariation("tile.marble.4.desc", 4, "marble/panel");
			marble.carverHelper.addVariation("tile.marble.5.desc", 5, "marble/block");
			marble.carverHelper.addVariation("tile.marble.6.desc", 6, "marble/terrain-pistonback-marblecreeperdark");
			marble.carverHelper.addVariation("tile.marble.7.desc", 7, "marble/terrain-pistonback-marblecreeperlight");
			marble.carverHelper.addVariation("tile.marble.8.desc", 8, "marble/a1-stoneornamental-marblecarved");
			marble.carverHelper.addVariation("tile.marble.9.desc", 9, "marble/a1-stoneornamental-marblecarvedradial");
			marble.carverHelper.addVariation("tile.marble.10.desc", 10, "marble/terrain-pistonback-marbledent");
			marble.carverHelper.addVariation("tile.marble.11.desc", 11, "marble/terrain-pistonback-marbledent-small");
			marble.carverHelper.addVariation("tile.marble.12.desc", 12, "marble/marble-bricks");
			marble.carverHelper.addVariation("tile.marble.13.desc", 13, "marble/marble-arranged-bricks");
			marble.carverHelper.addVariation("tile.marble.14.desc", 14, "marble/marble-fancy-bricks");
			marble.carverHelper.addVariation("tile.marble.15.desc", 15, "marble/marble-blocks");
			marble.carverHelper.registerAll(marble, "marble");
			OreDictionary.registerOre("marble", marble);
			OreDictionary.registerOre("blockMarble", marble);
			Carving.chisel.registerOre("marble", "marble");

			BlockCarvableSlab marble_slab = (BlockCarvableSlab) new BlockCarvableSlab(marble).setHardness(2.0F).setResistance(10F);

			marble_slab.carverHelper.addVariation("tile.marbleSlab.0.desc", 0, "marble");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.1.desc", 1, "marbleslab/a1-stoneornamental-marblebrick");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.2.desc", 2, "marbleslab/a1-stoneornamental-marbleclassicpanel");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.3.desc", 3, "marbleslab/a1-stoneornamental-marbleornate");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.4.desc", 4, "marbleslab/a1-stoneornamental-marblepanel");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.5.desc", 5, "marbleslab/terrain-pistonback-marble");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.6.desc", 6, "marbleslab/terrain-pistonback-marblecreeperdark");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.7.desc", 7, "marbleslab/terrain-pistonback-marblecreeperlight");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.8.desc", 8, "marbleslab/a1-stoneornamental-marblecarved");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.9.desc", 9, "marbleslab/a1-stoneornamental-marblecarvedradial");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.10.desc", 10, "marbleslab/terrain-pistonback-marbledent");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.11.desc", 11, "marbleslab/terrain-pistonback-marbledent-small");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.12.desc", 12, "marbleslab/marble-bricks");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.13.desc", 13, new SubmapManagerSlab("marbleslab/marble-arranged-bricks"));
			marble_slab.carverHelper.addVariation("tile.marbleSlab.14.desc", 14, "marbleslab/marble-fancy-bricks");
			marble_slab.carverHelper.addVariation("tile.marbleSlab.15.desc", 15, "marbleslab/marble-blocks");
			marble_slab.carverHelper.registerAll(marble_slab, "marble_slab", ItemCarvableSlab.class);
			registerSlabTop(marble_slab, marble_slab.top);
			Carving.chisel.registerOre("marble_slab", "marble_slab");

			CarvableStairsMaker makerMarbleStairs = new CarvableStairsMaker(marble);

			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.0.desc", 0, "marble");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.6.desc", 1, "marbleslab/a1-stoneornamental-marblebrick");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.2.desc", 2, "marbleslab/a1-stoneornamental-marbleclassicpanel");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.3.desc", 3, "marbleslab/a1-stoneornamental-marbleornate");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.4.desc", 4, "marbleslab/a1-stoneornamental-marblepanel");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.5.desc", 5, "marbleslab/terrain-pistonback-marble");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.6.desc", 6, "marbleslab/terrain-pistonback-marblecreeperdark");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.7.desc", 7, "marbleslab/terrain-pistonback-marblecreeperlight");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.8.desc", 8, "marbleslab/a1-stoneornamental-marblecarved");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.9.desc", 9, "marbleslab/a1-stoneornamental-marblecarvedradial");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.10.desc", 10, "marbleslab/terrain-pistonback-marbledent");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.11.desc", 11, "marbleslab/terrain-pistonback-marbledent-small");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.12.desc", 12, "marbleslab/marble-bricks");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.13.desc", 13, "marbleslab/marble-arranged-bricks");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.14.desc", 14, "marbleslab/marble-fancy-bricks");
			makerMarbleStairs.carverHelper.addVariation("tile.marbleStairs.15.desc", 15, "marbleslab/marble-blocks");
			makerMarbleStairs.create("marble_stairs", ChiselBlocks.marbleStairs);
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.marble_slab, 6, 0), "***", '*', new ItemStack(ChiselBlocks.marble, 1, OreDictionary.WILDCARD_VALUE));
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.marble, 4), "XX", "XX", 'X', new ItemStack(ChiselBlocks.marble_pillar, 1, OreDictionary.WILDCARD_VALUE));
		}
	},

	MARBLE_PILLAR(MARBLE) {

		@Override
		void addBlocks() {

			BlockCarvable marble_pillar;
			if (Configurations.oldPillars) {
				marble_pillar = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F)
						.setStepSound(Block.soundTypeStone);

				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.0.desc", 0, "marblepillarold/column");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.1.desc", 1, "marblepillarold/capstone");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.2.desc", 2, "marblepillarold/base");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.3.desc", 3, "marblepillarold/small");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.4.desc", 4, "marblepillarold/pillar-carved");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.5.desc", 5, "marblepillarold/a1-stoneornamental-marblegreek");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.6.desc", 6, "marblepillarold/a1-stonepillar-greek");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.7.desc", 7, "marblepillarold/a1-stonepillar-plain");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.8.desc", 8, "marblepillarold/a1-stonepillar-greektopplain");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.9.desc", 9, "marblepillarold/a1-stonepillar-plaintopplain");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.10.desc", 10, "marblepillarold/a1-stonepillar-greekbottomplain");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.11.desc", 11, "marblepillarold/a1-stonepillar-plainbottomplain");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.12.desc", 12, "marblepillarold/a1-stonepillar-greektopgreek");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.13.desc", 13, "marblepillarold/a1-stonepillar-plaintopgreek");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.14.desc", 14, "marblepillarold/a1-stonepillar-greekbottomgreek");
				marble_pillar.carverHelper.addVariation("tile.marblePillarOld.15.desc", 15, "marblepillarold/a1-stonepillar-plainbottomgreek");
			} else {
				marble_pillar = (BlockCarvable) new BlockCarvablePillar(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F)
						.setStepSound(Block.soundTypeStone);

				marble_pillar.carverHelper.addVariation("tile.marblePillar.0.desc", 0, "marblepillar/pillar");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.1.desc", 1, "marblepillar/default");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.2.desc", 2, "marblepillar/simple");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.3.desc", 3, "marblepillar/convex");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.4.desc", 4, "marblepillar/rough");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.5.desc", 5, "marblepillar/greekdecor");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.6.desc", 6, "marblepillar/greekgreek");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.7.desc", 7, "marblepillar/greekplain");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.8.desc", 8, "marblepillar/plaindecor");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.9.desc", 9, "marblepillar/plaingreek");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.10.desc", 10, "marblepillar/plainplain");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.11.desc", 11, "marblepillar/widedecor");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.12.desc", 12, "marblepillar/widegreek");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.13.desc", 13, "marblepillar/wideplain");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.14.desc", 14, "marblepillar/carved");
				marble_pillar.carverHelper.addVariation("tile.marblePillar.15.desc", 15, "marblepillar/ornamental");
			}
			marble_pillar.carverHelper.registerAll(marble_pillar, "marble_pillar");

			BlockCarvableSlab marble_pillar_slab = (BlockCarvableSlab) new BlockCarvableSlab(marble_pillar).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Block.soundTypeStone);

			if (Configurations.oldPillars) {
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.0.desc", 0, "marblepillarslabold/column");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.1.desc", 1, "marblepillarslabold/capstone");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.2.desc", 2, "marblepillarslabold/base");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.3.desc", 3, "marblepillarslabold/small");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.4.desc", 4, "marblepillarslabold/pillar-carved");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.5.desc", 5, "marblepillarslabold/a1-stoneornamental-marblegreek");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.6.desc", 6, "marblepillarslabold/a1-stonepillar-greek");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.7.desc", 7, "marblepillarslabold/a1-stonepillar-plain");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.8.desc", 8, "marblepillarslabold/a1-stonepillar-greektopplain");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.9.desc", 9, "marblepillarslabold/a1-stonepillar-plaintopplain");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.10.desc", 10, "marblepillarslabold/a1-stonepillar-greekbottomplain");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.11.desc", 11, "marblepillarslabold/a1-stonepillar-plainbottomplain");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.12.desc", 12, "marblepillarslabold/a1-stonepillar-greektopgreek");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.13.desc", 13, "marblepillarslabold/a1-stonepillar-plaintopgreek");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.14.desc", 14, "marblepillarslabold/a1-stonepillar-greekbottomgreek");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlabOld.15.desc", 15, "marblepillarslabold/a1-stonepillar-plainbottomgreek");
			} else {
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.0.desc", 0, "marblepillarslab/pillar");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.1.desc", 1, "marblepillarslab/default");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.2.desc", 2, "marblepillarslab/simple");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.3.desc", 3, "marblepillarslab/convex");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.4.desc", 4, "marblepillarslab/rough");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.5.desc", 5, "marblepillarslab/greekdecor");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.6.desc", 6, "marblepillarslab/greekgreek");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.7.desc", 7, "marblepillarslab/greekplain");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.8.desc", 8, "marblepillarslab/plaindecor");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.9.desc", 9, "marblepillarslab/plaingreek");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.10.desc", 10, "marblepillarslab/plainplain");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.11.desc", 11, "marblepillarslab/widedecor");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.12.desc", 12, "marblepillarslab/widegreek");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.13.desc", 13, "marblepillarslab/wideplain");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.14.desc", 14, "marblepillarslab/carved");
				marble_pillar_slab.carverHelper.addVariation("tile.marblePillarSlab.15.desc", 15, "marblepillarslab/ornamental");
			}
			marble_pillar_slab.carverHelper.registerAll(marble_pillar_slab, "marble_pillar_slab", ItemCarvableSlab.class);
			registerSlabTop(marble_pillar_slab, marble_pillar_slab.top);
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.marble_pillar, 6), "XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.marble, 1, OreDictionary.WILDCARD_VALUE));
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.marble_pillar_slab, 6, 0), "***", '*', new ItemStack(ChiselBlocks.marble_pillar, 1, OreDictionary.WILDCARD_VALUE));
		}
	},

    NATION {

        @Override
        void addBlocks() {
            BlockCarvable imperial = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10F);
            imperial.carverHelper.addVariation("tile.nation.0.desc", 0, "nation/imperialCamo");
            imperial.carverHelper.addVariation("tile.nation.1.desc", 1, "nation/imperialCamoSecluded");
            imperial.carverHelper.addVariation("tile.nation.2.desc", 2, "nation/imperialPlate");
            imperial.carverHelper.addVariation("tile.nation.3.desc", 3, "nation/imperialCautionWhite");
            imperial.carverHelper.addVariation("tile.nation.4.desc", 4, "nation/imperialCautionOrange");
            //imperial.carverHelper.registerBlock(imperial, "imperial");
            imperial.carverHelper.registerAll(imperial, "nation");

            BlockCarvable rebel = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10F);
            rebel.carverHelper.addVariation("tile.nation.0.desc", 0, "nation/rebelCamo");
            rebel.carverHelper.addVariation("tile.nation.1.desc", 1, "nation/rebelCamoSecluded");
            rebel.carverHelper.addVariation("tile.nation.2.desc", 2, "nation/rebelPlate");
            rebel.carverHelper.addVariation("tile.nation.3.desc", 3, "nation/rebelCautionWhite");
            rebel.carverHelper.addVariation("tile.nation.4.desc", 4, "nation/rebelCautionRed");
            rebel.carverHelper.registerBlock(rebel, "rebel");
            rebel.carverHelper.registerVariations("nation", rebel);

            Carving.chisel.registerOre("nation", "nation");
        }

        @Override
        void addRecipes() {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.nation, 32, 0), new Object[] { "xyx", "yzy", "xyx", 'x', "stone", 'y',
                    Items.iron_ingot, 'z', Items.gold_nugget }));
        }
    },

	NETHER_BRICK {

		@Override
		void addBlocks() {
			BlockCarvable nether_brick = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10.0F)
					.setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("nether_brick", Blocks.nether_brick, 0, 0);
			// netherBrick.carverHelper.addVariation("Nether brick", 0,
			// Blocks.nether_brick);
			nether_brick.carverHelper.addVariation("tile.netherBrick.1.desc", 1, "netherbrick/a1-netherbrick-brinstar");
			nether_brick.carverHelper.addVariation("tile.netherBrick.2.desc", 2, "netherbrick/a1-netherbrick-classicspatter");
			nether_brick.carverHelper.addVariation("tile.netherBrick.3.desc", 3, "netherbrick/a1-netherbrick-guts");
			nether_brick.carverHelper.addVariation("tile.netherBrick.4.desc", 4, "netherbrick/a1-netherbrick-gutsdark");
			nether_brick.carverHelper.addVariation("tile.netherBrick.5.desc", 5, "netherbrick/a1-netherbrick-gutssmall");
			nether_brick.carverHelper.addVariation("tile.netherBrick.6.desc", 6, "netherbrick/a1-netherbrick-lavabrinstar");
			nether_brick.carverHelper.addVariation("tile.netherBrick.7.desc", 7, "netherbrick/a1-netherbrick-lavabrown");
			nether_brick.carverHelper.addVariation("tile.netherBrick.8.desc", 8, "netherbrick/a1-netherbrick-lavaobsidian");
			nether_brick.carverHelper.addVariation("tile.netherBrick.9.desc", 9, "netherbrick/a1-netherbrick-lavastonedark");
			nether_brick.carverHelper.addVariation("tile.netherBrick.10.desc", 10, "netherbrick/a1-netherbrick-meat");
			nether_brick.carverHelper.addVariation("tile.netherBrick.11.desc", 11, "netherbrick/a1-netherbrick-meatred");
			nether_brick.carverHelper.addVariation("tile.netherBrick.12.desc", 12, "netherbrick/a1-netherbrick-meatredsmall");
			nether_brick.carverHelper.addVariation("tile.netherBrick.13.desc", 13, "netherbrick/a1-netherbrick-meatsmall");
			nether_brick.carverHelper.addVariation("tile.netherBrick.14.desc", 14, "netherbrick/a1-netherbrick-red");
			nether_brick.carverHelper.addVariation("tile.netherBrick.15.desc", 15, "netherbrick/a1-netherbrick-redsmall");
			nether_brick.carverHelper.registerAll(nether_brick, "nether_brick");
			Carving.chisel.registerOre("nether_brick", "nether_brick");
		}
	},

	NETHER_RACK {

		@Override
		void addBlocks() {

			BlockCarvable netherrack = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(0.4F).setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("netherrack", Blocks.netherrack, 0, 0);
			netherrack.carverHelper.addVariation("tile.hellrock.1.desc", 1, "netherrack/a1-netherrack-bloodgravel");
			netherrack.carverHelper.addVariation("tile.hellrock.2.desc", 2, "netherrack/a1-netherrack-bloodrock");
			netherrack.carverHelper.addVariation("tile.hellrock.3.desc", 3, "netherrack/a1-netherrack-bloodrockgrey");
			netherrack.carverHelper.addVariation("tile.hellrock.4.desc", 4, "netherrack/a1-netherrack-brinstar");
			netherrack.carverHelper.addVariation("tile.hellrock.5.desc", 5, "netherrack/a1-netherrack-brinstarshale");
			netherrack.carverHelper.addVariation("tile.hellrock.6.desc", 6, "netherrack/a1-netherrack-classic");
			netherrack.carverHelper.addVariation("tile.hellrock.7.desc", 7, "netherrack/a1-netherrack-classicspatter");
			netherrack.carverHelper.addVariation("tile.hellrock.8.desc", 8, "netherrack/a1-netherrack-guts");
			netherrack.carverHelper.addVariation("tile.hellrock.9.desc", 9, "netherrack/a1-netherrack-gutsdark");
			netherrack.carverHelper.addVariation("tile.hellrock.10.desc", 10, "netherrack/a1-netherrack-meat");
			netherrack.carverHelper.addVariation("tile.hellrock.11.desc", 11, "netherrack/a1-netherrack-meatred");
			netherrack.carverHelper.addVariation("tile.hellrock.12.desc", 12, "netherrack/a1-netherrack-meatrock");
			netherrack.carverHelper.addVariation("tile.hellrock.13.desc", 13, "netherrack/a1-netherrack-red");
			netherrack.carverHelper.addVariation("tile.hellrock.14.desc", 14, "netherrack/a1-netherrack-wells");

            netherrack.carverHelper.registerAll(netherrack, "netherrack");
			Carving.chisel.registerOre("netherrack", "netherrack");
		}
	},

	OBSIDIAN {

		@Override
		void addBlocks() {
			BlockCarvable obsidian = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(50.0F).setResistance(2000.0F)
					.setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("obsidian", Blocks.obsidian, 0, 0);
			obsidian.carverHelper.addVariation("tile.obsidian.1.desc", 1, "obsidian/pillar");
			obsidian.carverHelper.addVariation("tile.obsidian.2.desc", 2, "obsidian/pillar-quartz");
			obsidian.carverHelper.addVariation("tile.obsidian.3.desc", 3, "obsidian/chiseled");
			obsidian.carverHelper.addVariation("tile.obsidian.4.desc", 4, "obsidian/panel-shiny");
			obsidian.carverHelper.addVariation("tile.obsidian.5.desc", 5, "obsidian/panel");
			obsidian.carverHelper.addVariation("tile.obsidian.6.desc", 6, "obsidian/chunks");
			obsidian.carverHelper.addVariation("tile.obsidian.7.desc", 7, "obsidian/growth");
			obsidian.carverHelper.addVariation("tile.obsidian.8.desc", 8, "obsidian/crystal");
			obsidian.carverHelper.addVariation("tile.obsidian.9.desc", 9, "obsidian/map-a");
			obsidian.carverHelper.addVariation("tile.obsidian.10.desc", 10, "obsidian/map-b");
			obsidian.carverHelper.addVariation("tile.obsidian.11.desc", 11, "obsidian/panel-light");
			obsidian.carverHelper.addVariation("tile.obsidian.12.desc", 12, "obsidian/blocks");
			obsidian.carverHelper.addVariation("tile.obsidian.13.desc", 13, "obsidian/tiles");
			obsidian.carverHelper.addVariation("tile.obsidian.14.desc", 14, "obsidian/greek");
			obsidian.carverHelper.addVariation("tile.obsidian.15.desc", 15, "obsidian/crate");

            obsidian.carverHelper.registerAll(obsidian, "obsidian");
			Carving.chisel.registerOre("obsidian", "obsidian");
		}
	},

	PACKEDICE {

		@Override
		void addBlocks() {
			BlockCarvablePackedIce packedice = (BlockCarvablePackedIce) new BlockCarvablePackedIce().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setLightOpacity(3)
					.setStepSound(Block.soundTypeGlass);
			Carving.chisel.addVariation("packedice", Blocks.packed_ice, 0, 0);
			packedice.carverHelper.addVariation("tile.packedice.1.desc", 1, "ice/a1-ice-light");
			packedice.carverHelper.addVariation("tile.packedice.2.desc", 2, "ice/a1-stonecobble-icecobble");
			packedice.carverHelper.addVariation("tile.packedice.3.desc", 3, "ice/a1-netherbrick-ice");
			packedice.carverHelper.addVariation("tile.packedice.4.desc", 4, "ice/a1-stonecobble-icebrick");
			packedice.carverHelper.addVariation("tile.packedice.5.desc", 5, "ice/a1-stonecobble-icebricksmall");
			packedice.carverHelper.addVariation("tile.packedice.6.desc", 6, "ice/a1-stonecobble-icedungeon");
			packedice.carverHelper.addVariation("tile.packedice.7.desc", 7, "ice/a1-stonecobble-icefour");
			packedice.carverHelper.addVariation("tile.packedice.8.desc", 8, "ice/a1-stonecobble-icefrench");
			packedice.carverHelper.addVariation("tile.packedice.9.desc", 9, "ice/sunkentiles");
			packedice.carverHelper.addVariation("tile.packedice.10.desc", 10, "ice/tiles");
			packedice.carverHelper.addVariation("tile.packedice.11.desc", 11, "ice/a1-stonecobble-icepanel");
			packedice.carverHelper.addVariation("tile.packedice.12.desc", 12, "ice/a1-stoneslab-ice");
			packedice.carverHelper.addVariation("tile.packedice.13.desc", 13, "ice/zelda");
			packedice.carverHelper.addVariation("tile.packedice.14.desc", 14, "ice/bismuth");
			packedice.carverHelper.addVariation("tile.packedice.15.desc", 15, "ice/poison");
			packedice.carverHelper.registerAll(packedice, "packedice");
			Carving.chisel.registerOre("packedice", "packedice");
		}
		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.packedice_pillar, 6, 0), "XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.packedice, 1, OreDictionary.WILDCARD_VALUE));
		}
	},

	PACKEDICE_PILLAR {

		@Override
		void addBlocks() {
			BlockCarvablePackedIcePillar packedice_pillar = (BlockCarvablePackedIcePillar) new BlockCarvablePackedIcePillar(Material.ice).setCreativeTab(ChiselTabs.tabOtherChiselBlocks)
					.setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass);

			packedice_pillar.carverHelper.addVariation("tile.packedice_pillar.0.desc", 0, "icepillar/plainplain");
			packedice_pillar.carverHelper.addVariation("tile.packedice_pillar.1.desc", 1, "icepillar/plaingreek");
			packedice_pillar.carverHelper.addVariation("tile.packedice_pillar.2.desc", 2, "icepillar/greekplain");
			packedice_pillar.carverHelper.addVariation("tile.packedice_pillar.3.desc", 3, "icepillar/greekgreek");
			packedice_pillar.carverHelper.addVariation("tile.packedice_pillar.4.desc", 4, "icepillar/convexplain");
			packedice_pillar.carverHelper.addVariation("tile.packedice_pillar.5.desc", 5, "icepillar/carved");
			packedice_pillar.carverHelper.addVariation("tile.packedice_pillar.6.desc", 6, "icepillar/ornamental");
			packedice_pillar.carverHelper.registerAll(packedice_pillar, "packedice_pillar");
		}
	},

	PACKEDICE_STAIRS {

		@Override
		void addBlocks() {
			CarvableStairsMaker makerPackedIceStairs = new CarvableStairsMaker(Blocks.packed_ice);

			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.0.desc", 0, Blocks.packed_ice);
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.1.desc", 1, "ice/a1-ice-light");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.2.desc", 2, "ice/a1-stonecobble-icecobble");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.3.desc", 3, "ice/a1-netherbrick-ice");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.4.desc", 4, "ice/a1-stonecobble-icebrick");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.5.desc", 5, "ice/a1-stonecobble-icebricksmall");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.6.desc", 6, "ice/a1-stonecobble-icedungeon");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.7.desc", 7, "ice/a1-stonecobble-icefour");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.8.desc", 8, "ice/a1-stonecobble-icefrench");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.9.desc", 9, "ice/sunkentiles");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.10.desc", 10, "ice/tiles");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.11.desc", 11, "ice/a1-stonecobble-icepanel");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.12.desc", 12, "ice/a1-stoneslab-ice");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.13.desc", 13, "ice/zelda");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.14.desc", 14, "ice/bismuth");
			makerPackedIceStairs.carverHelper.addVariation("tile.packedice_stairs.15.desc", 15, "ice/poison");
			makerPackedIceStairs.create(new IStairsCreator() {

				@Override
				public BlockCarvableStairs create(Block block, int meta, CarvableHelper helper) {
					return new BlockCarvablePackedIceStairs(block, meta, helper);
				}
			}, "packedice_stairs", ChiselBlocks.packediceStairs);
			Carving.chisel.registerOre("packedice_stairs", "packedIceStairs");
		}
	},

	PAPER_WALL {

		@Override
		void addBlocks() {
			BlockCarvablePane paperwall = (BlockCarvablePane) new BlockCarvablePane(Material.ground, true).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setResistance(0.5F);

			paperwall.carverHelper.addVariation("tile.paperwall.0.desc", 0, "paper/box");
			paperwall.carverHelper.addVariation("tile.paperwall.1.desc", 1, "paper/throughMiddle");
			paperwall.carverHelper.addVariation("tile.paperwall.2.desc", 2, "paper/cross");
			paperwall.carverHelper.addVariation("tile.paperwall.3.desc", 3, "paper/sixSections");
			paperwall.carverHelper.addVariation("tile.paperwall.4.desc", 4, "paper/vertical");
			paperwall.carverHelper.addVariation("tile.paperwall.5.desc", 5, "paper/horizontal");
			paperwall.carverHelper.addVariation("tile.paperwall.6.desc", 6, "paper/floral");
			paperwall.carverHelper.addVariation("tile.paperwall.7.desc", 7, "paper/plain");
			paperwall.carverHelper.addVariation("tile.paperwall.8.desc", 8, "paper/door");

			paperwall.carverHelper.registerAll(paperwall, "paperwall");
			Carving.chisel.registerOre("paperwall", "paperwall");

			BlockCarvable paperwall_block = (BlockCarvable) new BlockCarvable(Material.ground).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setResistance(0.5F);

			paperwall_block.carverHelper.addVariation("tile.paperwall.0.desc", 0, "paper/box");
			paperwall_block.carverHelper.addVariation("tile.paperwall.1.desc", 1, "paper/throughMiddle");
			paperwall_block.carverHelper.addVariation("tile.paperwall.2.desc", 2, "paper/cross");
			paperwall_block.carverHelper.addVariation("tile.paperwall.3.desc", 3, "paper/sixSections");
			paperwall_block.carverHelper.addVariation("tile.paperwall.4.desc", 4, "paper/vertical");
			paperwall_block.carverHelper.addVariation("tile.paperwall.5.desc", 5, "paper/horizontal");
			paperwall_block.carverHelper.addVariation("tile.paperwall.6.desc", 6, "paper/floral");
			paperwall_block.carverHelper.addVariation("tile.paperwall.7.desc", 7, "paper/plain");
			paperwall_block.carverHelper.addVariation("tile.paperwall.8.desc", 8, "paper/door");

			paperwall_block.carverHelper.registerAll(paperwall_block, "paperwall_block");
			Carving.chisel.registerOre("paperwall_block", "paperwall_block");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.paperwall, 8), "ppp", "psp", "ppp", ('p'), Items.paper, ('s'), "stickWood"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.paperwall_block, 4), "pp", "pp", ('p'), ChiselBlocks.paperwall));
		}
	},

	PRESENT {

		@Override
		void addBlocks() {
			BlockPresent present = (BlockPresent) new BlockPresent().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(2.0F).setResistance(10.0F).setBlockName("chisel.present");
			for (int i = 0; i < 16; i++) {
				present.carverHelper.addVariation("tile.chisel.present.desc", i, "planks_oak", "minecraft");
			}
			present.carverHelper.registerAll(present, "present", ItemBlockPresent.class);
			Carving.chisel.registerOre("present", "chest");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ChiselBlocks.present, 1, meta), new ItemStack(Blocks.chest, 1), dyeOres[meta]));
		}

		@Override
		boolean needsMetaRecipes() {
			return true;
		}
	},

	PUMPKIN {

		@Override
		void addBlocks() {
			for (int metadata = 0; metadata < 16; metadata++) {
				pumpkin[metadata] = (BlockCarvablePumpkin) new BlockCarvablePumpkin(false).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(1.0F).setBlockName("pumpkin")
						.setCreativeTab(ChiselTabs.tabOtherChiselBlocks);
				pumpkin[metadata].setInformation("pumpkin/pumpkin_face_" + (metadata + 1) + "_off");
				GameRegistry.registerBlock(pumpkin[metadata], ItemCarvablePumpkin.class, "pumpkin" + (metadata + 1));
				Carving.chisel.addVariation("pumpkin", pumpkin[metadata], 0, (metadata + 1));
			}
			Carving.chisel.addVariation("pumpkin", Blocks.pumpkin, 0, 0);
			Carving.chisel.registerOre("pumpkin", "pumpkin");
		}

		@Override
		void addRecipes() {
			GameRegistry.addShapelessRecipe(new ItemStack(ChiselBlocks.jackolantern[meta]), new ItemStack(ChiselBlocks.pumpkin[meta], 1), new ItemStack(Item.getItemFromBlock(Blocks.torch), 1));
		}

		@Override
		boolean needsMetaRecipes() {
			return true;
		}
	},

	QUARTZ {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("quartz", Blocks.quartz_block, 0, 0);
			Carving.chisel.addVariation("quartz", Blocks.quartz_block, 1, 0);
			Carving.chisel.addVariation("quartz", Blocks.quartz_block, 2, 0);
			Carving.chisel.registerOre("quartz", "quartz");
		}
	},

	/**
	 * Dummy feature for all RC subsets
	 */
	RAILCRAFT("Railcraft"),

	RC_ABYSSAL_BLOCK(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Block abyssal = GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.abyssal");
			if (abyssal == null) {
				abyssal = GameRegistry.findBlock("Railcraft", "brick.abyssal");
			}

			Carving.chisel.addVariation("RCAbyssalBlock", abyssal, 0, 0);
			Carving.chisel.addVariation("RCAbyssalBlock", abyssal, 1, 1);
			Carving.chisel.addVariation("RCAbyssalBlock", abyssal, 2, 2);
			Carving.chisel.addVariation("RCAbyssalBlock", abyssal, 3, 3);
			Carving.chisel.addVariation("RCAbyssalBlock", abyssal, 4, 4);
			Carving.chisel.addVariation("RCAbyssalBlock", abyssal, 5, 5);
			Carving.chisel.registerOre("RCAbyssalBlock", "RCAbyssalBlock");
		}

		;
	},
	RC_BLEACHED_BONE(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Block bleachedBone = GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bleachedbone");
			if (bleachedBone == null) {
				bleachedBone = GameRegistry.findBlock("Railcraft", "brick.bleachedbone");
			}

			Carving.chisel.addVariation("RCBleachedBone", bleachedBone, 0, 0);
			Carving.chisel.addVariation("RCBleachedBone", bleachedBone, 1, 1);
			Carving.chisel.addVariation("RCBleachedBone", bleachedBone, 2, 2);
			Carving.chisel.addVariation("RCBleachedBone", bleachedBone, 3, 3);
			Carving.chisel.addVariation("RCBleachedBone", bleachedBone, 4, 4);
			Carving.chisel.addVariation("RCBleachedBone", bleachedBone, 5, 5);
			Carving.chisel.registerOre("RCBleachedBone", "RCBleachedBone");
		}
	},

	RC_BLOOD_STAINED(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Block bloodStained = GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bloodstained");
			if (bloodStained == null) {
				bloodStained = GameRegistry.findBlock("Railcraft", "brick.bloodstained");
			}

			Carving.chisel.addVariation("RCBloodStained", bloodStained, 0, 0);
			Carving.chisel.addVariation("RCBloodStained", bloodStained, 1, 1);
			Carving.chisel.addVariation("RCBloodStained", bloodStained, 2, 2);
			Carving.chisel.addVariation("RCBloodStained", bloodStained, 3, 3);
			Carving.chisel.addVariation("RCBloodStained", bloodStained, 4, 4);
			Carving.chisel.addVariation("RCBloodStained", bloodStained, 5, 5);
			Carving.chisel.registerOre("RCBloodStained", "RCBloodStained");
		}
	},

	RC_FROST_BOUND_BLOCK(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Block frostBound = GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.frostbound");
			if (frostBound == null) {
				frostBound = GameRegistry.findBlock("Railcraft", "brick.frostbound");
			}
			Carving.chisel.addVariation("RCFrostBoundBlock", frostBound, 0, 0);
			Carving.chisel.addVariation("RCFrostBoundBlock", frostBound, 1, 1);
			Carving.chisel.addVariation("RCFrostBoundBlock", frostBound, 2, 2);
			Carving.chisel.addVariation("RCFrostBoundBlock", frostBound, 3, 3);
			Carving.chisel.addVariation("RCFrostBoundBlock", frostBound, 4, 4);
			Carving.chisel.addVariation("RCFrostBoundBlock", frostBound, 5, 5);
			Carving.chisel.registerOre("RCFrostBoundBlock", "RCFrostBoundBlock");
		}
	},

	RC_INFERNAL_STONE(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Block infernal = GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.infernal");
			if (infernal == null) {
				infernal = GameRegistry.findBlock("Railcraft", "brick.infernal");
			}
			Carving.chisel.addVariation("RCInfernalStone", infernal, 0, 0);
			Carving.chisel.addVariation("RCInfernalStone", infernal, 1, 1);
			Carving.chisel.addVariation("RCInfernalStone", infernal, 2, 2);
			Carving.chisel.addVariation("RCInfernalStone", infernal, 3, 3);
			Carving.chisel.addVariation("RCInfernalStone", infernal, 4, 4);
			Carving.chisel.addVariation("RCInfernalStone", infernal, 5, 5);
			Carving.chisel.registerOre("RCInfernalStone", "RCInfernalStone");
		}
	},

	RC_QUARRIED_BLOCK(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Block quarried = GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.quarried");
			if (quarried == null) {
				quarried = GameRegistry.findBlock("Railcraft", "brick.quarried");
			}
			Carving.chisel.addVariation("RCQuarriedBlock", quarried, 0, 0);
			Carving.chisel.addVariation("RCQuarriedBlock", quarried, 1, 1);
			Carving.chisel.addVariation("RCQuarriedBlock", quarried, 2, 2);
			Carving.chisel.addVariation("RCQuarriedBlock", quarried, 3, 3);
			Carving.chisel.addVariation("RCQuarriedBlock", quarried, 4, 4);
			Carving.chisel.addVariation("RCQuarriedBlock", quarried, 5, 5);
			Carving.chisel.registerOre("RCQuarriedBlock", "RCQuarriedBlock");
		}

		;
	},

	RC_SANDY_STONE(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Block sandy = GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.sandy");
			if (sandy == null) {
				sandy = GameRegistry.findBlock("Railcraft", "brick.sandy");
			}
			Carving.chisel.addVariation("RCSandyStone", sandy, 0, 0);
			Carving.chisel.addVariation("RCSandyStone", sandy, 1, 1);
			Carving.chisel.addVariation("RCSandyStone", sandy, 2, 2);
			Carving.chisel.addVariation("RCSandyStone", sandy, 3, 3);
			Carving.chisel.addVariation("RCSandyStone", sandy, 4, 4);
			Carving.chisel.addVariation("RCSandyStone", sandy, 5, 5);
			Carving.chisel.registerOre("RCSandyStone", "RCSandyStone");
		}
	},

	REDSTONE_BLOCK {

		@Override
		void addBlocks() {
			BlockCarvable redstone_block = (BlockCarvablePowered) (new BlockCarvablePowered(Material.iron)).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(5.0F).setResistance(10.0F)
					.setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("redstone_block", Blocks.redstone_block, 0, 0);
			redstone_block.carverHelper.addVariation("tile.redstone_block.1.desc", 1, "redstone/smooth");
			redstone_block.carverHelper.addVariation("tile.redstone_block.2.desc", 2, "redstone/block");
			redstone_block.carverHelper.addVariation("tile.redstone_block.3.desc", 3, "redstone/blocks");
			redstone_block.carverHelper.addVariation("tile.redstone_block.4.desc", 4, "redstone/bricks");
			redstone_block.carverHelper.addVariation("tile.redstone_block.5.desc", 5, "redstone/smallbricks");
			redstone_block.carverHelper.addVariation("tile.redstone_block.6.desc", 6, "redstone/smallchaotic");
			redstone_block.carverHelper.addVariation("tile.redstone_block.7.desc", 7, "redstone/chiseled");
			redstone_block.carverHelper.addVariation("tile.redstone_block.8.desc", 8, "redstone/ere");
			redstone_block.carverHelper.addVariation("tile.redstone_block.9.desc", 9, "redstone/ornate-tiles");
			redstone_block.carverHelper.addVariation("tile.redstone_block.10.desc", 10, "redstone/pillar");
			redstone_block.carverHelper.addVariation("tile.redstone_block.11.desc", 11, "redstone/tiles");
			redstone_block.carverHelper.addVariation("tile.redstone_block.12.desc", 12, "redstone/circuit");
			redstone_block.carverHelper.addVariation("tile.redstone_block.13.desc", 13, "redstone/supaplex");
			redstone_block.carverHelper.addVariation("tile.redstone_block.14.desc", 14, "redstone/a1-blockredstone-skullred");
			redstone_block.carverHelper.addVariation("tile.redstone_block.15.desc", 15, "redstone/a1-blockredstone-redstonezelda");

            redstone_block.carverHelper.registerAll(redstone_block, "redstone_block");
			Carving.chisel.registerOre("redstone_block", "redstone");
		}
	},

	ROAD_LINE {

		@Override
		void addBlocks() {
			BlockRoadLine road_line = (BlockRoadLine) new BlockRoadLine().setStepSound(Block.soundTypeStone).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.01F)
					.setBlockName("roadLine");
			road_line.carverHelper.addVariation("tile.roadLine.0.desc", 0, "line-marking/white-center");
			road_line.carverHelper.addVariation("tile.roadLine.1.desc", 1, "line-marking/double-white-center");
			road_line.carverHelper.addVariation("tile.roadLine.2.desc", 2, "line-marking/yellow-center");
			road_line.carverHelper.addVariation("tile.roadLine.3.desc", 3, "line-marking/double-yellow-center");
			road_line.carverHelper.registerAll(road_line, "road_line");
			Carving.chisel.registerOre("road_line", "roadLine");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.road_line, 8, 0), "wrw", "wrw", "wrw", ('w'), "dyeWhite", ('r'), Items.redstone));
		}
	},

	SANDSTONE {

		@Override
		void addBlocks() {
			BlockCarvable sandstone = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setStepSound(Block.soundTypeStone).setHardness(0.8F);
			Carving.chisel.addVariation("sandstone", Blocks.sandstone, 0, 0);
			Carving.chisel.addVariation("sandstone", Blocks.sandstone, 1, 1);
			Carving.chisel.addVariation("sandstone", Blocks.sandstone, 2, 2);
			sandstone.carverHelper.addVariation("tile.sandstone.3.desc", 3, "sandstone/faded");
			sandstone.carverHelper.addVariation("tile.sandstone.4.desc", 4, "sandstone/column");
			sandstone.carverHelper.addVariation("tile.sandstone.5.desc", 5, "sandstone/capstone");
			sandstone.carverHelper.addVariation("tile.sandstone.6.desc", 6, "sandstone/small");
			sandstone.carverHelper.addVariation("tile.sandstone.7.desc", 7, "sandstone/base");
			sandstone.carverHelper.addVariation("tile.sandstone.8.desc", 8, "sandstone/smooth");
			sandstone.carverHelper.addVariation("tile.sandstone.9.desc", 9, "sandstone/smooth-cap");
			sandstone.carverHelper.addVariation("tile.sandstone.10.desc", 10, "sandstone/smooth-small");
			sandstone.carverHelper.addVariation("tile.sandstone.11.desc", 11, "sandstone/smooth-base");
			sandstone.carverHelper.addVariation("tile.sandstone.12.desc", 12, "sandstone/block");
			sandstone.carverHelper.addVariation("tile.sandstone.13.desc", 13, "sandstone/blocks");
			sandstone.carverHelper.addVariation("tile.sandstone.14.desc", 14, "sandstone/mosaic");
			sandstone.carverHelper.addVariation("tile.sandstone.15.desc", 15, "sandstone/horizontal-tiles");

            sandstone.carverHelper.registerAll(sandstone, "sandstone");
			Carving.chisel.registerOre("sandstone", "sandstone");
		}

		@Override
		void addRecipes() {
			if (meta == 0) {
				// The following recipe is due to bugs with Chisel 1.5.1 to
				// 1.5.6a
				GameRegistry.addRecipe(new ItemStack(Blocks.sandstone, 1, 0), "X", 'X', new ItemStack(ChiselBlocks.sandstone, 1, 0));
				// The following recipe is due to bug with Chisel 1.5.6b
				GameRegistry.addRecipe(new ItemStack(Blocks.sandstone, 1, 1), "X", 'X', new ItemStack(ChiselBlocks.sandstone, 1, 1));
			}
			GameRegistry.addRecipe(new ItemStack(Blocks.sandstone, 1, 1), "X", 'X', new ItemStack(ChiselBlocks.sandstone_scribbles, 1, OreDictionary.WILDCARD_VALUE));
		}
	},

	SANDSTONE_SCRIBBLES {

		@Override
		void addBlocks() {
			BlockCarvable sandstone_scribbles = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setStepSound(Block.soundTypeStone).setHardness(0.8F);
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 0, "sandstone-scribbles/scribbles-0");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 1, "sandstone-scribbles/scribbles-1");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 2, "sandstone-scribbles/scribbles-2");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 3, "sandstone-scribbles/scribbles-3");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 4, "sandstone-scribbles/scribbles-4");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 5, "sandstone-scribbles/scribbles-5");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 6, "sandstone-scribbles/scribbles-6");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 7, "sandstone-scribbles/scribbles-7");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 8, "sandstone-scribbles/scribbles-8");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 9, "sandstone-scribbles/scribbles-9");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 10, "sandstone-scribbles/scribbles-10");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 11, "sandstone-scribbles/scribbles-11");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 12, "sandstone-scribbles/scribbles-12");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 13, "sandstone-scribbles/scribbles-13");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 14, "sandstone-scribbles/scribbles-14");
			sandstone_scribbles.carverHelper.addVariation("tile.sandstoneScribbles.desc", 15, "sandstone-scribbles/scribbles-15");
			sandstone_scribbles.carverHelper.registerAll(sandstone_scribbles, "sandstone_scribbles");
			Carving.chisel.registerOre("sandstone_scribbles", "sandstone_scribbles");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.sandstone_scribbles, 1), "X", 'X', new ItemStack(ChiselBlocks.sandstone, 1, 8));
		}
	},

	SILVER {

		@Override
		void addBlocks() {
			BlockCarvable silver = (BlockCarvable) new BlockBeaconBase(Material.iron).setStepSound(Block.soundTypeMetal).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setHardness(5F)
					.setResistance(10F);
			silver.carverHelper.addVariation("tile.metalOre.0.desc", 0, "metals/silver/caution");
			silver.carverHelper.addVariation("tile.metalOre.1.desc", 1, "metals/silver/crate");
			silver.carverHelper.addVariation("tile.metalOre.2.desc", 2, "metals/silver/thermal");
			silver.carverHelper.addVariation("tile.metalOre.3.desc", 3, "metals/silver/adv");
			silver.carverHelper.addVariation("tile.metalOre.4.desc", 4, "metals/silver/egregious");
			silver.carverHelper.addVariation("tile.metalOre.5.desc", 5, "metals/silver/bolted");
			silver.carverHelper.registerAll(silver, "silverblock");
			Carving.chisel.registerOre("silverblock", "blockSilver");
		}
	},

	SMASHING_ROCK {

		@Override
		void addItems() {
			ItemSmashingRock smashingrock = (ItemSmashingRock) new ItemSmashingRock().setTextureName("Chisel:smashingrock").setCreativeTab(ChiselTabs.tabChisel);
			EntityRegistry.registerModEntity(EntitySmashingRock.class, "SmashingRock", 3, Chisel.instance, 40, 1, true);
			GameRegistry.registerItem(smashingrock, "smashingrock");
		}

		@Override
		void addRecipes() {
			GameRegistry.addShapelessRecipe(new ItemStack(ChiselItems.smashingrock, 16), new Object[] { new ItemStack(Items.stone_pickaxe), new ItemStack(Items.glass_bottle, 1),
					new ItemStack(Items.stone_shovel) });
		}
	},

	SNAKE_SANDSTONE(SANDSTONE) {

		@Override
		void addBlocks() {
			BlockSnakestone sand_snakestone = (BlockSnakestone) new BlockSnakestone("Chisel:snakestone/sandsnake/").setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setBlockName(
					"chisel.snakestoneSand");
			GameRegistry.registerBlock(sand_snakestone, ItemCarvable.class, "sand_snakestone");
			// TODO- eat me!
			// LanguageRegistry.addName(new ItemStack(sandSnakestone, 1, 1),
			// "Sandstone snake block head");
			// LanguageRegistry.addName(new ItemStack(sandSnakestone, 1,
			// 13), "Sandstone snake block body");
			Carving.chisel.addVariation("sandstone", sand_snakestone, 1, 16);
			Carving.chisel.addVariation("sandstone", sand_snakestone, 13, 17);
			// Carving.chisel.registerOre("sandstone", "sandSnakestone");
		}
	},

	SNAKESTONE {

		@Override
		void addBlocks() {
			BlockSnakestone stone_snakestone = (BlockSnakestone) new BlockSnakestone("Chisel:snakestone/snake/").setBlockName("chisel.snakestoneStone").setCreativeTab(ChiselTabs.tabStoneChiselBlocks);
			GameRegistry.registerBlock(stone_snakestone, ItemCarvable.class, "stone_snakestone");
			// LanguageRegistry.addName(new ItemStack(snakestone, 1, 1),
			// "Stone snake block head");
			// LanguageRegistry.addName(new ItemStack(snakestone, 1, 13),
			// "Stone snake block body");
			Carving.chisel.addVariation("stonebrick", stone_snakestone, 1, 16);
			Carving.chisel.addVariation("stonebrick", stone_snakestone, 13, 17);
			// Carving.chisel.registerOre("snakestoneStone", "snakestoneStone");
		}
	},

	SNAKESTONE_OBSIDIAN {

		@Override
		void addBlocks() {
			BlockSnakestoneObsidian obsidian_snakestone = (BlockSnakestoneObsidian) new BlockSnakestoneObsidian("Chisel:snakestone/obsidian/").setBlockName("chisel.obsidianSnakestone")
					.setHardness(50.0F).setResistance(2000.0F);
			GameRegistry.registerBlock(obsidian_snakestone, ItemCarvable.class, "obsidian_snakestone");
			Carving.chisel.addVariation("obsidian", obsidian_snakestone, 1, 16);
			Carving.chisel.addVariation("obsidian", obsidian_snakestone, 13, 17);
			// Carving.chisel.registerOre("obsidianSnakestone",
			// "obsidianSnakestone");
		}
	},

	STEEL {

		@Override
		void addBlocks() {
			BlockCarvable steel = (BlockCarvable) new BlockBeaconBase(Material.iron).setStepSound(Block.soundTypeMetal).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setHardness(5F)
					.setResistance(10F);
			steel.carverHelper.addVariation("tile.metalOre.0.desc", 0, "metals/steel/caution");
			steel.carverHelper.addVariation("tile.metalOre.1.desc", 1, "metals/steel/crate");
			steel.carverHelper.addVariation("tile.metalOre.2.desc", 2, "metals/steel/thermal");
			steel.carverHelper.addVariation("tile.metalOre.3.desc", 3, "metals/steel/adv");
			steel.carverHelper.addVariation("tile.metalOre.4.desc", 4, "metals/steel/egregious");
			steel.carverHelper.addVariation("tile.metalOre.5.desc", 5, "metals/steel/bolted");
			steel.carverHelper.registerAll(steel, "steelblock");
			Carving.chisel.registerOre("steelblock", "blockSteel");
		}
	},

	STONE_BRICK {

		@Override
		void addBlocks() {
			BlockCarvable stonebricksmooth = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(1.5F).setResistance(10.0F)
					.setStepSound(Block.soundTypeStone);
			if (Configurations.allowSmoothStone) {
				Carving.chisel.addVariation("stonebricksmooth", Blocks.stone, 0, -1);
			}
			for (int i = 0; i < 4; i++) {
				if ((i == 1) && (Configurations.allowMossy)) {
					Carving.chisel.addVariation("stonebricksmooth", Blocks.stonebrick, i, i);
				} else if (i != 1) {
					Carving.chisel.addVariation("stonebricksmooth", Blocks.stonebrick, i, i);
				}
			}

            stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.0.desc", 0, "stonebrick2/masonBricksPlain");
            stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.1.desc", 1, "stonebrick2/masonBricksFelsic");
            stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.2.desc", 2, "stonebrick2/masonBricksMafic");
            stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.3.desc", 3, "stonebrick2/masonBricksMixed");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.4.desc", 4, "stonebrick/smallbricks");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.5.desc", 5, "stonebrick/largebricks");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.6.desc", 6, "stonebrick/smallchaotic");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.7.desc", 7, "stonebrick/chaoticbricks");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.8.desc", 8, "stonebrick/chaotic");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.9.desc", 9, "stonebrick/fancy");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.10.desc", 10, "stonebrick/ornate");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.11.desc", 11, "stonebrick/largeornate");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.12.desc", 12, "stonebrick/panel-hard");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.13.desc", 13, "stonebrick/sunken");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.14.desc", 14, "stonebrick/ornatepanel");
			stonebricksmooth.carverHelper.addVariation("tile.stonebricksmooth.15.desc", 15, "stonebrick/poison");

			stonebricksmooth.carverHelper.registerAll(stonebricksmooth, "stonebricksmooth");
            Carving.chisel.registerOre("stonebricksmooth", "stonebricksmooth");

            //Carving.chisel.addVariation("stonebricksmooth", GameRegistry.findBlock("Chisel-2", "tile.TFTowerStone"), 0, 0);
		}
	},

	TALLOW("Thaumcraft") {

		@Override
		void addBlocks() {
			BlockCarvable tallow = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setStepSound(Block.soundTypeSnow);
			Carving.chisel.addVariation("tallow", GameRegistry.findBlock("Thaumcraft", "blockCosmeticSolid"), 5, 0);
			tallow.carverHelper.addVariation("tile.tallow.0.desc", 0, "tallow/smooth");
			tallow.carverHelper.addVariation("tile.tallow.0.desc", 1, "tallow/faces");
			tallow.carverHelper.registerAll(tallow, "tallow");
			Carving.chisel.registerOre("tallow", "tallow");
		}
	},

	TECHNICAL {

		@Override
		void addBlocks() {
			BlockCarvable technical = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10F);
			technical.carverHelper.addVariation("tile.technical.0.desc", 0, "technical/scaffold");
			technical.carverHelper.addVariation("tile.technical.1.desc", 1, "technical/cautiontape");
			technical.carverHelper.addVariation("tile.technical.2.desc", 2, "technical/industrialrelic");
			technical.carverHelper.addVariation("tile.technical.3.desc", 3, "technical/pipesLarge");
			technical.carverHelper.addVariation("tile.technical.4.desc", 4, "technical/fanFast");
			technical.carverHelper.addVariation("tile.technical.5.desc", 5, "technical/pipesSmall");
			technical.carverHelper.addVariation("tile.technical.6.desc", 6, "technical/fanStill");
			technical.carverHelper.addVariation("tile.technical.7.desc", 7, "technical/vent");
			technical.carverHelper.addVariation("tile.technical.8.desc", 8, "technical/ventGlowing");
			technical.carverHelper.addVariation("tile.technical.9.desc", 9, "technical/insulationv2");
			technical.carverHelper.addVariation("tile.technical.10.desc", 10, "technical/spinningStuffAnim");
			technical.carverHelper.addVariation("tile.technical.11.desc", 11, "technical/cables");
			technical.carverHelper.addVariation("tile.technical.12.desc", 12, "technical/rustyBoltedPlates");
			technical.carverHelper.addVariation("tile.technical.13.desc", 13, "technical/grate");
			technical.carverHelper.addVariation("tile.technical.14.desc", 14, "technical/malfunctionFan");
			technical.carverHelper.addVariation("tile.technical.15.desc", 15, "technical/grateRusty");
            technical.carverHelper.registerAll(technical, "technical");

			BlockCarvableGlass technical2 = (BlockCarvableGlass) new BlockCarvableGlass().setHardness(2.0F).setResistance(10F);
			technical2.carverHelper.addVariation("tile.technical.0.desc", 0, "technical/scaffoldTransparent");
			technical2.carverHelper.addVariation("tile.technical.4.desc", 1, "technical/fanFastTransparent");
			technical2.carverHelper.addVariation("tile.technical.6.desc", 2, "technical/fanStillTransparent");
			technical2.carverHelper.addVariation("tile.technical.14.desc", 3, "technical/fanStillTransparent");
            technical2.carverHelper.registerBlock(technical2, "technical2");
            technical2.carverHelper.registerVariations("technical", technical2);

            BlockCarvable technical3 = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10F);
            technical3.carverHelper.addVariation("tile.technical3.0.desc", 0, "technical/massiveFan");
            technical3.carverHelper.addVariation("tile.technical3.1.desc", 1, "technical/massiveHexPlating");
            technical2.carverHelper.registerBlock(technical3, "technical3");
            technical2.carverHelper.registerVariations("technical", technical3);

            Carving.chisel.registerOre("technical", "technical");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.technical, Configurations.factoryBlockAmount, 0), new Object[] { "xyx", "yxy", "xyx", 'x', "stone", 'y',
					Items.iron_ingot }));
			/*GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.technical2, Configurations.factoryBlockAmount, 0), new Object[] { "xyx", "yzy", "xyx", 'x', "stone", 'y',
					"ingotIron", 'z', Blocks.glass })); //*/
		}
	},

	TEMPLE_BLOCK {

		@Override
		void addBlocks() {
			BlockCarvable templeblock = (BlockCarvable) new BlockEldritch().setHardness(2.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setResistance(10F)
					.setStepSound(Chisel.soundTempleFootstep);

			templeblock.carverHelper.addVariation("tile.templeblock.0.desc", 0, "temple/cobble");
			templeblock.carverHelper.addVariation("tile.templeblock.1.desc", 1, "temple/ornate");
			templeblock.carverHelper.addVariation("tile.templeblock.2.desc", 2, "temple/plate");
			templeblock.carverHelper.addVariation("tile.templeblock.3.desc", 3, "temple/plate-cracked");
			templeblock.carverHelper.addVariation("tile.templeblock.4.desc", 4, "temple/bricks");
			templeblock.carverHelper.addVariation("tile.templeblock.5.desc", 5, "temple/bricks-large");
			templeblock.carverHelper.addVariation("tile.templeblock.6.desc", 6, "temple/bricks-weared");
			templeblock.carverHelper.addVariation("tile.templeblock.7.desc", 7, "temple/bricks-disarray");
			templeblock.carverHelper.addVariation("tile.templeblock.8.desc", 8, "temple/column");
			templeblock.carverHelper.addVariation("tile.templeblock.9.desc", 9, "temple/stand");
			templeblock.carverHelper.addVariation("tile.templeblock.10.desc", 10, "temple/stand-mosaic");
			templeblock.carverHelper.addVariation("tile.templeblock.11.desc", 11, "temple/stand-creeper");
			templeblock.carverHelper.addVariation("tile.templeblock.12.desc", 12, "temple/tiles");
			templeblock.carverHelper.addVariation("tile.templeblock.13.desc", 13, "temple/smalltiles");
			templeblock.carverHelper.addVariation("tile.templeblock.14.desc", 14, "temple/tiles-light");
			templeblock.carverHelper.addVariation("tile.templeblock.15.desc", 15, "temple/smalltiles-light");
			templeblock.carverHelper.registerAll(templeblock, "templeblock");
			Carving.chisel.registerOre("templeblock", "templeblock");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.templeblock, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', dyeOres[4]));
		}
	},

	TEMPLE_BLOCK_MOSSY {

		@Override
		void addBlocks() {
			BlockCarvable mossy_templeblock = (BlockCarvable) new BlockEldritch().setHardness(2.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setResistance(10F)
					.setStepSound(Chisel.soundTempleFootstep);

			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.0.desc", 0, "templemossy/cobble");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.1.desc", 1, "templemossy/ornate");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.2.desc", 2, "templemossy/plate");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.3.desc", 3, "templemossy/plate-cracked");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.4.desc", 4, "templemossy/bricks");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.5.desc", 5, "templemossy/bricks-large");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.6.desc", 6, "templemossy/bricks-weared");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.7.desc", 7, "templemossy/bricks-disarray");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.8.desc", 8, "templemossy/column");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.9.desc", 9, "templemossy/stand");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.10.desc", 10, "templemossy/stand-mosaic");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.11.desc", 11, "templemossy/stand-creeper");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.12.desc", 12, "templemossy/tiles");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.13.desc", 13, "templemossy/smalltiles");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.14.desc", 14, "templemossy/tiles-light");
			mossy_templeblock.carverHelper.addVariation("tile.mossy_templeblock.15.desc", 15, "templemossy/smalltiles-light");
			mossy_templeblock.carverHelper.registerAll(mossy_templeblock, "mossy_templeblock");
			Carving.chisel.registerOre("mossy_templeblock", "mossy_templeblock");
		}

		;
	},

	TIN {

		@Override
		void addBlocks() {
			BlockCarvable tin = (BlockCarvable) new BlockBeaconBase(Material.iron).setStepSound(Block.soundTypeMetal).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setHardness(5F)
					.setResistance(10F);
			tin.carverHelper.addVariation("tile.metalOre.0.desc", 0, "metals/tin/caution");
			tin.carverHelper.addVariation("tile.metalOre.1.desc", 1, "metals/tin/crate");
			tin.carverHelper.addVariation("tile.metalOre.2.desc", 2, "metals/tin/thermal");
			tin.carverHelper.addVariation("tile.metalOre.3.desc", 3, "metals/tin/adv");
			tin.carverHelper.addVariation("tile.metalOre.4.desc", 4, "metals/tin/egregious");
			tin.carverHelper.addVariation("tile.metalOre.5.desc", 5, "metals/tin/bolted");
			tin.carverHelper.registerAll(tin, "tinblock");
			Carving.chisel.registerOre("tinblock", "blockTin");
		}
	},

	/**
	 * Dummy feature for all TF subsets
	 */
	TWILGHT_FOREST("TwilightForest"),

	TF_MAZESTONE(TWILGHT_FOREST.getRequiredMod(), TWILGHT_FOREST) {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("TFMazestone", GameRegistry.findBlock("TwilightForest", "tile.TFMazestone"), 0, 0);
			Carving.chisel.addVariation("TFMazestone", GameRegistry.findBlock("TwilightForest", "tile.TFMazestone"), 1, 1);
			Carving.chisel.addVariation("TFMazestone", GameRegistry.findBlock("TwilightForest", "tile.TFMazestone"), 2, 2);
			Carving.chisel.addVariation("TFMazestone", GameRegistry.findBlock("TwilightForest", "tile.TFMazestone"), 3, 3);
			Carving.chisel.addVariation("TFMazestone", GameRegistry.findBlock("TwilightForest", "tile.TFMazestone"), 4, 4);
			Carving.chisel.addVariation("TFMazestone", GameRegistry.findBlock("TwilightForest", "tile.TFMazestone"), 5, 5);
			Carving.chisel.addVariation("TFMazestone", GameRegistry.findBlock("TwilightForest", "tile.TFMazestone"), 6, 6);
			Carving.chisel.addVariation("TFMazestone", GameRegistry.findBlock("TwilightForest", "tile.TFMazestone"), 7, 7);
			Carving.chisel.registerOre("TFMazestone", "TFMazestone");
		}
	},

	TF_TOWERSTONE(TWILGHT_FOREST.getRequiredMod(), TWILGHT_FOREST) {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("TFTowerStone", GameRegistry.findBlock("TwilightForest", "tile.TFTowerStone"), 0, 0);
			Carving.chisel.addVariation("TFTowerStone", GameRegistry.findBlock("TwilightForest", "tile.TFTowerStone"), 1, 1);
			Carving.chisel.addVariation("TFTowerStone", GameRegistry.findBlock("TwilightForest", "tile.TFTowerStone"), 2, 2);
			Carving.chisel.addVariation("TFTowerStone", GameRegistry.findBlock("TwilightForest", "tile.TFTowerStone"), 3, 3);
			Carving.chisel.registerOre("TFTowerStone", "TFTowerStone");
		}
	},

	TF_UNDER_BRICK(TWILGHT_FOREST.getRequiredMod(), TWILGHT_FOREST) {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("TFUnderBrick", GameRegistry.findBlock("TwilightForest", "tile.TFUnderBrick"), 0, 0);
			Carving.chisel.addVariation("TFUnderBrick", GameRegistry.findBlock("TwilightForest", "tile.TFUnderBrick"), 1, 1);
			Carving.chisel.addVariation("TFUnderBrick", GameRegistry.findBlock("TwilightForest", "tile.TFUnderBrick"), 2, 2);
			Carving.chisel.registerOre("TFUnderBrick", "TFUnderBrick");
		}
	},

	THAUMIUM("Thaumcraft") {

		@Override
		void addBlocks() {
			BlockCarvable thaumium = (BlockCarvable) new BlockCarvable(Material.iron).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("thaumium", GameRegistry.findBlock("Thaumcraft", "blockCosmeticSolid"), 4, 0);
			thaumium.carverHelper.addVariation("tile.thaumium.0.desc", 0, "thaumium/ornate");
			thaumium.carverHelper.addVariation("tile.thaumium.1.desc", 1, "thaumium/totem");
			thaumium.carverHelper.addVariation("tile.thaumium.2.desc", 2, "thaumium/thaumiumBigBricks");
			thaumium.carverHelper.addVariation("tile.thaumium.3.desc", 3, "thaumium/small");
			thaumium.carverHelper.addVariation("tile.thaumium.4.desc", 4, "thaumium/lattice");
			thaumium.carverHelper.addVariation("tile.thaumium.5.desc", 5, "thaumium/planks");
			thaumium.carverHelper.registerAll(thaumium, "thaumium");
			Carving.chisel.registerOre("thaumium", "thaumium");
		}
	},

	TORCH {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("torch", Blocks.torch, 0, 0);
			for (int type = 0; type < 10; type++) {
				String name = "torch" + (type + 1);
				BlockCarvableTorch t = new BlockCarvableTorch(type, name);
				if (type == 8 || type == 9) {
					t.disableParticles();
				}
				if (Loader.isModLoaded("ForgeMultipart")) {
					GameRegistry.registerBlock(t, ItemBlockChiselTorchPart.class, name, t);
				} else {
					GameRegistry.registerBlock(t, name);
				}
				Carving.chisel.addVariation("torch", t, 0, (type + 1));
				torches[type] = t;
			}
			Carving.chisel.registerOre("torch", "torch");
		}
	},

	TYRIAN {

		@Override
		void addBlocks() {
			BlockCarvable tyrian = (BlockCarvable) new BlockCarvable(Material.iron).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(5.0F).setResistance(10.0F)
					.setStepSound(Block.soundTypeMetal);

			tyrian.carverHelper.addVariation("tile.tyrian.0.desc", 0, "tyrian/shining");
			tyrian.carverHelper.addVariation("tile.tyrian.1.desc", 1, "tyrian/tyrian");
			tyrian.carverHelper.addVariation("tile.tyrian.2.desc", 2, "tyrian/chaotic");
			tyrian.carverHelper.addVariation("tile.tyrian.3.desc", 3, "tyrian/softplate");
			tyrian.carverHelper.addVariation("tile.tyrian.4.desc", 4, "tyrian/rust");
			tyrian.carverHelper.addVariation("tile.tyrian.5.desc", 5, "tyrian/elaborate");
			tyrian.carverHelper.addVariation("tile.tyrian.6.desc", 6, "tyrian/routes");
			tyrian.carverHelper.addVariation("tile.tyrian.7.desc", 7, "tyrian/platform");
			tyrian.carverHelper.addVariation("tile.tyrian.8.desc", 8, "tyrian/platetiles");
			tyrian.carverHelper.addVariation("tile.tyrian.9.desc", 9, "tyrian/diagonal");
			tyrian.carverHelper.addVariation("tile.tyrian.10.desc", 10, "tyrian/dent");
			tyrian.carverHelper.addVariation("tile.tyrian.11.desc", 11, "tyrian/blueplating");
			tyrian.carverHelper.addVariation("tile.tyrian.12.desc", 12, "tyrian/black");
			tyrian.carverHelper.addVariation("tile.tyrian.13.desc", 13, "tyrian/black2");
			tyrian.carverHelper.addVariation("tile.tyrian.14.desc", 14, "tyrian/opening");
			tyrian.carverHelper.addVariation("tile.tyrian.15.desc", 15, "tyrian/plate");
			tyrian.carverHelper.registerAll(tyrian, "tyrian");
			OreDictionary.registerOre("tyrian", tyrian);
			Carving.chisel.registerOre("tyrian", "tyrian");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.tyrian, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1));
		}
	},

	URANIUM /* Whose ranium? */{

		@Override
		void addBlocks() {
			BlockCarvable uranium = (BlockCarvable) new BlockBeaconBase(Material.iron).setStepSound(Block.soundTypeMetal).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setHardness(5F)
					.setResistance(10F);
			uranium.carverHelper.addVariation("tile.metalOre.0.desc", 0, "metals/uranium/caution");
			uranium.carverHelper.addVariation("tile.metalOre.1.desc", 1, "metals/uranium/crate");
			uranium.carverHelper.addVariation("tile.metalOre.2.desc", 2, "metals/uranium/thermal");
			uranium.carverHelper.addVariation("tile.metalOre.3.desc", 3, "metals/uranium/adv");
			uranium.carverHelper.addVariation("tile.metalOre.4.desc", 4, "metals/uranium/egregious");
			uranium.carverHelper.addVariation("tile.metalOre.5.desc", 5, "metals/uranium/bolted");
			uranium.carverHelper.registerAll(uranium, "uraniumblock");
			Carving.chisel.registerOre("uraniumblock", "blockUranium");
		}
	},

	VALENTINES {

		@Override
		void addBlocks() {
			BlockCarvable valentines = (BlockCarvable) new BlockCarvable(Material.wood).setStepSound(Block.soundTypeStone).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(2F)
					.setResistance(10F);
			valentines.carverHelper.addVariation("tile.valentines.0.desc", 0, "valentines/1");
			valentines.carverHelper.addVariation("tile.valentines.1.desc", 1, "valentines/companion");
			valentines.carverHelper.addVariation("tile.valentines.2.desc", 2, "valentines/2");
			valentines.carverHelper.addVariation("tile.valentines.3.desc", 3, "valentines/3");
			valentines.carverHelper.addVariation("tile.valentines.4.desc", 4, "valentines/4");
			valentines.carverHelper.addVariation("tile.valentines.5.desc", 5, "valentines/5");
			valentines.carverHelper.addVariation("tile.valentines.6.desc", 6, "valentines/6");
			valentines.carverHelper.addVariation("tile.valentines.7.desc", 7, "valentines/7");
			valentines.carverHelper.addVariation("tile.valentines.8.desc", 8, "valentines/8");
			valentines.carverHelper.addVariation("tile.valentines.9.desc", 9, "valentines/9");
			valentines.carverHelper.registerAll(valentines, "valentines");
			Carving.chisel.registerOre("valentines", "blockValentines");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.valentines, 8, 0), "***", "*X*", "***", '*', "stone", 'X', new ItemStack(Items.dye, 1, 9)));
		}
	},

	VOIDSTONE {

		@Override
		void addBlocks() {
			BlockCarvable voidstone = (BlockCarvable) new BlockCarvable().setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setStepSound(Block.soundTypeStone)
					.setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(5.0F).setResistance(10.0F);
			voidstone.carverHelper.addVariation("tile.voidstone.0.desc", 0, "voidstone/raw");
			voidstone.carverHelper.addVariation("tile.voidstone.1.desc", 1, "voidstone/quarters");
			voidstone.carverHelper.addVariation("tile.voidstone.2.desc", 2, "voidstone/smooth");
			voidstone.carverHelper.addVariation("tile.voidstone.3.desc", 3, "voidstone/skulls");
			voidstone.carverHelper.addVariation("tile.voidstone.4.desc", 4, "voidstone/rune");
			voidstone.carverHelper.addVariation("tile.voidstone.5.desc", 5, "voidstone/metalborder");
			voidstone.carverHelper.addVariation("tile.voidstone.6.desc", 6, "voidstone/eye");
			voidstone.carverHelper.addVariation("tile.voidstone.7.desc", 7, "voidstone/bevel");

			voidstone.carverHelper.registerAll(voidstone, "voidstone");
			Carving.chisel.registerOre("voidstone", "voidstone");

			BlockMultiLayer voidstone2 = (BlockMultiLayer) new BlockMultiLayer(Material.rock, Chisel.MOD_ID + ":voidstone/animated/void").setStepSound(Block.soundTypeStone)
					.setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(5.0F).setResistance(10.0F);
			voidstone2.carverHelper.addVariation("tile.voidstone.0.desc", 0, "voidstone/animated/raw");
			voidstone2.carverHelper.addVariation("tile.voidstone.1.desc", 1, "voidstone/animated/quarters");
			voidstone2.carverHelper.addVariation("tile.voidstone.2.desc", 2, "voidstone/animated/smooth");
			voidstone2.carverHelper.addVariation("tile.voidstone.3.desc", 3, "voidstone/animated/skulls");
			voidstone2.carverHelper.addVariation("tile.voidstone.4.desc", 4, "voidstone/animated/rune");
			voidstone2.carverHelper.addVariation("tile.voidstone.5.desc", 5, "voidstone/animated/metalborder");
			voidstone2.carverHelper.addVariation("tile.voidstone.6.desc", 6, "voidstone/animated/eye");
			voidstone2.carverHelper.addVariation("tile.voidstone.7.desc", 7, "voidstone/animated/bevel");

			voidstone2.carverHelper.registerAll(voidstone2, "voidstone2");
			Carving.chisel.registerOre("voidstone2", "voidstone2");

			BlockCarvable voidstoneRunic = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(5.0F).setResistance(10.0F);


			for (int i = 1; i < 16; i++)
				voidstoneRunic.carverHelper.addVariation("tile.voidstoneRunic." + sGNames[i].replaceAll(" ", "").toLowerCase() + ".desc", i, "voidstone/runes/rune" + sGNames[i].replaceAll(" ", ""));
			voidstoneRunic.carverHelper.registerAll(voidstoneRunic, "voidstoneRunic");
			voidstoneRunic.carverHelper.registerVariations("voidstone", voidstoneRunic);

			Carving.chisel.registerOre("voidstoneRunic", "voidstoneRunic");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstone, 8, 0), new Object[] { "oxo", "xyx", "oxo", 'x', new ItemStack(Blocks.stone, 1), 'y', new ItemStack(Items.ender_pearl, 1), 'o',
					new ItemStack(Blocks.obsidian, 1) });
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstone2, 8, 0), new Object[] { "oxo", "xyx", "oxo", 'x', new ItemStack(Blocks.stone, 1), 'y', new ItemStack(Items.ender_eye, 1), 'o',
					new ItemStack(Blocks.obsidian, 1) });
		}
	},

	VOIDSTONE_PILLARS(VOIDSTONE) {

		@Override
		void addBlocks() {
			BlockCarvablePillar voidstonePillar = (BlockCarvablePillar) new BlockCarvablePillar(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setStepSound(Block.soundTypeStone);
			voidstonePillar.carverHelper.addVariation("tile.voidstonePillar.0.desc", 0, "voidstone/pillar");
			voidstonePillar.carverHelper.registerAll(voidstonePillar, "voidstonePillar");
			Carving.chisel.registerOre("voidstonePillar", "voidstonePillar");

			BlockCarvablePillar voidstonePillar2 = (BlockCarvablePillar) new BlockCarvablePillar(Material.rock).setStepSound(Block.soundTypeStone);
			voidstonePillar2.carverHelper.addVariation("tile.voidstonePillar2.0.desc", 0, "voidstone/animated/pillar");
			voidstonePillar2.carverHelper.registerAll(voidstonePillar2, "voidstonePillar2");
			Carving.chisel.registerOre("voidstonePillar2", "voidstonePillar2");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstonePillar, 4, 0), "xx", "xx", 'x', new ItemStack(ChiselBlocks.voidstone, 1));
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstonePillar2, 4, 0), "xx", "xx", 'x', new ItemStack(ChiselBlocks.voidstone2, 1));
		}
	},

	WARNING_SIGN {

		@Override
		void addBlocks() {
			BlockCarvableLayered warningSign = (BlockCarvableLayered) new BlockCarvableLayered(Material.iron, "warning/base").setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F)
					.setResistance(10.0F);
			warningSign.carverHelper.addVariation("tile.warningSign.0.desc", 0, "warning/rad");
			warningSign.carverHelper.addVariation("tile.warningSign.1.desc", 1, "warning/bio");
			warningSign.carverHelper.addVariation("tile.warningSign.2.desc", 2, "warning/fire");
			warningSign.carverHelper.addVariation("tile.warningSign.3.desc", 3, "warning/explosion");
			warningSign.carverHelper.addVariation("tile.warningSign.4.desc", 4, "warning/death");
			warningSign.carverHelper.addVariation("tile.warningSign.5.desc", 5, "warning/falling");
			warningSign.carverHelper.addVariation("tile.warningSign.6.desc", 6, "warning/fall");
			warningSign.carverHelper.addVariation("tile.warningSign.7.desc", 7, "warning/voltage");
			warningSign.carverHelper.addVariation("tile.warningSign.8.desc", 8, "warning/generic");
			warningSign.carverHelper.addVariation("tile.warningSign.9.desc", 9, "warning/acid");
			warningSign.carverHelper.addVariation("tile.warningSign.10.desc", 10, "warning/underconstruction");
			warningSign.carverHelper.addVariation("tile.warningSign.11.desc", 11, "warning/sound");
			warningSign.carverHelper.addVariation("tile.warningSign.12.desc", 12, "warning/noentry");
			warningSign.carverHelper.addVariation("tile.warningSign.13.desc", 13, "warning/cryogenic");
			warningSign.carverHelper.addVariation("tile.warningSign.14.desc", 14, "warning/oxygen");
			warningSign.carverHelper.registerAll(warningSign, "warningSign");
			Carving.chisel.registerOre("warningSign", "warningSign");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.warningSign, 4, 0), new Object[] { "xxx", "xyx", "xxx", 'x', "stone", 'y', Items.sign }));
		}
	},

	WATERSTONE {

		@Override
		void addBlocks() {
			BlockWaterstone waterstone = (BlockWaterstone) new BlockWaterstone(Material.rock, "water_flow").setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10.0F);
			waterstone.carverHelper.addVariation("tile.waterstone.0.desc", 0, "waterstone/cobble");
			waterstone.carverHelper.addVariation("tile.waterstone.1.desc", 1, "waterstone/black");
			waterstone.carverHelper.addVariation("tile.waterstone.2.desc", 2, "waterstone/tiles");
			waterstone.carverHelper.addVariation("tile.waterstone.3.desc", 3, "waterstone/chaotic");
			waterstone.carverHelper.addVariation("tile.waterstone.4.desc", 4, "waterstone/creeper");
			waterstone.carverHelper.addVariation("tile.waterstone.5.desc", 5, "waterstone/panel");
			waterstone.carverHelper.addVariation("tile.waterstone.6.desc", 6, "waterstone/panel-ornate");
			waterstone.carverHelper.registerAll(waterstone, "waterstone");
			OreDictionary.registerOre("blockWaterstone", waterstone);
			Carving.chisel.registerOre("waterstone", "blockWaterstone");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.waterstone, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.water_bucket, 1));
		}
	},

	WOOD {

		@Override
		void addBlocks() {
			String[] plank_names = { "oak", "spruce", "birch", "jungle", "acacia", "dark-oak" };
			String[] plank_ucnames = { "Oak", "Spruce", "Birch", "Jungle", "Acacia", "Dark Oak" };
			for (int i = 0; i < plank_names.length; i++) {
				String n = plank_names[i];
				String u = plank_ucnames[i];
				final String name = n.replace('-', '_') + "_planks";

				planks[i] = (BlockCarvable) (new BlockCarvable(Material.wood)).setCreativeTab(ChiselTabs.tabWoodChiselBlocks).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood);

				planks[i].carverHelper.addVariation("Smooth " + n + " wood planks", 1, "planks-" + n + "/clean");
				planks[i].carverHelper.addVariation("Short " + n + " wood planks", 2, "planks-" + n + "/short");
				planks[i].carverHelper.addVariation("Fancy " + n + " wood plank arrangement", 6, "planks-" + n + "/fancy");
				planks[i].carverHelper.addVariation(u + " wood panel", 8, "planks-" + n + "/panel-nails");
				planks[i].carverHelper.addVariation(u + " wood double slab", 9, "planks-" + n + "/double");
				planks[i].carverHelper.addVariation(u + " wood crate", 10, "planks-" + n + "/crate");
				planks[i].carverHelper.addVariation("Fancy " + n + " wood crate", 11, "planks-" + n + "/crate-fancy");
				planks[i].carverHelper.addVariation("Large long " + n + " wood planks", 13, "planks-" + n + "/large");
				planks[i].carverHelper.addVariation("Vertical " + n + " wood planks", 3, "planks-" + n + "/vertical");
				planks[i].carverHelper.addVariation("Vertical uneven " + n + " wood planks", 4, "planks-" + n + "/vertical-uneven");
				planks[i].carverHelper.addVariation(u + " wood parquet", 5, "planks-" + n + "/parquet");
				planks[i].carverHelper.addVariation(u + " wood plank blinds", 7, "planks-" + n + "/blinds");
				planks[i].carverHelper.addVariation(u + " wood scaffold", 12, "planks-" + n + "/crateex");
				planks[i].carverHelper.addVariation(u + " wood planks in disarray", 14, "planks-" + n + "/chaotic-hor");
				planks[i].carverHelper.addVariation("Vertical " + n + " wood planks in disarray", 15, "planks-" + n + "/chaotic");
				planks[i].carverHelper.registerAll(planks[i], name);
				Carving.chisel.addVariation(name, Blocks.planks, i, 0);
				planks[i].setHarvestLevel("axe", 0);
				Carving.chisel.registerOre(name, "wood");
				Carving.chisel.setVariationSound(name, Chisel.MOD_ID + ":chisel.wood");
			}
		}
	},

	WOOLEN_CLAY {

		@Override
		void addBlocks() {
			BlockCarvable woolen_clay = (BlockCarvable) new BlockCarvable(Material.clay).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(2F).setResistance(10F);


			for (int i = 0; i < 16; i++)
				woolen_clay.carverHelper.addVariation("tile.woolenClay." + i + ".desc", i, "woolenClay/" + sGNames[i].replaceAll(" ", "").toLowerCase());
			woolen_clay.carverHelper.registerAll(woolen_clay, "woolen_clay");

			Carving.chisel.registerOre("woolen_clay", "woolen_clay");
		}

		@Override
		void addRecipes() {
			OreDictionary.registerOre("stainedClay" + sGNames[meta].replaceAll(" ", ""), new ItemStack(Blocks.stained_hardened_clay, 1, meta));
			OreDictionary.registerOre("blockWool" + sGNames[meta].replaceAll(" ", ""), new ItemStack(Blocks.wool, 1, meta));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ChiselBlocks.woolen_clay, 2, meta), new Object[] { "blockWool" + sGNames[meta].replaceAll(" ", ""),
					"stainedClay" + sGNames[meta].replaceAll(" ", "") }));
		}

		@Override
		boolean needsMetaRecipes() {
			return true;
		}
	};

	private static final String[] dyeOres = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow",
			"dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };

	private static int meta = 0;

	static void init() {
		Chisel.logger.info("Starting init...");
		loadRecipes();
		Chisel.logger.info("Init finished.");
	}

	private static void loadBlocks() {
		Chisel.logger.info("Loading blocks...");
		int num = 0;
		for (Features f : values()) {
			if (f.enabled()) {
				f.addBlocks();
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
				if (f.needsMetaRecipes()) {
					for (int i = 0; i < 16; i++) {
						meta = i;
						f.addRecipes();
					}
					meta = 0;
				} else {
					f.addRecipes();
				}
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

	void addBlocks() {
		;
	}

	void addItems() {
		;
	}

	void addRecipes() {
		;
	}

	public final boolean enabled() {
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
		String name = Block.blockRegistry.getNameForObject(bottom);
		name = name.substring(name.indexOf(':') + 1) + "_top";
		GameRegistry.registerBlock(top, ItemCarvableSlab.class, name);
	}
}
