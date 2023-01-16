package one.stoorx.listenerAgent.transformers.adaptors;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.ASM9;

public class MatchingClassAdapterDecorator extends ClassVisitor {
    private final String className;
    private boolean matched = false;

    protected MatchingClassAdapterDecorator(String className, ClassVisitor decoratedClassVisitor) {
        super(ASM9, decoratedClassVisitor);
        this.className = className;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (name.equals(className)) {
            matched = true;
            super.visit(version, access, name, signature, superName, interfaces);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (matched) {
            return super.visitAnnotation(descriptor, visible);
        } else return null;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        if (matched) {
            super.visitAttribute(attribute);
        }
    }

    @Override
    public void visitEnd() {
        if (matched) {
            super.visitEnd();
        }
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (matched) {
            return super.visitField(access, name, descriptor, signature, value);
        } else return null;
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        if (matched) {
            super.visitInnerClass(name, outerName, innerName, access);
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (matched) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        } else return null;
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        if (matched) {
            super.visitOuterClass(owner, name, descriptor);
        }
    }

    @Override
    public void visitSource(String source, String debug) {
        if (matched) {
            super.visitSource(source, debug);
        }
    }
}

