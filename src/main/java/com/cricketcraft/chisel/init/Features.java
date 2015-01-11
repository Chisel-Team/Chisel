package com.cricketcraft.chisel.init;

import static com.cricketcraft.chisel.init.ChiselBlocks.carpet_block;
import static com.cricketcraft.chisel.init.ChiselBlocks.jackolantern;
import static com.cricketcraft.chisel.init.ChiselBlocks.planks;
import static com.cricketcraft.chisel.init.ChiselBlocks.present;
import static com.cricketcraft.chisel.init.ChiselBlocks.pumpkin;
import static com.cricketcraft.chisel.init.ChiselBlocks.stainedGlass;
import static com.cricketcraft.chisel.init.ChiselBlocks.stainedGlassPane;
import static com.cricketcraft.chisel.init.ChiselBlocks.torch;
import static com.cricketcraft.chisel.utils.General.sGNames;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.BlockAutoChisel;
import com.cricketcraft.chisel.block.BlockBeaconBase;
import com.cricketcraft.chisel.block.BlockCarvable;
import com.cricketcraft.chisel.block.BlockCarvableGlass;
import com.cricketcraft.chisel.block.BlockCarvableGlow;
import com.cricketcraft.chisel.block.BlockCarvableGlowstone;
import com.cricketcraft.chisel.block.BlockCarvableLayered;
import com.cricketcraft.chisel.block.BlockCarvablePane;
import com.cricketcraft.chisel.block.BlockCarvablePowered;
import com.cricketcraft.chisel.block.BlockCarvablePumpkin;
import com.cricketcraft.chisel.block.BlockCarvableSand;
import com.cricketcraft.chisel.block.BlockCarvableSlab;
import com.cricketcraft.chisel.block.BlockCarvableStairs;
import com.cricketcraft.chisel.block.BlockCarvableTorch;
import com.cricketcraft.chisel.block.BlockCloud;
import com.cricketcraft.chisel.block.BlockConcrete;
import com.cricketcraft.chisel.block.BlockEldritch;
import com.cricketcraft.chisel.block.BlockGrimstone;
import com.cricketcraft.chisel.block.BlockHolystone;
import com.cricketcraft.chisel.block.BlockLavastone;
import com.cricketcraft.chisel.block.BlockLeaf;
import com.cricketcraft.chisel.block.BlockMarbleBookshelf;
import com.cricketcraft.chisel.block.BlockMarbleCarpet;
import com.cricketcraft.chisel.block.BlockMarbleIce;
import com.cricketcraft.chisel.block.BlockMarbleIceStairs;
import com.cricketcraft.chisel.block.BlockMarblePackedIce;
import com.cricketcraft.chisel.block.BlockMarblePackedIceStairs;
import com.cricketcraft.chisel.block.BlockMarblePillar;
import com.cricketcraft.chisel.block.BlockMarbleStairsMaker;
import com.cricketcraft.chisel.block.BlockMarbleStairsMakerCreator;
import com.cricketcraft.chisel.block.BlockMarbleTexturedOre;
import com.cricketcraft.chisel.block.BlockPresent;
import com.cricketcraft.chisel.block.BlockRoadLine;
import com.cricketcraft.chisel.block.BlockSnakestone;
import com.cricketcraft.chisel.block.BlockSnakestoneObsidian;
import com.cricketcraft.chisel.block.BlockVoidstonePillar;
import com.cricketcraft.chisel.block.BlockVoidstonePillar2;
import com.cricketcraft.chisel.block.BlockWaterstone;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.config.Configurations;
import com.cricketcraft.chisel.entity.EntityBallOMoss;
import com.cricketcraft.chisel.entity.EntityCloudInABottle;
import com.cricketcraft.chisel.entity.EntitySmashingRock;
import com.cricketcraft.chisel.item.ItemBallOMoss;
import com.cricketcraft.chisel.item.ItemCarvable;
import com.cricketcraft.chisel.item.ItemCloudInABottle;
import com.cricketcraft.chisel.item.ItemMarbleSlab;
import com.cricketcraft.chisel.item.ItemSmashingRock;
import com.cricketcraft.chisel.item.ItemUpgrade;
import com.cricketcraft.chisel.item.chisel.ItemChisel;
import com.cricketcraft.chisel.item.chisel.ItemChisel.ChiselType;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

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

	ANDESITE {

		@Override
		void addBlocks() {
			BlockCarvable andesite = (BlockCarvable) new BlockCarvable(Material.rock).setHardness(2.0F).setResistance(10.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks);
			andesite.carverHelper.setChiselBlockName("andesite");
			andesite.carverHelper.addVariation(StatCollector.translateToLocal("tile.andesite.0.desc"), 0, "andesite/andesite");
			andesite.carverHelper.addVariation(StatCollector.translateToLocal("tile.andesite.1.desc"), 1, "andesite/polishedAndesite");
			andesite.carverHelper.register(andesite, "andesite");
			Carving.chisel.registerOre("andesite", "andesite");
		}
	},

	ARCANE("Thaumcraft") {

		@Override
		void addBlocks() {
			BlockCarvable arcane = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("arcane", GameRegistry.findBlock("Thaumcraft", "blockCosmeticSolid"), 6, 0);
			Carving.chisel.addVariation("arcane", GameRegistry.findBlock("Thaumcraft", "blockCosmeticSolid"), 7, 1);
			arcane.carverHelper.addVariation(StatCollector.translateToLocal("tile.arcane.0.desc"), 0, "arcane/moonEngrave");
			arcane.carverHelper.addVariation(StatCollector.translateToLocal("tile.arcane.1.desc"), 1, "arcane/moonGlowAnim");
			arcane.carverHelper.addVariation(StatCollector.translateToLocal("tile.arcane.2.desc"), 2, "arcane/arcaneTile");
			arcane.carverHelper.addVariation(StatCollector.translateToLocal("tile.arcane.3.desc"), 3, "arcane/arcaneRunes");
			arcane.carverHelper.addVariation(StatCollector.translateToLocal("tile.arcane.4.desc"), 4, "arcane/arcaneRunesGlow");
			arcane.carverHelper.addVariation(StatCollector.translateToLocal("tile.arcane.5.desc"), 5, "arcane/bigBrick");
			arcane.carverHelper.addVariation(StatCollector.translateToLocal("tile.arcane.6.desc"), 6, "arcane/conduitGlowAnim");
			arcane.carverHelper.addVariation(StatCollector.translateToLocal("tile.arcane.7.desc"), 7, "arcane/BorderBrain");
			arcane.carverHelper.register(arcane, "arcane");
			Carving.chisel.registerOre("arcane", "arcane");
		}
	},

	THAUMIUM("Thaumcraft") {

		@Override
		void addBlocks() {
			BlockCarvable thaumium = (BlockCarvable) new BlockCarvable(Material.iron).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("thaumium", GameRegistry.findBlock("Thaumcraft", "blockCosmeticSolid"), 4, 0);
			thaumium.carverHelper.addVariation(StatCollector.translateToLocal("tile.thaumium.0.desc"), 0, "thaumium/bevel");
			thaumium.carverHelper.addVariation(StatCollector.translateToLocal("tile.thaumium.1.desc"), 1, "thaumium/chunks");
			thaumium.carverHelper.addVariation(StatCollector.translateToLocal("tile.thaumium.2.desc"), 2, "thaumium/purplerunes");
			thaumium.carverHelper.addVariation(StatCollector.translateToLocal("tile.thaumium.3.desc"), 3, "thaumium/runes");
			thaumium.carverHelper.register(thaumium, "thaumium");
			Carving.chisel.registerOre("thaumium", "thaumium");
		}
	},

	TALLOW("Thaumcraft") {

		@Override
		void addBlocks() {
			BlockCarvable tallow = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setStepSound(Block.soundTypeSnow);
			Carving.chisel.addVariation("tallow", GameRegistry.findBlock("Thaumcraft", "blockCosmeticSolid"), 5, 0);
			tallow.carverHelper.register(tallow, "tallow");
			Carving.chisel.registerOre("tallow", "tallow");
		}
	},

	AMBER("Thaumcraft") {

		@Override
		void addBlocks() {
			BlockCarvable amber = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabModdedChiselBlocks).setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("amber", GameRegistry.findBlock("Thaumcraft", "blockCosmeticOpaque"), 0, 0);
			Carving.chisel.addVariation("amber", GameRegistry.findBlock("Thaumcraft", "blockCosmeticOpaque"), 1, 1);
			amber.carverHelper.register(amber, "amber");
			Carving.chisel.registerOre("amber", "amber");
		}
	},

	AUTO_CHISEL {

		@Override
		void addBlocks() {
			Block autoChisel = new BlockAutoChisel().setBlockTextureName(Chisel.MOD_ID + ":autoChisel/autoChisel").setCreativeTab(ChiselTabs.tabChisel).setBlockName("autoChisel");
			GameRegistry.registerBlock(autoChisel, "autoChisel");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.autoChisel, 1), new Object[] { "XXX", "XYX", "XXX", Character.valueOf('X'), "plankWood", Character.valueOf('Y'),
					ChiselItems.chisel }));
		}
	},

	AUTO_CHISEL_UPGRADES(AUTO_CHISEL) {

		@Override
		void addItems() {
			ItemUpgrade upgrade = (ItemUpgrade) new ItemUpgrade("upgrade").setCreativeTab(ChiselTabs.tabChisel);
			GameRegistry.registerItem(upgrade, "upgrade");
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
			bloodRune.carverHelper.addVariation(StatCollector.translateToLocal("tile.bloodRune.0.desc"), 0, "bloodMagic/bloodRuneArranged");
			bloodRune.carverHelper.addVariation(StatCollector.translateToLocal("tile.bloodRune.1.desc"), 1, "bloodMagic/bloodRuneBricks");
			bloodRune.carverHelper.addVariation(StatCollector.translateToLocal("tile.bloodRune.2.desc"), 2, "bloodMagic/bloodRuneCarved");
			bloodRune.carverHelper.addVariation(StatCollector.translateToLocal("tile.bloodRune.3.desc"), 3, "bloodMagic/bloodRuneCarvedRadial");
			bloodRune.carverHelper.addVariation(StatCollector.translateToLocal("tile.bloodRune.4.desc"), 4, "bloodMagic/bloodRuneClassicPanel");
			bloodRune.carverHelper.addVariation(StatCollector.translateToLocal("tile.bloodRune.5.desc"), 5, "bloodMagic/bloodRuneTiles");
			bloodRune.carverHelper.register(bloodRune, "bloodRune");
			Carving.chisel.registerOre("bloodRune", "bloodRune");
		}
	},

	BOOKSHELF {

		@Override
		void addBlocks() {
			BlockCarvable bookshelf = (BlockCarvable) new BlockMarbleBookshelf().setHardness(1.5F).setCreativeTab(ChiselTabs.tabWoodChiselBlocks).setStepSound(Block.soundTypeWood);
			Carving.chisel.addVariation("bookshelf", Blocks.bookshelf, 0, 0);
			bookshelf.carverHelper.addVariation(StatCollector.translateToLocal("tile.bookshelf.1.desc"), 1, "bookshelf/rainbow");
			bookshelf.carverHelper.addVariation(StatCollector.translateToLocal("tile.bookshelf.2.desc"), 2, "bookshelf/necromancer-novice");
			bookshelf.carverHelper.addVariation(StatCollector.translateToLocal("tile.bookshelf.3.desc"), 3, "bookshelf/necromancer");
			bookshelf.carverHelper.addVariation(StatCollector.translateToLocal("tile.bookshelf.4.desc"), 4, "bookshelf/redtomes");
			bookshelf.carverHelper.addVariation(StatCollector.translateToLocal("tile.bookshelf.5.desc"), 5, "bookshelf/abandoned");
			bookshelf.carverHelper.addVariation(StatCollector.translateToLocal("tile.bookshelf.6.desc"), 6, "bookshelf/hoarder");
			bookshelf.carverHelper.addVariation(StatCollector.translateToLocal("tile.bookshelf.7.desc"), 7, "bookshelf/brim");
			bookshelf.carverHelper.addVariation(StatCollector.translateToLocal("tile.bookshelf.8.desc"), 8, "bookshelf/historician");
			bookshelf.carverHelper.register(bookshelf, "bookshelf");
			bookshelf.setHarvestLevel("axe", 0);
			Carving.chisel.registerOre("bookshelf", "bookshelf");
		}
	},

	BRICK_CUSTOM {

		@Override
		void addBlocks() {
			BlockCarvable brickCustom = (BlockCarvable) new BlockCarvable(Material.rock).setStepSound(Block.soundTypeStone).setCreativeTab(ChiselTabs.tabStoneChiselBlocks);
			Carving.chisel.addVariation("brickCustom", Blocks.brick_block, 0, 0);
			brickCustom.carverHelper.addVariation(StatCollector.translateToLocal("tile.brickCustom.1.desc"), 1, "brickCustom/large");
			brickCustom.carverHelper.addVariation(StatCollector.translateToLocal("tile.brickCustom.2.desc"), 2, "brickCustom/mortarless");
			brickCustom.carverHelper.addVariation(StatCollector.translateToLocal("tile.brickCustom.3.desc"), 3, "brickCustom/varied");
			// brickCustom.carverHelper.addVariation(StatCollector.translateToLocal("tile.brickCustom.4.desc"),
			// 4, "brickCustom/cracked");
			brickCustom.carverHelper.addVariation(StatCollector.translateToLocal("tile.brickCustom.5.desc"), 5, "brickCustom/aged");
			// brickCustom.carverHelper.addVariation(StatCollector.translateToLocal("tile.brickCustom.6.desc"),
			// 6, "brickCustom/mossy");
			brickCustom.carverHelper.register(brickCustom, "brickCustom");
			Carving.chisel.registerOre("brickCustom", "brickCustom");
		}
	},

	CARPET {

		@Override
		void addBlocks() {
			BlockCarvable carpet_block = (BlockCarvable) new BlockCarvable(Material.cloth).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Block.soundTypeCloth);
			carpet_block.carverHelper.setChiselBlockName("Carpet Block");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.0.desc"), 0, "carpet/white");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.1.desc"), 1, "carpet/orange");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.2.desc"), 2, "carpet/lily");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.3.desc"), 3, "carpet/lightblue");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.4.desc"), 4, "carpet/yellow");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.5.desc"), 5, "carpet/lightgreen");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.6.desc"), 6, "carpet/pink");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.7.desc"), 7, "carpet/darkgrey");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.8.desc"), 8, "carpet/grey");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.9.desc"), 9, "carpet/teal");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.10.desc"), 10, "carpet/purple");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.11.desc"), 11, "carpet/darkblue");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.12.desc"), 12, "carpet/brown");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.13.desc"), 13, "carpet/green");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.14.desc"), 14, "carpet/red");
			carpet_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet_block.15.desc"), 15, "carpet/black");
			carpet_block.carverHelper.forbidChiseling = true;
			carpet_block.carverHelper.register(carpet_block, "carpet_block");
			OreDictionary.registerOre("carpet", carpet_block);
			Carving.chisel.registerOre("carpet_block", "carpet");
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
			BlockMarbleCarpet carpet = (BlockMarbleCarpet) new BlockMarbleCarpet(Material.cloth).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Block.soundTypeCloth);
			carpet.carverHelper.setChiselBlockName("Carpet");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.0.desc"), 0, "carpet/white");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.1.desc"), 1, "carpet/orange");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.2.desc"), 2, "carpet/lily");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.3.desc"), 3, "carpet/lightblue");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.4.desc"), 4, "carpet/yellow");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.5.desc"), 5, "carpet/lightgreen");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.6.desc"), 6, "carpet/pink");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.7.desc"), 7, "carpet/darkgrey");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.8.desc"), 8, "carpet/grey");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.9.desc"), 9, "carpet/teal");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.10.desc"), 10, "carpet/purple");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.11.desc"), 11, "carpet/darkblue");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.12.desc"), 12, "carpet/brown");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.13.desc"), 13, "carpet/green");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.14.desc"), 14, "carpet/red");
			carpet.carverHelper.addVariation(StatCollector.translateToLocal("tile.carpet.15.desc"), 15, "carpet/black");
			carpet.carverHelper.forbidChiseling = true;
			carpet.carverHelper.register(carpet, "carpet");

			for (int i = 0; i < 16; i++) {
				String group = "carpet." + i;

				Carving.needle.addVariation(group, Blocks.carpet, i, 0);
				Carving.needle.addVariation(group, carpet, i, 2);
				Carving.needle.addVariation(group, carpet_block, i, 1);
			}

			Carving.chisel.registerOre("carpet", "carpet");
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
			GameRegistry.registerItem(chisel, "chisel");
			GameRegistry.registerItem(diamondChisel, "diamondChisel");
		}

		@Override
		void addRecipes() {
			if (Configurations.chiselRecipe) {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.chisel), " YY", " YY", "X  ", 'X', "stickWood", 'Y', "ingotIron"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.diamondChisel), " YY", " YY", "x  ", 'x', "stickWood", 'Y', "gemDiamond"));
			} else {
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.chisel), " Y", "X ", 'X', "stickWood", 'Y', "ingotIron"));
				GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselItems.diamondChisel), " Y", "X ", 'X', "stickWood", 'Y', "gemDiamond"));
			}
		}
	},

	CLOUD {

		@Override
		void addBlocks() {
			BlockCarvable cloud = (BlockCloud) new BlockCloud().setHardness(0.2F).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setLightOpacity(3).setStepSound(Block.soundTypeCloth);
			cloud.carverHelper.addVariation(StatCollector.translateToLocal("tile.cloud.0.desc"), 0, "cloud/cloud");
			cloud.carverHelper.addVariation(StatCollector.translateToLocal("tile.cloud.1.desc"), 1, "cloud/large");
			cloud.carverHelper.addVariation(StatCollector.translateToLocal("tile.cloud.2.desc"), 2, "cloud/small");
			cloud.carverHelper.addVariation(StatCollector.translateToLocal("tile.cloud.3.desc"), 3, "cloud/vertical");
			cloud.carverHelper.addVariation(StatCollector.translateToLocal("tile.cloud.4.desc"), 4, "cloud/grid");
			cloud.carverHelper.register(cloud, "cloud");
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
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.0.desc"), 1, "cobblestone/terrain-cobb-brickaligned");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.1.desc"), 2, "cobblestone/terrain-cob-detailedbrick");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.2.desc"), 3, "cobblestone/terrain-cob-smallbrick");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.3.desc"), 4, "cobblestone/terrain-cobblargetiledark");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.4.desc"), 5, "cobblestone/terrain-cobbsmalltile");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.5.desc"), 6, "cobblestone/terrain-cob-french");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.6.desc"), 7, "cobblestone/terrain-cob-french2");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.7.desc"), 8, "cobblestone/terrain-cobmoss-creepdungeon");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.8.desc"), 9, "cobblestone/terrain-mossysmalltiledark");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.9.desc"), 10, "cobblestone/terrain-pistonback-dungeontile");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.10.desc"), 11, "cobblestone/terrain-pistonback-darkcreeper");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.11.desc"), 12, "cobblestone/terrain-pistonback-darkdent");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.12.desc"), 13, "cobblestone/terrain-pistonback-darkemboss");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.13.desc"), 14, "cobblestone/terrain-pistonback-darkmarker");
			cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.cobblestone.14.desc"), 15, "cobblestone/terrain-pistonback-darkpanel");
			cobblestone.carverHelper.register(cobblestone, "cobblestone");
			Carving.chisel.registerOre("cobblestone", "cobblestone");
		}
	},

	COBBLESTONE_MOSSY {

		@Override
		void addBlocks() {

			BlockCarvable mossy_cobblestone = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10.0F)
					.setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("mossy_cobblestone", Blocks.mossy_cobblestone, 0, 0);
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.1.desc"), 1, "cobblestonemossy/terrain-cobb-brickaligned");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.2.desc"), 2, "cobblestonemossy/terrain-cob-detailedbrick");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.3.desc"), 3, "cobblestonemossy/terrain-cob-smallbrick");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.4.desc"), 4, "cobblestonemossy/terrain-cobblargetiledark");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.5.desc"), 5, "cobblestonemossy/terrain-cobbsmalltile");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.6.desc"), 6, "cobblestonemossy/terrain-cob-french");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.7.desc"), 7, "cobblestonemossy/terrain-cob-french2");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.8.desc"), 8, "cobblestonemossy/terrain-cobmoss-creepdungeon");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.9.desc"), 9, "cobblestonemossy/terrain-mossysmalltiledark");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.10.desc"), 10, "cobblestonemossy/terrain-pistonback-dungeontile");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.11.desc"), 11, "cobblestonemossy/terrain-pistonback-darkcreeper");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.12.desc"), 12, "cobblestonemossy/terrain-pistonback-darkdent");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.13.desc"), 13, "cobblestonemossy/terrain-pistonback-darkemboss");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.14.desc"), 14, "cobblestonemossy/terrain-pistonback-darkmarker");
			mossy_cobblestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneMoss.15.desc"), 15, "cobblestonemossy/terrain-pistonback-darkpanel");
			mossy_cobblestone.carverHelper.register(mossy_cobblestone, "mossy_cobblestone");
			Carving.chisel.registerOre("mossy_cobblestone", "mossy_cobblestone");
		}
	},

	COLORED_SAND {

		@Override
		void addBlocks() {
			BlockCarvableSand colored_sand = (BlockCarvableSand) new BlockCarvableSand().setCreativeTab(ChiselTabs.tabOtherChiselBlocks);
			colored_sand.carverHelper.setChiselBlockName("Colored Sand");

			for (int i = 0; i < 16; i++)
				colored_sand.carverHelper.addVariation(StatCollector.translateToLocal("tile.coloredSand." + i + ".desc"), i, "coloredSand/" + sGNames[i].replaceAll(" ", "").toLowerCase());
			colored_sand.carverHelper.register(colored_sand, "colored_sand");

			Carving.chisel.registerOre("colored_sand", "colored_sand");
		}

		final String[] dyes = { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.colored_sand, 1, meta), "sss", "sds", "sss", 's', Blocks.sand, 'd', "dye" + dyes[meta]));
			GameRegistry.addSmelting(new ItemStack(ChiselBlocks.colored_sand, 1, meta), new ItemStack(Blocks.stained_glass, 1, meta), 0.3F);
		}

		@Override
		boolean needsMetaRecipes() {
			return true;
		}
	},

	CONCRETE {

		@Override
		void addBlocks() {
			BlockCarvable concrete = (BlockConcrete) new BlockConcrete().setStepSound(Block.soundTypeStone).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(0.5F);
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.0.desc"), 0, "concrete/default");
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.1.desc"), 1, "concrete/block");
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.2.desc"), 2, "concrete/doubleslab");
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.3.desc"), 3, "concrete/blocks");
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.4.desc"), 4, "concrete/weathered");
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.5.desc"), 5, "concrete/weathered-block");
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.6.desc"), 6, "concrete/weathered-doubleslab");
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.7.desc"), 7, "concrete/weathered-blocks");
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.8.desc"), 8, "concrete/weathered-half");
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.9.desc"), 9, "concrete/weathered-block-half");
			concrete.carverHelper.addVariation(StatCollector.translateToLocal("tile.concrete.10.desc"), 10, "concrete/asphalt");
			concrete.carverHelper.register(concrete, "concrete");
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

	DIAMOND_BLOCK {

		@Override
		void addBlocks() {

			BlockCarvable diamond_block = (BlockBeaconBase) new BlockBeaconBase().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("diamond_block", Blocks.diamond_block, 0, 0);
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.1.desc"), 1, "diamond/terrain-diamond-embossed");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.2.desc"), 2, "diamond/terrain-diamond-gem");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.3.desc"), 3, "diamond/terrain-diamond-cells");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.4.desc"), 4, "diamond/terrain-diamond-space");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.5.desc"), 5, "diamond/terrain-diamond-spaceblack");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.6.desc"), 6, "diamond/terrain-diamond-simple");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.7.desc"), 7, "diamond/terrain-diamond-bismuth");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.8.desc"), 8, "diamond/terrain-diamond-crushed");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.9.desc"), 9, "diamond/terrain-diamond-four");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.10.desc"), 10, "diamond/terrain-diamond-fourornate");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.11.desc"), 11, "diamond/terrain-diamond-zelda");
			diamond_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.diamond.12.desc"), 12, "diamond/terrain-diamond-ornatelayer");
			diamond_block.carverHelper.register(diamond_block, "diamond_block");
			Carving.chisel.registerOre("diamond_block", "diamond");
		}
	},

	DIORITE {

		@Override
		void addBlocks() {
			BlockCarvable diorite = (BlockCarvable) new BlockCarvable(Material.rock).setHardness(2.0F).setResistance(10.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks);
			diorite.carverHelper.setChiselBlockName("diorite");
			diorite.carverHelper.addVariation(StatCollector.translateToLocal("tile.diorite.0.desc"), 0, "diorite/diorite");
			diorite.carverHelper.addVariation(StatCollector.translateToLocal("tile.diorite.1.desc"), 1, "diorite/polishedDiorite");
			diorite.carverHelper.register(diorite, "diorite");
			Carving.chisel.registerOre("diorite", "diorite");
		}
	},

	DIRT {

		@Override
		void addBlocks() {
			BlockCarvable dirt = (BlockCarvable) new BlockCarvable(Material.ground).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setStepSound(Block.soundTypeGravel)
					.setBlockName("dirt.default");
			Carving.chisel.addVariation("dirt", Blocks.dirt, 0, 0);
			dirt.carverHelper.setChiselBlockName("Dirt");
			// dirt.carverHelper.addVariation("Dirt", 0, Blocks.dirt);
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.0.desc"), 0, "dirt/bricks");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.1.desc"), 1, "dirt/netherbricks");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.2.desc"), 2, "dirt/bricks3");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.3.desc"), 3, "dirt/cobble");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.4.desc"), 4, "dirt/reinforcedCobbleDirt");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.5.desc"), 5, "dirt/reinforcedDirt");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.6.desc"), 6, "dirt/happy");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.7.desc"), 7, "dirt/bricks2");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.8.desc"), 8, "dirt/bricks+dirt2");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.9.desc"), 9, "dirt/hor");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.10.desc"), 10, "dirt/vert");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.11.desc"), 11, "dirt/layers");
			dirt.carverHelper.addVariation(StatCollector.translateToLocal("tile.dirt.12.desc"), 12, "dirt/vertical");
			dirt.carverHelper.register(dirt, "dirt");
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
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.1.desc"), 1, "emerald/panel");
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.2.desc"), 2, "emerald/panelclassic");
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.3.desc"), 3, "emerald/smooth");
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.4.desc"), 4, "emerald/chunk");
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.5.desc"), 5, "emerald/goldborder");
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.6.desc"), 6, "emerald/zelda");
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.7.desc"), 7, "emerald/cell");
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.8.desc"), 8, "emerald/cellbismuth");
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.9.desc"), 9, "emerald/four");
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.10.desc"), 10, "emerald/fourornate");
			emerald_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.emerald.11.desc"), 11, "emerald/ornate");
			emerald_block.carverHelper.register(emerald_block, "emerald_block");
			Carving.chisel.registerOre("emerald_block", "emerald");
		}
	},

	FACTORY {

		@Override
		void addBlocks() {
			BlockCarvable factoryblock = (BlockCarvable) new BlockCarvable(Material.iron).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Chisel.soundMetalFootstep);
			factoryblock.carverHelper.setChiselBlockName("factoryblock");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.0.desc"), 0, "factory/dots");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.1.desc"), 1, "factory/rust2");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.2.desc"), 2, "factory/rust");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.3.desc"), 3, "factory/platex");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.4.desc"), 4, "factory/wireframewhite");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.5.desc"), 5, "factory/wireframe");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.6.desc"), 6, "factory/hazard");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.7.desc"), 7, "factory/hazardorange");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.8.desc"), 8, "factory/circuit");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.9.desc"), 9, "factory/metalbox");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.10.desc"), 10, "factory/goldplate");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.11.desc"), 11, "factory/goldplating");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.12.desc"), 12, "factory/grinder");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.13.desc"), 13, "factory/plating");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.14.desc"), 14, "factory/rustplates");
			factoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.factory.15.desc"), 15, "factory/column");
			factoryblock.carverHelper.register(factoryblock, "factoryblock");
			Carving.chisel.registerOre("factoryblock", "factoryblock");
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
			fantasyblock.carverHelper.setChiselBlockName("Fantasy Block");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.0.desc"), 0, "fantasy/brick");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.1.desc"), 1, "fantasy/brick-faded");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.2.desc"), 2, "fantasy/brick-wear");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.3.desc"), 3, "fantasy/bricks");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.4.desc"), 4, "fantasy/decor");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.5.desc"), 5, "fantasy/decor-block");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.6.desc"), 6, "fantasy/pillar");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.7.desc"), 7, "fantasy/pillar-decorated");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.8.desc"), 8, "fantasy/gold-decor-1");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.9.desc"), 9, "fantasy/gold-decor-2");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.10.desc"), 10, "fantasy/gold-decor-3");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.11.desc"), 11, "fantasy/gold-decor-4");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.12.desc"), 12, "fantasy/plate");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.13.desc"), 13, "fantasy/block");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.14.desc"), 14, "fantasy/bricks-chaotic");
			fantasyblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock.15.desc"), 15, "fantasy/bricks-wear");
			fantasyblock.carverHelper.register(fantasyblock, "fantasyblock");
			OreDictionary.registerOre("fantasy", fantasyblock);
			Carving.chisel.registerOre("fantasyblock", "fantasy");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.fantasyblock, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.gold_nugget, 1));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.fantasyblock2, 8, 0), "***", "*X*", "***", '*', new ItemStack(ChiselBlocks.fantasyblock, 1), 'X', "dyeWhite"));
		}
	},

	FANTASY2 {

		@Override
		void addBlocks() {
			BlockCarvable fantasyblock2 = (BlockCarvable) new BlockCarvable().setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F);
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.0.desc"), 0, "fantasy2/brick");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.1.desc"), 1, "fantasy2/brick-faded");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.2.desc"), 2, "fantasy2/brick-wear");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.3.desc"), 3, "fantasy2/bricks");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.4.desc"), 4, "fantasy2/decor");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.5.desc"), 5, "fantasy2/decor-block");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.6.desc"), 6, "fantasy2/pillar");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.7.desc"), 7, "fantasy2/pillar-decorated");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.8.desc"), 8, "fantasy2/gold-decor-1");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.9.desc"), 9, "fantasy2/gold-decor-2");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.10.desc"), 10, "fantasy2/gold-decor-3");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.11.desc"), 11, "fantasy2/gold-decor-4");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.12.desc"), 12, "fantasy2/plate");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.13.desc"), 13, "fantasy2/block");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.14.desc"), 14, "fantasy2/bricks-chaotic");
			fantasyblock2.carverHelper.addVariation(StatCollector.translateToLocal("tile.fantasyblock2.15.desc"), 15, "fantasy2/bricks-wear");
			fantasyblock2.carverHelper.register(fantasyblock2, "fantasyblock2");
			OreDictionary.registerOre("fantasy2", fantasyblock2);
			Carving.chisel.registerOre("fantasyblock2", "fantasy2");
		}
	},

	FUTURA {

		// TODO Overlay is WIP
		@Override
		void addBlocks() {
			BlockCarvable futura = (BlockCarvable) new BlockCarvable(Material.rock/* , "futura/screenOverlay-ctm" */).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F)
					.setResistance(10F);
			futura.carverHelper.addVariation(StatCollector.translateToLocal("tile.futura.0.desc"), 0, "futura/WIP/screenMetallicWIP");
			futura.carverHelper.addVariation(StatCollector.translateToLocal("tile.futura.1.desc"), 1, "futura/WIP/screenCyanWIP");
            futura.carverHelper.addVariation(StatCollector.translateToLocal("tile.futura.2.desc"), 2, "futura/WIP/controllerWIP");
            futura.carverHelper.addVariation(StatCollector.translateToLocal("tile.futura.3.desc"), 3, "futura/WIP/wavyWIP");
			futura.carverHelper.register(futura, "futura");
			Carving.chisel.registerOre("futura", "futura");
		}
	},

	GLASS {

		@Override
		void addBlocks() {
			BlockCarvableGlass glass = (BlockCarvableGlass) new BlockCarvableGlass().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.3F).setStepSound(Block.soundTypeGlass);
			Carving.chisel.addVariation("glass", Blocks.glass, 0, 0);
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.1.desc"), 1, "glass/terrain-glassbubble");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.2.desc"), 2, "glass/terrain-glass-chinese");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.3.desc"), 3, "glass/japanese");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.4.desc"), 4, "glass/terrain-glassdungeon");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.5.desc"), 5, "glass/terrain-glasslight");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.6.desc"), 6, "glass/terrain-glassnoborder");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.7.desc"), 7, "glass/terrain-glass-ornatesteel");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.8.desc"), 8, "glass/terrain-glass-screen");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.9.desc"), 9, "glass/terrain-glassshale");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.10.desc"), 10, "glass/terrain-glass-steelframe");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.11.desc"), 11, "glass/terrain-glassstone");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.12.desc"), 12, "glass/terrain-glassstreak");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.13.desc"), 13, "glass/terrain-glass-thickgrid");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.14.desc"), 14, "glass/terrain-glass-thingrid");
			glass.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass.15.desc"), 15, "glass/a1-glasswindow-ironfencemodern");
			glass.carverHelper.register(glass, "glass");
			Carving.chisel.registerOre("glass", "glass");
		}
	},

	GLASS_PANE {

		@Override
		void addBlocks() {
			BlockCarvablePane glass_pane = (BlockCarvablePane) new BlockCarvablePane(Material.glass, false).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.3F)
					.setStepSound(Block.soundTypeGlass);
			Carving.chisel.addVariation("glass_pane", Blocks.glass_pane, 0, 0);
			glass_pane.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass_pane.1.desc"), 1, "glasspane/terrain-glassbubble");
			glass_pane.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass_pane.2.desc"), 2, "glasspane/terrain-glassnoborder");
			glass_pane.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass_pane.3.desc"), 3, "glasspane/terrain-glass-screen");
			glass_pane.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass_pane.4.desc"), 4, "glasspane/terrain-glassstreak");
			glass_pane.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass_pane.12.desc"), 12, "glasspane/chinese");
			glass_pane.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass_pane.13.desc"), 13, "glasspane/chinese2");
			glass_pane.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass_pane.14.desc"), 14, "glasspane/japanese");
			glass_pane.carverHelper.addVariation(StatCollector.translateToLocal("tile.glass_pane.15.desc"), 15, "glasspane/japanese2");
			glass_pane.carverHelper.register(glass_pane, "glass_pane");
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
				for (CarvableVariation cv : stainedGlass[glassId].carverHelper.variations) {
					if (cv.metadata < glassPrefix || cv.metadata >= glassPrefix + 4)
						continue;
					stainedGlass[glassId].carverHelper.registerVariation(blockName, cv, stainedGlass[glassId], cv.metadata);
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
					stainedGlassPane[glassId].carverHelper.blockName = "Stained Glass Pane";
				}
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " bubble glass", glassPrefix, texName + "bubble");
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " glass panel", glassPrefix + 1, texName + "panel");
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " fancy glass panel", glassPrefix + 2, texName + "panel-fancy");
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " borderless glass", glassPrefix + 3, texName + "transparent");
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " quadrant glass", glassPrefix + 4, texName + "quad");
				stainedGlassPane[glassId].carverHelper.addVariation(sGNames[i] + " fancy quadrant glass", glassPrefix + 5, texName + "quad-fancy");
				OreDictionary.registerOre(oreName, new ItemStack(Blocks.stained_glass_pane, 1, i));
				Carving.chisel.registerOre(blockName, oreName);
				for (CarvableVariation cv : stainedGlassPane[glassId].carverHelper.variations) {
					if (cv.metadata < glassPrefix || cv.metadata >= glassPrefix + 8)
						continue;
					stainedGlassPane[glassId].carverHelper.registerVariation(blockName, cv, stainedGlassPane[glassId], cv.metadata);
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
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.1.desc"), 1, "lightstone/terrain-sulphur-cobble");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.2.desc"), 2, "lightstone/terrain-sulphur-corroded");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.3.desc"), 3, "lightstone/terrain-sulphur-glass");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.4.desc"), 4, "lightstone/terrain-sulphur-neon");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.5.desc"), 5, "lightstone/terrain-sulphur-ornate");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.6.desc"), 6, "lightstone/terrain-sulphur-rocky");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.7.desc"), 7, "lightstone/terrain-sulphur-shale");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.8.desc"), 8, "lightstone/terrain-sulphur-tile");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.9.desc"), 9, "lightstone/terrain-sulphur-weavelanternlight");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.10.desc"), 10, "lightstone/a1-glowstone-cobble");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.11.desc"), 11, "lightstone/a1-glowstone-growth");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.12.desc"), 12, "lightstone/a1-glowstone-layers");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.13.desc"), 13, "lightstone/a1-glowstone-tilecorroded");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.14.desc"), 14, "lightstone/glowstone-bismuth");
			glowstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lightstone.15.desc"), 15, "lightstone/glowstone-bismuth-panel");
			glowstone.carverHelper.register(glowstone, "glowstone");
			Carving.chisel.registerOre("glowstone", "glowstone");
		}
	},

	GOLD_BLOCK {

		@Override
		void addBlocks() {
			BlockCarvable gold_block = (BlockBeaconBase) new BlockBeaconBase().setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(3F).setResistance(10F).setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("gold_block", Blocks.gold_block, 0, 0);
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.1.desc"), 1, "gold/terrain-gold-largeingot");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.2.desc"), 2, "gold/terrain-gold-smallingot");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.3.desc"), 3, "gold/terrain-gold-brick");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.4.desc"), 4, "gold/terrain-gold-cart");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.5.desc"), 5, "gold/terrain-gold-coin-heads");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.6.desc"), 6, "gold/terrain-gold-coin-tails");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.7.desc"), 7, "gold/terrain-gold-crate-dark");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.8.desc"), 8, "gold/terrain-gold-crate-light");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.9.desc"), 9, "gold/terrain-gold-plates");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.10.desc"), 10, "gold/terrain-gold-rivets");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.11.desc"), 11, "gold/terrain-gold-star");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.12.desc"), 12, "gold/terrain-gold-space");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.13.desc"), 13, "gold/terrain-gold-spaceblack");
			gold_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.gold.14.desc"), 14, "gold/terrain-gold-simple");
			gold_block.carverHelper.register(gold_block, "gold_block");
			Carving.chisel.registerOre("gold_block", "gold");
		}
	},

	GRANITE {

		@Override
		void addBlocks() {
			BlockCarvable granite = (BlockCarvable) new BlockCarvable(Material.rock).setHardness(2.0F).setResistance(10.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks);
			granite.carverHelper.setChiselBlockName("granite");
			granite.carverHelper.addVariation(StatCollector.translateToLocal("tile.granite.0.desc"), 0, "granite/granite");
			granite.carverHelper.addVariation(StatCollector.translateToLocal("tile.granite.1.desc"), 1, "granite/polishedGranite");
			granite.carverHelper.register(granite, "granite");
			Carving.chisel.registerOre("granite", "granite");
		}
	},

	GRIMSTONE {

		@Override
		void addBlocks() {
			BlockCarvable grimstone = (BlockGrimstone) new BlockGrimstone(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F);
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.0.desc"), 0, "grimstone/grimstone");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.1.desc"), 1, "grimstone/smooth");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.2.desc"), 2, "grimstone/hate");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.3.desc"), 3, "grimstone/chiseled");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.4.desc"), 4, "grimstone/blocks");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.5.desc"), 5, "grimstone/blocks-rough");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.6.desc"), 6, "grimstone/brick");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.7.desc"), 7, "grimstone/largebricks");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.8.desc"), 8, "grimstone/platform");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.9.desc"), 9, "grimstone/platform-tiles");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.10.desc"), 10, "grimstone/construction");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.11.desc"), 11, "grimstone/fancy-tiles");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.12.desc"), 12, "grimstone/plate");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.13.desc"), 13, "grimstone/plate-rough");
			grimstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.grimstone.14.desc"), 14, "grimstone/flaky");
			grimstone.carverHelper.register(grimstone, "grimstone");
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
			BlockCarvable hexPlating = (BlockCarvable) new BlockCarvableGlow("hexPlating/hexAnim").setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10.0F);
			for (int i = 0; i < 16; i++) {
				hexPlating.carverHelper.addVariation(StatCollector.translateToLocal("tile.hexPlating." + ItemDye.field_150921_b[i] + ".desc"), i, "hexPlating/hexBase");
			}
			hexPlating.carverHelper.register(hexPlating, "hexPlating");
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
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.0.desc"), 0, "holystone/holystone");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.1.desc"), 1, "holystone/smooth");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.2.desc"), 2, "holystone/love");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.3.desc"), 3, "holystone/chiseled");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.4.desc"), 4, "holystone/blocks");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.5.desc"), 5, "holystone/blocks-rough");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.6.desc"), 6, "holystone/brick");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.7.desc"), 7, "holystone/largebricks");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.8.desc"), 8, "holystone/platform");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.9.desc"), 9, "holystone/platform-tiles");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.10.desc"), 10, "holystone/construction");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.11.desc"), 11, "holystone/fancy-tiles");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.12.desc"), 12, "holystone/plate");
			holystone.carverHelper.addVariation(StatCollector.translateToLocal("tile.holystone.13.desc"), 13, "holystone/plate-rough");
			holystone.carverHelper.register(holystone, "holystone");
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
			BlockMarbleIce ice = (BlockMarbleIce) new BlockMarbleIce().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass);
			Carving.chisel.addVariation("ice", Blocks.ice, 0, 0);
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.1.desc"), 1, "ice/a1-ice-light");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.2.desc"), 2, "ice/a1-stonecobble-icecobble");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.3.desc"), 3, "ice/a1-netherbrick-ice");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.4.desc"), 4, "ice/a1-stonecobble-icebrick");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.5.desc"), 5, "ice/a1-stonecobble-icebricksmall");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.6.desc"), 6, "ice/a1-stonecobble-icedungeon");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.7.desc"), 7, "ice/a1-stonecobble-icefour");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.8.desc"), 8, "ice/a1-stonecobble-icefrench");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.9.desc"), 9, "ice/sunkentiles");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.10.desc"), 10, "ice/tiles");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.11.desc"), 11, "ice/a1-stonecobble-icepanel");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.12.desc"), 12, "ice/a1-stoneslab-ice");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.13.desc"), 13, "ice/zelda");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.14.desc"), 14, "ice/bismuth");
			ice.carverHelper.addVariation(StatCollector.translateToLocal("tile.ice.15.desc"), 15, "ice/poison");
			ice.carverHelper.register(ice, "ice");
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
			BlockMarbleIce ice_pillar = (BlockMarbleIce) new BlockMarbleIce().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setLightOpacity(3).setStepSound(Block.soundTypeGlass);
			ice_pillar.carverHelper.setChiselBlockName("Ice Pillar");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.0.desc"), 0, "icepillar/column");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.1.desc"), 1, "icepillar/capstone");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.2.desc"), 2, "icepillar/base");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.3.desc"), 3, "icepillar/small");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.4.desc"), 4, "icepillar/pillar-carved");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.5.desc"), 5, "icepillar/a1-stoneornamental-marblegreek");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.6.desc"), 6, "icepillar/a1-stonepillar-greek");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.7.desc"), 7, "icepillar/a1-stonepillar-plain");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.8.desc"), 8, "icepillar/a1-stonepillar-greektopplain");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.9.desc"), 9, "icepillar/a1-stonepillar-plaintopplain");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.10.desc"), 10, "icepillar/a1-stonepillar-greekbottomplain");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.11.desc"), 11, "icepillar/a1-stonepillar-plainbottomplain");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.12.desc"), 12, "icepillar/a1-stonepillar-greektopgreek");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.13.desc"), 13, "icepillar/a1-stonepillar-plaintopgreek");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.14.desc"), 14, "icepillar/a1-stonepillar-greekbottomgreek");
			ice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.icePillar.15.desc"), 15, "icepillar/a1-stonepillar-plainbottomgreek");
			ice_pillar.carverHelper.register(ice_pillar, "ice_pillar");
			Carving.chisel.setGroupClass("ice_pillar", "ice");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.ice_pillar, 6, 1), "XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.ice, 1, OreDictionary.WILDCARD_VALUE));
		}
	},

	ICE_STAIRS(ICE) {

		@Override
		void addBlocks() {
			BlockMarbleStairsMaker makerIceStairs = new BlockMarbleStairsMaker(Blocks.ice);
			makerIceStairs.carverHelper.setChiselBlockName("Ice Stairs");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.0.desc"), 0, Blocks.ice);
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.1.desc"), 1, "ice/a1-ice-light");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.2.desc"), 2, "ice/a1-stonecobble-icecobble");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.3.desc"), 3, "ice/a1-netherbrick-ice");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.4.desc"), 4, "ice/a1-stonecobble-icebrick");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.5.desc"), 5, "ice/a1-stonecobble-icebricksmall");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.6.desc"), 6, "ice/a1-stonecobble-icedungeon");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.7.desc"), 7, "ice/a1-stonecobble-icefour");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.8.desc"), 8, "ice/a1-stonecobble-icefrench");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.9.desc"), 9, "ice/sunkentiles");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.10.desc"), 10, "ice/tiles");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.11.desc"), 11, "ice/a1-stonecobble-icepanel");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.12.desc"), 12, "ice/a1-stoneslab-ice");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.13.desc"), 13, "ice/zelda");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.14.desc"), 14, "ice/bismuth");
			makerIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.iceStairs.15.desc"), 15, "ice/poison");
			makerIceStairs.create(new BlockMarbleStairsMakerCreator() {

				@Override
				public BlockCarvableStairs create(Block block, int meta, CarvableHelper helper) {
					return new BlockMarbleIceStairs(block, meta, helper);
				}
			}, "ice_stairs");
			Carving.chisel.registerOre("iceStairs", "iceStairs");
		}
	},

	IRON_BARS {

		@Override
		void addBlocks() {
			BlockCarvablePane iron_bars = (BlockCarvablePane) new BlockCarvablePane(Material.iron, true).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.3F)
					.setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("iron_bars", Blocks.iron_bars, 0, 0);
			iron_bars.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron_bars.1.desc"), 1, "ironpane/fenceIron");
			iron_bars.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron_bars.2.desc"), 2, "ironpane/barbedwire");
			iron_bars.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron_bars.3.desc"), 3, "ironpane/cage");
			iron_bars.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron_bars.4.desc"), 4, "ironpane/fenceIronTop");
			iron_bars.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron_bars.5.desc"), 5, "ironpane/terrain-glass-thickgrid");
			iron_bars.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron_bars.6.desc"), 6, "ironpane/terrain-glass-thingrid");
			iron_bars.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron_bars.7.desc"), 7, "ironpane/terrain-glass-ornatesteel");
			iron_bars.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron_bars.8.desc"), 8, "ironpane/bars");
			iron_bars.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron_bars.9.desc"), 9, "ironpane/spikes");
			iron_bars.carverHelper.register(iron_bars, "iron_bars");
			Carving.chisel.registerOre("iron_bars", "iron_bars");
		}
	},

	IRON_BLOCK {

		@Override
		void addBlocks() {

			BlockCarvable iron_block = (BlockBeaconBase) new BlockBeaconBase().setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("iron_block", Blocks.iron_block, 0, 0);
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.1.desc"), 1, "iron/terrain-iron-largeingot");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.2.desc"), 2, "iron/terrain-iron-smallingot");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.3.desc"), 3, "iron/terrain-iron-gears");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.4.desc"), 4, "iron/terrain-iron-brick");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.5.desc"), 5, "iron/terrain-iron-plates");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.6.desc"), 6, "iron/terrain-iron-rivets");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.7.desc"), 7, "iron/terrain-iron-coin-heads");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.8.desc"), 8, "iron/terrain-iron-coin-tails");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.9.desc"), 9, "iron/terrain-iron-crate-dark");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.10.desc"), 10, "iron/terrain-iron-crate-light");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.11.desc"), 11, "iron/terrain-iron-moon");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.12.desc"), 12, "iron/terrain-iron-space");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.13.desc"), 13, "iron/terrain-iron-spaceblack");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.14.desc"), 14, "iron/terrain-iron-vents");
			iron_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.iron.15.desc"), 15, "iron/terrain-iron-simple");
			iron_block.carverHelper.register(iron_block, "iron_block");
			Carving.chisel.registerOre("iron_block", "iron");
		}
	},

	JACKOLANTERN {

		@Override
		void addBlocks() {
			for (int metadata = 0; metadata < 16; metadata++) {
				jackolantern[metadata] = (BlockCarvablePumpkin) new BlockCarvablePumpkin(true).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(1.0F).setBlockName("litpumpkin")
						.setCreativeTab(ChiselTabs.tabOtherChiselBlocks);
				jackolantern[metadata].setInformation("pumpkin/pumpkin_face_" + (metadata + 1) + "_on");
				GameRegistry.registerBlock(jackolantern[metadata], ("jackolantern" + (metadata + 1)));
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
			laboratoryblock.carverHelper.setChiselBlockName("laboratoryblock");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.0.desc"), 0, "laboratory/wallpanel");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.1.desc"), 1, "laboratory/dottedpanel");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.2.desc"), 2, "laboratory/largewall");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.3.desc"), 3, "laboratory/roundel");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.4.desc"), 4, "laboratory/wallvents");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.5.desc"), 5, "laboratory/largetile");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.6.desc"), 6, "laboratory/smalltile");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.7.desc"), 7, "laboratory/floortile");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.8.desc"), 8, "laboratory/checkertile");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.9.desc"), 9, "laboratory/clearscreen");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.10.desc"), 10, "laboratory/fuzzscreen");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.11.desc"), 11, "laboratory/largesteel");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.12.desc"), 12, "laboratory/smallsteel");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.13.desc"), 13, "laboratory/directionright");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.14.desc"), 14, "laboratory/directionleft");
			laboratoryblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.laboratory.15.desc"), 15, "laboratory/infocon");
			laboratoryblock.carverHelper.register(laboratoryblock, "laboratoryblock");
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
			lapis_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.lapis.1.desc"), 1, "lapis/terrain-lapisblock-chunky");
			lapis_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.lapis.2.desc"), 2, "lapis/terrain-lapisblock-panel");
			lapis_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.lapis.3.desc"), 3, "lapis/terrain-lapisblock-zelda");
			lapis_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.lapis.4.desc"), 4, "lapis/terrain-lapisornate");
			lapis_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.lapis.5.desc"), 5, "lapis/terrain-lapistile");
			lapis_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.lapis.6.desc"), 6, "lapis/a1-blocklapis-panel");
			lapis_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.lapis.7.desc"), 7, "lapis/a1-blocklapis-smooth");
			lapis_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.lapis.8.desc"), 8, "lapis/a1-blocklapis-ornatelayer");
			lapis_block.carverHelper.register(lapis_block, "lapis_block");
			Carving.chisel.registerOre("lapis_block", "lapis");
		}
	},

	LAVASTONE {

		@Override
		void addBlocks() {
			BlockLavastone lavastone = (BlockLavastone) new BlockLavastone(Material.rock, "lava_flow").setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F);
			lavastone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lavastone.0.desc"), 0, "lavastone/cobble");
			lavastone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lavastone.1.desc"), 1, "lavastone/black");
			lavastone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lavastone.2.desc"), 2, "lavastone/tiles");
			lavastone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lavastone.3.desc"), 3, "lavastone/chaotic");
			lavastone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lavastone.4.desc"), 4, "lavastone/creeper");
			lavastone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lavastone.5.desc"), 5, "lavastone/panel");
			lavastone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lavastone.6.desc"), 6, "lavastone/panel-ornate");
			lavastone.carverHelper.addVariation(StatCollector.translateToLocal("tile.lavastone.7.desc"), 7, "lavastone/dark");
			lavastone.carverHelper.register(lavastone, "lavastone");
			OreDictionary.registerOre("lavastone", lavastone);
			Carving.chisel.registerOre("lavastone", "lavastone");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.lavastone, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.lava_bucket, 1));
		}
	},

	LEAVES {

		@Override
		void addBlocks() {
			BlockLeaf leaves = (BlockLeaf) new BlockLeaf(Material.leaves).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.2F).setStepSound(Block.soundTypeGrass);
			Carving.chisel.addVariation("leaves", Blocks.leaves, 0, 0);
			Carving.chisel.addVariation("leaves", Blocks.leaves, 1, 0);
			Carving.chisel.addVariation("leaves", Blocks.leaves, 2, 0);
			Carving.chisel.addVariation("leaves", Blocks.leaves, 3, 0);
			Carving.chisel.addVariation("leaves", Blocks.leaves2, 0, 0);
			Carving.chisel.addVariation("leaves", Blocks.leaves2, 1, 0);
			if (Configurations.fancy) {
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.6.desc"), 6, "leaves/dead");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.7.desc"), 7, "leaves/fancy");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.8.desc"), 8, "leaves/pinkpetal");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.9.desc"), 9, "leaves/red_roses");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.10.desc"), 10, "leaves/roses");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.11.desc"), 11, "leaves/christmasBalls");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.12.desc"), 12, "leaves/christmasLights");
			} else {
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.6.desc"), 6, "leaves/dead_opaque");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.7.desc"), 7, "leaves/fancy_opaque");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.8.desc"), 8, "leaves/pinkpetal_opaque");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.9.desc"), 9, "leaves/red_roses_opaque");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.10.desc"), 10, "leaves/roses_opaque");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.11.desc"), 11, "leaves/christmasBalls_opaque");
				leaves.carverHelper.addVariation(StatCollector.translateToLocal("tile.leaves.12.desc"), 12, "leaves/christmasLights_opaque");
			}

			leaves.carverHelper.register(leaves, "leaves");
			Carving.chisel.registerOre("leaves", "leaves");
		}
	},

	LIMESTONE {

		@Override
		void addBlocks() {
			BlockCarvable limestone = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Block.soundTypeStone);
			limestone.carverHelper.setChiselBlockName("Limestone");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.0.desc"), 0, "limestone");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.1.desc"), 1, "limestone/terrain-cobbsmalltilelight");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.2.desc"), 2, "limestone/terrain-cob-frenchlight");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.3.desc"), 3, "limestone/terrain-cob-french2light");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.4.desc"), 4, "limestone/terrain-cobmoss-creepdungeonlight");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.5.desc"), 5, "limestone/terrain-cob-smallbricklight");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.6.desc"), 6, "limestone/terrain-mossysmalltilelight");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.7.desc"), 7, "limestone/terrain-pistonback-dungeon");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.8.desc"), 8, "limestone/terrain-pistonback-dungeonornate");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.9.desc"), 9, "limestone/terrain-pistonback-dungeonvent");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.10.desc"), 10, "limestone/terrain-pistonback-lightcreeper");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.11.desc"), 11, "limestone/terrain-pistonback-lightdent");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.12.desc"), 12, "limestone/terrain-pistonback-lightemboss");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.13.desc"), 13, "limestone/terrain-pistonback-lightfour");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.14.desc"), 14, "limestone/terrain-pistonback-lightmarker");
			limestone.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestone.15.desc"), 15, "limestone/terrain-pistonback-lightpanel");
			limestone.carverHelper.register(limestone, "limestone");
			OreDictionary.registerOre("limestone", limestone);
			Carving.chisel.registerOre("limestone", "limestone");

			BlockCarvable limestone_slab = (BlockCarvableSlab) new BlockCarvableSlab(limestone).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F);
			limestone_slab.carverHelper.setChiselBlockName("Limestone Slab");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.0.desc"), 0, "limestone");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.1.desc"), 1, "limestone/terrain-cobbsmalltilelight");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.2.desc"), 2, "limestone/terrain-cob-frenchlight");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.3.desc"), 3, "limestone/terrain-cob-french2light");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.4.desc"), 4, "limestone/terrain-cobmoss-creepdungeonlight");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.5.desc"), 5, "limestone/terrain-cob-smallbricklight");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.6.desc"), 6, "limestone/terrain-mossysmalltilelight");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.7.desc"), 7, "limestone/terrain-pistonback-dungeon");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.8.desc"), 8, "limestone/terrain-pistonback-dungeonornate");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.9.desc"), 9, "limestone/terrain-pistonback-dungeonvent");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.10.desc"), 10, "limestone/terrain-pistonback-lightcreeper");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.11.desc"), 11, "limestone/terrain-pistonback-lightdent");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.12.desc"), 12, "limestone/terrain-pistonback-lightemboss");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.13.desc"), 13, "limestone/terrain-pistonback-lightfour");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.14.desc"), 14, "limestone/terrain-pistonback-lightmarker");
			limestone_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneSlab.15.desc"), 15, "limestone/terrain-pistonback-lightpanel");
			limestone_slab.carverHelper.register(limestone_slab, "limestone_slab", ItemMarbleSlab.class);
			Carving.chisel.registerOre("limestone_slab", "limestone_slab");

			BlockMarbleStairsMaker makerLimestoneStairs = new BlockMarbleStairsMaker(limestone);
			makerLimestoneStairs.carverHelper.setChiselBlockName("Limestone Stairs");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.0.desc"), 0, "limestone");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.1.desc"), 1, "limestone/terrain-cobbsmalltilelight");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.2.desc"), 2, "limestone/terrain-cob-frenchlight");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.3.desc"), 3, "limestone/terrain-cob-french2light");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.4.desc"), 4, "limestone/terrain-cobmoss-creepdungeonlight");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.5.desc"), 5, "limestone/terrain-cob-smallbricklight");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.6.desc"), 6, "limestone/terrain-mossysmalltilelight");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.7.desc"), 7, "limestone/terrain-pistonback-dungeon");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.8.desc"), 8, "limestone/terrain-pistonback-dungeonornate");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.9.desc"), 9, "limestone/terrain-pistonback-dungeonvent");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.10.desc"), 10, "limestone/terrain-pistonback-lightcreeper");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.11.desc"), 11, "limestone/terrain-pistonback-lightdent");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.12.desc"), 12, "limestone/terrain-pistonback-lightemboss");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.13.desc"), 13, "limestone/terrain-pistonback-lightfour");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.14.desc"), 14, "limestone/terrain-pistonback-lightmarker");
			makerLimestoneStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.limestoneStairs.15.desc"), 15, "limestone/terrain-pistonback-lightpanel");
			makerLimestoneStairs.create("limestone_stairs");
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
			marble.carverHelper.setChiselBlockName("Marble");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.0.desc"), 0, "marble");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.1.desc"), 1, "marble/a1-stoneornamental-marblebrick");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.2.desc"), 2, "marble/a1-stoneornamental-marbleclassicpanel");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.3.desc"), 3, "marble/a1-stoneornamental-marbleornate");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.4.desc"), 4, "marble/panel");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.5.desc"), 5, "marble/block");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.6.desc"), 6, "marble/terrain-pistonback-marblecreeperdark");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.7.desc"), 7, "marble/terrain-pistonback-marblecreeperlight");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.8.desc"), 8, "marble/a1-stoneornamental-marblecarved");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.9.desc"), 9, "marble/a1-stoneornamental-marblecarvedradial");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.10.desc"), 10, "marble/terrain-pistonback-marbledent");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.11.desc"), 11, "marble/terrain-pistonback-marbledent-small");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.12.desc"), 12, "marble/marble-bricks");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.13.desc"), 13, "marble/marble-arranged-bricks");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.14.desc"), 14, "marble/marble-fancy-bricks");
			marble.carverHelper.addVariation(StatCollector.translateToLocal("tile.marble.15.desc"), 15, "marble/marble-blocks");
			marble.carverHelper.register(marble, "marble");
			OreDictionary.registerOre("marble", marble);
			OreDictionary.registerOre("blockMarble", marble);
			Carving.chisel.registerOre("marble", "marble");

			BlockCarvable marble_slab = (BlockCarvableSlab) new BlockCarvableSlab(marble).setHardness(2.0F).setResistance(10F);
			marble_slab.carverHelper.setChiselBlockName("Marble Slab");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.0.desc"), 0, "marble");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.1.desc"), 1, "marbleslab/a1-stoneornamental-marblebrick");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.2.desc"), 2, "marbleslab/a1-stoneornamental-marbleclassicpanel");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.3.desc"), 3, "marbleslab/a1-stoneornamental-marbleornate");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.4.desc"), 4, "marbleslab/a1-stoneornamental-marblepanel");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.5.desc"), 5, "marbleslab/terrain-pistonback-marble");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.6.desc"), 6, "marbleslab/terrain-pistonback-marblecreeperdark");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.7.desc"), 7, "marbleslab/terrain-pistonback-marblecreeperlight");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.8.desc"), 8, "marbleslab/a1-stoneornamental-marblecarved");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.9.desc"), 9, "marbleslab/a1-stoneornamental-marblecarvedradial");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.10.desc"), 10, "marbleslab/terrain-pistonback-marbledent");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.11.desc"), 11, "marbleslab/terrain-pistonback-marbledent-small");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.12.desc"), 12, "marbleslab/marble-bricks");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.13.desc"), 13, "marbleslab/marble-arranged-bricks");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.14.desc"), 14, "marbleslab/marble-fancy-bricks");
			marble_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleSlab.15.desc"), 15, "marbleslab/marble-blocks");
			marble_slab.carverHelper.register(marble_slab, "marble_slab", ItemMarbleSlab.class);
			Carving.chisel.registerOre("marble_slab", "marble_slab");

			BlockMarbleStairsMaker makerMarbleStairs = new BlockMarbleStairsMaker(marble);
			makerMarbleStairs.carverHelper.setChiselBlockName("Marble Stairs");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.0.desc"), 0, "marble");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.6.desc"), 1, "marbleslab/a1-stoneornamental-marblebrick");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.2.desc"), 2, "marbleslab/a1-stoneornamental-marbleclassicpanel");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.3.desc"), 3, "marbleslab/a1-stoneornamental-marbleornate");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.4.desc"), 4, "marbleslab/a1-stoneornamental-marblepanel");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.5.desc"), 5, "marbleslab/terrain-pistonback-marble");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.6.desc"), 6, "marbleslab/terrain-pistonback-marblecreeperdark");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.7.desc"), 7, "marbleslab/terrain-pistonback-marblecreeperlight");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.8.desc"), 8, "marbleslab/a1-stoneornamental-marblecarved");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.9.desc"), 9, "marbleslab/a1-stoneornamental-marblecarvedradial");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.10.desc"), 10, "marbleslab/terrain-pistonback-marbledent");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.11.desc"), 11, "marbleslab/terrain-pistonback-marbledent-small");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.12.desc"), 12, "marbleslab/marble-bricks");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.13.desc"), 13, "marbleslab/marble-arranged-bricks");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.14.desc"), 14, "marbleslab/marble-fancy-bricks");
			makerMarbleStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.marbleStairs.15.desc"), 15, "marbleslab/marble-blocks");
			makerMarbleStairs.create("marble_stairs");
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
				marble_pillar.carverHelper.setChiselBlockName("Marble Pillar");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.0.desc"), 0, "marblepillarold/column");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.1.desc"), 1, "marblepillarold/capstone");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.2.desc"), 2, "marblepillarold/base");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.3.desc"), 3, "marblepillarold/small");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.4.desc"), 4, "marblepillarold/pillar-carved");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.5.desc"), 5, "marblepillarold/a1-stoneornamental-marblegreek");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.6.desc"), 6, "marblepillarold/a1-stonepillar-greek");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.7.desc"), 7, "marblepillarold/a1-stonepillar-plain");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.8.desc"), 8, "marblepillarold/a1-stonepillar-greektopplain");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.9.desc"), 9, "marblepillarold/a1-stonepillar-plaintopplain");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.10.desc"), 10, "marblepillarold/a1-stonepillar-greekbottomplain");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.11.desc"), 11, "marblepillarold/a1-stonepillar-plainbottomplain");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.12.desc"), 12, "marblepillarold/a1-stonepillar-greektopgreek");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.13.desc"), 13, "marblepillarold/a1-stonepillar-plaintopgreek");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.14.desc"), 14, "marblepillarold/a1-stonepillar-greekbottomgreek");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarOld.15.desc"), 15, "marblepillarold/a1-stonepillar-plainbottomgreek");
			} else {
				marble_pillar = (BlockCarvable) new BlockMarblePillar(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F)
						.setStepSound(Block.soundTypeStone);
				marble_pillar.carverHelper.setChiselBlockName("Marble Pillar");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.0.desc"), 0, "marblepillar/pillar");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.1.desc"), 1, "marblepillar/default");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.2.desc"), 2, "marblepillar/simple");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.3.desc"), 3, "marblepillar/convex");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.4.desc"), 4, "marblepillar/rough");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.5.desc"), 5, "marblepillar/greekdecor");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.6.desc"), 6, "marblepillar/greekgreek");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.7.desc"), 7, "marblepillar/greekplain");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.8.desc"), 8, "marblepillar/plaindecor");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.9.desc"), 9, "marblepillar/plaingreek");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.10.desc"), 10, "marblepillar/plainplain");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.11.desc"), 11, "marblepillar/widedecor");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.12.desc"), 12, "marblepillar/widegreek");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.13.desc"), 13, "marblepillar/wideplain");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.14.desc"), 14, "marblepillar/carved");
				marble_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillar.15.desc"), 15, "marblepillar/ornamental");
			}
			marble_pillar.carverHelper.register(marble_pillar, "marble_pillar");
			Carving.chisel.setGroupClass("marble_pillar", "marble");

			BlockCarvable marble_pillar_slab = (BlockCarvableSlab) new BlockCarvableSlab(marble_pillar).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10F)
					.setStepSound(Block.soundTypeStone);
			marble_pillar_slab.carverHelper.setChiselBlockName("Marble Pillar Slab");
			if (Configurations.oldPillars) {
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.0.desc"), 0, "marblepillarslabold/column");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.1.desc"), 1, "marblepillarslabold/capstone");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.2.desc"), 2, "marblepillarslabold/base");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.3.desc"), 3, "marblepillarslabold/small");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.4.desc"), 4, "marblepillarslabold/pillar-carved");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.5.desc"), 5, "marblepillarslabold/a1-stoneornamental-marblegreek");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.6.desc"), 6, "marblepillarslabold/a1-stonepillar-greek");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.7.desc"), 7, "marblepillarslabold/a1-stonepillar-plain");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.8.desc"), 8, "marblepillarslabold/a1-stonepillar-greektopplain");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.9.desc"), 9, "marblepillarslabold/a1-stonepillar-plaintopplain");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.10.desc"), 10, "marblepillarslabold/a1-stonepillar-greekbottomplain");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.11.desc"), 11, "marblepillarslabold/a1-stonepillar-plainbottomplain");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.12.desc"), 12, "marblepillarslabold/a1-stonepillar-greektopgreek");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.13.desc"), 13, "marblepillarslabold/a1-stonepillar-plaintopgreek");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.14.desc"), 14, "marblepillarslabold/a1-stonepillar-greekbottomgreek");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlabOld.15.desc"), 15, "marblepillarslabold/a1-stonepillar-plainbottomgreek");
			} else {
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.0.desc"), 0, "marblepillarslab/pillar");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.1.desc"), 1, "marblepillarslab/default");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.2.desc"), 2, "marblepillarslab/simple");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.3.desc"), 3, "marblepillarslab/convex");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.4.desc"), 4, "marblepillarslab/rough");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.5.desc"), 5, "marblepillarslab/greekdecor");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.6.desc"), 6, "marblepillarslab/greekgreek");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.7.desc"), 7, "marblepillarslab/greekplain");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.8.desc"), 8, "marblepillarslab/plaindecor");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.9.desc"), 9, "marblepillarslab/plaingreek");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.10.desc"), 10, "marblepillarslab/plainplain");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.11.desc"), 11, "marblepillarslab/widedecor");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.12.desc"), 12, "marblepillarslab/widegreek");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.13.desc"), 13, "marblepillarslab/wideplain");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.14.desc"), 14, "marblepillarslab/carved");
				marble_pillar_slab.carverHelper.addVariation(StatCollector.translateToLocal("tile.marblePillarSlab.15.desc"), 15, "marblepillarslab/ornamental");
			}
			marble_pillar_slab.carverHelper.register(marble_pillar_slab, "marble_pillar_slab", ItemMarbleSlab.class);
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.marble_pillar, 6), "XX", "XX", "XX", 'X', new ItemStack(ChiselBlocks.marble, 1, OreDictionary.WILDCARD_VALUE));
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.marble_pillar_slab, 6, 0), "***", '*', new ItemStack(ChiselBlocks.marble_pillar, 1, OreDictionary.WILDCARD_VALUE));
		}
	},

	NETHER_BRICK {

		@Override
		void addBlocks() {
			BlockCarvable nether_brick = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10.0F)
					.setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("nether_brick", Blocks.nether_brick, 0, 0);
			// netherBrick.carverHelper.addVariation("Nether brick", 0, Blocks.nether_brick);
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.1.desc"), 1, "netherbrick/a1-netherbrick-brinstar");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.2.desc"), 2, "netherbrick/a1-netherbrick-classicspatter");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.3.desc"), 3, "netherbrick/a1-netherbrick-guts");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.4.desc"), 4, "netherbrick/a1-netherbrick-gutsdark");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.5.desc"), 5, "netherbrick/a1-netherbrick-gutssmall");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.6.desc"), 6, "netherbrick/a1-netherbrick-lavabrinstar");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.7.desc"), 7, "netherbrick/a1-netherbrick-lavabrown");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.8.desc"), 8, "netherbrick/a1-netherbrick-lavaobsidian");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.9.desc"), 9, "netherbrick/a1-netherbrick-lavastonedark");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.10.desc"), 10, "netherbrick/a1-netherbrick-meat");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.11.desc"), 11, "netherbrick/a1-netherbrick-meatred");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.12.desc"), 12, "netherbrick/a1-netherbrick-meatredsmall");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.13.desc"), 13, "netherbrick/a1-netherbrick-meatsmall");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.14.desc"), 14, "netherbrick/a1-netherbrick-red");
			nether_brick.carverHelper.addVariation(StatCollector.translateToLocal("tile.netherBrick.15.desc"), 15, "netherbrick/a1-netherbrick-redsmall");
			nether_brick.carverHelper.register(nether_brick, "nether_brick");
			Carving.chisel.registerOre("nether_brick", "nether_brick");
		}
	},

	NETHER_RACK {

		@Override
		void addBlocks() {

			BlockCarvable netherrack = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(0.4F).setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("netherrack", Blocks.netherrack, 0, 0);
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.1.desc"), 1, "netherrack/a1-netherrack-bloodgravel");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.2.desc"), 2, "netherrack/a1-netherrack-bloodrock");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.3.desc"), 3, "netherrack/a1-netherrack-bloodrockgrey");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.4.desc"), 4, "netherrack/a1-netherrack-brinstar");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.5.desc"), 5, "netherrack/a1-netherrack-brinstarshale");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.6.desc"), 6, "netherrack/a1-netherrack-classic");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.7.desc"), 7, "netherrack/a1-netherrack-classicspatter");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.8.desc"), 8, "netherrack/a1-netherrack-guts");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.9.desc"), 9, "netherrack/a1-netherrack-gutsdark");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.10.desc"), 10, "netherrack/a1-netherrack-meat");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.11.desc"), 11, "netherrack/a1-netherrack-meatred");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.12.desc"), 12, "netherrack/a1-netherrack-meatrock");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.13.desc"), 13, "netherrack/a1-netherrack-red");
			netherrack.carverHelper.addVariation(StatCollector.translateToLocal("tile.hellrock.14.desc"), 14, "netherrack/a1-netherrack-wells");
			netherrack.carverHelper.register(netherrack, "netherrack");
			Carving.chisel.registerOre("netherrack", "netherrack");
		}
	},

	OBSIDIAN {

		@Override
		void addBlocks() {
			BlockCarvable obsidian = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(50.0F).setResistance(2000.0F)
					.setStepSound(Block.soundTypeStone);
			Carving.chisel.addVariation("obsidian", Blocks.obsidian, 0, 0);
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.1.desc"), 1, "obsidian/pillar");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.2.desc"), 2, "obsidian/pillar-quartz");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.3.desc"), 3, "obsidian/chiseled");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.4.desc"), 4, "obsidian/panel-shiny");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.5.desc"), 5, "obsidian/panel");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.6.desc"), 6, "obsidian/chunks");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.7.desc"), 7, "obsidian/growth");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.8.desc"), 8, "obsidian/crystal");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.9.desc"), 9, "obsidian/map-a");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.10.desc"), 10, "obsidian/map-b");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.11.desc"), 11, "obsidian/panel-light");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.12.desc"), 12, "obsidian/blocks");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.13.desc"), 13, "obsidian/tiles");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.14.desc"), 14, "obsidian/greek");
			obsidian.carverHelper.addVariation(StatCollector.translateToLocal("tile.obsidian.15.desc"), 15, "obsidian/crate");
			obsidian.carverHelper.register(obsidian, "obsidian");
			Carving.chisel.registerOre("obsidian", "obsidian");
		}
	},

	PACKEDICE {

		@Override
		void addBlocks() {
			BlockMarblePackedIce packedice = (BlockMarblePackedIce) new BlockMarblePackedIce().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setLightOpacity(3)
					.setStepSound(Block.soundTypeGlass);
			Carving.chisel.addVariation("packedice", Blocks.packed_ice, 0, 0);
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.1.desc"), 1, "packedice/a1-ice-light");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.2.desc"), 2, "packedice/a1-stonecobble-icecobble");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.3.desc"), 3, "packedice/a1-netherbrick-ice");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.4.desc"), 4, "packedice/a1-stonecobble-icebrick");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.5.desc"), 5, "packedice/a1-stonecobble-icebricksmall");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.6.desc"), 6, "packedice/a1-stonecobble-icedungeon");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.7.desc"), 7, "packedice/a1-stonecobble-icefour");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.8.desc"), 8, "packedice/a1-stonecobble-icefrench");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.9.desc"), 9, "packedice/sunkentiles");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.10.desc"), 10, "packedice/tiles");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.11.desc"), 11, "packedice/a1-stonecobble-icepanel");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.12.desc"), 12, "packedice/a1-stoneslab-ice");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.13.desc"), 13, "packedice/zelda");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.14.desc"), 14, "packedice/bismuth");
			packedice.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedice.15.desc"), 15, "packedice/poison");
			packedice.carverHelper.register(packedice, "packedice");
			Carving.chisel.registerOre("packedice", "packedice");
		}
	},

	PACKEDICE_PILLAR {

		@Override
		void addBlocks() {
			BlockMarblePackedIce packedice_pillar = (BlockMarblePackedIce) new BlockMarblePackedIce().setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setLightOpacity(3)
					.setStepSound(Block.soundTypeGlass);
			packedice_pillar.carverHelper.setChiselBlockName("Packed Ice Pillar");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.0.desc"), 0, "packedicepillar/column");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.1.desc"), 1, "packedicepillar/capstone");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.2.desc"), 2, "packedicepillar/base");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.3.desc"), 3, "packedicepillar/small");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.4.desc"), 4, "packedicepillar/pillar-carved");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.5.desc"), 5, "packedicepillar/a1-stoneornamental-marblegreek");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.6.desc"), 6, "packedicepillar/a1-stonepillar-greek");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.7.desc"), 7, "packedicepillar/a1-stonepillar-plain");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.8.desc"), 8, "packedicepillar/a1-stonepillar-greektopplain");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.9.desc"), 9, "packedicepillar/a1-stonepillar-plaintopplain");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.10.desc"), 10, "packedicepillar/a1-stonepillar-greekbottomplain");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.11.desc"), 11, "packedicepillar/a1-stonepillar-plainbottomplain");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.12.desc"), 12, "packedicepillar/a1-stonepillar-greektopgreek");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.13.desc"), 13, "packedicepillar/a1-stonepillar-plaintopgreek");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.14.desc"), 14, "packedicepillar/a1-stonepillar-greekbottomgreek");
			packedice_pillar.carverHelper.addVariation(StatCollector.translateToLocal("tile.packedicePillar.15.desc"), 15, "packedicepillar/a1-stonepillar-plainbottomgreek");
			packedice_pillar.carverHelper.register(packedice_pillar, "packedice_pillar");
			Carving.chisel.setGroupClass("packedice_pillar", "packedice");
		}
	},

	PACKEDICE_STAIRS {

		@Override
		void addBlocks() {
			BlockMarbleStairsMaker makerPackedIceStairs = new BlockMarbleStairsMaker(Blocks.packed_ice);
			makerPackedIceStairs.carverHelper.setChiselBlockName("Packed Ice Stairs");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.0.desc"), 0, Blocks.packed_ice);
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.1.desc"), 1, "packedice/a1-ice-light");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.2.desc"), 2, "packedice/a1-stonecobble-icecobble");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.3.desc"), 3, "packedice/a1-netherbrick-ice");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.4.desc"), 4, "packedice/a1-stonecobble-icebrick");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.5.desc"), 5, "packedice/a1-stonecobble-icebricksmall");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.6.desc"), 6, "packedice/a1-stonecobble-icedungeon");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.7.desc"), 7, "packedice/a1-stonecobble-icefour");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.8.desc"), 8, "packedice/a1-stonecobble-icefrench");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.9.desc"), 9, "packedice/sunkentiles");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.10.desc"), 10, "packedice/tiles");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.11.desc"), 11, "packedice/a1-stonecobble-icepanel");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.12.desc"), 12, "packedice/a1-stoneslab-ice");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.13.desc"), 13, "packedice/zelda");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.14.desc"), 14, "packedice/bismuth");
			makerPackedIceStairs.carverHelper.addVariation(StatCollector.translateToLocal("tile.packediceStairs.15.desc"), 15, "packedice/poison");
			makerPackedIceStairs.create(new BlockMarbleStairsMakerCreator() {

				@Override
				public BlockCarvableStairs create(Block block, int meta, CarvableHelper helper) {
					return new BlockMarblePackedIceStairs(block, meta, helper);
				}
			}, "packedice_stairs");
			Carving.chisel.registerOre("packedIceStairs", "packedIceStairs");
		}
	},

	PAPER_WALL {

		@Override
		void addBlocks() {
			BlockCarvablePane paperwall = (BlockCarvablePane) new BlockCarvablePane(Material.ground, true).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.5F).setResistance(10F);
			paperwall.carverHelper.setChiselBlockName("Paper Wall");
			paperwall.carverHelper.addVariation(StatCollector.translateToLocal("tile.paperwall.0.desc"), 0, "paper/box");
			paperwall.carverHelper.addVariation(StatCollector.translateToLocal("tile.paperwall.1.desc"), 1, "paper/throughMiddle");
			paperwall.carverHelper.addVariation(StatCollector.translateToLocal("tile.paperwall.2.desc"), 2, "paper/cross");
			paperwall.carverHelper.addVariation(StatCollector.translateToLocal("tile.paperwall.3.desc"), 3, "paper/sixSections");
			paperwall.carverHelper.addVariation(StatCollector.translateToLocal("tile.paperwall.4.desc"), 4, "paper/vertical");
			paperwall.carverHelper.addVariation(StatCollector.translateToLocal("tile.paperwall.5.desc"), 5, "paper/horizontal");
			paperwall.carverHelper.addVariation(StatCollector.translateToLocal("tile.paperwall.6.desc"), 6, "paper/floral");
			paperwall.carverHelper.addVariation(StatCollector.translateToLocal("tile.paperwall.7.desc"), 7, "paper/plain");
			paperwall.carverHelper.addVariation(StatCollector.translateToLocal("tile.paperwall.8.desc"), 8, "paper/door");

			paperwall.carverHelper.register(paperwall, "paperwall");
			Carving.chisel.registerOre("paperwall", "paperwall");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.paperwall, 8), "ppp", "psp", "ppp", ('p'), Items.paper, ('s'), "stickWood"));
		}
	},

	PRESENT {

		@Override
		void addBlocks() {
			for (int x = 0; x < 16; x++) {
				present[x] = (BlockPresent) new BlockPresent(x).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(2.0F).setResistance(10.0F).setBlockName("present");
				GameRegistry.registerBlock(present[x], "chest" + x);
				Carving.chisel.addVariation("present", present[x], 0, (x + 1));
			}
			Carving.chisel.registerOre("chest", "chest");
		}

		@Override
		void addRecipes() {
			GameRegistry.addShapelessRecipe(new ItemStack(ChiselBlocks.present[meta]), new ItemStack(Blocks.chest, 1), new ItemStack(Items.dye, 1, meta));
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
				GameRegistry.registerBlock(pumpkin[metadata], "pumpkin" + (metadata + 1));
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

	RC_BLEACHED_BONE(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("RCBleachedBone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bleachedbone"), 0, 0);
			Carving.chisel.addVariation("RCBleachedBone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bleachedbone"), 1, 1);
			Carving.chisel.addVariation("RCBleachedBone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bleachedbone"), 2, 2);
			Carving.chisel.addVariation("RCBleachedBone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bleachedbone"), 3, 3);
			Carving.chisel.addVariation("RCBleachedBone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bleachedbone"), 4, 4);
			Carving.chisel.addVariation("RCBleachedBone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bleachedbone"), 5, 5);
			Carving.chisel.registerOre("RCBleachedBone", "RCBleachedBone");
		}
	},

	RC_BLOOD_STAINED(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("RCBloodStained", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bloodstained"), 0, 0);
			Carving.chisel.addVariation("RCBloodStained", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bloodstained"), 1, 1);
			Carving.chisel.addVariation("RCBloodStained", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bloodstained"), 2, 2);
			Carving.chisel.addVariation("RCBloodStained", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bloodstained"), 3, 3);
			Carving.chisel.addVariation("RCBloodStained", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bloodstained"), 4, 4);
			Carving.chisel.addVariation("RCBloodStained", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.bloodstained"), 5, 5);
			Carving.chisel.registerOre("RCBloodStained", "RCBloodStained");
		}
	},

	RC_FROST_BOUND_BLOCK(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("RCFrostBoundBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.frostbound"), 0, 0);
			Carving.chisel.addVariation("RCFrostBoundBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.frostbound"), 1, 1);
			Carving.chisel.addVariation("RCFrostBoundBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.frostbound"), 2, 2);
			Carving.chisel.addVariation("RCFrostBoundBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.frostbound"), 3, 3);
			Carving.chisel.addVariation("RCFrostBoundBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.frostbound"), 4, 4);
			Carving.chisel.addVariation("RCFrostBoundBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.frostbound"), 5, 5);
			Carving.chisel.registerOre("RCFrostBoundBlock", "RCFrostBoundBlock");
		}
	},

	RC_INFERNAL_STONE(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("RCInfernalStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.infernal"), 0, 0);
			Carving.chisel.addVariation("RCInfernalStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.infernal"), 1, 1);
			Carving.chisel.addVariation("RCInfernalStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.infernal"), 2, 2);
			Carving.chisel.addVariation("RCInfernalStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.infernal"), 3, 3);
			Carving.chisel.addVariation("RCInfernalStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.infernal"), 4, 4);
			Carving.chisel.addVariation("RCInfernalStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.infernal"), 5, 5);
			Carving.chisel.registerOre("RCInfernalStone", "RCInfernalStone");
		}
	},

	RC_QUARRIED_BLOCK(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("RCQuarriedBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.quarried"), 0, 0);
			Carving.chisel.addVariation("RCQuarriedBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.quarried"), 1, 1);
			Carving.chisel.addVariation("RCQuarriedBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.quarried"), 2, 2);
			Carving.chisel.addVariation("RCQuarriedBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.quarried"), 3, 3);
			Carving.chisel.addVariation("RCQuarriedBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.quarried"), 4, 4);
			Carving.chisel.addVariation("RCQuarriedBlock", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.quarried"), 5, 5);
			Carving.chisel.registerOre("RCQuarriedBlock", "RCQuarriedBlock");
		};
	},

	RC_SANDY_STONE(RAILCRAFT.getRequiredMod(), RAILCRAFT) {

		@Override
		void addBlocks() {
			Carving.chisel.addVariation("RCSandyStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.sandy"), 0, 0);
			Carving.chisel.addVariation("RCSandyStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.sandy"), 1, 1);
			Carving.chisel.addVariation("RCSandyStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.sandy"), 2, 2);
			Carving.chisel.addVariation("RCSandyStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.sandy"), 3, 3);
			Carving.chisel.addVariation("RCSandyStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.sandy"), 4, 4);
			Carving.chisel.addVariation("RCSandyStone", GameRegistry.findBlock("Railcraft", "tile.railcraft.brick.sandy"), 5, 5);
			Carving.chisel.registerOre("RCSandyStone", "RCSandyStone");
		}
	},

	REDSTONE_BLOCK {

		@Override
		void addBlocks() {
			BlockCarvable redstone_block = (BlockCarvablePowered) (new BlockCarvablePowered(Material.iron)).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(5.0F).setResistance(10.0F)
					.setStepSound(Block.soundTypeMetal);
			Carving.chisel.addVariation("redstone_block", Blocks.redstone_block, 0, 0);
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.1.desc"), 1, "redstone/smooth");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.2.desc"), 2, "redstone/block");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.3.desc"), 3, "redstone/blocks");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.4.desc"), 4, "redstone/bricks");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.5.desc"), 5, "redstone/smallbricks");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.6.desc"), 6, "redstone/smallchaotic");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.7.desc"), 7, "redstone/chiseled");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.8.desc"), 8, "redstone/ere");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.9.desc"), 9, "redstone/ornate-tiles");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.10.desc"), 10, "redstone/pillar");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.11.desc"), 11, "redstone/tiles");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.12.desc"), 12, "redstone/circuit");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.13.desc"), 13, "redstone/supaplex");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.14.desc"), 14, "redstone/a1-blockredstone-skullred");
			redstone_block.carverHelper.addVariation(StatCollector.translateToLocal("tile.redstone_block.15.desc"), 15, "redstone/a1-blockredstone-redstonezelda");
			redstone_block.carverHelper.register(redstone_block, "redstone_block");
			Carving.chisel.registerOre("redstone_block", "redstone");
		}
	},

	ROAD_LINE {

		@Override
		void addBlocks() {
			BlockRoadLine road_line = (BlockRoadLine) new BlockRoadLine().setStepSound(Block.soundTypeStone).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(0.01F)
					.setBlockName("roadLine");
			road_line.carverHelper.addVariation(StatCollector.translateToLocal("tile.roadLine.0.desc"), 0, "line-marking/white-center");
			road_line.carverHelper.addVariation(StatCollector.translateToLocal("tile.roadLine.1.desc"), 1, "line-marking/double-white-center");
			road_line.carverHelper.addVariation(StatCollector.translateToLocal("tile.roadLine.2.desc"), 2, "line-marking/yellow-center");
			road_line.carverHelper.addVariation(StatCollector.translateToLocal("tile.roadine.3.desc"), 3, "line-marking/double-yellow-center");
			GameRegistry.registerBlock(road_line, ItemCarvable.class, "road_line");
			Carving.chisel.registerOre("roadLine", "roadLine");
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
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.3.desc"), 3, "sandstone/faded");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.4.desc"), 4, "sandstone/column");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.5.desc"), 5, "sandstone/capstone");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.6.desc"), 6, "sandstone/small");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.7.desc"), 7, "sandstone/base");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.8.desc"), 8, "sandstone/smooth");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.9.desc"), 9, "sandstone/smooth-cap");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.10.desc"), 10, "sandstone/smooth-small");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.11.desc"), 11, "sandstone/smooth-base");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.12.desc"), 12, "sandstone/block");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.13.desc"), 13, "sandstone/blocks");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.14.desc"), 14, "sandstone/mosaic");
			sandstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstone.15.desc"), 15, "sandstone/horizontal-tiles");
			sandstone.carverHelper.register(sandstone, "sandstone");
			Carving.chisel.registerOre("sandstone", "sandstone");
		}

		@Override
		void addRecipes() {
			if (meta == 0) {
				// The following recipe is due to bugs with Chisel 1.5.1 to 1.5.6a
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
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 0, "sandstone-scribbles/scribbles-0");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 1, "sandstone-scribbles/scribbles-1");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 2, "sandstone-scribbles/scribbles-2");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 3, "sandstone-scribbles/scribbles-3");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 4, "sandstone-scribbles/scribbles-4");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 5, "sandstone-scribbles/scribbles-5");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 6, "sandstone-scribbles/scribbles-6");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 7, "sandstone-scribbles/scribbles-7");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 8, "sandstone-scribbles/scribbles-8");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 9, "sandstone-scribbles/scribbles-9");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 10, "sandstone-scribbles/scribbles-10");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 11, "sandstone-scribbles/scribbles-11");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 12, "sandstone-scribbles/scribbles-12");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 13, "sandstone-scribbles/scribbles-13");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 14, "sandstone-scribbles/scribbles-14");
			sandstone_scribbles.carverHelper.addVariation(StatCollector.translateToLocal("tile.sandstoneScribbles.desc"), 15, "sandstone-scribbles/scribbles-15");
			sandstone_scribbles.carverHelper.register(sandstone_scribbles, "sandstone_scribbles");
			Carving.chisel.registerOre("sandstone_scribbles", "sandstone_scribbles");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.sandstone_scribbles, 1), "X", 'X', new ItemStack(ChiselBlocks.sandstone, 1, 8));
		}
	},

	SMASHING_ROCK {

		@Override
		void addItems() {
			ItemSmashingRock smashingrock = (ItemSmashingRock) new ItemSmashingRock().setTextureName("Chisel:smashingrock").setCreativeTab(ChiselTabs.tabChisel);
			EntityRegistry.registerModEntity(EntitySmashingRock.class, "SmashingRock", 2, Chisel.instance, 40, 1, true);
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
			BlockSnakestone sand_snakestone = (BlockSnakestone) new BlockSnakestone("Chisel:snakestone/sandsnake/").setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setBlockName("snakestoneSand");
			GameRegistry.registerBlock(sand_snakestone, ItemCarvable.class, "sand_snakestone");
			// TODO- eat me!
			// LanguageRegistry.addName(new ItemStack(sandSnakestone, 1, 1),
			// "Sandstone snake block head");
			// LanguageRegistry.addName(new ItemStack(sandSnakestone, 1,
			// 13), "Sandstone snake block body");
			Carving.chisel.addVariation("sandstone", sand_snakestone, 1, 16);
			Carving.chisel.addVariation("sandstone", sand_snakestone, 13, 17);
			Carving.chisel.registerOre("sandSnakestone", "sandSnakestone");
		}
	},

	SNAKESTONE {

		@Override
		void addBlocks() {
			BlockSnakestone stone_snakestone = (BlockSnakestone) new BlockSnakestone("Chisel:snakestone/snake/").setBlockName("snakestoneStone").setCreativeTab(ChiselTabs.tabStoneChiselBlocks);
			GameRegistry.registerBlock(stone_snakestone, ItemCarvable.class, "stone_snakestone");
			// LanguageRegistry.addName(new ItemStack(snakestone, 1, 1),
			// "Stone snake block head");
			// LanguageRegistry.addName(new ItemStack(snakestone, 1, 13),
			// "Stone snake block body");
			Carving.chisel.addVariation("stonebrick", stone_snakestone, 1, 16);
			Carving.chisel.addVariation("stonebrick", stone_snakestone, 13, 17);
			Carving.chisel.registerOre("snakestoneStone", "snakestoneStone");
		}
	},

	SNAKESTONE_OBSIDIAN {

		@Override
		void addBlocks() {
			BlockSnakestoneObsidian obsidian_snakestone = (BlockSnakestoneObsidian) new BlockSnakestoneObsidian("Chisel:snakestone/obsidian/").setBlockName("obsidianSnakestone").setHardness(50.0F)
					.setResistance(2000.0F);
			GameRegistry.registerBlock(obsidian_snakestone, ItemCarvable.class, "obsidian_snakestone");
			Carving.chisel.addVariation("obsidian", obsidian_snakestone, 1, 16);
			Carving.chisel.addVariation("obsidian", obsidian_snakestone, 13, 17);
			Carving.chisel.registerOre("obsidianSnakestone", "obsidianSnakestone");
		}
	},

	STONE_BRICK {

		@Override
		void addBlocks() {
			BlockCarvable stonebricksmooth = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(1.5F).setResistance(10.0F)
					.setStepSound(Block.soundTypeStone);
			for (int i = 0; i < 4; i++) {
				if (i == 1) {
					if (Configurations.allowMossy)
						Carving.chisel.addVariation("stonebricksmooth", Blocks.stonebrick, i, i);
				} else
					Carving.chisel.addVariation("stonebricksmooth", Blocks.stonebrick, i, i);
			}
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.4.desc"), 4, "stonebrick/smallbricks");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.5.desc"), 5, "stonebrick/largebricks");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.6.desc"), 6, "stonebrick/smallchaotic");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.7.desc"), 7, "stonebrick/chaoticbricks");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.8.desc"), 8, "stonebrick/chaotic");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.9.desc"), 9, "stonebrick/fancy");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.10.desc"), 10, "stonebrick/ornate");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.11.desc"), 11, "stonebrick/largeornate");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.12.desc"), 12, "stonebrick/panel-hard");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.13.desc"), 13, "stonebrick/sunken");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.14.desc"), 14, "stonebrick/ornatepanel");
			stonebricksmooth.carverHelper.addVariation(StatCollector.translateToLocal("tile.stoneBrick.15.desc"), 15, "stonebrick/poison");
			stonebricksmooth.carverHelper.register(stonebricksmooth, "stonebricksmooth");
			Carving.chisel.registerOre("stonebricksmooth", "stonebricksmooth");
		}
	},

	TECHNICAL {

		@Override
		void addBlocks() {
			BlockCarvable technical = (BlockCarvable) new BlockCarvable(Material.rock).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10F);
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.0.desc"), 0, "technical/scaffold");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.1.desc"), 1, "technical/cautiontape");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.2.desc"), 2, "technical/industrialrelic");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.3.desc"), 3, "technical/pipesLarge");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.4.desc"), 4, "technical/fanFast");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.5.desc"), 5, "technical/pipesSmall");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.6.desc"), 6, "technical/fanStill");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.7.desc"), 7, "technical/vent");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.8.desc"), 8, "technical/ventGlowing");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.9.desc"), 9, "technical/insulationv2");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.10.desc"), 10, "technical/spinningStuffAnim");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.11.desc"), 11, "technical/cables");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.12.desc"), 12, "technical/rustyBoltedPlates");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.13.desc"), 13, "technical/grate");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.14.desc"), 14, "technical/fanMalfunction");
			technical.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.15.desc"), 15, "technical/grateRusty");
			technical.carverHelper.register(technical, "technical");
			Carving.chisel.registerOre("technical", "technical");

			BlockCarvableGlass technical2 = (BlockCarvableGlass) new BlockCarvableGlass().setHardness(2.0F).setResistance(10F);
			technical2.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.0.desc"), 0, "technical/scaffoldTransparent");
			technical2.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.4.desc"), 1, "technical/fanFastTransparent");
			technical2.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.6.desc"), 2, "technical/fanStillTransparent");
			technical2.carverHelper.addVariation(StatCollector.translateToLocal("tile.technical.14.desc"), 3, "technical/fanStillTransparent");
			technical2.carverHelper.register(technical2, "technical2");
			Carving.chisel.registerOre("technical2", "technical2");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.technical, Configurations.factoryBlockAmount, 0), new Object[] { "xyx", "yxy", "xyx", 'x', "stone", 'y',
					Items.iron_ingot }));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.technical2, Configurations.factoryBlockAmount, 0), new Object[] { "xyx", "yzy", "xyx", 'x', "stone", 'y',
					"ingotIron", 'z', Blocks.glass }));
		}
	},

	TEMPLE_BLOCK {

		@Override
		void addBlocks() {
			BlockCarvable templeblock = (BlockCarvable) new BlockEldritch().setHardness(2.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setResistance(10F)
					.setStepSound(Chisel.soundTempleFootstep);
			templeblock.carverHelper.setChiselBlockName("Temple Block");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.0.desc"), 0, "temple/cobble");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.1.desc"), 1, "temple/ornate");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.2.desc"), 2, "temple/plate");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.3.desc"), 3, "temple/plate-cracked");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.4.desc"), 4, "temple/bricks");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.5.desc"), 5, "temple/bricks-large");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.6.desc"), 6, "temple/bricks-weared");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.7.desc"), 7, "temple/bricks-disarray");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.8.desc"), 8, "temple/column");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.9.desc"), 9, "temple/stand");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.10.desc"), 10, "temple/stand-mosaic");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.11.desc"), 11, "temple/stand-creeper");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.12.desc"), 12, "temple/tiles");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.13.desc"), 13, "temple/smalltiles");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.14.desc"), 14, "temple/tiles-light");
			templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.templeblock.15.desc"), 15, "temple/smalltiles-light");
			templeblock.carverHelper.register(templeblock, "templeblock");
			Carving.chisel.registerOre("templeblock", "templeblock");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.templeblock, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.dye, 1, 4));
		}
	},

	TEMPLE_BLOCK_MOSSY {

		@Override
		void addBlocks() {
			BlockCarvable mossy_templeblock = (BlockCarvable) new BlockEldritch().setHardness(2.0F).setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setResistance(10F)
					.setStepSound(Chisel.soundTempleFootstep);
			mossy_templeblock.carverHelper.setChiselBlockName("Mossy Temple Block");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.0.desc"), 0, "templemossy/cobble");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.1.desc"), 1, "templemossy/ornate");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.2.desc"), 2, "templemossy/plate");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.3.desc"), 3, "templemossy/plate-cracked");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.4.desc"), 4, "templemossy/bricks");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.5.desc"), 5, "templemossy/bricks-large");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.6.desc"), 6, "templemossy/bricks-weared");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.7.desc"), 7, "templemossy/bricks-disarray");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.8.desc"), 8, "templemossy/column");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.9.desc"), 9, "templemossy/stand");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.10.desc"), 10, "templemossy/stand-mosaic");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.11.desc"), 11, "templemossy/stand-creeper");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.12.desc"), 12, "templemossy/tiles");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.13.desc"), 13, "templemossy/smalltiles");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.14.desc"), 14, "templemossy/tiles-light");
			mossy_templeblock.carverHelper.addVariation(StatCollector.translateToLocal("tile.mossy_templeblock.15.desc"), 15, "templemossy/smalltiles-light");
			mossy_templeblock.carverHelper.register(mossy_templeblock, "mossy_templeblock");
			Carving.chisel.registerOre("mossy_templeblock", "mossy_templeblock");
		};
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

	TYRIAN {

		@Override
		void addBlocks() {
			BlockCarvable tyrian = (BlockCarvable) new BlockCarvable(Material.iron).setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(5.0F).setResistance(10.0F)
					.setStepSound(Block.soundTypeMetal);
			tyrian.carverHelper.setChiselBlockName("Futuristic Armor Plating Block");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.0.desc"), 0, "tyrian/shining");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.1.desc"), 1, "tyrian/tyrian");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.2.desc"), 2, "tyrian/chaotic");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.3.desc"), 3, "tyrian/softplate");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.4.desc"), 4, "tyrian/rust");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.5.desc"), 5, "tyrian/elaborate");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.6.desc"), 6, "tyrian/routes");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.7.desc"), 7, "tyrian/platform");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.8.desc"), 8, "tyrian/platetiles");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.9.desc"), 9, "tyrian/diagonal");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.10.desc"), 10, "tyrian/dent");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.11.desc"), 11, "tyrian/blueplating");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.12.desc"), 12, "tyrian/black");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.13.desc"), 13, "tyrian/black2");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.14.desc"), 14, "tyrian/opening");
			tyrian.carverHelper.addVariation(StatCollector.translateToLocal("tile.tyrian.15.desc"), 15, "tyrian/plate");
			tyrian.carverHelper.register(tyrian, "tyrian");
			OreDictionary.registerOre("tyrian", tyrian);
			Carving.chisel.registerOre("tyrian", "tyrian");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.tyrian, 8, 0), "***", "*X*", "***", '*', new ItemStack(Blocks.stone, 1), 'X', new ItemStack(Items.iron_ingot, 1));
		}
	},

	VOIDSTONE {

		@Override
		void addBlocks() {
			BlockCarvable voidstone = (BlockCarvable) new BlockCarvable().setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setStepSound(Block.soundTypeStone)
					.setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(5.0F).setResistance(10.0F);
			voidstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.0.desc"), 0, "voidstone/raw");
			voidstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.1.desc"), 1, "voidstone/quarters");
			voidstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.2.desc"), 2, "voidstone/smooth");
			voidstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.3.desc"), 3, "voidstone/skulls");
			voidstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.4.desc"), 4, "voidstone/rune");
			voidstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.5.desc"), 5, "voidstone/metalborder");
			voidstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.6.desc"), 6, "voidstone/eye");
			voidstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.7.desc"), 7, "voidstone/bevel");

			voidstone.carverHelper.register(voidstone, "voidstone");
			Carving.chisel.registerOre("voidstone", "voidstone");

			BlockMarbleTexturedOre voidstone2 = (BlockMarbleTexturedOre) new BlockMarbleTexturedOre(Material.rock, Chisel.MOD_ID + ":voidstone/animated/void").setStepSound(Block.soundTypeStone)
					.setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(5.0F).setResistance(10.0F);
			voidstone2.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.0.desc"), 0, "voidstone/animated/raw");
			voidstone2.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.1.desc"), 1, "voidstone/animated/quarters");
			voidstone2.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.2.desc"), 2, "voidstone/animated/smooth");
			voidstone2.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.3.desc"), 3, "voidstone/animated/skulls");
			voidstone2.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.4.desc"), 4, "voidstone/animated/rune");
			voidstone2.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.5.desc"), 5, "voidstone/animated/metalborder");
			voidstone2.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.6.desc"), 6, "voidstone/animated/eye");
			voidstone2.carverHelper.addVariation(StatCollector.translateToLocal("tile.voidstone.7.desc"), 7, "voidstone/animated/bevel");

			voidstone2.carverHelper.register(voidstone2, "voidstone2");
			Carving.chisel.registerOre("voidstone2", "voidstone2");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstone, 8, 0), new Object[] { "oxo", "xyx", "oxo", 'x', new ItemStack(Blocks.stone, 1), 'y', new ItemStack(Items.ender_pearl, 1), 'o',
					new ItemStack(Blocks.obsidian, 1) });
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstone2, 8, 0), new Object[] { "oxo", "xyx", "oxo", 'x', new ItemStack(Blocks.stone, 1), 'y', new ItemStack(Items.ender_eye, 1), 'o',
					new ItemStack(Blocks.obsidian, 1) });
		}
	},

	VOIDSTONE_PILLARS {

		@Override
		void addBlocks() {
			BlockVoidstonePillar voidstonePillar = (BlockVoidstonePillar) new BlockVoidstonePillar().setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setStepSound(Block.soundTypeStone);
			voidstonePillar.carvableHelper.addVariation(StatCollector.translateToLocal("tile.voidstonePillar.0.desc"), 0, "voidstone/pillar-side");
			voidstonePillar.carvableHelper.register(voidstonePillar, "voidstonePillar");
			Carving.chisel.registerOre("voidstonePillar", "voidstonePillar");

			BlockVoidstonePillar2 voidstonePillar2 = (BlockVoidstonePillar2) new BlockVoidstonePillar2().setStepSound(Block.soundTypeStone);
			voidstonePillar2.carvableHelper.addVariation(StatCollector.translateToLocal("tile.voidstonePillar2.0.desc"), 0, "voidstone/pillar-side");
			voidstonePillar2.carvableHelper.register(voidstonePillar2, "voidstonePillar2");
			Carving.chisel.registerOre("voidstonePillar2", "voidstonePillar2");
		}

		@Override
		void addRecipes() {
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstonePillar, 4, 0), "xx", "xx", 'x', new ItemStack(ChiselBlocks.voidstone, 1));
			GameRegistry.addRecipe(new ItemStack(ChiselBlocks.voidstonePillar2, 4, 0), "xx", "xx", 'x', new ItemStack(ChiselBlocks.voidstone2, 1));
		}
	},

	WATERSTONE {

		@Override
		void addBlocks() {
			BlockWaterstone waterstone = (BlockWaterstone) new BlockWaterstone(Material.rock, "water_flow").setCreativeTab(ChiselTabs.tabStoneChiselBlocks).setHardness(2.0F).setResistance(10.0F);
			waterstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.waterstone.0.desc"), 0, "waterstone/cobble");
			waterstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.waterstone.1.desc"), 1, "waterstone/black");
			waterstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.waterstone.2.desc"), 2, "waterstone/tiles");
			waterstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.waterstone.3.desc"), 3, "waterstone/chaotic");
			waterstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.waterstone.4.desc"), 4, "waterstone/creeper");
			waterstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.waterstone.5.desc"), 5, "waterstone/panel");
			waterstone.carverHelper.addVariation(StatCollector.translateToLocal("tile.waterstone.6.desc"), 6, "waterstone/panel-ornate");
			waterstone.carverHelper.register(waterstone, "waterstone");
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
			for (int i = 0; i < 6; i++) {
				String n = plank_names[i];
				String u = plank_ucnames[i];
				final String orename = n.replace('-', '_') + "_planks";

				planks[i] = (BlockCarvable) (new BlockCarvable(Material.wood)).setCreativeTab(ChiselTabs.tabWoodChiselBlocks).setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood);
				planks[i].carverHelper.setChiselBlockName(u + " Wood Planks");
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
				planks[i].carverHelper.register(planks[i], orename);
				Carving.chisel.addVariation(orename, Blocks.planks, i, 0);
				planks[i].setHarvestLevel("axe", 0);
				Carving.chisel.registerOre("wood", "wood");
				Carving.chisel.setVariationSound(orename, Chisel.MOD_ID + ":chisel.wood");
			}
		}
	},

	WOOLEN_CLAY {

		@Override
		void addBlocks() {
			BlockCarvable woolen_clay = (BlockCarvable) new BlockCarvable(Material.clay).setCreativeTab(ChiselTabs.tabOtherChiselBlocks).setHardness(2F).setResistance(10F);
			woolen_clay.carverHelper.setChiselBlockName("Woolen Clay");

			for (int i = 0; i < 16; i++)
				woolen_clay.carverHelper.addVariation(StatCollector.translateToLocal("tile.woolenClay." + i + ".desc"), i, "woolenClay/" + sGNames[i].replaceAll(" ", "").toLowerCase());
			woolen_clay.carverHelper.register(woolen_clay, "woolen_clay");

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
	},

	WARNING_SIGN {
		@Override
		void addBlocks() {
			BlockCarvableLayered sign = (BlockCarvableLayered) new BlockCarvableLayered(Material.iron, "warning/base").setCreativeTab(ChiselTabs.tabMetalChiselBlocks).setHardness(2.0F).setResistance(10.0F);
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.0.desc"), 0, "warning/rad");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.1.desc"), 1, "warning/bio");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.2.desc"), 2, "warning/fire");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.3.desc"), 3, "warning/explosion");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.4.desc"), 4, "warning/death");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.5.desc"), 5, "warning/falling");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.6.desc"), 6, "warning/fall");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.7.desc"), 7, "warning/voltage");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.8.desc"), 8, "warning/generic");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.9.desc"), 9, "warning/acid");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.10.desc"), 10, "warning/underconstruction");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.11.desc"), 11, "warning/sound");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.12.desc"), 12, "warning/noentry");
			sign.carverHelper.addVariation(StatCollector.translateToLocal("tile.warningSign.13.desc"), 13, "warning/cryogenic");
			sign.carverHelper.register(sign, "warningSign");
			Carving.chisel.registerOre("warningSign", "warningSign");
		}
		@Override
		void addRecipes() {
			 GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ChiselBlocks.sign, 4, 0), new Object[]{"xxx", "xyx", "xxx", 'x', "stone", 'y', Items.sign}));
		}
	},

	TORCH {
		@Override
		void addBlocks() {
		Carving.chisel.addVariation("torch", Blocks.torch, 0, 0);
		for(int type = 0; type < 10; type++){
			if(type == 8 || type == 9)
				torch[type] = new BlockCarvableTorch().disableParticles();
			else
				torch[type] = new BlockCarvableTorch();

			torch[type].setInformation("torch" + (type + 1));
			GameRegistry.registerBlock(torch[type], "torch" + (type + 1));
			Carving.chisel.addVariation("torch", torch[type], 0, (type + 1));
		}
		Carving.chisel.registerOre("torch", "torch");
		}
	},

	;

	public static void init() {
		loadRecipes();
	}

	private static void loadBlocks() {
		for (Features f : values()) {
			if (f.enabled()) {
				f.addBlocks();
			}
		}
		Chisel.proxy.registerTileEntities();
	}

	private static void loadItems() {
		for (Features f : values()) {
			if (f.enabled()) {
				f.addItems();
			}
		}
	}

	private static void loadRecipes() {
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
			}
		}
	}

	public static void preInit() {
		loadBlocks();
		loadItems();
	}

	private Features parent;

	private String requiredMod;

	Features() {
		this(null, null);
	}

	Features(Features parent) {
		this(null, parent);
	}

	Features(String requiredMod) {
		this(requiredMod, null);
	}

	Features(String requriedMod, Features parent) {
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

	static int meta = 0;

	boolean needsMetaRecipes() {
		return false;
	}

	public final boolean enabled() {
		return Configurations.featureEnabled(this) && (requiredMod == null || Loader.isModLoaded(requiredMod)) && (parent == null || parent.enabled());
	}

	private String getRequiredMod() {
		return requiredMod;
	}

	public static boolean oneModdedFeatureLoaded() {
		for (Features f : values()) {
			if (f.getRequiredMod() != null && Loader.isModLoaded(f.getRequiredMod())) {
				return true;
			}
		}
		return false;
	}
}
