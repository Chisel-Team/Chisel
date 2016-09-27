package team.chisel.client.core;

import net.minecraftforge.common.property.IUnlistedProperty;

public enum PropertyWorldContext implements IUnlistedProperty<WorldContext> {

    INSTANCE;

    @Override
    public String getName(){
        return "World Context";
    }

    @Override
    public boolean isValid(WorldContext value){
        return true;
    }

    @Override
    public Class<WorldContext> getType(){
        return WorldContext.class;
    }

    @Override
    public String valueToString(WorldContext value){
        return value.toString();
    }

}
