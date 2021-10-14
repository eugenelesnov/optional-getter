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

    @Test
    void 'should generate optional getter with Boolean as generic and with public modifier by default'() {
        assertScript '''
        import java.lang.reflect.ParameterizedType
        
        import groovyjarjarasm.asm.Opcodes       
       
        import com.github.eugenelesnov.OptionalGetter
        import com.github.eugenelesnov.Visibility
       
        class Foo {
            @OptionalGetter
            private Boolean someField
            
            void setSomeField(Boolean someField) {
                this.someField = someField
            }
        }
        
        Foo foo = new Foo()
        foo.setSomeField(true)

        def getter = foo.class.getDeclaredMethod("getSomeFieldOptional", new Class[] {})
        assert getter != null
        assert Arrays.asList(getter.modifiers).contains(Opcodes.ACC_PUBLIC)
        assert getter.returnType == Optional<Boolean>
        assert foo.getProperty("someField") == foo.getSomeFieldOptional().orElse(false)
'''
    }

    @Test
    void 'should not generate optional getter if duplicated method with same signature exists'() {
        assertScript '''
        
        import org.codehaus.groovy.ast.Parameter

        import java.lang.reflect.ParameterizedType
        
        import groovyjarjarasm.asm.Opcodes       
       
        import com.github.eugenelesnov.OptionalGetter
        import com.github.eugenelesnov.Visibility

        import java.util.stream.Collectors
       
        class Foo {
            @OptionalGetter
            private Boolean someField
            
            void setSomeField(Boolean someField) {
                this.someField = someField
            }
            
            Optional<Boolean> getSomeFieldOptional() {
                return Optional.ofNullable(someField)
            }
        }
        
        Foo foo = new Foo()
        foo.setSomeField(true)

        def methods = Arrays.asList(foo.class.getDeclaredMethods()).stream()
                                             .filter(m -> m.name == "getSomeFieldOptional")
                                             .collect(Collectors.toList())
        assert methods.size() == 1
        def method = methods.get(0)
        assert method.returnType == Optional<Boolean>
        assert method.parameters == new java.lang.reflect.Parameter[] {}
'''
    }
}
