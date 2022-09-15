package com.morak.back.core.exception;

import com.morak.back.core.support.Generated;
import java.util.List;
import lombok.Getter;

@Getter
@Generated
public class SchedulingException extends MorakException {

    private final List<ExternalException> exceptions;

    public SchedulingException(CustomErrorCode code, String logMessage, List<ExternalException> exceptions) {
        super(code, logMessage);
        this.exceptions = exceptions;
    }
}
