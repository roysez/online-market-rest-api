package com.market.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidAdStateTransitionException extends RuntimeException {

    public InvalidAdStateTransitionException(String message) {
        super(message);
    }

}
