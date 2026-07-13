package com.well.tech.task.manager.common.exceptions.auth;


import com.well.tech.task.manager.common.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {

    public EmailAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }
}
