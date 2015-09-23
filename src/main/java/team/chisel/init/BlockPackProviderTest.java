//package team.chisel.init;
//
//import cpw.mods.fml.common.event.FMLInitializationEvent;
//import cpw.mods.fml.common.event.FMLPostInitializationEvent;
//import cpw.mods.fml.common.event.FMLPreInitializationEvent;
//import team.chisel.Chisel;
//import team.chisel.api.blockpack.BlockPackProvider;
//import team.chisel.api.blockpack.IBlockPackProvider;
//import team.chisel.api.blockpack.IProvidedBlockPack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@BlockPackProvider(value = "testBlockPack", owner = Chisel.MOD_ID)
//public class BlockPackProviderTest implements IBlockPackProvider{
//
//    public List<IProvidedBlockPack> getProvidedPacks(FMLPreInitializationEvent event){
//        Chisel.logger.info("Providing Block pack!");
//        List<IProvidedBlockPack> providedList = new ArrayList<IProvidedBlockPack>();
//        providedList.add(new ProvidedPack("pack"+System.currentTimeMillis()));
//        return providedList;
//    }
//
//    public static class ProvidedPack implements IProvidedBlockPack {
//
//        private String name;
//
//
//        public ProvidedPack(String name){
//            this.name = name;
//        }
//
//
//        public String getName(){
//            return this.name;
//        }
//
//        public void preInit(FMLPreInitializationEvent event){
//            Chisel.logger.info(name + "preInit");
//        }
//
//        public void init(FMLInitializationEvent event){
//            Chisel.logger.info(name + "init");
//        }
//
//        public void postInit(FMLPostInitializationEvent event){
//            Chisel.logger.info(name + "postInit");
//        }
//    }
//
//}
