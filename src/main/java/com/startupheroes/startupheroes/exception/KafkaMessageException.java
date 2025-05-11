package com.startupheroes.startupheroes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class KafkaMessageException extends RuntimeException {
    public KafkaMessageException(String message) {
        super(message);
    }

    public KafkaMessageException(String message, Throwable cause) {
        super(message, cause);
    }
} 