package com.github.eugenelesnov

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.FieldExpression
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.FieldASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import static org.codehaus.groovy.ast.tools.GenericsUtils.makeClassSafeWithGenerics

@CompileStatic
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
        if (!isDuplicateExists(annotatedField).isPresent()) {
            annotatedField.owner.addMethod(
                    "get${annotatedField.name.capitalize()}Optional",
                    getVisibilityProperty(annotation),
                    makeClassSafeWithGenerics(Optional, annotatedField.type),
                    Parameter.EMPTY_ARRAY,
                    ClassNode.EMPTY_ARRAY,
                    buildMethodBody(annotatedField)
            )
        }
    }

    private Optional<MethodNode> isDuplicateExists(FieldNode annotatedField) {
        return annotatedField.owner.methods.stream()
                .filter(m -> {
                    m.returnType == makeClassSafeWithGenerics(Optional, annotatedField.type) &&
                            m.name == "get${annotatedField.name.capitalize()}Optional" &&
                            m.parameters == Parameter.EMPTY_ARRAY
                })
                .findAny()
    }

    private ReturnStatement buildMethodBody(FieldNode annotatedField) {
        return new ReturnStatement(
                new StaticMethodCallExpression(
                        new ClassNode(Optional),
                        "ofNullable",
                        new FieldExpression(annotatedField)
                )
        )
    }

    private int getVisibilityProperty(AnnotationNode annotation) {
        def visibility = annotation.getMember("visibility")
        if (visibility == null) {
            return Visibility.PUBLIC.code
        }
        def propertyExpression = visibility.properties["property"] as ConstantExpression
        return Visibility.valueOf(propertyExpression.value as String).code
    }
}
