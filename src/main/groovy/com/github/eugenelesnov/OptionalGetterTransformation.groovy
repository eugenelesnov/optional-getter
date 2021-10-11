package com.github.eugenelesnov

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import static com.github.eugenelesnov.Visibility.parseOpCode
import static org.codehaus.groovy.ast.ClassHelper.make

@CompileStatic
@SuppressWarnings("GrMethodMayBeStatic")
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class OptionalGetterTransformation extends AbstractASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        if (nodes != null) {
            super.init(nodes, source)

            AnnotationNode annotation = nodes[0] as AnnotationNode
            AnnotatedNode annotatedField = nodes[1] as AnnotatedNode

            if (annotatedField instanceof FieldNode) {
                visitNode(annotation, annotatedField)
            }
        }
    }

    private void visitNode(AnnotationNode annotation, FieldNode annotatedField) {
        annotatedField.owner.addMethod(
                "get" + annotatedField.name.capitalize() + "Optional",
                getVisibilityProperty(annotation),
                new ClassNode(Optional),
                Parameter.EMPTY_ARRAY,
                ClassNode.EMPTY_ARRAY,
                buildMethodBody(annotatedField.initialValueExpression)
        )
    }

    private ReturnStatement buildMethodBody(Expression valueToReturn) {
        return new ReturnStatement(
                new StaticMethodCallExpression(
                        make(Optional),
                        "ofNullable",
                        new ConstantExpression(valueToReturn)
                )
        )
    }

    private int getVisibilityProperty(AnnotationNode annotation) {
        def visibility = annotation.getMember("visibility").properties["property"] as ConstantExpression
        return parseOpCode(visibility.value as String)
    }
}
