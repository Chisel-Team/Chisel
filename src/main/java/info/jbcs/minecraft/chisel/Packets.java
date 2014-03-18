package info.jbcs.minecraft.chisel;

import java.io.IOException;

import info.jbcs.minecraft.utilities.General;
import pl.asie.lib.network.MessageHandlerBase;
import pl.asie.lib.network.Packet;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class Packets extends MessageHandlerBase {
	public static final int CHISELED = 1;
	
	public void onChiseled(Packet stream, EntityPlayer player) throws IOException {
		final int x=stream.readInt();
		final int y=stream.readInt();
		final int z=stream.readInt();
		
		switch(FMLCommonHandler.instance().getEffectiveSide()){
			case SERVER:
				Packet packet = Chisel.packet.create(CHISELED).writeInt(x).writeInt(y).writeInt(z);
				Chisel.packet.sendToAllAround(packet, new TargetPoint(player.dimension, x, y, z, 30.0f));
				break;
			case CLIENT:
				Block block =player.worldObj.getBlock(x,y,z);
				int blockMeta=player.worldObj.getBlockMetadata(x,y,z);
				GeneralChiselClient.spawnChiselEffect(x, y, z, Carving.chisel.getVariationSound(block, blockMeta));
	
				break;
			default:
				break;
		}

	}

	@Override
	public void onMessage(Packet packet, INetHandler handler,
			EntityPlayer player, int command) throws IOException {
		switch(command) {
			case CHISELED:
				onChiseled(packet, player);
				return;
		}
	}

}
