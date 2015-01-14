package com.cricketcraft.chisel.compat.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.carving.CarvingGroup;
import com.cricketcraft.chisel.carving.CarvingVariation;

public class RecipeHandlerChisel extends TemplateRecipeHandler {

	private static final int SLOTS_PER_PAGE = 45;

	public class CachedChiselRecipe extends CachedRecipe {

		private List<PositionedStack> input = new ArrayList<PositionedStack>();
		private List<PositionedStack> outputs = new ArrayList<PositionedStack>();
		public Point focus;

		public CachedChiselRecipe(List<ItemStack> variations, ItemStack base, ItemStack focus) {
			PositionedStack pStack = new PositionedStack(base != null ? base : variations, 74, 4);
			pStack.setMaxSize(1);
			this.input.add(pStack);

			int row = 0;
			int col = 0;
			for (ItemStack v : variations) {
				this.outputs.add(new PositionedStack(v, 3 + 18 * col, 37 + 18 * row));

				if (focus != null && NEIServerUtils.areStacksSameTypeCrafting(focus, v)) {
					this.focus = new Point(2 + 18 * col, 36 + 18 * row);
				}

				col++;
				if (col > 8) {
					col = 0;
					row++;
				}
			}
		}

		public CachedChiselRecipe(List<ItemStack> variations) {
			this(variations, null, null);
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(cycleticks / 20, this.input);
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			return this.outputs;
		}

		@Override
		public PositionedStack getResult() {
			return null;
		}

	}

	@Override
	public String getRecipeName() {
		return "Chisel";
	}

	@Override
	public String getGuiTexture() {
		return "Chisel:textures/chiselNEI.png";
	}

	@Override
	public void drawBackground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 130);

		Point focus = ((CachedChiselRecipe) this.arecipes.get(recipeIndex)).focus;
		if (focus != null) {
			GuiDraw.drawTexturedModalRect(focus.x, focus.y, 166, 0, 18, 18);
		}
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void loadTransferRects() {
		this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(75, 22, 15, 13), "chisel2.chisel", new Object[0]));
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("chisel2.chisel")) {
			for (String name : Carving.chisel.getSortedGroupNames()) {
				CarvingGroup g = Carving.chisel.getGroup(name);
				if (!g.getVariations().isEmpty()) {
					addCached(getVariationStacks(g));
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for (String name : Carving.chisel.getSortedGroupNames()) {
			CarvingGroup g = Carving.chisel.getGroup(name);
			List<ItemStack> variations = getVariationStacks(g);
			for (ItemStack stack : variations) {
				if (NEIServerUtils.areStacksSameTypeCrafting(stack, result)) {
					addCached(variations, null, result);
				}
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for (String name : Carving.chisel.getSortedGroupNames()) {
			CarvingGroup g = Carving.chisel.getGroup(name);
			List<ItemStack> variations = getVariationStacks(g);
			for (ItemStack stack : variations) {
				if (NEIServerUtils.areStacksSameTypeCrafting(stack, ingredient)) {
					addCached(variations, ingredient, null);
				}
			}
		}
	}

	private void addCached(List<ItemStack> variations, ItemStack base, ItemStack focus) {
		if (variations.size() > SLOTS_PER_PAGE) {
			List<List<ItemStack>> parts = new ArrayList<List<ItemStack>>();
			int size = variations.size();
			for (int i = 0; i < size; i += SLOTS_PER_PAGE) {
				parts.add(new ArrayList<ItemStack>(variations.subList(i, Math.min(size, i + SLOTS_PER_PAGE))));
			}
			for (List<ItemStack> part : parts) {
				this.arecipes.add(new CachedChiselRecipe(part, base, focus));
			}
		} else {
			this.arecipes.add(new CachedChiselRecipe(variations, base, focus));
		}
	}

	private void addCached(List<ItemStack> variations) {
		addCached(variations, null, null);
	}

	private static List<ItemStack> getVariationStacks(CarvingGroup g) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		for (CarvingVariation v : g.getVariations()) {
			stacks.add(v.getStack());
		}
		String oreName = g.getOreName();
		if (oreName != null) {
			check: for (ItemStack ore : OreDictionary.getOres(oreName)) {
				for (ItemStack stack : stacks) {
					if (NEIServerUtils.areStacksSameTypeCrafting(stack, ore)) {
						continue check;
					}
				}
				stacks.add(ore.copy());
			}
		}
		return stacks;
	}

}
