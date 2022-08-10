package team.chisel.common.util;

import java.lang.annotation.*;

/**
 * An alternative to {@link javax.annotation.Nonnull} which works on type parameters (J8 feature).
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NonnullType {
}
