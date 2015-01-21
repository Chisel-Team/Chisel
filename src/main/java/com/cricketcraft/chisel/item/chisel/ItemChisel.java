package com.cricketcraft.chisel.item.chisel;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.config.Configurations;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ItemChisel extends Item implements IChiselItem {

	public enum ChiselType {
		IRON(Configurations.ironChiselMaxDamage, Configurations.ironChiselAttackDamage),
		DIAMOND(Configurations.diamondChiselMaxDamage, Configurations.diamondChiselAttackDamage);

		final int maxDamage;
		final int attackDamage;

		ChiselType(int maxDamage, int attackDamage) {
			this.maxDamage = maxDamage;
			this.attackDamage = attackDamage;
		}
	}

	public static Carving carving = Carving.chisel;

	private ChiselType type;

	public ItemChisel(ChiselType type) {
		super();
		this.type = type;
		setMaxStackSize(1);
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
		String base = "item.chisel.chisel.desc.";
		String gui = I18n.format(base + "gui");
		String lc1 = I18n.format(base + "lc1");
		String lc2 = I18n.format(base + "lc2");
		String modes = I18n.format(base + "modes");
		list.add(gui);
		if (type == ChiselType.DIAMOND || Configurations.ironChiselCanLeftClick) {
			list.add(lc1);
			list.add(lc2);
		}
		if (type == ChiselType.DIAMOND || Configurations.ironChiselHasModes) {
			list.add("");
			list.add(modes);
		}
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Multimap getAttributeModifiers(ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Chisel Damage", type.attackDamage, 0));
		return multimap;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		return super.hitEntity(stack, attacker, target);
	}
	
	@Override
	public boolean canOpenGui(World world, EntityPlayer player, ItemStack chisel) {
		return true;
	}

	@Override
	public boolean canChisel(World world, ItemStack chisel, ICarvingVariation target) {
		return true;
	}

	@Override
	public boolean onChisel(World world, ItemStack chisel, ICarvingVariation target) {
		return Configurations.allowChiselDamage;
	}

	@Override
	public boolean canChiselBlock(World world, int x, int y, int z, Block block, int metadata) {
		return type == ChiselType.DIAMOND || Configurations.ironChiselCanLeftClick;
	}

	@Override
	public boolean hasModes(ItemStack chisel) {
		return type == ChiselType.DIAMOND || Configurations.ironChiselHasModes;
	}
}