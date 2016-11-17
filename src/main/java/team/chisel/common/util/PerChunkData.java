package team.chisel.common.util;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.google.common.base.Throwables;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
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
            this.chunk = chunk.getChunkCoordIntPair();
            this.key = key;
            this.tag = tag;
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(chunk.chunkXPos);
            buf.writeInt(chunk.chunkZPos);
            ByteBufUtils.writeUTF8String(buf, key);
            ByteBufUtils.writeTag(buf, tag);
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.chunk = new ChunkPos(buf.readInt(), buf.readInt());
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
                    Chunk chunk = Chisel.proxy.getClientWorld().getChunkFromChunkCoords(message.chunk.chunkXPos, message.chunk.chunkZPos);
                    IChunkData<?> data = INSTANCE.data.get(message.key);
                    data.readFromNBT(chunk, message.tag);
                    int x = chunk.xPosition << 4;
                    int z = chunk.zPosition << 4;
                    Chisel.proxy.getClientWorld().markBlockRangeForRenderUpdate(x, 0, z, x, 255, z);
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

        protected final Table<Integer, ChunkPos, T> data = HashBasedTable.create();
        protected final Class<? extends T> clazz;
        private final boolean needsClientSync;

        public ChunkDataBase(Class<? extends T> clazz, boolean needsClientSync) {
            this.clazz = clazz;
            this.needsClientSync = needsClientSync;
        }

        @Override
        public void writeToNBT(@Nonnull Chunk chunk, @Nonnull NBTTagCompound tag) {
            T t = data.get(chunk.getWorld().provider.getDimension(), chunk.getChunkCoordIntPair());
            if (t != null) {
                t.write(tag);
            }
        }

        @Override
        public void readFromNBT(@Nonnull Chunk chunk, @Nonnull NBTTagCompound tag) {
            int dimID = chunk.getWorld().provider.getDimension();
            ChunkPos coords = chunk.getChunkCoordIntPair();
            if (tag.hasNoTags()) {
                data.remove(dimID, coords);
                return;
            }
            T t = getOrCreateNew(dimID, coords);
            t.read(tag);
        }
        
        @SuppressWarnings("null")
        protected T getOrCreateNew(int dimID, @Nonnull ChunkPos coords) {
            T t = data.get(dimID, coords);
            if (t == null) {
                try {
                    t = clazz.newInstance();
                } catch (Exception e) {
                    Chisel.logger.error("Could not instantiate NBTSaveable " + clazz.getName() + "!");
                    Throwables.propagate(e);
                }
            }
            data.put(dimID, coords, t);
            return t;
        }

        @Override
        public boolean requiresClientSync() {
            return needsClientSync;
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

    public void chunkModified(Chunk chunk, String key) {
        IChunkData<?> cd = data.get(key);
        chunk.setModified(true);
        updateClient(chunk, key, cd);
    }
    
    private void updateClient(@Nonnull Chunk chunk, String key, IChunkData<?> cd) {
        if (cd.requiresClientSync()) {
            NBTTagCompound tag = new NBTTagCompound();
            cd.writeToNBT(chunk, tag);
            PlayerChunkMapEntry entry = ((WorldServer)chunk.getWorld()).getPlayerChunkMap().getEntry(chunk.xPosition, chunk.zPosition);
            if (entry != null) {
                entry.sendPacket(Chisel.network.getPacketFrom(new MessageChunkData(chunk, key, tag)));
            }
        }
    }
}