package one.stoorx.listenerAgent.history;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public enum FileAccessHistory {
    INSTANCE;

    private final Queue<FileAccessRecord> history = new ConcurrentLinkedQueue<>();

    public void clear() {
        history.clear();
    }

    public List<FileAccessRecord> getHistory() {
        return history.stream().toList();
    }

    public void registerAccess(FileAccessRecord fileAccessRecord) {
        history.add(fileAccessRecord);
    }
}

