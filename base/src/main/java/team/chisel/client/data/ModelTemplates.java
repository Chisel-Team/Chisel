package team.chisel.client.data;

import java.util.function.Function;

import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraftforge.client.model.generators.ModelFile;
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

    public static ModelTemplate paneblock() {
        return (prov, block) -> {
            MultiPartBlockStateBuilder builder =  prov.getMultipartBuilder(block);

            builder.part().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation("chisel", "pane"), prov.models().existingFileHelper)).addModel().end();


            builder.part().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation("chisel", "ctm_n"), prov.models().existingFileHelper))
                    .addModel().condition(PipeBlock.NORTH, true).end()
                    .part().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation("chisel", "ctm_e"), prov.models().existingFileHelper))
                    .addModel().condition(PipeBlock.EAST, true).end()
                    .part().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation("chisel", "ctm_s"), prov.models().existingFileHelper))
                    .addModel().condition(PipeBlock.SOUTH, true).end()
                    .part().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation("chisel", "ctm_w"), prov.models().existingFileHelper))
                    .addModel().condition(PipeBlock.WEST, true).end()

                    .part().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation("chisel", "ctm_ne"), prov.models().existingFileHelper))
                    .addModel().condition(PipeBlock.NORTH, true).condition(PipeBlock.EAST, true).end()
                    .part().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation("chisel", "ctm_se"), prov.models().existingFileHelper))
                    .addModel().condition(PipeBlock.SOUTH, true).condition(PipeBlock.EAST, true).end()
                    .part().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation("chisel", "ctm_sw"), prov.models().existingFileHelper))
                    .addModel().condition(PipeBlock.SOUTH, true).condition(PipeBlock.WEST, true).end()
                    .part().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation("chisel", "ctm_nw"), prov.models().existingFileHelper))
                    .addModel().condition(PipeBlock.NORTH, true).condition(PipeBlock.WEST, true).end();
        };
    }
}
