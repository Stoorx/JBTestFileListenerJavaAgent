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

import java.nio.file.Path;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ASM9;

public class WindowsFileSystemProviderTransformationSpec implements ClassTransformationSpec {
    private static final String NEW_BYTE_CHANNEL_NAME = "newByteChannel";
    private static final String NEW_BYTE_CHANNEL_DESCRIPTOR = "(Ljava/nio/file/Path;Ljava/util/Set;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/channels/SeekableByteChannel;";

    public static void registerHistory(final Path path) {
        if (path != null)
            FileAccessHistory.INSTANCE.registerAccess(FileAccessRecord.of(path.toFile().getAbsolutePath()));
    }

    @SneakyThrows
    @Override
    public ClassVisitor createClassVisitor(ClassVisitor nextClassVisitor) {
        return new ClassVisitor(ASM9, nextClassVisitor) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if (name.equals(NEW_BYTE_CHANNEL_NAME) && descriptor.equals(NEW_BYTE_CHANNEL_DESCRIPTOR)) {
                    return new AdviceAdapter(ASM9, super.visitMethod(access, name, descriptor, signature, exceptions),
                            ACC_PUBLIC, NEW_BYTE_CHANNEL_NAME, NEW_BYTE_CHANNEL_DESCRIPTOR) {
                        @SneakyThrows
                        @Override
                        protected void onMethodExit(int opcode) {
                            if (opcode == ARETURN) {
                                loadArg(0);
                                invokeStatic(Type.getType(WindowsFileSystemProviderTransformationSpec.class),
                                        Method.getMethod(WindowsFileSystemProviderTransformationSpec.class
                                                .getDeclaredMethod("registerHistory", Path.class)));
                            }
                        }
                    };
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        };
    }

    @Override
    public String getClassName() {
        return "sun.nio.fs.WindowsFileSystemProvider";
    }
}
