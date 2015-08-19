package team.chisel.compat.nei;

import team.chisel.Chisel;
import team.chisel.client.gui.GuiChisel;
import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.registry.GameRegistry;

public class NEIChiselConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return Chisel.MOD_NAME;
	}

	@Override
	public String getVersion() {
		return Chisel.VERSION;
	}

	@Override
	public void loadConfig() {
		API.hideItem(new ItemStack(GameRegistry.findBlock("chisel", "limestone_slab_top")));
		API.hideItem(new ItemStack(GameRegistry.findBlock("chisel", "marble_slab_top")));
		API.hideItem(new ItemStack(GameRegistry.findBlock("chisel", "marble_pillar_slab_top")));

		RecipeHandlerChisel handler = new RecipeHandlerChisel();
		API.registerRecipeHandler(handler);
		API.registerUsageHandler(handler);
		API.setGuiOffset(GuiChisel.class, -50, 40);
	}
}
