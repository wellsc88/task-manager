package com.well.tech.task.manager.common.exceptions;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final int status;
    protected BaseException(String message, int status) {
        super(message);
        this.status = status;
    }
}