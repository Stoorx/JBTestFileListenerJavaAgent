package one.stoorx.listenerAgent.loadingTool.agentLoaders;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import one.stoorx.listenerAgent.loadingTool.exceptions.AgentLoadFailedException;
import one.stoorx.listenerAgent.loadingTool.parameters.AgentLoadParameters;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class JarAgentLoader implements AgentLoader {
    @Getter
    private URI uri;

    @Override
    public void loadWith(AgentLoadParameters agentLoadParameters) {
        try {
            var jarPath = Path.of(((JarURLConnection) uri.toURL().openConnection())
                    .getJarFileURL().toURI()).toAbsolutePath().toString();
            agentLoadParameters.virtualMachine()
                    .loadAgent(jarPath, agentLoadParameters.options());
        } catch (AgentLoadException | AgentInitializationException | IOException | URISyntaxException e) {
            throw new AgentLoadFailedException(this, e);
        }
    }
}

