package one.stoorx.listenerAgent;

public class StubAgent {
    public static void agentmain(String options) {
        System.out.println("Hello from the Stub Java Agent!");
    }

    public static void premain(String options) {
        agentmain(options);
    }
}
