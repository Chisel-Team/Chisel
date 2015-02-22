package com.cricketcraft.chisel.carving;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.carving.CarvableVariation.CarvableVariationCTM;
import com.cricketcraft.chisel.client.render.CTM;
import com.cricketcraft.chisel.client.render.TextureSubmap;
import com.cricketcraft.chisel.compat.FMPIntegration;
import com.cricketcraft.chisel.config.Configurations;
import com.cricketcraft.chisel.item.ItemCarvable;
import com.cricketcraft.chisel.utils.GeneralClient;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class CarvableHelper {

	public static final int NORMAL = 0;
	public static final int TOPSIDE = 1;
	public static final int TOPBOTSIDE = 2;
	public static final int CTM3 = 3;
	public static final int CTMV = 4;
	public static final int CTMH = 5;
	public static final int V9 = 6;
	public static final int V4 = 7;
    public static final int CTMX = 8;
    public static final int R16 = 9;
    public static final int R9 = 10;
    public static final int R4 = 11;

    private static final Random rand = new Random();

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

			boolean any = Chisel.class.getResource(path + ".png") != null;
			boolean ctm3 = Chisel.class.getResource(path + "-ctm1.png") != null && Chisel.class.getResource(path + "-ctm2.png") != null && Chisel.class.getResource(path + "-ctm3.png") != null;
			boolean ctmv = Chisel.class.getResource(path + "-ctmv.png") != null;
			boolean ctmh = Chisel.class.getResource(path + "-ctmh.png") != null;
			boolean side = Chisel.class.getResource(path + "-side.png") != null;
			boolean top = Chisel.class.getResource(path + "-top.png") != null;
			boolean bot = Chisel.class.getResource(path + "-bottom.png") != null;
			boolean v9 = Chisel.class.getResource(path + "-v9.png") != null;
			boolean v4 = Chisel.class.getResource(path + "-v4.png") != null;
			boolean ctmx = Chisel.class.getResource(path + "-ctm.png") != null;
            boolean r16 = Chisel.class.getResource(path + "-r16.png") != null;
            boolean r9 = Chisel.class.getResource(path + "-r9.png") != null;
            boolean r4 = Chisel.class.getResource(path + "-r4.png") != null;

			if (ctm3) {
				variation.kind = 3;
			} else if (ctmh && top) {
				variation.kind = 5;
			} else if (ctmv && top) {
				variation.kind = CTMV;
			} else if (bot && top && side) {
				variation.kind = 2;
			} else if (top && side) {
				variation.kind = 1;
			} else if (v9) {
				variation.kind = V9;
			} else if (v4) {
				variation.kind = V4;
			} else if (any && ctmx && !Configurations.disableCTM) {
				variation.kind = CTMX;
			} else if (r16) {
                variation.kind = R16;
            } else if (r9) {
                variation.kind = R9;
            } else if (r4) {
                variation.kind = R4;
            } else if (any) {
				variation.kind = 0;
			} else {
				throw new RuntimeException("No valid textures found for chisel block variation '" + description + "' (" + variation.texture + ")");
			}
		} else {
			variation.block = block;
			variation.kind = 2;
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

		switch (variation.kind) {
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
            case CTM3:
                return variation.ctm.seams[0].icons[0];
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

		switch (variation.kind) {
		case NORMAL:
		case TOPSIDE:
		case TOPBOTSIDE:
			return getIcon(side, metadata);
		case CTM3:
			int tex = CTM.getTexture(world, x, y, z, side);

			int row = tex / 16;
			int col = tex % 16;

			return variation.ctm.seams[col / 4].icons[col % 4 + row * 4];
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
            int variationSize = (variation.kind == V9) ? 3 : 2;

            int xModulus = x % variationSize;
            int zModulus = z % variationSize;
            //This ensures that blocks placed near 0,0 or it's axis' do not misbehave
            int textureX = (xModulus < 0) ? (xModulus + variationSize) : xModulus;
            int textureZ = (zModulus < 0) ? (zModulus + variationSize) : zModulus;
            //Always invert the y index
            int textureY = (variationSize - (y % variationSize) - 1);

            if (side == 2 || side == 5) {
                //For WEST, SOUTH reverse the indexes for both X and Z
                textureX = (variationSize - textureX - 1);
                textureZ = (variationSize - textureZ - 1);
            } /*else if (side == 0) {
                //For DOWN, reverse the indexes for only Z
                textureZ = (variationSize - textureZ - 1);
            }//*/

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
            if ((side==2)||(side==5)) {
                indexRan = -indexRan;
            }
            while (indexRan < 0) {
                indexRan = indexRan+10000;
            }

            //rand.setSeed(indexRan); // Broken

            return variation.variations9.icons[ /*rand*/ indexRan % ((variation.kind == R9) ? 9 : 4)];
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
					switch (variation.kind) {
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
				switch (variation.kind) {
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
				case CTM3:
					CarvableVariationCTM ctm = new CarvableVariationCTM();
					ctm.seams[0] = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-ctm1"), 4, 4);
					ctm.seams[1] = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-ctm2"), 4, 4);
					ctm.seams[2] = new TextureSubmap(register.registerIcon(modName + ":" + variation.texture + "-ctm3"), 4, 4);
					variation.ctm = ctm;
					break;
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

	public static Set<Block> getChiselBlockSet() {
		HashSet<Block> tools = new HashSet<Block>();
		return tools;
	}

	public void registerOre(String ore) {
		OreDictionary.registerOre(ore, variations.get(0).block);
	}
}
