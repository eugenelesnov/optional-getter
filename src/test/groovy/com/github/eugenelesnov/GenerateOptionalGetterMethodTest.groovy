package com.github.eugenelesnov

import groovy.transform.ASTTest
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode

import static org.codehaus.groovy.control.CompilePhase.CANONICALIZATION

@ASTTest(phase = CANONICALIZATION, value = {
    when: 'we inspect the result of applying AST to the class of annotated field'
    def classNode = node as ClassNode

    then: 'classNode contains getter method that returns Optional'
    MethodNode methodNode = classNode.getAllDeclaredMethods().stream()
            .filter(methodNode -> methodNode.getName() == "getSomeFieldOptional")
            .findAny()
            .orElseThrow()

    andThen: 'classNode contains getter method that returns Optional'
    assert methodNode.getReturnType().getTypeClass() == Optional

    andThen: 'generated method should have protected modifier'
    assert methodNode.getModifiers() == Opcodes.ACC_PROTECTED
})
class GenerateOptionalGetterMethodTest {

    @OptionalGetter(visibility = Visibility.PROTECTED)
    private String someField = "testValue"

}

