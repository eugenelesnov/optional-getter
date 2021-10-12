package com.github.eugenelesnov

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.FieldASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import static org.codehaus.groovy.ast.tools.GenericsUtils.makeClassSafeWithGenerics

@SuppressWarnings("GrMethodMayBeStatic")
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class OptionalGetterTransformation extends FieldASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        if (nodes != null) {

            AnnotationNode annotation = nodes[0] as AnnotationNode
            AnnotatedNode annotatedField = nodes[1] as AnnotatedNode

            if (annotatedField instanceof FieldNode) {
                visitNode(annotation, annotatedField)
            }
        }
    }

    private void visitNode(AnnotationNode annotation, FieldNode annotatedField) {
        def fieldValue = annotatedField.properties as ConstantExpression

        annotatedField.owner.addMethod(
                "get" + annotatedField.name.capitalize() + "Optional",
                getVisibilityProperty(annotation),
                makeClassSafeWithGenerics(Optional, fieldValue.type),
                Parameter.EMPTY_ARRAY,
                ClassNode.EMPTY_ARRAY,
                macro { return Optional.ofNullable(fieldValue.value) }
        )
    }

    private int getVisibilityProperty(AnnotationNode annotation) {
        def visibility = annotation.getMember("visibility").properties["property"] as ConstantExpression
        return Visibility.valueOf(visibility.value as String).code
    }
}
