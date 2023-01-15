package one.stoorx.listenerAgent.loadingTool.agentLoaders;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import one.stoorx.listenerAgent.loadingTool.exceptions.AgentLoadFailedException;
import one.stoorx.listenerAgent.loadingTool.parameters.AgentLoadParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ClasspathAgentLoader implements AgentLoader {
    @Getter
    private URI uri;
    @NonNull
    private Class<?> agentClass;

    private File createFakeJar() throws IOException {
        File agentFakeJar = Files.createTempFile(null, ".jar").toFile();
        var zipOutputStream = new ZipOutputStream(new FileOutputStream(agentFakeJar));

        putToJar(zipOutputStream, new ZipEntry(
                        agentClass.getName().replace('.', '/') + ".class"),
                Files.readAllBytes(Path.of(getUri())));

        putToJar(zipOutputStream, new ZipEntry("META-INF/MANIFEST.MF"),
                prepareManifest(
                        Map.of("Manifest-Version", "1.0",
                                "Agent-Class", agentClass.getCanonicalName())
                ).getBytes(StandardCharsets.UTF_8)
        );
        zipOutputStream.close();
        return agentFakeJar;
    }

    @Override
    public void loadWith(AgentLoadParameters agentLoadParameters) {
        try {
            agentLoadParameters.virtualMachine()
                    .loadAgent(createFakeJar().getAbsolutePath(), agentLoadParameters.options());
        } catch (IOException | AgentLoadException | AgentInitializationException e) {
            throw new AgentLoadFailedException(this, e);
        }
    }

    private String prepareManifest(Map<String, String> manifestData) {
        var sb = new StringBuilder();
        var lineSeparator = System.lineSeparator();
        manifestData.forEach((k, v) -> sb.append(k).append(": ").append(v).append(lineSeparator));
        sb.append(lineSeparator);
        return sb.toString();
    }

    private void putToJar(ZipOutputStream zipStream, ZipEntry entry, byte[] data) throws IOException {
        zipStream.putNextEntry(entry);
        zipStream.write(data);
        zipStream.closeEntry();
    }
}
