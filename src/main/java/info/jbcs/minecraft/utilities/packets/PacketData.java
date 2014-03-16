package info.jbcs.minecraft.utilities.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class PacketData {
	ByteArrayOutputStream s = new ByteArrayOutputStream();
	byte packetIndex;

	public abstract void data(DataOutputStream stream) throws IOException;

	ByteArrayOutputStream getData() {
		DataOutputStream stream = new DataOutputStream(s);

		try {
			stream.writeByte(packetIndex);
			data(stream);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return s;
	}
}
