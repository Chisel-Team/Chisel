package team.chisel.common.util;

import net.minecraftforge.common.property.IUnlistedProperty;
import team.chisel.api.render.RenderContextList;

/**
 * Block Property for the render context list
 */
public class PropertyRenderContextList implements IUnlistedProperty<RenderContextList> {

    @Override
    public String getName(){
        return "connections";
    }

    @Override
    public boolean isValid(RenderContextList value){
        return true;
    }

    @Override
    public Class<RenderContextList> getType(){
        return RenderContextList.class;
    }

    @Override
    public String valueToString(RenderContextList value){
        return value.toString();
    }
}
