package com.cricketcraft.chisel.network.message.base;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.cricketcraft.chisel.Chisel;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public abstract class MessageCoords implements IMessage {

	public int x, y, z;

	protected MessageCoords() {
		;
	}

	protected MessageCoords(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	protected MessageCoords(TileEntity te) {
		this(te.xCoord, te.yCoord, te.zCoord);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
	}

	protected Block getBlock(MessageContext ctx) {
		return getWorld(ctx).getBlock(x, y, z);
	}

	protected TileEntity getTileEntity(MessageContext ctx) {
		return getWorld(ctx).getTileEntity(x, y, z);
	}

	protected World getWorld(MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			return Chisel.proxy.getClientWorld();
		} else {
			return ctx.getServerHandler().playerEntity.worldObj;
		}
	}
}
