package team.chisel.client.render.ctx;

import team.chisel.api.render.IBlockRenderContext;
import team.chisel.common.util.EnumConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Block Render Context for CTM
 */
public class CTMBlockRenderContext implements IBlockRenderContext {

    private List<EnumConnection> connections;

    public CTMBlockRenderContext(List<EnumConnection> connections){
        this.connections = connections;
    }

    public CTMBlockRenderContext(){
        this(new ArrayList<EnumConnection>());
    }

    public CTMBlockRenderContext(EnumConnection... connections){
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
