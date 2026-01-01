package vajra.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// Indicates that something is accessed by reflection, despite not having any obvious code usages. Care must be taken
/// when modifying or removing anything with this annotation because it may break the reflection code in unobvious ways.
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD})
public @interface Reflected {

}
