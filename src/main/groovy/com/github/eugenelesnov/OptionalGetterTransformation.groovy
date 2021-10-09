package com.github.eugenelesnov

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression
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
        if (nodes != null) {
            super.init(nodes, source)

            AnnotationNode annotation = nodes[0] as AnnotationNode
            AnnotatedNode annotatedField = nodes[1] as AnnotatedNode

            if (annotatedField instanceof FieldNode) {
                visitNode(annotatedField)
            }
        }
    }

    private static void visitNode(FieldNode annotatedField) {
        if (isFieldValidForAST(annotatedField)) {
            annotatedField.getOwner().addMethod(
                    "get" + annotatedField.getName().capitalize() + "Optional",
                    ACC_PUBLIC,
                    annotatedField.getType(),
                    Parameter.EMPTY_ARRAY,
                    ClassNode.EMPTY_ARRAY,
                    buildMethodBody(annotatedField.getInitialValueExpression())
            )
        }
    }

    private static ReturnStatement buildMethodBody(Expression valueToReturn) {
        new ReturnStatement(
                new StaticMethodCallExpression(new ClassNode(Optional.class), "ofNullable", valueToReturn)
        )
    }

    private static boolean isFieldValidForAST(FieldNode annotatedField) {
        return annotatedField.getAnnotations().stream()
                .anyMatch(a -> a.getClassNode() == OPTIONAL_GETTER_ANNOTATION)
    }
}
