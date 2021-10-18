![Build status](https://github.com/EugeneLesnov/optional-getter/actions/workflows/actions.yml/badge.svg)
[![](https://jitpack.io/v/EugeneLesnov/optional-getter.svg)](https://jitpack.io/#EugeneLesnov/optional-getter)
[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-green.svg)](https://conventionalcommits.org)

# optional-getter

Generating getter method with Optional as a return type by annotation using Groovy and AST transformations.

## What is it?

Let's assume you have class like this:

```groovy
class Foo {

    private String someField
}
```

And you want to have method `Optional<String> getSomeFieldOptional()`

Sounds like a boilerplate, huh?

Well, put `@OptionalGetter` on field and enjoy the magic of AST transformations :)

```groovy
class Foo {
    @OptionalGetter
    private String someField
}
```

As of result of AST transformation you will have .class similar to:

```java
public class Foo implements GroovyObject {
    private String someField;

    // generated constructor omitted 

    public Optional<String> getSomeFieldOptional() {
        CallSite[] var1 = $getCallSiteArray();
        return (Optional) ScriptBytecodeAdapter.castToType(var1[0].callStatic(Optional.class, this.someField), Optional.class);
    }
}
```

## How to use?

1. Add to your build.gradle:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add dependency:

```groovy
dependencies {
   implementation 'com.github.eugenelesnov:optional-getter:1.0'
}
```

## What's next?

Well, there are plans...

1) Fixing possible bugs
2) IDE support. Of course, Intelij IDEA is a priority ;)
   For now, it works for Groovy only. There is no way (**yet**!) to use this library with Java because IDE won't let you
   use the method generated at the compile-time..