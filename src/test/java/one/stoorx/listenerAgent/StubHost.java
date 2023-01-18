package one.stoorx.listenerAgent;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StubHost {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws IOException {
        Files.readString(Paths.get("a.txt"));
        Files.newInputStream(Paths.get("a.txt"));
        Files.newByteChannel(Paths.get("a.txt"));
        new FileReader("a.txt").read();
        new RandomAccessFile("a.txt", "r").read();
    }
}
