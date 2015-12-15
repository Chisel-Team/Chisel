//package team.chisel.common.init;
//
//import net.minecraft.util.ResourceLocation;
//import team.chisel.api.render.IChiselTexture;
//import team.chisel.common.util.PossibleType;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Stores all registered textures
// */
//public class TextureRegistry {
//
//    private static Map<ResourceLocation, IChiselTexture> texMap = new HashMap<ResourceLocation, IChiselTexture>();
//
//    private static Map<ResourceLocation, CombinedChiselTexture> combinedMap = new HashMap<ResourceLocation, CombinedChiselTexture>();
//
//
//    public static void registerTexture(ResourceLocation loc, IChiselTexture texture){
//        if (isValid(loc)){
//            texMap.put(loc, texture);
//        }
//        else {
//            throw new IllegalArgumentException("Chisel texture "+loc+" has already been registered!");
//        }
//    }
//
//    public static void registerTexture(ResourceLocation loc, CombinedChiselTexture texture){
//        if (isValid(loc)){
//            combinedMap.put(loc, texture);
//        }
//        else {
//            throw new IllegalArgumentException("Chisel texture "+loc+" has already been registered!");
//        }
//    }
//
//    public static IChiselTexture getTexture(ResourceLocation loc){
//        if (texMap.containsKey(loc)){
//            return texMap.get(loc);
//        }
//        else if (combinedMap.containsKey(loc)){
//            throw new IllegalArgumentException("Chisel Texture " + loc + " is combined!");
//        }
//        else {
//            throw new IllegalArgumentException("There is no Chisel texture with the name "+loc);
//        }
//    }
//
//    public static PossibleType<IChiselTexture, CombinedChiselTexture> getPossible(ResourceLocation loc){
//        if (isNonCombinedTex(loc)){
//            return PossibleType.makeFirst(getTexture(loc));
//        }
//        else if (isCombinedTex(loc)){
//            return PossibleType.makeSecond(getCombinedTexture(loc));
//        }
//        else {
//            throw new IllegalArgumentException("There is no Chisel texture with the name "+loc);
//        }
//    }
//
//    public static boolean isTex(ResourceLocation loc){
//        return isNonCombinedTex(loc) || isCombinedTex(loc);
//    }
//
//    public static boolean isNonCombinedTex(ResourceLocation loc){
//        return texMap.containsKey(loc);
//    }
//
//    public static boolean isCombinedTex(ResourceLocation loc){
//        return combinedMap.containsKey(loc);
//    }
//
//    public static CombinedChiselTexture getCombinedTexture(ResourceLocation loc){
//        if (combinedMap.containsKey(loc)){
//            return combinedMap.get(loc);
//        }
//        else if (texMap.containsKey(loc)){
//            throw new IllegalArgumentException("Chisel Texture "+ loc + " is not a combined texture!");
//        }
//        else {
//            throw new IllegalArgumentException("There is no Chisel texture with the name "+loc);
//        }
//    }
//
//    private static boolean isValid(ResourceLocation loc){
//        return !texMap.containsKey(loc) && !combinedMap.containsKey(loc);
//    }
//}
