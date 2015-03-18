package com.cricketcraft.chisel.client.render.ctm;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.List;

/**
 * Represents a ctm face
 *
 * @author minecreatr
 */
public class CTMFace {

    private BakedQuad bottom_left; // Place 0 in array
    private BakedQuad bottom_right; // Place 1 in array
    private BakedQuad top_right; // Place 2 in array
    private BakedQuad top_left; // Place 3 in array

    public CTMFace(BakedQuad bottom_left, BakedQuad bottom_right, BakedQuad top_right, BakedQuad top_left){
        this.bottom_left=bottom_left;
        this.bottom_right=bottom_right;
        this.top_right=top_right;
        this.top_left=top_left;
    }

    public void addToList(List<BakedQuad> quads){
        quads.add(bottom_left);
        quads.add(bottom_right);
        quads.add(top_right);
        quads.add(top_left);
    }
}
