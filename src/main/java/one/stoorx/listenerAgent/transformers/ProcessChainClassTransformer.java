package one.stoorx.listenerAgent.transformers;

import lombok.Getter;
import lombok.SneakyThrows;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessChainClassTransformer implements ClassFileTransformer {
    @Getter
    private final Map<String, ClassTransformationSpec> classTransformationSpecMap;

    public ProcessChainClassTransformer(ClassTransformationSpec... transformationSpecs) {
        classTransformationSpecMap = Arrays.stream(transformationSpecs)
                .collect(Collectors.toMap(ClassTransformationSpec::getClassName,
                        classTransformationSpec -> classTransformationSpec));
    }

    @SneakyThrows
    public List<Class<?>> getTransformableClasses() {
        List<Class<?>> list = new ArrayList<>();
        for (String s : classTransformationSpecMap.keySet()) {
            Class<?> aClass = Class.forName(s);
            list.add(aClass);
        }
        return list;
    }

    @SneakyThrows
    @Override
    public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        var transformSpec = classTransformationSpecMap.get(className.replace('/', '.'));
        if (transformSpec != null) {
            var classReader = new ClassReader(classfileBuffer);
            var classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            var classTransformer = transformSpec.createClassVisitor(classWriter);
            classReader.accept(classTransformer, ClassReader.EXPAND_FRAMES);
            return classWriter.toByteArray();
        } else return classfileBuffer;
    }
}
