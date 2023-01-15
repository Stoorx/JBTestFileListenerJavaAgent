package one.stoorx.listenerAgent.loadingTool.parameters;

import lombok.NonNull;
import one.stoorx.listenerAgent.loadingTool.Util;
import one.stoorx.listenerAgent.loadingTool.exceptions.MissingParameterException;

public record UtilityParameters(@NonNull String vmIdentifier, String options) {
    public UtilityParameters(@NonNull String vmIdentifier) {
        this(vmIdentifier, null);
    }

    public static UtilityParameters ofCommandLine(String[] args) {
        String vmId = Util.getOrElse(args, 0, () -> {
            throw new MissingParameterException("vmId");
        });
        String options = Util.getOrNull(args, 1);
        return new UtilityParameters(vmId, options);
    }
}

