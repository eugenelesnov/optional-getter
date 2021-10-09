package com.github.eugenelesnov

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.stmt.ReturnStatement
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
        super.init(nodes, source)

        Arrays.stream(nodes)
                .filter(node -> node instanceof FieldNode)
                .forEach(node -> visitNode((FieldNode) node))
    }

    private static void visitNode(FieldNode annotatedField) {
        if (isFieldValidForAST(annotatedField)) {
            def owner = annotatedField.getOwner()
            owner.addMethod(
                    "getOptional" + annotatedField.getName().capitalize(),
                    ACC_PUBLIC,
                    annotatedField.getType(),
                    Parameter.EMPTY_ARRAY,
                    ClassNode.EMPTY_ARRAY,
                    new ReturnStatement(
                            new ConstantExpression(Optional.ofNullable(annotatedField.getInitialExpression()))
                    )
            )
        }
    }

    private static boolean isFieldValidForAST(FieldNode annotatedField) {
        return annotatedField.getAnnotations().stream()
                .anyMatch(a -> a.getClassNode() == OPTIONAL_GETTER_ANNOTATION)
    }
}
