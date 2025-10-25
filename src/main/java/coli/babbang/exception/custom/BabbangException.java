package coli.babbang.exception.custom;

import coli.babbang.exception.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BabbangException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BabbangException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getStatus();
    }
}
