package team.chisel.client.render;


import lombok.ToString;

import org.lwjgl.util.vector.Vector3f;

/**
 * Two vectors to represent the position of a quad
 */
@ToString
public class QuadPos {

    public final Vector3f from;
    public final Vector3f to;

    public QuadPos(Vector3f from, Vector3f to) {
        this.from = from;
        this.to = to;
    }
}