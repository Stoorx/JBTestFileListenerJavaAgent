package one.stoorx.listenerAgent.transformers.impl;

import lombok.SneakyThrows;
import one.stoorx.listenerAgent.history.FileAccessHistory;
import one.stoorx.listenerAgent.history.FileAccessRecord;
import one.stoorx.listenerAgent.transformers.ClassTransformationSpec;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

import java.io.File;

import static org.objectweb.asm.Opcodes.ASM9;

public class RandomAccessFileTransformationSpec implements ClassTransformationSpec {
    private static final String RANDOM_ACCESS_FILE_CLASS_NAME = "java.io.RandomAccessFile";
    private static final String RANDOM_ACCESS_FILE_OPEN_METHOD_NAME = "open";
    private static final String RANDOM_ACCESS_FILE_OPEN_METHOD_DESCRIPTOR = "(Ljava/lang/String;I)V";
    private static final int MODE_READONLY = 1;
    private static final int MODE_READ = 2;

    public static void registerHistory(final String name, final int mode) {
        if (name != null && ((mode & (MODE_READONLY | MODE_READ)) != 0))
            FileAccessHistory.INSTANCE.registerAccess(FileAccessRecord.of(new File(name).getAbsolutePath()));
    }

    @Override
    public ClassVisitor createClassVisitor(ClassVisitor nextClassVisitor) {
        return new ClassVisitor(ASM9, nextClassVisitor) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                var defaultMethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (name.equals(RANDOM_ACCESS_FILE_OPEN_METHOD_NAME) &&
                        descriptor.equals(RANDOM_ACCESS_FILE_OPEN_METHOD_DESCRIPTOR)) {
                    return new AdviceAdapter(ASM9, defaultMethodVisitor, access, name, descriptor) {
                        @SneakyThrows
                        @Override
                        protected void onMethodExit(int opcode) {
                            loadArg(0);
                            loadArg(1);
                            invokeStatic(Type.getType(RandomAccessFileTransformationSpec.class),
                                    Method.getMethod(RandomAccessFileTransformationSpec.class
                                            .getDeclaredMethod("registerHistory", String.class, int.class)));
                        }
                    };
                } else return defaultMethodVisitor;
            }
        };
    }

    @Override
    public String getClassName() {
        return RANDOM_ACCESS_FILE_CLASS_NAME;
    }
}
