package com.github.eugenelesnov

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Annotation marker for generating a getter
 *
 * @author Eugene Lesnov
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
@GroovyASTTransformationClass(classes = [OptionalGetterTransformation])
@interface OptionalGetter {

    Visibility visibility() default Visibility.PUBLIC
}