package com.cricketcraft.chisel.item.chisel;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import org.apache.commons.lang3.ArrayUtils;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.PacketHandler;
import com.cricketcraft.chisel.api.ChiselMode;
import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.carving.CarvingVariation;
import com.cricketcraft.chisel.client.GeneralChiselClient;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

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

		// TODO config for diamond only
		if (held == null || !(held.getItem() instanceof IChiselItem)) {
			return;
		}

		switch (event.action) {
		case LEFT_CLICK_BLOCK:
			int x = event.x,
			y = event.y,
			z = event.z;
			Block block = event.world.getBlock(x, y, z);
			int metadata = event.world.getBlockMetadata(x, y, z);
			CarvingVariation[] variations = Carving.chisel.getVariations(block, metadata);

			if (variations != null) {
				ItemStack target = ((IChiselItem) held.getItem()).getTarget(held);

				if (target != null) {
					for (CarvingVariation v : variations) {
						if (v.block == Block.getBlockFromItem(target.getItem()) && v.meta == target.getItemDamage()) {
							setVariation(event.entityPlayer, event.world, x, y, z, v, target);
						}
					}
				} else {
					for (int i = 0; i < variations.length; i++) {
						CarvingVariation v = variations[i];
						if (v.block == block && v.meta == metadata) {
							variations = ArrayUtils.remove(variations, i--);
						}
					}

					int index = event.world.rand.nextInt(variations.length);
					CarvingVariation newVar = variations[index];
					setVariation(event.entityPlayer, event.world, x, y, z, newVar, new ItemStack(newVar.block, 1, newVar.damage));
					event.entityPlayer.inventory.currentItem = slot;
				}
			}
			break;
		case RIGHT_CLICK_AIR:
		case RIGHT_CLICK_BLOCK:
			event.entityPlayer.openGui(Chisel.instance, 0, event.world, 0, 0, 0);
			break;
		}
		if (event.action == Action.LEFT_CLICK_BLOCK) {

		}
	}

	/**
	 * Assumes that the player is holding a chisel
	 */
	private void setVariation(EntityPlayer player, World world, int x, int y, int z, CarvingVariation v, ItemStack target) {
		PacketHandler.INSTANCE.sendTo(new PacketPlaySound(x, y, z, v), (EntityPlayerMP) player);
		world.setBlock(x, y, z, v.block);
		world.setBlockMetadataWithNotify(x, y, z, v.meta, 3);
		((IChiselItem) player.getCurrentEquippedItem().getItem()).onChisel(world, player.inventory, player.inventory.currentItem, player.getCurrentEquippedItem(), target);
	}

	public static class PacketPlaySound implements IMessage {

		public PacketPlaySound() {
		}

		private int x, y, z;
		private int block;
		private byte meta;

		public PacketPlaySound(int x, int y, int z, CarvingVariation v) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.block = Block.getIdFromBlock(v.block);
			this.meta = (byte) v.meta;
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
			buf.writeInt(block);
			buf.writeByte(meta);
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			x = buf.readInt();
			y = buf.readInt();
			z = buf.readInt();
			block = buf.readInt();
			meta = buf.readByte();
		}
	}

	public static class PlaySoundHandler implements IMessageHandler<PacketPlaySound, IMessage> {

		@Override
		public IMessage onMessage(PacketPlaySound message, MessageContext ctx) {
			String sound = ItemChisel.carving.getVariationSound(Block.getBlockById(message.block), message.meta);
			GeneralChiselClient.spawnChiselEffect(message.x, message.y, message.z, sound);
			return null;
		}
	}

}
