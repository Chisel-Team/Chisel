package team.chisel.client.data;

import java.util.function.Function;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import team.chisel.api.block.ModelTemplate;

public class ModelTemplates {

    private static String modid(Block block) {
        return block.getRegistryName().getNamespace();
    }
    
    public static String name(Block block) {
        return block.getRegistryName().getPath();
    }
    
    public static ModelTemplate simpleBlock() {
        return ModelTemplates::simpleBlock;
    }
    
    private static void simpleBlock(RegistrateBlockstateProvider prov, Block block) {
        // TODO fix this mess in forge, it should check for explicitly "block/" or "item/" not any folder prefix
        prov.simpleBlock(block, prov.models().cubeAll("block/" + name(block), prov.modLoc("block/" + name(block))));
    }
    
    public static String replaceVariant(String name, String newVariant) {
        return name.replaceAll("\\w+$", newVariant);
    }
    
    public static String replaceBlock(String name, String newBlock) {
        return name.replaceAll("^block/\\w+", "block/" + newBlock);
    }
    
    public static ModelTemplate cubeBottomTop() {
        return cubeBottomTop(name -> name + "-side", name -> name + "-bottom", name -> name + "-top");
    }

    public static ModelTemplate cubeBottomTop(String side, String bottom, String top) {
        return cubeBottomTop(name -> replaceVariant(name, side), name -> replaceVariant(name, bottom), name -> replaceVariant(name, top));
    }
    
    private static ModelTemplate cubeBottomTop(Function<String, String> side, Function<String, String> bottom, Function<String, String> top) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            prov.simpleBlock(block, prov.models().cubeBottomTop(name, prov.modLoc(side.apply(name)), prov.modLoc(bottom.apply(name)), prov.modLoc(top.apply(name))));
        };
    }
    
    public static ModelTemplate cubeColumn() {
        return cubeColumn(name -> name + "-side", name -> name + "-top");
    }
    
    public static ModelTemplate cubeColumn(Function<String, String> top) {
        return cubeColumn(name -> name + "-side", top);
    }
    
    public static ModelTemplate cubeColumn(String side, String top) {
        return cubeColumn(name -> replaceVariant(name, side), name -> replaceVariant(name, top));
    }
    
    private static ModelTemplate cubeColumn(Function<String, String> side, Function<String, String> top) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            prov.simpleBlock(block, prov.models().cubeColumn(name, prov.modLoc(side.apply(name)), prov.modLoc(top.apply(name))));
        };
    }
    
    public static ModelTemplate ctm(String variant) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            String texName = replaceVariant(name, variant);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("cube_ctm"))
                    .texture("all", texName)
                    .texture("connected_tex", texName + "-ctm"));
        };
    }
    
    public static ModelTemplate twoLayerWithTop(String top, boolean shade) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc(shade ? "cube_2_layer" : "cube_2_layer_no_shade"))
                    .texture("bot", name)
                    .texture("top", replaceVariant(name, top)));
        };
    }

    public static ModelTemplate axisFaces() {
        return (prov, block) -> {
            String name = "block/" + name(block);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("cube_axis"))
                    .texture("x", name + "-ew")
                    .texture("y", name + "-tb")
                    .texture("z", name + "-ns"));
        };
    }
    
    public static ModelTemplate mossy(String base) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            String texName = replaceBlock(name, base);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/mossy/mossy"))
                    .texture("bot", texName));
        };
    }
    
    public static ModelTemplate mossyColumn(String base) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            String texName = replaceBlock(name, base);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/mossy/mossy_column"))
                    .texture("side", texName + "-side")
                    .texture("end", texName + "-top"));
        };
    }
    
    public static ModelTemplate mossyCtm(String base, String variant) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            String texName = replaceBlock(name, base);
            texName = replaceVariant(texName, variant);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/mossy/mossy_ctm"))
                    .texture("bot", texName)
                    .texture("connect_bot", texName + "-ctm"));
        };
    }

    public static ModelTemplate twoLayerTopShaded(String top, String bottom) {
        return twoLayerTopShaded(top, top, bottom);
    }

    public static ModelTemplate twoLayerTopShaded(String particle, String top, String bottom) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/cube_2_layer_topshaded"))
                    .texture("particle", replaceVariant(name, particle))
                    .texture("top", replaceVariant(name, top))
                    .texture("bot", replaceVariant(name, bottom)));
        };
    }

    public static ModelTemplate threeLayerTopShaded(String particle, String top, String mid, String bottom) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/cube_3_layer_topshaded"))
                    .texture("particle", replaceVariant(name, particle))
                    .texture("top", replaceVariant(name, top))
                    .texture("mid", replaceVariant(name, mid))
                    .texture("bot", replaceVariant(name, bottom)));
        };
    }

    public static ModelTemplate paneBlock(String edge) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            String texture = "block/" + name(block).replaceFirst("pane", "");
            MultiPartBlockStateBuilder builder =  prov.getMultipartBuilder(block);

            builder.part().modelFile(prov.models().withExistingParent(name + "/post", prov.modLoc("block/pane/post"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, false)
                    .end()

                    .part().modelFile(prov.models().withExistingParent(name + "/n", prov.modLoc("block/pane/n"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                        .addModel().condition(PipeBlock.NORTH, true)
                            .condition(PipeBlock.EAST, false)
                            .condition(PipeBlock.SOUTH, false)
                            .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/e", prov.modLoc("block/pane/e"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                        .addModel().condition(PipeBlock.NORTH, false)
                        .condition(PipeBlock.EAST, true)
                        .condition(PipeBlock.SOUTH, false)
                        .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/s", prov.modLoc("block/pane/s"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                        .addModel().condition(PipeBlock.NORTH, false)
                        .condition(PipeBlock.EAST, false)
                        .condition(PipeBlock.SOUTH, true)
                        .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/w", prov.modLoc("block/pane/w"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                        .addModel().condition(PipeBlock.NORTH, false)
                        .condition(PipeBlock.EAST, false)
                        .condition(PipeBlock.SOUTH, false)
                        .condition(PipeBlock.WEST, true)
                    .end()

                    .part().modelFile(prov.models().withExistingParent(name + "/ne", prov.modLoc("block/pane/ne"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                        .addModel().condition(PipeBlock.NORTH, true)
                        .condition(PipeBlock.EAST, true)
                        .condition(PipeBlock.SOUTH, false)
                        .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/se", prov.modLoc("block/pane/se"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/sw", prov.modLoc("block/pane/sw"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, true)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/nw", prov.modLoc("block/pane/nw"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, true)
                    .end()

                    .part().modelFile(prov.models().withExistingParent(name + "/ns", prov.modLoc("block/pane/ns"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/ew", prov.modLoc("block/pane/ew"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, true)
                    .end()

                    .part().modelFile(prov.models().withExistingParent(name + "/nse", prov.modLoc("block/pane/nse"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/sew", prov.modLoc("block/pane/sew"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, true)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/nsw", prov.modLoc("block/pane/nsw"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, true)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/new", prov.modLoc("block/pane/new"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, true)
                    .end()

                    .part().modelFile(prov.models().withExistingParent(name + "/nsew", prov.modLoc("block/pane/nsew"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, true)
                    .end();
        };
    }

    public static ModelTemplate paneBlockCTM(String edge) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            String texture = "block/" + name(block).replaceFirst("pane", "");
            MultiPartBlockStateBuilder builder =  prov.getMultipartBuilder(block);

            builder.part().modelFile(prov.models().withExistingParent(name + "/post", prov.modLoc("block/pane/post"))
                            .texture("pane", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, false)
                    .end()

                    .part().modelFile(prov.models().withExistingParent(name + "/n", prov.modLoc("block/pane/ctm_n"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/e", prov.modLoc("block/pane/ctm_e"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/s", prov.modLoc("block/pane/ctm_s"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/w", prov.modLoc("block/pane/ctm_w"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, true)
                    .end()

                    .part().modelFile(prov.models().withExistingParent(name + "/ne", prov.modLoc("block/pane/ctm_ne"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/se", prov.modLoc("block/pane/ctm_se"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/sw", prov.modLoc("block/pane/ctm_sw"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, true)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/nw", prov.modLoc("block/pane/ctm_nw"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, true)
                    .end()

                    .part().modelFile(prov.models().withExistingParent(name + "/ns", prov.modLoc("block/pane/ctm_ns"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/ew", prov.modLoc("block/pane/ctm_ew"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, true)
                    .end()

                    .part().modelFile(prov.models().withExistingParent(name + "/nse", prov.modLoc("block/pane/ctm_nse"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, false)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/sew", prov.modLoc("block/pane/ctm_sew"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, false)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, true)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/nsw", prov.modLoc("block/pane/ctm_nsw"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, false)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, true)
                    .end()
                    .part().modelFile(prov.models().withExistingParent(name + "/new", prov.modLoc("block/pane/ctm_new"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, false)
                    .condition(PipeBlock.WEST, true)
                    .end()

                    .part().modelFile(prov.models().withExistingParent(name + "/nsew", prov.modLoc("block/pane/ctm_nsew"))
                            .texture("pane", texture)
                            .texture("pane_ct", texture)
                            .texture("edge", edge))
                    .addModel().condition(PipeBlock.NORTH, true)
                    .condition(PipeBlock.EAST, true)
                    .condition(PipeBlock.SOUTH, true)
                    .condition(PipeBlock.WEST, true)
                    .end();
        };
    }

    public static ModelTemplate cubeCTMTranslusent(String all, String ctm) {
        return (prov, block) -> {
            String name = "block/" + name(block);

            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/cube_ctm_translucent"))
                    .texture("all", all)
                    .texture("connected_tex", ctm));
        };
    }

    public static ModelTemplate fluidCube(String fluid) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            String variant = name(block).split("/")[name(block).split("/").length - 1];

            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/" + fluid + "stone/cube_" + fluid))
                    .texture("bot", "minecraft:block/" + fluid + "_still")
                    .texture("top", "block/fluid/" + variant));
        };
    }

    public static ModelTemplate fluidCubeCTM(String fluid, String variant) {
        return (prov, block) -> {
            String name = "block/" + name(block);

            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/" + fluid + "stone/cube_ctm_" + fluid))
                    .texture("bot", "minecraft:block/" + fluid + "_still")
                    .texture("top", "block/fluid/" + variant)
                    .texture("connect_top", "block/fluid/" + variant + "-ctm"));
        };
    }

    public static ModelTemplate fluidPassCube(String fluid) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            String variant = name(block).split("/")[name(block).split("/").length - 1];

            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/" + fluid + "stone/cube_pass_" + fluid))
                    .texture("bot", "minecraft:block/" + fluid + "_still")
                    .texture("top", "block/fluid/" + variant));
        };
    }

    public static ModelTemplate fluidPassColume(String fluid) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            String variant = name(block).split("/")[name(block).split("/").length - 1];

            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/" + fluid + "stone/column_" + fluid))
                    .texture("bot", "minecraft:block/" + fluid + "_still")
                    .texture("side", "block/fluid/" + variant + "-side")
                    .texture("top", "block/fluid/" + variant + "-top"));
        };
    }

    public static ModelTemplate cubeEldritch() {
        return (prov, block) -> {
            String name = "block/" + name(block);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/cube_eldritch"))
                    .texture("all", name));
        };
    }

    public static ModelTemplate columnEldritch(String top) {
        return (prov, block) -> {
            String name = "block/" + name(block);
            prov.simpleBlock(block, prov.models().withExistingParent(name, prov.modLoc("block/column_eldritch"))
                    .texture("end", replaceVariant(name, top) + "-top")
                    .texture("side", name + "-side"));
        };
    }
}
