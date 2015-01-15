package com.cricketcraft.chisel.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;

import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.cricketcraft.chisel.carving.CarvingVariation;
import com.cricketcraft.chisel.client.GeneralChiselClient;
import com.cricketcraft.chisel.item.chisel.ItemChisel;
import com.cricketcraft.chisel.network.message.base.MessageCoords;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageChiselSound extends MessageCoords {

	public MessageChiselSound() {
		super();
	}

	private int block;
	private byte meta;

	public MessageChiselSound(int x, int y, int z, ICarvingVariation v) {
		super(x, y, z);
		this.block = Block.getIdFromBlock(v.getBlock());
		this.meta = (byte) v.getBlockMeta();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(block);
		buf.writeByte(meta);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
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
