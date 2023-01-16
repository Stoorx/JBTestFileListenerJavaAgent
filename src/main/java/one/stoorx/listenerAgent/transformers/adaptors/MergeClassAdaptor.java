package one.stoorx.listenerAgent.transformers.adaptors;

import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class MergeClassAdaptor extends ClassVisitor {
    private boolean hasVisited = false;

    public MergeClassAdaptor(ClassVisitor nextClassVisitor) {
        super(ASM9, nextClassVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (!hasVisited) {
            super.visit(version, access, name, signature, superName, interfaces);
            hasVisited = true;
        } else {
            throw new IllegalStateException(String.format("Repeated visit on class %s", name));
        }
    }
}
