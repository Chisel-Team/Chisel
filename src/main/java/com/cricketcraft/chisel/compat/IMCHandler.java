package com.cricketcraft.chisel.compat;

import net.minecraft.block.Block;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.IMC;
import com.cricketcraft.chisel.api.carving.ICarvingRegistry;
import com.cricketcraft.chisel.carving.Carving;

import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

public class IMCHandler {

	public static final IMCHandler INSTANCE = new IMCHandler();

	private int order = 1000;
	
	private IMCHandler() {
	}

	public void handleMessage(IMCMessage message) {
		for (IMC imc : IMC.values()) {
			if (imc.key.equals(message.key)) {
				handle(message, imc, message.getStringValue());
			}
		}
	}

	private void handle(IMCMessage message, IMC type, String value) {
		ICarvingRegistry reg = Carving.chisel;
		String[] data = value.split("\\|");
		try {
			switch (type) {
			case ADD_VARIATION:
			case REMOVE_VARIATION:
				Block block = (Block) Block.blockRegistry.getObject(data[1]);
				int meta = Short.parseShort(data[2]);
				if (type == IMC.ADD_VARIATION) {
					reg.addVariation(data[0], block, meta, order++);
				} else {
					reg.removeVariation(block, meta, data[0]);
				}
			case REGISTER_GROUP_ORE:
				reg.registerOre(data[0], data[1]);
			default:
				throw new IllegalArgumentException("Invalid IMC constant! How...what...?");
			}
		} catch (Exception e) {
			Chisel.logger.error("Could not handle data {} for IMC type {}. This was sent from mod {}.\n"
					+ "!! This is a bug in that mod !!\nSwallowing error and continuing...",
					value, type.name(), message.getSender());
			e.printStackTrace();
		}
	}
}
