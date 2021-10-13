package com.github.eugenelesnov

import groovy.transform.ASTTest
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.control.CompilePhase

import static org.codehaus.groovy.ast.ClassHelper.make

class OptionalGetterTransformationAstTest {

    @ASTTest(phase = CompilePhase.SEMANTIC_ANALYSIS, value = {
        when: 'we inspect the result of applying AST to the annotated field'
        def fieldNode = node as FieldNode

        then: 'generated method name should be getSomeStringFieldOptional'
        MethodNode methodNode = fieldNode.owner.getDeclaredMethod("getSomeStringFieldOptional", Parameter.EMPTY_ARRAY)
        assert methodNode != null

        andThen: 'generated method should return Optional<String>'
        assert methodNode.returnType == make(Optional<String>)

        andThen: 'generated method should have protected modifier'
        assert methodNode.isProtected()
    })
    @OptionalGetter(visibility = Visibility.PROTECTED)
    private String someStringField

    @ASTTest(phase = CompilePhase.SEMANTIC_ANALYSIS, value = {
        when: 'we inspect the result of applying AST to the annotated field'
        def fieldNode = node as FieldNode

        then: 'generated method name should be getSomeBooleanFieldOptional'
        MethodNode methodNode = fieldNode.owner.getDeclaredMethod("getSomeBooleanFieldOptional", Parameter.EMPTY_ARRAY)
        assert methodNode != null

        andThen: 'generated method should return Optional<Boolean>'
        assert methodNode.returnType == make(Optional<Boolean>)

        andThen: 'generated method should have public modifier'
        assert methodNode.isPublic()
    })
    @OptionalGetter(visibility = Visibility.PUBLIC)
    private Boolean someBooleanField

    @ASTTest(phase = CompilePhase.SEMANTIC_ANALYSIS, value = {
        when: 'we inspect the result of applying AST to the annotated field'
        def fieldNode = node as FieldNode

        then: 'generated method name should be getSomeIntegerFieldOptional'
        MethodNode methodNode = fieldNode.owner.getDeclaredMethod("getSomeIntegerFieldOptional", Parameter.EMPTY_ARRAY)
        assert methodNode != null

        andThen: 'generated method should return Optional<Integer>'
        assert methodNode.returnType == make(Optional<Integer>)

        andThen: 'generated method should have public modifier by default '
        assert methodNode.isPublic()
    })
    @OptionalGetter
    private Integer someIntegerField

}

