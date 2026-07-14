package com.well.tech.task.manager.common.exceptions.auth;

import com.well.tech.task.manager.common.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class UserDisabledException extends BaseException {
    public UserDisabledException(String message) {
        super(message, HttpStatus.FORBIDDEN.value());
    }
}
