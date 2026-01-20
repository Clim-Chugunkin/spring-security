package com.kiselev.sergey.security.security;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.naming.AuthenticationException;

@Getter
public class JWTAuthException extends AuthenticationException {
    HttpStatus httpStatus;

    public JWTAuthException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
