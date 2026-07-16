package com.well.tech.task.manager.common.exceptions.validation;

import com.well.tech.task.manager.common.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidParameterException extends BaseException {

    public InvalidParameterException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}