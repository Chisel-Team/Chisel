package team.chisel.api.carving;

import net.minecraft.client.resources.I18n;
import team.chisel.api.render.IBlockRenderType;

public class VariationInfoBase implements IVariationInfo {

	private ICarvingVariation variation;
	private String unlocDesc;
	private IBlockRenderType type;

	public VariationInfoBase(ICarvingVariation variation, String unlocDesc, IBlockRenderType type) {
		this.variation = variation;
		this.unlocDesc = unlocDesc;
		this.type = type;
	}

	@Override
	public ICarvingVariation getVariation() {
		return variation;
	}

	@Override
	public String getDescription() {
		return I18n.format(unlocDesc);
	}

	@Override
	public IBlockRenderType getType() {
		return type;
	}
}
