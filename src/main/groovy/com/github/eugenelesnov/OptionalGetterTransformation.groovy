package com.github.eugenelesnov

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import static org.codehaus.groovy.ast.ClassHelper.make

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class OptionalGetterTransformation extends AbstractASTTransformation {

    private static final ClassNode OPTIONAL_GETTER_ANNOTATION = make(OptionalGetter)

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {

    }
}
