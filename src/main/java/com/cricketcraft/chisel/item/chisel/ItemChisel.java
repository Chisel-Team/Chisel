package com.cricketcraft.chisel.item.chisel;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ChiselMode;
import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.config.Configurations;
import com.cricketcraft.chisel.utils.GeneralClient;

public class ItemChisel extends Item implements IChiselItem {

	public enum ChiselType {
		IRON(Configurations.ironChiselMaxDamage), DIAMOND(Configurations.diamondChiselMaxDamage);

		final int maxDamage;

		ChiselType(int maxDamage) {
			this.maxDamage = maxDamage;
		}
	}

	public static Carving carving = Carving.chisel;

	private ChiselType type;

	public ItemChisel(ChiselType type) {
		super();
		this.type = type;
		setMaxStackSize(1);
		setHasSubtypes(true);
		setTextureName(Chisel.MOD_ID + ":chisel_" + type.name().toLowerCase());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		if (Configurations.allowChiselDamage) {
			return type.maxDamage;
		}
		return 0;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + Chisel.MOD_ID + ".chisel_" + type.name().toLowerCase();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean held) {
		String unloc = "item.chisel.chisel.desc.";
		for (int i = 0; i < 3; i ++) {
			list.add(StatCollector.translateToLocal(unloc + i));
		}
		
		if (type == ChiselType.DIAMOND) {
			list.add("");
			list.add(StatCollector.translateToLocal(getUnlocalizedName(stack) + ".desc"));
		}
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public ChiselMode getChiselMode(ItemStack itemStack) {
		return ChiselMode.SINGLE;
	}

	@Override
	public void onChisel(World world, IInventory inv, int slot, ItemStack chisel, ItemStack target) {
		if (Configurations.allowChiselDamage) {
			chisel.setItemDamage(chisel.getItemDamage() + 1);
		}

		if (world.isRemote) {
			String sound = carving.getVariationSound(target.getItem(), target.getItemDamage());
			EntityPlayer player = Chisel.proxy.getClientPlayer();
			GeneralClient.playChiselSound(world, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), sound);
		} else {
			if (chisel.getItemDamage() >= chisel.getMaxDamage()) {
				inv.decrStackSize(slot, 1);
			}
		}
	}
}