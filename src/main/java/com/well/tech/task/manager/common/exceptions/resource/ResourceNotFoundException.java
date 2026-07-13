package com.well.tech.task.manager.common.exceptions.resource;

import com.well.tech.task.manager.common.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
