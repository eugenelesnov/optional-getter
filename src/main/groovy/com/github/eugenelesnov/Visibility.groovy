package com.github.eugenelesnov

import groovyjarjarasm.asm.Opcodes

/**
 * Visibility variants for {@link OptionalGetter}
 *
 * @author Eugene Lesnov
 */
enum Visibility {
    PUBLIC(Opcodes.ACC_PUBLIC), PROTECTED(Opcodes.ACC_PROTECTED)

    private final int code

    Visibility(int code) {
        this.code = code
    }

    int getCode() {
        return code
    }
}