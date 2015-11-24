package team.chisel.client.texture;

/**
 * Represents a texture that can be shown on a block
 *
 * @author minecreatr
 */
public interface ITextureResource {

    /**
     * Get the type of this resource
     * @return The Type of this resource
     */
    TextureType getType();
}
