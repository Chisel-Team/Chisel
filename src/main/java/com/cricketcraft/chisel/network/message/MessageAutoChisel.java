package com.cricketcraft.chisel.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.network.message.base.MessageCoords;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageAutoChisel extends MessageCoords {

	public MessageAutoChisel() {
	}

	ItemStack base;
	boolean playSound, breakChisel;
	int chiseled;

	public MessageAutoChisel(TileEntityAutoChisel tile, int chiseled, boolean playSound, boolean breakChisel) {
		super(tile);
		this.base = tile.getStackInSlot(TileEntityAutoChisel.BASE);
		if (base != null) {
			base = base.copy();
		}
		this.playSound = playSound;
		this.breakChisel = breakChisel;
		this.chiseled = chiseled;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(playSound);
		buf.writeBoolean(breakChisel);
		buf.writeInt(chiseled);
		ByteBufUtils.writeItemStack(buf, base);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		this.playSound = buf.readBoolean();
		this.breakChisel = buf.readBoolean();
		this.chiseled = buf.readInt();
		this.base = ByteBufUtils.readItemStack(buf);
	}

	public static class Handler implements IMessageHandler<MessageAutoChisel, IMessage> {

		@Override
		public IMessage onMessage(MessageAutoChisel message, MessageContext ctx) {

			TileEntity te = message.getTileEntity(ctx);
			if (te instanceof TileEntityAutoChisel) {
				((TileEntityAutoChisel) te).doChiselAnim(message.base, message.chiseled, message.playSound, message.breakChisel);
			}
			return null;
		}
	}

}
