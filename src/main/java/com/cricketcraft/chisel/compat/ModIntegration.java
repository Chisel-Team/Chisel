package com.cricketcraft.chisel.compat;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.Loader;

public class ModIntegration {

    private static List<IModIntegration> integratedMods = new ArrayList<IModIntegration>();

    public static void addMod(Class<? extends IModIntegration> cls){
        try{
            IModIntegration obj = cls.newInstance();

            if(Loader.isModLoaded(obj.getModId())){
                integratedMods.add(obj);
            }
        } catch(Throwable throwable){
            throwable.printStackTrace();
        }
    }

    public static void init(){
        try{
            for(IModIntegration integration : integratedMods){
                integration.onInit();
            }
        } catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    public static void postInit(){
        try {
            for (IModIntegration integration : integratedMods){
                integration.onPostInit();
            }
        } catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    static interface IModIntegration{
        String getModId();
        void onInit();
        void onPostInit();
    }
}
