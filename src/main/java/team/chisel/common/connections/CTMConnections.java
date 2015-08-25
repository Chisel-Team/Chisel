package team.chisel.common.connections;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;
import scala.actors.threadpool.Arrays;
import team.chisel.client.render.BlockResources;
import team.chisel.client.render.IBlockResources;
import team.chisel.common.util.SubBlockUtil;
import team.chisel.common.variation.Variation;

import java.util.ArrayList;
import java.util.List;

/**
 * List of all the connections a ctm block has
 */
public class CTMConnections {

    private List<EnumConnection> connections;

    public CTMConnections(List<EnumConnection> connections){
        this.connections = connections;
    }

    public CTMConnections(){
        this(new ArrayList<EnumConnection>());
    }

    public CTMConnections(EnumConnection... connections){
        this(Arrays.asList(connections));
    }

    public boolean isConnected(EnumConnection connection){
        return connections.contains(connection);
    }

    public void addConnection(EnumConnection connection){
        if (!connections.contains(connection)){
            connections.add(connection);
        }
    }

    public void removeConnection(EnumConnection connection){
        if (connections.contains(connection)){
            connections.remove(connection);
        }
    }

    public String toString(){
        String out = "{";
        for (EnumConnection connection : connections){
            out = out +","+connection;
        }
        return out+"}";
    }



}
