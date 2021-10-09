package com.github.eugenelesnov

import groovy.transform.ASTTest
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

    assert methodNode.getName() == "getSomeFieldOptional"
    println methodNode.getTypeDescriptor()
})
class GenerateOptionalGetterMethodTest {

    @OptionalGetter
    private String someField = "testValue"

}

