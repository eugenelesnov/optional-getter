package com.github.eugenelesnov

import groovy.test.GroovyAssert
import org.junit.jupiter.api.Test

class OptionalGetterTransformationTest extends GroovyAssert {

    @Test
    void 'should generate optional getter with public modifier'() {
        assertScript '''
        import com.github.eugenelesnov.OptionalGetter
        import com.github.eugenelesnov.Visibility

        class Foo {
            @OptionalGetter(visibility = Visibility.PUBLIC)
            private String someField
        }
        
        void main() {
            Foo foo = new Foo()
            foo.setSomeField("value")

            def getter = foo.class.getDeclaredMethod("getSomeFieldOptional", new Class[] {})
            assert getter != null
            assert Arrays.asList(getter.modifiers).contains(Opcodes.ACC_PUBLIC)
            assert getter.returnType instanceof Optional
        }
'''
    }

    @Test
    void 'should generate optional getter with protected modifier'() {
        assertScript '''
        import com.github.eugenelesnov.OptionalGetter
        import com.github.eugenelesnov.Visibility
        import jdk.internal.org.objectweb.asm.Opcodes

        class Foo {
            @OptionalGetter(visibility = Visibility.PROTECTED)
            private String someField
        }
        
        void main() {
            Foo foo = new Foo()
            foo.setSomeField("value")

            def getter = foo.class.getDeclaredMethod("getSomeFieldOptional", new Class[] {})
            assert getter != null
            assert Arrays.asList(getter.modifiers).contains(Opcodes.ACC_PROTECTED)
            assert getter.returnType instanceof Optional
        }
'''
    }

    @Test
    void 'should generate optional getter with private modifier'() {
        assertScript '''
        import com.github.eugenelesnov.OptionalGetter
        import com.github.eugenelesnov.Visibility
        import jdk.internal.org.objectweb.asm.Opcodes

        class Foo {
            @OptionalGetter(visibility = Visibility.PRIVATE)
            private String someField
        }
        
        void main() {
            Foo foo = new Foo()
            foo.setSomeField("value")

            def getter = foo.class.getDeclaredMethod("getSomeFieldOptional", new Class[] {})
            assert getter != null
            assert Arrays.asList(getter.modifiers).contains(Opcodes.ACC_PRIVATE)
            assert getter.returnType instanceof Optional
        }
'''
    }
}
