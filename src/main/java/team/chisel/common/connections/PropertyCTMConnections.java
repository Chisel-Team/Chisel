package team.chisel.common.connections;

import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * The Property for ctm connections
 *
 * @author minecreatr
 */
public class PropertyCTMConnections implements IUnlistedProperty<CTMConnections>{

    public String getName(){
        return "connections";
    }

    public boolean isValid(CTMConnections value){
        return true;
    }

    public Class<CTMConnections> getType(){
        return CTMConnections.class;
    }

    public String valueToString(CTMConnections value){
        return value.toString();
    }
}
