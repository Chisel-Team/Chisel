package team.chisel.compat.fmp;

import team.chisel.init.ChiselBlocks;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.multipart.minecraft.TorchPart;

public class PartChiselTorch extends TorchPart {

	private int idx;

	public PartChiselTorch() {
	}

	public PartChiselTorch(int idx, int meta) {
		super(meta);
		this.idx = idx;
	}

	@Override
	public void writeDesc(MCDataOutput packet) {
		super.writeDesc(packet);
		packet.writeInt(idx);
	}

	@Override
	public void readDesc(MCDataInput packet) {
		super.readDesc(packet);
		this.idx = packet.readInt();
	}

	@Override
	public void save(NBTTagCompound tag) {
		super.save(tag);
		tag.setInteger("idx", idx);
	}

	@Override
	public void load(NBTTagCompound tag) {
		super.load(tag);
		this.idx = tag.getInteger("idx");
	}

	@Override
	public Block getBlock() {
		return ChiselBlocks.torches[idx];
	}

	@Override
	public String getType() {
		return "chisel_torch";
	}
}
