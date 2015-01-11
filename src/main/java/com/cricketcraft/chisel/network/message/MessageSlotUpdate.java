package com.cricketcraft.chisel.network.message;

import com.cricketcraft.chisel.Chisel;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageSlotUpdate implements IMessage {

	public MessageSlotUpdate() {
	}

	private int x, y, z;
	private int slot;
	private ItemStack stack;

	public MessageSlotUpdate(TileEntity te, int slot, ItemStack stack) {
		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
		this.slot = slot;
		this.stack = stack;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(slot);
		ByteBufUtils.writeItemStack(buf, stack);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
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
