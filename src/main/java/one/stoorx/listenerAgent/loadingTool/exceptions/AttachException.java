package one.stoorx.listenerAgent.loadingTool.exceptions;


import lombok.Getter;
import lombok.NonNull;


public class AttachException extends RuntimeException {
    @Getter
    @NonNull
    private final String vmIdentifier;

    public AttachException(@NonNull String vmIdentifier) {
        this(vmIdentifier, null);
    }

    public AttachException(@NonNull String vmIdentifier, Throwable cause) {
        super(String.format("Attachment to target VM with ID <%s> is failed.", vmIdentifier), cause);
        this.vmIdentifier = vmIdentifier;
    }
}

