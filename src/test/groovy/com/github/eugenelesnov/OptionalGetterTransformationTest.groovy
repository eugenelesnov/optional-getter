package com.github.eugenelesnov

import groovy.test.GroovyAssert
import org.junit.jupiter.api.Test

class OptionalGetterTransformationTest extends GroovyAssert {

    @Test
    void 'should generate optional getter'() {
        assertScript '''
        import com.github.eugenelesnov.OptionalGetter
        import com.github.eugenelesnov.Visibility

        class Foo {
            @OptionalGetter(visibility = Visibility.PUBLIC)
            private String someField

            void setSomeField(String someField) {
                this.someField = someField
            }
        }
        
        void main() {
            Foo foo = new Foo()
            foo.setSomeField("value")

            def getter = foo.getClass().getDeclaredMethod("getSomeFieldOptional", new Class[] {})
            assert getter != null
            assert getter.getReturnType() instanceof Optional
        }
'''
    }
}
