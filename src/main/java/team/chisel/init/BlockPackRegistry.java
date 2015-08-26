package team.chisel.init;

import java.util.List;
import java.util.Map;
import java.util.Set;

import team.chisel.api.blockpack.BlockPack;
import team.chisel.api.blockpack.IBlockPack;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public enum BlockPackRegistry {
	INSTANCE;

	private class BlockPackKey {

		IBlockPack pack;
		String name;

		private BlockPackKey(IBlockPack pack, String name) {
			this.pack = pack;
			this.name = name;
		}
	}

	private class BlockPackData {

		private ASMData data;
		private String name;
		private List<String> modDeps, blockPackDeps;

		@SuppressWarnings("unchecked")
		private BlockPackData(ASMData data) {
			this.data = data;
			this.name = (String) data.getAnnotationInfo().get("value");
			this.modDeps = (List<String>) data.getAnnotationInfo().get("modDeps");
			this.blockPackDeps = (List<String>) data.getAnnotationInfo().get("blockPackDeps");
			
			if (this.modDeps == null) {
				this.modDeps = Lists.newArrayList();
			}
			if (this.blockPackDeps == null) {
				this.blockPackDeps = Lists.newArrayList();
			}
		}
	}

	private Set<ASMData> annotations;
	private Set<BlockPackKey> blockPacks = Sets.newHashSet();

	public void preInit(FMLPreInitializationEvent event) {
		annotations = event.getAsmData().getAll(BlockPack.class.getName());
		register();
		for (BlockPackKey key : blockPacks) {
			key.pack.preInit(event);
		}
	}
	
	public void init(FMLInitializationEvent event) {
		for (BlockPackKey key : blockPacks) {
			key.pack.init(event);
		}
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		for (BlockPackKey key : blockPacks) {
			key.pack.postInit(event);
		}
	}

	private boolean registered = false;

	/**
	 * For internal use only. Do not call. Callers will be sacked.
	 */
	private void register() {
		if (registered) {
			throw new IllegalStateException("I warned you!");
		}

		Map<String, BlockPackData> loaded = Maps.newHashMap();
		for (ASMData data : annotations) {
			BlockPackData info = new BlockPackData(data);
			loaded.put(info.name, info);
		}

		Set<String> blacklist = Sets.newHashSet();
		for (BlockPackData data : loaded.values()) {
			for (String s : data.modDeps) {
				if (!Loader.isModLoaded(s)) {
					blacklist.add(data.name);
				}
			}
			for (String s : data.blockPackDeps) {
				BlockPackData dep = loaded.get(s);
				if (dep == null || blacklist.contains(dep)) {
					blacklist.add(data.name);
				}
			}
		}

		for (String s : blacklist) {
			loaded.remove(s);
		}

		for (BlockPackData pack : loaded.values()) {
			registerBlockPack(pack);
		}

		registered = true;
	}

	@SuppressWarnings("unchecked")
	private void registerBlockPack(BlockPackData pack) {
		try {
			Class<? extends IBlockPack> clazz = (Class<? extends IBlockPack>) Class.forName(pack.data.getClassName());
			IBlockPack inst = clazz.newInstance();
			blockPacks.add(new BlockPackKey(inst, pack.name));
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}
}
