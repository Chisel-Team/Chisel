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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import team.chisel.Chisel;
import team.chisel.api.chunkdata.ChunkData;
import team.chisel.api.chunkdata.IChunkData;
import team.chisel.api.chunkdata.IChunkDataRegistry;
import team.chisel.client.ClientProxy;

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
                    chunk = ClientProxy.getClientWorld().getChunk(this.chunk.x, this.chunk.z);
                }
                IChunkData<?> data = INSTANCE.data.get(this.key);
                if (chunk != null) {
                    data.readFromNBT(chunk, this.tag);
                    int x = chunk.getPos().x << 4;
                    int z = chunk.getPos().z << 4;
                    // TODO 1.14 ClientProxy.getClientWorld().markBlockRangeForRenderUpdate(x, 0, z, x, 255, z);
                } else {
                    for (ChunkPos pos : data.readFromNBT(this.tag.getList("l", Constants.NBT.TAG_COMPOUND))) {
                        // TODO 1.14 ClientProxy.getClientWorld().markBlockRangeForRenderUpdate(pos.x, 0, pos.z, pos.x, 255, pos.z);
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

        protected final Map<Pair<DimensionType, ChunkPos>, T> data = new HashMap<>();
        protected final Class<? extends T> clazz;
        private final boolean needsClientSync;

        public ChunkDataBase(Class<? extends T> clazz, boolean needsClientSync) {
            this.clazz = clazz;
            this.needsClientSync = needsClientSync;
        }
        
        @Override
        public ListNBT writeToNBT() {
            ListNBT tags = new ListNBT();
            for (Entry<Pair<DimensionType, ChunkPos>, T> e : data.entrySet()) {
                CompoundNBT entry = new CompoundNBT();
                entry.putString("d", e.getKey().getLeft().getRegistryName().toString());
                entry.putLong("p", (e.getKey().getRight().x << 32) | e.getKey().getRight().z);
                CompoundNBT data = new CompoundNBT();
                e.getValue().write(data);
                entry.put("v", data);
                tags.add(entry);
            }
            return tags;
        }

        @Override
        public void writeToNBT(@Nonnull IChunk chunk, @Nonnull CompoundNBT tag) {
            T t = data.get(Pair.of(chunk.getWorldForge().getDimension().getType(), chunk.getPos()));
            if (t != null) {
                t.write(tag);
            }
        }

        @Override
        public Iterable<ChunkPos> readFromNBT(@Nonnull ListNBT tags) {
            List<ChunkPos> changed = new ArrayList<>();
            for (int i = 0; i < tags.size(); i++) {
                CompoundNBT entry = tags.getCompound(i);
                DimensionType dim = DimensionType.byName(new ResourceLocation(entry.getString("d")));
                long coordsRaw = entry.getLong("p");
                ChunkPos coords = new ChunkPos((int) ((coordsRaw >>> 32) & 0xFFFFFFFF), (int) (coordsRaw & 0xFFFFFFFF));
                if (readFromNBT(dim, coords, entry.getCompound("v"))) {
                    changed.add(coords);
                }
            }
            return changed;
        }

        @Override
        public void readFromNBT(@Nonnull IChunk chunk, @Nonnull CompoundNBT tag) {
            DimensionType type = chunk.getWorldForge().getDimension().getType();
            ChunkPos coords = chunk.getPos();
            readFromNBT(type, coords, tag);
        }
        
        private boolean readFromNBT(DimensionType dim, ChunkPos coords, CompoundNBT tag) {
            if (tag.isEmpty()) {
                data.remove(dim, coords);
                return false;
            }
            T t = getOrCreateNew(dim, coords);
            t.read(tag);
            return true;
        }
        
        protected T getOrCreateNew(DimensionType dim, @Nonnull ChunkPos coords) {
            val pair = Pair.of(dim, coords);
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
        public T getDataForChunk(DimensionType dim, @Nonnull ChunkPos coords) {
            return getOrCreateNew(dim, coords);
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
            event.getData().put("chisel:" + e.getKey(), tag);
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        for (Entry<String, IChunkData<?>> e : data.entrySet()) {
            CompoundNBT tag = event.getData().getCompound("chisel:" + e.getKey());
            IChunk chunk = event.getChunk();
            e.getValue().readFromNBT(chunk, tag);
            if (chunk instanceof Chunk) {
                updateClient((Chunk) chunk, e.getKey(), e.getValue());
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        for (Entry<String, IChunkData<?>> e : data.entrySet()) {
            if (e.getValue().requiresClientSync()) {
                Chisel.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new MessageChunkData(e.getKey(), e.getValue()));
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
            Chisel.network.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new MessageChunkData(chunk, key, tag));
        }
    }
}