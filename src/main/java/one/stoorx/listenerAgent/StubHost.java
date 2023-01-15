package one.stoorx.listenerAgent;

import java.io.IOException;

public class StubHost {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        System.out.printf("Hello from stub host with VmId <%d>%n", ProcessHandle.current().pid());
        System.in.read(); // Just to stop current thread
    }
}
