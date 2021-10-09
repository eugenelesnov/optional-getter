package com.github.eugenelesnov

import groovyjarjarasm.asm.Opcodes

enum Visibility {
    PUBLIC, PROTECTED, PRIVATE

    static int parseOpCode(Visibility visibility) {
        switch (visibility) {
            case PUBLIC: return Opcodes.ACC_PUBLIC
            case PROTECTED: return Opcodes.ACC_PROTECTED
            case PRIVATE: return Opcodes.ACC_PRIVATE
        }
    }
}