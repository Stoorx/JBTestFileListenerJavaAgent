package one.stoorx.listenerAgent.loadingTool.exceptions;

import lombok.Getter;
import lombok.NonNull;
import one.stoorx.listenerAgent.loadingTool.agentLoaders.AgentLoader;

public class AgentLoadFailedException extends RuntimeException {
    @Getter
    @NonNull
    private final AgentLoader agentLoader;

    public AgentLoadFailedException(@NonNull AgentLoader agentLoader) {
        this(agentLoader, null);
    }

    public AgentLoadFailedException(@NonNull AgentLoader agentLoader, Throwable cause) {
        super(String.format("Agent load from <%s> is failed.", agentLoader.getUri().getPath()), cause);
        this.agentLoader = agentLoader;
    }
}
