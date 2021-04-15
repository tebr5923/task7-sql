package com.foxminded.dao;

public class RunTimeDaoException extends RuntimeException{
    public RunTimeDaoException() {
        super();
    }

    public RunTimeDaoException(String message) {
        super(message);
    }

    public RunTimeDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
