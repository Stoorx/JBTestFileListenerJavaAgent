package one.stoorx.listenerAgent;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import one.stoorx.listenerAgent.history.FileAccessHistory;
import one.stoorx.listenerAgent.transformers.ProcessChainClassTransformer;
import one.stoorx.listenerAgent.transformers.impl.FileInputStreamTransformationSpec;
import one.stoorx.listenerAgent.transformers.impl.FilesTransformationSpec;
import one.stoorx.listenerAgent.transformers.impl.RandomAccessFileTransformationSpec;

import java.lang.instrument.Instrumentation;

@AllArgsConstructor
public class ListenerAgent {
    @NonNull Instrumentation instrumentation;

    public static void agentmain(String options, Instrumentation instrumentation) {
        var agent = new ListenerAgent(instrumentation);
        agent.installShutdownHook();
        agent.installTransformers();
    }

    public static void premain(String options, Instrumentation instrumentation) {
        agentmain(options, instrumentation);
    }

    private void installShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread("ListenerAgent Shutdown Hook") {
            @Override
            public void run() {
                var history = FileAccessHistory.INSTANCE.getHistory();
                if (history.size() > 0) {
                    history.forEach(System.out::println);
                } else {
                    System.out.println("No files were read");
                }

            }
        });
    }

    @SneakyThrows
    private void installTransformers() {
        var transformer = new ProcessChainClassTransformer(
                new FileInputStreamTransformationSpec(),
                new RandomAccessFileTransformationSpec(),
                new FilesTransformationSpec()
        );

        instrumentation.addTransformer(transformer, true);
        instrumentation.retransformClasses(transformer.getTransformableClasses().toArray(Class[]::new));
    }
}
