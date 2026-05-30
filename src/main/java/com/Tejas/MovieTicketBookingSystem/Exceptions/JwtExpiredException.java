package com.Tejas.MovieTicketBookingSystem.Exceptions;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException(String message){
        super(message);
    }
}
