package com.cricketcraft.chisel.carving;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IIcon;

import com.cricketcraft.chisel.carving.CarvableHelper.TextureType;
import com.cricketcraft.chisel.client.render.TextureSubmap;

public class CarvableVariation {

	public String blockName;
	public String descriptionUnloc;
	public int metadata;
	public TextureType type = TextureType.NORMAL;

	public Block block;
	public int blockMeta;

	public String texture;

	public IIcon icon;
	public IIcon iconBot;
	public IIcon iconTop;

	public CarvableVariationCTM ctm;
	public TextureSubmap seamsCtmVert;
	public TextureSubmap variations9;

	public TextureSubmap submap;
	public TextureSubmap submapSmall;

	public String getDescription() {
		return I18n.format(descriptionUnloc);
	}

	public void setDescriptionUnloc(String desc) {
		this.descriptionUnloc = desc;
	}

	static class CarvableVariationCTM {

		TextureSubmap seams[] = new TextureSubmap[3];
	}
}
