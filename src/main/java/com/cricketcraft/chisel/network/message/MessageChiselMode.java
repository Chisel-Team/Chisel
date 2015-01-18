package com.cricketcraft.chisel.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.item.chisel.ChiselMode;
import com.cricketcraft.chisel.utils.General;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


public class MessageChiselMode implements IMessage {

	public MessageChiselMode(){
	}
	
	private String mode;
	
	public MessageChiselMode(ChiselMode current) {
		this.mode = current.next().name();
	}

	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, mode);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.mode = ByteBufUtils.readUTF8String(buf);
	}
	
	public static class Handler implements IMessageHandler<MessageChiselMode, IMessage> {
		@Override
		public IMessage onMessage(MessageChiselMode message, MessageContext ctx) {
			ItemStack stack = ctx.getServerHandler().playerEntity.getCurrentEquippedItem();
			if (stack != null && stack.getItem() instanceof IChiselItem) {
				General.setChiselMode(stack, ChiselMode.valueOf(message.mode));
			}
			return null;
		}
	}
}
