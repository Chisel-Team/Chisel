package com.cricketcraft.chisel.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.network.message.base.MessageCoords;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageSlotUpdate extends MessageCoords {

	public MessageSlotUpdate() {
	}

	private int slot;
	private ItemStack stack;

	public MessageSlotUpdate(TileEntity te, int slot, ItemStack stack) {
		super(te);
		this.slot = slot;
		this.stack = stack;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(slot);
		ByteBufUtils.writeItemStack(buf, stack);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		this.slot = buf.readInt();
		this.stack = ByteBufUtils.readItemStack(buf);
	}

	public static class Handler implements IMessageHandler<MessageSlotUpdate, IMessage> {

		@Override
		public IMessage onMessage(MessageSlotUpdate message, MessageContext ctx) {
			TileEntity te = Chisel.proxy.getClientWorld().getTileEntity(message.x, message.y, message.z);
			if (te != null && te instanceof IInventory) {
				((IInventory) te).setInventorySlotContents(message.slot, message.stack);
			}
			return null;
		}
	}

}
