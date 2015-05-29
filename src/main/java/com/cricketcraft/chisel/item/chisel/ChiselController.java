package com.cricketcraft.chisel.item.chisel;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.cricketcraft.chisel.api.carving.IChiselMode;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.utils.General;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class ChiselController {

	public static final ChiselController INSTANCE = new ChiselController();

	private long lastTickClick = 0;

	private ChiselController() {
	}

	public void preInit() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event) {
		ItemStack held = event.entityPlayer.getCurrentEquippedItem();
		int slot = event.entityPlayer.inventory.currentItem;

		if (held == null || !(held.getItem() instanceof IChiselItem)) {
			return;
		}

		IChiselItem chisel = (IChiselItem) held.getItem();

		switch (event.action) {
		case LEFT_CLICK_BLOCK:
			int x = event.x,
			y = event.y,
			z = event.z;
			Block block = event.world.getBlock(x, y, z);
			int metadata = event.world.getBlockMetadata(x, y, z);
			ICarvingGroup group = Carving.chisel.getGroup(block, metadata);

			if (group == null) {
				return;
			}

			List<ICarvingVariation> list = group.getVariations();

			main: for (ItemStack stack : OreDictionary.getOres(group.getOreName())) {
				ICarvingVariation v = General.getVariation(stack);
				for (ICarvingVariation check : list) {
					if (check.getBlock() == v.getBlock() && check.getBlockMeta() == v.getBlockMeta()) {
						continue main;
					}
				}
				list.add(General.getVariation(stack));
			}

			ICarvingVariation[] variations = list.toArray(new ICarvingVariation[] {});

			if (chisel.canChiselBlock(event.world, event.entityPlayer, x, y, z, block, metadata)) {
				ItemStack target = General.getChiselTarget(held);
				IChiselMode mode = General.getChiselMode(held);
				ForgeDirection sideHit = ForgeDirection.VALID_DIRECTIONS[event.face];

				if (target != null) {
					for (ICarvingVariation v : variations) {
						if (v.getBlock() == Block.getBlockFromItem(target.getItem()) && v.getBlockMeta() == target.getItemDamage()) {
							mode.chiselAll(event.entityPlayer, event.world, x, y, z, sideHit, v);
						}
					}
				} else {
					int idx = 0;
					for (int i = 0; i < variations.length; i++) {
						ICarvingVariation v = variations[i];
						if (v.getBlock() == block && v.getBlockMeta() == metadata) {
							idx = (i + 1) % variations.length; // move to the next in the group
							break;
						}
					}

					ICarvingVariation newVar = variations[idx];
					mode.chiselAll(event.entityPlayer, event.world, x, y, z, sideHit, newVar);
					event.entityPlayer.inventory.currentItem = slot;
				}
			}
			break;
		case RIGHT_CLICK_AIR:
		case RIGHT_CLICK_BLOCK:
			// Make sure we have not responded this tick
			if (event.world.getTotalWorldTime() == lastTickClick) {
				break;
			} else {
				lastTickClick = event.world.getTotalWorldTime();
			}
			if (!event.world.isRemote && chisel.canOpenGui(event.world, event.entityPlayer, held)) {
				event.entityPlayer.openGui(Chisel.instance, 0, event.world, 0, 0, 0);
			}
			break;
		}
	}

}
