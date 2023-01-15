package one.stoorx.listenerAgent.loadingTool.parameters;

import com.sun.tools.attach.VirtualMachine;
import lombok.NonNull;

public record AgentLoadParameters(@NonNull VirtualMachine virtualMachine, String options) {
    public AgentLoadParameters(@NonNull VirtualMachine virtualMachine) {
        this(virtualMachine, null);
    }
}
