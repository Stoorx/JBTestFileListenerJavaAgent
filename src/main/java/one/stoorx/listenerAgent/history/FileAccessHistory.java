package one.stoorx.listenerAgent.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum FileAccessHistory {
    INSTANCE;

    private final List<FileAccessRecord> history = new ArrayList<>();

    public synchronized void clear() {
        history.clear();
    }

    public synchronized List<FileAccessRecord> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public synchronized void registerAccess(FileAccessRecord fileAccessRecord) {
        history.add(fileAccessRecord);
    }
}

