package com.cricketcraft.chisel.network.message;

import net.minecraft.tileentity.TileEntity;

import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.network.message.base.MessageCoords;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageAutoChisel extends MessageCoords {

	public MessageAutoChisel() {
	}

	public MessageAutoChisel(TileEntityAutoChisel tile) {
		super(tile);
	}

	public static class Handler implements IMessageHandler<MessageAutoChisel, IMessage> {

		public IMessage onMessage(MessageAutoChisel message, MessageContext ctx) {

			TileEntity te = message.getTileEntity(ctx);
			if (te instanceof TileEntityAutoChisel) {
				((TileEntityAutoChisel) te).doChiselAnim();
			}
			return null;
		}
	}

}
