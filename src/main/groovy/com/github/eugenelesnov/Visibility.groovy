package com.github.eugenelesnov

import groovyjarjarasm.asm.Opcodes

enum Visibility {
    PUBLIC, PROTECTED, PRIVATE

    static Integer parseOpCode(String visibility) {
        switch (visibility) {
            case PUBLIC.name(): return Opcodes.ACC_PUBLIC
            case PROTECTED.name(): return Opcodes.ACC_PROTECTED
            case PRIVATE.name(): return Opcodes.ACC_PRIVATE
            default: null
        }
    }
}