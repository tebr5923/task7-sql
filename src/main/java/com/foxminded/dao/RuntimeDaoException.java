package com.foxminded.dao;

public class RuntimeDaoException extends RuntimeException{
    public RuntimeDaoException() {
        super();
    }

    public RuntimeDaoException(String message) {
        super(message);
    }

    public RuntimeDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
