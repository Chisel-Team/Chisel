//package team.chisel.client.render;
//
//import team.chisel.Chisel;
//import team.chisel.api.render.RenderType;
//import team.chisel.client.TextureStitcher;
//import team.chisel.common.block.BlockCarvable;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.renderer.texture.TextureMap;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static team.chisel.api.render.RenderType.*;
//
///**
// * Represents the block resources for a connected textures block
// * Each of these is for 1 sub block
// *
// * @author minecreatr
// */
//public class CTMBlockResources extends BlockResources {
//
//    private static List<CTMBlockResources> allResources = new ArrayList<CTMBlockResources>();
//
//    public TextureAtlasSprite ctmTexture;
//
//
//    public CTMBlockResources(String parentName, String name, TextureAtlasSprite texture, TextureAtlasSprite ctmTexture,
//                             TextureAtlasSprite side, TextureAtlasSprite top, TextureAtlasSprite bottom, RenderType type) {
//        super(parentName, name, texture, side, top, bottom, type);
//        this.ctmTexture = ctmTexture;
//        this.texture = texture;
//        allResources.add(this);
//    }
//
//
//    /**
//     * Generates a ctm block resources for the given information
//     *
//     * @param parentName The Name of the parent block
//     * @param subBlockName
//     * @return The generated block resource
//     */
//    public static CTMBlockResources generateBlockResources(String parentName, String subBlockName) {
//        String prefix = MOD_ID.toLowerCase() + ":blocks/" + parentName + "/";
//        Chisel.debug(prefix);
//        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
//        if (map == null) {
//            throw new RuntimeException("TextureMap is null");
//        }
//        RenderType type = CTM;
//        if (isCTMH(parentName, subBlockName)) {
//            type = CTMH;
//        } else if (isCTMV(parentName, subBlockName)) {
//            Chisel.debug("CTMV for " + subBlockName + " parent " + parentName);
//            type = CTMV;
//        }
//        TextureAtlasSprite texture = map.getAtlasSprite(prefix + subBlockName);
//        TextureAtlasSprite ctm;
//        if (type == CTMH) {
//            ctm = map.getAtlasSprite(prefix + subBlockName + "-ctmh");
//        } else if (type == CTMV) {
//            ctm = map.getAtlasSprite(prefix + subBlockName + "-ctmv");
//        } else {
//            ctm = map.getAtlasSprite(prefix + subBlockName + "-ctm");
//        }
//        TextureAtlasSprite side = null;
//        TextureAtlasSprite top = null;
//        TextureAtlasSprite bottom = null;
//        if (hasSideOverride(parentName, subBlockName)) {
//            side = map.getAtlasSprite(prefix + subBlockName + "-side");
//        }
//        if (hasTopOverride(parentName, subBlockName)) {
//            top = map.getAtlasSprite(prefix + subBlockName + "-top");
//        }
//        if (hasBottomOverride(parentName, subBlockName)) {
//            bottom = map.getAtlasSprite(prefix + subBlockName + "-bottom");
//        } else if (top != null) {
//            bottom = top;
//        }
//
//        return new CTMBlockResources(parentName, subBlockName, texture, ctm, side, top, bottom, type);
//    }
//
//    public static RenderType preGenerateBlockResources(BlockCarvable parent, String subBlockName){
//        return preGenerateBlockResources(parent.getName(), subBlockName);
//    }
//
//    public static RenderType preGenerateBlockResources(String parentName, String subBlockName) {
//        String prefix = MOD_ID.toLowerCase() + ":blocks/" + parentName + "/";
//        TextureStitcher.register(prefix + subBlockName);
//        RenderType type = CTM;
//        if (isCTMH(parentName, subBlockName)) {
//            TextureStitcher.register(prefix + subBlockName + "-ctmh");
//            type = CTMH;
//        } else if (isCTMV(parentName, subBlockName)) {
//            TextureStitcher.register(prefix + subBlockName + "-ctmv");
//            type = CTMV;
//        } else {
//            TextureStitcher.register(prefix + subBlockName + "-ctm");
//        }
//        if (hasSideOverride(parentName, subBlockName)) {
//            TextureStitcher.register(prefix + subBlockName + "-side");
//        }
//        if (hasTopOverride(parentName, subBlockName)) {
//            TextureStitcher.register(prefix + subBlockName + "-top");
//        }
//        if (hasBottomOverride(parentName, subBlockName)) {
//            TextureStitcher.register(prefix + subBlockName + "-bottom");
//        }
//        return type;
//    }
//
//    @Override
//    public TextureAtlasSprite getDefaultTexture() {
//        if (type == CTMH || type == CTMV) {
//            return this.ctmTexture;
//        }
//        return this.texture;
//    }
//
//}
