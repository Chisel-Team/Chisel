package team.chisel.client.texture;

/**
 * Enum of different types a texture resource can be
 *
 * @author minecreatr
 */
public enum TextureType {

    NORMAL("normal"),
    CTM("ctm"),
    CTMH("ctmh"),
    CTMV("ctmv"),
    V9("v9"),
    V4("v4"),
    R16("r16"),
    R9("r9"),
    R4("r4"),
    COMBINED("combined");

    private String name;

    TextureType(String name){
        this.name = name;
    }
}
