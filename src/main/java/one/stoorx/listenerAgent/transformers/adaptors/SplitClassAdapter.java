package one.stoorx.listenerAgent.transformers.adaptors;

import org.objectweb.asm.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.objectweb.asm.Opcodes.ASM9;

public class SplitClassAdapter extends ClassVisitor {
    private final List<ClassVisitor> nextClassVisitors;

    public SplitClassAdapter(ClassVisitor... nextClassVisitors) {
        super(ASM9);
        this.nextClassVisitors = Arrays.asList(nextClassVisitors);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        for (var currentClassVisitor : nextClassVisitors)
            currentClassVisitor.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new AnnotationVisitor(ASM9) {
            final List<AnnotationVisitor> nextAnnotationVisitors = nextClassVisitors.stream()
                    .map(classVisitor -> classVisitor.visitAnnotation(descriptor, visible))
                    .filter(Objects::nonNull).toList();

            @Override
            public void visit(String name, Object value) {
                nextAnnotationVisitors.forEach(annotationVisitor -> annotationVisitor.visit(name, value));
            }

            @Override
            public AnnotationVisitor visitAnnotation(String name, String descriptor) {
                throw new UnsupportedOperationException("Nested annotations is not supported");
                //return super.visitAnnotation(name, descriptor);
            }

            @Override
            public AnnotationVisitor visitArray(String name) {
                throw new UnsupportedOperationException("Array annotations is not supported");
                //return super.visitArray(name);
            }

            @Override
            public void visitEnd() {
                nextAnnotationVisitors.forEach(AnnotationVisitor::visitEnd);
            }

            @Override
            public void visitEnum(String name, String descriptor, String value) {
                nextAnnotationVisitors.forEach(annotationVisitor -> annotationVisitor.visitEnum(name, descriptor, value));
            }
        };
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        nextClassVisitors.forEach(currentClassVisitor -> currentClassVisitor.visitAttribute(attribute));
    }

    @Override
    public void visitEnd() {
        nextClassVisitors.forEach(ClassVisitor::visitEnd);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return new FieldVisitor(ASM9) {
            final List<FieldVisitor> nextFieldVisitors = nextClassVisitors.stream()
                    .map(classVisitor -> classVisitor.visitField(access, name, descriptor, signature, value))
                    .filter(Objects::nonNull).toList();

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                throw new UnsupportedOperationException();
//                return super.visitAnnotation(descriptor, visible);
            }

            @Override
            public void visitAttribute(Attribute attribute) {
                nextFieldVisitors.forEach(fieldVisitor -> fieldVisitor.visitAttribute(attribute));
            }

            @Override
            public void visitEnd() {
                nextFieldVisitors.forEach(FieldVisitor::visitEnd);
            }

            @Override
            public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                throw new UnsupportedOperationException();
//                return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
            }
        };
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        nextClassVisitors.forEach(currentClassVisitor -> currentClassVisitor.visitInnerClass(name, outerName, innerName, access));
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new MethodVisitor(ASM9) {
            final List<MethodVisitor> nextMethodVisitors = nextClassVisitors.stream()
                    .map(classVisitor -> classVisitor.visitMethod(access, name, descriptor, signature, exceptions))
                    .filter(Objects::nonNull).toList();

            @Override
            public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitAnnotableParameterCount(parameterCount, visible));
            }

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                throw new UnsupportedOperationException();
                //return super.visitAnnotation(descriptor, visible);
            }

            @Override
            public AnnotationVisitor visitAnnotationDefault() {
                throw new UnsupportedOperationException();
                //return super.visitAnnotationDefault();
            }

            @Override
            public void visitAttribute(Attribute attribute) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitAttribute(attribute));
            }

            @Override
            public void visitCode() {
                nextMethodVisitors.forEach(MethodVisitor::visitCode);
            }

            @Override
            public void visitEnd() {
                nextMethodVisitors.forEach(MethodVisitor::visitEnd);
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitFieldInsn(opcode, owner, name, descriptor));
            }

            @Override
            public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitFrame(type, numLocal, local, numStack, stack));
            }

            @Override
            public void visitIincInsn(int varIndex, int increment) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitIincInsn(varIndex, increment));
            }

            @Override
            public void visitInsn(int opcode) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitInsn(opcode));
            }

            @Override
            public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                throw new UnsupportedOperationException();
                //return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
            }

            @Override
            public void visitIntInsn(int opcode, int operand) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitIntInsn(opcode, operand));
            }

            @Override
            public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
            }

            @Override
            public void visitJumpInsn(int opcode, Label label) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitJumpInsn(opcode, label));
            }

            @Override
            public void visitLabel(Label label) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitLabel(label));
            }

            @Override
            public void visitLdcInsn(Object value) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitLdcInsn(value));
            }

            @Override
            public void visitLineNumber(int line, Label start) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitLineNumber(line, start));
            }

            @Override
            public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitLocalVariable(name, descriptor, signature, start, end, index));
            }

            @Override
            public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
                throw new UnsupportedOperationException();
                //return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
            }

            @Override
            public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitLookupSwitchInsn(dflt, keys, labels));
            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitMaxs(maxStack, maxLocals));
            }

            @Override
            @Deprecated
            @SuppressWarnings("deprecation")
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitMethodInsn(opcode, owner, name, descriptor));
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitMethodInsn(opcode, owner, name, descriptor, isInterface));
            }

            @Override
            public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitMultiANewArrayInsn(descriptor, numDimensions));
            }

            @Override
            public void visitParameter(String name, int access) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitParameter(name, access));
            }

            @Override
            public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
                throw new UnsupportedOperationException();
                //return super.visitParameterAnnotation(parameter, descriptor, visible);
            }

            @Override
            public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitTableSwitchInsn(min, max, dflt, labels));
            }

            @Override
            public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                throw new UnsupportedOperationException();
                //return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
            }

            @Override
            public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitTryCatchBlock(start, end, handler, type));
            }

            @Override
            public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                throw new UnsupportedOperationException();
                //return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
            }

            @Override
            public void visitTypeInsn(int opcode, String type) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitTypeInsn(opcode, type));
            }

            @Override
            public void visitVarInsn(int opcode, int varIndex) {
                nextMethodVisitors.forEach(methodVisitor -> methodVisitor.visitVarInsn(opcode, varIndex));
            }
        };
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        return new ModuleVisitor(ASM9) {
            final List<ModuleVisitor> nextModuleVisitors = nextClassVisitors.stream()
                    .map(classVisitor -> classVisitor.visitModule(name, access, version))
                    .filter(Objects::nonNull).toList();

            @Override
            public void visitEnd() {
                nextModuleVisitors.forEach(ModuleVisitor::visitEnd);
            }

            @Override
            public void visitExport(String packaze, int access, String... modules) {
                nextModuleVisitors.forEach(moduleVisitor -> moduleVisitor.visitExport(packaze, access, modules));
            }

            @Override
            public void visitMainClass(String mainClass) {
                nextModuleVisitors.forEach(moduleVisitor -> moduleVisitor.visitMainClass(mainClass));
            }

            @Override
            public void visitOpen(String packaze, int access, String... modules) {
                nextModuleVisitors.forEach(moduleVisitor -> moduleVisitor.visitOpen(packaze, access, modules));
            }

            @Override
            public void visitPackage(String packaze) {
                nextModuleVisitors.forEach(moduleVisitor -> moduleVisitor.visitPackage(packaze));
            }

            @Override
            public void visitProvide(String service, String... providers) {
                nextModuleVisitors.forEach(moduleVisitor -> moduleVisitor.visitProvide(service, providers));
            }

            @Override
            public void visitRequire(String module, int access, String version) {
                nextModuleVisitors.forEach(moduleVisitor -> moduleVisitor.visitRequire(module, access, version));
            }

            @Override
            public void visitUse(String service) {
                nextModuleVisitors.forEach(moduleVisitor -> moduleVisitor.visitUse(service));
            }
        };
    }

    @Override
    public void visitNestHost(String nestHost) {
        nextClassVisitors.forEach(currentClassVisitor -> currentClassVisitor.visitNestHost(nestHost));
    }

    @Override
    public void visitNestMember(String nestMember) {
        nextClassVisitors.forEach(currentClassVisitor -> currentClassVisitor.visitNestMember(nestMember));
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        nextClassVisitors.forEach(currentClassVisitor -> currentClassVisitor.visitOuterClass(owner, name, descriptor));
    }

    @Override
    public void visitPermittedSubclass(String permittedSubclass) {
        nextClassVisitors.forEach(currentClassVisitor -> currentClassVisitor.visitPermittedSubclass(permittedSubclass));
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        return new RecordComponentVisitor(ASM9) {
            final List<RecordComponentVisitor> nextRecordComponentVisitors = nextClassVisitors.stream()
                    .map(classVisitor -> classVisitor.visitRecordComponent(name, descriptor, signature))
                    .filter(Objects::nonNull).toList();

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                throw new UnsupportedOperationException();
                //return super.visitAnnotation(descriptor, visible);
            }

            @Override
            public void visitAttribute(Attribute attribute) {
                nextRecordComponentVisitors.forEach(recordComponentVisitor -> recordComponentVisitor.visitAttribute(attribute));
            }

            @Override
            public void visitEnd() {
                nextRecordComponentVisitors.forEach(RecordComponentVisitor::visitEnd);
                super.visitEnd();
            }

            @Override
            public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
                throw new UnsupportedOperationException();
                //return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
            }
        };
    }

    @Override
    public void visitSource(String source, String debug) {
        nextClassVisitors.forEach(currentClassVisitor -> currentClassVisitor.visitSource(source, debug));
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return new AnnotationVisitor(ASM9) {
            final List<AnnotationVisitor> nextTypeAnnotationVisitors = nextClassVisitors.stream()
                    .map(classVisitor -> classVisitor.visitTypeAnnotation(typeRef, typePath, descriptor, visible))
                    .filter(Objects::nonNull).toList();

            @Override
            public void visit(String name, Object value) {
                nextTypeAnnotationVisitors.forEach(annotationVisitor -> annotationVisitor.visit(name, value));
            }

            @Override
            public AnnotationVisitor visitAnnotation(String name, String descriptor) {
                throw new UnsupportedOperationException("Nested annotations is not supported");
                //return super.visitAnnotation(name, descriptor);
            }

            @Override
            public AnnotationVisitor visitArray(String name) {
                throw new UnsupportedOperationException("Array annotations is not supported");
                //return super.visitArray(name);
            }

            @Override
            public void visitEnd() {
                nextTypeAnnotationVisitors.forEach(AnnotationVisitor::visitEnd);
            }

            @Override
            public void visitEnum(String name, String descriptor, String value) {
                nextTypeAnnotationVisitors.forEach(annotationVisitor -> annotationVisitor.visitEnum(name, descriptor, value));
            }
        };
    }
}

