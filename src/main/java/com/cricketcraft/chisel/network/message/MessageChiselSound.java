package com.cricketcraft.chisel.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;

import com.cricketcraft.chisel.carving.CarvingVariation;
import com.cricketcraft.chisel.client.GeneralChiselClient;
import com.cricketcraft.chisel.item.chisel.ItemChisel;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageChiselSound implements IMessage {

	public MessageChiselSound() {
	}

	private int x, y, z;
	private int block;
	private byte meta;

	public MessageChiselSound(int x, int y, int z, CarvingVariation v) {
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

	public static class Handler implements IMessageHandler<MessageChiselSound, IMessage> {

		@Override
		public IMessage onMessage(MessageChiselSound message, MessageContext ctx) {
			String sound = ItemChisel.carving.getVariationSound(Block.getBlockById(message.block), message.meta);
			GeneralChiselClient.spawnChiselEffect(message.x, message.y, message.z, sound);
			return null;
		}
	}
}
