//package team.chisel.client.render;
//
//import team.chisel.Chisel;
//import team.chisel.api.render.RenderType;
//import team.chisel.client.TextureStitcher;
//import team.chisel.common.Reference;
//import team.chisel.common.block.BlockCarvable;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.renderer.texture.TextureMap;
//
//import java.util.List;
//import static team.chisel.api.render.RenderType.*;
//
///**
// * Represents the resources for a non Connected Texture block
// * aka Textures and lore
// * Each of these is for a sub block
// *
// * @author minecreatr
// */
//public class BlockResources implements IBlockResources, Reference {
//
//
//    public TextureAtlasSprite texture;
//
//    /**
//     * Side override texture
//     */
//    public TextureAtlasSprite side;
//
//    /**
//     * Top override texture
//     */
//    public TextureAtlasSprite top;
//
//    /**
//     * Bottom override texture
//     */
//    public TextureAtlasSprite bottom;
//
//    protected String variantName;
//    protected String parentName;
//
//    public RenderType type;
//
//    protected BlockResources() {
//    }
//
//    ;
//
//    public BlockResources(String parentName, String name, TextureAtlasSprite texture,
//                          TextureAtlasSprite side, TextureAtlasSprite top, TextureAtlasSprite bottom, RenderType type) {
//        this.texture = texture;
//        this.variantName = name;
//        this.parentName = parentName;
//        this.side = side;
//        this.top = top;
//        this.bottom = bottom;
//        this.type = type;
//    }
//
//    @Override
//    public TextureAtlasSprite getDefaultTexture() {
//        return this.texture;
//    }
//
//    @Override
//    public String getParentName() {
//        return this.parentName;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        if (object instanceof BlockResources) {
//            BlockResources res = (BlockResources) object;
//            return (res.getParentName().equals(this.getParentName())) && (this.getVariantName().equals(res.getVariantName()));
//        }
//        return false;
//    }
//
//    @Override
//    public String getVariantName() {
//        return this.variantName;
//    }
//
//
//    public static BlockResources generateBlockResources(String parentName, String subBlockName) {
//        String prefix = MOD_ID.toLowerCase() + ":blocks/" + parentName + "/";
//        Chisel.debug(prefix);
//        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
//        if (map == null) {
//            throw new RuntimeException("TextureMap is null");
//        }
//        RenderType type = NORMAL;
//        TextureAtlasSprite texture;
//        if (isV9(parentName, subBlockName)) {
//            texture = map.getAtlasSprite(prefix + subBlockName + "-v9");
//            type = V9;
//        } else if (isV4(parentName, subBlockName)) {
//            texture = map.getAtlasSprite(prefix + subBlockName + "-v4");
//            type = V4;
//        } else if (isR16(parentName, subBlockName)) {
//            texture = map.getAtlasSprite(prefix + subBlockName + "-r16");
//            type = R16;
//        } else if (isR9(parentName, subBlockName)) {
//            texture = map.getAtlasSprite(prefix + subBlockName + "-r9");
//            type = R9;
//        } else if (isR4(parentName, subBlockName)) {
//            texture = map.getAtlasSprite(prefix + subBlockName + "-r4");
//            type = R4;
//        } else {
//            texture = map.getAtlasSprite(prefix + subBlockName);
//        }
//
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
//        }
//        if (top != null && bottom == null) {
//            bottom = top;
//        }
//
//        return new BlockResources(parentName, subBlockName, texture, side, top, bottom, type);
//    }
//
//    public static RenderType preGenerateBlockResources(BlockCarvable parent, String subBlockName){
//        return preGenerateBlockResources(parent.getName(), subBlockName);
//    }
//
//    public static RenderType preGenerateBlockResources(String parentName, String subBlockName) {
//        String prefix = MOD_ID.toLowerCase() + ":blocks/" + parentName + "/";
//        RenderType type = NORMAL;
//        if (isV9(parentName, subBlockName)) {
//            TextureStitcher.register(prefix + subBlockName + "-v9");
//            type = V9;
//        } else if (isV4(parentName, subBlockName)) {
//            TextureStitcher.register(prefix + subBlockName + "-v4");
//            type = V4;
//        } else if (isR16(parentName, subBlockName)) {
//            TextureStitcher.register(prefix + subBlockName + "-r16");
//            type = R16;
//        } else if (isR9(parentName, subBlockName)) {
//            TextureStitcher.register(prefix + subBlockName + "-r9");
//            type = R9;
//        } else if (isR4(parentName, subBlockName)) {
//            TextureStitcher.register(prefix + subBlockName + "-r4");
//            type = R4;
//        } else {
//            TextureStitcher.register(prefix + subBlockName);
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
//    public static int getVariationWidth(RenderType type) {
//        if (type == V9 || type == R9) {
//            return 3;
//        } else if (type == V4 || type == R4) {
//            return 2;
//        } else if (type == R16) {
//            return 4;
//        } else {
//            return 0;
//        }
//    }
//
//    public static int getVariationSize(RenderType type) {
//        return getVariationWidth(type) * getVariationWidth(type);
//    }
//
//    public static boolean isV(RenderType type) {
//        return (type == V4 || type == V9);
//    }
//
//
//    protected static boolean hasSideOverride(String blockName, String variation) {
//        String path = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-side.png";
//        return Chisel.class.getResource(path) != null;
//    }
//
//    protected static boolean hasTopOverride(String blockName, String variation) {
//        String path = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-top.png";
//        return Chisel.class.getResource(path) != null;
//    }
//
//    protected static boolean hasBottomOverride(String blockName, String variation) {
//        String path = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-bottom.png";
//        return Chisel.class.getResource(path) != null;
//    }
//
//    protected static boolean isCTMH(String blockName, String variation) {
//        String path = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-ctmh.png";
//        return Chisel.class.getResource(path) != null;
//    }
//
//    protected static boolean isCTMV(String blockName, String variation) {
//        String path = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-ctmv.png";
//        boolean r = Chisel.class.getResource(path) != null;
//        if (r) {
//            Chisel.debug("Returning true ctmv for " + blockName + " variation " + variation);
//        } else {
//            Chisel.debug("Returning false for ctmv for path\n" + path);
//        }
//        return r;
//    }
//
//    protected static boolean isV9(String blockName, String variation) {
//        String path = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-v9.png";
//        return Chisel.class.getResource(path) != null;
//    }
//
//    protected static boolean isV4(String blockName, String variation) {
//        String path = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-v4.png";
//        return Chisel.class.getResource(path) != null;
//    }
//
//    protected static boolean isR16(String blockName, String variation) {
//        String path = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-r16.png";
//        return Chisel.class.getResource(path) != null;
//    }
//
//    protected static boolean isR9(String blockName, String variation) {
//        String path = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-r9.png";
//        return Chisel.class.getResource(path) != null;
//    }
//
//    protected static boolean isR4(String blockName, String variation) {
//        String path = "/assets/" + MOD_ID.toLowerCase() + "/textures/blocks/" + blockName + "/" + variation + "-r4.png";
//        return Chisel.class.getResource(path) != null;
//    }
//
//    public static int getIntervals(RenderType in) {
//        if (in == V9 || in == R9) {
//            return 3;
//        } else if (in == V4 || in == R4) {
//            return 2;
//        } else if (in == R16) {
//            return 4;
//        }
//        return 1;
//    }
//
//    @Override
//    public RenderType getType() {
//        return this.type;
//    }
//
//
//}
