package com.cricketcraft.chisel.compat.nei;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.carving.CarvingGroup;
import com.cricketcraft.chisel.carving.CarvingVariation;
import com.cricketcraft.chisel.client.gui.GuiChisel;
import com.google.common.collect.Lists;

public class RecipeHandlerChisel extends TemplateRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Chiseling";
	}

	@Override
	public String getGuiTexture() {
		return "Chisel:textures/chisel2Gui.png";
	}

	public PositionedStack getResult() {
		return null;
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiChisel.class;
	}

	@Override
	public String getOverlayIdentifier() {
		return "EnderIOSagMill";
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(149, 32, 16, 16), "ChiselRecipes", new Object[0]));
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		// is this right? idk
		for (CarvingGroup g : Carving.chisel.getGroups()) {
			arecipes.add(new CachedChiselRecipe(g));
		}
	}
	
	@Override
	public void loadCraftingRecipes(final ItemStack result) {
		// unfinished
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		// unfinished
	}

	@Override
	public void drawBackground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 65);
	}

	public class CachedChiselRecipe extends TemplateRecipeHandler.CachedRecipe {

		private PositionedStack input;
		private List<PositionedStack> outputs;

		@Override
		public List<PositionedStack> getIngredients() {
			return Lists.newArrayList(input);
		}

		@Override
		public PositionedStack getResult() {
			return outputs.size() > 0 ? outputs.get(0) : null;
		}
		
		public CachedChiselRecipe(CarvingGroup group) {
			this.outputs = Lists.newArrayList();
			for (CarvingVariation v : group.getVariations()) {
				if (this.input == null) {
					this.input = new PositionedStack(stack(v), 10, 10);
				}
				outputs.add(new PositionedStack(stack(v), 50, 10));
			}
		}
		
		private ItemStack stack(CarvingVariation v) {
			return new ItemStack(v.block, 1, v.damage);
		}
	}
}