package team.chisel.common.util;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;
import team.chisel.Chisel;
import team.chisel.api.chunkdata.ChunkData;
import team.chisel.api.chunkdata.IChunkData;
import team.chisel.api.chunkdata.IChunkDataRegistry;
import team.chisel.client.ClientProxy;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

public enum PerChunkData implements IChunkDataRegistry {

    INSTANCE;

    private final Map<String, IChunkData<?>> data = Maps.newHashMap();

    PerChunkData() {
        ChunkData.setOffsetRegistry(this);
    }

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
            CompoundTag tag = new CompoundTag();
            e.getValue().writeToNBT(event.getChunk(), tag);
            event.getData().put("chisel:" + e.getKey(), tag);
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        for (Entry<String, IChunkData<?>> e : data.entrySet()) {
            CompoundTag tag = event.getData().getCompound("chisel:" + e.getKey());
            ChunkAccess chunk = event.getChunk();
            e.getValue().readFromNBT(chunk, tag);
            if (chunk instanceof LevelChunk) {
                updateClient((LevelChunk) chunk, e.getKey(), e.getValue());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        for (Entry<String, IChunkData<?>> e : data.entrySet()) {
            if (e.getValue().requiresClientSync()) {
                Chisel.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getPlayer()), new MessageChunkData(e.getKey(), e.getValue()));
            }
        }
    }

    public void chunkModified(LevelChunk chunk, String key) {
        IChunkData<?> cd = data.get(key);
        chunk.setUnsaved(true);
        updateClient(chunk, key, cd);
    }

    private void updateClient(@Nonnull LevelChunk chunk, String key, IChunkData<?> cd) {
        if (cd.requiresClientSync()) {
            CompoundTag tag = new CompoundTag();
            cd.writeToNBT(chunk, tag);
            Chisel.network.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new MessageChunkData(chunk, key, tag));
        }
    }

    @RequiredArgsConstructor
    public static class MessageChunkData {

        private final ChunkPos chunk;
        private final String key;
        private final @Nonnull CompoundTag tag;

        public MessageChunkData(LevelChunk chunk, String key, @Nonnull CompoundTag tag) {
            this(chunk.getPos(), key, tag);
        }

        public MessageChunkData(String key, IChunkData<?> iChunkData) {
            this((ChunkPos) null, key, new CompoundTag());
            this.tag.put("l", iChunkData.writeToNBT());
        }

        public static MessageChunkData decode(FriendlyByteBuf buf) {
            ChunkPos chunk = null;
            if (buf.readBoolean()) {
                chunk = new ChunkPos(buf.readInt(), buf.readInt());
            }
            return new MessageChunkData(chunk, buf.readUtf(64), Objects.requireNonNull(buf.readNbt()));
        }

        public void encode(FriendlyByteBuf buf) {
            if (chunk == null) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                buf.writeInt(chunk.x);
                buf.writeInt(chunk.z);
            }
            buf.writeUtf(key, 64);
            buf.writeNbt(tag);
        }

        public void handle(Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {

                LevelChunk chunk = null;
                if (this.chunk != null) {
                    chunk = ClientProxy.getClientWorld().getChunk(this.chunk.x, this.chunk.z);
                }
                IChunkData<?> data = INSTANCE.data.get(this.key);
                if (chunk != null) {
                    data.readFromNBT(chunk, this.tag);
                    int x = chunk.getPos().x << 4;
                    int z = chunk.getPos().z << 4;
                    ClientProxy.getWorldRenderer().setBlocksDirty(x, 0, z, x, 255, z);
                } else {
                    for (ChunkPos pos : data.readFromNBT(this.tag.getList("l", Tag.TAG_COMPOUND))) {
                        ClientProxy.getWorldRenderer().setBlocksDirty(pos.x, 0, pos.z, pos.x, 255, pos.z);
                    }
                }
            });

            ctx.get().setPacketHandled(true);
        }
    }

    /**
     * @param <T> MUST have a default constructor.
     */
    public static class ChunkDataBase<T extends NBTSaveable> implements IChunkData<T> {

        protected final Map<Pair<ResourceKey<Level>, ChunkPos>, T> data = new HashMap<>();
        protected final Class<? extends T> clazz;
        private final boolean needsClientSync;

        public ChunkDataBase(Class<? extends T> clazz, boolean needsClientSync) {
            this.clazz = clazz;
            this.needsClientSync = needsClientSync;
        }

        @Override
        public ListTag writeToNBT() {
            ListTag tags = new ListTag();
            for (Entry<Pair<ResourceKey<Level>, ChunkPos>, T> e : data.entrySet()) {
                CompoundTag entry = new CompoundTag();
                entry.putString("d", e.getKey().getLeft().location().toString());
                entry.putLong("p", ((long) e.getKey().getRight().x << 32) | e.getKey().getRight().z);
                CompoundTag data = new CompoundTag();
                e.getValue().write(data);
                entry.put("v", data);
                tags.add(entry);
            }
            return tags;
        }

        @Override
        public void writeToNBT(@Nonnull ChunkAccess chunk, @Nonnull CompoundTag tag) {
            T t = data.get(Pair.of(((Level) Objects.requireNonNull(chunk.getWorldForge())).dimension(), chunk.getPos()));
            if (t != null) {
                t.write(tag);
            }
        }

        @Override
        public Iterable<ChunkPos> readFromNBT(@Nonnull ListTag tags) {
            List<ChunkPos> changed = new ArrayList<>();
            for (int i = 0; i < tags.size(); i++) {
                CompoundTag entry = tags.getCompound(i);
                ResourceKey<Level> dim = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(entry.getString("d")));
                long coordsRaw = entry.getLong("p");
                ChunkPos coords = new ChunkPos((int) ((coordsRaw >>> 32)), (int) (coordsRaw));
                if (readFromNBT(dim, coords, entry.getCompound("v"))) {
                    changed.add(coords);
                }
            }
            return changed;
        }

        @Override
        public void readFromNBT(@Nonnull ChunkAccess chunk, @Nonnull CompoundTag tag) {
            ResourceKey<Level> type = ((Level) Objects.requireNonNull(chunk.getWorldForge())).dimension();
            ChunkPos coords = chunk.getPos();
            readFromNBT(type, coords, tag);
        }

        @SuppressWarnings("SuspiciousMethodCalls")
        private boolean readFromNBT(ResourceKey<Level> dim, ChunkPos coords, CompoundTag tag) {
            if (tag.isEmpty()) {
                data.remove(dim, coords);
                return false;
            }
            T t = getOrCreateNew(dim, coords);
            t.read(tag);
            return true;
        }

        @SuppressWarnings("deprecation")
        protected T getOrCreateNew(ResourceKey<Level> dim, @Nonnull ChunkPos coords) {
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
        public T getDataForChunk(ResourceKey<Level> dim, @Nonnull ChunkPos coords) {
            return getOrCreateNew(dim, coords);
        }
    }
}