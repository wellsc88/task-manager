package com.well.tech.task.manager.common.exceptions.auth;

import com.well.tech.task.manager.common.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
