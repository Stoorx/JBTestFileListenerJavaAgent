package one.stoorx.listenerAgent.loadingTool.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public
class MissingParameterException extends IllegalArgumentException {
    @Getter
    private String parameterName;

    public MissingParameterException(String parameterName) {
        super(String.format("Required parameter <%s> is missing. See usage for help.", parameterName));
        this.parameterName = parameterName;
    }
}
