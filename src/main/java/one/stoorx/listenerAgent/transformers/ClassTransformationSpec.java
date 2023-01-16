package one.stoorx.listenerAgent.transformers;

import org.objectweb.asm.ClassVisitor;

public interface ClassTransformationSpec {
    ClassVisitor createClassVisitor(ClassVisitor nextClassVisitor);

    String getClassName();
}
