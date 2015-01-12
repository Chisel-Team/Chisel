package com.cricketcraft.chisel.network;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.network.message.MessageAutoChisel;
import com.cricketcraft.chisel.network.message.MessageChiselSound;
import com.cricketcraft.chisel.network.message.MessageSlotUpdate;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Chisel.MOD_ID);

	public static void init() {
		int id = 0;

		INSTANCE.registerMessage(MessageChiselSound.Handler.class, MessageChiselSound.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageSlotUpdate.Handler.class, MessageSlotUpdate.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageAutoChisel.Handler.class, MessageAutoChisel.class, id++, Side.CLIENT);
	}
}