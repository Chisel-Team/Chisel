package info.jbcs.minecraft.utilities.packets;

import info.jbcs.minecraft.utilities.General;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public abstract class PacketHandler implements Comparable {
	/*static ArrayList<PacketHandler> items = new ArrayList<PacketHandler>();

	public static final String channel = "AUTO:Multi";
	public static final String channelDummy = "AUTO:-";

	int index;
	Object mod;
	String name;

	public PacketHandler(String n) {
		items.add(this);
		name = n;
	}

	public void create() {
	}

	@Override
	public int compareTo(Object a) {
		return name.compareTo(((PacketHandler) a).name);
	}

	public void sendToPlayer(EntityPlayerMP player,PacketData data) {
		if (FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER)
			return;
		

		data.packetIndex = (byte) index;
		ByteArrayOutputStream stream = data.getData();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = channel;
		packet.data = stream.toByteArray();
		packet.length = stream.size();
		player.playerNetServerHandler.sendPacketToPlayer(packet);
	}

	public void sendToPlayers(double posX,double posY,double posZ,int dimension,double distance,PacketData data) {
		ServerConfigurationManager mgr = MinecraftServer.getServer().getConfigurationManager();
		for (int j = 0; j < mgr.playerEntityList.size(); ++j) {
			EntityPlayerMP p = (EntityPlayerMP) mgr.playerEntityList.get(j);

			if (p.dimension != dimension) continue;
			if (! General.isInRange(distance, p.posX, p.posY, p.posZ, posX, posY, posZ)) continue;

			sendToPlayer(p,data);
		}
	}

	public void sendToServer(PacketData data) {
		if (FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT)
			return;

		data.packetIndex = (byte) index;
		ByteArrayOutputStream stream = data.getData();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = channel;
		packet.data = stream.toByteArray();
		packet.length = stream.size();
		PacketHandlerClient.send(packet);
	}

	public static void register(Object mod) {
		Collections.sort(items);
		int index = 0;

		for (PacketHandler h : items) {
			if(h.mod==null)
				h.mod = mod;
			h.index = index++;
		}
	}

	public static void onPacketData(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer player) {
		if (!packet.channel.equals(channel))
			return;
		packet.channel=channelDummy;
		

		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

		try {
			byte index = inputStream.readByte();

			if (index >= items.size()) {
				return;
			}

			items.get(index).onData(inputStream, player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void onData(DataInputStream stream, EntityPlayer player) throws IOException;
*/
}
