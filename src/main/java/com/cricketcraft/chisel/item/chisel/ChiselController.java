package com.cricketcraft.chisel.item.chisel;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ChiselMode;
import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.carving.CarvingVariation;
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
			List<ICarvingVariation> list = Carving.chisel.getVariations(block, metadata);

			if (list == null || list.isEmpty()) {
				break;
			}

			ICarvingVariation[] variations = list.toArray(new CarvingVariation[] {});

			if (chisel.canChiselBlock(event.world, x, y, z, block, metadata)) {
				ItemStack target = General.getChiselTarget(held);
				ChiselMode mode = General.getChiselMode(held);
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
			
			if (!event.world.isRemote && event.entityPlayer.isSneaking()) {
				ChiselMode mode = General.getChiselMode(held);
				General.setChiselMode(held, ChiselMode.values()[(mode.ordinal() + 1) % 3]);
				event.entityPlayer.addChatMessage(new ChatComponentText(General.getChiselMode(held).toString()));
				break;
			}
			if (!event.world.isRemote && chisel.canOpenGui(event.world, event.entityPlayer, held)) {
				event.entityPlayer.openGui(Chisel.instance, 0, event.world, 0, 0, 0);
			}
			break;
		}
	}

}
