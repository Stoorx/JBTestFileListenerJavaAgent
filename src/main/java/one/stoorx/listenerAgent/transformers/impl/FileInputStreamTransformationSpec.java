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

public class FileInputStreamTransformationSpec implements ClassTransformationSpec {
    public static final String FILE_INPUT_STREAM_CLASS_NAME = "java.io.FileInputStream";
    public static final String FILE_INPUT_STREAM_OPEN_METHOD_NAME = "open";
    public static final String FILE_INPUT_STREAM_OPEN_METHOD_DESCRIPTOR = "(Ljava/lang/String;)V";

    public static void registerHistory(final String name) {
        if (name != null)
            FileAccessHistory.INSTANCE.registerAccess(FileAccessRecord.of(new File(name).getAbsolutePath()));
    }

    @Override
    public ClassVisitor createClassVisitor(ClassVisitor nextClassVisitor) {
        return new ClassVisitor(ASM9, nextClassVisitor) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                var defaultMethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (name.equals(FILE_INPUT_STREAM_OPEN_METHOD_NAME) &&
                        descriptor.equals(FILE_INPUT_STREAM_OPEN_METHOD_DESCRIPTOR)) {
                    return new AdviceAdapter(ASM9, defaultMethodVisitor, access, name, descriptor) {
                        @SneakyThrows
                        @Override
                        protected void onMethodExit(int opcode) {
                            loadArg(0);
                            invokeStatic(Type.getType(FileInputStreamTransformationSpec.class),
                                    Method.getMethod(FileInputStreamTransformationSpec.class.getDeclaredMethod(
                                            "registerHistory", String.class)));
                        }
                    };
                } else return defaultMethodVisitor;
            }
        };
    }

    @Override
    public String getClassName() {
        return FILE_INPUT_STREAM_CLASS_NAME;
    }
}
