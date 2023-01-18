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

import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;

import static java.nio.file.StandardOpenOption.READ;
import static org.objectweb.asm.Opcodes.ASM9;


public class FilesTransformationSpec implements ClassTransformationSpec {
    private static final String FILES_CLASS_NAME = "java.nio.file.Files";
    private static final String FILES_NEW_BYTE_CHANNEL_METHOD_NAME = "newByteChannel";
    private static final String FILES_NEW_BYTE_CHANNEL_METHOD_DESCRIPTOR = "(Ljava/nio/file/Path;Ljava/util/Set;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/channels/SeekableByteChannel;";

    public static void registerHistory(final Path path, final Set<? extends OpenOption> options) {
        if (path != null && (options.isEmpty() || options.contains(READ)))
            FileAccessHistory.INSTANCE.registerAccess(FileAccessRecord.of(path.toFile().getAbsolutePath()));
    }

    @Override
    public ClassVisitor createClassVisitor(ClassVisitor nextClassVisitor) {
        return new ClassVisitor(ASM9, nextClassVisitor) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                var defaultMethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (name.equals(FILES_NEW_BYTE_CHANNEL_METHOD_NAME) &&
                        descriptor.equals(FILES_NEW_BYTE_CHANNEL_METHOD_DESCRIPTOR)) {
                    return new AdviceAdapter(ASM9, defaultMethodVisitor, access, name, descriptor) {
                        @SneakyThrows
                        @Override
                        protected void onMethodExit(int opcode) {
                            loadArg(0);
                            loadArg(1);
                            invokeStatic(Type.getType(FilesTransformationSpec.class),
                                    Method.getMethod(FilesTransformationSpec.class
                                            .getDeclaredMethod("registerHistory", Path.class, Set.class)));
                        }
                    };
                }
                return defaultMethodVisitor;
            }
        };
    }

    @Override
    public String getClassName() {
        return FILES_CLASS_NAME;
    }
}
