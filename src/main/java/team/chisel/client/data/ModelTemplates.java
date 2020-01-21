package team.chisel.client.data;

import javax.annotation.ParametersAreNonnullByDefault;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import team.chisel.api.block.ModelTemplate;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModelTemplates {
    
    private static String modid(Block block) {
        return block.getRegistryName().getNamespace();
    }
    
    private static String name(Block block) {
        return block.getRegistryName().getPath();
    }
    
    public static ModelTemplate simpleBlock() {
        return ModelTemplates::simpleBlock;
    }
    
    private static void simpleBlock(RegistrateBlockstateProvider prov, Block block) {
        // TODO fix this mess in forge, it should check for explicitly "block/" or "item/" not any folder prefix
        prov.simpleBlock(block, prov.cubeAll("block/" + name(block), prov.modLoc("block/" + name(block))));
    }
    
    public static ModelTemplate cubeBottomTop() {
        return (prov, block) -> {
            String name = "block/" + name(block);
            prov.simpleBlock(block, prov.cubeBottomTop(name, prov.modLoc(name + "-side"), prov.modLoc(name + "-bottom"), prov.modLoc(name + "-top")));
        };
    }
    
    public static ModelTemplate cubeColumn() {
        return (prov, block) -> {
            String name = "block/" + name(block);
            prov.simpleBlock(block, prov.cubeColumn(name, prov.modLoc(name + "-side"), prov.modLoc(name + "-top")));
        };
    }
    
    public static ModelTemplate ctm(String variant) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            String texName = name.replaceAll("\\w+$", variant);
            prov.simpleBlock(block, prov.withExistingParent(name, prov.modLoc("cube_ctm"))
                    .texture("all", texName)
                    .texture("connected_tex", texName + "-ctm"));
        };
    }
}
