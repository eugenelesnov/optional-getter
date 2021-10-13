package com.github.eugenelesnov

import org.junit.jupiter.api.Test

import static groovy.test.GroovyAssert.assertScript

class OptionalGetterTransformationTest {

    @Test
    void 'should generate optional getter with String as generic and with public modifier'() {
        assertScript '''
        import groovyjarjarasm.asm.Opcodes       
       
        import com.github.eugenelesnov.OptionalGetter
        import com.github.eugenelesnov.Visibility
       
        class Foo {
            @OptionalGetter(visibility = Visibility.PUBLIC)
            private String someField
            
            void setSomeField(String someField) {
                this.someField = someField
            }
        }
        
        Foo foo = new Foo()
        foo.setSomeField("value")

        def getter = foo.class.getDeclaredMethod("getSomeFieldOptional", new Class[] {})
        assert getter != null
        assert Arrays.asList(getter.modifiers).contains(Opcodes.ACC_PUBLIC)
        assert getter.returnType == Optional<String>
        assert foo.getProperty("someField") == foo.getSomeFieldOptional().orElse("error")
'''
    }

    @Test
    void 'should generate optional getter with Integer as generic and with protected modifier'() {
        assertScript '''
        import java.lang.reflect.ParameterizedType
        
        import groovyjarjarasm.asm.Opcodes       
       
        import com.github.eugenelesnov.OptionalGetter
        import com.github.eugenelesnov.Visibility
       
        class Foo {
            @OptionalGetter(visibility = Visibility.PROTECTED)
            private Integer someField
            
            void setSomeField(Integer someField) {
                this.someField = someField
            }
        }
        
        Foo foo = new Foo()
        foo.setSomeField(1)

        def getter = foo.class.getDeclaredMethod("getSomeFieldOptional", new Class[] {})
        assert getter != null
        assert Arrays.asList(getter.modifiers).contains(Opcodes.ACC_PROTECTED)
        assert getter.returnType == Optional<Integer>
        assert foo.getProperty("someField") == foo.getSomeFieldOptional().orElse(Integer.MAX_VALUE)
'''
    }
}
