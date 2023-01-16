package one.stoorx.listenerAgent.history;

import java.time.Instant;
import java.util.Objects;

public record FileAccessRecord(String name, Instant instant, Thread thread) implements Comparable<FileAccessRecord> {
    public static FileAccessRecord of(String name) {
        return new FileAccessRecord(name, Instant.now(), Thread.currentThread());
    }

    @Override
    public int compareTo(FileAccessRecord o) {
        return Objects.compare(instant, o.instant, Instant::compareTo);
    }
}
