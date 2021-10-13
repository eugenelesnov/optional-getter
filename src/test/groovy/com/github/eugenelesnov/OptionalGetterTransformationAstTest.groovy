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

        then: 'generated method name should be getSomeFieldOptional'
        MethodNode methodNode = fieldNode.owner.getDeclaredMethod("getSomeFieldOptional", Parameter.EMPTY_ARRAY)
        assert methodNode != null

        andThen: 'generated method should return Optional<String>'
        assert methodNode.returnType == make(Optional<String>)

        andThen: 'generated method should have protected modifier'
        assert methodNode.isProtected()
    })
    @OptionalGetter(visibility = Visibility.PROTECTED)
    private String someField

}

