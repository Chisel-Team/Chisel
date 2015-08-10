package team.chisel.common.block.subblocks;

import team.chisel.client.render.BlockResources;
import team.chisel.common.block.BlockCarvable;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of ISubBlock for non ctm
 *
 * @author minecreatr
 */
public class SubBlock implements ISubBlock {

    private BlockResources resources;
    protected String name;
    protected BlockCarvable parent;


    public SubBlock(BlockResources resources, String name, BlockCarvable parent) {
        this.name = name;
        this.parent = parent;
        this.resources = resources;
    }

    public String getName() {
        return this.name;
    }

    public BlockCarvable getParent() {
        return this.parent;
    }

    @Override
    public BlockResources getResources() {
        return this.resources;
    }

    public static SubBlock generateSubBlock(BlockCarvable parent, String name, String... lore) {
        List<String> loreList = Arrays.asList(lore);
        return new SubBlock(BlockResources.generateBlockResources(parent, name, loreList), name, parent);
    }
}
