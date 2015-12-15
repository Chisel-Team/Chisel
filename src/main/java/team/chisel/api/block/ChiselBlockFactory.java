package team.chisel.api.block;

/**
 * Factory to create builders to create blocks
 */
public class ChiselBlockFactory {

    private String domain;

    private ChiselBlockFactory(String domain){
        this.domain = domain;
    }

    public static ChiselBlockFactory newFactory(String domain){
        return new ChiselBlockFactory(domain);
    }

    public ChiselBlockBuilder newBlock(String blockName){
        return new ChiselBlockBuilder(domain, blockName);
    }
}
