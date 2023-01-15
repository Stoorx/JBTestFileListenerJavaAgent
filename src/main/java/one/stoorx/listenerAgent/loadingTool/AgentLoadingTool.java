package one.stoorx.listenerAgent.loadingTool;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import lombok.SneakyThrows;
import one.stoorx.listenerAgent.StubAgent;
import one.stoorx.listenerAgent.loadingTool.agentLoaders.AgentLoader;
import one.stoorx.listenerAgent.loadingTool.exceptions.AttachException;
import one.stoorx.listenerAgent.loadingTool.parameters.AgentLoadParameters;
import one.stoorx.listenerAgent.loadingTool.parameters.UtilityParameters;

import java.io.IOException;

public class AgentLoadingTool {
    private final UtilityParameters utilityParameters;

    AgentLoadingTool(String[] args) {
        utilityParameters = UtilityParameters.ofCommandLine(args);
    }

    public static void main(String[] args) {
        try {
            new AgentLoadingTool(args).run();
        } catch (Throwable t) {
            System.err.println(t.getMessage());
            System.exit(-1);
        }
    }

    private VirtualMachine attachToVm() {
        try {
            return VirtualMachine.attach(utilityParameters.vmIdentifier());
        } catch (IOException | AttachNotSupportedException e) {
            throw new AttachException(utilityParameters.vmIdentifier());
        }
    }

    @SneakyThrows
    private void run() {
        var vm = attachToVm();
        try {
            AgentLoader.ofClass(StubAgent.class).loadWith(new AgentLoadParameters(vm, utilityParameters.options()));
        } finally {
            vm.detach();
        }
    }
}

