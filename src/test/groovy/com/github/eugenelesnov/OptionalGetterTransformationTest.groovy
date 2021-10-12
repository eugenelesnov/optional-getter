package com.github.eugenelesnov

import org.junit.jupiter.api.Test

import static groovy.test.GroovyAssert.assertScript

class OptionalGetterTransformationTest {

    @Test
    void 'should generate optional getter with String as generic and with public modifier'() {
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
            assert getter.returnType instanceof Optional<String>
            Optional<String> optional = foo.getSomeFieldOptional()
            optional.ifPresentOrElse(value -> { assert value == getSomeField()}, 
                    () -> { throw new Exception("actual value not equals to expected")})
        }
'''
    }

    @Test
    void 'should generate optional getter with Integer as generic and with protected modifier'() {
        assertScript '''
        import com.github.eugenelesnov.OptionalGetter
        import com.github.eugenelesnov.Visibility
        import jdk.internal.org.objectweb.asm.Opcodes

        class Foo {
            @OptionalGetter(visibility = Visibility.PROTECTED)
            private Integer someField
        }
        
        void main() {
            Foo foo = new Foo()
            foo.setSomeField(1)

            def getter = foo.class.getDeclaredMethod("getSomeFieldOptional", new Class[] {})
            assert getter != null
            assert Arrays.asList(getter.modifiers).contains(Opcodes.ACC_PROTECTED)
            assert getter.returnType instanceof Optional<Integer>
            Optional<Integer> optional = foo.getSomeFieldOptional()
            optional.ifPresentOrElse(value -> { assert value == foo.getSomeField()}, 
                    () -> { throw new Exception("actual value not equals to expected")})
        }
'''
    }

    @Test
    void 'should generate optional getter with some custom object as generic and with private modifier'() {
        assertScript '''
        import com.github.eugenelesnov.OptionalGetter
        import com.github.eugenelesnov.Visibility
        import jdk.internal.org.objectweb.asm.Opcodes

        class Bar {
            private String bar
        }

        class Foo {
            @OptionalGetter(visibility = Visibility.PRIVATE)
            private Bar someField
        }
        
        void main() {
            Foo foo = new Foo()
            Bar bar = new Bar()
            bar.setBar("bar")
            foo.setSomeField(bar)

            def getter = foo.class.getDeclaredMethod("getSomeFieldOptional", new Class[] {})
            assert getter != null
            assert Arrays.asList(getter.modifiers).contains(Opcodes.ACC_PRIVATE)
            assert getter.returnType instanceof Optional<Bar>
            Optional<Bar> optional = foo.getSomeFieldOptional()
            optional.ifPresentOrElse(value -> { assert value == getSomeField()}, 
                    () -> { throw new Exception("actual value not equals to expected")})
        }
'''
    }
}
