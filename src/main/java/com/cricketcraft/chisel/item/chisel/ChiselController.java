package com.cricketcraft.chisel.item.chisel;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import org.apache.commons.lang3.ArrayUtils;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.carving.CarvingVariation;
import com.cricketcraft.chisel.network.PacketHandler;
import com.cricketcraft.chisel.network.message.MessageChiselSound;
import com.cricketcraft.chisel.utils.General;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class ChiselController {

	public static final ChiselController INSTANCE = new ChiselController();

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
			
			ICarvingVariation[]	variations = list.toArray(new CarvingVariation[] {});

			if (chisel.canChiselBlock(event.world, x, y, z, block, metadata)) {
				ItemStack target = General.getChiselTarget(held);

				if (target != null) {
					for (ICarvingVariation v : variations) {
						if (v.getBlock() == Block.getBlockFromItem(target.getItem()) && v.getBlockMeta() == target.getItemDamage()) {
							setVariation(event.entityPlayer, event.world, x, y, z, v, target);
						}
					}
				} else {
					for (int i = 0; i < variations.length; i++) {
						ICarvingVariation v = variations[i];
						if (v.getBlock() == block && v.getBlockMeta() == metadata) {
							variations = ArrayUtils.remove(variations, i--);
						}
					}

					int index = event.world.rand.nextInt(variations.length);
					ICarvingVariation newVar = variations[index];
					setVariation(event.entityPlayer, event.world, x, y, z, newVar, new ItemStack(newVar.getBlock(), 1, newVar.getItemMeta()));
					event.entityPlayer.inventory.currentItem = slot;
				}
			}
			break;
		case RIGHT_CLICK_AIR:
		case RIGHT_CLICK_BLOCK:
			if (!event.world.isRemote && chisel.canOpenGui(event.world, event.entityPlayer, held)) {
				event.entityPlayer.openGui(Chisel.instance, 0, event.world, 0, 0, 0);
			}
			break;
		}
	}

	/**
	 * Assumes that the player is holding a chisel
	 */
	private void setVariation(EntityPlayer player, World world, int x, int y, int z, ICarvingVariation v, ItemStack target) {
		PacketHandler.INSTANCE.sendTo(new MessageChiselSound(x, y, z, v), (EntityPlayerMP) player);
		world.setBlock(x, y, z, v.getBlock(), v.getBlockMeta(), 3);
		((IChiselItem) player.getCurrentEquippedItem().getItem()).onChisel(world, player.inventory, player.inventory.currentItem, player.getCurrentEquippedItem(), target);
	}
}
