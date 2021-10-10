package com.github.eugenelesnov

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
@GroovyASTTransformationClass(classes = [OptionalGetterTransformation])
@interface OptionalGetter {

    Visibility visibility()
}