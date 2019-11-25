package team.chisel.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntityMP;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import team.chisel.Chisel;
import team.chisel.api.chunkdata.ChunkData;
import team.chisel.api.chunkdata.IChunkData;
import team.chisel.api.chunkdata.IChunkDataRegistry;

public enum PerChunkData implements IChunkDataRegistry {
    
    INSTANCE;

    @RequiredArgsConstructor
    public static class MessageChunkData {

        private final ChunkPos chunk;
        private final String key;
        private final @Nonnull CompoundNBT tag;

        public MessageChunkData(Chunk chunk, String key, @Nonnull CompoundNBT tag) {
            this(chunk.getPos(), key, tag);
        }
        
        public MessageChunkData(String key, IChunkData<?> iChunkData) {
            this((ChunkPos) null, key, new CompoundNBT());
            this.tag.put("l", iChunkData.writeToNBT());
        }

        public void encode(PacketBuffer buf) {
            if (chunk == null) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                buf.writeInt(chunk.x);
                buf.writeInt(chunk.z);
            }
            buf.writeString(key, 64);
            buf.writeCompoundTag(tag);
        }

        public static MessageChunkData decode(PacketBuffer buf) {
            ChunkPos chunk = null;
            if (buf.readBoolean()) {
                chunk = new ChunkPos(buf.readInt(), buf.readInt());
            }
            return new MessageChunkData(chunk, buf.readString(64), buf.readCompoundTag());
        }
        
        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
        
                Chunk chunk = null;
                if (this.chunk != null) {
                    chunk = Chisel.proxy.getClientWorld().getChunkFromChunkCoords(this.chunk.x, this.chunk.z);
                }
                IChunkData<?> data = INSTANCE.data.get(this.key);
                if (chunk != null) {
                    data.readFromNBT(chunk, this.tag);
                    int x = chunk.getPos().x << 4;
                    int z = chunk.getPos().z << 4;
                    Chisel.proxy.getClientWorld().markBlockRangeForRenderUpdate(x, 0, z, x, 255, z);
                } else {
                    for (ChunkPos pos : data.readFromNBT(this.tag.getList("l", Constants.NBT.TAG_COMPOUND))) {
                        Chisel.proxy.getClientWorld().markBlockRangeForRenderUpdate(pos.x, 0, pos.z, pos.x, 255, pos.z);
                    }
                }
            });
            
            ctx.get().setPacketHandled(true);
        }
    }

    /**
     * @param <T>
     *            MUST have a default constructor.
     */
    public static class ChunkDataBase<T extends NBTSaveable> implements IChunkData<T> {

        protected final Map<Pair<Integer, ChunkPos>, T> data = new HashMap<>();
        protected final Class<? extends T> clazz;
        private final boolean needsClientSync;

        public ChunkDataBase(Class<? extends T> clazz, boolean needsClientSync) {
            this.clazz = clazz;
            this.needsClientSync = needsClientSync;
        }
        
        @Override
        public ListNBT writeToNBT() {
            ListNBT tags = new ListNBT();
            for (Entry<Pair<Integer, ChunkPos>, T> e : data.entrySet()) {
                CompoundNBT entry = new CompoundNBT();
                entry.setInteger("d", e.getKey().getLeft());
                entry.setLong("p", (e.getKey().getRight().x << 32) | e.getKey().getRight().z);
                CompoundNBT data = new CompoundNBT();
                e.getValue().write(data);
                entry.setTag("v", data);
                tags.appendTag(entry);
            }
            return tags;
        }

        @Override
        public void writeToNBT(@Nonnull Chunk chunk, @Nonnull CompoundNBT tag) {
            T t = data.get(Pair.of(chunk.getWorld().provider.getDimension(), chunk.getPos()));
            if (t != null) {
                t.write(tag);
            }
        }

        @Override
        public Iterable<ChunkPos> readFromNBT(@Nonnull ListNBT tags) {
            List<ChunkPos> changed = new ArrayList<>();
            for (int i = 0; i < tags.tagCount(); i++) {
                CompoundNBT entry = tags.getCompoundTagAt(i);
                int dimID = entry.getInteger("d");
                long coordsRaw = entry.getLong("p");
                ChunkPos coords = new ChunkPos((int) ((coordsRaw >>> 32) & 0xFFFFFFFF), (int) (coordsRaw & 0xFFFFFFFF));
                if (readFromNBT(dimID, coords, entry.getCompoundTag("v"))) {
                    changed.add(coords);
                }
            }
            return changed;
        }

        @Override
        public void readFromNBT(@Nonnull Chunk chunk, @Nonnull CompoundNBT tag) {
            int dimID = chunk.getWorld().provider.getDimension();
            ChunkPos coords = chunk.getPos();
            readFromNBT(dimID, coords, tag);
        }
        
        private boolean readFromNBT(int dimID, ChunkPos coords, CompoundNBT tag) {
            if (tag.hasNoTags()) {
                data.remove(dimID, coords);
                return false;
            }
            T t = getOrCreateNew(dimID, coords);
            t.read(tag);
            return true;
        }
        
        protected T getOrCreateNew(int dimID, @Nonnull ChunkPos coords) {
            val pair = Pair.of(dimID, coords);
            T t = data.get(pair);
            if (t == null) {
                try {
                    t = clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not instantiate NBTSaveable " + clazz.getName() + "!", e);
                }
            }
            data.put(pair, t);
            return t;
        }

        @Override
        public boolean requiresClientSync() {
            return needsClientSync && !data.isEmpty();
        }

        @Override
        public T getDataForChunk(int dimID, @Nonnull ChunkPos coords) {
            return getOrCreateNew(dimID, coords);
        }
    }

    private PerChunkData() {
        ChunkData.setOffsetRegistry(this);
    }

    private Map<String, IChunkData<?>> data = Maps.newHashMap();

    public void registerChunkData(String key, IChunkData<?> cd) {
        data.put(key, cd);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IChunkData<?>> T getData(String key) {
        return (T) data.get(key);
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        for (Entry<String, IChunkData<?>> e : data.entrySet()) {
            CompoundNBT tag = new CompoundNBT();
            e.getValue().writeToNBT(event.getChunk(), tag);
            event.getData().setTag("chisel:" + e.getKey(), tag);
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        for (Entry<String, IChunkData<?>> e : data.entrySet()) {
            CompoundNBT tag = event.getData().getCompoundTag("chisel:" + e.getKey());
            e.getValue().readFromNBT(event.getChunk(), tag);
            updateClient(event.getChunk(), e.getKey(), e.getValue());
        }
    }
    
    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        for (Entry<String, IChunkData<?>> e : data.entrySet()) {
            if (e.getValue().requiresClientSync()) {
                Chisel.network.sendTo(new MessageChunkData(e.getKey(), e.getValue()), (PlayerEntityMP) event.player);
            }
        }
    }

    public void chunkModified(Chunk chunk, String key) {
        IChunkData<?> cd = data.get(key);
        chunk.setModified(true);
        updateClient(chunk, key, cd);
    }
    
    private void updateClient(@Nonnull Chunk chunk, String key, IChunkData<?> cd) {
        if (cd.requiresClientSync()) {
            CompoundNBT tag = new CompoundNBT();
            cd.writeToNBT(chunk, tag);
            PlayerChunkMapEntry entry = ((WorldServer)chunk.getWorld()).getPlayerChunkMap().getEntry(chunk.x, chunk.z);
            if (entry != null) {
                entry.sendPacket(Chisel.network.getPacketFrom(new MessageChunkData(chunk, key, tag)));
            }
        }
    }
}