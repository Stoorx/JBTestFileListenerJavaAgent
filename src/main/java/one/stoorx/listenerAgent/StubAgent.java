package one.stoorx.listenerAgent;

import java.lang.instrument.Instrumentation;

public class StubAgent {
    public static void agentmain(String options, Instrumentation instrumentation) {
        System.out.println("Hello from the Stub Java Agent!");
        AnotherClass.print();
    }

    public static void premain(String options, Instrumentation instrumentation) {
        agentmain(options, instrumentation);
    }

    static class AnotherClass {
        public static void print() {
            System.out.println("Hello from another class");
        }
    }
}
