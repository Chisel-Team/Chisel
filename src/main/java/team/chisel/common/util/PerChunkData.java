package team.chisel.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;

import io.netty.buffer.ByteBuf;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.chisel.Chisel;
import team.chisel.api.chunkdata.ChunkData;
import team.chisel.api.chunkdata.IChunkData;
import team.chisel.api.chunkdata.IChunkDataRegistry;

public enum PerChunkData implements IChunkDataRegistry {
    
    INSTANCE;

    public static class MessageChunkData implements IMessage {

        private ChunkPos chunk;
        private String key;
        private @Nonnull NBTTagCompound tag;

        @SuppressWarnings("null")
        public MessageChunkData() {
        }

        public MessageChunkData(Chunk chunk, String key, @Nonnull NBTTagCompound tag) {
            this.chunk = chunk.getPos();
            this.key = key;
            this.tag = tag;
        }
        
        public MessageChunkData(String key, IChunkData<?> iChunkData) {
            this.chunk = null;
            this.key = key;
            this.tag = new NBTTagCompound();
            this.tag.setTag("l", iChunkData.writeToNBT());
        }

        @Override
        public void toBytes(ByteBuf buf) {
            if (chunk == null) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                buf.writeInt(chunk.x);
                buf.writeInt(chunk.z);
            }
            ByteBufUtils.writeUTF8String(buf, key);
            ByteBufUtils.writeTag(buf, tag);
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            if (buf.readBoolean()) {
                this.chunk = new ChunkPos(buf.readInt(), buf.readInt());
            }
            this.key = ByteBufUtils.readUTF8String(buf);
            this.tag = ByteBufUtils.readTag(buf);
        }
    }

    public static class MessageChunkDataHandler implements IMessageHandler<MessageChunkData, IMessage> {

        @Override
        public IMessage onMessage(MessageChunkData message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                
                @Override
                public void run() {
                    Chunk chunk = null;
                    if (message.chunk != null) {
                        chunk = Chisel.proxy.getClientWorld().getChunkFromChunkCoords(message.chunk.x, message.chunk.z);
                    }
                    IChunkData<?> data = INSTANCE.data.get(message.key);
                    if (chunk != null) {
                        data.readFromNBT(chunk, message.tag);
                        int x = chunk.x << 4;
                        int z = chunk.z << 4;
                        Chisel.proxy.getClientWorld().markBlockRangeForRenderUpdate(x, 0, z, x, 255, z);
                    } else {
                        for (ChunkPos pos : data.readFromNBT(message.tag.getTagList("l", Constants.NBT.TAG_COMPOUND))) {
                            Chisel.proxy.getClientWorld().markBlockRangeForRenderUpdate(pos.x, 0, pos.z, pos.x, 255, pos.z);
                        }
                    }
                }
            });
            
            return null;
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
        public NBTTagList writeToNBT() {
            NBTTagList tags = new NBTTagList();
            for (Entry<Pair<Integer, ChunkPos>, T> e : data.entrySet()) {
                NBTTagCompound entry = new NBTTagCompound();
                entry.setInteger("d", e.getKey().getLeft());
                entry.setLong("p", (e.getKey().getRight().x << 32) | e.getKey().getRight().z);
                NBTTagCompound data = new NBTTagCompound();
                e.getValue().write(data);
                entry.setTag("v", data);
                tags.appendTag(entry);
            }
            return tags;
        }

        @Override
        public void writeToNBT(@Nonnull Chunk chunk, @Nonnull NBTTagCompound tag) {
            T t = data.get(Pair.of(chunk.getWorld().provider.getDimension(), chunk.getPos()));
            if (t != null) {
                t.write(tag);
            }
        }

        @Override
        public Iterable<ChunkPos> readFromNBT(@Nonnull NBTTagList tags) {
            List<ChunkPos> changed = new ArrayList<>();
            for (int i = 0; i < tags.tagCount(); i++) {
                NBTTagCompound entry = tags.getCompoundTagAt(i);
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
        public void readFromNBT(@Nonnull Chunk chunk, @Nonnull NBTTagCompound tag) {
            int dimID = chunk.getWorld().provider.getDimension();
            ChunkPos coords = chunk.getPos();
            readFromNBT(dimID, coords, tag);
        }
        
        private boolean readFromNBT(int dimID, ChunkPos coords, NBTTagCompound tag) {
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
            NBTTagCompound tag = new NBTTagCompound();
            e.getValue().writeToNBT(event.getChunk(), tag);
            event.getData().setTag("chisel:" + e.getKey(), tag);
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        for (Entry<String, IChunkData<?>> e : data.entrySet()) {
            NBTTagCompound tag = event.getData().getCompoundTag("chisel:" + e.getKey());
            e.getValue().readFromNBT(event.getChunk(), tag);
            updateClient(event.getChunk(), e.getKey(), e.getValue());
        }
    }
    
    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        for (Entry<String, IChunkData<?>> e : data.entrySet()) {
            if (e.getValue().requiresClientSync()) {
                Chisel.network.sendTo(new MessageChunkData(e.getKey(), e.getValue()), (EntityPlayerMP) event.player);
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
            NBTTagCompound tag = new NBTTagCompound();
            cd.writeToNBT(chunk, tag);
            PlayerChunkMapEntry entry = ((WorldServer)chunk.getWorld()).getPlayerChunkMap().getEntry(chunk.x, chunk.z);
            if (entry != null) {
                entry.sendPacket(Chisel.network.getPacketFrom(new MessageChunkData(chunk, key, tag)));
            }
        }
    }
}