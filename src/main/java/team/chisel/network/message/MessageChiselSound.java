package team.chisel.network.message;

import com.cricketcraft.chisel.api.carving.ICarvingVariation;

import team.chisel.Chisel;
import team.chisel.client.GeneralChiselClient;
import team.chisel.item.chisel.ItemChisel;
import team.chisel.network.message.base.MessageCoords;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageChiselSound extends MessageCoords {

	public MessageChiselSound() {
		super();
	}

	private int block;
	private byte meta;
	private boolean breakChisel;

	public MessageChiselSound(int x, int y, int z, ICarvingVariation v, boolean breakChisel) {
		super(x, y, z);
		this.block = Block.getIdFromBlock(v.getBlock());
		this.meta = (byte) v.getBlockMeta();
		this.breakChisel = breakChisel;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(block);
		buf.writeByte(meta);
		buf.writeBoolean(breakChisel);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		block = buf.readInt();
		meta = buf.readByte();
		breakChisel = buf.readBoolean();
	}

	public static class Handler implements IMessageHandler<MessageChiselSound, IMessage> {

		@Override
		public IMessage onMessage(MessageChiselSound message, MessageContext ctx) {
			String sound = ItemChisel.carving.getVariationSound(Block.getBlockById(message.block), message.meta);
			GeneralChiselClient.spawnChiselEffect(message.x, message.y, message.z, sound);
			if (message.breakChisel) {
				EntityPlayer player = Chisel.proxy.getClientPlayer();
				player.renderBrokenItemStack(player.getCurrentEquippedItem());
			}
			return null;
		}
	}
}
