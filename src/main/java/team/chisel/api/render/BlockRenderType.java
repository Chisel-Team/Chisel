package team.chisel.api.render;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to register an IBlockRenderType
 *
 * CLASSES THAT HAVE THIS ANNOTATION MUST HAVE A NO ARGUMENT CONSTRUCTOR
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BlockRenderType {

    /**
     * The String used for serialization/deserialization
     * For example connected textures would be CTM
     * @return
     */
    String value();
}
