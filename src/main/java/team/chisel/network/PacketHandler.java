package team.chisel.network;

import team.chisel.Chisel;
import team.chisel.network.message.MessageAutoChisel;
import team.chisel.network.message.MessageChiselMode;
import team.chisel.network.message.MessageChiselSound;
import team.chisel.network.message.MessagePresentConnect;
import team.chisel.network.message.MessageSlotUpdate;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Chisel.MOD_ID);

	public static void init() {
		int id = 0;

		INSTANCE.registerMessage(MessageChiselSound.Handler.class, MessageChiselSound.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageSlotUpdate.Handler.class, MessageSlotUpdate.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageAutoChisel.Handler.class, MessageAutoChisel.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageChiselMode.Handler.class, MessageChiselMode.class, id++, Side.SERVER);
		INSTANCE.registerMessage(MessagePresentConnect.Handler.class, MessagePresentConnect.class, id++, Side.CLIENT);
	}
}