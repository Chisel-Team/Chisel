package team.chisel.client.render;


import org.lwjgl.util.vector.Vector3f;

/**
 * Two vectors to represent the position of a quad
 */
public class QuadPos {
    public Vector3f from;
    public Vector3f to;

    public QuadPos(Vector3f from, Vector3f to) {
        this.from = from;
        this.to = to;
    }
}