package team.chisel.network.message;

import com.cricketcraft.chisel.api.IAdvancedChisel;
import com.cricketcraft.chisel.api.IChiselItem;
import com.cricketcraft.chisel.api.carving.IChiselMode;

import team.chisel.item.chisel.ChiselMode;
import team.chisel.utils.General;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageChiselMode implements IMessage {

	public MessageChiselMode() {
	}

	private String mode;

	public MessageChiselMode(IChiselMode iChiselMode) {
		this.mode = iChiselMode.name();
	}

	@Override
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
				if (stack.getItem() instanceof IAdvancedChisel) {
					General.setChiselMode(stack, ((IAdvancedChisel) stack.getItem()).getMode(stack, message.mode));
				} else {
					General.setChiselMode(stack, ChiselMode.valueOf(message.mode));
				}
			}
			return null;
		}
	}
}
