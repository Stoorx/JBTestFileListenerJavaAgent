package one.stoorx.listenerAgent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StubHost {
    public static void main(String[] args) throws IOException {
        Files.readString(Paths.get("a.txt"));
    }
}
