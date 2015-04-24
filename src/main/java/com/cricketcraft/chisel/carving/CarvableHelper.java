package com.cricketcraft.chisel.carving;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.carving.IVariationInfo;
import com.cricketcraft.chisel.api.carving.VariationInfoBase;
import com.cricketcraft.chisel.api.rendering.ISubmapManager;
import com.cricketcraft.chisel.api.rendering.TextureType;
import com.cricketcraft.chisel.compat.fmp.FMPIMC;
import com.cricketcraft.chisel.item.ItemCarvable;
import com.cricketcraft.chisel.utils.GeneralClient;

import cpw.mods.fml.common.registry.GameRegistry;

public class CarvableHelper {

	private Block theBlock;
	
	public ArrayList<IVariationInfo> infoList = new ArrayList<IVariationInfo>();
	IVariationInfo[] infoMap = new IVariationInfo[16];
	public boolean forbidChiseling = false;
	
	public CarvableHelper(Block block) {
		this.theBlock = block;
	}
    public void addVariation(String description, int metadata, ISubmapManager manager) {
    	addVariation(description, metadata, null, manager);
    }

    public void addVariation(String description, int metadata, Block bb) {
        addVariation(description, metadata, null, bb, 0, Chisel.MOD_ID);
    }

    public void addVariation(String description, int metadata, Block bb, int blockMeta) {
        addVariation(description, metadata, null, bb, blockMeta, Chisel.MOD_ID);
    }

    public void addVariation(String description, int metadata, Block bb, int blockMeta, Material material) {
        addVariation(description, metadata, null, bb, blockMeta, Chisel.MOD_ID);
    }

    public void addVariation(String description, int metadata, String texture) {
        addVariation(description, metadata, texture, (ISubmapManager) null);
    }
    
    public void addVariation(String description, int metadata, String texture, ISubmapManager manager) {
    	addVariation(description, metadata, texture, null, 0, Chisel.MOD_ID, manager);
    }

	public void addVariation(String description, int metadata, Block bb, String modid) {
		addVariation(description, metadata, null, bb, 0, modid);
	}

	public void addVariation(String description, int metadata, Block bb, int blockMeta, String modid) {
		addVariation(description, metadata, null, bb, blockMeta, modid);
	}

	public void addVariation(String description, int metadata, Block bb, int blockMeta, Material material, String modid) {
		addVariation(description, metadata, null, bb, blockMeta, modid);
	}

	public void addVariation(String description, int metadata, String texture, String modid) {
		addVariation(description, metadata, texture, null, 0, modid);
	}

	public void addVariation(String description, int metadata, String texture, Block block, int blockMeta, String modid) {
		addVariation(description, metadata, texture, block, blockMeta, modid, null);
	}

	public void addVariation(String description, int metadata, String texture, Block block, int blockMeta, String modid, ISubmapManager customManager) {

		if (infoList.size() >= 16)
			return;

		CarvingVariation var = new CarvingVariation(theBlock, metadata, metadata);
		TextureType type = TextureType.getTypeFor(this, modid, texture);
		if (type == TextureType.CUSTOM && customManager == null) {
			throw new IllegalArgumentException(String.format("Could not find texture %s, and no custom texture manager was provided.", texture));
		}
		
		IVariationInfo info;
		if (customManager != null) {
			info = new VariationInfoBase(var, description, customManager);
		} else {
			info = new VariationInfoBase(var, description, type.createManagerFor(var, texture));
		}
		
		// TODO handling for block texture override params

		infoList.add(info);
		infoMap[metadata] = info;
	}


	public IVariationInfo getVariation(int metadata) {
		if (metadata < 0 || metadata > 15)
			metadata = 0;

		IVariationInfo info = infoMap[metadata];
		if (info == null)
			return null;

		return info;
	}

	public IIcon getIcon(int side, int metadata) {
		if (metadata < 0 || metadata > 15)
			metadata = 0;

		IVariationInfo info = infoMap[metadata];
		if (info == null)
			return GeneralClient.getMissingIcon();
		
		return info.getSubmapManager().getIcon(side, metadata);
	}

	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int metadata = world.getBlockMetadata(x, y, z);

		if (metadata < 0 || metadata > 15)
			metadata = 0;

		IVariationInfo info = infoMap[metadata];
		if (info == null)
			return GeneralClient.getMissingIcon();

		return info.getSubmapManager().getIcon(world, x, y, z, side);
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
		for (IVariationInfo info : infoList) {
			registerVariation(name, info);
		}
	}

	public void registerVariation(String name, IVariationInfo info) {

		Block block = info.getVariation().getBlock();
		
		if (block.renderAsNormalBlock() || block.isOpaqueCube() || block.isNormalCube()) {
			FMPIMC.registerFMP(block, info.getVariation().getBlockMeta());
		}

		if (forbidChiseling)
			return;

		Carving.chisel.addVariation(name, info.getVariation());
	}

	public void registerBlockIcons(String modName, Block block, IIconRegister register) {
		for (IVariationInfo info : infoList) {
			info.getSubmapManager().registerIcons(modName, block, register);
		}
	}

	public void registerSubBlocks(Block block, CreativeTabs tabs, List<ItemStack> list) {
		for (IVariationInfo info : infoList) {
			list.add(new ItemStack(block, 1, info.getVariation().getItemMeta()));
		}
	}

	public void registerOre(String ore) {
		OreDictionary.registerOre(ore, infoList.get(0).getVariation().getBlock());
	}
}
