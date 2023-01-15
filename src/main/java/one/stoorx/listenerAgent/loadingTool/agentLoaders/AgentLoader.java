package one.stoorx.listenerAgent.loadingTool.agentLoaders;

import lombok.SneakyThrows;
import one.stoorx.listenerAgent.loadingTool.exceptions.AgentNotFoundException;
import one.stoorx.listenerAgent.loadingTool.parameters.AgentLoadParameters;

import java.net.URI;
import java.util.Objects;

public interface AgentLoader {
    @SneakyThrows
    static AgentLoader ofClass(Class<?> agentClass) {
        var agentClassUri = Objects.requireNonNull(
                agentClass.getResource('/' + agentClass.getName().replace('.', '/') + ".class"),
                () -> {
                    throw new AgentNotFoundException();
                }
        ).toURI();

        return switch (agentClassUri.getScheme()) {
            case "jar" -> new JarAgentLoader(agentClassUri);
            case "file" -> new ClasspathAgentLoader(agentClassUri, agentClass);
            default -> throw new IllegalArgumentException();
        };
    }

    URI getUri();

    void loadWith(AgentLoadParameters agentLoadParameters);
}


