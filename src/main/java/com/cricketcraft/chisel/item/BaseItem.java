package com.cricketcraft.chisel.item;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class BaseItem extends Item {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		getWrappedDesc(list, itemStack);
	}

	public static void getWrappedDesc(List<String> list, ItemStack stack) {
		String[] wrappedDesc;
		wrappedDesc = wrap(StatCollector.translateToLocal(stack.getUnlocalizedName() + ".desc"), 35);
		for (String element : wrappedDesc)
			list.add(element.trim());
	}

	public static String[] wrap(String input, int len) {
		// return empty array for null text
		if (input == null)
			return new String[] {};

		// return text if len is zero or less
		if (len <= 0)
			return new String[] { input };

		// return text if less than length
		if (input.length() <= len)
			return new String[] { input };

		char[] chars = input.toCharArray();
		Vector<String> lines = new Vector<String>();
		StringBuffer line = new StringBuffer();
		StringBuffer word = new StringBuffer();

		for (char c : chars) {
			word.append(c);

			if (c == ' ') {
				if ((line.length() + word.length()) > len) {
					lines.add(line.toString());
					line.delete(0, line.length());
				}

				line.append(word);
				word.delete(0, word.length());
			}
		}

		// handle any extra chars in current word
		if (word.length() > 0) {
			if ((line.length() + word.length()) > len) {
				lines.add(line.toString());
				line.delete(0, line.length());
			}
			line.append(word);
		}

		// handle extra line
		if (line.length() > 0) {
			lines.add(line.toString());
		}

		String[] ret = new String[lines.size()];
		int c = 0; // counter
		for (Enumeration<String> e = lines.elements(); e.hasMoreElements(); c++) {
			ret[c] = e.nextElement();
		}

		return ret;
	}
}
