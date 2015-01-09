package com.cricketcraft.chisel;

import com.cricketcraft.chisel.item.chisel.ChiselController;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Chisel.MOD_ID);

	public static void init() {
		INSTANCE.registerMessage(ChiselController.PlaySoundHandler.class, ChiselController.PacketPlaySound.class, 0, Side.CLIENT);
	}
}