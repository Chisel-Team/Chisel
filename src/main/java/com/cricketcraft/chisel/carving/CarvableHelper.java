package com.cricketcraft.chisel.carving;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.CTM;
import com.cricketcraft.chisel.client.render.TextureSubmap;
import com.cricketcraft.chisel.compat.FMPIntegration;
import com.cricketcraft.chisel.item.ItemCarvable;
import com.cricketcraft.chisel.utils.GeneralClient;

import cpw.mods.fml.common.registry.GameRegistry;

public class CarvableHelper {

	public interface ISubmapManager {

		IIcon getIcon(int side, int meta);

		IIcon getIcon(IBlockAccess world, int x, int y, int z, int side);

		void registerIcons(String modName, Block block, IIconRegister register);
	}

	public enum TextureType {

		TOPSIDE("top", "side"),
		TOPBOTSIDE("top", "bottom", "side"),
		CTMV("ctmv", "top"),
		CTMH("ctmh", "top"),
		V9("v9"),
		V4("v4"),
		CTMX("", "ctm"),
		R16("r16"),
		R9("r9"),
		R4("r4"),
		NORMAL,
		CUSTOM;
		
		private static final TextureType[] VALUES;
		private String[] suffixes;
		static {
			VALUES = ArrayUtils.subarray(values(), 0, values().length - 1);
		}

		private TextureType(String... suffixes) {
			this.suffixes = suffixes.length == 0 ? new String[] { "" } : suffixes;
		}

		public static TextureType getTypeFor(String path) {
			for (TextureType t : VALUES) {
				boolean matches = true;
				for (String s : t.suffixes) {
					String res = "".equals(s) ? path + ".png" : path + "-" + s + ".png";
					if (Chisel.class.getResource(res) == null) {
						matches = false;
					}
				}
				if (matches) {
					return t;
				}
			}
			return CUSTOM;
		}
	}

	public ArrayList<CarvableVariation> variations = new ArrayList<CarvableVariation>();
	CarvableVariation[] map = new CarvableVariation[16];
	public boolean forbidChiseling = false;
	public String blockName;

	public void addVariation(String description, int metadata, Block bb) {
		addVariation(description, metadata, null, bb, 0);
	}

	public void addVariation(String description, int metadata, Block bb, int blockMeta) {
		addVariation(description, metadata, null, bb, blockMeta);
	}

	public void addVariation(String description, int metadata, Block bb, int blockMeta, Material material) {
		addVariation(description, metadata, null, bb, blockMeta);
	}

	public void addVariation(String description, int metadata, String texture) {
		addVariation(description, metadata, texture, null, 0);
	}

	public void addVariation(String description, int metadata, String texture, Block block, int blockMeta) {
		addVariation(description, metadata, texture, block, blockMeta);
	}

	public void addVariation(String description, int metadata, String texture, Block block, int blockMeta, ISubmapManager customManager) {
		if (variations.size() >= 16)
			return;

		if (blockName == null && block != null)
			blockName = block.getLocalizedName();
		else if (blockName == null && description != null)
			blockName = description;

		CarvableVariation variation = new CarvableVariation();
		variation.setDescriptionUnloc(description);
		variation.metadata = metadata;
		variation.blockName = blockName;

		if (texture != null) {
			variation.texture = texture;
			String path = "/assets/" + Chisel.MOD_ID + "/textures/blocks/" + variation.texture;
			variation.type = (TextureType) TextureType.getTypeFor(path);
			if (variation.type == TextureType.CUSTOM && customManager == null) {
				throw new IllegalArgumentException(String.format("Could not find texture for block %s, and no custom texture manager was provided.", block));
			}
			variation.manager = customManager;
		} else {
			variation.block = block;
			variation.type = TextureType.TOPBOTSIDE;
			variation.blockMeta = blockMeta;
		}

		variations.add(variation);
		map[metadata] = variation;
	}

	public CarvableVariation getVariation(int metadata) {
		if (metadata < 0 || metadata > 15)
			metadata = 0;

		CarvableVariation variation = map[metadata];
		if (variation == null)
			return null;

		return variation;
	}

	public IIcon getIcon(int side, int metadata) {
		if (metadata < 0 || metadata > 15)
			metadata = 0;

		CarvableVariation variation = map[metadata];
		if (variation == null)
			return GeneralClient.getMissingIcon();

		switch (variation.type) {
		case NORMAL:
			return variation.icon;
		case TOPSIDE:
			if (side == 0 || side == 1)
				return variation.iconTop;
			else
				return variation.icon;
		case TOPBOTSIDE:
			if (side == 1)
				return variation.iconTop;
			else if (side == 0)
				return variation.iconBot;
			else
				return variation.icon;
		case CTMV:
			if (side < 2)
				return variation.iconTop;
			else
				return variation.seamsCtmVert.icons[0];
		case CTMH:
			if (side < 2)
				return variation.iconTop;
			else
				return variation.seamsCtmVert.icons[0];
		case V9:
			return variation.variations9.icons[4];
		case V4:
			return variation.variations9.icons[0];
		case CTMX:
			return variation.icon;
		case R16:
			return variation.variations9.icons[5];
		case R9:
			return variation.variations9.icons[4];
		case R4:
			return variation.variations9.icons[0];
		case CUSTOM:
			return variation.manager.getIcon(side, metadata);
		}

		return GeneralClient.getMissingIcon();
	}

	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int metadata = world.getBlockMetadata(x, y, z);

		if (metadata < 0 || metadata > 15)
			metadata = 0;

		CarvableVariation variation = map[metadata];
		if (variation == null)
			return GeneralClient.getMissingIcon();

		switch (variation.type) {
		case NORMAL:
		case TOPSIDE:
		case TOPBOTSIDE:
			return getIcon(side, metadata);
//		case CTM3:
//			int tex = CTM.getTexture(world, x, y, z, side);
//
//			int row = tex / 16;
//			int col = tex % 16;
//
//			return variation.ctm.seams[col / 4].icons[col % 4 + row * 4];
		case CTMV: {
			if (side < 2)
				return variation.iconTop;

			Block block = world.getBlock(x, y, z);
			boolean topConnected = CTM.isConnected(world, x, y + 1, z, side, block, metadata);
			boolean botConnected = CTM.isConnected(world, x, y - 1, z, side, block, metadata);

			if (topConnected && botConnected)
				return variation.seamsCtmVert.icons[2];
			if (topConnected && !botConnected)
				return variation.seamsCtmVert.icons[3];
			if (!topConnected && botConnected)
				return variation.seamsCtmVert.icons[1];
			return variation.seamsCtmVert.icons[0];
		}
		case CTMH:
			if (side < 2)
				return variation.iconTop;

			Block block = CTM.getBlockOrFacade(world, x, y, z, side);

			boolean p;
			boolean n;
			boolean reverse = side == 2 || side == 4;

			if (side < 4) {
				p = CTM.isConnected(world, x - 1, y, z, side, block, metadata);
				n = CTM.isConnected(world, x + 1, y, z, side, block, metadata);
			} else {
				p = CTM.isConnected(world, x, y, z - 1, side, block, metadata);
				n = CTM.isConnected(world, x, y, z + 1, side, block, metadata);
			}

			if (p && n)
				return variation.seamsCtmVert.icons[1];
			else if (p)
				return variation.seamsCtmVert.icons[reverse ? 2 : 3];
			else if (n)
				return variation.seamsCtmVert.icons[reverse ? 3 : 2];
			return variation.seamsCtmVert.icons[0];
		case V9:
		case V4:
			int variationSize = (variation.type == TextureType.V9) ? 3 : 2;

			int xModulus = x % variationSize;
			int zModulus = z % variationSize;
			// This ensures that blocks placed near 0,0 or it's axis' do not misbehave
			int textureX = (xModulus < 0) ? (xModulus + variationSize) : xModulus;
			int textureZ = (zModulus < 0) ? (zModulus + variationSize) : zModulus;
			// Always invert the y index
			int textureY = (variationSize - (y % variationSize) - 1);

			if (side == 2 || side == 5) {
				// For WEST, SOUTH reverse the indexes for both X and Z
				textureX = (variationSize - textureX - 1);
				textureZ = (variationSize - textureZ - 1);
			} else if (side == 0) {
				// For DOWN, reverse the indexes for only Z
				textureZ = (variationSize - textureZ - 1);
			}

			int index;
			if (side == 0 || side == 1) {
				// DOWN || UP
				index = textureX + textureZ * variationSize;
			} else if (side == 2 || side == 3) {
				// NORTH || SOUTH
				index = textureX + textureY * variationSize;
			} else {
				// WEST || EAST
				index = textureZ + textureY * variationSize;
			}

			return variation.variations9.icons[index];
		case CTMX:
			return variation.icon;
		case R16:
		case R9:
		case R4:

			int indexRan = x + y + z;
			if ((side == 2) || (side == 5)) {
				indexRan = -indexRan;
			}
			while (indexRan < 0) {
				indexRan = indexRan + 10000;
			}

			// rand.setSeed(indexRan); // Broken

			return variation.variations9.icons[ /* rand */indexRan % ((variation.type == TextureType.R9) ? 9 : 4)];
		case CUSTOM:
			return variation.manager.getIcon(world, x, y, z, side);
		}

		return GeneralClient.getMissingIcon();
	}

	public void registerAll(Block block, String name) {
		registerAll(block, name, ItemCarvable.class);
	}

	public void registerBlock(Block block, String name) {
		registerBlock(block, name, ItemCarvable.class);
	}

	void registerBlock(Block block, String name, Class<? extends ItemCarvable> cl) {
		block.setBlockName("chisel." + name);
		GameRegistry.registerBlock(block, cl, name);
	}

	public void registerAll(Block block, String name, Class<? extends ItemCarvable> cl) {
		registerBlock(block, name, cl);
		registerVariations(name, block);
	}

	public void registerVariations(String name, Block block) {
		for (CarvableVariation variation : variations) {
			registerVariation(name, variation, block, variation.metadata);
		}
	}

	public void registerVariation(String name, CarvableVariation variation, Block block, int blockMeta) {

		if (block.renderAsNormalBlock() || block.isOpaqueCube() || block.isNormalCube()) {
			FMPIntegration.registerFMP(block, blockMeta);
		}

		if (forbidChiseling)
			return;

		Carving.chisel.addVariation(name, block, blockMeta, variation.metadata);
	}

	public void registerBlockIcons(String modName, Block block, IIconRegister register) {
		for (CarvableVariation variation : variations) {
			if (variation.block != null) {
				variation.block.registerBlockIcons(register);

				if (variation.block instanceof BlockPane) {
					variation.icon = variation.block.getBlockTextureFromSide(2);
					variation.iconTop = ((BlockPane) variation.block).getBlockTextureFromSide(0);
					variation.iconBot = ((BlockPane) variation.block).getBlockTextureFromSide(0);

				} else {
					switch (variation.type) {
					case NORMAL:
						variation.icon = variation.block.getIcon(2, variation.blockMeta);
						break;
					case TOPSIDE:
						variation.icon = variation.block.getIcon(2, variation.blockMeta);
						variation.iconTop = variation.block.getIcon(0, variation.blockMeta);
						break;
					case TOPBOTSIDE:
						variation.icon = variation.block.getIcon(2, variation.blockMeta);
						variation.iconTop = variation.block.getIcon(1, variation.blockMeta);
						variation.iconBot = variation.block.getIcon(0, variation.blockMeta);
						break;
					}
				}
			} else {
				switch (variation.type) {
				case NORMAL:
					variation.icon = register.registerIcon(modName + ":" + variation.texture);
					break;
				case TOPSIDE:
					variation.icon = register.registerIcon(modName + ":" + variation.texture + "-side");
					variation.iconTop = register.registerIcon(modName + ":" + variation.texture + "-top");
					break;
				case TOPBOTSIDE:
					variation.icon = register.registerIcon(modName + ":" + variation.texture + "-side");
					variation.iconTop = register.registerIcon(modName + ":" + variation.texture + "-top");
					variation.iconBot = register.registerIcon(modName + ":" + variation.texture + "-bottom");
					break;
//				case CTM3:
//					CarvableVariationCTM ctm = new CarvableVariationCTM();
//					ctm.seams[0] = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-ctm1"), 4, 4);
//					ctm.seams[1] = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-ctm2"), 4, 4);
//					ctm.seams[2] = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-ctm3"), 4, 4);
//					variation.ctm = ctm;
//					break;
				case CTMV:
					variation.seamsCtmVert = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-ctmv"), 2, 2);
					variation.iconTop = register.registerIcon(modName + ":" + variation.texture + "-top");
					break;
				case CTMH:
					variation.seamsCtmVert = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-ctmh"), 2, 2);
					variation.iconTop = register.registerIcon(modName + ":" + variation.texture + "-top");
					break;
				case V9:
					variation.variations9 = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-v9"), 3, 3);
					break;
				case V4:
					variation.variations9 = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-v4"), 2, 2);
					break;
				case CTMX:
					variation.icon = register.registerIcon(modName + ":" + variation.texture);
					variation.submap = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-ctm"), 4, 4);
					variation.submapSmall = new TextureSubmap(variation.icon, 2, 2);
					break;
				case R16:
					variation.variations9 = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-r16"), 4, 4);
					break;
				case R9:
					variation.variations9 = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-r9"), 3, 3);
					break;
				case R4:
					variation.variations9 = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-r4"), 2, 2);
					break;
				case CUSTOM:
					variation.manager.registerIcons(modName, block, register);
				}
			}
		}
	}

	public void registerSubBlocks(Block block, CreativeTabs tabs, List<ItemStack> list) {
		for (CarvableVariation variation : variations) {
			list.add(new ItemStack(block, 1, variation.metadata));
		}
	}

	public void setChiselBlockName(String name) {
		blockName = name;
	}

	public void registerOre(String ore) {
		OreDictionary.registerOre(ore, variations.get(0).block);
	}
}
